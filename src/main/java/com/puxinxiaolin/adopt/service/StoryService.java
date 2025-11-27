package com.puxinxiaolin.adopt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.puxinxiaolin.adopt.entity.entity.Story;
import com.puxinxiaolin.adopt.entity.vo.StoryVO;

import java.util.List;

/**
 * 故事服务接口
 */
public interface StoryService extends IService<Story> {
    
    /**
     * 获取所有故事列表
     */
    List<StoryVO> getAllStories();
    
    /**
     * 根据ID获取故事详情（需要传入用户ID用于检查点赞状态）
     */
    StoryVO getStoryDetail(Long id, Long userId);
    
    /**
     * 点赞故事
     */
    void likeStory(Long storyId, Long userId);
    
    /**
     * 取消点赞故事
     */
    void unlikeStory(Long storyId, Long userId);
    
    /**
     * 收藏故事
     */
    void favoriteStory(Long storyId, Long userId);
    
    /**
     * 取消收藏故事
     */
    void unfavoriteStory(Long storyId, Long userId);
    
    /**
     * 检查用户是否已点赞故事
     */
    boolean isStoryLiked(Long storyId, Long userId);
    
    /**
     * 检查用户是否已收藏故事
     */
    boolean isStoryFavorited(Long storyId, Long userId);
}
