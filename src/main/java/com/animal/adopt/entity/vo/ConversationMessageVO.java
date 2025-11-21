package com.animal.adopt.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

/**
 * 对话消息VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConversationMessageVO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /** 消息ID */
    private Long id;
    
    /** 会话ID */
    private String sessionId;
    
    /** 消息角色 (user/assistant) */
    private String role;
    
    /** 消息内容 */
    private String content;
    
    /** 调用的工具名称 */
    private String toolName;
    
    /** 工具调用参数 */
    private String toolParams;
    
    /** 工具返回结果 */
    private String toolResult;
    
    /** 消息时间戳 */
    private Long timestamp;
    
    /** 消息发送时间 (格式化) */
    private String createTime;
}
