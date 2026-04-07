package com.puxinxiaolin.adopt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.puxinxiaolin.adopt.entity.entity.FavoriteUnified;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 统一收藏 Mapper 接口
 */
@Mapper
public interface FavoriteUnifiedMapper extends BaseMapper<FavoriteUnified> {
    
    /**
     * 查询用户是否收藏
     */
    int isFavorited(@Param("userId") Long userId, 
                    @Param("targetId") Long targetId, 
                    @Param("targetType") String targetType);
    
    /**
     * 获取目标的收藏数
     */
    int getFavoriteCount(@Param("targetId") Long targetId, 
                        @Param("targetType") String targetType);
    
    /**
     * 获取用户的收藏列表
     */
    List<FavoriteUnified> getUserFavorites(@Param("userId") Long userId, 
                                          @Param("targetType") String targetType);
}
