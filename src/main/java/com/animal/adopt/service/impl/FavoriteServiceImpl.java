package com.animal.adopt.service.impl;

import com.animal.adopt.common.ResultCode;
import com.animal.adopt.entity.Favorite;
import com.animal.adopt.entity.Pet;
import com.animal.adopt.exception.BusinessException;
import com.animal.adopt.mapper.FavoriteMapper;
import com.animal.adopt.service.FavoriteService;
import com.animal.adopt.service.PetService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 收藏服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl extends ServiceImpl<FavoriteMapper, Favorite> implements FavoriteService {
    
    private final PetService petService;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addFavorite(Long userId, Long petId) {
        log.info("添加收藏, 用户ID: {}, 宠物ID: {}", userId, petId);
        
        // 检查宠物是否存在
        Pet pet = petService.getById(petId);
        if (pet == null) {
            throw new BusinessException(ResultCode.PET_NOT_FOUND);
        }
        
        // 检查是否已收藏
        if (isFavorite(userId, petId)) {
            log.warn("该宠物已收藏, 用户ID: {}, 宠物ID: {}", userId, petId);
            return true;
        }
        
        // 创建收藏记录
        Favorite favorite = new Favorite();
        favorite.setUserId(userId);
        favorite.setPetId(petId);
        
        return this.save(favorite);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeFavorite(Long userId, Long petId) {
        log.info("取消收藏, 用户ID: {}, 宠物ID: {}", userId, petId);
        
        LambdaQueryWrapper<Favorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Favorite::getUserId, userId)
                .eq(Favorite::getPetId, petId);
        
        return this.remove(wrapper);
    }
    
    @Override
    public boolean isFavorite(Long userId, Long petId) {
        LambdaQueryWrapper<Favorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Favorite::getUserId, userId)
                .eq(Favorite::getPetId, petId);
        
        return this.count(wrapper) > 0;
    }
    
    @Override
    public Page<Favorite> queryUserFavorites(Page<Favorite> page, Long userId) {
        log.info("查询用户收藏列表, 用户ID: {}", userId);
        
        LambdaQueryWrapper<Favorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Favorite::getUserId, userId)
                .orderByDesc(Favorite::getCreateTime);
        
        return this.page(page, wrapper);
    }
}

