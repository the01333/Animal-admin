package com.puxinxiaolin.adopt.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.List;

/**
 * 对话会话VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConversationSessionVO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /** 会话ID */
    private Long id;
    
    /** 会话唯一标识 */
    private String sessionId;
    
    /** 会话标题 */
    private String title;
    
    /** 会话描述 */
    private String description;
    
    /** 消息总数 */
    private Integer messageCount;
    
    /** 会话状态 */
    private String status;
    
    /** 最后一条消息 */
    private String lastMessage;
    
    /** 最后一条消息时间 */
    private String lastMessageTime;
    
    /** 会话创建时间 */
    private String createTime;
    
    /** 会话更新时间 */
    private String updateTime;
    
    /** 对话消息列表 */
    private List<ConversationMessageVO> messages;
}
