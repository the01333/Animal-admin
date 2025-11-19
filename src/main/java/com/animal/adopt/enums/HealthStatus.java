package com.animal.adopt.enums;

import lombok.Getter;

/**
 * 健康状态枚举
 */
@Getter
public enum HealthStatus {
    
    /** 健康 */
    HEALTHY("healthy", "健康"),
    
    /** 生病 */
    SICK("sick", "生病"),
    
    /** 受伤 */
    INJURED("injured", "受伤"),
    
    /** 康复中 */
    RECOVERING("recovering", "康复中");
    
    private final String code;
    private final String desc;
    
    HealthStatus(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    
    /**
     * 根据code获取枚举
     */
    public static HealthStatus fromCode(String code) {
        for (HealthStatus status : HealthStatus.values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }
}


