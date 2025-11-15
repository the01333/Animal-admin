package com.animal.adopt.entity.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 文章信息VO
 *
 * @author Animal Adopt System
 * @date 2025-11-10
 */
@Data
public class ArticleVO {

    private Long id;
    private String title;
    private String category;
    private String coverImage;
    private String summary;
    private String content;
    private String author;
    private Integer viewCount;
    private Integer likeCount;
    private Integer favoriteCount;
    private Integer status;
    private LocalDateTime publishTime;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    // ========== 用户交互状态 ==========
    /**
     * 当前用户是否已点赞
     */
    private Boolean isLiked;
    
    /**
     * 当前用户是否已收藏
     */
    private Boolean isFavorited;
}
