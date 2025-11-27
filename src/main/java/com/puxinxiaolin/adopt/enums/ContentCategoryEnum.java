package com.puxinxiaolin.adopt.enums;

import lombok.Getter;

/**
 * 聚合内容分类（仅领养指南与领养故事）
 */
@Getter
public enum ContentCategoryEnum {
    GUIDE("领养指南"),
    STORY("领养故事");

    private final String label;

    ContentCategoryEnum(String label) {
        this.label = label;
    }

    public static ContentCategoryEnum fromCode(String code) {
        if (code == null) {
            throw new IllegalArgumentException("分类不能为空");
        }
        try {
            return ContentCategoryEnum.valueOf(code.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("不支持的分类: " + code);
        }
    }
}
