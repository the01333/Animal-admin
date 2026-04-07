package com.puxinxiaolin.adopt.service;

import com.puxinxiaolin.adopt.entity.entity.Like;
import com.puxinxiaolin.adopt.enums.TargetType;

import java.util.List;

/**
 * 统一点赞服务接口
 * 
 * 支持新旧表结构，优先使用新表（t_like），如果新表不存在则使用旧表
 */
public interface UnifiedLikeService {
    
    /**
     * 点赞
     * 
     * @param userId 用户ID
     * @param targetId 目标ID
     * @param targetType 目标类型
     * @return 是否成功
     */
    boolean like(Long userId, Long targetId, TargetType targetType);
    
    /**
     * 取消点赞
     * 
     * @param userId 用户ID
     * @param targetId 目标ID
     * @param targetType 目标类型
     * @return 是否成功
     */
    boolean unlike(Long userId, Long targetId, TargetType targetType);
    
    /**
     * 查询是否点赞
     * 
     * @param userId 用户ID
     * @param targetId 目标ID
     * @param targetType 目标类型
     * @return 是否已点赞
     */
    boolean isLiked(Long userId, Long targetId, TargetType targetType);
    
    /**
     * 获取点赞数
     * 
     * @param targetId 目标ID
     * @param targetType 目标类型
     * @return 点赞数
     */
    int getLikeCount(Long targetId, TargetType targetType);
    
    /**
     * 获取用户的点赞列表
     * 
     * @param userId 用户ID
     * @param targetType 目标类型
     * @return 点赞列表
     */
    List<Like> getUserLikes(Long userId, TargetType targetType);
}
