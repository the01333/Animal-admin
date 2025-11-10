package com.animal.adopt.mapper;

import com.animal.adopt.entity.po.PetLike;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 宠物点赞Mapper接口
 */
@Mapper
public interface PetLikeMapper extends BaseMapper<PetLike> {
}
