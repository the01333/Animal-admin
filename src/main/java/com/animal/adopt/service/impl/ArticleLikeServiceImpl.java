package com.animal.adopt.service.impl;

import com.animal.adopt.constants.RedisKeyConstant;
import com.animal.adopt.entity.po.ArticleLike;
import com.animal.adopt.mapper.ArticleLikeMapper;
import com.animal.adopt.mapper.ArticleMapper;
import com.animal.adopt.service.ArticleLikeService;
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
public class ArticleLikeServiceImpl extends ServiceImpl<ArticleLikeMapper, ArticleLike> implements ArticleLikeService {

    private final ArticleMapper articleMapper;
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean likeArticle(Long userId, Long articleId) {
        LambdaQueryWrapper<ArticleLike> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ArticleLike::getUserId, userId).eq(ArticleLike::getArticleId, articleId);
        if (this.count(wrapper) > 0) {
            return false;
        }
        ArticleLike like = new ArticleLike();
        like.setUserId(userId);
        like.setArticleId(articleId);
        boolean saved = this.save(like);
        if (saved) {
            int updated = articleMapper.incrementLikeCount(articleId);
            if (updated > 0) {
                redisTemplate.delete(RedisKeyConstant.buildArticleLikeCountKey(articleId));
            }
        }
        return saved;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean unlikeArticle(Long userId, Long articleId) {
        LambdaQueryWrapper<ArticleLike> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ArticleLike::getUserId, userId).eq(ArticleLike::getArticleId, articleId);
        ArticleLike like = this.getOne(wrapper);
        if (like == null) {
            return false;
        }
        boolean removed = this.removeById(like.getId());
        if (removed) {
            int updated = articleMapper.decrementLikeCount(articleId);
            if (updated > 0) {
                redisTemplate.delete(RedisKeyConstant.buildArticleLikeCountKey(articleId));
            }
        }
        return removed;
    }

    @Override
    public boolean isLiked(Long userId, Long articleId) {
        LambdaQueryWrapper<ArticleLike> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ArticleLike::getUserId, userId).eq(ArticleLike::getArticleId, articleId);
        return this.count(wrapper) > 0;
    }
}