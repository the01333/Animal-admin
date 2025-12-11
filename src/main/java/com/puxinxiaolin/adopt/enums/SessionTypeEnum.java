package com.puxinxiaolin.adopt.enums;

import lombok.Getter;

/**
 * 会话类型枚举
 */
@Getter
public enum SessionTypeEnum {

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

    SessionTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 根据code获取枚举
     */
    public static SessionTypeEnum getByCode(String code) {
        for (SessionTypeEnum type : SessionTypeEnum.values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }
}


