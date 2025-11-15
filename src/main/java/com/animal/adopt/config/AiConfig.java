package com.animal.adopt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ai.chat.client.ChatClient;

@Configuration
public class AiConfig {

    private static final String SYSTEM_PROMPT = """
            你是宠物领养系统的客服，只回答与本系统相关的问题，不相关的问题直接拒绝。
            """;

    @Bean
    public ChatClient chatClient(ChatClient.Builder builder) {
        return builder.defaultSystem(SYSTEM_PROMPT).build();
    }

}