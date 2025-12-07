package com.puxinxiaolin.adopt.entity.dto;

import lombok.Data;

/**
 * 智能客服流式对话请求 DTO
 */
@Data
public class ChatStreamRequestDTO {

    /**
     * 会话 ID, 可为空表示新会话
     */
    private String sessionId;

    /**
     * 用户输入内容
     */
    private String content;
}
