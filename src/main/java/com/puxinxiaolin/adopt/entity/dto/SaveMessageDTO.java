package com.puxinxiaolin.adopt.entity.dto;

import lombok.Data;

/**
 * 保存对话消息 DTO
 */
@Data
public class SaveMessageDTO {

    /**
     * 会话 ID
     */
    private String sessionId;

    /**
     * 角色: user/assistant
     */
    private String role;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 工具名称
     */
    private String toolName;

    /**
     * 工具参数
     */
    private String toolParams;

    /**
     * 工具结果
     */
    private String toolResult;
}
