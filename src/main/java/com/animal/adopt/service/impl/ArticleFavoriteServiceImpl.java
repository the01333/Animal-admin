package com.animal.adopt.service.impl;

import com.animal.adopt.constants.RedisKeyConstant;
import com.animal.adopt.entity.po.ArticleFavorite;
import com.animal.adopt.mapper.ArticleFavoriteMapper;
import com.animal.adopt.mapper.ArticleMapper;
import com.animal.adopt.service.ArticleFavoriteService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleFavoriteServiceImpl extends ServiceImpl<ArticleFavoriteMapper, ArticleFavorite> implements ArticleFavoriteService {

    private final ArticleMapper articleMapper;
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean favoriteArticle(Long userId, Long articleId) {
        LambdaQueryWrapper<ArticleFavorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ArticleFavorite::getUserId, userId).eq(ArticleFavorite::getArticleId, articleId);
        if (this.count(wrapper) > 0) {
            return true;
        }
        ArticleFavorite fav = new ArticleFavorite();
        fav.setUserId(userId);
        fav.setArticleId(articleId);
        boolean saved = this.save(fav);
        if (saved) {
            int updated = articleMapper.incrementFavoriteCount(articleId);
            if (updated > 0) {
                redisTemplate.delete(RedisKeyConstant.buildArticleFavoriteCountKey(articleId));
            }
        }
        return saved;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean unfavoriteArticle(Long userId, Long articleId) {
        LambdaQueryWrapper<ArticleFavorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ArticleFavorite::getUserId, userId).eq(ArticleFavorite::getArticleId, articleId);
        ArticleFavorite fav = this.getOne(wrapper);
        if (fav == null) {
            return false;
        }
        boolean removed = this.removeById(fav.getId());
        if (removed) {
            int updated = articleMapper.decrementFavoriteCount(articleId);
            if (updated > 0) {
                redisTemplate.delete(RedisKeyConstant.buildArticleFavoriteCountKey(articleId));
            }
        }
        return removed;
    }

    @Override
    public boolean isFavorited(Long userId, Long articleId) {
        LambdaQueryWrapper<ArticleFavorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ArticleFavorite::getUserId, userId).eq(ArticleFavorite::getArticleId, articleId);
        return this.count(wrapper) > 0;
    }
}