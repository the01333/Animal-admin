package com.puxinxiaolin.adopt.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.puxinxiaolin.adopt.common.Result;
import com.puxinxiaolin.adopt.common.ResultCode;
import com.puxinxiaolin.adopt.context.UserContext;
import com.puxinxiaolin.adopt.entity.dto.ChatStreamRequestDTO;
import com.puxinxiaolin.adopt.entity.dto.ChatStreamResult;
import com.puxinxiaolin.adopt.entity.dto.SaveMessageDTO;
import com.puxinxiaolin.adopt.entity.entity.ConversationSession;
import com.puxinxiaolin.adopt.entity.vo.ConversationSessionVO;
import com.puxinxiaolin.adopt.service.ConversationService;
import com.puxinxiaolin.adopt.service.IntelligentCustomerService;
import com.puxinxiaolin.adopt.service.SessionMemoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Duration;

/**
 * 智能客服业务门面实现，承接限流、会话创建、消息保存等逻辑
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class IntelligentCustomerServiceImpl implements IntelligentCustomerService {

    private final AiChatService aiChatService;
    private final ConversationService conversationService;
    private final SessionMemoryService sessionMemoryService;
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public ChatStreamResult chatStream(ChatStreamRequestDTO request, String clientIp) {
        rateLimit(clientIp);
        String content = request.getContent() == null ? "" : request.getContent();
        Flux<String> stream = aiChatService.chatStream(content).map(this::escapeJsonString);
        return new ChatStreamResult(null, stream);
    }

    @Override
    public ChatStreamResult chatWithMemoryStream(ChatStreamRequestDTO request, String clientIp) {
        rateLimit(clientIp);
        Long userId = UserContext.getUserId();
        if (userId == null) {
            return new ChatStreamResult(null, Flux.error(new RuntimeException("未登录")));
        }

        String content = request.getContent() == null ? "" : request.getContent();
        String sessionId = StringUtils.isNotBlank(request.getSessionId()) ? request.getSessionId() : "";
        if (sessionId.isEmpty()) {
            ConversationSession session = conversationService.createSession(userId, "新对话");
            sessionId = session.getSessionId();
        }
        final String finalSessionId = sessionId;
        Flux<String> stream = aiChatService.chatWithMemoryStream(finalSessionId, content, userId)
                .map(this::escapeJsonString);
        return new ChatStreamResult(finalSessionId, stream);
    }

    @Override
    public Result<String> saveMessage(SaveMessageDTO request) {
        Long userId = UserContext.getUserId();
        if (StringUtils.isBlank(request.getSessionId())) {
            return Result.error(ResultCode.BAD_REQUEST.getCode(), "会话ID不能为空");
        }
        String role = StringUtils.defaultIfBlank(request.getRole(), "assistant");
        String content = StringUtils.isNotBlank(request.getContent()) ? request.getContent() : "";
        try {
            conversationService.saveMessage(request.getSessionId(), userId, role, content,
                    request.getToolName(), request.getToolParams(), request.getToolResult());
            if ("assistant".equalsIgnoreCase(role)) {
                sessionMemoryService.addAssistantMessage(userId, request.getSessionId(), content);
            } else {
                sessionMemoryService.addUserMessage(userId, request.getSessionId(), content);
            }
            return Result.success("消息保存成功", null);
        } catch (Exception e) {
            log.error("保存消息失败: {}", e.getMessage(), e);
            return Result.error(ResultCode.ERROR.getCode(), "消息保存失败");
        }
    }

    @Override
    public Result<Object> getSessionMessages(String sessionId) {
        Long userId = UserContext.getUserId();
        try {
            ConversationSessionVO sessionVO = conversationService.getSessionDetail(sessionId, userId);
            if (sessionVO == null) {
                return Result.error(ResultCode.BAD_REQUEST.getCode(), "会话不存在或无权限访问");
            }
            return Result.success(sessionVO.getMessages());
        } catch (Exception e) {
            log.error("获取会话消息失败: {}", e.getMessage(), e);
            return Result.error(ResultCode.ERROR.getCode(), "获取失败");
        }
    }

    @Override
    public Result<String> deleteSession(String sessionId) {
        Long userId = UserContext.getUserId();
        try {
            conversationService.deleteSession(sessionId, userId);
            return Result.success("会话已删除", null);
        } catch (Exception e) {
            log.error("删除会话失败: {}", e.getMessage(), e);
            return Result.error(ResultCode.ERROR.getCode(), "删除失败");
        }
    }

    private void rateLimit(String ip) {
        String key = "ai:limit:" + ip;
        if (redisTemplate.hasKey(key)) {
            throw new RuntimeException("请求过于频繁, 请稍后再试");
        }
        
        redisTemplate.opsForValue().set(key, 1, Duration.ofSeconds(10));
    }

    /**
     * 对 AI 答复进行处理
     *
     * @param str
     * @return
     */
    private String escapeJsonString(String str) {
        if (str == null) {
            return "\"\"";
        }
        try {
            return new ObjectMapper().writeValueAsString(str);
        } catch (Exception e) {
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
            sb.append('"');
            return sb.toString();
        }
    }
}
