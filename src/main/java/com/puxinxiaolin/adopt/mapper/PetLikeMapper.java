package com.puxinxiaolin.adopt.mapper;

import com.puxinxiaolin.adopt.entity.entity.PetLike;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * 宠物点赞Mapper接口
 */
@Mapper
public interface PetLikeMapper extends BaseMapper<PetLike> {
    
    /**
     * 查询点赞记录（包括已删除的），绕过逻辑删除
     */
    @Select("SELECT * FROM t_pet_like WHERE user_id = #{userId} AND pet_id = #{petId} LIMIT 1")
    PetLike selectByUserAndPetIgnoreDeleted(@Param("userId") Long userId, @Param("petId") Long petId);
    
    /**
     * 恢复已删除的点赞记录
     */
    @Update("UPDATE t_pet_like SET deleted = 0 WHERE id = #{id}")
    int restoreById(@Param("id") Long id);
}
