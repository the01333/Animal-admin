package com.puxinxiaolin.adopt.entity.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户认证实体类
 */
@Data
@TableName("t_user_certification")
public class UserCertification implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 认证ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 身份证号
     */
    @TableField("id_card")
    private String idCard;

    /**
     * 身份证正面URL
     */
    @TableField("id_card_front_url")
    private String idCardFrontUrl;

    /**
     * 身份证反面URL
     */
    @TableField("id_card_back_url")
    private String idCardBackUrl;

    /**
     * 认证状态 (not_submitted:未提交 pending:审核中 approved:已认证 rejected:已拒绝)
     */
    private String status;

    /**
     * 拒绝原因
     */
    @TableField("reject_reason")
    private String rejectReason;

    /**
     * 审核人ID
     */
    @TableField("reviewer_id")
    private Long reviewerId;

    /**
     * 审核时间
     */
    @TableField("review_time")
    private LocalDateTime reviewTime;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 逻辑删除
     */
    @TableLogic
    private Integer deleted;
}
