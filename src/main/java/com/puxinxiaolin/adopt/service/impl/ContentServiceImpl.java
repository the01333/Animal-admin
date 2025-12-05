package com.puxinxiaolin.adopt.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.puxinxiaolin.adopt.common.ResultCode;
import com.puxinxiaolin.adopt.constants.DateConstant;
import com.puxinxiaolin.adopt.constants.MessageConstant;
import com.puxinxiaolin.adopt.entity.dto.ContentDTO;
import com.puxinxiaolin.adopt.entity.dto.ContentQueryDTO;
import com.puxinxiaolin.adopt.entity.dto.ContentUserQueryDTO;
import com.puxinxiaolin.adopt.entity.entity.*;
import com.puxinxiaolin.adopt.entity.vo.ContentCategoryVO;
import com.puxinxiaolin.adopt.entity.vo.ContentVO;
import com.puxinxiaolin.adopt.enums.ContentCategoryEnum;
import com.puxinxiaolin.adopt.exception.BizException;
import com.puxinxiaolin.adopt.mapper.*;
import com.puxinxiaolin.adopt.service.ContentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.function.Function;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContentServiceImpl implements ContentService {
    private final GuideMapper guideMapper;
    private final StoryMapper storyMapper;
    private final GuideLikeMapper guideLikeMapper;
    private final GuideFavoriteMapper guideFavoriteMapper;
    private final StoryLikeMapper storyLikeMapper;
    private final StoryFavoriteMapper storyFavoriteMapper;
    private final OssUrlService ossUrlService;
    private final ViewCountService viewCountService;

    private static final List<ContentCategoryVO> CATEGORY_OPTIONS = List.of(
            new ContentCategoryVO(ContentCategoryEnum.GUIDE.name(), ContentCategoryEnum.GUIDE.getLabel()),
            new ContentCategoryVO(ContentCategoryEnum.STORY.name(), ContentCategoryEnum.STORY.getLabel())
    );

    @Override
    public Page<ContentVO> queryContentPage(ContentQueryDTO queryDTO) {
        long current = queryDTO.getCurrent() == null || queryDTO.getCurrent() <= 0 ? 1L : queryDTO.getCurrent();
        long size = queryDTO.getSize() == null || queryDTO.getSize() <= 0 ? 10L : queryDTO.getSize();

        List<ContentVO> merged = new ArrayList<>();
        boolean includeGuide = StrUtil.isBlank(queryDTO.getCategory()) || ContentCategoryEnum.GUIDE.name().equalsIgnoreCase(queryDTO.getCategory());
        boolean includeStory = StrUtil.isBlank(queryDTO.getCategory()) || ContentCategoryEnum.STORY.name().equalsIgnoreCase(queryDTO.getCategory());

        if (includeGuide) {
            merged.addAll(loadGuideContent(queryDTO));
        }
        if (includeStory) {
            merged.addAll(loadStoryContent(queryDTO));
        }

        merged.sort(Comparator.comparing(ContentVO::getPublishTime, Comparator.nullsLast(LocalDateTime::compareTo)).reversed());

        Page<ContentVO> page = new Page<>(current, size);
        page.setTotal(merged.size());
        long fromIndex = Math.min((current - 1) * size, merged.size());
        long toIndex = Math.min(fromIndex + size, merged.size());
        page.setRecords(merged.subList((int) fromIndex, (int) toIndex));
        return page;
    }

    @Override
    public ContentVO getContentDetail(String category, Long id, Long userId) {
        ContentCategoryEnum contentCategoryEnum = ContentCategoryEnum.fromCode(category);
        viewCountService.incrementContentView(contentCategoryEnum, id);
        return switch (contentCategoryEnum) {
            case GUIDE -> toContentVO(requireGuide(id), userId);
            case STORY -> toContentVO(requireStory(id), userId);
        };
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ContentVO createContent(ContentDTO dto, Long userId) {
        ContentCategoryEnum contentCategoryEnum = ContentCategoryEnum.fromCode(dto.getCategory());
        if (contentCategoryEnum == ContentCategoryEnum.GUIDE) {
            Guide guide = applyGuideFields(dto, null);
            guideMapper.insert(guide);
            return toContentVO(guide, null);
        }
        Story story = applyStoryFields(dto, null);
        storyMapper.insert(story);
        return toContentVO(story, null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ContentVO updateContent(Long id, ContentDTO dto) {
        ContentCategoryEnum contentCategoryEnum = ContentCategoryEnum.fromCode(dto.getCategory());
        if (contentCategoryEnum == ContentCategoryEnum.GUIDE) {
            Guide guide = applyGuideFields(dto, requireGuide(id));
            guideMapper.updateById(guide);
            return toContentVO(guide, null);
        }
        Story story = applyStoryFields(dto, requireStory(id));
        storyMapper.updateById(story);
        return toContentVO(story, null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteContent(String category, Long id) {
        ContentCategoryEnum contentCategoryEnum = ContentCategoryEnum.fromCode(category);
        int affected;
        if (contentCategoryEnum == ContentCategoryEnum.GUIDE) {
            affected = guideMapper.deleteById(id);
            if (affected == 0) {
                throw new BizException(ResultCode.NOT_FOUND.getCode(), MessageConstant.GUIDE_NOT_FOUND);
            }
        } else {
            affected = storyMapper.deleteById(id);
            if (affected == 0) {
                throw new BizException(ResultCode.NOT_FOUND.getCode(), MessageConstant.STORY_NOT_FOUND);
            }
        }
    }

    @Override
    public List<ContentCategoryVO> listCategories() {
        return CATEGORY_OPTIONS;
    }

    private List<ContentVO> loadGuideContent(ContentQueryDTO queryDTO) {
        LambdaQueryWrapper<Guide> wrapper = new LambdaQueryWrapper<>();
        if (StrUtil.isNotBlank(queryDTO.getKeyword())) {
            wrapper.and(w -> w.like(Guide::getCategory, queryDTO.getKeyword()));
        }

        List<Guide> guides = guideMapper.selectList(wrapper);
        return guides.stream()
                .map(guide -> toContentVO(guide, null))
                .toList();
    }

    private List<ContentVO> loadStoryContent(ContentQueryDTO queryDTO) {
        LambdaQueryWrapper<Story> wrapper = new LambdaQueryWrapper<>();
        if (StrUtil.isNotBlank(queryDTO.getKeyword())) {
            wrapper.and(w -> w.like(Story::getTitle, queryDTO.getKeyword()));
        }

        List<Story> stories = storyMapper.selectList(wrapper);
        return stories.stream()
                .map(story -> toContentVO(story, null))
                .toList();
    }

    private Guide applyGuideFields(ContentDTO dto, Guide source) {
        String guideCategory = StrUtil.emptyToDefault(dto.getGuideCategory(), null);
        if (StrUtil.isBlank(guideCategory)) {
            throw new BizException(ResultCode.PARAM_ERROR.getCode(), "指南分类不能为空");
        }

        Guide guide = source != null ? source : new Guide();
        guide.setTitle(dto.getTitle());
        guide.setExcerpt(dto.getSummary());
        guide.setContent(dto.getContent());
        guide.setImage(dto.getCoverImage());
        guide.setCategory(guideCategory);
        if (source == null) {
            guide.setViews(0);
            guide.setLikeCount(0);
            guide.setFavoriteCount(0);
            guide.setCreatedAt(LocalDateTime.now());
        }
        guide.setUpdatedAt(LocalDateTime.now());
        return guide;
    }

    private Story applyStoryFields(ContentDTO dto, Story source) {
        if (StrUtil.isBlank(dto.getAuthor())) {
            throw new BizException(ResultCode.PARAM_ERROR.getCode(), "故事作者不能为空");
        }

        Story story = source != null ? source : new Story();
        story.setTitle(dto.getTitle());
        story.setExcerpt(dto.getSummary());
        story.setContent(dto.getContent());
        story.setImage(dto.getCoverImage());
        story.setAuthor(dto.getAuthor());
        story.setTags(CollUtil.isEmpty(dto.getTags()) ? null : String.join(",", dto.getTags()));
        if (source == null) {
            story.setLikes(0);
            story.setFavoriteCount(0);
            story.setCreatedAt(LocalDateTime.now());
        }
        story.setUpdatedAt(LocalDateTime.now());
        return story;
    }

    /**
     * Entity -> VO
     *
     * @param guide
     * @param userId
     * @return
     */
    private ContentVO toContentVO(Guide guide, Long userId) {
        if (guide == null) {
            return null;
        }

        ContentVO vo = new ContentVO();
        vo.setId(guide.getId());
        vo.setCategory(ContentCategoryEnum.GUIDE.name());
        vo.setTitle(guide.getTitle());
        vo.setSummary(guide.getExcerpt());
        vo.setContent(guide.getContent());
        vo.setCoverImage(ossUrlService.normalizeUrl(guide.getImage()));
        vo.setGuideCategory(guide.getCategory());
        vo.setViewCount(calculateViewCount(ContentCategoryEnum.GUIDE, guide.getId(), guide.getViews()));
        vo.setLikeCount(calculateLikeCount(ContentCategoryEnum.GUIDE, guide.getId(), guide.getLikeCount()));
        vo.setFavoriteCount(calculateFavoriteCount(ContentCategoryEnum.GUIDE, guide.getId(), guide.getFavoriteCount()));
        if (userId != null) {
            vo.setLiked(guideLikeMapper.checkUserLiked(userId, guide.getId()) > 0);
            vo.setFavorited(guideFavoriteMapper.checkUserFavorited(userId, guide.getId()) > 0);
        } else {
            vo.setLiked(false);
            vo.setFavorited(false);
        }
        vo.setPublishTime(guide.getCreatedAt());
        if (guide.getCreatedAt() != null) {
            vo.setPublishDate(guide.getCreatedAt().format(DateConstant.Y_M_D));
        }

        return vo;
    }

    private ContentVO toContentVO(Story story, Long userId) {
        if (story == null) {
            return null;
        }
        ContentVO vo = new ContentVO();
        vo.setId(story.getId());
        vo.setCategory(ContentCategoryEnum.STORY.name());
        vo.setTitle(story.getTitle());
        vo.setSummary(story.getExcerpt());
        vo.setContent(story.getContent());
        vo.setCoverImage(ossUrlService.normalizeUrl(story.getImage()));
        vo.setAuthor(story.getAuthor());
        if (StrUtil.isNotBlank(story.getTags())) {
            vo.setTags(new ArrayList<>(StrUtil.splitTrim(story.getTags(), ',')));
        }
        vo.setLikeCount(calculateLikeCount(ContentCategoryEnum.STORY, story.getId(), story.getLikes()));
        vo.setFavoriteCount(calculateFavoriteCount(ContentCategoryEnum.STORY, story.getId(), story.getFavoriteCount()));
        if (userId != null) {
            vo.setLiked(storyLikeMapper.checkUserLiked(userId, story.getId()) > 0);
            vo.setFavorited(storyFavoriteMapper.checkUserFavorited(userId, story.getId()) > 0);
        } else {
            vo.setLiked(false);
            vo.setFavorited(false);
        }
        vo.setPublishTime(story.getCreatedAt());
        if (story.getCreatedAt() != null) {
            vo.setPublishDate(story.getCreatedAt().format(DateConstant.Y_M_D));
        }
        vo.setViewCount(calculateViewCount(ContentCategoryEnum.STORY, story.getId(), story.getViews()));
        return vo;
    }

    @Override
    public void likeContent(String category, Long id, Long userId) {
        ContentCategoryEnum contentCategoryEnum = ContentCategoryEnum.fromCode(category);
        if (contentCategoryEnum == ContentCategoryEnum.GUIDE) {
            requireGuide(id);
            if (guideLikeMapper.checkUserLiked(userId, id) == 0) {
                GuideLike like = new GuideLike();
                like.setGuideId(id);
                like.setUserId(userId);
                like.setCreatedAt(LocalDateTime.now());
                guideLikeMapper.insert(like);
                viewCountService.incrementContentLike(ContentCategoryEnum.GUIDE, id, 1);
            }
        } else {
            Story story = requireStory(id);
            if (storyLikeMapper.checkUserLiked(userId, id) == 0) {
                StoryLike like = new StoryLike();
                like.setStoryId(id);
                like.setUserId(userId);
                like.setCreatedAt(LocalDateTime.now());
                storyLikeMapper.insert(like);
                viewCountService.incrementContentLike(ContentCategoryEnum.STORY, id, 1);
            }
        }
    }

    @Override
    public void unlikeContent(String category, Long id, Long userId) {
        ContentCategoryEnum contentCategoryEnum = ContentCategoryEnum.fromCode(category);
        if (contentCategoryEnum == ContentCategoryEnum.GUIDE) {
            LambdaQueryWrapper<GuideLike> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(GuideLike::getGuideId, id).eq(GuideLike::getUserId, userId);
            int deleted = guideLikeMapper.delete(wrapper);
            if (deleted > 0) {
                viewCountService.incrementContentLike(ContentCategoryEnum.GUIDE, id, -1);
            }
        } else {
            LambdaQueryWrapper<StoryLike> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(StoryLike::getStoryId, id).eq(StoryLike::getUserId, userId);
            int deleted = storyLikeMapper.delete(wrapper);
            if (deleted > 0) {
                viewCountService.incrementContentLike(ContentCategoryEnum.STORY, id, -1);
            }
        }
    }

    @Override
    public void favoriteContent(String category, Long id, Long userId) {
        ContentCategoryEnum contentCategoryEnum = ContentCategoryEnum.fromCode(category);
        if (contentCategoryEnum == ContentCategoryEnum.GUIDE) {
            requireGuide(id);
            if (guideFavoriteMapper.checkUserFavorited(userId, id) == 0) {
                GuideFavorite favorite = new GuideFavorite();
                favorite.setGuideId(id);
                favorite.setUserId(userId);
                favorite.setCreatedAt(LocalDateTime.now());
                guideFavoriteMapper.insert(favorite);
                viewCountService.incrementContentFavorite(ContentCategoryEnum.GUIDE, id, 1);
            }
        } else {
            requireStory(id);
            if (storyFavoriteMapper.checkUserFavorited(userId, id) == 0) {
                StoryFavorite favorite = new StoryFavorite();
                favorite.setStoryId(id);
                favorite.setUserId(userId);
                favorite.setCreatedAt(LocalDateTime.now());
                storyFavoriteMapper.insert(favorite);
                viewCountService.incrementContentFavorite(ContentCategoryEnum.STORY, id, 1);
            }
        }
    }

    @Override
    public void unfavoriteContent(String category, Long id, Long userId) {
        ContentCategoryEnum contentCategoryEnum = ContentCategoryEnum.fromCode(category);
        if (contentCategoryEnum == ContentCategoryEnum.GUIDE) {
            LambdaQueryWrapper<GuideFavorite> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(GuideFavorite::getGuideId, id).eq(GuideFavorite::getUserId, userId);
            int deleted = guideFavoriteMapper.delete(wrapper);
            if (deleted > 0) {
                viewCountService.incrementContentFavorite(ContentCategoryEnum.GUIDE, id, -1);
            }
        } else {
            LambdaQueryWrapper<StoryFavorite> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(StoryFavorite::getStoryId, id).eq(StoryFavorite::getUserId, userId);
            int deleted = storyFavoriteMapper.delete(wrapper);
            if (deleted > 0) {
                viewCountService.incrementContentFavorite(ContentCategoryEnum.STORY, id, -1);
            }
        }
    }

    @Override
    public boolean isContentLiked(String category, Long id, Long userId) {
        ContentCategoryEnum contentCategoryEnum = ContentCategoryEnum.fromCode(category);
        if (contentCategoryEnum == ContentCategoryEnum.GUIDE) {
            return guideLikeMapper.checkUserLiked(userId, id) > 0;
        }
        return storyLikeMapper.checkUserLiked(userId, id) > 0;
    }

    @Override
    public boolean isContentFavorited(String category, Long id, Long userId) {
        ContentCategoryEnum contentCategoryEnum = ContentCategoryEnum.fromCode(category);
        if (contentCategoryEnum == ContentCategoryEnum.GUIDE) {
            return guideFavoriteMapper.checkUserFavorited(userId, id) > 0;
        }
        return storyFavoriteMapper.checkUserFavorited(userId, id) > 0;
    }

    @Override
    public Page<ContentVO> queryUserLikedContent(ContentUserQueryDTO queryDTO, Long userId) {
        ensureUserLoggedIn(userId);
        long current = normalizeCurrent(queryDTO.getCurrent());
        long size = normalizeSize(queryDTO.getSize());

        List<ContentVO> merged = new ArrayList<>();
        if (shouldIncludeGuide(queryDTO.getCategory())) {
            merged.addAll(loadUserGuideLikes(userId));
        }
        if (shouldIncludeStory(queryDTO.getCategory())) {
            merged.addAll(loadUserStoryLikes(userId));
        }

        sortByRelationTime(merged);
        return buildPage(merged, current, size);
    }

    @Override
    public Page<ContentVO> queryUserFavoritedContent(ContentUserQueryDTO queryDTO, Long userId) {
        ensureUserLoggedIn(userId);
        long current = normalizeCurrent(queryDTO.getCurrent());
        long size = normalizeSize(queryDTO.getSize());

        List<ContentVO> merged = new ArrayList<>();
        if (shouldIncludeGuide(queryDTO.getCategory())) {
            merged.addAll(loadUserGuideFavorites(userId));
        }
        if (shouldIncludeStory(queryDTO.getCategory())) {
            merged.addAll(loadUserStoryFavorites(userId));
        }

        sortByRelationTime(merged);
        return buildPage(merged, current, size);
    }

    private void ensureUserLoggedIn(Long userId) {
        if (userId == null) {
            throw new BizException(ResultCode.UNAUTHORIZED.getCode(), "请先登录");
        }
    }

    private long normalizeCurrent(Long current) {
        return current == null || current <= 0 ? 1L : current;
    }

    private long normalizeSize(Long size) {
        return size == null || size <= 0 ? 10L : size;
    }

    private boolean shouldIncludeGuide(String category) {
        return StrUtil.isBlank(category) || ContentCategoryEnum.GUIDE.name().equalsIgnoreCase(category);
    }

    private boolean shouldIncludeStory(String category) {
        return StrUtil.isBlank(category) || ContentCategoryEnum.STORY.name().equalsIgnoreCase(category);
    }

    private void sortByRelationTime(List<ContentVO> items) {
        items.sort(Comparator
                .comparing(ContentVO::getRelationTime, Comparator.nullsLast(LocalDateTime::compareTo)).reversed()
                .thenComparing(ContentVO::getPublishTime, Comparator.nullsLast(LocalDateTime::compareTo)).reversed());
    }

    private Page<ContentVO> buildPage(List<ContentVO> merged, long current, long size) {
        Page<ContentVO> page = new Page<>(current, size);
        long total = merged.size();
        page.setTotal(total);

        long fromIndex = Math.min((current - 1) * size, total);
        long toIndex = Math.min(fromIndex + size, total);
        if (fromIndex >= toIndex) {
            page.setRecords(Collections.emptyList());
        } else {
            page.setRecords(merged.subList((int) fromIndex, (int) toIndex));
        }
        return page;
    }

    private List<ContentVO> loadUserGuideLikes(Long userId) {
        LambdaQueryWrapper<GuideLike> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(GuideLike::getUserId, userId).orderByDesc(GuideLike::getCreatedAt);
        List<GuideLike> likes = guideLikeMapper.selectList(wrapper);
        if (likes.isEmpty()) {
            return Collections.emptyList();
        }

        Set<Long> guideIds = extractIds(likes, GuideLike::getGuideId);
        if (guideIds.isEmpty()) {
            return Collections.emptyList();
        }

        Map<Long, Guide> guideMap = guideMapper.selectBatchIds(guideIds).stream()
                .collect(Collectors.toMap(Guide::getId, guide -> guide));

        List<ContentVO> result = new ArrayList<>();
        for (GuideLike like : likes) {
            Guide guide = guideMap.get(like.getGuideId());
            if (guide == null) {
                continue;
            }
            ContentVO vo = toContentVO(guide, null);
            vo.setLiked(Boolean.TRUE);
            vo.setRelationTime(like.getCreatedAt());
            result.add(vo);
        }
        return result;
    }

    private List<ContentVO> loadUserStoryLikes(Long userId) {
        LambdaQueryWrapper<StoryLike> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StoryLike::getUserId, userId).orderByDesc(StoryLike::getCreatedAt);
        List<StoryLike> likes = storyLikeMapper.selectList(wrapper);
        if (likes.isEmpty()) {
            return Collections.emptyList();
        }

        Set<Long> storyIds = extractIds(likes, StoryLike::getStoryId);
        if (storyIds.isEmpty()) {
            return Collections.emptyList();
        }
        Map<Long, Story> storyMap = storyMapper.selectBatchIds(storyIds).stream()
                .collect(Collectors.toMap(Story::getId, story -> story));

        List<ContentVO> result = new ArrayList<>();
        for (StoryLike like : likes) {
            Story story = storyMap.get(like.getStoryId());
            if (story == null) {
                continue;
            }
            ContentVO vo = toContentVO(story, null);
            vo.setLiked(Boolean.TRUE);
            vo.setRelationTime(like.getCreatedAt());
            result.add(vo);
        }
        return result;
    }

    private List<ContentVO> loadUserGuideFavorites(Long userId) {
        LambdaQueryWrapper<GuideFavorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(GuideFavorite::getUserId, userId).orderByDesc(GuideFavorite::getCreatedAt);
        List<GuideFavorite> favorites = guideFavoriteMapper.selectList(wrapper);
        if (favorites.isEmpty()) {
            return Collections.emptyList();
        }

        Set<Long> guideIds = extractIds(favorites, GuideFavorite::getGuideId);
        if (guideIds.isEmpty()) {
            return Collections.emptyList();
        }
        Map<Long, Guide> guideMap = guideMapper.selectBatchIds(guideIds).stream()
                .collect(Collectors.toMap(Guide::getId, guide -> guide));

        List<ContentVO> result = new ArrayList<>();
        for (GuideFavorite favorite : favorites) {
            Guide guide = guideMap.get(favorite.getGuideId());
            if (guide == null) {
                continue;
            }
            ContentVO vo = toContentVO(guide, null);
            vo.setFavorited(Boolean.TRUE);
            vo.setRelationTime(favorite.getCreatedAt());
            result.add(vo);
        }
        return result;
    }

    private List<ContentVO> loadUserStoryFavorites(Long userId) {
        LambdaQueryWrapper<StoryFavorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StoryFavorite::getUserId, userId).orderByDesc(StoryFavorite::getCreatedAt);
        List<StoryFavorite> favorites = storyFavoriteMapper.selectList(wrapper);
        if (favorites.isEmpty()) {
            return Collections.emptyList();
        }

        Set<Long> storyIds = extractIds(favorites, StoryFavorite::getStoryId);
        if (storyIds.isEmpty()) {
            return Collections.emptyList();
        }
        Map<Long, Story> storyMap = storyMapper.selectBatchIds(storyIds).stream()
                .collect(Collectors.toMap(Story::getId, story -> story));

        List<ContentVO> result = new ArrayList<>();
        for (StoryFavorite favorite : favorites) {
            Story story = storyMap.get(favorite.getStoryId());
            if (story == null) {
                continue;
            }
            ContentVO vo = toContentVO(story, null);
            vo.setFavorited(Boolean.TRUE);
            vo.setRelationTime(favorite.getCreatedAt());
            result.add(vo);
        }
        return result;
    }
    
    private <T> Set<Long> extractIds(List<T> relations, Function<T, Long> idGetter) {
        return relations.stream()
                .map(idGetter)
                .collect(Collectors.toSet());
    }

    private Guide requireGuide(Long id) {
        Guide guide = guideMapper.selectById(id);
        if (guide == null) {
            throw new BizException(ResultCode.NOT_FOUND.getCode(), MessageConstant.GUIDE_NOT_FOUND);
        }
        return guide;
    }

    private Story requireStory(Long id) {
        Story story = storyMapper.selectById(id);
        if (story == null) {
            throw new BizException(ResultCode.NOT_FOUND.getCode(), MessageConstant.STORY_NOT_FOUND);
        }
        return story;
    }

    private int calculateViewCount(ContentCategoryEnum category, Long id, Integer persisted) {
        int base = persisted == null ? 0 : persisted;
        int increment = viewCountService.getContentViewIncrement(category, id);
        return base + increment;
    }

    private int calculateLikeCount(ContentCategoryEnum category, Long id, Integer persisted) {
        int base = persisted == null ? 0 : persisted;
        return viewCountService.getContentLikeCount(category, id, () -> base);
    }

    private int calculateFavoriteCount(ContentCategoryEnum category, Long id, Integer persisted) {
        int base = persisted == null ? 0 : persisted;
        return viewCountService.getContentFavoriteCount(category, id, () -> base);
    }
}
