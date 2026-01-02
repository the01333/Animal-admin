package com.puxinxiaolin.adopt.mapper;

import com.puxinxiaolin.adopt.entity.entity.Pet;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 宠物 Mapper
 */
@Mapper
public interface PetMapper extends BaseMapper<Pet> {
    
    /**
     * 增加浏览次数
     */
    int incrementViewCount(@Param("petId") Long petId, @Param("increment") int increment);
    
    /**
     * 增加点赞次数
     */
    int incrementLikeCount(@Param("petId") Long petId);
    
    /**
     * 减少点赞次数
     */
    int decrementLikeCount(@Param("petId") Long petId);
    
    /**
     * 增加收藏次数
     */
    int incrementFavoriteCount(@Param("petId") Long petId);
    
    /**
     * 减少收藏次数
     */
    int decrementFavoriteCount(@Param("petId") Long petId);
    
    /**
     * 增加申请次数
     */
    int incrementApplicationCount(@Param("petId") Long petId);
    
    /**
     * 查询所有宠物类型（去重）
     */
    List<String> selectDistinctCategories();
    
    /**
     * 查询所有领养状态（去重）
     */
    List<String> selectDistinctAdoptionStatuses();
}
