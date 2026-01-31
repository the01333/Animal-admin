package com.puxinxiaolin.adopt.config;

import cn.hutool.core.util.StrUtil;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Redisson 配置类
 * <p>
 * 复用 Spring Data Redis 的配置
 * </p>
 */
@Configuration
public class RedissonConfig {

    @Bean
    public RedissonClient redissonClient(RedisProperties redisProperties) {
        Config config = new Config();
        
        String address = "redis://" + redisProperties.getHost() + ":" + redisProperties.getPort();
        
        config.useSingleServer()
                .setAddress(address)
                .setDatabase(redisProperties.getDatabase())
                .setConnectionMinimumIdleSize(4)
                .setConnectionPoolSize(16);

        // 设置密码（如果有）
        String password = redisProperties.getPassword();
        if (StrUtil.isNotBlank(password)) {
            config.useSingleServer().setPassword(password);
        }

        return Redisson.create(config);
    }
}
