package com.animal.adopt.entity.vo;

import lombok.Data;

/**
 * 认证信息VO
 */
@Data
public class CertificationInfoVO {

    /**
     * 认证状态  -  not_submitted:未提交  pending:审核中  approved:已认证  rejected:已拒绝
     */
    private String status;

    /**
     * 拒绝原因
     */
    private String rejectReason;
}
