package com.puxinxiaolin.adopt.service;

import com.puxinxiaolin.adopt.entity.dto.FavoritePageQueryDTO;
import com.puxinxiaolin.adopt.entity.entity.Favorite;
import com.puxinxiaolin.adopt.entity.vo.FavoriteVO;
import com.puxinxiaolin.adopt.entity.vo.PetVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 收藏服务接口
 */
public interface FavoriteService extends IService<Favorite> {
    
    /**
     * 添加收藏
     * @param petId 宠物ID
     * @return 是否成功
     */
    boolean addFavorite(Long petId);
    
    /**
     * 取消收藏
     * @param petId 宠物ID
     * @return 是否成功
     */
    boolean removeFavorite(Long petId);
    
    /**
     * 检查是否已收藏
     * @param petId 宠物ID
     * @return 是否已收藏
     */
    boolean isFavorite(Long petId);
    
    /**
     * 查询用户的收藏列表
     * @return 收藏分页数据
     */
    Page<FavoriteVO> queryUserFavorites(FavoritePageQueryDTO queryDTO);
    
    /**
     * 查询用户收藏的宠物列表（包含完整宠物信息）
     * @return 宠物分页数据
     */
    Page<PetVO> queryUserFavoritePets(FavoritePageQueryDTO queryDTO);
    
    /**
     * 获取宠物收藏数量（无需认证）
     * @param petId 宠物ID
     * @return 收藏数量
     */
    long getFavoriteCount(Long petId);
}


