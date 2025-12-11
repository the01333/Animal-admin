package com.puxinxiaolin.adopt.entity.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 对话会话实体类
 * 用于管理用户与AI客服的对话会话
 */
@Data
@TableName("t_conversation_session")
public class ConversationSession implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 会话ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 会话唯一标识 (UUID)
     */
    private String sessionId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 会话标题
     */
    private String title;

    /**
     * 会话描述
     */
    private String description;

    /**
     * 消息总数
     */
    private Integer messageCount;

    /**
     * 会话状态 (active:活跃 archived:已归档 deleted:已删除)
     */
    private String status;

    /**
     * 最后一条消息内容
     */
    private String lastMessage;

    /**
     * 最后一条消息时间
     */
    private LocalDateTime lastMessageTime;

    /**
     * 会话创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 会话更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 会话关闭时间
     */
    private LocalDateTime closedTime;

    /**
     * 逻辑删除
     */
    @TableLogic
    private Integer deleted;
}
