package com.animal.adopt.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 通知消息实体类
 */
@Data
@TableName("t_notification")
public class Notification implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /** 通知ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    /** 接收用户ID */
    private Long userId;
    
    /** 通知标题 */
    private String title;
    
    /** 通知内容 */
    private String content;
    
    /** 通知类型 (system:系统通知 adoption:领养通知 comment:评论通知) */
    private String type;
    
    /** 关联ID（如申请ID、宠物ID等） */
    private Long relatedId;
    
    /** 是否已读 */
    private Boolean isRead;
    
    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    /** 逻辑删除 */
    @TableLogic
    private Integer deleted;
}


