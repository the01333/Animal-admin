package com.animal.adopt.service.impl;

import cn.hutool.core.util.StrUtil;
import com.animal.adopt.common.ResultCode;
import com.animal.adopt.entity.po.Article;
import com.animal.adopt.exception.BusinessException;
import com.animal.adopt.mapper.ArticleMapper;
import com.animal.adopt.service.ArticleService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import com.animal.adopt.service.impl.ViewCountService;
import com.animal.adopt.service.impl.MinioUrlService;
import org.springframework.data.redis.core.RedisTemplate;
import com.animal.adopt.constants.RedisKeyConstant;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 文章服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    private final ViewCountService viewCountService;
    private final RedisTemplate<String, Object> redisTemplate;
    private final MinioUrlService minioUrlService;
    
    @Override
    public Page<Article> queryArticlePage(Page<Article> page, String category, Integer status, String keyword) {
        log.info("分页查询文章列表, 分类: {}, 状态: {}, 关键词: {}", category, status, keyword);
        
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        
        // 分类筛选
        if (StrUtil.isNotBlank(category)) {
            wrapper.eq(Article::getCategory, category);
        }
        
        // 状态筛选
        if (status != null) {
            wrapper.eq(Article::getStatus, status);
        }
        
        // 关键词搜索（标题、摘要）
        if (StrUtil.isNotBlank(keyword)) {
            wrapper.and(w -> w.like(Article::getTitle, keyword)
                    .or()
                    .like(Article::getSummary, keyword));
        }
        
        // 按发布时间倒序
        wrapper.orderByDesc(Article::getSortOrder, Article::getPublishTime);
        var result = this.page(page, wrapper);
        for (Article a : result.getRecords()) {
            a.setCoverImage(minioUrlService.normalizeUrl(a.getCoverImage()));
        }
        // 列表计数读缓存
        for (Article a : result.getRecords()) {
            Long aid = a.getId();
            String likeKey = com.animal.adopt.constants.RedisKeyConstant.buildArticleLikeCountKey(aid);
            Object likeVal = redisTemplate.opsForValue().get(likeKey);
            if (likeVal instanceof Number) {
                a.setLikeCount(((Number) likeVal).intValue());
            } else {
                redisTemplate.opsForValue().set(likeKey, a.getLikeCount());
            }
            String favKey = com.animal.adopt.constants.RedisKeyConstant.buildArticleFavoriteCountKey(aid);
            Object favVal = redisTemplate.opsForValue().get(favKey);
            if (favVal instanceof Number) {
                a.setFavoriteCount(((Number) favVal).intValue());
            } else {
                redisTemplate.opsForValue().set(favKey, a.getFavoriteCount());
            }
        }
        return result;
    }
    
    @Override
    public Article getArticleDetail(Long id) {
        log.info("查询文章详情, ID: {}", id);
        
        Article article = this.getById(id);
        if (article == null) {
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "文章不存在");
        }
        
        // 增加浏览次数（Redis增量）
        viewCountService.incrementArticleView(id);
        // 合并增量浏览次数
        int inc = viewCountService.getArticleViewIncrement(id);
        article.setViewCount(article.getViewCount() + inc);
        // 点赞/收藏计数读缓存
        String likeKey = RedisKeyConstant.buildArticleLikeCountKey(id);
        Object likeVal = redisTemplate.opsForValue().get(likeKey);
        if (likeVal instanceof Number) {
            article.setLikeCount(((Number) likeVal).intValue());
        } else {
            redisTemplate.opsForValue().set(likeKey, article.getLikeCount());
        }
        String favKey = RedisKeyConstant.buildArticleFavoriteCountKey(id);
        Object favVal = redisTemplate.opsForValue().get(favKey);
        if (favVal instanceof Number) {
            article.setFavoriteCount(((Number) favVal).intValue());
        } else {
            redisTemplate.opsForValue().set(favKey, article.getFavoriteCount());
        }
        article.setCoverImage(minioUrlService.normalizeUrl(article.getCoverImage()));
        return article;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void incrementViewCount(Long id) {
        // 已改为通过Redis增量统计与定时任务入库
        viewCountService.incrementArticleView(id);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean publishArticle(Long id) {
        log.info("发布文章, ID: {}", id);
        
        Article article = this.getById(id);
        if (article == null) {
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "文章不存在");
        }
        
        article.setStatus(1); // 已发布
        article.setPublishTime(LocalDateTime.now());
        return this.updateById(article);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean unpublishArticle(Long id) {
        log.info("下架文章, ID: {}", id);
        
        Article article = this.getById(id);
        if (article == null) {
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "文章不存在");
        }
        
        article.setStatus(2); // 已下架
        return this.updateById(article);
    }
}


