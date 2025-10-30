package com.animal.adopt.dto;

import lombok.Data;

/**
 * 聊天消息DTO
 */
@Data
public class ChatMessageDTO {
    
    /** 消息类型 */
    private String messageType;
    
    /** 消息内容 */
    private String content;
    
    /** 接收者ID */
    private Long receiverId;
}

