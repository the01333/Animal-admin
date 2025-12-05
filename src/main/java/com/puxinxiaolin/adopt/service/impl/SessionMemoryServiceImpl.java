package com.puxinxiaolin.adopt.service.impl;

import com.puxinxiaolin.adopt.entity.cassandra.ConversationHistoryCassandra;
import com.puxinxiaolin.adopt.entity.entity.ConversationSession;
import com.puxinxiaolin.adopt.mapper.ConversationSessionMapper;
import com.puxinxiaolin.adopt.repository.ConversationHistoryCassandraRepository;
import com.puxinxiaolin.adopt.service.SessionMemoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 会话记忆服务实现
 * <p>
 * 核心特性：
 * 1. 用户隔离 - 每个用户的对话完全独立
 * 2. 持久化存储 - 使用 Cassandra 存储对话历史
 * 3. 缓存加速 - 使用 Redis 缓存热数据
 * 4. 权限验证 - 确保用户只能访问自己的对话
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SessionMemoryServiceImpl implements SessionMemoryService {

    private final ConversationHistoryCassandraRepository cassandraRepository;
    private final ConversationSessionMapper sessionMapper;
    private final RedisTemplate<String, Object> redisTemplate;

    private static final String MEMORY_CACHE_PREFIX = "session:memory:";
    private static final long CACHE_EXPIRE_HOURS = 24;

    @Override
    public List<Message> getSessionHistory(Long userId, String sessionId, int limit) {
        log.info("获取会话历史 - 用户ID: {}, 会话ID: {}, 限制: {}", userId, sessionId, limit);

        // 1. 验证用户权限
        if (!hasAccess(userId, sessionId)) {
            log.warn("用户无权访问该会话 - 用户ID: {}, 会话ID: {}", userId, sessionId);
            return new ArrayList<>();
        }

        // 2. 尝试从缓存获取
        String cacheKey = MEMORY_CACHE_PREFIX + sessionId;
        @SuppressWarnings("unchecked")
        List<Message> cachedMessages = (List<Message>) redisTemplate.opsForValue().get(cacheKey);
        if (cachedMessages != null && !cachedMessages.isEmpty()) {
            log.debug("从缓存获取会话历史 - 会话ID: {}", sessionId);
            // 返回最近的N条消息
            int startIndex = Math.max(0, cachedMessages.size() - limit);
            return cachedMessages.subList(startIndex, cachedMessages.size());
        }

        // 3. 从 Cassandra 获取
        List<ConversationHistoryCassandra> histories = cassandraRepository.findRecentMessages(sessionId, limit);
        List<Message> messages = histories.stream()
                .sorted(Comparator.comparing(h -> h.getKey().getTimestamp()))
                .map(h -> {
                    if ("user".equals(h.getRole())) {
                        return new UserMessage(h.getContent());
                    } else {
                        return new AssistantMessage(h.getContent());
                    }
                })
                .collect(Collectors.toList());

        // 4. 缓存到 Redis
        if (!messages.isEmpty()) {
            redisTemplate.opsForValue().set(cacheKey, messages,
                    java.time.Duration.ofHours(CACHE_EXPIRE_HOURS));
        }

        log.debug("从 Cassandra 获取会话历史 - 会话ID: {}, 消息数: {}", sessionId, messages.size());
        return messages;
    }

    @Override
    public void addUserMessage(Long userId, String sessionId, String content) {
        log.info("添加用户消息 - 用户ID: {}, 会话ID: {}", userId, sessionId);

        if (!hasAccess(userId, sessionId)) {
            log.warn("用户无权访问该会话");
            return;
        }

        // 保存到 Cassandra
        ConversationHistoryCassandra.ConversationHistoryKey key =
                ConversationHistoryCassandra.ConversationHistoryKey.builder()
                        .sessionId(sessionId)
                        .timestamp(Instant.now())
                        .messageId(UUID.randomUUID())
                        .build();

        ConversationHistoryCassandra history = ConversationHistoryCassandra.builder()
                .key(key)
                .userId(userId)
                .role("user")
                .content(content)
                .createdAt(Instant.now())
                .build();

        cassandraRepository.save(history);

        // 清除缓存, 下次会重新加载
        clearCache(sessionId);

        log.debug("用户消息已保存 - 会话ID: {}", sessionId);
    }

    @Override
    public void addAssistantMessage(Long userId, String sessionId, String content) {
        log.info("添加 AI 回复 - 用户ID: {}, 会话ID: {}", userId, sessionId);

        if (!hasAccess(userId, sessionId)) {
            log.warn("用户无权访问该会话");
            return;
        }

        // 保存到 Cassandra
        ConversationHistoryCassandra.ConversationHistoryKey key = ConversationHistoryCassandra.ConversationHistoryKey.builder()
                .sessionId(sessionId)
                .timestamp(Instant.now())
                .messageId(UUID.randomUUID())
                .build();
        ConversationHistoryCassandra history = ConversationHistoryCassandra.builder()
                .key(key)
                .userId(userId)
                .role("assistant")
                .content(content)
                .createdAt(Instant.now())
                .build();
        cassandraRepository.save(history);

        // 清除缓存
        clearCache(sessionId);

        log.debug("AI 回复已保存 - 会话ID: {}", sessionId);
    }

    @Override
    public List<Message> getFullHistory(Long userId, String sessionId) {
        log.info("获取完整对话历史 - 用户ID: {}, 会话ID: {}", userId, sessionId);

        if (!hasAccess(userId, sessionId)) {
            log.warn("用户无权访问该会话");
            return new ArrayList<>();
        }

        // 从 Cassandra 获取所有消息
        List<ConversationHistoryCassandra> histories = cassandraRepository.findBySessionId(sessionId);

        return histories.stream()
                .sorted(Comparator.comparing(h -> h.getKey().getTimestamp()))
                .map(h -> {
                    if ("user".equals(h.getRole())) {
                        return new UserMessage(h.getContent());
                    } else {
                        return new AssistantMessage(h.getContent());
                    }
                })
                .collect(Collectors.toList());
    }

    @Override
    public void clearHistory(Long userId, String sessionId) {
        log.info("清空会话历史 - 用户ID: {}, 会话ID: {}", userId, sessionId);

        if (!hasAccess(userId, sessionId)) {
            log.warn("用户无权访问该会话");
            return;
        }

        // 从 Cassandra 删除
        cassandraRepository.deleteBySessionId(sessionId);

        // 清除缓存
        clearCache(sessionId);

        log.info("会话历史已清空 - 会话ID: {}", sessionId);
    }

    @Override
    public boolean hasAccess(Long userId, String sessionId) {
        // 从数据库验证用户是否拥有该会话
        ConversationSession session = sessionMapper.getBySessionId(sessionId);
        if (session == null) {
            log.warn("会话不存在 - 会话ID: {}", sessionId);
            return false;
        }

        boolean hasAccess = session.getUserId().equals(userId);
        if (!hasAccess) {
            log.warn("用户无权访问该会话 - 用户ID: {}, 会话ID: {}", userId, sessionId);
        }

        return hasAccess;
    }

    /**
     * 清除会话的缓存
     */
    private void clearCache(String sessionId) {
        String cacheKey = MEMORY_CACHE_PREFIX + sessionId;
        redisTemplate.delete(cacheKey);
        log.debug("缓存已清除 - 会话ID: {}", sessionId);
    }
}
