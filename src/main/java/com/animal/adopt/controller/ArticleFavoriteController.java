package com.animal.adopt.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.animal.adopt.common.Result;
import com.animal.adopt.service.ArticleFavoriteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Validated
@RestController
@RequestMapping("/article/favorite")
@RequiredArgsConstructor
public class ArticleFavoriteController {

    private final ArticleFavoriteService articleFavoriteService;

    @PostMapping("/{articleId}")
    public Result<String> favoriteArticle(@PathVariable Long articleId) {
        Long userId = StpUtil.getLoginIdAsLong();
        articleFavoriteService.favoriteArticle(userId, articleId);
        
        return Result.success("收藏成功", null);
    }

    @DeleteMapping("/{articleId}")
    public Result<String> unfavoriteArticle(@PathVariable Long articleId) {
        Long userId = StpUtil.getLoginIdAsLong();
        articleFavoriteService.unfavoriteArticle(userId, articleId);
        
        return Result.success("取消收藏成功", null);
    }

    @GetMapping("/check/{articleId}")
    public Result<Boolean> isFavorited(@PathVariable Long articleId) {
        Long userId = StpUtil.getLoginIdAsLong();
        
        return Result.success(articleFavoriteService.isFavorited(userId, articleId));
    }

    @GetMapping("/count/{articleId}")
    public Result<Long> getFavoriteCount(@PathVariable Long articleId) {
        long count = articleFavoriteService.getFavoriteCount(articleId);
        return Result.success(count);
    }
}