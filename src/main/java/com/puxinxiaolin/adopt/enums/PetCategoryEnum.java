package com.puxinxiaolin.adopt.enums;

import lombok.Getter;

/**
 * 宠物种类枚举
 */
@Getter
public enum PetCategoryEnum {

    /**
     * 狗
     */
    DOG("dog", "狗"),

    /**
     * 猫
     */
    CAT("cat", "猫"),

    /**
     * 鸟
     */
    BIRD("bird", "鸟"),

    /**
     * 兔子
     */
    RABBIT("rabbit", "兔子");

    private final String code;
    private final String desc;

    PetCategoryEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 根据code获取枚举
     */
    public static PetCategoryEnum getByCode(String code) {
        for (PetCategoryEnum category : PetCategoryEnum.values()) {
            if (category.getCode().equals(code)) {
                return category;
            }
        }
        return null;
    }
}


