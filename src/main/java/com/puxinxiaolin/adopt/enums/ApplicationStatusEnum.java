package com.puxinxiaolin.adopt.enums;

import lombok.Getter;

/**
 * 申请状态枚举
 */
@Getter
public enum ApplicationStatusEnum {

    /**
     * 待审核
     */
    PENDING("pending", "待审核"),

    /**
     * 已通过
     */
    APPROVED("approved", "已通过"),

    /**
     * 已拒绝
     */
    REJECTED("rejected", "已拒绝"),

    /**
     * 已撤销
     */
    CANCELLED("cancelled", "已撤销");

    private final String code;
    private final String desc;

    ApplicationStatusEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 根据code获取枚举
     */
    public static ApplicationStatusEnum getByCode(String code) {
        for (ApplicationStatusEnum status : ApplicationStatusEnum.values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }

    /**
     * 判断是否可以修改
     */
    public boolean canModify() {
        return this == PENDING;
    }

    /**
     * 判断是否是终态
     */
    public boolean isFinalState() {
        return this == APPROVED || this == REJECTED || this == CANCELLED;
    }
}


