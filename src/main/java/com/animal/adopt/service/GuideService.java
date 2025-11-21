package com.animal.adopt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.animal.adopt.entity.po.Guide;
import com.animal.adopt.entity.vo.GuideVO;

import java.util.List;

/**
 * 指南服务接口
 */
public interface GuideService extends IService<Guide> {
    
    /**
     * 获取所有指南列表
     */
    List<GuideVO> getAllGuides();
    
    /**
     * 根据ID获取指南详情（需要传入用户ID用于检查点赞状态）
     */
    GuideVO getGuideDetail(Long id, Long userId);
    
    /**
     * 根据分类获取指南列表
     */
    List<GuideVO> getGuidesByCategory(String category);
    
    /**
     * 增加浏览次数
     */
    void increaseViews(Long id);
    
    /**
     * 点赞指南
     */
    void likeGuide(Long guideId, Long userId);
    
    /**
     * 取消点赞指南
     */
    void unlikeGuide(Long guideId, Long userId);
    
    /**
     * 收藏指南
     */
    void favoriteGuide(Long guideId, Long userId);
    
    /**
     * 取消收藏指南
     */
    void unfavoriteGuide(Long guideId, Long userId);
}
