package com.puxinxiaolin.adopt.entity.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

/**
 * 聚合内容提交 DTO
 */
@Data
public class ContentDTO {

    private Long id;

    /**
     * 内容类型：GUIDE 或 STORY
     */
    @NotBlank(message = "分类不能为空")
    private String category;

    @NotBlank(message = "标题不能为空")
    private String title;

    /**
     * 摘要/简介
     */
    private String summary;

    /**
     * 封面图片
     */
    private String coverImage;

    @NotBlank(message = "内容不能为空")
    private String content;

    /**
     * 作者（故事类必填, 指南可空）
     */
    private String author;

    /**
     * 标签（仅故事使用）
     */
    private List<String> tags;

    /**
     * 指南子分类（可为空）
     */
    private String guideCategory;
}
