package com.animal.adopt.controller;

import com.animal.adopt.common.Result;
import com.animal.adopt.entity.vo.GuideVO;
import com.animal.adopt.service.GuideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 指南控制器
 */
@RestController
@RequestMapping("/guide")
@CrossOrigin
public class GuideController {
    
    @Autowired
    private GuideService guideService;
    
    /**
     * 获取所有指南列表
     */
    @GetMapping("/list")
    public Result<List<GuideVO>> getAllGuides() {
        return Result.success(guideService.getAllGuides());
    }
    
    /**
     * 根据ID获取指南详情
     */
    @GetMapping("/{id}")
    public Result<GuideVO> getGuideDetail(
            @PathVariable Long id,
            @RequestParam(required = false) Long userId) {
        GuideVO guide = guideService.getGuideDetail(id, userId);
        if (guide == null) {
            return Result.error("指南不存在");
        }
        // 增加浏览次数
        guideService.increaseViews(id);
        return Result.success(guide);
    }
    
    /**
     * 根据分类获取指南列表
     */
    @GetMapping("/category/{category}")
    public Result<List<GuideVO>> getGuidesByCategory(@PathVariable String category) {
        List<GuideVO> guides = guideService.getGuidesByCategory(category);
        return Result.success(guides);
    }
    
    /**
     * 点赞指南
     */
    @PostMapping("/{id}/like")
    public Result<Void> likeGuide(
            @PathVariable Long id,
            @RequestParam Long userId) {
        guideService.likeGuide(id, userId);
        return Result.success();
    }
    
    /**
     * 取消点赞指南
     */
    @DeleteMapping("/{id}/like")
    public Result<Void> unlikeGuide(
            @PathVariable Long id,
            @RequestParam Long userId) {
        guideService.unlikeGuide(id, userId);
        return Result.success();
    }
    
    /**
     * 收藏指南
     */
    @PostMapping("/{id}/favorite")
    public Result<Void> favoriteGuide(
            @PathVariable Long id,
            @RequestParam Long userId) {
        guideService.favoriteGuide(id, userId);
        return Result.success();
    }
    
    /**
     * 取消收藏指南
     */
    @DeleteMapping("/{id}/favorite")
    public Result<Void> unfavoriteGuide(
            @PathVariable Long id,
            @RequestParam Long userId) {
        guideService.unfavoriteGuide(id, userId);
        return Result.success();
    }

    /**
     * 获取指南点赞数量（无需认证）
     */
    @GetMapping("/{id}/like/count")
    public Result<Long> getGuideLikeCount(@PathVariable Long id) {
        GuideVO guide = guideService.getGuideDetail(id, null);
        if (guide == null) {
            return Result.error("指南不存在");
        }
        return Result.success((long) (guide.getLikeCount() != null ? guide.getLikeCount() : 0));
    }

    /**
     * 获取指南收藏数量（无需认证）
     */
    @GetMapping("/{id}/favorite/count")
    public Result<Long> getGuideFavoriteCount(@PathVariable Long id) {
        GuideVO guide = guideService.getGuideDetail(id, null);
        if (guide == null) {
            return Result.error("指南不存在");
        }
        return Result.success(0L);  // 指南表中没有收藏计数字段，返回0
    }

    /**
     * 检查用户是否已点赞指南（需要认证）
     */
    @GetMapping("/{id}/like/check")
    public Result<Boolean> isGuideLiked(@PathVariable Long id) {
        Long userId = cn.dev33.satoken.stp.StpUtil.getLoginIdAsLong();
        boolean liked = guideService.isGuideLiked(id, userId);
        return Result.success(liked);
    }

    /**
     * 检查用户是否已收藏指南（需要认证）
     */
    @GetMapping("/{id}/favorite/check")
    public Result<Boolean> isGuideFavorited(@PathVariable Long id) {
        Long userId = cn.dev33.satoken.stp.StpUtil.getLoginIdAsLong();
        boolean favorited = guideService.isGuideFavorited(id, userId);
        return Result.success(favorited);
    }
}
