package com.animal.adopt.service.impl;

import cn.hutool.core.util.IdUtil;
import com.animal.adopt.entity.po.ConversationHistory;
import com.animal.adopt.entity.po.ConversationSession;
import com.animal.adopt.entity.vo.ConversationMessageVO;
import com.animal.adopt.entity.vo.ConversationSessionVO;
import com.animal.adopt.mapper.ConversationHistoryMapper;
import com.animal.adopt.mapper.ConversationSessionMapper;
import com.animal.adopt.service.ConversationService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        
        // 获取会话的所有消息
        List<ConversationHistory> histories = conversationHistoryMapper.getBySessionId(sessionId);
        
        ConversationSessionVO vo = convertSessionToVO(session);
        vo.setMessages(histories.stream()
                .map(this::convertHistoryToVO)
                .collect(Collectors.toList()));
        
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
        
        // 保存消息到数据库
        ConversationHistory history = new ConversationHistory();
        history.setSessionId(sessionId);
        history.setUserId(userId);
        history.setRole(role);
        history.setContent(content);
        history.setToolName(toolName);
        history.setToolParams(toolParams);
        history.setToolResult(toolResult);
        history.setTimestamp(System.currentTimeMillis());
        
        conversationHistoryMapper.insert(history);
        
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
        
        // 删除会话的所有消息
        conversationHistoryMapper.deleteBySessionId(sessionId);
        
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
        
        // 删除所有消息
        conversationHistoryMapper.deleteBySessionId(sessionId);
        
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
                .timestamp(history.getTimestamp())
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
