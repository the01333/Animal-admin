package com.animal.adopt.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.animal.adopt.common.Result;
import com.animal.adopt.entity.Article;
import com.animal.adopt.service.ArticleService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

/**
 * 文章控制器
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/article")
@RequiredArgsConstructor
public class ArticleController {
    
    private final ArticleService articleService;
    
    /**
     * 分页查询文章列表
     */
    @GetMapping("/page")
    public Result<Page<Article>> queryArticlePage(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) String keyword) {
        Page<Article> page = new Page<>(current, size);
        Page<Article> result = articleService.queryArticlePage(page, category, status, keyword);
        return Result.success(result);
    }
    
    /**
     * 根据ID查询文章详情
     */
    @GetMapping("/{id}")
    public Result<Article> getArticleDetail(@PathVariable Long id) {
        Article article = articleService.getArticleDetail(id);
        return Result.success(article);
    }
    
    /**
     * 创建文章
     */
    @PostMapping
    public Result<String> createArticle(@Valid @RequestBody Article article) {
        Long userId = StpUtil.getLoginIdAsLong();
        article.setCreateBy(userId);
        article.setStatus(0); // 默认草稿状态
        articleService.save(article);
        return Result.success("创建成功", null);
    }
    
    /**
     * 更新文章
     */
    @PutMapping("/{id}")
    public Result<String> updateArticle(@PathVariable Long id, @Valid @RequestBody Article article) {
        article.setId(id);
        articleService.updateById(article);
        return Result.success("更新成功", null);
    }
    
    /**
     * 删除文章
     */
    @DeleteMapping("/{id}")
    public Result<String> deleteArticle(@PathVariable Long id) {
        articleService.removeById(id);
        return Result.success("删除成功", null);
    }
    
    /**
     * 发布文章
     */
    @PutMapping("/{id}/publish")
    public Result<String> publishArticle(@PathVariable Long id) {
        articleService.publishArticle(id);
        return Result.success("发布成功", null);
    }
    
    /**
     * 下架文章
     */
    @PutMapping("/{id}/unpublish")
    public Result<String> unpublishArticle(@PathVariable Long id) {
        articleService.unpublishArticle(id);
        return Result.success("下架成功", null);
    }
}

