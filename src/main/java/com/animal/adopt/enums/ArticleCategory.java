package com.animal.adopt.enums;

import lombok.Getter;

/**
 * 文章分类枚举
 */
@Getter
public enum ArticleCategory {

    /**
     * 养宠指南
     */
    GUIDE("guide", "养宠指南"),

    /**
     * 领养故事
     */
    STORY("story", "领养故事"),
    
    /**
     * 新闻动态
     */
    NEWS("news", "新闻动态");

    private final String code;
    private final String desc;

    ArticleCategory(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 根据code获取枚举
     */
    public static ArticleCategory fromCode(String code) {
        for (ArticleCategory category : ArticleCategory.values()) {
            if (category.getCode().equals(code)) {
                return category;
            }
        }
        return null;
    }
}


