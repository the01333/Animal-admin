package com.puxinxiaolin.adopt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.puxinxiaolin.adopt.entity.entity.GuideFavorite;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 指南收藏Mapper
 */
@Mapper
public interface GuideFavoriteMapper extends BaseMapper<GuideFavorite> {
    
    /**
     * 检查用户是否收藏过指南
     */
    @Select("SELECT COUNT(*) FROM t_guide_favorite WHERE user_id = #{userId} AND guide_id = #{guideId}")
    int checkUserFavorited(Long userId, Long guideId);

    /**
     * 获取指南收藏总数
     */
    @Select("SELECT COUNT(*) FROM t_guide_favorite WHERE guide_id = #{guideId}")
    int getGuideFavoriteCount(Long guideId);
}
