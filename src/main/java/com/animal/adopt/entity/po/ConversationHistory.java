package com.animal.adopt.entity.po;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 对话历史记录实体类
 * 用于存储用户与AI客服的对话内容
 */
@Data
@TableName("t_conversation_history")
public class ConversationHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 对话记录ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 会话ID (用于关联同一次对话)
     */
    private String sessionId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 消息角色 (user:用户 assistant:AI助手)
     */
    private String role;

    /**
     * 消息内容
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private String content;

    /**
     * 调用的工具名称 (可选)
     */
    private String toolName;

    /**
     * 工具调用参数 (JSON格式)
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private String toolParams;

    /**
     * 工具返回结果 (JSON格式)
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private String toolResult;

    /**
     * 消息时间戳
     */
    private LocalDateTime timestamp;
    
    /**
     * 消息发送时间
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
