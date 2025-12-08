package com.puxinxiaolin.adopt.controller;

import com.puxinxiaolin.adopt.common.Result;
import com.puxinxiaolin.adopt.entity.dto.ChatStreamRequestDTO;
import com.puxinxiaolin.adopt.entity.dto.ChatStreamResult;
import com.puxinxiaolin.adopt.entity.dto.SaveMessageDTO;
import com.puxinxiaolin.adopt.service.IntelligentCustomerService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@Slf4j
@RestController
@RequestMapping("/ai/service")
@RequiredArgsConstructor
public class IntelligentCustomerServiceController {

    private final IntelligentCustomerService intelligentCustomerService;

    /**
     * 流式单轮对话（不使用会话记忆）
     */
    @PostMapping(value = "/chat-stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> chatStream(@RequestBody ChatStreamRequestDTO body, HttpServletRequest request) {
        ChatStreamResult result = intelligentCustomerService.chatStream(body, request.getRemoteAddr());
        return result.getStream();
    }

    /**
     * 流式多轮对话（使用会话记忆）
     * 认证由拦截器处理, 这里直接从上下文获取用户ID
     */
    @PostMapping(value = "/chat-with-memory-stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> chatWithMemoryStream(
            @RequestBody ChatStreamRequestDTO body,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        ChatStreamResult result = intelligentCustomerService.chatWithMemoryStream(body, request.getRemoteAddr());
        if (result.getSessionId() != null) {
            response.setHeader("X-Session-Id", result.getSessionId());
        }
        return result.getStream();
    }

    /**
     * 保存AI回复内容到数据库
     * <p>
     * 认证由拦截器处理, 这里直接从上下文获取用户 ID
     * <p>
     * 通过自定义会话存储服务确保: <p>
     * 1. 用户隔离 - 不同用户的对话完全分离 <p>
     * 2. 持久化 - 对话历史保存到 Cassandra <p>
     * 3. 缓存加速 - 使用 Redis 缓存热数据
     */
    @PostMapping("/save-message")
    public Result<String> saveMessage(@RequestBody SaveMessageDTO body) {
        return intelligentCustomerService.saveMessage(body);
    }

    /**
     * 获取会话的聊天记录
     * <p>
     * 用于页面刷新时恢复聊天记录
     */
    @GetMapping("/session/{sessionId}/messages")
    public Result<Object> getSessionMessages(@PathVariable String sessionId) {
        return intelligentCustomerService.getSessionMessages(sessionId);
    }

    /**
     * 删除会话及其所有消息（非逻辑删除）
     * <p>
     * 用于登出时清空会话数据, 减少内存成本
     */
    @DeleteMapping("/session/{sessionId}")
    public Result<String> deleteSession(@PathVariable String sessionId) {
        return intelligentCustomerService.deleteSession(sessionId);
    }

}
