package com.animal.adopt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.animal.adopt.entity.po.GuideLike;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 指南点赞Mapper
 */
@Mapper
public interface GuideLikeMapper extends BaseMapper<GuideLike> {
    
    /**
     * 检查用户是否点赞过指南
     */
    @Select("SELECT COUNT(*) FROM t_guide_like WHERE user_id = #{userId} AND guide_id = #{guideId}")
    int checkUserLiked(Long userId, Long guideId);
    
    /**
     * 获取指南的点赞总数
     */
    @Select("SELECT COUNT(*) FROM t_guide_like WHERE guide_id = #{guideId}")
    int getGuideLikeCount(Long guideId);
}
