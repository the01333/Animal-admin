package com.puxinxiaolin.adopt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.puxinxiaolin.adopt.entity.entity.Story;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * 故事Mapper
 */
@Mapper
public interface StoryMapper extends BaseMapper<Story> {

    @Update("UPDATE t_story SET views = views + #{increment} WHERE id = #{id}")
    int incrementViewCount(@Param("id") Long id, @Param("increment") int increment);

    @Update("UPDATE t_story SET likes = likes + #{increment} WHERE id = #{id}")
    int incrementLikeCount(@Param("id") Long id, @Param("increment") int increment);

    @Update("UPDATE t_story SET favorite_count = favorite_count + #{increment} WHERE id = #{id}")
    int incrementFavoriteCount(@Param("id") Long id, @Param("increment") int increment);
}
