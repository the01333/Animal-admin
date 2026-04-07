package com.puxinxiaolin.adopt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.puxinxiaolin.adopt.entity.entity.Like;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 统一点赞 Mapper 接口
 */
@Mapper
public interface LikeMapper extends BaseMapper<Like> {
    
    /**
     * 查询用户是否点赞
     */
    int isLiked(@Param("userId") Long userId, 
                @Param("targetId") Long targetId, 
                @Param("targetType") String targetType);
    
    /**
     * 获取目标的点赞数
     */
    int getLikeCount(@Param("targetId") Long targetId, 
                     @Param("targetType") String targetType);
    
    /**
     * 获取用户的点赞列表
     */
    List<Like> getUserLikes(@Param("userId") Long userId, 
                           @Param("targetType") String targetType);
}
