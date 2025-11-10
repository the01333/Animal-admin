package com.animal.adopt.entity.dto;

import lombok.Data;

/**
 * 宠物查询DTO
 */
@Data
public class PetQueryDTO {

    /**
     * 当前页
     */
    private Integer current = 1;

    /**
     * 每页大小
     */
    private Integer size = 10;

    /**
     * 宠物名称
     */
    private String name;

    /**
     * 种类
     */
    private String category;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 是否绝育
     */
    private Boolean neutered;

    /**
     * 是否接种疫苗
     */
    private Boolean vaccinated;

    /**
     * 领养状态
     */
    private String adoptionStatus;

    /**
     * 上架状态
     */
    private Integer shelfStatus;

    /**
     * 最小年龄（月）
     */
    private Integer minAge;

    /**
     * 最大年龄（月）
     */
    private Integer maxAge;
}

