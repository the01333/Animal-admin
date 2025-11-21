package com.animal.adopt.controller;

import com.animal.adopt.common.Result;
import com.animal.adopt.entity.po.ConversationSession;
import com.animal.adopt.entity.vo.ConversationSessionVO;
import com.animal.adopt.service.ConversationService;
import com.animal.adopt.service.impl.AiChatService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.List;
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
     * 单轮对话（不使用会话记忆）
     */
    @PostMapping("/chat")
    public Result<String> chat(@RequestBody Map<String, Object> body, HttpServletRequest request) {
        String content = body.get("content") == null ? "" : body.get("content").toString();
        String ip = request.getRemoteAddr();
        String key = "ai:limit:" + ip;
        Boolean exists = redisTemplate.hasKey(key);
        if (exists) {
            return Result.error("请求过于频繁，请稍后再试");
        }
        redisTemplate.opsForValue().set(key, 1, java.time.Duration.ofSeconds(10));
        String reply = aiChatService.chat(content);
        return Result.success(reply);
    }

    /**
     * 多轮对话（使用会话记忆）
     */
    @PostMapping("/chat-with-memory")
    public Result<Map<String, Object>> chatWithMemory(@RequestBody Map<String, Object> body, HttpServletRequest request,
                                        @RequestAttribute(name = "userId", required = false) Long userId) {
        if (userId == null) {
            return Result.error("用户未登录");
        }

        String content = body.get("content") == null ? "" : body.get("content").toString();
        String sessionId = body.get("sessionId") == null ? "" : body.get("sessionId").toString();

        // 如果没有会话ID，创建新会话
        if (sessionId.isEmpty()) {
            ConversationSession session = conversationService.createSession(userId, "新对话");
            sessionId = session.getSessionId();
        }

        // 请求限流
        String ip = request.getRemoteAddr();
        String key = "ai:limit:" + ip;
        Boolean exists = redisTemplate.hasKey(key);
        if (exists) {
            return Result.error("请求过于频繁，请稍后再试");
        }
        redisTemplate.opsForValue().set(key, 1, Duration.ofSeconds(10));

        try {
            String reply = aiChatService.chatWithMemory(sessionId, content, userId);
            // 返回包含sessionId的响应数据
            Map<String, Object> responseData = new java.util.HashMap<>();
            responseData.put("reply", reply);
            responseData.put("sessionId", sessionId);
            return Result.success(responseData);
        } catch (Exception e) {
            log.error("多轮对话出错", e);
            return Result.error("对话服务出错，请稍后重试");
        }
    }

    /**
     * 获取用户的所有会话
     */
    @GetMapping("/sessions")
    public Result<List<ConversationSessionVO>> getSessions(@RequestAttribute(name = "userId", required = false) Long userId) {
        if (userId == null) {
            return Result.error("用户未登录");
        }

        List<ConversationSessionVO> sessions = conversationService.getUserSessions(userId);
        return Result.success(sessions);
    }

    /**
     * 获取会话详情
     */
    @GetMapping("/session/{sessionId}")
    public Result<ConversationSessionVO> getSessionDetail(
            @PathVariable String sessionId,
            @RequestAttribute(name = "userId", required = false) Long userId
    ) {
        if (userId == null) {
            return Result.error("用户未登录");
        }

        ConversationSessionVO session = conversationService.getSessionDetail(sessionId, userId);
        if (session == null) {
            return Result.error("会话不存在");
        }

        return Result.success(session);
    }

    /**
     * 创建新会话
     */
    @PostMapping("/session/create")
    public Result<ConversationSessionVO> createSession(
            @RequestBody Map<String, Object> body,
            @RequestAttribute(name = "userId", required = false) Long userId
    ) {
        if (userId == null) {
            return Result.error("用户未登录");
        }

        String title = body.get("title") == null ? "新对话" : body.get("title").toString();
        ConversationSession session = conversationService.createSession(userId, title);

        ConversationSessionVO vo = ConversationSessionVO.builder()
                .id(session.getId())
                .sessionId(session.getSessionId())
                .title(session.getTitle())
                .status(session.getStatus())
                .messageCount(0)
                .build();

        return Result.success(vo);
    }

    /**
     * 关闭会话
     */
    @PostMapping("/session/{sessionId}/close")
    public Result<Void> closeSession(
            @PathVariable String sessionId,
            @RequestAttribute(name = "userId", required = false) Long userId
    ) {
        if (userId == null) {
            return Result.error("用户未登录");
        }

        conversationService.closeSession(sessionId, userId);
        return Result.success();
    }

    /**
     * 删除会话
     */
    @DeleteMapping("/session/{sessionId}")
    public Result<Void> deleteSession(
            @PathVariable String sessionId,
            @RequestAttribute(name = "userId", required = false) Long userId
    ) {
        if (userId == null) {
            return Result.error("用户未登录");
        }

        conversationService.deleteSession(sessionId, userId);
        return Result.success();
    }

    /**
     * 清空会话消息
     */
    @PostMapping("/session/{sessionId}/clear")
    public Result<Void> clearSession(
            @PathVariable String sessionId,
            @RequestAttribute(name = "userId", required = false) Long userId
    ) {
        if (userId == null) {
            return Result.error("用户未登录");
        }

        conversationService.clearSessionMessages(sessionId, userId);
        return Result.success();
    }
}
