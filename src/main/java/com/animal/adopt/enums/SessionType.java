package com.animal.adopt.enums;

import lombok.Getter;

/**
 * 会话类型枚举
 */
@Getter
public enum SessionType {

    /**
     * AI助手
     */
    AI("ai", "AI助手"),

    /**
     * 管家咨询
     */
    HOUSEKEEPER("housekeeper", "管家咨询");

    private final String code;
    private final String desc;

    SessionType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 根据code获取枚举
     */
    public static SessionType fromCode(String code) {
        for (SessionType type : SessionType.values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }
}


