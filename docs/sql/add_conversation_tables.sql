-- ============================================
-- 宠物领养管理系统 - 会话记忆表
-- 版本: 1.0.0
-- 创建时间: 2025-11-23
-- 说明: 用于AI客服多轮对话的会话管理
-- ============================================

USE `animal_adopt`;

-- ============================================
-- 会话表
-- ============================================
DROP TABLE IF EXISTS `t_conversation_history`;
DROP TABLE IF EXISTS `t_conversation_session`;

-- 1. 会话表
CREATE TABLE `t_conversation_session` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `session_id` VARCHAR(100) NOT NULL UNIQUE COMMENT '会话ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `title` VARCHAR(255) DEFAULT NULL COMMENT '会话标题',
    `description` TEXT DEFAULT NULL COMMENT '会话描述',
    `message_count` INT DEFAULT 0 COMMENT '消息数量',
    `status` VARCHAR(20) DEFAULT 'active' COMMENT '会话状态: active(活跃), closed(已关闭)',
    `last_message` TEXT DEFAULT NULL COMMENT '最后一条消息',
    `last_message_time` DATETIME DEFAULT NULL COMMENT '最后消息时间',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `closed_time` DATETIME DEFAULT NULL COMMENT '关闭时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '是否删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_session_id` (`session_id`),
    KEY `idx_user_id_status_update_time` (`user_id`, `status`, `update_time`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='AI客服会话表';

-- 2. 对话历史表
CREATE TABLE `t_conversation_history` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `session_id` VARCHAR(100) NOT NULL COMMENT '会话ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `role` VARCHAR(20) NOT NULL COMMENT '角色: user(用户), assistant(AI)',
    `content` LONGTEXT NOT NULL COMMENT '消息内容',
    `tool_name` VARCHAR(100) DEFAULT NULL COMMENT '工具名称',
    `tool_params` JSON DEFAULT NULL COMMENT '工具参数',
    `tool_result` LONGTEXT DEFAULT NULL COMMENT '工具执行结果',
    `timestamp` DATETIME DEFAULT NULL COMMENT '消息时间戳',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '是否删除',
    PRIMARY KEY (`id`),
    KEY `idx_session_id_create_time` (`session_id`, `create_time`),
    KEY `idx_user_id_role` (`user_id`, `role`),
    KEY `idx_session_id` (`session_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='AI客服对话历史表';

-- ============================================
-- 索引说明
-- ============================================
-- t_conversation_session:
--   uk_session_id: 会话ID唯一索引，用于快速查找会话
--   idx_user_id_status_update_time: 复合索引，用于查询用户的活跃会话
--   idx_user_id: 用户ID索引，用于查询用户所有会话
--
-- t_conversation_history:
--   idx_session_id_create_time: 复合索引，用于查询会话的对话历史
--   idx_user_id_role: 复合索引，用于查询用户的特定角色消息
--   idx_session_id: 会话ID索引，用于快速查找会话的消息

-- ============================================
-- 初始化数据（可选）
-- ============================================
-- 暂无初始数据，会话由用户创建时自动生成
