package com.animal.adopt.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Redis 配置类
 * 配置Redis序列化方式和RedisTemplate
 */
@Configuration
@EnableCaching
public class RedisConfig {
    
    /**
     * 配置自定义 RedisTemplate
     * 使用 Jackson 序列化器替代默认的 JDK 序列化器
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        
        // 使用 GenericJackson2JsonRedisSerializer 替代 Jackson2JsonRedisSerializer
        // 这是 Spring Data Redis 3.x 推荐的方式，自动处理类型信息
        GenericJackson2JsonRedisSerializer jsonSerializer = new GenericJackson2JsonRedisSerializer(createObjectMapper());
        
        // 配置 String 序列化器
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        
        // key 采用 String 的序列化方式
        template.setKeySerializer(stringRedisSerializer);
        // hash 的 key 也采用 String 的序列化方式
        template.setHashKeySerializer(stringRedisSerializer);
        
        // value 序列化方式采用 GenericJackson2JsonRedisSerializer
        template.setValueSerializer(jsonSerializer);
        // hash 的 value 序列化方式采用 GenericJackson2JsonRedisSerializer
        template.setHashValueSerializer(jsonSerializer);
        
        // 开启默认序列化
        template.setEnableDefaultSerializer(true);
        template.setEnableTransactionSupport(false);
        
        // 初始化 RedisTemplate
        template.afterPropertiesSet();
        
        return template;
    }
    
    /**
     * 创建自定义的 ObjectMapper
     * 支持 Java 8 时间类型和其他特性
     */
    private ObjectMapper createObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        
        // 设置可见性：允许访问所有字段
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        
        // 启用默认类型，用于序列化时保存类型信息
        // 这样反序列化时可以还原为正确的类型
        objectMapper.activateDefaultTyping(
            LaissezFaireSubTypeValidator.instance,
            ObjectMapper.DefaultTyping.NON_FINAL,
            JsonTypeInfo.As.PROPERTY
        );
        
        // 注册 JavaTimeModule 以支持 Java 8 时间类型（LocalDateTime, LocalDate 等）
        objectMapper.registerModule(new JavaTimeModule());
        
        return objectMapper;
    }
}

