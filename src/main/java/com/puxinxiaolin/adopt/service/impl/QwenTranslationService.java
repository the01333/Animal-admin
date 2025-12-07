package com.puxinxiaolin.adopt.service.impl;

import com.puxinxiaolin.adopt.service.TranslationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * 基于 Spring AI + Qwen 的翻译服务实现
 */
@Slf4j
@Service
public class QwenTranslationService implements TranslationService {

    private final RestClient restClient;
    private final String model;

    public QwenTranslationService(
            @Value("${spring.ai.openai.base-url}") String baseUrl,
            @Value("${spring.ai.openai.api-key}") String apiKey,
            @Value("${spring.ai.openai.chat.options.model:qwen-plus}") String model) {
        this.model = model;
        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Override
    public String translateToEnglishKey(String label) {
        if (label == null) {
            return null;
        }
        String trimmed = label.trim();
        if (trimmed.isEmpty()) {
            return null;
        }

        String system = """
                你是一个专业的术语翻译助手。
                任务：将给定的中文宠物类别翻译成适合作为代码/数据库键使用的英文标识。
                要求：
                1. 只输出小写英文。
                2. 只包含字母、数字和下划线。
                3. 单词之间使用下划线分隔。
                4. 不要输出任何解释、标点或其他多余内容，只输出转换后的标识本身。
                示例：
                松鼠 -> squirrel
                金毛 -> golden_retriever
                英短 -> british_shorthair
                """;

        String user = "中文宠物类别：" + trimmed;

        try {
            Map<String, Object> request = new HashMap<>();
            request.put("model", model);
            request.put("temperature", 0.0);
            List<Map<String, String>> messages = List.of(
                    Map.of("role", "system", "content", system),
                    Map.of("role", "user", "content", user)
            );
            request.put("messages", messages);

            @SuppressWarnings("unchecked")
            Map<String, Object> response = restClient.post()
                    .uri("/v1/chat/completions")
                    .body(request)
                    .retrieve()
                    .body(Map.class);

            if (response == null) {
                return null;
            }

            Object choicesObj = response.get("choices");
            if (!(choicesObj instanceof List<?> choices) || choices.isEmpty()) {
                return null;
            }
            Object firstChoice = choices.getFirst();
            if (!(firstChoice instanceof Map<?, ?> firstMap)) {
                return null;
            }
            Object messageObj = firstMap.get("message");
            if (!(messageObj instanceof Map<?, ?> msg)) {
                return null;
            }
            Object contentObj = msg.get("content");
            if (!(contentObj instanceof String raw)) {
                return null;
            }

            String key = raw.trim().toLowerCase(Locale.ROOT);
            // 只保留 [a-z0-9_]，其他全部替换为下划线
            key = key.replaceAll("[^a-z0-9_]+", "_");
            key = key.replaceAll("^_+|_+$", "");
            if (key.isEmpty()) {
                return null;
            }
            if (key.length() > 32) {
                key = key.substring(0, 32);
            }
            return key;
        } catch (Exception e) {
            log.error("调用 Qwen 翻译宠物类别失败, label={}", trimmed, e);
            return null;
        }
    }
}
