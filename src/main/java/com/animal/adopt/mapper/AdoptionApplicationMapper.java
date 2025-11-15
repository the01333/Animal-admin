package com.animal.adopt.mapper;

import com.animal.adopt.entity.po.AdoptionApplication;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 领养申请 Mapper
 */
@Mapper
public interface AdoptionApplicationMapper extends BaseMapper<AdoptionApplication> {
}

