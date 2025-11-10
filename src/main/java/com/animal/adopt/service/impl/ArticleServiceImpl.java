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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 文章服务实现类
 */
@Slf4j
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {
    
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
        
        return this.page(page, wrapper);
    }
    
    @Override
    public Article getArticleDetail(Long id) {
        log.info("查询文章详情, ID: {}", id);
        
        Article article = this.getById(id);
        if (article == null) {
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "文章不存在");
        }
        
        // 增加浏览次数
        this.incrementViewCount(id);
        
        return article;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void incrementViewCount(Long id) {
        log.debug("增加文章, 次数, ID: {}", id);
        
        LambdaUpdateWrapper<Article> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Article::getId, id)
                .setSql("view_count = view_count + 1");
        this.update(wrapper);
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


