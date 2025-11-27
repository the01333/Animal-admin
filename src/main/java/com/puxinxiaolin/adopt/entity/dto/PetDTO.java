package com.puxinxiaolin.adopt.entity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 宠物创建/更新 DTO
 */
@Data
public class PetDTO {

    private Long id;

    @NotBlank(message = "宠物名称不能为空")
    private String name;

    @NotBlank(message = "宠物类型不能为空")
    private String category;

    @NotBlank(message = "宠物品种不能为空")
    private String breed;

    @NotNull(message = "宠物年龄不能为空")
    private Integer age;

    @NotNull(message = "宠物性别不能为空")
    private Integer gender;

    private BigDecimal weight;

    private String color;

    private String personality;

    private String description;

    private String coverImage;

    private String images;

    private String healthStatus;

    private String adoptionStatus;

    @NotNull(message = "上架状态不能为空")
    private Integer shelfStatus = 1;

    private Integer sortOrder;
}
