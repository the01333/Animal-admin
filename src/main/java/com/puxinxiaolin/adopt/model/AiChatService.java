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

/**
 * @Description: 提供大模型对话能力
 * @Author: YCcLin
 * @Date: 2026/3/5 19:39
 */
@Slf4j
@Service
public class AiChatService {

    @Autowired
    private ChatClient chatClient;

    @Autowired
    private SessionMemoryService sessionMemoryService;

    /**
     * 流式记忆会话
     *
     * @param sessionId
     * @param content
     * @param userId
     * @return
     */
    public Flux<String> chatWithMemoryStream(String sessionId, String content, Long userId) {
        log.info("流式多轮对话, 会话ID: {}, 用户ID: {}, 问题: {}", sessionId, userId, content);

        // 验证用户权限（确保用户隔离）
        if (!sessionMemoryService.hasAccess(userId, sessionId)) {
            log.warn("用户无权访问该会话");
            return Flux.error(new RuntimeException("用户无权访问该会话"));
        }

        // 获取会话历史（从 cassandra 获取最近 10 条消息用于上下文）
        List<Message> conversationHistory = sessionMemoryService.getSessionHistory(userId, sessionId, 10);

        List<Message> messages = new ArrayList<>();
        messages.add(new SystemMessage(ModelSystemPrompt.SYSTEM_PROMPT));
        messages.addAll(conversationHistory);
        messages.add(new UserMessage(content == null ? "" : content));

        // 保存用户消息到 Cassandra（确保不丢失）
        sessionMemoryService.addUserMessage(userId, sessionId, content);

        Prompt prompt = new Prompt(messages);
        return chatClient.prompt(prompt)
                .stream()
                .content()
//                // 开始订阅时（即前端开始请求时）回调
//                .doOnSubscribe(subscription -> log.info("开始流式响应 - 会话ID: {}", sessionId))
//                // 每收到一个数据块时回调
//                .doOnNext(chunk -> {
//                    if (log.isDebugEnabled()) {
//                        log.debug("发送数据块: {} 字符", chunk.length());
//                    }
//                })
//                // 响应全部时回调
//                .doOnComplete(() -> log.info("流式响应完成 - 会话ID: {}", sessionId))
//                // 响应发生异常时回调
//                .doOnError(e -> log.error("流式响应错误 - 会话ID: {}, 错误类型: {}, 错误信息: {}", sessionId, e.getClass().getSimpleName(), e.getMessage(), e))
//                // 响应主动被取消时回调
//                .doOnCancel(() -> log.warn("流式响应被取消 - 会话ID: {}", sessionId))
//                .onErrorResume(e -> {
//                    // 优雅降级：返回错误提示而不是直接中断
//                    log.error("流式响应异常，返回错误提示 - 会话ID: {}", sessionId, e);
//                    return Flux.just("抱歉，我在处理您的问题时遇到了一些技术问题。请稍后再试，或者换个方式提问。如果问题持续存在，请联系客服人员。");
//                })
                ;
        // MY_KEY: 完整的AI回复内容由前端收集后通过 /save-message 接口保存
    }
}