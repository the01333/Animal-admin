package com.animal.adopt.service;

import com.animal.adopt.entity.po.ArticleFavorite;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 文章收藏服务接口
 */
public interface ArticleFavoriteService extends IService<ArticleFavorite> {
    
    /**
     * 收藏文章
     * @param userId 用户ID
     * @param articleId 文章ID
     * @return 是否成功
     */
    boolean favoriteArticle(Long userId, Long articleId);
    
    /**
     * 取消收藏
     * @param userId 用户ID
     * @param articleId 文章ID
     * @return 是否成功
     */
    boolean unfavoriteArticle(Long userId, Long articleId);
    
    /**
     * 检查是否已收藏
     * @param userId 用户ID
     * @param articleId 文章ID
     * @return 是否已收藏
     */
    boolean isFavorited(Long userId, Long articleId);
    
    /**
     * 获取文章收藏数量（无需认证）
     * @param articleId 文章ID
     * @return 收藏数量
     */
    long getFavoriteCount(Long articleId);
}
