package com.puxinxiaolin.adopt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.puxinxiaolin.adopt.entity.entity.GuideFavorite;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 指南收藏 Mapper
 */
@Mapper
public interface GuideFavoriteMapper extends BaseMapper<GuideFavorite> {
    
    /**
     * 检查用户是否收藏过指南
     */
    int checkUserFavorited(@Param("userId") Long userId, @Param("guideId") Long guideId);

    /**
     * 获取指南收藏总数
     */
    int getGuideFavoriteCount(@Param("guideId") Long guideId);
}
