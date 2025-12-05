package com.puxinxiaolin.adopt.service.impl;

import com.puxinxiaolin.adopt.constants.DateConstant;
import com.puxinxiaolin.adopt.entity.entity.Guide;
import com.puxinxiaolin.adopt.entity.entity.GuideFavorite;
import com.puxinxiaolin.adopt.entity.entity.GuideLike;
import com.puxinxiaolin.adopt.entity.vo.GuideVO;
import com.puxinxiaolin.adopt.mapper.GuideFavoriteMapper;
import com.puxinxiaolin.adopt.mapper.GuideLikeMapper;
import com.puxinxiaolin.adopt.mapper.GuideMapper;
import com.puxinxiaolin.adopt.service.GuideService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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

    @Override
    public List<GuideVO> getAllGuides() {
        List<Guide> guides = this.list();
        
        return guides.stream()
                .map(guide -> convertToVO(guide, null))
                .collect(Collectors.toList());
    }

    @Override
    public GuideVO getGuideDetail(Long id, Long userId) {
        Guide guide = this.getById(id);
        if (guide == null) {
            return null;
        }

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

    @Override
    public void likeGuide(Long guideId, Long userId) {
        // 检查是否已经点赞
        int count = guideLikeMapper.checkUserLiked(userId, guideId);
        if (count == 0) {
            GuideLike guideLike = new GuideLike();
            guideLike.setUserId(userId);
            guideLike.setGuideId(guideId);
            guideLikeMapper.insert(guideLike);

            // 注意：指南表中没有likes字段, 只有likeCount通过查询t_guide_like表计算
            // 所以这里不需要更新指南表
        }
    }

    @Override
    public void unlikeGuide(Long guideId, Long userId) {
        LambdaQueryWrapper<GuideLike> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(GuideLike::getUserId, userId)
                .eq(GuideLike::getGuideId, guideId);
        guideLikeMapper.delete(wrapper);

        // 注意：指南表中没有likes字段, 只有likeCount通过查询t_guide_like表计算
        // 所以这里不需要更新指南表
    }

    @Override
    public void favoriteGuide(Long guideId, Long userId) {
        // 检查是否已经收藏
        int count = guideFavoriteMapper.checkUserFavorited(userId, guideId);
        if (count == 0) {
            GuideFavorite guideFavorite = new GuideFavorite();
            guideFavorite.setUserId(userId);
            guideFavorite.setGuideId(guideId);
            guideFavoriteMapper.insert(guideFavorite);
        }
    }

    @Override
    public void unfavoriteGuide(Long guideId, Long userId) {
        LambdaQueryWrapper<GuideFavorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(GuideFavorite::getUserId, userId)
                .eq(GuideFavorite::getGuideId, guideId);
        guideFavoriteMapper.delete(wrapper);
    }

    @Override
    public boolean isGuideLiked(Long guideId, Long userId) {
        int count = guideLikeMapper.checkUserLiked(userId, guideId);
        return count > 0;
    }

    @Override
    public boolean isGuideFavorited(Long guideId, Long userId) {
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
        // 查询所有指南, 提取不重复的分类
        List<Guide> guides = this.list();
        return guides.stream()
                .map(Guide::getCategory)
                .distinct()
                .collect(Collectors.toList());
    }
}
