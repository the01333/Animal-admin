package com.puxinxiaolin.adopt.mapper;

import com.puxinxiaolin.adopt.entity.entity.Favorite;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * 收藏 Mapper
 */
@Mapper
public interface FavoriteMapper extends BaseMapper<Favorite> {
    
    /**
     * 查询收藏记录（包括已删除的），绕过逻辑删除
     */
    @Select("SELECT * FROM t_pet_favorite WHERE user_id = #{userId} AND pet_id = #{petId} LIMIT 1")
    Favorite selectByUserAndPetIgnoreDeleted(@Param("userId") Long userId, @Param("petId") Long petId);
    
    /**
     * 恢复已删除的收藏记录
     */
    @Update("UPDATE t_pet_favorite SET deleted = 0 WHERE id = #{id}")
    int restoreById(@Param("id") Long id);
}

