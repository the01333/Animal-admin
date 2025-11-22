package com.animal.adopt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.animal.adopt.entity.po.Story;
import com.animal.adopt.entity.po.StoryLike;
import com.animal.adopt.entity.vo.StoryVO;
import com.animal.adopt.mapper.StoryMapper;
import com.animal.adopt.mapper.StoryLikeMapper;
import com.animal.adopt.entity.po.StoryFavorite;
import com.animal.adopt.mapper.StoryFavoriteMapper;
import com.animal.adopt.service.StoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 故事服务实现
 */
@Service
public class StoryServiceImpl extends ServiceImpl<StoryMapper, Story> implements StoryService {
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    @Autowired
    private StoryLikeMapper storyLikeMapper;
    
    @Autowired
    private StoryFavoriteMapper storyFavoriteMapper;
    
    @Autowired
    private OssUrlService ossUrlService;
    
    @Override
    public List<StoryVO> getAllStories() {
        List<Story> stories = this.list();
        
        return stories.stream()
                .map(story -> convertToVO(story, null))
                .collect(Collectors.toList());
    }
    
    @Override
    public StoryVO getStoryDetail(Long id, Long userId) {
        Story story = this.getById(id);
        if (story == null) {
            return null;
        }
        return convertToVO(story, userId);
    }
    
    @Override
    public void likeStory(Long storyId, Long userId) {
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
    public void unlikeStory(Long storyId, Long userId) {
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
    public void favoriteStory(Long storyId, Long userId) {
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
    public void unfavoriteStory(Long storyId, Long userId) {
        LambdaQueryWrapper<StoryFavorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StoryFavorite::getUserId, userId)
               .eq(StoryFavorite::getStoryId, storyId);
        storyFavoriteMapper.delete(wrapper);
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
            vo.setPublishDate(story.getCreatedAt().format(DATE_FORMATTER));
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
}
