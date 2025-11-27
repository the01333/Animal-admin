package com.puxinxiaolin.adopt.entity.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 聚合内容 VO（指南 + 故事）
 */
@Data
public class ContentVO {

    private Long id;
    /**
     * GUIDE 或 STORY
     */
    private String category;
    private String title;
    private String summary;
    private String content;
    private String coverImage;

    // 指南字段
    private String guideCategory;
    private Integer viewCount;

    // 故事字段
    private String author;
    private List<String> tags;
    private Integer likeCount;
    private Integer favoriteCount;

    private Boolean liked;
    private Boolean favorited;

    private String publishDate;
    private LocalDateTime publishTime;
}
