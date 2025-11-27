package com.puxinxiaolin.adopt.service.impl;

import cn.hutool.core.util.IdUtil;
import com.puxinxiaolin.adopt.entity.cassandra.ConversationHistoryCassandra;
import com.puxinxiaolin.adopt.entity.entity.ConversationHistory;
import com.puxinxiaolin.adopt.entity.entity.ConversationSession;
import com.puxinxiaolin.adopt.entity.vo.ConversationMessageVO;
import com.puxinxiaolin.adopt.entity.vo.ConversationSessionVO;
import com.puxinxiaolin.adopt.mapper.ConversationHistoryMapper;
import com.puxinxiaolin.adopt.mapper.ConversationSessionMapper;
import com.puxinxiaolin.adopt.repository.ConversationHistoryCassandraRepository;
import com.puxinxiaolin.adopt.service.ConversationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 对话服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ConversationServiceImpl extends ServiceImpl<ConversationSessionMapper, ConversationSession>
        implements ConversationService {
    
    private final ConversationHistoryMapper conversationHistoryMapper;
    private final ConversationHistoryCassandraRepository cassandraRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    
    private static final String CONVERSATION_CACHE_PREFIX = "conversation:";
    private static final long CACHE_EXPIRE_HOURS = 24;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ConversationSession createSession(Long userId, String title) {
        log.info("创建新的对话会话, 用户ID: {}, 标题: {}", userId, title);
        
        ConversationSession session = new ConversationSession();
        session.setSessionId(IdUtil.simpleUUID());
        session.setUserId(userId);
        session.setTitle(title != null ? title : "新对话");
        session.setStatus("active");
        session.setMessageCount(0);
        
        this.save(session);
        log.info("对话会话创建成功, 会话ID: {}", session.getSessionId());
        
        return session;
    }
    
    @Override
    public List<ConversationSessionVO> getUserSessions(Long userId) {
        log.info("获取用户的所有会话, 用户ID: {}", userId);
        
        List<ConversationSession> sessions = this.baseMapper.getUserSessions(userId);
        
        return sessions.stream()
                .map(this::convertSessionToVO)
                .collect(Collectors.toList());
    }
    
    @Override
    public ConversationSessionVO getSessionDetail(String sessionId, Long userId) {
        log.info("获取会话详情, 会话ID: {}, 用户ID: {}", sessionId, userId);
        
        ConversationSession session = this.baseMapper.getBySessionId(sessionId);
        if (session == null || !session.getUserId().equals(userId)) {
            log.warn("会话不存在或用户无权限, 会话ID: {}", sessionId);
            return null;
        }
        
        // 直接从 Cassandra 获取所有消息（包括用户消息和 AI 回复）
        List<ConversationHistoryCassandra> cassandraHistories = cassandraRepository.findBySessionId(sessionId);
        log.info("从 Cassandra 查询到的消息数: {}", cassandraHistories.size());
        
        // 将 Cassandra 数据转换为前端格式, 并去除重复消息
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Map<String, ConversationMessageVO> messageMap = new LinkedHashMap<>();
        
        cassandraHistories.stream()
                .sorted(Comparator.comparing((ConversationHistoryCassandra h) -> h.getKey().getTimestamp())
                        .thenComparing(h -> h.getKey().getMessageId()))
                .forEach(h -> {
                    Instant instant = h.getKey().getTimestamp();
                    LocalDateTime ldt = LocalDateTime.ofInstant(instant, java.time.ZoneId.systemDefault());
                    
                    ConversationMessageVO msg = ConversationMessageVO.builder()
                            .id(h.getKey().getMessageId() != null ? h.getKey().getMessageId().getMostSignificantBits() : 0L)
                            .sessionId(h.getKey().getSessionId())
                            .role(h.getRole())
                            .content(h.getContent())
                            .toolName(h.getToolName())
                            .toolParams(h.getToolParams())
                            .toolResult(h.getToolResult())
                            .timestamp(ldt.format(formatter))
                            .createTime(h.getCreatedAt() != null ? 
                                    LocalDateTime.ofInstant(h.getCreatedAt(), java.time.ZoneId.systemDefault()).format(formatter) : null)
                            .build();
                    
                    // 使用 "role + timestamp + content" 作为去重键, 避免完全相同的消息重复
                    String key = msg.getRole() + "|" + msg.getTimestamp() + "|" + msg.getContent();
                    messageMap.putIfAbsent(key, msg);
                    
                    log.debug("消息: role={}, timestamp={}, content长度={}", msg.getRole(), msg.getTimestamp(), msg.getContent().length());
                });
        
        List<ConversationMessageVO> messages = new ArrayList<>(messageMap.values());
        log.info("去重后返回给前端的消息数: {}", messages.size());
        
        ConversationSessionVO vo = convertSessionToVO(session);
        vo.setMessages(messages);
        
        return vo;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveMessage(String sessionId, Long userId, String role, String content,
                           String toolName, String toolParams, String toolResult) {
        log.info("保存对话消息, 会话ID: {}, 角色: {}", sessionId, role);
        
        // 验证会话所有权
        ConversationSession session = this.baseMapper.getBySessionId(sessionId);
        if (session == null || !session.getUserId().equals(userId)) {
            log.warn("会话不存在或用户无权限");
            return;
        }
        
        // 保存消息到 MySQL 数据库
        ConversationHistory history = new ConversationHistory();
        history.setSessionId(sessionId);
        history.setUserId(userId);
        history.setRole(role);
        history.setContent(content);
        history.setToolName(toolName);
        history.setToolParams(toolParams);
        history.setToolResult(toolResult);
        history.setTimestamp(LocalDateTime.now());
        
        conversationHistoryMapper.insert(history);
        
        // 同时保存消息到 Cassandra（异步, 不阻塞主流程）
        try {
            saveMessageToCassandra(sessionId, userId, role, content, toolName, toolParams, toolResult);
        } catch (Exception e) {
            log.warn("保存消息到 Cassandra 失败: {}", e.getMessage());
            // 不影响主流程, 仅记录警告
        }
        
        // 更新会话的消息计数和最后消息
        session.setMessageCount(session.getMessageCount() + 1);
        session.setLastMessage(content);
        session.setLastMessageTime(LocalDateTime.now());
        session.setUpdateTime(LocalDateTime.now());
        this.updateById(session);
        
        // 清除缓存
        clearSessionCache(sessionId);
        
        log.info("对话消息保存成功");
    }
    
    /**
     * 保存消息到 Cassandra
     */
    private void saveMessageToCassandra(String sessionId, Long userId, String role, String content,
                                       String toolName, String toolParams, String toolResult) {
        try {
            Instant now = Instant.now();
            UUID messageId = UUID.randomUUID();
            
            ConversationHistoryCassandra.ConversationHistoryKey key = 
                    ConversationHistoryCassandra.ConversationHistoryKey.builder()
                    .sessionId(sessionId)
                    .timestamp(now)
                    .messageId(messageId)
                    .build();
            
            ConversationHistoryCassandra cassandraHistory = ConversationHistoryCassandra.builder()
                    .key(key)
                    .userId(userId)
                    .role(role)
                    .content(content)
                    .toolName(toolName)
                    .toolParams(toolParams)
                    .toolResult(toolResult)
                    .createdAt(now)
                    .build();
            
            cassandraRepository.save(cassandraHistory);
            log.debug("消息已保存到 Cassandra, 会话ID: {}, 消息ID: {}", sessionId, messageId);
        } catch (Exception e) {
            log.error("保存消息到 Cassandra 失败: {}", e.getMessage(), e);
            throw new RuntimeException("保存消息到 Cassandra 失败", e);
        }
    }
    
    @Override
    public List<Message> getConversationHistory(String sessionId) {
        log.info("获取会话的对话历史, 会话ID: {}", sessionId);
        
        // 先从缓存获取
        String cacheKey = CONVERSATION_CACHE_PREFIX + sessionId;
        @SuppressWarnings("unchecked")
        List<Message> cachedMessages = (List<Message>) redisTemplate.opsForValue().get(cacheKey);
        if (cachedMessages != null && !cachedMessages.isEmpty()) {
            log.info("从缓存获取对话历史, 会话ID: {}", sessionId);
            return cachedMessages;
        }
        
        // 从数据库获取
        List<ConversationHistory> histories = conversationHistoryMapper.getBySessionId(sessionId);
        List<Message> messages = histories.stream()
                .map(h -> {
                    if ("user".equals(h.getRole())) {
                        return new UserMessage(h.getContent());
                    } else {
                        return new AssistantMessage(h.getContent());
                    }
                })
                .collect(Collectors.toList());
        
        // 缓存到Redis
        if (!messages.isEmpty()) {
            redisTemplate.opsForValue().set(cacheKey, messages, 
                    java.time.Duration.ofHours(CACHE_EXPIRE_HOURS));
        }
        
        return messages;
    }
    
    @Override
    public ConversationSession getActiveSession(Long userId) {
        log.info("获取用户的活跃会话, 用户ID: {}", userId);
        return this.baseMapper.getActiveSession(userId);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void closeSession(String sessionId, Long userId) {
        log.info("关闭会话, 会话ID: {}, 用户ID: {}", sessionId, userId);
        
        ConversationSession session = this.baseMapper.getBySessionId(sessionId);
        if (session == null || !session.getUserId().equals(userId)) {
            log.warn("会话不存在或用户无权限");
            return;
        }
        
        session.setStatus("archived");
        session.setClosedTime(LocalDateTime.now());
        this.updateById(session);
        
        clearSessionCache(sessionId);
        log.info("会话已关闭");
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteSession(String sessionId, Long userId) {
        log.info("删除会话, 会话ID: {}, 用户ID: {}", sessionId, userId);
        
        ConversationSession session = this.baseMapper.getBySessionId(sessionId);
        if (session == null || !session.getUserId().equals(userId)) {
            log.warn("会话不存在或用户无权限");
            return;
        }
        
        // 删除会话的所有消息（MySQL）
        conversationHistoryMapper.deleteBySessionId(sessionId);
        
        // 删除会话的所有消息（Cassandra）
        try {
            cassandraRepository.deleteBySessionId(sessionId);
        } catch (Exception e) {
            log.warn("从 Cassandra 删除消息失败: {}", e.getMessage());
        }
        
        // 删除会话
        this.removeById(session.getId());
        
        clearSessionCache(sessionId);
        log.info("会话已删除");
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void clearSessionMessages(String sessionId, Long userId) {
        log.info("清空会话消息, 会话ID: {}, 用户ID: {}", sessionId, userId);
        
        ConversationSession session = this.baseMapper.getBySessionId(sessionId);
        if (session == null || !session.getUserId().equals(userId)) {
            log.warn("会话不存在或用户无权限");
            return;
        }
        
        // 删除所有消息（MySQL）
        conversationHistoryMapper.deleteBySessionId(sessionId);
        
        // 删除所有消息（Cassandra）
        try {
            cassandraRepository.deleteBySessionId(sessionId);
        } catch (Exception e) {
            log.warn("从 Cassandra 删除消息失败: {}", e.getMessage());
        }
        
        // 重置会话
        session.setMessageCount(0);
        session.setLastMessage(null);
        session.setLastMessageTime(null);
        this.updateById(session);
        
        clearSessionCache(sessionId);
        log.info("会话消息已清空");
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSessionLastMessage(String sessionId, String lastMessage) {
        ConversationSession session = this.baseMapper.getBySessionId(sessionId);
        if (session != null) {
            session.setLastMessage(lastMessage);
            session.setLastMessageTime(LocalDateTime.now());
            this.updateById(session);
        }
    }
    
    /**
     * 将会话转换为VO
     */
    private ConversationSessionVO convertSessionToVO(ConversationSession session) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        
        return ConversationSessionVO.builder()
                .id(session.getId())
                .sessionId(session.getSessionId())
                .title(session.getTitle())
                .description(session.getDescription())
                .messageCount(session.getMessageCount())
                .status(session.getStatus())
                .lastMessage(session.getLastMessage())
                .lastMessageTime(session.getLastMessageTime() != null ? 
                        session.getLastMessageTime().format(formatter) : null)
                .createTime(session.getCreateTime() != null ? 
                        session.getCreateTime().format(formatter) : null)
                .updateTime(session.getUpdateTime() != null ? 
                        session.getUpdateTime().format(formatter) : null)
                .build();
    }
    
    /**
     * 将历史记录转换为VO
     */
    private ConversationMessageVO convertHistoryToVO(ConversationHistory history) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        
        return ConversationMessageVO.builder()
                .id(history.getId())
                .sessionId(history.getSessionId())
                .role(history.getRole())
                .content(history.getContent())
                .toolName(history.getToolName())
                .toolParams(history.getToolParams())
                .toolResult(history.getToolResult())
                .timestamp(history.getTimestamp() != null ? 
                        history.getTimestamp().format(formatter) : null)
                .createTime(history.getCreateTime() != null ? 
                        history.getCreateTime().format(formatter) : null)
                .build();
    }
    
    /**
     * 清除会话缓存
     */
    private void clearSessionCache(String sessionId) {
        String cacheKey = CONVERSATION_CACHE_PREFIX + sessionId;
        redisTemplate.delete(cacheKey);
    }
}
