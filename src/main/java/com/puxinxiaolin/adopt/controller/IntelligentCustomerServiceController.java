package com.puxinxiaolin.adopt.controller;

import com.puxinxiaolin.adopt.context.UserContext;
import com.puxinxiaolin.adopt.entity.entity.ConversationSession;
import com.puxinxiaolin.adopt.entity.vo.ConversationSessionVO;
import com.puxinxiaolin.adopt.service.ConversationService;
import com.puxinxiaolin.adopt.service.SessionMemoryService;
import com.puxinxiaolin.adopt.service.impl.AiChatService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/ai/service")
@RequiredArgsConstructor
public class IntelligentCustomerServiceController {

    private final AiChatService aiChatService;
    private final ConversationService conversationService;
    private final SessionMemoryService sessionMemoryService;
    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 流式单轮对话（不使用会话记忆）
     */
    @PostMapping(value = "/chat-stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> chatStream(@RequestBody Map<String, Object> body, HttpServletRequest request) {
        String content = body.get("content") == null ? "" : body.get("content").toString();
        String ip = request.getRemoteAddr();
        String key = "ai:limit:" + ip;
        Boolean exists = redisTemplate.hasKey(key);
        if (exists) {
            return Flux.error(new RuntimeException("请求过于频繁, 请稍后再试"));
        }

        redisTemplate.opsForValue().set(key, 1, Duration.ofSeconds(10));
        return aiChatService.chatStream(content)
                .map(this::escapeJsonString);
    }

    /**
     * 流式多轮对话（使用会话记忆）
     * 认证由拦截器处理, 这里直接从上下文获取用户ID
     */
    @PostMapping(value = "/chat-with-memory-stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> chatWithMemoryStream(
            @RequestBody Map<String, Object> body,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        // 从上下文获取用户ID（拦截器已验证登录状态并存储到上下文）
        Long userId = UserContext.getUserId();

        String content = body.get("content") == null ? "" : body.get("content").toString();
        String sessionId = body.get("sessionId") == null ? "" : body.get("sessionId").toString();

        // 如果没有会话ID, 创建新会话
        if (sessionId.isEmpty()) {
            ConversationSession session = conversationService.createSession(userId, "新对话");
            sessionId = session.getSessionId();
        }

        // 在响应头中返回 sessionId
        response.setHeader("X-Session-Id", sessionId);

        // 请求限流
        String ip = request.getRemoteAddr();
        String key = "ai:limit:" + ip;
        Boolean exists = redisTemplate.hasKey(key);
        if (exists) {
            return Flux.error(new RuntimeException("请求过于频繁, 请稍后再试"));
        }

        redisTemplate.opsForValue().set(key, 1, Duration.ofSeconds(10));

        final String finalSessionId = sessionId;
        return aiChatService.chatWithMemoryStream(finalSessionId, content, userId)
                .map(this::escapeJsonString);
    }

    /**
     * 保存AI回复内容到数据库
     * 用于流式对话完成后, 前端将收集到的完整内容保存
     * 认证由拦截器处理, 这里直接从上下文获取用户ID
     * <p>
     * 使用 SessionMemoryService 确保：
     * 1. 用户隔离 - 不同用户的对话完全分离
     * 2. 持久化 - 对话历史保存到 Cassandra
     * 3. 缓存加速 - 使用 Redis 缓存热数据
     */
    @PostMapping("/save-message")
    public Map<String, Object> saveMessage(
            @RequestBody Map<String, Object> body
    ) {
        // 从上下文获取用户ID（拦截器已验证登录状态并存储到上下文）
        Long userId = UserContext.getUserId();

        String sessionId = body.get("sessionId") == null ? "" : body.get("sessionId").toString();
        String role = body.get("role") == null ? "assistant" : body.get("role").toString();
        String content = body.get("content") == null ? "" : body.get("content").toString();

        if (sessionId.isEmpty()) {
            return Map.of("code", 400, "message", "会话ID不能为空");
        }

        try {
            // 同时保存到 MySQL 和 Cassandra
            conversationService.saveMessage(sessionId, userId, role, content, null, null, null);

            // 如果是 AI 回复, 也保存到会话记忆服务
            if ("assistant".equals(role)) {
                sessionMemoryService.addAssistantMessage(userId, sessionId, content);
            }

            return Map.of("code", 200, "message", "消息保存成功");
        } catch (Exception e) {
            log.error("保存消息失败: {}", e.getMessage(), e);
            return Map.of("code", 500, "message", "消息保存失败");
        }
    }

    /**
     * 获取会话的聊天记录
     * 用于页面刷新时恢复聊天记录
     */
    @GetMapping("/session/{sessionId}/messages")
    public Map<String, Object> getSessionMessages(@PathVariable String sessionId) {
        Long userId = UserContext.getUserId();

        try {
            ConversationSessionVO sessionVO = conversationService.getSessionDetail(sessionId, userId);
            if (sessionVO == null) {
                return Map.of("code", 404, "message", "会话不存在或无权限访问");
            }

            return Map.of(
                    "code", 200,
                    "message", "获取成功",
                    "data", sessionVO.getMessages()
            );
        } catch (Exception e) {
            log.error("获取会话消息失败: {}", e.getMessage(), e);
            return Map.of("code", 500, "message", "获取失败");
        }
    }

    /**
     * 删除会话及其所有消息（真正删除, 不是逻辑删除）
     * 用于登出时清空会话数据
     */
    @DeleteMapping("/session/{sessionId}")
    public Map<String, Object> deleteSession(@PathVariable String sessionId) {
        Long userId = UserContext.getUserId();

        try {
            conversationService.deleteSession(sessionId, userId);
            return Map.of("code", 200, "message", "会话已删除");
        } catch (Exception e) {
            log.error("删除会话失败: {}", e.getMessage(), e);
            return Map.of("code", 500, "message", "删除失败");
        }
    }

    /**
     * 转义JSON字符串, 保留换行符等特殊字符
     */
    private String escapeJsonString(String str) {
        if (str == null) {
            return "\"\"";
        }
        // 使用ObjectMapper来进行JSON序列化, 确保正确转义
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(str);
        } catch (Exception e) {
            // 如果序列化失败, 使用手动转义
            StringBuilder sb = new StringBuilder("\"");
            for (char c : str.toCharArray()) {
                switch (c) {
                    case '"':
                        sb.append("\\\"");
                        break;
                    case '\\':
                        sb.append("\\\\");
                        break;
                    case '\b':
                        sb.append("\\b");
                        break;
                    case '\f':
                        sb.append("\\f");
                        break;
                    case '\n':
                        sb.append("\\n");
                        break;
                    case '\r':
                        sb.append("\\r");
                        break;
                    case '\t':
                        sb.append("\\t");
                        break;
                    default:
                        if (c < 32) {
                            sb.append(String.format("\\u%04x", (int) c));
                        } else {
                            sb.append(c);
                        }
                }
            }
            sb.append("\"");
            return sb.toString();
        }
    }
}
