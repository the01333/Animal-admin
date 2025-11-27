package com.puxinxiaolin.adopt.entity.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 领养申请展示 VO
 */
@Data
public class AdoptionApplicationVO {

    private Long id;
    private String applicationNo;
    private Long userId;
    private Long petId;

    private String status;
    private String statusText;
    private String reason;
    private String familyInfo;
    private String careplan;
    private String additionalInfo;
    private String contactPhone;
    private String contactAddress;
    private String reviewComment;
    private Long reviewerId;
    private String reviewerName;
    private LocalDateTime reviewTime;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    // 申请人信息
    private String applicantUsername;
    private String applicantNickname;
    private String applicantAvatar;
    private String applicantPhone;
    private String applicantEmail;
    private String applicantAddress;
    private Boolean applicantCertified;
    private Boolean applicantHasExperience;
    private String applicantRole;

    // 宠物信息
    private String petName;
    private String petCoverImage;
    private String petCategory;
    private String petCategoryText;
    private Integer petGender;
    private String petAdoptionStatus;
    private String petAdoptionStatusText;
}
