package com.animal.adopt.service;

import com.animal.adopt.entity.po.PetLike;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 宠物点赞服务接口
 */
public interface PetLikeService extends IService<PetLike> {
    
    /**
     * 点赞宠物
     * @param userId 用户ID
     * @param petId 宠物ID
     * @return 是否成功
     */
    boolean likePet(Long userId, Long petId);
    
    /**
     * 取消点赞
     * @param userId 用户ID
     * @param petId 宠物ID
     * @return 是否成功
     */
    boolean unlikePet(Long userId, Long petId);
    
    /**
     * 检查是否已点赞
     * @param userId 用户ID
     * @param petId 宠物ID
     * @return 是否已点赞
     */
    boolean isLiked(Long userId, Long petId);
}
