package com.puxinxiaolin.adopt.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.puxinxiaolin.adopt.constants.DateConstant;
import com.puxinxiaolin.adopt.entity.entity.Story;
import com.puxinxiaolin.adopt.entity.entity.StoryFavorite;
import com.puxinxiaolin.adopt.entity.entity.StoryLike;
import com.puxinxiaolin.adopt.entity.vo.DictItemVO;
import com.puxinxiaolin.adopt.entity.vo.StoryVO;
import com.puxinxiaolin.adopt.mapper.StoryFavoriteMapper;
import com.puxinxiaolin.adopt.mapper.StoryLikeMapper;
import com.puxinxiaolin.adopt.mapper.StoryMapper;
import com.puxinxiaolin.adopt.service.DictService;
import com.puxinxiaolin.adopt.service.StoryService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: 故事服务实现
 * @Author: YCcLin
 * @Date: 2025/11/28 20:54
 */
@Slf4j
@Service
public class StoryServiceImpl extends ServiceImpl<StoryMapper, Story> implements StoryService {
    @Autowired
    private StoryLikeMapper storyLikeMapper;
    @Autowired
    private StoryFavoriteMapper storyFavoriteMapper;
    @Autowired
    private OssUrlService ossUrlService;
    @Autowired
    private DictService dictService;

    @Override
    public List<StoryVO> getAllStories() {
        List<Story> stories = this.list();

        return stories.stream()
                .map(story -> convertToVO(story, null))
                .collect(Collectors.toList());
    }

    @Override
    public StoryVO getStoryDetail(Long id) {
        Long userId = StpUtil.isLogin() ? StpUtil.getLoginIdAsLong() : null;
        Story story = this.getById(id);
        if (story == null) {
            return null;
        }
        return convertToVO(story, userId);
    }

    @Override
    public void likeStory(Long storyId) {
        Long userId = StpUtil.getLoginIdAsLong();
        // 检查是否已经点赞
        int count = storyLikeMapper.checkUserLiked(userId, storyId);
        if (count == 0) {
            StoryLike storyLike = new StoryLike();
            storyLike.setUserId(userId);
            storyLike.setStoryId(storyId);
            storyLikeMapper.insert(storyLike);

            // 更新故事表中的点赞数
            Story story = this.getById(storyId);
            if (story != null) {
                story.setLikes(story.getLikes() + 1);
                this.updateById(story);
            }
        }
    }

    @Override
    public void unlikeStory(Long storyId) {
        Long userId = StpUtil.getLoginIdAsLong();
        LambdaQueryWrapper<StoryLike> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StoryLike::getUserId, userId)
                .eq(StoryLike::getStoryId, storyId);
        int deleted = storyLikeMapper.delete(wrapper);

        // 如果成功删除了点赞记录, 则更新故事表中的点赞数
        if (deleted > 0) {
            Story story = this.getById(storyId);
            if (story != null && story.getLikes() > 0) {
                story.setLikes(story.getLikes() - 1);
                this.updateById(story);
            }
        }
    }

    @Override
    public void favoriteStory(Long storyId) {
        Long userId = StpUtil.getLoginIdAsLong();
        // 检查是否已经收藏
        int count = storyFavoriteMapper.checkUserFavorited(userId, storyId);
        if (count == 0) {
            StoryFavorite storyFavorite = new StoryFavorite();
            storyFavorite.setUserId(userId);
            storyFavorite.setStoryId(storyId);
            storyFavoriteMapper.insert(storyFavorite);
        }
    }

    @Override
    public void unfavoriteStory(Long storyId) {
        Long userId = StpUtil.getLoginIdAsLong();
        LambdaQueryWrapper<StoryFavorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StoryFavorite::getUserId, userId)
                .eq(StoryFavorite::getStoryId, storyId);
        storyFavoriteMapper.delete(wrapper);
    }

    @Override
    public boolean isStoryLiked(Long storyId) {
        Long userId = StpUtil.getLoginIdAsLong();
        int count = storyLikeMapper.checkUserLiked(userId, storyId);
        return count > 0;
    }

    @Override
    public boolean isStoryFavorited(Long storyId) {
        Long userId = StpUtil.getLoginIdAsLong();
        int count = storyFavoriteMapper.checkUserFavorited(userId, storyId);
        return count > 0;
    }

    /**
     * 将Story转换为StoryVO
     */
    private StoryVO convertToVO(Story story, Long userId) {
        StoryVO vo = new StoryVO();
        vo.setId(story.getId());
        vo.setTitle(story.getTitle());
        vo.setExcerpt(story.getExcerpt());
        vo.setContent(story.getContent());
        // 处理图片URL
        vo.setImage(ossUrlService.normalizeUrl(story.getImage()));
        vo.setAuthor(story.getAuthor());

        // 解析标签
        if (story.getTags() != null && !story.getTags().isEmpty()) {
            List<String> tags = Arrays.stream(story.getTags().split(","))
                    .map(String::trim)
                    .collect(Collectors.toList());
            vo.setTags(tags);
        }

        vo.setLikes(story.getLikes());
        if (story.getCreatedAt() != null) {
            vo.setPublishDate(story.getCreatedAt().format(DateConstant.Y_M_D));
        }

        // 设置点赞信息
        if (userId != null) {
            int isLiked = storyLikeMapper.checkUserLiked(userId, story.getId());
            vo.setLiked(isLiked > 0);
        } else {
            vo.setLiked(false);
        }

        // 设置收藏信息
        if (userId != null) {
            int isFavorited = storyFavoriteMapper.checkUserFavorited(userId, story.getId());
            vo.setFavorited(isFavorited > 0);
        } else {
            vo.setFavorited(false);
        }

        return vo;
    }

    @Override
    public List<String> getAllCategories() {
        // 1. 优先从通用字典中读取故事标签（story_tag）
        List<DictItemVO> dictItems = dictService.listDictItems("story_tag");
        if (dictItems != null && !dictItems.isEmpty()) {
            return dictItems.stream()
                    .map(DictItemVO::getDictKey)
                    .filter(tag -> tag != null && !tag.isEmpty())
                    .distinct()
                    .collect(Collectors.toList());
        }

        // 2. 字典为空时, 回退到从故事表中提取不重复标签
        List<Story> stories = this.list();
        log.info("获取故事分类, 总故事数: {}", stories.size());

        List<String> categories = stories.stream()
                .map(Story::getTags)
                .filter(tags -> tags != null && !tags.isEmpty())
                .flatMap(tags -> Arrays.stream(tags.split(","))
                        .map(String::trim)
                        .filter(tag -> !tag.isEmpty()))
                .distinct()
                .collect(Collectors.toList());

        log.info("获取到的故事分类: {}", categories);
        return categories;
    }
}
