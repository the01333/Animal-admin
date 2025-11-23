package com.animal.adopt.controller;

import com.animal.adopt.common.Result;
import com.animal.adopt.entity.vo.StoryVO;
import com.animal.adopt.service.StoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 故事控制器
 */
@RestController
@RequestMapping("/story")
@CrossOrigin
public class StoryController {

    @Autowired
    private StoryService storyService;

    /**
     * 获取所有故事列表
     */
    @GetMapping("/list")
    public Result<List<StoryVO>> getAllStories() {
        return Result.success(storyService.getAllStories());
    }

    /**
     * 根据ID获取故事详情
     */
    @GetMapping("/{id}")
    public Result<StoryVO> getStoryDetail(@PathVariable Long id, @RequestParam(required = false) Long userId) {
        StoryVO story = storyService.getStoryDetail(id, userId);
        if (story == null) {
            return Result.error("故事不存在");
        }
        
        return Result.success(story);
    }

    /**
     * 点赞故事
     */
    @PostMapping("/{id}/like")
    public Result<Void> likeStory(@PathVariable Long id, @RequestParam Long userId) {
        storyService.likeStory(id, userId);
        
        return Result.success();
    }

    /**
     * 取消点赞故事
     */
    @DeleteMapping("/{id}/like")
    public Result<Void> unlikeStory(@PathVariable Long id, @RequestParam Long userId) {
        storyService.unlikeStory(id, userId);
        
        return Result.success();
    }

    /**
     * 收藏故事
     */
    @PostMapping("/{id}/favorite")
    public Result<Void> favoriteStory(@PathVariable Long id, @RequestParam Long userId) {
        storyService.favoriteStory(id, userId);
        
        return Result.success();
    }

    /**
     * 取消收藏故事
     */
    @DeleteMapping("/{id}/favorite")
    public Result<Void> unfavoriteStory(@PathVariable Long id, @RequestParam Long userId) {
        storyService.unfavoriteStory(id, userId);
        
        return Result.success();
    }

    /**
     * 检查用户是否已点赞故事（需要认证）
     */
    @GetMapping("/{id}/like/check")
    public Result<Boolean> isStoryLiked(@PathVariable Long id) {
        Long userId = cn.dev33.satoken.stp.StpUtil.getLoginIdAsLong();
        boolean liked = storyService.isStoryLiked(id, userId);
        return Result.success(liked);
    }

    /**
     * 检查用户是否已收藏故事（需要认证）
     */
    @GetMapping("/{id}/favorite/check")
    public Result<Boolean> isStoryFavorited(@PathVariable Long id) {
        Long userId = cn.dev33.satoken.stp.StpUtil.getLoginIdAsLong();
        boolean favorited = storyService.isStoryFavorited(id, userId);
        return Result.success(favorited);
    }

    /**
     * 获取故事点赞数量（无需认证）
     */
    @GetMapping("/{id}/like/count")
    public Result<Long> getStoryLikeCount(@PathVariable Long id) {
        StoryVO story = storyService.getStoryDetail(id, null);
        if (story == null) {
            return Result.error("故事不存在");
        }
        return Result.success((long) (story.getLikes() != null ? story.getLikes() : 0));
    }

    /**
     * 获取故事收藏数量（无需认证）
     */
    @GetMapping("/{id}/favorite/count")
    public Result<Long> getStoryFavoriteCount(@PathVariable Long id) {
        StoryVO story = storyService.getStoryDetail(id, null);
        if (story == null) {
            return Result.error("故事不存在");
        }
        return Result.success(0L);  // 故事表中没有收藏计数字段，返回0
    }
}
