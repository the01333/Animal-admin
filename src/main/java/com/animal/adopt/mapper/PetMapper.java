package com.animal.adopt.mapper;

import com.animal.adopt.entity.Pet;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 宠物 Mapper
 */
@Mapper
public interface PetMapper extends BaseMapper<Pet> {
}

