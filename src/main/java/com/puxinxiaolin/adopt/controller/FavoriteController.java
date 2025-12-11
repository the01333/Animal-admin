package com.puxinxiaolin.adopt.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.puxinxiaolin.adopt.common.Result;
import com.puxinxiaolin.adopt.entity.dto.FavoritePageQueryDTO;
import com.puxinxiaolin.adopt.entity.vo.FavoriteVO;
import com.puxinxiaolin.adopt.entity.vo.PetVO;
import com.puxinxiaolin.adopt.service.FavoriteService;
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
        favoriteService.addFavorite(petId);
        return Result.success("收藏成功", null);
    }
    
    /**
     * 取消收藏
     */
    @DeleteMapping("/{petId}")
    public Result<String> removeFavorite(@PathVariable Long petId) {
        favoriteService.removeFavorite(petId);
        return Result.success("取消收藏成功", null);
    }
    
    /**
     * 获取收藏数量（无需认证）
     */
    @GetMapping("/count/{petId}")
    public Result<Long> getFavoriteCount(@PathVariable Long petId) {
        long count = favoriteService.getFavoriteCount(petId);
        return Result.success(count);
    }
    
    /**
     * 检查是否已收藏（需要认证）
     */
    @GetMapping("/check/{petId}")
    public Result<Boolean> isFavorite(@PathVariable Long petId) {
        return Result.success(favoriteService.isFavorite(petId));
    }
    
    /**
     * 查询当前用户的收藏列表
     */
    @GetMapping("/my")
    public Result<Page<FavoriteVO>> queryMyFavorites(@ModelAttribute FavoritePageQueryDTO queryDTO) {
        return Result.success(favoriteService.queryUserFavorites(queryDTO));
    }

    /**
     * 查询当前用户收藏的宠物列表（包含完整宠物信息）
     */
    @GetMapping("/my/pets")
    public Result<Page<PetVO>> queryMyFavoritePets(@ModelAttribute FavoritePageQueryDTO queryDTO) {
        return Result.success(favoriteService.queryUserFavoritePets(queryDTO));
    }
}

