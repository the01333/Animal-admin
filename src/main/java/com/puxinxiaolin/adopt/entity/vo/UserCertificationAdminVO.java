package com.puxinxiaolin.adopt.entity.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 管理端用户认证视图对象
 */
@Data
public class UserCertificationAdminVO {

    private Long id;

    private Long userId;

    private String username;

    private String nickname;

    private String phone;

    private String email;

    private Boolean certified;

    private String idCard;

    private String idCardFrontUrl;

    private String idCardBackUrl;

    private String status;

    private String rejectReason;

    private Long reviewerId;

    private String reviewerName;

    private LocalDateTime reviewTime;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
