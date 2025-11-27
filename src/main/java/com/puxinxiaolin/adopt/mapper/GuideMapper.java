package com.puxinxiaolin.adopt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.puxinxiaolin.adopt.entity.entity.Guide;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * 指南Mapper
 */
@Mapper
public interface GuideMapper extends BaseMapper<Guide> {

    @Update("UPDATE t_guide SET views = views + #{increment} WHERE id = #{id}")
    int incrementViewCount(@Param("id") Long id, @Param("increment") int increment);

    @Update("UPDATE t_guide SET like_count = like_count + #{increment} WHERE id = #{id}")
    int incrementLikeCount(@Param("id") Long id, @Param("increment") int increment);

    @Update("UPDATE t_guide SET favorite_count = favorite_count + #{increment} WHERE id = #{id}")
    int incrementFavoriteCount(@Param("id") Long id, @Param("increment") int increment);
}
