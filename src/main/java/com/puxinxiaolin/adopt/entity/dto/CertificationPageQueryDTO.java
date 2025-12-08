package com.puxinxiaolin.adopt.entity.dto;

import com.puxinxiaolin.adopt.common.PageInfo;
import lombok.Data;

/**
 * 管理端认证分页查询 DTO
 */
@Data
public class CertificationPageQueryDTO extends PageInfo {

    /**
     * 认证状态: pending/approved/rejected
     */
    private String status;

    /**
     * 关键词（手机号或身份证号）
     */
    private String keyword;
}
