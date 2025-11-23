package com.animal.adopt.service;

import com.animal.adopt.entity.po.ArticleLike;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 文章点赞服务接口
 */
public interface ArticleLikeService extends IService<ArticleLike> {
    
    /**
     * 点赞文章
     * @param userId 用户ID
     * @param articleId 文章ID
     * @return 是否成功
     */
    boolean likeArticle(Long userId, Long articleId);
    
    /**
     * 取消点赞
     * @param userId 用户ID
     * @param articleId 文章ID
     * @return 是否成功
     */
    boolean unlikeArticle(Long userId, Long articleId);
    
    /**
     * 检查是否已点赞
     * @param userId 用户ID
     * @param articleId 文章ID
     * @return 是否已点赞
     */
    boolean isLiked(Long userId, Long articleId);
    
    /**
     * 获取文章点赞数量（无需认证）
     * @param articleId 文章ID
     * @return 点赞数量
     */
    long getLikeCount(Long articleId);
}
