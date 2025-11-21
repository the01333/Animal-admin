package com.animal.adopt.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 故事VO类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoryVO {
    
    private Long id;
    
    private String title;
    
    private String excerpt;
    
    private String content;
    
    private String image;
    
    private String author;
    
    private List<String> tags;
    
    private Integer likes;
    
    private String publishDate;
    
    /**
     * 当前用户是否点赞
     */
    private Boolean liked;
    
    /**
     * 当前用户是否收藏
     */
    private Boolean favorited;
}
