package com.animal.adopt.service.impl;

import com.animal.adopt.constants.RedisConstant;
import com.animal.adopt.entity.po.Article;
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
                redisTemplate.delete(RedisConstant.buildArticleFavoriteCountKey(articleId));
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
                redisTemplate.delete(RedisConstant.buildArticleFavoriteCountKey(articleId));
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

    @Override
    public long getFavoriteCount(Long articleId) {
        // 先从 Redis 缓存获取
        String key = RedisConstant.buildArticleFavoriteCountKey(articleId);
        Object cached = redisTemplate.opsForValue().get(key);
        if (cached instanceof Number) {
            return ((Number) cached).longValue();
        }
        
        // 从数据库获取
        Article article = articleMapper.selectById(articleId);
        if (article == null) {
            return 0;
        }
        
        long count = article.getFavoriteCount();
        // 缓存到 Redis
        redisTemplate.opsForValue().set(key, count);
        return count;
    }
}