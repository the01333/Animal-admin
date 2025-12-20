package com.puxinxiaolin.adopt.entity.dto;

import com.puxinxiaolin.adopt.entity.common.PageInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 宠物查询DTO
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class PetQueryDTO extends PageInfo {
    
    /**
     * 宠物名称或品种（模糊查询）
     */
    private String name;

    /**
     * 品种（可选, 用于精确查询）
     */
    private String breed;

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

