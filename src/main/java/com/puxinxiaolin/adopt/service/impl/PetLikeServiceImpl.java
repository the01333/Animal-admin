package com.puxinxiaolin.adopt.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.puxinxiaolin.adopt.constants.RedisConstant;
import com.puxinxiaolin.adopt.entity.dto.PetLikePageQueryDTO;
import com.puxinxiaolin.adopt.entity.entity.Like;
import com.puxinxiaolin.adopt.entity.entity.Pet;
import com.puxinxiaolin.adopt.entity.entity.PetLike;
import com.puxinxiaolin.adopt.entity.vo.PetVO;
import com.puxinxiaolin.adopt.enums.TargetType;
import com.puxinxiaolin.adopt.mapper.PetLikeMapper;
import com.puxinxiaolin.adopt.mapper.PetMapper;
import com.puxinxiaolin.adopt.service.PetLikeService;
import com.puxinxiaolin.adopt.service.UnifiedLikeService;
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
 * 使用统一点赞服务，兼容新旧表结构
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PetLikeServiceImpl extends ServiceImpl<PetLikeMapper, PetLike> implements PetLikeService {

    private final PetMapper petMapper;
    private final RedisUtil redisUtil;
    private final UnifiedLikeService unifiedLikeService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean likePet(Long petId) {
        Long userId = StpUtil.getLoginIdAsLong();

        // 使用统一点赞服务
        boolean success = unifiedLikeService.like(userId, petId, TargetType.PET);
        
        if (success) {
            petMapper.incrementLikeCount(petId);
            deleteLikeCountCache(petId);
            log.info("用户 {} 点赞宠物 {}", userId, petId);
        }
        
        return success;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean unlikePet(Long petId) {
        Long userId = StpUtil.getLoginIdAsLong();

        // 使用统一点赞服务
        boolean success = unifiedLikeService.unlike(userId, petId, TargetType.PET);
        
        if (success) {
            petMapper.decrementLikeCount(petId);
            deleteLikeCountCache(petId);
            log.info("用户 {} 取消点赞宠物 {}", userId, petId);
        }
        
        return success;
    }

    /**
     * 删除点赞数缓存
     *
     * @param petId
     */
    private void deleteLikeCountCache(Long petId) {
        String cacheKey = RedisConstant.buildPetLikeCountKey(petId);
        redisUtil.delete(cacheKey);
    }

    @Override
    public boolean isLiked(Long petId) {
        Long userId = StpUtil.getLoginIdAsLong();
        // 使用统一点赞服务
        return unifiedLikeService.isLiked(userId, petId, TargetType.PET);
    }

    @Override
    public long getLikeCount(Long petId) {
        Pet pet = petMapper.selectById(petId);
        return pet != null ? pet.getLikeCount() : 0;
    }

    @Override
    public Page<PetVO> queryUserLikedPets(PetLikePageQueryDTO queryDTO) {
        Long userId = StpUtil.getLoginIdAsLong();

        // 使用统一点赞服务获取点赞列表
        List<Like> likes = unifiedLikeService.getUserLikes(userId, TargetType.PET);
        
        // 手动分页
        Long current = queryDTO.getCurrent();
        int size = Math.toIntExact(queryDTO.getSize());
        int total = likes.size();
        int start = (int) ((current - 1) * size);
        int end = Math.min(start + size, total);
        
        List<Like> pagedLikes = start < total ? likes.subList(start, end) : List.of();

        Page<PetVO> result = new Page<>();
        result.setCurrent(current);
        result.setSize(size);
        result.setTotal(total);
        result.setPages((total + size - 1) / size);

        List<PetVO> petVOList = pagedLikes.stream()
                .map(like -> {
                    Pet pet = petMapper.selectById(like.getTargetId());
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
