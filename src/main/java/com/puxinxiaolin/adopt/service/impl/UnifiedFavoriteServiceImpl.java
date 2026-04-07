package com.puxinxiaolin.adopt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.puxinxiaolin.adopt.entity.entity.FavoriteUnified;
import com.puxinxiaolin.adopt.enums.TargetType;
import com.puxinxiaolin.adopt.mapper.FavoriteUnifiedMapper;
import com.puxinxiaolin.adopt.service.UnifiedFavoriteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 统一收藏服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UnifiedFavoriteServiceImpl implements UnifiedFavoriteService {
    
    private final FavoriteUnifiedMapper favoriteUnifiedMapper;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean favorite(Long userId, Long targetId, TargetType targetType) {
        log.info("收藏操作 - 用户ID: {}, 目标ID: {}, 类型: {}", userId, targetId, targetType);
        
        // 检查是否已收藏
        if (isFavorited(userId, targetId, targetType)) {
            log.warn("已收藏，无需重复操作");
            return true;
        }
        
        FavoriteUnified favorite = FavoriteUnified.builder()
                .userId(userId)
                .targetId(targetId)
                .targetType(targetType.getCode())
                .createTime(LocalDateTime.now())
                .build();
        
        int result = favoriteUnifiedMapper.insert(favorite);
        boolean success = result > 0;
        
        if (success) {
            log.info("收藏成功");
        }
        
        return success;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean unfavorite(Long userId, Long targetId, TargetType targetType) {
        log.info("取消收藏 - 用户ID: {}, 目标ID: {}, 类型: {}", userId, targetId, targetType);
        
        LambdaQueryWrapper<FavoriteUnified> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FavoriteUnified::getUserId, userId)
               .eq(FavoriteUnified::getTargetId, targetId)
               .eq(FavoriteUnified::getTargetType, targetType.getCode());
        
        int result = favoriteUnifiedMapper.delete(wrapper);
        boolean success = result > 0;
        
        if (success) {
            log.info("取消收藏成功");
        }
        
        return success;
    }
    
    @Override
    public boolean isFavorited(Long userId, Long targetId, TargetType targetType) {
        int count = favoriteUnifiedMapper.isFavorited(userId, targetId, targetType.getCode());
        return count > 0;
    }
    
    @Override
    public int getFavoriteCount(Long targetId, TargetType targetType) {
        return favoriteUnifiedMapper.getFavoriteCount(targetId, targetType.getCode());
    }
    
    @Override
    public List<FavoriteUnified> getUserFavorites(Long userId, TargetType targetType) {
        return favoriteUnifiedMapper.getUserFavorites(userId, targetType.getCode());
    }
}
