package com.animal.adopt.service;

import org.springframework.ai.chat.messages.Message;
import java.util.List;

/**
 * 会话记忆服务接口
 * 
 * 负责管理用户的对话历史，确保：
 * 1. 用户隔离 - 不同用户的对话完全分离
 * 2. 持久化 - 对话历史保存到 Cassandra
 * 3. 快速访问 - 使用 Redis 缓存热数据
 * 4. 多轮对话 - 支持完整的对话上下文
 */
public interface SessionMemoryService {

    /**
     * 获取用户的对话历史（用于 AI 上下文）
     * 
     * @param userId 用户ID
     * @param sessionId 会话ID
     * @param limit 返回最近N条消息
     * @return 对话消息列表（按时间升序）
     */
    List<Message> getSessionHistory(Long userId, String sessionId, int limit);

    /**
     * 添加用户消息到会话记忆
     * 
     * @param userId 用户ID
     * @param sessionId 会话ID
     * @param content 用户输入内容
     */
    void addUserMessage(Long userId, String sessionId, String content);

    /**
     * 添加 AI 回复到会话记忆
     * 
     * @param userId 用户ID
     * @param sessionId 会话ID
     * @param content AI 回复内容
     */
    void addAssistantMessage(Long userId, String sessionId, String content);

    /**
     * 获取会话的完整对话历史
     * 
     * @param userId 用户ID
     * @param sessionId 会话ID
     * @return 完整的对话消息列表
     */
    List<Message> getFullHistory(Long userId, String sessionId);

    /**
     * 清空会话的所有对话历史
     * 
     * @param userId 用户ID
     * @param sessionId 会话ID
     */
    void clearHistory(Long userId, String sessionId);

    /**
     * 验证用户是否有权访问该会话
     * 
     * @param userId 用户ID
     * @param sessionId 会话ID
     * @return 是否有权限
     */
    boolean hasAccess(Long userId, String sessionId);
}
