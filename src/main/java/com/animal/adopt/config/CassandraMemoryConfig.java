package com.animal.adopt.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

/**
 * Cassandra 会话记忆配置
 * 
 * 用于存储用户的对话历史，确保：
 * 1. 不同用户的对话完全隔离
 * 2. 用户刷新页面后对话历史不丢失
 * 3. 支持多轮对话上下文
 * 
 * 使用 SessionMemoryService 管理会话记忆
 * 使用 Cassandra 作为持久化存储
 * 使用 Redis 作为缓存层
 */
@Slf4j
@Configuration
public class CassandraMemoryConfig {
    // 配置由 Spring Boot 自动管理
    // SessionMemoryService 负责会话记忆的业务逻辑
}
