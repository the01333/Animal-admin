package com.animal.adopt.controller;

import com.animal.adopt.common.Result;
import com.animal.adopt.service.impl.AiChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.redis.core.RedisTemplate;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/ai/service")
@RequiredArgsConstructor
public class IntelligentCustomerServiceController {

    private final AiChatService aiChatService;
    private final RedisTemplate<String, Object> redisTemplate;

    @PostMapping("/chat")
    public Result<String> chat(@RequestBody java.util.Map<String, Object> body, HttpServletRequest request) {
        String content = body.get("content") == null ? "" : body.get("content").toString();
        String ip = request.getRemoteAddr();
        String key = "ai:limit:" + ip;
        Boolean exists = redisTemplate.hasKey(key);
        if (Boolean.TRUE.equals(exists)) {
            return Result.error("请求过于频繁，请稍后再试");
        }
        redisTemplate.opsForValue().set(key, 1, java.time.Duration.ofSeconds(10));
        String reply = aiChatService.chat(content);
        return Result.success(reply);
    }
}
