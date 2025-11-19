-- ============================================
-- 宠物领养管理系统 - 完整数据库脚本
-- 版本: 3.0.0
-- 创建时间: 2025-11-10
-- 说明: 
-- 1. 整合所有表结构和初始数据
-- 2. 移除触发器, 改用后端接口处理
-- 3. 浏览次数通过Redis缓存+定时任务实现
-- ============================================

-- 创建数据库
CREATE DATABASE IF NOT EXISTS `animal_adopt` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `animal_adopt`;

-- ============================================
-- 核心业务表
-- ============================================

-- 1. 用户表
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `username` VARCHAR(50) DEFAULT NULL COMMENT '用户名',
    `password` VARCHAR(255) DEFAULT NULL COMMENT '密码',
    `nickname` VARCHAR(50) DEFAULT NULL COMMENT '昵称',
    `real_name` VARCHAR(50) DEFAULT NULL COMMENT '真实姓名',
    `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
    `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    `avatar` VARCHAR(500) DEFAULT NULL COMMENT '头像URL',
    `gender` TINYINT DEFAULT 0 COMMENT '性别',
    `age` INT DEFAULT NULL COMMENT '年龄',
    `address` VARCHAR(255) DEFAULT NULL COMMENT '地址',
    `id_card` VARCHAR(20) DEFAULT NULL COMMENT '身份证号',
    `occupation` VARCHAR(100) DEFAULT NULL COMMENT '职业',
    `housing` VARCHAR(100) DEFAULT NULL COMMENT '住房情况',
    `personality` VARCHAR(255) DEFAULT NULL COMMENT '性格特点',
    `has_experience` TINYINT(1) DEFAULT 0 COMMENT '是否有养宠经验',
    `wechat_openid` VARCHAR(100) DEFAULT NULL COMMENT '微信OpenID',
    `wechat_unionid` VARCHAR(100) DEFAULT NULL COMMENT '微信UnionID',
    `qq_openid` VARCHAR(100) DEFAULT NULL COMMENT 'QQ OpenID',
    `register_type` VARCHAR(20) DEFAULT 'username' COMMENT '注册方式',
    `role` VARCHAR(50) NOT NULL DEFAULT 'user' COMMENT '用户角色',
    `status` TINYINT DEFAULT 1 COMMENT '用户状态',
    `certified` TINYINT(1) DEFAULT 0 COMMENT '是否已认证',
    `certification_files` TEXT DEFAULT NULL COMMENT '认证资料URL',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted` TINYINT DEFAULT 0,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`),
    UNIQUE KEY `uk_phone` (`phone`),
    UNIQUE KEY `uk_email` (`email`),
    UNIQUE KEY `uk_wechat_openid` (`wechat_openid`),
    UNIQUE KEY `uk_wechat_unionid` (`wechat_unionid`),
    UNIQUE KEY `uk_qq_openid` (`qq_openid`),
    KEY `idx_role` (`role`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 2. 验证码表
DROP TABLE IF EXISTS `t_verification_code`;
CREATE TABLE `t_verification_code` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `code_type` VARCHAR(20) NOT NULL COMMENT 'email/phone',
    `target` VARCHAR(100) NOT NULL,
    `code` VARCHAR(10) NOT NULL,
    `purpose` VARCHAR(20) NOT NULL COMMENT 'register/login/reset_password',
    `expire_time` DATETIME NOT NULL,
    `is_used` TINYINT(1) DEFAULT 0,
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_target_type` (`target`, `code_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='验证码表';

-- 3. 宠物表
DROP TABLE IF EXISTS `t_pet`;
CREATE TABLE `t_pet` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(100) NOT NULL,
    `category` VARCHAR(50) NOT NULL,
    `breed` VARCHAR(100) DEFAULT NULL,
    `gender` TINYINT DEFAULT 0,
    `age` INT DEFAULT NULL,
    `weight` DECIMAL(5,2) DEFAULT NULL,
    `color` VARCHAR(100) DEFAULT NULL,
    `neutered` TINYINT(1) DEFAULT 0,
    `vaccinated` TINYINT(1) DEFAULT 0,
    `health_status` VARCHAR(50) DEFAULT 'healthy',
    `health_description` TEXT DEFAULT NULL,
    `personality` TEXT DEFAULT NULL,
    `description` TEXT DEFAULT NULL,
    `images` TEXT DEFAULT NULL,
    `cover_image` VARCHAR(500) DEFAULT NULL,
    `rescue_date` DATE DEFAULT NULL,
    `rescue_location` VARCHAR(255) DEFAULT NULL,
    `adoption_requirements` TEXT DEFAULT NULL,
    `adoption_status` VARCHAR(50) NOT NULL DEFAULT 'available',
    `shelf_status` TINYINT DEFAULT 1,
    `view_count` INT DEFAULT 0 COMMENT '浏览次数',
    `like_count` INT DEFAULT 0,
    `favorite_count` INT DEFAULT 0,
    `application_count` INT DEFAULT 0,
    `sort_order` INT DEFAULT 0,
    `create_by` BIGINT DEFAULT NULL,
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted` TINYINT DEFAULT 0,
    PRIMARY KEY (`id`),
    KEY `idx_category` (`category`),
    KEY `idx_adoption_status` (`adoption_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='宠物表';

-- 4. 宠物点赞表
DROP TABLE IF EXISTS `t_pet_like`;
CREATE TABLE `t_pet_like` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `user_id` BIGINT NOT NULL,
    `pet_id` BIGINT NOT NULL,
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `deleted` TINYINT DEFAULT 0,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_pet` (`user_id`, `pet_id`),
    KEY `idx_pet_id` (`pet_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='宠物点赞表';

-- 5. 宠物收藏表
DROP TABLE IF EXISTS `t_pet_favorite`;
CREATE TABLE `t_pet_favorite` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `user_id` BIGINT NOT NULL,
    `pet_id` BIGINT NOT NULL,
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `deleted` TINYINT DEFAULT 0,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_pet` (`user_id`, `pet_id`),
    KEY `idx_pet_id` (`pet_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='宠物收藏表';

-- 6. 领养申请表
DROP TABLE IF EXISTS `t_adoption_application`;
CREATE TABLE `t_adoption_application` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `application_no` VARCHAR(50) NOT NULL,
    `user_id` BIGINT NOT NULL,
    `pet_id` BIGINT NOT NULL,
    `reason` TEXT DEFAULT NULL,
    `family_info` TEXT DEFAULT NULL,
    `careplan` TEXT DEFAULT NULL,
    `additional_info` TEXT DEFAULT NULL,
    `contact_phone` VARCHAR(20) NOT NULL,
    `contact_address` VARCHAR(255) NOT NULL,
    `status` VARCHAR(50) NOT NULL DEFAULT 'pending',
    `reviewer_id` BIGINT DEFAULT NULL,
    `review_time` DATETIME DEFAULT NULL,
    `review_comment` TEXT DEFAULT NULL,
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted` TINYINT DEFAULT 0,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_application_no` (`application_no`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_pet_id` (`pet_id`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='领养申请表';

-- 7. 文章表
DROP TABLE IF EXISTS `t_article`;
CREATE TABLE `t_article` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `title` VARCHAR(200) NOT NULL,
    `category` VARCHAR(50) NOT NULL,
    `cover_image` VARCHAR(500) DEFAULT NULL,
    `summary` VARCHAR(500) DEFAULT NULL,
    `content` LONGTEXT NOT NULL,
    `author` VARCHAR(100) DEFAULT NULL,
    `view_count` INT DEFAULT 0 COMMENT '浏览次数',
    `like_count` INT DEFAULT 0,
    `favorite_count` INT DEFAULT 0,
    `status` TINYINT DEFAULT 1,
    `publish_time` DATETIME DEFAULT NULL,
    `sort_order` INT DEFAULT 0,
    `create_by` BIGINT DEFAULT NULL,
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted` TINYINT DEFAULT 0,
    PRIMARY KEY (`id`),
    KEY `idx_category` (`category`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文章表';

-- 8. 文章点赞表
DROP TABLE IF EXISTS `t_article_like`;
CREATE TABLE `t_article_like` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `user_id` BIGINT NOT NULL,
    `article_id` BIGINT NOT NULL,
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `deleted` TINYINT DEFAULT 0,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_article` (`user_id`, `article_id`),
    KEY `idx_article_id` (`article_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文章点赞表';

-- 9. 文章收藏表
DROP TABLE IF EXISTS `t_article_favorite`;
CREATE TABLE `t_article_favorite` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `user_id` BIGINT NOT NULL,
    `article_id` BIGINT NOT NULL,
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `deleted` TINYINT DEFAULT 0,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_article` (`user_id`, `article_id`),
    KEY `idx_article_id` (`article_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文章收藏表';

-- 10. 会话表
DROP TABLE IF EXISTS `t_chat_session`;
CREATE TABLE `t_chat_session` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `session_type` VARCHAR(50) NOT NULL,
    `user_id` BIGINT NOT NULL,
    `housekeeper_id` BIGINT DEFAULT NULL,
    `pet_id` BIGINT DEFAULT NULL,
    `last_message` TEXT DEFAULT NULL,
    `last_message_time` DATETIME DEFAULT NULL,
    `unread_count` INT DEFAULT 0,
    `status` TINYINT DEFAULT 1,
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted` TINYINT DEFAULT 0,
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_session_type` (`session_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='会话表';

-- 11. 聊天消息表
DROP TABLE IF EXISTS `t_chat_message`;
CREATE TABLE `t_chat_message` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `session_id` BIGINT NOT NULL,
    `sender_id` BIGINT NOT NULL,
    `sender_type` VARCHAR(50) NOT NULL,
    `content` TEXT NOT NULL,
    `message_type` VARCHAR(50) DEFAULT 'text',
    `is_read` TINYINT(1) DEFAULT 0,
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `deleted` TINYINT DEFAULT 0,
    PRIMARY KEY (`id`),
    KEY `idx_session_id` (`session_id`),
    KEY `idx_sender_id` (`sender_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='聊天消息表';

-- 12. 操作日志表
DROP TABLE IF EXISTS `t_operation_log`;
CREATE TABLE `t_operation_log` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `user_id` BIGINT DEFAULT NULL,
    `username` VARCHAR(50) DEFAULT NULL,
    `operation` VARCHAR(200) DEFAULT NULL,
    `method` VARCHAR(200) DEFAULT NULL,
    `params` TEXT DEFAULT NULL,
    `result` TEXT DEFAULT NULL,
    `ip` VARCHAR(50) DEFAULT NULL,
    `location` VARCHAR(200) DEFAULT NULL,
    `time` INT DEFAULT NULL,
    `status` TINYINT DEFAULT 1,
    `error_msg` TEXT DEFAULT NULL,
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='操作日志表';

-- 13. 系统配置表
DROP TABLE IF EXISTS `t_system_config`;
CREATE TABLE `t_system_config` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `config_key` VARCHAR(100) NOT NULL,
    `config_value` TEXT DEFAULT NULL,
    `config_desc` VARCHAR(255) DEFAULT NULL,
    `config_type` VARCHAR(50) DEFAULT 'string',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_config_key` (`config_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统配置表';

-- 14. 通知消息表
DROP TABLE IF EXISTS `t_notification`;
CREATE TABLE `t_notification` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `user_id` BIGINT NOT NULL,
    `title` VARCHAR(200) NOT NULL,
    `content` TEXT NOT NULL,
    `type` VARCHAR(50) NOT NULL,
    `related_id` BIGINT DEFAULT NULL,
    `is_read` TINYINT(1) DEFAULT 0,
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `deleted` TINYINT DEFAULT 0,
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_is_read` (`is_read`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='通知消息表';

-- 创建用户认证表
DROP TABLE IF EXISTS `t_user_certification`;
CREATE TABLE `t_user_certification` (
                                        `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '认证ID',
                                        `user_id` BIGINT NOT NULL COMMENT '用户ID',
                                        `id_card` VARCHAR(20) DEFAULT NULL COMMENT '身份证号',
                                        `id_card_front_url` VARCHAR(500) DEFAULT NULL COMMENT '身份证正面URL',
                                        `id_card_back_url` VARCHAR(500) DEFAULT NULL COMMENT '身份证反面URL',
                                        `status` VARCHAR(50) NOT NULL DEFAULT 'not_submitted' COMMENT '认证状态 (not_submitted:未提交 pending:审核中 approved:已认证 rejected:已拒绝)',
                                        `reject_reason` TEXT DEFAULT NULL COMMENT '拒绝原因',
                                        `reviewer_id` BIGINT DEFAULT NULL COMMENT '审核人ID',
                                        `review_time` DATETIME DEFAULT NULL COMMENT '审核时间',
                                        `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                        `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                        `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除',
                                        PRIMARY KEY (`id`),
                                        KEY `idx_user_id` (`user_id`),
                                        KEY `idx_status` (`status`),
                                        KEY `idx_create_time` (`create_time`),
                                        CONSTRAINT `fk_user_id` FOREIGN KEY (`user_id`) REFERENCES `t_user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户认证表';

-- 添加索引用于快速查询用户最新的认证记录
CREATE INDEX idx_user_id_create_time ON `t_user_certification` (`user_id`, `create_time` DESC);


-- ============================================
-- 初始化管理员数据
-- ============================================

-- 插入管理员账户
-- 注意：密码使用BCrypt加密, 原始密码为 admin123
-- 加密后的密码：$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi

INSERT INTO `t_user` (`username`, `password`, `nickname`, `role`, `status`, `certified`, `register_type`) 
VALUES 
-- 超级管理员账户
-- 用户名: admin
-- 原始密码: admin123
-- 角色: super_admin (拥有所有权限)
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '超级管理员', 'super_admin', 1, 1, 'username'),

-- 审核员账户
-- 用户名: auditor
-- 原始密码: admin123
-- 角色: auditor (负责审核领养申请)
('auditor', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '审核员', 'application_auditor', 1, 1, 'username');

-- 普通管理员（无审核权限）
-- 用户名: manager
-- 原始密码: admin123
-- 角色: admin
INSERT INTO `t_user` (`username`, `password`, `nickname`, `role`, `status`, `certified`, `register_type`)
VALUES ('manager', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '管理员', 'admin', 1, 1, 'username');

-- ============================================
-- 宠物数据请执行 insert_pets_data.sql
-- ============================================



-- ============================================
-- 优化 t_pet_favorite 表索引
-- ============================================

-- 1. 添加 user_id 的索引（用于查询用户的收藏列表）
-- 检查索引是否存在，不存在则创建
ALTER TABLE `t_pet_favorite` ADD KEY `idx_user_id` (`user_id`);

-- 2. 优化唯一键顺序（user_id 在前，便于范围查询）
-- 注意：如果要修改唯一键顺序，需要先删除再重建
-- 但由于已有数据，建议保持现有的 uk_user_pet 不变
-- 可以添加一个新的复合索引来优化查询
ALTER TABLE `t_pet_favorite` ADD KEY `idx_user_pet` (`user_id`, `pet_id`);

-- ============================================
-- 优化 t_pet_like 表索引
-- ============================================

-- 1. 添加 user_id 的索引（用于查询用户的点赞列表）
ALTER TABLE `t_pet_like` ADD KEY `idx_user_id` (`user_id`);

-- 2. 添加复合索引优化查询
ALTER TABLE `t_pet_like` ADD KEY `idx_user_pet` (`user_id`, `pet_id`);

-- ============================================
-- 优化 t_article_favorite 表索引
-- ============================================

-- 1. 添加 user_id 的索引
ALTER TABLE `t_article_favorite` ADD KEY `idx_user_id` (`user_id`);

-- 2. 添加复合索引
ALTER TABLE `t_article_favorite` ADD KEY `idx_user_article` (`user_id`, `article_id`);

-- ============================================
-- 优化 t_article_like 表索引
-- ============================================

-- 1. 添加 user_id 的索引
ALTER TABLE `t_article_like` ADD KEY `idx_user_id` (`user_id`);

-- 2. 添加复合索引
ALTER TABLE `t_article_like` ADD KEY `idx_user_article` (`user_id`, `article_id`);

-- ============================================
-- 优化 t_pet 表索引
-- ============================================

-- 1. 添加 name 字段的索引（用于模糊查询）
ALTER TABLE `t_pet` ADD KEY `idx_name` (`name`);

-- 2. 添加复合索引优化分页查询
ALTER TABLE `t_pet` ADD KEY `idx_adoption_status_create_time` (`adoption_status`, `create_time`);

-- 3. 添加 shelf_status 的索引（用于上架状态查询）
ALTER TABLE `t_pet` ADD KEY `idx_shelf_status` (`shelf_status`);