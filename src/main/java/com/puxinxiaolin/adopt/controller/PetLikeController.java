package com.puxinxiaolin.adopt.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.puxinxiaolin.adopt.common.Result;
import com.puxinxiaolin.adopt.entity.vo.PetVO;
import com.puxinxiaolin.adopt.service.PetLikeService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
    public Result<Boolean> likePet(@PathVariable Long petId) {
        Long userId = StpUtil.getLoginIdAsLong();
        boolean success = petLikeService.likePet(userId, petId);
        return success ? Result.success(true) : Result.error("点赞失败");
    }

    /**
     * 取消点赞
     */
    @DeleteMapping("/{petId}")
    public Result<Boolean> unlikePet(@PathVariable Long petId) {
        Long userId = StpUtil.getLoginIdAsLong();
        boolean success = petLikeService.unlikePet(userId, petId);
        return success ? Result.success(true) : Result.error("取消点赞失败");
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

    /**
     * 查询当前用户点赞的宠物列表
     */
    @GetMapping("/my")
    public Result<Page<PetVO>> queryMyLikes(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size) {
        Long userId = StpUtil.getLoginIdAsLong();
        Page<PetVO> page = new Page<>(current, size);
        Page<PetVO> result = petLikeService.queryUserLikedPets(page, userId);
        return Result.success(result);
    }
}