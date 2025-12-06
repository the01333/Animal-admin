package com.puxinxiaolin.adopt.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import reactor.core.publisher.Flux;

/**
 * 流式对话结果，包含会话ID与流式内容
 */
@Data
@AllArgsConstructor
public class ChatStreamResult {
    private String sessionId;
    private Flux<String> stream;
}
