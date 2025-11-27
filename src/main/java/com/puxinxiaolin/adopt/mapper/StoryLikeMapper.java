package com.puxinxiaolin.adopt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.puxinxiaolin.adopt.entity.entity.StoryLike;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 故事点赞Mapper
 */
@Mapper
public interface StoryLikeMapper extends BaseMapper<StoryLike> {
    
    /**
     * 检查用户是否点赞过故事
     */
    @Select("SELECT COUNT(*) FROM t_story_like WHERE user_id = #{userId} AND story_id = #{storyId}")
    int checkUserLiked(Long userId, Long storyId);
    
    /**
     * 获取故事的点赞总数
     */
    @Select("SELECT COUNT(*) FROM t_story_like WHERE story_id = #{storyId}")
    int getStoryLikeCount(Long storyId);
}
