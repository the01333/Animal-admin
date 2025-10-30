package com.animal.adopt.enums;

import lombok.Getter;

/**
 * 宠物种类枚举
 */
@Getter
public enum PetCategory {
    
    /** 狗 */
    DOG("dog", "狗"),
    
    /** 猫 */
    CAT("cat", "猫"),
    
    /** 鸟 */
    BIRD("bird", "鸟"),
    
    /** 兔子 */
    RABBIT("rabbit", "兔子"),
    
    /** 其他 */
    OTHER("other", "其他");
    
    private final String code;
    private final String desc;
    
    PetCategory(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    
    /**
     * 根据code获取枚举
     */
    public static PetCategory fromCode(String code) {
        for (PetCategory category : PetCategory.values()) {
            if (category.getCode().equals(code)) {
                return category;
            }
        }
        return null;
    }
}


