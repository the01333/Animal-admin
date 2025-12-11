package com.puxinxiaolin.adopt.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.puxinxiaolin.adopt.entity.dto.ContentDTO;
import com.puxinxiaolin.adopt.entity.dto.ContentQueryDTO;
import com.puxinxiaolin.adopt.entity.dto.ContentUserQueryDTO;
import com.puxinxiaolin.adopt.entity.vo.ContentCategoryVO;
import com.puxinxiaolin.adopt.entity.vo.ContentVO;

import java.util.List;

/**
 * 聚合内容服务（指南 + 故事）
 */
public interface ContentService {

    /**
     * 分页查询文章
     *
     * @param queryDTO
     * @return
     */
    Page<ContentVO> queryContentPage(ContentQueryDTO queryDTO);

    /**
     * 查询文章详情
     *
     * @param category
     * @param id
     * @return
     */
    ContentVO getContentDetail(String category, Long id);

    /**
     * 创建文章
     *
     * @param dto
     * @return
     */
    ContentVO createContent(ContentDTO dto);

    /**
     * 更新文章
     *
     * @param id
     * @param dto
     * @return
     */
    ContentVO updateContent(Long id, ContentDTO dto);

    /**
     * 删除文章
     *
     * @param category
     * @param id
     */
    void deleteContent(String category, Long id);

    /**
     * 获取所有分类
     *
     * @return
     */
    List<ContentCategoryVO> listCategories();

    /**
     * 点赞
     *
     * @param category
     * @param id
     */
    void likeContent(String category, Long id);

    /**
     * 取消点赞
     *
     * @param category
     * @param id
     */
    void unlikeContent(String category, Long id);

    /**
     * 收藏
     *
     * @param category
     * @param id
     */
    void favoriteContent(String category, Long id);

    /**
     * 取消收藏
     *
     * @param category
     * @param id
     */
    void unfavoriteContent(String category, Long id);

    /**
     * 是否已点赞
     *
     * @param category
     * @param id
     * @return
     */
    boolean isContentLiked(String category, Long id);

    /**
     * 是否已收藏
     *
     * @param category
     * @param id
     * @return
     */
    boolean isContentFavorited(String category, Long id);

    /**
     * 查询用户点赞的文章
     *
     * @param queryDTO
     * @return
     */
    Page<ContentVO> queryUserLikedContent(ContentUserQueryDTO queryDTO);

    /**
     * 查询用户收藏的文章
     *
     * @param queryDTO
     * @return
     */
    Page<ContentVO> queryUserFavoritedContent(ContentUserQueryDTO queryDTO);
}
