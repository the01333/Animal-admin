package com.animal.adopt.mapper;

import com.animal.adopt.entity.po.Pet;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * 宠物 Mapper
 */
@Mapper
public interface PetMapper extends BaseMapper<Pet> {
    
    /**
     * 增加浏览次数
     * 使用 view_count = view_count + ? 避免并发覆盖问题
     * 
     * @param petId 宠物ID
     * @param increment 增量值
     * @return 影响的行数
     */
    @Update("UPDATE t_pet SET view_count = view_count + #{increment} WHERE id = #{petId} AND deleted = 0")
    int incrementViewCount(@Param("petId") Long petId, @Param("increment") int increment);
    
    /**
     * 增加点赞次数
     * 
     * @param petId 宠物ID
     * @return 影响的行数
     */
    @Update("UPDATE t_pet SET like_count = like_count + 1 WHERE id = #{petId} AND deleted = 0")
    int incrementLikeCount(@Param("petId") Long petId);
    
    /**
     * 减少点赞次数
     * 
     * @param petId 宠物ID
     * @return 影响的行数
     */
    @Update("UPDATE t_pet SET like_count = like_count - 1 WHERE id = #{petId} AND deleted = 0 AND like_count > 0")
    int decrementLikeCount(@Param("petId") Long petId);
    
    /**
     * 增加收藏次数
     * 
     * @param petId 宠物ID
     * @return 影响的行数
     */
    @Update("UPDATE t_pet SET favorite_count = favorite_count + 1 WHERE id = #{petId} AND deleted = 0")
    int incrementFavoriteCount(@Param("petId") Long petId);
    
    /**
     * 减少收藏次数
     * 
     * @param petId 宠物ID
     * @return 影响的行数
     */
    @Update("UPDATE t_pet SET favorite_count = favorite_count - 1 WHERE id = #{petId} AND deleted = 0 AND favorite_count > 0")
    int decrementFavoriteCount(@Param("petId") Long petId);
    
    /**
     * 增加申请次数
     * 
     * @param petId 宠物ID
     * @return 影响的行数
     */
    @Update("UPDATE t_pet SET application_count = application_count + 1 WHERE id = #{petId} AND deleted = 0")
    int incrementApplicationCount(@Param("petId") Long petId);
}

