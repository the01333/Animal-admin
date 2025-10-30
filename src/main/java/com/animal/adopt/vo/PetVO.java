package com.animal.adopt.vo;

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
    private Integer favoriteCount;
    private Integer applicationCount;
    private LocalDateTime createTime;
}

