package com.puxinxiaolin.adopt.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.puxinxiaolin.adopt.constants.RedisConstant;
import com.puxinxiaolin.adopt.entity.dto.PetLikePageQueryDTO;
import com.puxinxiaolin.adopt.entity.entity.Pet;
import com.puxinxiaolin.adopt.entity.entity.PetLike;
import com.puxinxiaolin.adopt.entity.vo.PetVO;
import com.puxinxiaolin.adopt.mapper.PetLikeMapper;
import com.puxinxiaolin.adopt.mapper.PetMapper;
import com.puxinxiaolin.adopt.service.PetLikeService;
import com.puxinxiaolin.adopt.utils.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 宠物点赞服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PetLikeServiceImpl extends ServiceImpl<PetLikeMapper, PetLike> implements PetLikeService {

    private final PetMapper petMapper;
    private final RedisUtil redisUtil;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean likePet(Long petId) {
        Long userId = StpUtil.getLoginIdAsLong();
        
        // 检查是否已点赞
        if (isLiked(petId)) {
            log.warn("该宠物已点赞, 用户ID: {}, 宠物ID: {}", userId, petId);
            return true;
        }

        // 新增点赞记录
        PetLike petLike = new PetLike();
        petLike.setUserId(userId);
        petLike.setPetId(petId);
        
        this.save(petLike);
        petMapper.incrementLikeCount(petId);
        deleteLikeCountCache(petId);
        log.info("用户 {} 点赞宠物 {}", userId, petId);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean unlikePet(Long petId) {
        Long userId = StpUtil.getLoginIdAsLong();
        
        LambdaQueryWrapper<PetLike> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PetLike::getUserId, userId)
                .eq(PetLike::getPetId, petId);
        
        boolean removed = this.remove(wrapper);
        if (removed) {
            petMapper.decrementLikeCount(petId);
            deleteLikeCountCache(petId);
            log.info("用户 {} 取消点赞宠物 {}", userId, petId);
        }
        return removed;
    }
    
    private void deleteLikeCountCache(Long petId) {
        String cacheKey = RedisConstant.buildPetLikeCountKey(petId);
        redisUtil.delete(cacheKey);
    }
    
    @Override
    public boolean isLiked(Long petId) {
        Long userId = StpUtil.getLoginIdAsLong();
        LambdaQueryWrapper<PetLike> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PetLike::getUserId, userId)
                .eq(PetLike::getPetId, petId);
        return this.count(wrapper) > 0;
    }
    
    @Override
    public long getLikeCount(Long petId) {
        Pet pet = petMapper.selectById(petId);
        return pet != null ? pet.getLikeCount() : 0;
    }

    @Override
    public Page<PetVO> queryUserLikedPets(PetLikePageQueryDTO queryDTO) {
        Long userId = StpUtil.getLoginIdAsLong();
        
        LambdaQueryWrapper<PetLike> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PetLike::getUserId, userId)
                .orderByDesc(PetLike::getCreateTime);

        Page<PetLike> petLikePage = this.page(new Page<>(queryDTO.getCurrent(), queryDTO.getSize()), wrapper);
        
        Page<PetVO> result = new Page<>();
        result.setCurrent(petLikePage.getCurrent());
        result.setSize(petLikePage.getSize());
        result.setTotal(petLikePage.getTotal());
        result.setPages(petLikePage.getPages());
        
        List<PetVO> petVOList = petLikePage.getRecords().stream()
                .map(petLike -> {
                    Pet pet = petMapper.selectById(petLike.getPetId());
                    if (pet == null) {
                        return null;
                    }
                    PetVO vo = new PetVO();
                    vo.setId(pet.getId());
                    vo.setName(pet.getName());
                    vo.setBreed(pet.getBreed());
                    vo.setAge(pet.getAge());
                    vo.setGender(pet.getGender());
                    vo.setCategory(pet.getCategory());
                    vo.setCoverImage(pet.getCoverImage());
                    vo.setDescription(pet.getDescription());
                    vo.setLikeCount(pet.getLikeCount());
                    vo.setFavoriteCount(pet.getFavoriteCount());
                    return vo;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        
        result.setRecords(petVOList);
        return result;
    }
}
