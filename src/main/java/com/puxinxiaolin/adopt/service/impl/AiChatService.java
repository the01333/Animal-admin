package com.puxinxiaolin.adopt.service.impl;

import com.puxinxiaolin.adopt.model.ModelSystemPrompt;
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
    
    public Flux<String> chatStream(String content) {
        String system = ModelSystemPrompt.SYSTEM_PROMPT;
        Message user = new UserMessage(content == null ? "" : content);
        Prompt prompt = new Prompt(List.of(new SystemMessage(system), user));

        return chatClient.prompt(prompt)
                .stream()
                .content();
    }

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
        log.info("流式多轮对话, 会话ID: {}, 用户ID: {}", sessionId, userId);

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

        // 5. 调用AI流式接口
        Prompt prompt = new Prompt(messages);
        return chatClient.prompt(prompt)
                .stream()
                .content();
        // 注意: 完整的AI回复内容由前端收集后通过 /save-message 接口保存
    }

    /**
     * 按换行符分割字符串并返回 Flux
     */
    private Flux<String> splitByNewline(String text) {
        if (text == null || text.isEmpty()) {
            return Flux.empty();
        }

        String[] lines = text.split("\n");
        List<String> result = new ArrayList<>();

        for (String line : lines) {
            if (!line.isEmpty()) {
                result.add(line + "\n");
            }
        }

        return Flux.fromIterable(result);
    }
}