package com.animal.adopt.service.impl;

import com.animal.adopt.service.AiToolService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AiChatService {

    private final ChatClient chatClient;
    private final AiToolService aiToolService;

    public String chat(String content) {
        String system = "你是宠物领养系统的客服。只回答与本系统相关的问题；需要查询时请调用工具。";
        Message user = new UserMessage(content == null ? "" : content);
        Prompt prompt = new Prompt(List.of(new SystemMessage(system), user));
        
        return chatClient.prompt(prompt)
                .tools(aiToolService)
                .call()
                .content();
    }
}