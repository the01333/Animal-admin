package com.puxinxiaolin.adopt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.puxinxiaolin.adopt.entity.entity.StoryFavorite;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 故事收藏 Mapper
 */
@Mapper
public interface StoryFavoriteMapper extends BaseMapper<StoryFavorite> {
    
    /**
     * 检查用户是否收藏过故事
     */
    int checkUserFavorited(@Param("userId") Long userId, @Param("storyId") Long storyId);

    /**
     * 获取故事收藏总数
     */
    int getStoryFavoriteCount(@Param("storyId") Long storyId);
}
