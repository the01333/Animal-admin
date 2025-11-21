package com.animal.adopt.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 指南VO类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GuideVO {
    
    private Long id;
    
    private String title;
    
    private String excerpt;
    
    private String content;
    
    private String image;
    
    private String category;
    
    private Integer views;
    
    private String publishDate;
    
    /**
     * 当前用户是否点赞
     */
    private Boolean liked;
    
    /**
     * 点赞总数
     */
    private Integer likeCount;
    
    /**
     * 当前用户是否收藏
     */
    private Boolean favorited;
}
