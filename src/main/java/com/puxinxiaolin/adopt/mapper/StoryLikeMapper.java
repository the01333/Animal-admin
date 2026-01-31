package com.puxinxiaolin.adopt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.puxinxiaolin.adopt.entity.entity.StoryLike;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 故事点赞 Mapper
 */
@Mapper
public interface StoryLikeMapper extends BaseMapper<StoryLike> {
    
    /**
     * 检查用户是否点赞过故事
     */
    int checkUserLiked(@Param("userId") Long userId, @Param("storyId") Long storyId);
    
    /**
     * 获取故事的点赞总数
     */
    int getStoryLikeCount(@Param("storyId") Long storyId);
}
