package com.animal.adopt.entity.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 宠物信息VO
 */
@Data
public class PetVO {

    private Long id;
    private String name;
    private String category;
    private String breed;
    private Integer gender;
    private Integer age;
    private BigDecimal weight;
    private String color;
    private Boolean neutered;
    private Boolean vaccinated;
    private String healthStatus;
    private String healthDescription;
    private String personality;
    private String description;
    private String images;
    private String coverImage;
    private LocalDate rescueDate;
    private String rescueLocation;
    private String adoptionRequirements;
    private String adoptionStatus;
    private Integer viewCount;
    private Integer likeCount;
    private Integer favoriteCount;
    private Integer applicationCount;
    private LocalDateTime createTime;

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

