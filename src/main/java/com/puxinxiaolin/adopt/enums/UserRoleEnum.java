package com.puxinxiaolin.adopt.enums;

import lombok.Getter;

/**
 * 用户角色枚举
 */
@Getter
public enum UserRoleEnum {

    /**
     * 普通用户
     */
    USER("user", "普通用户"),

    /**
     * 超级管理员
     */
    SUPER_ADMIN("super_admin", "超级管理员"),

    /**
     * 管理员
     */
    ADMIN("admin", "管理员"),
    ;

    private final String code;
    private final String desc;

    UserRoleEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 根据 code 获取枚举
     */
    public static UserRoleEnum getByCode(String code) {
        for (UserRoleEnum role : UserRoleEnum.values()) {
            if (role.getCode().equals(code)) {
                return role;
            }
        }
        return null;
    }

    /**
     * 判断是否是管理员角色
     */
    public boolean isAdmin() {
        return this == SUPER_ADMIN || this == ADMIN;
    }
}


