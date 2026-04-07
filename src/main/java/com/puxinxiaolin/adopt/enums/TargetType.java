package com.puxinxiaolin.adopt.enums;

import lombok.Getter;

/**
 * 目标类型枚举
 * 用于统一的点赞和收藏表
 */
@Getter
public enum TargetType {
    
    /**
     * 宠物
     */
    PET("PET", "宠物"),
    
    /**
     * 指南
     */
    GUIDE("GUIDE", "指南"),
    
    /**
     * 故事
     */
    STORY("STORY", "故事");
    
    private final String code;
    private final String desc;
    
    TargetType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    
    /**
     * 根据 code 获取枚举
     */
    public static TargetType fromCode(String code) {
        for (TargetType type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown target type code: " + code);
    }
}
