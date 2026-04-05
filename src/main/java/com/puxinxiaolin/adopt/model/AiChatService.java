package com.puxinxiaolin.adopt.model;

import com.puxinxiaolin.adopt.service.SessionMemoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class AiChatService {

    @Autowired
    private ChatClient chatClient;
    
    @Autowired
    private SessionMemoryService sessionMemoryService;

    /**
     * 流式多轮对话（使用会话记忆）
     * <p>
     * 核心特性:
     * 1. 用户隔离 - 不同用户的对话完全分离
     * 2. 持久化 - 对话历史保存到 Cassandra
     * 3. 缓存加速 - 使用 Redis 缓存热数据
     * 4. 多轮对话 - 支持完整的对话上下文
     *
     * @param sessionId 会话ID
     * @param content   用户输入内容
     * @param userId    用户ID
     * @return 流式AI回复
     */
    public Flux<String> chatWithMemoryStream(String sessionId, String content, Long userId) {
        log.info("流式多轮对话, 会话ID: {}, 用户ID: {}, 问题: {}", sessionId, userId, content);

        // 1. 验证用户权限（确保用户隔离）
        if (!sessionMemoryService.hasAccess(userId, sessionId)) {
            log.warn("用户无权访问该会话");
            return Flux.error(new RuntimeException("用户无权访问该会话"));
        }

        // 2. 获取会话历史（最近10条消息用于上下文）
        List<Message> conversationHistory = sessionMemoryService.getSessionHistory(userId, sessionId, 10);

        // 3. 构建消息列表
        List<Message> messages = new ArrayList<>();
        messages.add(new SystemMessage(ModelSystemPrompt.SYSTEM_PROMPT));
        messages.addAll(conversationHistory);
        messages.add(new UserMessage(content == null ? "" : content));

        // 4. 保存用户消息到 Cassandra（确保不丢失）
        sessionMemoryService.addUserMessage(userId, sessionId, content);

        // 5. 调用AI流式接口，添加详细的监控日志和错误处理
        Prompt prompt = new Prompt(messages);
        return chatClient.prompt(prompt)
                .stream()
                .content()
                .doOnSubscribe(subscription -> log.info("🚀 开始流式响应 - 会话ID: {}", sessionId))
                .doOnNext(chunk -> {
                    if (log.isDebugEnabled()) {
                        log.debug("✅ 发送数据块: {} 字符", chunk.length());
                    }
                })
                .doOnComplete(() -> log.info("✅ 流式响应完成 - 会话ID: {}", sessionId))
                .doOnError(e -> {
                    log.error("❌ 流式响应错误 - 会话ID: {}, 错误类型: {}, 错误信息: {}", 
                            sessionId, e.getClass().getSimpleName(), e.getMessage(), e);
                })
                .doOnCancel(() -> log.warn("⚠️ 流式响应被取消 - 会话ID: {}", sessionId))
                .onErrorResume(e -> {
                    // 优雅降级：返回错误提示而不是直接中断
                    log.error("💥 流式响应异常，返回错误提示 - 会话ID: {}", sessionId, e);
                    return Flux.just("抱歉，我在处理您的问题时遇到了一些技术问题。请稍后再试，或者换个方式提问。如果问题持续存在，请联系客服人员。");
                });
        // MY_KEY: 完整的AI回复内容由前端收集后通过 /save-message 接口保存
    }
}