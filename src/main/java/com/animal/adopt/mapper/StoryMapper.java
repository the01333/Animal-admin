package com.animal.adopt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.animal.adopt.entity.po.Story;
import org.apache.ibatis.annotations.Mapper;

/**
 * 故事Mapper
 */
@Mapper
public interface StoryMapper extends BaseMapper<Story> {
}
