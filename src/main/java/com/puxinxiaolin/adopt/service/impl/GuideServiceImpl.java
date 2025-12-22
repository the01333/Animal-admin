package com.puxinxiaolin.adopt.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.puxinxiaolin.adopt.constants.DateConstant;
import com.puxinxiaolin.adopt.entity.entity.Guide;
import com.puxinxiaolin.adopt.entity.entity.GuideFavorite;
import com.puxinxiaolin.adopt.entity.entity.GuideLike;
import com.puxinxiaolin.adopt.entity.vo.DictItemVO;
import com.puxinxiaolin.adopt.entity.vo.GuideVO;
import com.puxinxiaolin.adopt.mapper.GuideFavoriteMapper;
import com.puxinxiaolin.adopt.mapper.GuideLikeMapper;
import com.puxinxiaolin.adopt.mapper.GuideMapper;
import com.puxinxiaolin.adopt.service.DictService;
import com.puxinxiaolin.adopt.service.GuideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 指南服务实现
 */
@Service
public class GuideServiceImpl extends ServiceImpl<GuideMapper, Guide> implements GuideService {
    
    @Autowired
    private GuideLikeMapper guideLikeMapper;
    
    @Autowired
    private GuideFavoriteMapper guideFavoriteMapper;
    
    @Autowired
    private OssUrlService ossUrlService;
    
    @Autowired
    private DictService dictService;

    @Override
    public List<GuideVO> getAllGuides() {
        List<Guide> guides = this.list();
        
        return guides.stream()
                .map(guide -> convertToVO(guide, null))
                .collect(Collectors.toList());
    }

    @Override
    public GuideVO getGuideDetail(Long id) {
        Long userId = StpUtil.isLogin() ? StpUtil.getLoginIdAsLong() : null;
        Guide guide = this.getById(id);
        if (guide == null) {
            return null;
        }
        // 查看详情时增加浏览次数
        increaseViews(id);
        return convertToVO(guide, userId);
    }

    @Override
    public List<GuideVO> getGuidesByCategory(String category) {
        LambdaQueryWrapper<Guide> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Guide::getCategory, category);
        List<Guide> guides = this.list(wrapper);
        return guides.stream()
                .map(guide -> convertToVO(guide, null))
                .collect(Collectors.toList());
    }

    @Override
    public void increaseViews(Long id) {
        Guide guide = this.getById(id);
        if (guide != null) {
            guide.setViews(guide.getViews() + 1);
            this.updateById(guide);
        }
    }

    /**
     * 点赞指南
     */
    public void likeGuide(Long guideId) {
        Long userId = StpUtil.getLoginIdAsLong();
        // 检查是否已经点赞
        int count = guideLikeMapper.checkUserLiked(userId, guideId);
        if (count == 0) {
            GuideLike guideLike = new GuideLike();
            guideLike.setUserId(userId);
            guideLike.setGuideId(guideId);
            guideLikeMapper.insert(guideLike);

            // 注意: 指南表中没有likes字段, 只有likeCount通过查询t_guide_like表计算
            // 所以这里不需要更新指南表
        }
    }

    /**
     * 取消点赞指南
     */
    public void unlikeGuide(Long guideId) {
        Long userId = StpUtil.getLoginIdAsLong();
        LambdaQueryWrapper<GuideLike> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(GuideLike::getUserId, userId)
                .eq(GuideLike::getGuideId, guideId);
        guideLikeMapper.delete(wrapper);

        // 注意: 指南表中没有likes字段, 只有likeCount通过查询t_guide_like表计算
        // 所以这里不需要更新指南表
    }

    /**
     * 收藏指南
     */
    public void favoriteGuide(Long guideId) {
        Long userId = StpUtil.getLoginIdAsLong();
        // 检查是否已经收藏
        int count = guideFavoriteMapper.checkUserFavorited(userId, guideId);
        if (count == 0) {
            GuideFavorite guideFavorite = new GuideFavorite();
            guideFavorite.setUserId(userId);
            guideFavorite.setGuideId(guideId);
            guideFavoriteMapper.insert(guideFavorite);
        }
    }

    /**
     * 取消收藏指南
     */
    public void unfavoriteGuide(Long guideId) {
        Long userId = StpUtil.getLoginIdAsLong();
        LambdaQueryWrapper<GuideFavorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(GuideFavorite::getUserId, userId)
                .eq(GuideFavorite::getGuideId, guideId);
        guideFavoriteMapper.delete(wrapper);
    }

    @Override
    public boolean isGuideLiked(Long guideId) {
        Long userId = StpUtil.getLoginIdAsLong();
        int count = guideLikeMapper.checkUserLiked(userId, guideId);
        return count > 0;
    }

    /**
     * 检查用户是否已收藏指南
     */
    public boolean isGuideFavorited(Long guideId) {
        Long userId = StpUtil.getLoginIdAsLong();
        int count = guideFavoriteMapper.checkUserFavorited(userId, guideId);
        return count > 0;
    }

    /**
     * 将 Guide 转换为 GuideVO
     *
     * @param guide
     * @param userId
     * @return
     */
    private GuideVO convertToVO(Guide guide, Long userId) {
        GuideVO vo = new GuideVO();
        vo.setId(guide.getId());
        vo.setTitle(guide.getTitle());
        vo.setExcerpt(guide.getExcerpt());
        vo.setContent(guide.getContent());
        // 处理图片URL
        vo.setImage(ossUrlService.normalizeUrl(guide.getImage()));
        vo.setCategory(guide.getCategory());
        vo.setViews(guide.getViews());
        if (guide.getCreatedAt() != null) {
            vo.setPublishDate(guide.getCreatedAt().format(DateConstant.Y_M_D));
        }

        // 设置点赞信息
        if (userId != null) {
            int isLiked = guideLikeMapper.checkUserLiked(userId, guide.getId());
            vo.setLiked(isLiked > 0);
        } else {
            vo.setLiked(false);
        }

        
        // 获取点赞总数
        int likeCount = guideLikeMapper.getGuideLikeCount(guide.getId());
        vo.setLikeCount(likeCount);

        // 设置收藏信息
        if (userId != null) {
            int isFavorited = guideFavoriteMapper.checkUserFavorited(userId, guide.getId());
            vo.setFavorited(isFavorited > 0);
        } else {
            vo.setFavorited(false);
        }

        return vo;
    }

    @Override
    public List<String> getAllCategories() {
        // 1. 优先从通用字典读取指南分类（guide_category）
        List<DictItemVO> dictItems = dictService.listDictItems("guide_category");
        if (dictItems != null && !dictItems.isEmpty()) {
            return dictItems.stream()
                    .map(DictItemVO::getDictKey)
                    .filter(category -> category != null && !category.isEmpty())
                    .distinct()
                    .collect(Collectors.toList());
        }

        // 2. 字典为空时, 回退到从指南表中提取不重复分类
        List<Guide> guides = this.list();
        return guides.stream()
                .map(Guide::getCategory)
                .filter(category -> category != null && !category.isEmpty())
                .distinct()
                .collect(Collectors.toList());
    }
}
