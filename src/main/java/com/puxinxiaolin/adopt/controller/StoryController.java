package com.puxinxiaolin.adopt.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.puxinxiaolin.adopt.common.Result;
import com.puxinxiaolin.adopt.constants.MessageConstants;
import com.puxinxiaolin.adopt.entity.vo.StoryVO;
import com.puxinxiaolin.adopt.service.StoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 故事控制器
 */
@Slf4j
@RestController
@RequestMapping("/story")
@CrossOrigin
@RequiredArgsConstructor
public class StoryController {

    private final StoryService storyService;

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
            return Result.error(MessageConstants.STORY_NOT_FOUND);
        }
        
        return Result.success(story);
    }

    /**
     * 点赞故事
     */
    @PostMapping("/{id}/like")
    public Result<Boolean> likeStory(@PathVariable Long id) {
        Long userId = StpUtil.getLoginIdAsLong();
        storyService.likeStory(id, userId);
        return Result.success(true);
    }

    /**
     * 取消点赞故事
     */
    @DeleteMapping("/{id}/like")
    public Result<Boolean> unlikeStory(@PathVariable Long id) {
        Long userId = StpUtil.getLoginIdAsLong();
        storyService.unlikeStory(id, userId);
        return Result.success(true);
    }

    /**
     * 收藏故事
     */
    @PostMapping("/{id}/favorite")
    public Result<Boolean> favoriteStory(@PathVariable Long id) {
        Long userId = StpUtil.getLoginIdAsLong();
        storyService.favoriteStory(id, userId);
        return Result.success(true);
    }

    /**
     * 取消收藏故事
     */
    @DeleteMapping("/{id}/favorite")
    public Result<Boolean> unfavoriteStory(@PathVariable Long id) {
        Long userId = StpUtil.getLoginIdAsLong();
        storyService.unfavoriteStory(id, userId);
        return Result.success(true);
    }

    /**
     * 检查用户是否已点赞故事（需要认证）
     */
    @GetMapping("/{id}/like/check")
    public Result<Boolean> isStoryLiked(@PathVariable Long id) {
        Long userId = StpUtil.getLoginIdAsLong();
        boolean liked = storyService.isStoryLiked(id, userId);
        return Result.success(liked);
    }

    /**
     * 检查用户是否已收藏故事（需要认证）
     */
    @GetMapping("/{id}/favorite/check")
    public Result<Boolean> isStoryFavorited(@PathVariable Long id) {
        Long userId = StpUtil.getLoginIdAsLong();
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
            return Result.error(MessageConstants.STORY_NOT_FOUND);
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
            return Result.error(MessageConstants.STORY_NOT_FOUND);
        }
        return Result.success(0L);  // 故事表中没有收藏计数字段, 返回0
    }

    /**
     * 获取所有故事分类
     */
    @GetMapping("/categories/list")
    public Result<List<String>> getCategories() {
        List<String> categories = storyService.getAllCategories();
        return Result.success(categories);
    }
}
