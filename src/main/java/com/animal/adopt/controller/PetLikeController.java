package com.animal.adopt.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.animal.adopt.common.Result;
import com.animal.adopt.service.PetLikeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 宠物点赞控制器
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/pet/like")
@RequiredArgsConstructor
public class PetLikeController {

    private final PetLikeService petLikeService;

    /**
     * 点赞宠物
     */
    @PostMapping("/{petId}")
    public Result<String> likePet(@PathVariable Long petId) {
        Long userId = StpUtil.getLoginIdAsLong();
        petLikeService.likePet(userId, petId);
        return Result.success("点赞成功", null);
    }

    /**
     * 取消点赞
     */
    @DeleteMapping("/{petId}")
    public Result<String> unlikePet(@PathVariable Long petId) {
        Long userId = StpUtil.getLoginIdAsLong();
        petLikeService.unlikePet(userId, petId);
        return Result.success("取消点赞成功", null);
    }

    /**
     * 获取点赞数量（无需认证）
     */
    @GetMapping("/count/{petId}")
    public Result<Long> getLikeCount(@PathVariable Long petId) {
        long count = petLikeService.getLikeCount(petId);
        return Result.success(count);
    }

    /**
     * 检查是否已点赞（需要认证）
     */
    @GetMapping("/check/{petId}")
    public Result<Boolean> isLiked(@PathVariable Long petId) {
        Long userId = StpUtil.getLoginIdAsLong();
        boolean liked = petLikeService.isLiked(userId, petId);
        return Result.success(liked);
    }
}