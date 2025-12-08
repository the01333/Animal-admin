package com.puxinxiaolin.adopt.entity.dto;

import lombok.Data;

/**
 * WebSocket 人工客服聊天消息 DTO
 */
@Data
public class CsWsChatMessageDTO {

    /** 会话ID (t_cs_session.id) */
    private Long sessionId;

    /** 消息类型: text / image 等 */
    private String messageType;

    /** 消息内容 */
    private String content;
}
