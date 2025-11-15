package com.animal.adopt.mapper;

import com.animal.adopt.entity.po.ArticleLike;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 文章点赞Mapper接口
 */
@Mapper
public interface ArticleLikeMapper extends BaseMapper<ArticleLike> {
}
