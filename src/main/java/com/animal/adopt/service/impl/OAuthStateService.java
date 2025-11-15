package com.animal.adopt.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class OAuthStateService {
    private final RedisTemplate<String, Object> redisTemplate;
    public String createState() {
        String state = UUID.randomUUID().toString().replace("-", "");
        redisTemplate.opsForValue().set("oauth:state:" + state, 1, 5, TimeUnit.MINUTES);
        return state;
    }
    public boolean verifyAndConsume(String state) {
        String key = "oauth:state:" + state;
        Boolean exists = redisTemplate.hasKey(key);
        if (Boolean.TRUE.equals(exists)) {
            redisTemplate.delete(key);
            return true;
        }
        return false;
    }
}