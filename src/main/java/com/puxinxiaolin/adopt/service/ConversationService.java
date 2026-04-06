package com.puxinxiaolin.adopt.service;

import com.puxinxiaolin.adopt.entity.entity.ConversationSession;
import com.puxinxiaolin.adopt.entity.vo.ConversationSessionVO;
import org.springframework.ai.chat.messages.Message;
import java.util.List;

/**
 * 对话服务接口
 */
public interface ConversationService {
    
    /**
     * 创建新的对话会话
     */
    ConversationSession createSession(Long userId, String title);
    
    /**
     * 获取会话详情（包含所有消息）
     */
    ConversationSessionVO getSessionDetail(String sessionId, Long userId);
    
    /**
     * 保存对话消息
     */
    void saveMessage(String sessionId, Long userId, String role, String content, 
                    String toolName, String toolParams, String toolResult);
    
    /**
     * 获取会话的对话历史（用于AI上下文）
     */
    List<Message> getConversationHistory(String sessionId);
    
    /**
     * 删除会话
     */
    void deleteSession(String sessionId, Long userId);
}
