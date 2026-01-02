package com.puxinxiaolin.adopt.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.puxinxiaolin.adopt.constants.RedisConstant;
import com.puxinxiaolin.adopt.entity.dto.FavoritePageQueryDTO;
import com.puxinxiaolin.adopt.entity.entity.Favorite;
import com.puxinxiaolin.adopt.entity.entity.Pet;
import com.puxinxiaolin.adopt.entity.vo.FavoriteVO;
import com.puxinxiaolin.adopt.entity.vo.PetVO;
import com.puxinxiaolin.adopt.enums.common.ResultCodeEnum;
import com.puxinxiaolin.adopt.exception.BizException;
import com.puxinxiaolin.adopt.mapper.FavoriteMapper;
import com.puxinxiaolin.adopt.mapper.PetMapper;
import com.puxinxiaolin.adopt.service.FavoriteService;
import com.puxinxiaolin.adopt.service.PetService;
import com.puxinxiaolin.adopt.utils.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 收藏服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl extends ServiceImpl<FavoriteMapper, Favorite> implements FavoriteService {
    
    private final PetService petService;
    private final PetMapper petMapper;
    private final RedisUtil redisUtil;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addFavorite(Long petId) {
        Long userId = StpUtil.getLoginIdAsLong();
        
        // 检查宠物是否存在
        Pet pet = petService.getById(petId);
        if (pet == null) {
            throw new BizException(ResultCodeEnum.PET_NOT_FOUND);
        }
        
        // 检查是否已收藏
        if (isFavorite(petId)) {
            log.warn("该宠物已收藏, 用户ID: {}, 宠物ID: {}", userId, petId);
            return true;
        }
        
        // 新增收藏记录
        Favorite favorite = new Favorite();
        favorite.setUserId(userId);
        favorite.setPetId(petId);
        
        this.save(favorite);
        petMapper.incrementFavoriteCount(petId);
        deleteFavoriteCountCache(petId);
        log.info("用户 {} 收藏宠物 {}", userId, petId);
        return true;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeFavorite(Long petId) {
        Long userId = StpUtil.getLoginIdAsLong();
        
        LambdaQueryWrapper<Favorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Favorite::getUserId, userId)
                .eq(Favorite::getPetId, petId);
        
        boolean removed = this.remove(wrapper);
        if (removed) {
            petMapper.decrementFavoriteCount(petId);
            deleteFavoriteCountCache(petId);
            log.info("用户 {} 取消收藏宠物 {}", userId, petId);
        }
        return removed;
    }
    
    private void deleteFavoriteCountCache(Long petId) {
        String cacheKey = RedisConstant.buildPetFavoriteCountKey(petId);
        redisUtil.delete(cacheKey);
    }
    
    @Override
    public boolean isFavorite(Long petId) {
        Long userId = StpUtil.getLoginIdAsLong();
        LambdaQueryWrapper<Favorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Favorite::getUserId, userId)
                .eq(Favorite::getPetId, petId);
        return this.count(wrapper) > 0;
    }
    
    @Override
    public Page<FavoriteVO> queryUserFavorites(FavoritePageQueryDTO queryDTO) {
        Long userId = StpUtil.getLoginIdAsLong();
        
        LambdaQueryWrapper<Favorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Favorite::getUserId, userId)
                .orderByDesc(Favorite::getCreateTime);

        Page<Favorite> favoritePage = this.page(new Page<>(queryDTO.getCurrent(), queryDTO.getSize()), wrapper);
        
        Page<FavoriteVO> voPage = new Page<>(favoritePage.getCurrent(), favoritePage.getSize(), favoritePage.getTotal());
        voPage.setRecords(favoritePage.getRecords().stream()
                .map(f -> {
                    FavoriteVO vo = new FavoriteVO();
                    vo.setId(f.getId());
                    vo.setUserId(f.getUserId());
                    vo.setPetId(f.getPetId());
                    vo.setCreateTime(f.getCreateTime());
                    return vo;
                })
                .collect(Collectors.toList()));
        return voPage;
    }
    
    @Override
    public Page<PetVO> queryUserFavoritePets(FavoritePageQueryDTO queryDTO) {
        Long userId = StpUtil.getLoginIdAsLong();
        
        LambdaQueryWrapper<Favorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Favorite::getUserId, userId)
                .orderByDesc(Favorite::getCreateTime);
        
        Page<Favorite> favoritePage = this.page(new Page<>(queryDTO.getCurrent(), queryDTO.getSize()), wrapper);
        
        Page<PetVO> result = new Page<>();
        result.setCurrent(favoritePage.getCurrent());
        result.setSize(favoritePage.getSize());
        result.setTotal(favoritePage.getTotal());
        result.setPages(favoritePage.getPages());
        
        List<PetVO> petVOList = favoritePage.getRecords().stream()
                .map(favorite -> {
                    Pet pet = petMapper.selectById(favorite.getPetId());
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
    
    @Override
    public long getFavoriteCount(Long petId) {
        Pet pet = petService.getById(petId);
        return pet != null ? pet.getFavoriteCount() : 0;
    }
}
