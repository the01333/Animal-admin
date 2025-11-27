package com.puxinxiaolin.adopt.entity.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 领养申请实体类
 */
@Data
@TableName("t_adoption_application")
public class AdoptionApplication implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /** 申请ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    /** 申请编号 */
    private String applicationNo;
    
    /** 申请人ID */
    private Long userId;
    
    /** 宠物ID */
    private Long petId;
    
    /** 申请原因 */
    private String reason;
    
    /** 家庭成员情况 */
    private String familyInfo;
    
    /** 养宠计划 */
    private String careplan;
    
    /** 补充说明 */
    private String additionalInfo;
    
    /** 联系电话 */
    private String contactPhone;
    
    /** 联系地址 */
    private String contactAddress;
    
    /** 申请状态 (pending:待审核 approved:已通过 rejected:已拒绝 cancelled:已撤销) */
    private String status;
    
    /** 审核人ID */
    private Long reviewerId;
    
    /** 审核时间 */
    private LocalDateTime reviewTime;
    
    /** 审核意见 */
    private String reviewComment;
    
    /** 申请时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    /** 更新时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    /** 逻辑删除 */
    @TableLogic
    private Integer deleted;
}

