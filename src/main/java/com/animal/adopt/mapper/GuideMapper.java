package com.animal.adopt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.animal.adopt.entity.po.Guide;
import org.apache.ibatis.annotations.Mapper;

/**
 * 指南Mapper
 */
@Mapper
public interface GuideMapper extends BaseMapper<Guide> {
}
