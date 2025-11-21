package com.animal.adopt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.animal.adopt.entity.po.GuideFavorite;
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
}
