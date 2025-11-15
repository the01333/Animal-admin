package com.animal.adopt.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.animal.adopt.common.Result;
import com.animal.adopt.entity.po.Favorite;
import com.animal.adopt.service.FavoriteService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 收藏控制器
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/favorite")
@RequiredArgsConstructor
public class FavoriteController {
    
    private final FavoriteService favoriteService;
    
    /**
     * 添加收藏
     */
    @PostMapping("/{petId}")
    public Result<String> addFavorite(@PathVariable Long petId) {
        Long userId = StpUtil.getLoginIdAsLong();
        favoriteService.addFavorite(userId, petId);
        return Result.success("收藏成功", null);
    }
    
    /**
     * 取消收藏
     */
    @DeleteMapping("/{petId}")
    public Result<String> removeFavorite(@PathVariable Long petId) {
        Long userId = StpUtil.getLoginIdAsLong();
        favoriteService.removeFavorite(userId, petId);
        return Result.success("取消收藏成功", null);
    }
    
    /**
     * 检查是否已收藏
     */
    @GetMapping("/check/{petId}")
    public Result<Boolean> isFavorite(@PathVariable Long petId) {
        Long userId = StpUtil.getLoginIdAsLong();
        boolean isFavorite = favoriteService.isFavorite(userId, petId);
        return Result.success(isFavorite);
    }
    
    /**
     * 查询当前用户的收藏列表
     */
    @GetMapping("/my")
    public Result<Page<Favorite>> queryMyFavorites(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size) {
        Long userId = StpUtil.getLoginIdAsLong();
        Page<Favorite> page = new Page<>(current, size);
        Page<Favorite> result = favoriteService.queryUserFavorites(page, userId);
        return Result.success(result);
    }
}

