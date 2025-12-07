package com.puxinxiaolin.adopt.enums;

import lombok.Getter;

/**
 * 领养状态枚举
 */
@Getter
public enum AdoptionStatusEnum {

    /**
     * 可领养
     */
    AVAILABLE("available", "可领养"),

    /**
     * 待审核
     */
    PENDING("pending", "待审核"),

    /**
     * 已领养
     */
    ADOPTED("adopted", "已领养"),

    /**
     * 已预定
     */
    RESERVED("reserved", "已预定");

    private final String code;
    private final String desc;

    AdoptionStatusEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 根据code获取枚举
     */
    public static AdoptionStatusEnum getByCode(String code) {
        for (AdoptionStatusEnum status : AdoptionStatusEnum.values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }
}


