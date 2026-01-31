package com.puxinxiaolin.adopt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.puxinxiaolin.adopt.entity.entity.Guide;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 指南 Mapper
 */
@Mapper
public interface GuideMapper extends BaseMapper<Guide> {

    /**
     * 增加浏览次数
     */
    int incrementViewCount(@Param("id") Long id, @Param("increment") int increment);

    /**
     * 增加点赞次数
     */
    int incrementLikeCount(@Param("id") Long id, @Param("increment") int increment);

    /**
     * 增加收藏次数
     */
    int incrementFavoriteCount(@Param("id") Long id, @Param("increment") int increment);
}
