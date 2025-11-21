package com.animal.adopt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.animal.adopt.entity.po.StoryFavorite;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 故事收藏Mapper
 */
@Mapper
public interface StoryFavoriteMapper extends BaseMapper<StoryFavorite> {
    
    /**
     * 检查用户是否收藏过故事
     */
    @Select("SELECT COUNT(*) FROM t_story_favorite WHERE user_id = #{userId} AND story_id = #{storyId}")
    int checkUserFavorited(Long userId, Long storyId);
}
