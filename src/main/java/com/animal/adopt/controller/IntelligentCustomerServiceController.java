package com.animal.adopt.controller;

import com.animal.adopt.entity.po.ConversationSession;
import com.animal.adopt.service.ConversationService;
import com.animal.adopt.service.impl.AiChatService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
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
     */
    @PostMapping(value = "/chat-with-memory-stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> chatWithMemoryStream(
            @RequestBody Map<String, Object> body,
            HttpServletRequest request,
            @RequestAttribute(name = "userId", required = false) Long userId
    ) {
        if (userId == null) {
            return Flux.error(new RuntimeException("用户未登录"));
        }

        String content = body.get("content") == null ? "" : body.get("content").toString();
        String sessionId = body.get("sessionId") == null ? "" : body.get("sessionId").toString();

        // 如果没有会话ID, 创建新会话
        if (sessionId.isEmpty()) {
            ConversationSession session = conversationService.createSession(userId, "新对话");
            sessionId = session.getSessionId();
        }

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
