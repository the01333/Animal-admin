package com.animal.adopt.mapper;

import com.animal.adopt.entity.po.PetFavorite;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 宠物收藏Mapper接口
 */
@Mapper
public interface PetFavoriteMapper extends BaseMapper<PetFavorite> {
}
