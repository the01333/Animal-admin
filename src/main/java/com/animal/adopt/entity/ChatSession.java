package com.animal.adopt.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 聊天会话实体类
 */
@Data
@TableName("t_chat_session")
public class ChatSession implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /** 会话ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    /** 会话唯一标识 */
    private String sessionId;
    
    /** 用户ID */
    private Long userId;
    
    /** 管家ID */
    private Long housekeeperId;
    
    /** 最后一条消息 */
    private String lastMessage;
    
    /** 最后消息时间 */
    private LocalDateTime lastMessageTime;
    
    /** 未读消息数 */
    private Integer unreadCount;
    
    /** 会话状态 (active:活跃 waiting:等待 closed:已关闭) */
    private String status;
    
    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    /** 更新时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    /** 逻辑删除 */
    @TableLogic
    private Integer deleted;
}

