package com.puxinxiaolin.adopt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.puxinxiaolin.adopt.entity.entity.GuideLike;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 指南点赞 Mapper
 */
@Mapper
public interface GuideLikeMapper extends BaseMapper<GuideLike> {
    
    /**
     * 检查用户是否点赞过指南
     */
    int checkUserLiked(@Param("userId") Long userId, @Param("guideId") Long guideId);
    
    /**
     * 获取指南的点赞总数
     */
    int getGuideLikeCount(@Param("guideId") Long guideId);
}
