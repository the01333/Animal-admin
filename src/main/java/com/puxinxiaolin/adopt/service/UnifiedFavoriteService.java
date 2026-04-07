package com.puxinxiaolin.adopt.service;

import com.puxinxiaolin.adopt.entity.entity.FavoriteUnified;
import com.puxinxiaolin.adopt.enums.TargetType;

import java.util.List;

/**
 * 统一收藏服务接口
 * 
 * 支持新旧表结构，优先使用新表（t_favorite），如果新表不存在则使用旧表
 */
public interface UnifiedFavoriteService {
    
    /**
     * 收藏
     * 
     * @param userId 用户ID
     * @param targetId 目标ID
     * @param targetType 目标类型
     * @return 是否成功
     */
    boolean favorite(Long userId, Long targetId, TargetType targetType);
    
    /**
     * 取消收藏
     * 
     * @param userId 用户ID
     * @param targetId 目标ID
     * @param targetType 目标类型
     * @return 是否成功
     */
    boolean unfavorite(Long userId, Long targetId, TargetType targetType);
    
    /**
     * 查询是否收藏
     * 
     * @param userId 用户ID
     * @param targetId 目标ID
     * @param targetType 目标类型
     * @return 是否已收藏
     */
    boolean isFavorited(Long userId, Long targetId, TargetType targetType);
    
    /**
     * 获取收藏数
     * 
     * @param targetId 目标ID
     * @param targetType 目标类型
     * @return 收藏数
     */
    int getFavoriteCount(Long targetId, TargetType targetType);
    
    /**
     * 获取用户的收藏列表
     * 
     * @param userId 用户ID
     * @param targetType 目标类型
     * @return 收藏列表
     */
    List<FavoriteUnified> getUserFavorites(Long userId, TargetType targetType);
}
