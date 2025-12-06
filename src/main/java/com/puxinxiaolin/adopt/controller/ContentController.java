package com.puxinxiaolin.adopt.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.annotation.SaMode;
import com.puxinxiaolin.adopt.common.Result;
import com.puxinxiaolin.adopt.entity.dto.ContentDTO;
import com.puxinxiaolin.adopt.entity.dto.ContentQueryDTO;
import com.puxinxiaolin.adopt.entity.dto.ContentUserQueryDTO;
import com.puxinxiaolin.adopt.entity.vo.ContentCategoryVO;
import com.puxinxiaolin.adopt.entity.vo.ContentVO;
import com.puxinxiaolin.adopt.service.ContentService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/content")
@RequiredArgsConstructor
public class ContentController {

    private final ContentService contentService;

    /**
     * 聚合分页查询（指南 + 故事）
     */
    @GetMapping("/page")
    public Result<Page<ContentVO>> queryContentPage(@Valid ContentQueryDTO queryDTO) {
        log.info("分页查询内容: {}", queryDTO);
        return Result.success(contentService.queryContentPage(queryDTO));
    }

    /**
     * 查询内容详情
     */
    @GetMapping("/{category}/{id}")
    public Result<ContentVO> getContentDetail(@PathVariable("category") String category,
                                              @PathVariable("id") Long id) {
        return Result.success(contentService.getContentDetail(category, id));
    }

    /**
     * 创建内容
     */
    @PostMapping
    @SaCheckRole(value = {"admin", "super_admin"}, mode = SaMode.OR)
    public Result<ContentVO> createContent(@Valid @RequestBody ContentDTO dto) {
        ContentVO vo = contentService.createContent(dto);
        return Result.success("创建成功", vo);
    }

    /**
     * 更新内容
     */
    @PutMapping("/{category}/{id}")
    @SaCheckRole(value = {"admin", "super_admin"}, mode = SaMode.OR)
    public Result<ContentVO> updateContent(@PathVariable("category") String category,
                                           @PathVariable("id") Long id,
                                           @Valid @RequestBody ContentDTO dto) {
        dto.setCategory(category);
        ContentVO vo = contentService.updateContent(id, dto);
        return Result.success("更新成功", vo);
    }

    /**
     * 删除内容
     */
    @DeleteMapping("/{category}/{id}")
    @SaCheckRole(value = {"admin", "super_admin"}, mode = SaMode.OR)
    public Result<String> deleteContent(@PathVariable("category") String category,
                                        @PathVariable("id") Long id) {
        contentService.deleteContent(category, id);
        return Result.success("删除成功", null);
    }

    /**
     * 内容分类
     */
    @GetMapping("/categories")
    public Result<List<ContentCategoryVO>> listCategories() {
        return Result.success(contentService.listCategories());
    }

    /**
     * 点赞内容
     */
    @PostMapping("/{category}/{id}/like")
    public Result<Boolean> likeContent(@PathVariable("category") String category,
                                       @PathVariable("id") Long id) {
        contentService.likeContent(category, id);
        return Result.success(true);
    }

    /**
     * 取消点赞内容
     */
    @DeleteMapping("/{category}/{id}/like")
    public Result<Boolean> unlikeContent(@PathVariable("category") String category,
                                         @PathVariable("id") Long id) {
        contentService.unlikeContent(category, id);
        return Result.success(true);
    }

    /**
     * 收藏内容
     */
    @PostMapping("/{category}/{id}/favorite")
    public Result<Boolean> favoriteContent(@PathVariable("category") String category,
                                           @PathVariable("id") Long id) {
        contentService.favoriteContent(category, id);
        return Result.success(true);
    }

    /**
     * 取消收藏内容
     */
    @DeleteMapping("/{category}/{id}/favorite")
    public Result<Boolean> unfavoriteContent(@PathVariable("category") String category,
                                             @PathVariable("id") Long id) {
        contentService.unfavoriteContent(category, id);
        return Result.success(true);
    }

    /**
     * 检查用户是否点赞
     */
    @GetMapping("/{category}/{id}/like/check")
    public Result<Boolean> isContentLiked(@PathVariable("category") String category,
                                          @PathVariable("id") Long id) {
        return Result.success(contentService.isContentLiked(category, id));
    }

    /**
     * 检查用户是否收藏
     */
    @GetMapping("/{category}/{id}/favorite/check")
    public Result<Boolean> isContentFavorited(@PathVariable("category") String category,
                                              @PathVariable("id") Long id) {
        return Result.success(contentService.isContentFavorited(category, id));
    }

    /**
     * 查询当前用户点赞的文章（指南/故事）
     */
    @GetMapping("/like/my")
    public Result<Page<ContentVO>> queryMyLikedContent(@Valid ContentUserQueryDTO queryDTO) {
        return Result.success(contentService.queryUserLikedContent(queryDTO));
    }

    /**
     * 查询当前用户收藏的文章（指南/故事）
     */
    @GetMapping("/favorite/my")
    public Result<Page<ContentVO>> queryMyFavoritedContent(@Valid ContentUserQueryDTO queryDTO) {
        return Result.success(contentService.queryUserFavoritedContent(queryDTO));
    }
}
