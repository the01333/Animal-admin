package com.puxinxiaolin.adopt.service;

import com.puxinxiaolin.adopt.entity.dto.ContentDTO;
import com.puxinxiaolin.adopt.entity.dto.ContentQueryDTO;
import com.puxinxiaolin.adopt.entity.vo.ContentCategoryVO;
import com.puxinxiaolin.adopt.entity.vo.ContentVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * 聚合内容服务（指南 + 故事）
 */
public interface ContentService {

    Page<ContentVO> queryContentPage(ContentQueryDTO queryDTO);

    ContentVO getContentDetail(String category, Long id, Long userId);

    ContentVO createContent(ContentDTO dto, Long userId);

    ContentVO updateContent(Long id, ContentDTO dto);

    void deleteContent(String category, Long id);

    List<ContentCategoryVO> listCategories();

    void likeContent(String category, Long id, Long userId);

    void unlikeContent(String category, Long id, Long userId);

    void favoriteContent(String category, Long id, Long userId);

    void unfavoriteContent(String category, Long id, Long userId);

    boolean isContentLiked(String category, Long id, Long userId);

    boolean isContentFavorited(String category, Long id, Long userId);
}
