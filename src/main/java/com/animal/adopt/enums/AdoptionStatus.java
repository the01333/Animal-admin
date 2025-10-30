package com.animal.adopt.enums;

import lombok.Getter;

/**
 * 领养状态枚举
 */
@Getter
public enum AdoptionStatus {
    
    /** 可领养 */
    AVAILABLE("available", "可领养"),
    
    /** 待审核 */
    PENDING("pending", "待审核"),
    
    /** 已领养 */
    ADOPTED("adopted", "已领养"),
    
    /** 已预定 */
    RESERVED("reserved", "已预定");
    
    private final String code;
    private final String desc;
    
    AdoptionStatus(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    
    /**
     * 根据code获取枚举
     */
    public static AdoptionStatus fromCode(String code) {
        for (AdoptionStatus status : AdoptionStatus.values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }
}

