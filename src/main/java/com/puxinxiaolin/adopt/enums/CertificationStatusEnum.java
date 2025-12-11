package com.puxinxiaolin.adopt.enums;

import lombok.Getter;

@Getter
public enum CertificationStatusEnum {
    NOT_SUBMITTED("not_submitted"),
    PENDING("pending"),
    APPROVED("approved"),
    REJECTED("rejected");

    private final String code;

    CertificationStatusEnum(String code) {
        this.code = code;
    }

    public static CertificationStatusEnum getByCode(String code) {
        if (code == null) {
            return null;
        }
        for (CertificationStatusEnum value : CertificationStatusEnum.values()) {
            if (value.code.equalsIgnoreCase(code)) {
                return value;
            }
        }
        return null;
    }
}
