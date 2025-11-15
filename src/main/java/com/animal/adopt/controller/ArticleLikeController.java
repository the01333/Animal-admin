package com.animal.adopt.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.animal.adopt.common.Result;
import com.animal.adopt.service.ArticleLikeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Validated
@RestController
@RequestMapping("/article/like")
@RequiredArgsConstructor
public class ArticleLikeController {

    private final ArticleLikeService articleLikeService;

    @PostMapping("/{articleId}")
    public Result<String> likeArticle(@PathVariable Long articleId) {
        Long userId = StpUtil.getLoginIdAsLong();
        articleLikeService.likeArticle(userId, articleId);
        return Result.success("点赞成功", null);
    }

    @DeleteMapping("/{articleId}")
    public Result<String> unlikeArticle(@PathVariable Long articleId) {
        Long userId = StpUtil.getLoginIdAsLong();
        articleLikeService.unlikeArticle(userId, articleId);
        return Result.success("取消点赞成功", null);
    }

    @GetMapping("/check/{articleId}")
    public Result<Boolean> isLiked(@PathVariable Long articleId) {
        Long userId = StpUtil.getLoginIdAsLong();
        boolean liked = articleLikeService.isLiked(userId, articleId);
        return Result.success(liked);
    }
}