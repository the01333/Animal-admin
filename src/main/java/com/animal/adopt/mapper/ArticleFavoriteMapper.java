package com.animal.adopt.mapper;

import com.animal.adopt.entity.po.ArticleFavorite;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 文章收藏Mapper接口
 */
@Mapper
public interface ArticleFavoriteMapper extends BaseMapper<ArticleFavorite> {
}
