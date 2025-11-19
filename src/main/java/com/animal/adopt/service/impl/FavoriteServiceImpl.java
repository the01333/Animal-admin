package com.animal.adopt.service.impl;

import com.animal.adopt.common.ResultCode;
import com.animal.adopt.entity.po.Favorite;
import com.animal.adopt.entity.po.Pet;
import com.animal.adopt.exception.BusinessException;
import com.animal.adopt.mapper.FavoriteMapper;
import com.animal.adopt.mapper.PetMapper;
import com.animal.adopt.service.FavoriteService;
import com.animal.adopt.service.PetService;
import com.animal.adopt.constants.RedisConstant;
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
    private final PetMapper petMapper;
    private final org.springframework.data.redis.core.RedisTemplate<String, Object> redisTemplate;
    
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
        LambdaQueryWrapper<Favorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Favorite::getUserId, userId)
                .eq(Favorite::getPetId, petId);
        
        Favorite existing = this.getOne(wrapper);
        if (existing != null) {
            log.warn("该宠物已收藏, 用户ID: {}, 宠物ID: {}", userId, petId);
            return true;
        }
        
        // 创建收藏记录
        Favorite favorite = new Favorite();
        favorite.setUserId(userId);
        favorite.setPetId(petId);
        
        try {
            boolean saved = this.save(favorite);
            if (saved) {
                int updated = petMapper.incrementFavoriteCount(petId);
                if (updated > 0) {
                    redisTemplate.delete(RedisConstant.buildPetFavoriteCountKey(petId));
                }
            }
            return saved;
        } catch (Exception e) {
            // 处理唯一键冲突异常 - 可能是并发操作导致的
            if (e.getCause() != null && e.getCause().getMessage() != null 
                    && e.getCause().getMessage().contains("Duplicate entry")) {
                log.warn("收藏记录已存在（并发操作）, 用户ID: {}, 宠物ID: {}", userId, petId);
                return true;
            }
            throw e;
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeFavorite(Long userId, Long petId) {
        log.info("取消收藏, 用户ID: {}, 宠物ID: {}", userId, petId);
        
        LambdaQueryWrapper<Favorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Favorite::getUserId, userId)
                .eq(Favorite::getPetId, petId);
        
        boolean removed = this.remove(wrapper);
        if (removed) {
            int updated = petMapper.decrementFavoriteCount(petId);
            if (updated > 0) {
                redisTemplate.delete(RedisConstant.buildPetFavoriteCountKey(petId));
            }
        }
        return removed;
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


