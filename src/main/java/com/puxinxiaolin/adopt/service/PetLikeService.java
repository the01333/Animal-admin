package com.puxinxiaolin.adopt.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.puxinxiaolin.adopt.entity.dto.PetLikePageQueryDTO;
import com.puxinxiaolin.adopt.entity.entity.PetLike;
import com.puxinxiaolin.adopt.entity.vo.PetVO;

/**
 * 宠物点赞服务接口
 */
public interface PetLikeService extends IService<PetLike> {

    /**
     * 点赞宠物
     *
     * @param petId 宠物ID
     * @return 是否成功
     */
    boolean likePet(Long petId);

    /**
     * 取消点赞
     *
     * @param petId 宠物ID
     * @return 是否成功
     */
    boolean unlikePet(Long petId);

    /**
     * 检查是否已点赞
     *
     * @param petId 宠物ID
     * @return 是否已点赞
     */
    boolean isLiked(Long petId);

    /**
     * 获取宠物点赞数量（无需认证）
     *
     * @param petId 宠物ID
     * @return 点赞数量
     */
    long getLikeCount(Long petId);

    /**
     * 查询用户点赞的宠物列表
     *
     * @return 点赞宠物列表
     */
    Page<PetVO> queryUserLikedPets(PetLikePageQueryDTO queryDTO);
}
