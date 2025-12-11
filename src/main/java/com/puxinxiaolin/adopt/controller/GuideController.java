package com.puxinxiaolin.adopt.controller;

import com.puxinxiaolin.adopt.common.Result;
import com.puxinxiaolin.adopt.constants.MessageConstant;
import com.puxinxiaolin.adopt.entity.vo.GuideVO;
import com.puxinxiaolin.adopt.service.GuideService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 指南控制器
 */
@Slf4j
@RestController
@RequestMapping("/guide")
@CrossOrigin
@RequiredArgsConstructor
public class GuideController {
    
    private final GuideService guideService;
    
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
    public Result<GuideVO> getGuideDetail(@PathVariable Long id) {
        GuideVO guide = guideService.getGuideDetail(id);
        if (guide == null) {
            return Result.error(MessageConstant.GUIDE_NOT_FOUND);
        }
        
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
    public Result<Boolean> likeGuide(@PathVariable Long id) {
        guideService.likeGuide(id);
        return Result.success(true);
    }
    
    /**
     * 取消点赞指南
     */
    @DeleteMapping("/{id}/like")
    public Result<Boolean> unlikeGuide(@PathVariable Long id) {
        guideService.unlikeGuide(id);
        return Result.success(true);
    }
    
    /**
     * 收藏指南
     */
    @PostMapping("/{id}/favorite")
    public Result<Boolean> favoriteGuide(@PathVariable Long id) {
        guideService.favoriteGuide(id);
        return Result.success(true);
    }
    
    /**
     * 取消收藏指南
     */
    @DeleteMapping("/{id}/favorite")
    public Result<Boolean> unfavoriteGuide(@PathVariable Long id) {
        guideService.unfavoriteGuide(id);
        return Result.success(true);
    }

    /**
     * 获取指南点赞数量（无需认证）
     */
    @GetMapping("/{id}/like/count")
    public Result<Long> getGuideLikeCount(@PathVariable Long id) {
        GuideVO guide = guideService.getGuideDetail(id);
        if (guide == null) {
            return Result.error(MessageConstant.GUIDE_NOT_FOUND);
        }
        return Result.success((long) (guide.getLikeCount() != null ? guide.getLikeCount() : 0));
    }

    /**
     * 获取指南收藏数量（无需认证）
     */
    @GetMapping("/{id}/favorite/count")
    public Result<Long> getGuideFavoriteCount(@PathVariable Long id) {
        GuideVO guide = guideService.getGuideDetail(id);
        if (guide == null) {
            return Result.error(MessageConstant.GUIDE_NOT_FOUND);
        }
        return Result.success(0L);  // 指南表中没有收藏计数字段, 返回0
    }

    /**
     * 检查用户是否已点赞指南（需要认证）
     */
    @GetMapping("/{id}/like/check")
    public Result<Boolean> isGuideLiked(@PathVariable Long id) {
        return Result.success(guideService.isGuideLiked(id));
    }

    /**
     * 检查用户是否已收藏指南（需要认证）
     */
    @GetMapping("/{id}/favorite/check")
    public Result<Boolean> isGuideFavorited(@PathVariable Long id) {
        return Result.success(guideService.isGuideFavorited(id));
    }

    /**
     * 获取所有指南分类
     */
    @GetMapping("/categories/list")
    public Result<List<String>> getCategories() {
        List<String> categories = guideService.getAllCategories();
        return Result.success(categories);
    }
}
