-- =============================================================
-- 宠物领养管理系统 - 初始化数据库脚本
-- 说明：
-- 1. 汇总当前后端所需的全部表结构（含内容聚合改造）
-- 2. 统一使用 `animal_adopt` 数据库
-- 3. 如需演示数据，可在执行完本脚本后导入 insert_pets_data.sql 等补充脚本
-- =============================================================

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

CREATE DATABASE IF NOT EXISTS `animal_adopt`
  DEFAULT CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;
USE `animal_adopt`;

-- =============================================================
-- 用户与认证体系
-- =============================================================

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
  KEY `idx_role` (`role`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

DROP TABLE IF EXISTS `t_user_certification`;
CREATE TABLE `t_user_certification` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '认证ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `id_card` VARCHAR(20) DEFAULT NULL COMMENT '身份证号',
  `id_card_front_url` VARCHAR(500) DEFAULT NULL COMMENT '身份证正面URL',
  `id_card_back_url` VARCHAR(500) DEFAULT NULL COMMENT '身份证反面URL',
  `status` VARCHAR(50) NOT NULL DEFAULT 'not_submitted' COMMENT '认证状态',
  `reject_reason` TEXT DEFAULT NULL COMMENT '拒绝原因',
  `reviewer_id` BIGINT DEFAULT NULL COMMENT '审核人ID',
  `review_time` DATETIME DEFAULT NULL COMMENT '审核时间',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_status` (`status`),
  KEY `idx_create_time` (`create_time`),
  KEY `idx_user_id_create_time` (`user_id`, `create_time` DESC),
  CONSTRAINT `fk_user_cert_user` FOREIGN KEY (`user_id`) REFERENCES `t_user`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户认证表';

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

INSERT INTO `t_user` VALUES
(1, 'admin', '$2a$10$smWyW1Z7pZq.nzzYNaCLdOrzUE6p3Pz3vZAKnkv4pshEX8jebulQi', '超级管理员', NULL, NULL, NULL, 'https://192.168.99.228:9000/animal-adopt/googleAvatar.jpg', 0, NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, 'username', 'super_admin', 1, 1, NULL, '2025-11-19 11:16:41', '2025-11-19 22:17:53', 0),
(2, 'auditor', '$2a$10$smWyW1Z7pZq.nzzYNaCLdOrzUE6p3Pz3vZAKnkv4pshEX8jebulQi', '审核员', NULL, NULL, NULL, 'https://192.168.99.228:9000/animal-adopt/微信图片_20250929211436_125_10.jpg', 0, NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, 'username', 'application_auditor', 1, 1, NULL, '2025-11-19 11:16:41', '2025-11-19 22:19:40', 0),
(3, 'manager', '$2a$10$smWyW1Z7pZq.nzzYNaCLdOrzUE6p3Pz3vZAKnkv4pshEX8jebulQi', '管理员', NULL, NULL, NULL, 'https://192.168.99.228:9000/animal-adopt/微信图片_20250929211439_126_10.jpg', 0, NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, 'username', 'admin', 1, 1, NULL, '2025-11-19 11:16:41', '2025-11-19 22:19:53', 0);

-- =============================================================
-- 宠物 & 领养业务
-- =============================================================

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
  `view_count` INT DEFAULT 0,
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
  KEY `idx_adoption_status` (`adoption_status`),
  KEY `idx_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='宠物表';

DROP TABLE IF EXISTS `t_pet_like`;
CREATE TABLE `t_pet_like` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL,
  `pet_id` BIGINT NOT NULL,
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `deleted` TINYINT DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_pet` (`user_id`, `pet_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_pet_id` (`pet_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='宠物点赞表';

DROP TABLE IF EXISTS `t_pet_favorite`;
CREATE TABLE `t_pet_favorite` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL,
  `pet_id` BIGINT NOT NULL,
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `deleted` TINYINT DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_pet` (`user_id`, `pet_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_pet_id` (`pet_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='宠物收藏表';

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

-- =============================================================
-- 内容聚合（指南 & 故事）及互动
-- =============================================================

DROP TABLE IF EXISTS `t_guide`;
CREATE TABLE `t_guide` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(255) NOT NULL COMMENT '标题',
  `excerpt` VARCHAR(500) DEFAULT NULL COMMENT '摘要',
  `content` LONGTEXT NOT NULL COMMENT 'Markdown内容',
  `image` VARCHAR(500) DEFAULT NULL COMMENT '封面图',
  `category` VARCHAR(100) NOT NULL COMMENT '分类',
  `views` INT DEFAULT 0 COMMENT '浏览次数',
  `like_count` INT DEFAULT 0 COMMENT '点赞数',
  `favorite_count` INT DEFAULT 0 COMMENT '收藏数',
  `created_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_category` (`category`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='领养指南表';

DROP TABLE IF EXISTS `t_story`;
CREATE TABLE `t_story` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(255) NOT NULL COMMENT '标题',
  `excerpt` VARCHAR(500) DEFAULT NULL COMMENT '摘要',
  `content` LONGTEXT NOT NULL COMMENT 'Markdown内容',
  `image` VARCHAR(500) DEFAULT NULL COMMENT '封面图片URL',
  `author` VARCHAR(100) NOT NULL COMMENT '作者',
  `tags` VARCHAR(255) DEFAULT NULL COMMENT '标签，逗号分隔',
  `views` INT DEFAULT 0 COMMENT '浏览次数',
  `likes` INT DEFAULT 0 COMMENT '点赞数',
  `favorite_count` INT DEFAULT 0 COMMENT '收藏数',
  `created_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_author` (`author`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='领养故事表';

DROP TABLE IF EXISTS `t_guide_like`;
CREATE TABLE `t_guide_like` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `guide_id` BIGINT NOT NULL COMMENT '指南ID',
  `created_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_guide` (`user_id`, `guide_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_guide_id` (`guide_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='指南点赞记录表';

DROP TABLE IF EXISTS `t_guide_favorite`;
CREATE TABLE `t_guide_favorite` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `guide_id` BIGINT NOT NULL COMMENT '指南ID',
  `created_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_guide` (`user_id`, `guide_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_guide_id` (`guide_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='指南收藏记录表';

DROP TABLE IF EXISTS `t_story_like`;
CREATE TABLE `t_story_like` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `story_id` BIGINT NOT NULL COMMENT '故事ID',
  `created_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_story` (`user_id`, `story_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_story_id` (`story_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='故事点赞记录表';

DROP TABLE IF EXISTS `t_story_favorite`;
CREATE TABLE `t_story_favorite` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `story_id` BIGINT NOT NULL COMMENT '故事ID',
  `created_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_story` (`user_id`, `story_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_story_id` (`story_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='故事收藏记录表';

-- =============================================================
-- AI 客服 / 会话 & 通知
-- =============================================================

DROP TABLE IF EXISTS `t_conversation_session`;
CREATE TABLE `t_conversation_session` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `session_id` VARCHAR(100) NOT NULL COMMENT '会话ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `title` VARCHAR(255) DEFAULT NULL COMMENT '会话标题',
  `description` TEXT DEFAULT NULL COMMENT '会话描述',
  `message_count` INT DEFAULT 0 COMMENT '消息数量',
  `status` VARCHAR(20) DEFAULT 'active' COMMENT '状态',
  `last_message` TEXT DEFAULT NULL COMMENT '最后一条消息',
  `last_message_time` DATETIME DEFAULT NULL COMMENT '最后消息时间',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `closed_time` DATETIME DEFAULT NULL COMMENT '关闭时间',
  `deleted` TINYINT DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_session_id` (`session_id`),
  KEY `idx_user_id_status` (`user_id`, `status`, `update_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='AI客服会话表';

DROP TABLE IF EXISTS `t_conversation_history`;
CREATE TABLE `t_conversation_history` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `session_id` VARCHAR(100) NOT NULL COMMENT '会话ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `role` VARCHAR(20) NOT NULL COMMENT '角色 user/assistant',
  `content` LONGTEXT NOT NULL COMMENT '消息内容',
  `tool_name` VARCHAR(100) DEFAULT NULL,
  `tool_params` JSON DEFAULT NULL,
  `tool_result` LONGTEXT DEFAULT NULL,
  `timestamp` DATETIME DEFAULT NULL COMMENT '时间戳',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` TINYINT DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `idx_session_id_create_time` (`session_id`, `create_time`),
  KEY `idx_user_id_role` (`user_id`, `role`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='AI客服对话历史表';

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

-- 为 t_user_certification 增加联合索引，提升状态筛选 + 时间排序性能
ALTER TABLE `t_user_certification`
    ADD INDEX `idx_status_create_time`(`status`, `create_time` DESC);

-- 为身份证号增加索引，方便管理员按身份证关键词检索
ALTER TABLE `t_user_certification`
    ADD INDEX `idx_id_card`(`id_card`);

ALTER TABLE t_pet ADD COLUMN adopted_by BIGINT COMMENT '领养者ID' AFTER adoption_status;
CREATE INDEX idx_adopted_by ON t_pet(adopted_by);

-- =============================================================
-- 初始化数据（来自现网导出）
-- =============================================================

-- 宠物
INSERT INTO `t_pet` VALUES
(1, '小刺', 'hedgehog', '刺猬', 1, 6, 0.50, '棕色', 0, 1, 'healthy', NULL, '性格温顺, 喜欢安静的环境, 晚上比较活跃', '这是一只可爱的小刺猬, 身体健康, 适应能力强。喜欢吃水果和昆虫, 需要提供温暖干燥的环境。', '["https://192.168.99.228:9000/animal-adopt/刺猬1.png"]', 'https://192.168.99.228:9000/animal-adopt/刺猬1.png', NULL, '城市公园', '需要有养刺猬经验, 能提供适宜的温度环境（20-25℃）, 定期清理笼舍', 'available', 1, 0, 0, 0, 0, 1, NULL, '2025-11-19 13:44:14', '2025-11-24 15:18:38', 0),
(2, '小白', 'rabbit', '白兔', 2, 8, 2.50, '纯白色', 1, 1, 'healthy', NULL, '性格活泼可爱, 喜欢吃胡萝卜和青菜, 很亲人', '这是一只纯白色的可爱兔子, 毛发柔软, 眼睛明亮。已绝育并接种疫苗, 身体健康。喜欢在草地上奔跑, 适合有院子的家庭。', '["https://192.168.99.228:9000/animal-adopt/兔子1.jpg"]', 'https://192.168.99.228:9000/animal-adopt/兔子1.jpg', NULL, '宠物救助站', '需要有足够的活动空间, 定期提供新鲜蔬菜和干草, 每天至少1小时的户外活动时间', 'available', 1, 0, 0, 0, 0, 2, NULL, '2025-11-19 13:44:14', '2025-11-19 13:44:14', 0),
(3, '金金', 'dog', '金毛寻回犬', 1, 18, 28.50, '金黄色', 1, 1, 'healthy', NULL, '性格温顺友善, 聪明听话, 喜欢和人互动, 对小孩很有耐心', '这是一只帅气的金毛犬, 毛色金黄, 性格温顺。已完成绝育和疫苗接种, 训练有素, 会基本指令。非常适合家庭饲养, 是孩子们的好伙伴。', '["https://192.168.99.228:9000/animal-adopt/小狗1.png"]', 'https://192.168.99.228:9000/animal-adopt/小狗1.png', NULL, '市区流浪犬救助中心', '需要有足够的活动空间, 每天遛狗2次以上, 定期梳理毛发, 有养大型犬经验者优先', 'available', 1, 0, 0, 0, 0, 3, NULL, '2025-11-19 13:44:14', '2025-11-19 13:44:14', 0),
(4, '小柴', 'dog', '柴犬', 2, 12, 9.80, '赤色', 1, 1, 'healthy', NULL, '性格独立但忠诚, 活泼好动, 喜欢户外活动, 警惕性高', '这是一只可爱的柴犬, 标准的赤色毛发, 笑容治愈。已绝育并完成疫苗接种, 身体健康。性格活泼, 需要足够的运动量。', '["https://192.168.99.228:9000/animal-adopt/小狗2.jpg"]', 'https://192.168.99.228:9000/animal-adopt/小狗2.jpg', NULL, '郊区宠物救助站', '需要每天至少1小时户外运动, 定期梳理毛发, 有养中型犬经验', 'available', 1, 1, 0, 0, 0, 4, NULL, '2025-11-19 13:44:14', '2025-11-23 23:25:00', 0),
(5, '泰迪', 'dog', '泰迪犬', 1, 10, 4.20, '棕色', 1, 1, 'healthy', NULL, '性格活泼粘人, 聪明伶俐, 喜欢和主人互动, 适合陪伴', '这是一只可爱的泰迪犬, 毛发卷曲蓬松, 非常可爱。已绝育并接种疫苗, 身体健康。性格粘人, 适合需要陪伴的家庭。', '["https://192.168.99.228:9000/animal-adopt/小狗3.jpg"]', 'https://192.168.99.228:9000/animal-adopt/小狗3.jpg', NULL, '市区宠物医院', '需要定期美容修剪毛发, 每天陪伴玩耍, 适合有时间照顾的家庭', 'available', 1, 0, 0, 0, 0, 5, NULL, '2025-11-19 13:44:14', '2025-11-19 13:44:14', 0),
(6, '二哈', 'dog', '哈士奇', 1, 15, 22.00, '黑白色', 1, 1, 'healthy', NULL, '性格活泼好动, 精力充沛, 喜欢奔跑, 有时会有点调皮', '这是一只帅气的哈士奇, 标志性的蓝眼睛和黑白毛色。已绝育并完成疫苗接种, 精力旺盛, 需要大量运动。性格友善但有点调皮, 适合有经验的主人。', '["https://192.168.99.228:9000/animal-adopt/小狗4.png"]', 'https://192.168.99.228:9000/animal-adopt/小狗4.png', NULL, '郊区救助站', '需要大量运动空间, 每天至少2小时户外活动, 有养大型犬经验, 能接受它的调皮性格', 'available', 1, 4, 0, 0, 0, 6, NULL, '2025-11-19 13:44:14', '2025-11-24 10:20:00', 0),
(7, '小黑', 'dog', '边境牧羊犬', 1, 20, 18.50, '黑白色', 1, 1, 'healthy', NULL, '智商极高, 学习能力强, 精力充沛, 需要大量脑力和体力活动', '这是一只聪明的边牧, 黑白相间的毛色, 眼神专注。已绝育并完成疫苗接种, 智商很高, 学东西很快。需要主人有足够时间陪伴和训练。', '["https://192.168.99.228:9000/animal-adopt/小狗5.jpg"]', 'https://192.168.99.228:9000/animal-adopt/小狗5.jpg', NULL, '训练基地', '需要大量运动和智力游戏, 每天至少2小时户外活动, 有养犬经验, 能提供足够的训练和刺激', 'available', 1, 0, 0, 0, 0, 7, NULL, '2025-11-19 13:44:14', '2025-11-19 13:44:14', 0),
(8, '拉拉', 'dog', '拉布拉多寻回犬', 2, 24, 26.00, '黄色', 1, 1, 'healthy', NULL, '性格温顺友善, 对人友好, 喜欢游泳, 是优秀的家庭伴侣犬', '这是一只温柔的拉布拉多, 毛色金黄, 性格超好。已绝育并完成疫苗接种, 非常适合家庭饲养。喜欢和人互动, 对小孩特别有耐心。', '["https://192.168.99.228:9000/animal-adopt/小狗6.jpg"]', 'https://192.168.99.228:9000/animal-adopt/小狗6.jpg', NULL, '市区救助中心', '需要足够的活动空间, 每天遛狗2次, 定期梳理毛发, 适合有院子的家庭', 'available', 1, 0, 0, 0, 0, 8, NULL, '2025-11-19 13:44:14', '2025-11-19 13:44:14', 0),
(9, '圆圆', 'cat', '英国短毛猫', 2, 14, 4.50, '蓝灰色', 1, 1, 'healthy', NULL, '性格温顺安静, 喜欢睡觉, 不太爱动, 适合公寓饲养', '这是一只可爱的英短猫, 圆圆的脸蛋, 蓝灰色的毛发。已绝育并完成疫苗接种, 性格温顺, 不吵不闹, 非常适合公寓饲养。', '["https://192.168.99.228:9000/animal-adopt/小猫1.jpg"]', 'https://192.168.99.228:9000/animal-adopt/小猫1.jpg', NULL, '宠物店', '需要定期梳理毛发, 提供猫砂盆和猫爬架, 定期体检', 'available', 1, 2, 0, 0, 0, 9, NULL, '2025-11-19 13:44:14', '2025-11-22 16:45:00', 0),
(10, '虎斑', 'cat', '美国短毛猫', 1, 16, 5.20, '银色虎斑', 1, 1, 'healthy', NULL, '性格活泼好动, 喜欢玩耍, 好奇心强, 适应能力强', '这是一只帅气的美短猫, 银色虎斑纹路清晰。已绝育并完成疫苗接种, 性格活泼, 喜欢和人互动。身体健壮, 适应能力强。', '["https://192.168.99.228:9000/animal-adopt/小猫2.jpg"]', 'https://192.168.99.228:9000/animal-adopt/小猫2.jpg', NULL, '流浪猫救助站', '需要提供玩具和猫爬架, 定期陪伴玩耍, 适合有时间的家庭', 'available', 1, 2, 1, 1, 0, 10, NULL, '2025-11-19 13:44:14', '2025-11-24 10:50:00', 0),
(11, '橘子', 'cat', '中华田园猫', 1, 12, 6.00, '橘色', 1, 1, 'healthy', NULL, '性格亲人温顺, 食量较大, 喜欢晒太阳, 很会撒娇', '这是一只可爱的橘猫, 毛色橙黄, 圆滚滚的很可爱。已绝育并完成疫苗接种, 性格亲人, 喜欢和主人互动。食量比较大, 需要控制饮食。', '["https://192.168.99.228:9000/animal-adopt/小猫3.png"]', 'https://192.168.99.228:9000/animal-adopt/小猫3.png', NULL, '小区救助', '需要控制饮食避免过胖, 提供猫砂盆和猫爬架, 定期体检', 'available', 1, 4, 0, 0, 0, 11, NULL, '2025-11-19 13:44:14', '2025-11-21 23:35:00', 0),
(12, '小暹', 'cat', '暹罗猫', 2, 18, 3.80, '重点色', 1, 1, 'healthy', NULL, '性格活泼粘人, 喜欢和主人交流, 叫声独特, 需要陪伴', '这是一只优雅的暹罗猫, 蓝色眼睛, 重点色毛发。已绝育并完成疫苗接种, 性格粘人, 喜欢和主人说话。需要主人有足够时间陪伴。', '["https://192.168.99.228:9000/animal-adopt/小猫4.jpg"]', 'https://192.168.99.228:9000/animal-adopt/小猫4.jpg', NULL, '宠物医院', '需要大量陪伴和互动, 不适合长时间独处, 适合在家办公或时间充裕的家庭', 'available', 1, 3, 0, 0, 0, 12, NULL, '2025-11-19 13:44:14', '2025-11-24 10:50:00', 0),
(13, '布布', 'cat', '布偶猫', 2, 20, 5.50, '海豹双色', 1, 1, 'healthy', NULL, '性格温顺甜美, 喜欢被抱, 叫声轻柔, 是完美的家庭伴侣', '这是一只漂亮的布偶猫, 蓝色眼睛, 海豹双色毛发。已绝育并完成疫苗接种, 性格超级温顺, 喜欢被抱。毛发需要定期打理。', '["https://192.168.99.228:9000/animal-adopt/小猫5.jpg"]', 'https://192.168.99.228:9000/animal-adopt/小猫5.jpg', NULL, '繁育基地', '需要每天梳理毛发, 提供高质量猫粮, 定期体检, 适合有养猫经验的家庭', 'available', 1, 14, 0, 0, 0, 13, NULL, '2025-11-19 13:44:14', '2025-11-24 10:50:00', 0),
(14, '小狸', 'cat', '中华狸花猫', 1, 10, 4.80, '棕色虎斑', 1, 1, 'healthy', NULL, '性格独立机警, 身体强壮, 适应能力强, 是优秀的捕鼠能手', '这是一只帅气的狸花猫, 棕色虎斑纹路清晰。已绝育并完成疫苗接种, 性格独立但也亲人。身体强壮, 适应能力很强。', '["https://192.168.99.228:9000/animal-adopt/小猫6.jpg"]', 'https://192.168.99.228:9000/animal-adopt/小猫6.jpg', NULL, '街道救助', '需要提供猫砂盆和猫爬架, 定期体检, 适合各类家庭', 'available', 1, 7, 0, 0, 0, 14, NULL, '2025-11-19 13:44:14', '2025-11-24 10:45:00', 0),
(20, '耶耶', 'dog', '萨摩耶', 2, 5, 15.00, '白色', 0, 0, 'healthy', NULL, '温顺', '一只可爱的萨摩耶', '["http://localhost:9000/animal-adopt/pet-images/8962033cf5c44e629554661ac480f271.png"]', 'http://localhost:9000/animal-adopt/pet-covers/5f372549aeed448590d9fc4e41e20c6b.png', NULL, NULL, NULL, 'available', 1, 7, 1, 1, 0, 0, 1, '2025-11-23 00:30:33', '2025-11-24 14:00:00', 0),
(21, '小博', 'dog', '博美犬', 1, 3, 10.00, '白色', 0, 0, 'healthy', NULL, '温顺', '一只可爱温顺的博美犬', '["http://localhost:9000/animal-adopt/pet-images/08352439a0244cd6aa188ec7b290f653.png"]', 'http://localhost:9000/animal-adopt/pet-covers/0b0f079552b04de3952ea0e27c895a84.png', NULL, NULL, NULL, 'available', 1, 4, 0, 0, 0, 0, 1, '2025-11-24 14:13:34', '2025-11-24 15:03:51', 0),
(22, '布布', 'cat', '布偶猫', 2, 3, 6.00, '白色', 0, 0, 'healthy', NULL, '调皮', '一只和小高一样调皮的可爱布偶猫', '["http://localhost:9000/animal-adopt/pet-images/c9a740e1c6b743b0b8930343a26678b9.png"]', 'http://localhost:9000/animal-adopt/pet-covers/cd04017645314c5ca9a64e5d95e2600f.png', NULL, NULL, NULL, 'available', 1, 0, 0, 0, 0, 0, 1, '2025-11-24 15:05:08', '2025-11-24 15:05:08', 0),
(23, '边边', 'dog', '边牧犬', 1, 4, 14.00, '黑白相见', 0, 0, 'healthy', NULL, '活泼', '和小高一样皮', '["http://localhost:9000/animal-adopt/pet-images/39d3242eb0a84cdf8c55199a95a24840.png"]', 'http://localhost:9000/animal-adopt/pet-covers/c68f5065d8a84819a75a0019c9d04927.png', NULL, NULL, NULL, 'available', 1, 1, 0, 0, 0, 0, 1, '2025-11-24 15:06:49', '2025-11-24 15:10:00', 0),
(24, '金金', 'dog', '金毛', 1, 4, 15.00, '棕色', 0, 0, 'healthy', NULL, '活泼', '一只金毛', '["http://localhost:9000/animal-adopt/pet-images/8f9b25c437a54df2927d477b46535406.png"]', 'http://localhost:9000/animal-adopt/pet-covers/412005b4bf8c43b89c398a3e4b8160e9.png', NULL, NULL, NULL, 'available', 1, 1, 0, 0, 0, 0, 1, '2025-11-24 15:11:43', '2025-11-24 15:15:00', 0),
(25, '松松', '松鼠', '松鼠', 1, 3, 4.00, '黑棕色', 0, 0, 'healthy', NULL, '谨慎', '胆小的松鼠', '["http://localhost:9000/animal-adopt/pet-images/a3f842d804d14893a24f776594f76dc0.png"]', 'http://localhost:9000/animal-adopt/pet-covers/a097fdcf5c2e43bf895465c537c2125f.png', NULL, NULL, NULL, 'available', 1, 1, 0, 0, 0, 0, 1, '2025-11-24 15:27:05', '2025-11-24 22:15:00', 0),
(26, '小松', '松鼠', '美洲红松鼠', 2, 3, 6.00, '红棕色', 0, 0, 'healthy', NULL, '活泼', '活泼的松鼠', '["http://localhost:9000/animal-adopt/pet-images/32cce10b71bd4c35b7a40053bbc6c894.png"]', 'http://localhost:9000/animal-adopt/pet-covers/9eebd6cf4f3d47f1acaf6c8fedae6587.png', NULL, NULL, NULL, 'available', 1, 0, 0, 0, 0, 0, 1, '2025-11-24 15:45:43', '2025-11-24 15:45:44', 0),
(27, '大松', '松鼠', '美洲红松鼠', 1, 2, 2.00, '红棕色', 0, 0, 'healthy', NULL, '活泼', '松鼠', '["http://localhost:9000/animal-adopt/pet-images/d755515cc94b4185ac57897f5a056431.png"]', 'http://localhost:9000/animal-adopt/pet-covers/53695e368c5e4024a39b8a2f220d89bb.png', NULL, NULL, NULL, 'available', 1, 0, 0, 0, 0, 0, 1, '2025-11-24 15:49:12', '2025-11-24 15:55:56', 1),
(28, '大松', '松鼠', '美洲红松鼠', 1, 2, 2.00, '红棕色', 0, 0, 'healthy', NULL, '胆小', '胆小的松鼠', '["http://localhost:9000/animal-adopt/pet-images/97c11e865e914da691a4700f6751db27.png"]', 'http://localhost:9000/animal-adopt/pet-covers/c06a6676216847af8c1794ba188c63f8.png', NULL, NULL, NULL, 'available', 1, 0, 0, 0, 0, 0, 1, '2025-11-24 15:56:49', '2025-11-24 19:15:02', 1),
(29, '大松', '松鼠', '美洲红松鼠', 1, 3, 4.00, '红棕色', 0, 0, 'healthy', NULL, '谨慎', '谨慎的松鼠', '["http://localhost:9000/animal-adopt/pet-images/ab25cef1966448e6ab9d15c5f3c2f703.png"]', 'http://localhost:9000/animal-adopt/pet-covers/50e686e36754405497e7ca53f4e6954e.png', NULL, NULL, NULL, 'available', 1, 0, 0, 0, 0, 0, 1, '2025-11-24 19:15:56', '2025-11-24 19:15:56', 0),
(33, '仓仓', '仓鼠', '仓鼠', 1, 2, 2.00, '白色', 0, 0, 'healthy', NULL, '胆小、谨慎', '小心的仓鼠', '["http://localhost:9000/animal-adopt/pet-images/5dbea0668a4342de8ddf71da8b9bc530.png"]', 'http://localhost:9000/animal-adopt/pet-covers/5af36d9a6bcf4fbe983d78da208e0b19.png', NULL, NULL, NULL, 'available', 1, 2, 0, 0, 0, 0, 1, '2025-11-24 22:38:51', '2025-11-24 22:50:00', 0);

-- 宠物互动
INSERT INTO `t_pet_like` VALUES
(1, 1, 14, '2025-11-19 11:27:33', 0),
(2, 1, 11, '2025-11-19 14:31:54', 1),
(8, 1, 13, '2025-11-20 23:21:27', 0),
(9, 1, 10, '2025-11-21 20:20:32', 0),
(17, 1, 20, '2025-11-23 16:44:25', 0);

INSERT INTO `t_pet_favorite` VALUES
(1, 1, 14, '2025-11-19 11:27:35', 0),
(2, 1, 11, '2025-11-19 14:31:55', 1),
(8, 1, 13, '2025-11-20 23:21:28', 0),
(9, 1, 10, '2025-11-21 20:20:32', 0),
(19, 1, 20, '2025-11-23 16:44:24', 0);

-- 指南内容（含基础统计）
INSERT INTO `t_guide` (`id`,`title`,`excerpt`,`content`,`image`,`category`,`views`,`like_count`,`favorite_count`,`created_at`,`updated_at`) VALUES
(1, '领养前的准备工作', '详细介绍领养宠物前需要做的各项准备工作，包括心理准备、物质准备和环境准备。', '# 领养前的准备工作\r\r\n\r\r领养宠物是一项长期的责任，在决定领养之前，需要做好充分的准备工作。\r\r\n\r\r## 心理准备\r\r\n\r\r领养宠物意味着承担起一个生命的责任，需要有长期照顾它的心理准备。宠物会陪伴您很多年，期间可能会遇到各种问题，如生病、行为问题等，都需要您耐心处理。\r\r\n\r\r## 物质准备\r\r\n\r\r在宠物到家之前，需要准备好基本的生活用品：\r\r\n\r\r- 食物和饮水器\r\r\n- 舒适的窝或笼子\r\r\n- 玩具和磨爪板（猫咪）\r\r\n- 牵引绳和项圈（狗狗）\r\r\n- 猫砂盆和猫砂（猫咪）\r\r\n- 基础药品和急救包\r\r\n\r\r## 环境准备\r\r\n\r\r确保家中环境对宠物安全友好：\r\r\n\r\r- 收好易碎物品和危险品\r\r\n- 封堵可能的逃跑通道\r\r\n- 准备一个安静的适应空间\r\r\n- 了解附近的宠物医院\r\r\n\r\r做好这些准备工作，可以帮助宠物更快适应新家，也能让您更好地享受与宠物相伴的快乐时光。', 'https://192.168.99.228:9000/animal-adopt/1.png', '领养准备', 1278, 1, 1, '2025-11-20 22:53:07', '2025-11-22 16:51:34'),
(2, '新宠物到家的第一周', '帮助新领养的宠物适应新环境的实用指南，包括饮食、作息和互动建议。', '# 新宠物到家的第一周\r\r\n\r\r新宠物到家的第一周是适应期，需要特别的关注和照顾。\r\r\n\r\r## 第一天：安静适应\r\r\n\r\r第一天不要过度互动，给宠物一个安静的空间适应环境。准备好食物和水，但不要强迫进食。\r\r\n\r\r## 第2-3天：建立信任\r\r\n\r\r开始轻声说话和温和互动，用手喂食建立信任关系。观察宠物的行为和健康状况。\r\r\n\r\r## 第4-7天：逐步熟悉\r\r\n\r\r逐步增加互动时间，引导宠物熟悉家中各个区域。开始建立基本的作息规律。\r\r\n\r\r记住，每只宠物的适应速度不同，要有耐心，不要急于求成。', 'https://192.168.99.228:9000/animal-adopt/2.png', '日常护理', 997, 1, 1, '2025-11-20 22:53:07', '2025-11-22 16:51:39'),
(3, '基础服从训练技巧', '教授基本的服从指令训练方法，帮助建立良好的人宠关系。', '# 基础服从训练技巧\r\r\n\r\r训练宠物不仅能让它们更好地融入家庭生活，还能增进与主人的感情。\r\r\n\r\r## 基本原则\r\r\n\r\r训练要遵循正向强化原则，多鼓励少惩罚。每次训练时间不宜过长，保持在10-15分钟为宜。\r\r\n\r\r## 常用指令\r\r\n\r\r- **坐下：** 手持零食，引导宠物坐下，给予奖励\r\r\n- **趴下：** 在坐下的基础上，将零食往地面移动\r\r\n- **过来：** 用欢快的语气叫名字，宠物靠近时给予奖励\r\r\n- **握手：** 轻轻抬起宠物的爪子，说"握手"并奖励\r\r\n\r\r## 注意事项\r\r\n\r\r训练要有耐心和一致性，全家人使用相同的指令和手势。训练后要及时奖励，让宠物建立正确的条件反射。', 'https://192.168.99.228:9000/animal-adopt/3.png', '训练教育', 757, 1, 0, '2025-11-20 22:53:07', '2025-11-22 16:51:45'),
(4, '常见疾病预防与处理', '介绍宠物常见疾病的症状识别、预防措施和应急处理方法。', '# 常见疾病预防与处理\r\r\n\r\r了解宠物常见疾病，及早发现和预防，是每个宠物主人的必备知识。\r\r\n\r\r## 预防接种\r\r\n\r\r按时接种疫苗是预防疾病的最佳方式。幼年宠物需要接种多联疫苗和狂犬病疫苗，成年后每年加强免疫。\r\r\n\r\r## 常见症状\r\r\n\r\r- 食欲不振、精神萎靡\r\r\n- 呕吐、腹泻\r\r\n- 咳嗽、流鼻涕\r\r\n- 皮肤瘙痒、脱毛\r\r\n- 频繁舔爪或某个部位\r\r\n\r\r## 日常保健\r\r\n\r\r定期驱虫，保持环境清洁，提供均衡营养，适量运动。发现异常及时就医，不要自行用药。', 'https://192.168.99.228:9000/animal-adopt/4.png', '健康管理', 1104, 1, 1, '2025-11-20 22:53:07', '2025-11-22 16:51:51'),
(5, '分离焦虑症的应对', '分析宠物分离焦虑的原因和表现，并提供有效的缓解方法。', '# 分离焦虑症的应对\r\r\n\r\r分离焦虑是许多宠物常见的行为问题，表现为主人离开后出现焦虑和破坏行为。\r\r\n\r\r## 症状表现\r\r\n\r\r- 主人离开时过度吠叫或哀鸣\r\r\n- 破坏家具或物品\r\r\n- 在不该的地方大小便\r\r\n- 过度舔毛或咬爪子\r\r\n- 主人回来时过度兴奋\r\r\n\r\r## 缓解方法\r\r\n\r\r渐进式脱敏训练：开始时短时间离开，逐步延长时间。离开和回来时保持平静，不要过度安抚。\r\r\n\r\r## 其他建议\r\r\n\r\r提供足够的玩具和活动空间，确保宠物在独处时有事可做。考虑使用安抚玩具或背景音乐。严重情况建议咨询专业训练师或兽医。', 'https://192.168.99.228:9000/animal-adopt/5.png', '行为问题', 825, 1, 1, '2025-11-20 22:53:07', '2025-11-22 16:51:56'),
(6, '营养饮食搭配指南', '根据不同年龄和健康状况的宠物，提供科学的饮食搭配建议。', '# 营养饮食搭配指南\r\r\n\r\r合理的饮食搭配是宠物健康的基础，不同年龄段的宠物有不同的营养需求。\r\r\n\r\r## 幼年期（0-1岁）\r\r\n\r\r需要高蛋白、高能量的食物支持快速生长。每天3-4餐，选择专门的幼年宠物粮。\r\r\n\r\r## 成年期（1-7岁）\r\r\n\r\r每天1-2餐，保持营养均衡。蛋白质占30-40%，脂肪10-20%，碳水化合物30-50%。\r\r\n\r\r## 老年期（7岁以上）\r\r\n\r\r降低热量摄入，增加易消化的优质蛋白。考虑添加关节保健成分和抗氧化剂。\r\r\n\r\r## 禁忌食物\r\r\n\r\r- 巧克力、咖啡因\r\r\n- 葡萄、洋葱、大蒜\r\r\n- 生肉、生鸡蛋\r\r\n- 高盐、高糖食物\r\r\n\r\r记得提供充足的清洁饮水，根据宠物的活动量调整食物份量。', 'https://192.168.99.228:9000/animal-adopt/6.png', '日常护理', 659, 0, 1, '2025-11-20 22:53:07', '2025-11-22 16:52:02');

INSERT INTO `t_guide_like` VALUES
(5, 1, 3, '2025-11-21 23:08:48'),
(6, 1, 2, '2025-11-22 17:06:09'),
(9, 1, 1, '2025-11-23 17:13:43'),
(10, 1, 4, '2025-11-24 22:11:10'),
(11, 1, 5, '2025-11-24 22:11:17');

INSERT INTO `t_guide_favorite` VALUES
(1, 1, 2, '2025-11-20 23:21:04'),
(2, 1, 6, '2025-11-23 16:46:17'),
(4, 1, 1, '2025-11-23 17:14:21'),
(5, 1, 4, '2025-11-24 22:11:13'),
(6, 1, 5, '2025-11-24 22:11:17');

-- 故事内容及互动
INSERT INTO `t_story` (`id`,`title`,`excerpt`,`content`,`image`,`author`,`tags`,`views`,`likes`,`favorite_count`,`created_at`,`updated_at`) VALUES
(1, '从小流浪到幸福家庭', '小黄在街头流浪了两年，直到遇到了爱心志愿者...', '# 从小流浪到幸福家庭\r\r\n\r\r小黄是一只金毛犬，在街头流浪了整整两年。它的毛发变得脏乱，身体也瘦弱不堪，但它依然保持着对人类的友善。\r\r\n\r\r## 遇见希望\r\r\n\r\r2023年的冬天，志愿者小王在巡查时发现了蜷缩在街角的小黄。看到它瑟瑟发抖的样子，小王毫不犹豫地将它带回了救助站。\r\r\n\r\r在救助站，兽医为小黄做了全面检查，发现它除了营养不良外，身体健康状况良好。经过一个月的精心照料，小黄恢复了往日的活力。\r\r\n\r\r## 寻找新家\r\r\n\r\r救助站工作人员开始为小黄寻找合适的领养家庭。他们收到了许多领养申请，经过仔细筛选和家访，最终选择了一家有爱心且有养犬经验的家庭。\r\r\n\r\r新主人是一对年轻夫妇，他们有一个宽敞的院子，非常适合小黄活动。第一次见面时，小黄就表现出了极大的热情，摇着尾巴围着他们转圈。\r\r\n\r\r## 幸福新生活\r\r\n\r\r如今的小黄已经完全融入了新家庭，它有了自己的小窝、玩具和定期的散步时间。主人说，小黄给他们带来了无尽的欢乐，是家庭不可缺少的一员。\r\r\n\r\r这个故事告诉我们，每一只流浪动物都值得被关爱，它们只是缺少一个机会去展现自己的美好。', 'https://192.168.99.228:9000/animal-adopt/7.png', '志愿者小王', '成功领养,狗狗', 127, 0, 0, '2025-11-20 22:53:07', '2025-11-22 22:59:28'),
(2, '失散三年终重聚', '猫咪咪咪走失三年后，通过救助站的帮助终于与主人团聚...', '# 失散三年终重聚\r\r\n\r\r咪咪是一只英短蓝猫，三年前在一次搬家过程中意外走失。主人李先生和家人四处寻找，但始终没有找到它的踪迹。\r\r\n\r\r## 意外发现\r\r\n\r\r三年后的某天，救助站接收了一只流浪猫，经过检查发现它脖子上有一个模糊的 tattoo，经过仔细辨认，工作人员确认这就是走失的咪咪。\r\r\n\r\r通过 tattoo 上的信息，救助站联系到了李先生。当李先生听到这个消息时，简直不敢相信自己的耳朵。三年了，他从未放弃过寻找咪咪的希望。\r\r\n\r\r## 重逢时刻\r\r\n\r\r在救助站的重逢现场，李先生激动得热泪盈眶。而咪咪在看到主人的那一刻，也发出了熟悉的叫声，仿佛在说："我终于找到你了！"\r\r\n\r\r经过检查，咪咪虽然瘦了一些，但身体健康状况良好。三年的流浪生活并没有磨灭它对主人的记忆和感情。\r\r\n\r\r## 珍惜当下\r\r\n\r\r回到家中，咪咪很快就适应了环境，它重新占据了自己最喜欢的沙发位置，仿佛从未离开过。\r\r\n\r\r李先生表示，这次重逢让他们更加珍惜彼此。他们为咪咪做了全面的健康检查，并决定以后无论去哪里都要更加小心，绝不再让它走失。', 'https://192.168.99.228:9000/animal-adopt/8.png', '救助站', '失散重聚,猫咪', 96, 1, 1, '2025-11-20 22:53:07', '2025-11-22 22:59:32'),
(3, '从病重到康复', '受伤的流浪狗豆豆被救助后，经过精心治疗重获新生...', '# 从病重到康复\r\r\n\r\r豆豆是一只三岁的混血狗，被发现时已经奄奄一息。它的后腿被车撞伤，伤口感染严重，几乎没有人认为它能活下来。\r\r\n\r\r## 紧急救治\r\r\n\r\r救助站的医生为豆豆进行了紧急手术。手术过程中，医生们发现豆豆的伤情比预想的更严重，但他们没有放弃。经过6小时的手术，豆豆终于脱离了危险。\r\r\n\r\r## 康复之路\r\r\n\r\r康复期间，豆豆经历了巨大的痛苦。但在医护人员和志愿者的精心照料下，它逐渐恢复了精神。虽然后腿永久性受损，但豆豆学会了用前腿行走。\r\r\n\r\r## 新的开始\r\r\n\r\r一个月后，豆豆被一个充满爱心的家庭领养了。新主人为它配备了特殊的轮椅，豆豆现在可以像其他狗狗一样快乐地奔跑。', 'https://192.168.99.228:9000/animal-adopt/9.png', '宠物医生李医生', '救助故事,狗狗', 152, 1, 0, '2025-11-20 22:53:07', '2025-11-22 22:59:36'),
(4, '高龄猫咪的新家', '八岁的老猫花花在救助站等待了半年，终于遇到了理解它的新主人...', '# 高龄猫咪的新家\r\r\n\r\r花花是一只八岁的波斯猫，因为主人搬家无法照顾而被送到了救助站。高龄的它在救助站里显得有些孤独，很少有人愿意领养一只老猫。\r\r\n\r\r## 漫长的等待\r\r\n\r\r半年过去了，花花在救助站里度过了它最孤独的日子。它每天都趴在窗边，看着来来往往的访客，希望有人能带它回家。\r\r\n\r\r## 命运的相遇\r\r\n\r\r直到有一天，一位退休的老奶奶来到了救助站。她说她也很孤独，需要一个伴侣。当她看到花花时，两个孤独的灵魂瞬间产生了共鸣。\r\r\n\r\r## 完美的匹配\r\r\n\r\r现在，花花和老奶奶每天都在一起。老奶奶为花花准备了最舒适的窝，花花则用温暖的陪伴填补了老奶奶心中的空缺。这是一个完美的匹配。', 'https://192.168.99.228:9000/animal-adopt/10.png', '领养者张女士', '成功领养,猫咪', 87, 0, 0, '2025-11-20 22:53:07', '2025-11-22 22:59:49'),
(5, '残疾狗狗的逆袭', '三条腿的狗狗小黑通过训练成为了一只优秀的治疗犬...', '# 残疾狗狗的逆袭\r\r\n\r\r小黑原本是一只流浪狗，在一次车祸中失去了一条前腿。人们都认为它的人生已经完结，但小黑用行动证明了生命的韧性。\r\r\n\r\r## 不放弃的精神\r\r\n\r\r被救助后，小黑虽然失去了一条腿，但它从未放弃过。它用三条腿学会了走路、跑步，甚至可以跳跃。它的坚强感动了每一个见到它的人。\r\r\n\r\r## 特殊的训练\r\r\n\r\r一位专业的训练师看到了小黑的潜力，决定将它训练成一只治疗犬。经过六个月的训练，小黑成功了。\r\r\n\r\r## 新的使命\r\r\n\r\r现在，小黑在医院里工作，为患者们带去欢乐和治疗。它用自己的故事告诉每一个人：残疾不是终点，而是新的开始。', 'https://192.168.99.228:9000/animal-adopt/11.png', '训练师刘老师', '救助故事,狗狗', 203, 0, 0, '2025-11-20 22:53:07', '2025-11-22 22:59:54'),
(6, '从恐惧到信任', '受过伤害的猫咪小白如何在新主人的关爱下重新学会信任...', '# 从恐惧到信任\r\r\n\r\r小白是一只曾经被虐待的猫咪。当它被救助站救出时，它对人类充满了恐惧和不信任。它躲在角落里，拒绝任何人的接近。\r\r\n\r\r## 漫长的治愈\r\r\n\r\r救助站的工作人员没有放弃。他们每天都坐在小白身边，用耐心和爱心慢慢接近它。几个月过去了，小白终于敢于接受抚摸。\r\r\n\r\r## 遇见真爱\r\r\n\r\r一位心理咨询师来到救助站，她看到了小白眼中的恐惧，也看到了它渴望被爱的渴望。她决定领养小白。\r\r\n\r\r## 重生\r\r\n\r\r在新主人的关爱下，小白逐渐打开了心扉。它学会了信任，学会了撒娇，学会了享受被爱的感觉。现在，小白是一只充满活力和爱心的猫咪，它用自己的故事告诉我们：爱可以治愈一切。', 'https://192.168.99.228:9000/animal-adopt/12.png', '心理咨询师赵老师', '成功领养,猫咪', 143, 0, 1, '2025-11-20 22:53:07', '2025-11-22 22:59:59');

INSERT INTO `t_story_like` VALUES
(3, 1, 3, '2025-11-20 22:55:23'),
(7, 1, 6, '2025-11-21 23:09:05'),
(8, 1, 2, '2025-11-23 17:16:51');

INSERT INTO `t_story_favorite` VALUES
(1, 1, 6, '2025-11-21 23:08:58'),
(2, 1, 2, '2025-11-23 17:16:52');

-- AI 客服会话数据
INSERT INTO `t_conversation_session` VALUES
(1, '6ad414bd2da544f4b070317cce91193a', 1, '新对话', NULL, 0, 'active', NULL, NULL, '2025-11-23 11:50:15', '2025-11-23 11:50:15', NULL, 0),
(2, '7b46d056fc7f4d05ae6c2eb87b8399d6', 1, '新对话', NULL, 0, 'active', NULL, NULL, '2025-11-23 11:55:01', '2025-11-23 11:55:01', NULL, 0),
(3, 'e32ba0c74c574a978d53c779aa7ab329', 1, '新对话', NULL, 0, 'active', NULL, NULL, '2025-11-23 11:59:51', '2025-11-23 11:59:51', NULL, 0),
(4, '6cf8eff39da545c78df3de8c2b1ebe24', 1, '新对话', NULL, 0, 'active', NULL, NULL, '2025-11-23 12:04:20', '2025-11-23 12:04:20', NULL, 0),
(5, 'b7f0555fcc244f77b2a2e8ab55843a80', 1, '新对话', NULL, 0, 'active', NULL, NULL, '2025-11-23 12:15:33', '2025-11-23 12:15:33', NULL, 0),
(6, 'c8a52268d5084fccaf6d2633cc5d78cd', 1, '新对话', NULL, 0, 'active', NULL, NULL, '2025-11-23 12:21:06', '2025-11-23 12:21:06', NULL, 0),
(7, 'c19a9554998c4f37885d637326d90341', 1, '新对话', NULL, 0, 'active', NULL, NULL, '2025-11-23 12:22:42', '2025-11-23 12:22:42', NULL, 0),
(8, '664222a15fe94df4ab263a6f98618aa1', 1, '新对话', NULL, 0, 'active', NULL, NULL, '2025-11-23 12:24:30', '2025-11-23 12:24:30', NULL, 0),
(9, '91f9451c0bc04670b55538234f95781b', 1, '新对话', NULL, 0, 'active', NULL, NULL, '2025-11-23 12:26:16', '2025-11-23 12:26:16', NULL, 0),
(10, 'a4f9b2e83a9e4a809b1173b9e41614ce', 1, '新对话', NULL, 0, 'active', NULL, NULL, '2025-11-23 12:27:52', '2025-11-23 12:27:52', NULL, 0),
(11, 'f1aef5fe047a45ad80eaa04da0e904c6', 1, '新对话', NULL, 0, 'active', NULL, NULL, '2025-11-23 12:32:53', '2025-11-23 12:32:53', NULL, 0),
(12, '15776eeecf2e43ccb629a5c88ab1ec2f', 1, '新对话', NULL, 0, 'active', NULL, NULL, '2025-11-23 12:34:35', '2025-11-23 12:34:35', NULL, 0),
(13, '5e3d7fec817140a68f3e77a551f00719', 1, '新对话', NULL, 0, 'active', NULL, NULL, '2025-11-23 12:39:28', '2025-11-23 12:39:28', NULL, 0),
(14, 'cea28f9a97804499af28f7a2c2d5eeb2', 1, '新对话', NULL, 0, 'active', NULL, NULL, '2025-11-23 13:31:26', '2025-11-23 13:31:26', NULL, 0),
(15, 'fc0d8b8fc0844a0997af6cc6864ab5d4', 1, '新对话', NULL, 0, 'active', NULL, NULL, '2025-11-23 13:47:56', '2025-11-23 13:47:56', NULL, 0),
(16, '01adf03ac957459a97564b37676f3aa7', 1, '新对话', NULL, 0, 'active', NULL, NULL, '2025-11-23 17:20:49', '2025-11-23 17:20:49', NULL, 0),
(17, '26274b7e741a4b9ba56820c27f28a3af', 1, '新对话', NULL, 2, 'active', '当然可以！以下是i宠园目前正在等待领养的几只可爱小狗，它们性格各异，总有一款适合您：\n\n🐾 **傻蛋小高 - 萨摩耶**\n温顺可爱的萨摩耶...', '2025-11-23 17:28:57', '2025-11-23 17:27:24', '2025-11-23 17:28:57', NULL, 0),
(18, 'c913f669867b42c7a68203b8f4a1033e', 1, '新对话', NULL, 1, 'active', '您好！我注意到您提到了“刚才推荐的那些”，但目前我们的对话中还没有进行过具体的宠物推荐。可能是之前的对话记录没有显示完整。...', '2025-11-23 17:29:59', '2025-11-23 17:29:54', '2025-11-23 17:29:59', NULL, 0),
(19, '7560b3265c014b4e99d4f79569a93b4b', 1, '新对话', NULL, 2, 'active', '很高兴您喜欢小狗！根据系统中的信息，我为您精选了几只性格各异、正在等待领养的可爱狗狗...', '2025-11-23 17:40:30', '2025-11-23 17:39:39', '2025-11-23 17:40:30', NULL, 0),
(20, '100780d55ae343a680d419c1dda86216', 1, '新对话', NULL, 2, 'active', '看到您喜欢狗狗，我非常高兴！根据我们i宠园当前可领养的犬只信息，为您精心挑选了几位可爱的毛孩子...', '2025-11-23 17:52:57', '2025-11-23 17:52:29', '2025-11-23 17:52:57', NULL, 0),
(21, 'e084d86ab45c4238b00e1b9253632462', 1, '新对话', NULL, 1, 'active', '您好！欢迎来到i宠园，很高兴您对养宠物感兴趣。作为新手，了解相关知识是非常重要的一步...', '2025-11-23 17:56:08', '2025-11-23 17:55:50', '2025-11-23 17:56:08', NULL, 0),
(22, '7afabbff034745e39a983fbd632c1217', 1, '新对话', NULL, 2, 'active', '您好！作为第一次养宠物的新手，您选择领养是非常有爱心的决定。我为您精选了几只性格温顺、适合新手饲养的可领养宠物...', '2025-11-23 19:06:24', '2025-11-23 19:06:00', '2025-11-23 19:06:24', NULL, 0),
(23, 'beb671326596427eae5cf39d1fa20333', 1, '新对话', NULL, 3, 'active', '很高兴您喜欢小狗！我已经为您筛选出几只性格各异、正在等待新家的可爱狗狗，它们都已绝育并完成疫苗接种...', '2025-11-23 19:17:17', '2025-11-23 19:11:11', '2025-11-23 19:17:17', NULL, 0),
(24, '6b1f0aee1fd84d05aaa904b9c0bb3c0f', 1, '新对话', NULL, 2, 'active', '太棒了！小猫确实是很好的伴侣，温柔又治愈。根据你的喜好，我为你筛选了几只性格温顺、适合新手的可领养猫咪...', '2025-11-24 11:01:33', '2025-11-24 11:01:00', '2025-11-24 11:01:33', NULL, 0),
(25, '9aeeafe151544d248cd740165e83fe4d', 1, '新对话', NULL, 0, 'active', NULL, NULL, '2025-11-24 14:38:08', '2025-11-24 14:38:08', NULL, 0),
(26, '0a21213bfca643bd93fec283b29ea565', 1, '新对话', NULL, 2, 'active', '很高兴您想要开启养宠之旅！作为首次养宠家庭，您需要做好以下准备：...', '2025-11-24 14:43:38', '2025-11-24 14:42:45', '2025-11-24 14:43:38', NULL, 0),
(27, 'a2a2a226462f4e2cbf1d7d27f1f22c67', 1, '新对话', NULL, 2, 'active', '目前在i宠园可领养的宠物主要包括以下几类：\n\n🐾 **狗狗**\n- 耶耶 - 萨摩耶：性格温顺，亲人友善...', '2025-11-24 22:49:55', '2025-11-24 22:49:19', '2025-11-24 22:49:55', NULL, 0);

-- AI客服对话历史
INSERT INTO `t_conversation_history` VALUES
(1, '26274b7e741a4b9ba56820c27f28a3af', 1, 'assistant', '"您好！欢迎来到i宠园，很高兴您想要开启养宠之旅 🐾\n\n虽然目前系统中暂无专门针对新手的入门指南文章，但我们可以为您提供一些关键建议来帮助您做好准备：\n\n- 了解养宠的基本责任：包括日常喂养、清洁、医疗保健和情感陪伴\n- 不同宠物有不同的需求，例如狗狗需要每天遛，猫咪相对独立但也需要互动\n- 领养前请评估自己的时间、经济能力和居住环境是否适合\n\n我可以为您推荐最适合新手的宠物类型，或者介绍当前正在等待领养的可爱毛孩子们。\n\n您更想先了解哪方面呢？比如“适合新手的宠物推荐”或“领养流程说明”？我很乐意继续协助您 ❤️"', NULL, NULL, NULL, '2025-11-23 17:27:33', '2025-11-23 17:27:33', '2025-11-23 17:27:33', 0),
(2, '26274b7e741a4b9ba56820c27f28a3af', 1, 'assistant', '"当然可以！以下是i宠园目前正在等待领养的几只可爱小狗，它们性格各异，总有一款适合您：\n\n🐾 **傻蛋小高 - 萨摩耶**\n温顺可爱的萨摩耶，白色毛发如雪般纯净，性格温和亲人，喜欢安静陪伴。适合想要一个温柔伴侣的家庭。\n\n🐾 **拉拉 - 拉布拉多寻回犬**\n性格温顺友善，对人热情，特别喜欢游泳和户外活动。已绝育并完成疫苗接种，是理想的家庭伴侣犬，尤其适合有孩子的家庭。\n\n🐾 **小黑 - 边境牧羊犬**\n智商极高、学习能力强，精力充沛且专注力好。需要主人有足够时间进行训练和互动，适合有养犬经验、能提供丰富智力刺激的家庭。\n\n🐾 **二哈 - 哈士奇**\n活泼好动，蓝眼睛帅气迷人，黑白毛色经典醒目。精力旺盛，喜欢奔跑跳跃，需要大量运动空间，适合热爱户外活动的主人。\n\n🐾 **泰迪 - 泰迪犬**\n活泼粘人，聪明伶俐，棕色卷毛非常可爱。已绝育打针，身体健康，喜欢与主人亲密互动，非常适合城市公寓生活。\n\n🐾 **小柴 - 柴犬**\n独立忠诚，赤色毛发配上标志笑容治愈力满分。性格活泼但不过分依赖，警惕性高，适合喜欢中型犬又希望有一定独立性的主人。\n\n🐾 **金金 - 金毛寻回犬**\n性格温顺听话，金黄色毛发闪耀动人，会基本指令，对小孩极有耐心。已完成绝育和疫苗接种，是公认的“国民级”家庭宠物首选。\n\n【温馨提示】\n每只狗狗都有独特的个性和需求，请根据您的居住环境、时间和生活方式选择最适合的伙伴。如果您对某位小朋友感兴趣，欢迎查看详情或提交领养申请，我们的工作人员将协助您完成后续流程 ❤️"', NULL, NULL, NULL, '2025-11-23 17:28:57', '2025-11-23 17:28:57', '2025-11-23 17:28:57', 0),
(3, 'c913f669867b42c7a68203b8f4a1033e', 1, 'assistant', '"您好！我注意到您提到了“刚才推荐的那些”，但目前我们的对话中还没有进行过具体的宠物推荐。可能是之前的对话记录没有显示完整。\n\n为了更好地帮助您，我可以根据您的偏好重新为您推荐一些优秀的宠物候选：\n\n- 您想要什么类型的宠物？（如猫、狗等）\n- 您希望它有什么样的性格？（如活泼、温顺、独立等）\n- 您的生活环境是怎样的？（如公寓、有院子、是否有小孩等）\n\n或者，我可以先为您展示当前最受欢迎的几只可领养宠物，这些都是非常可爱且适合家庭的好伙伴。\n\n【温馨提示】  \n每只宠物都是独特的个体，我们建议您根据自己的生活方式选择最适合的伴侣，而不是仅仅基于 popularity 做决定。我很乐意为您提供个性化建议！"', NULL, NULL, NULL, '2025-11-23 17:29:59', '2025-11-23 17:29:59', '2025-11-23 17:29:59', 0),
(4, '7560b3265c014b4e99d4f79569a93b4b', 1, 'assistant', '"我理解您是第一次考虑养宠物，这是一件非常值得期待的事！为了帮助您更好地准备，我为您查找了一些适合新手的入门指南和相关文章。\n\n目前系统中暂无直接匹配的入门指南内容，但您可以关注以下几个方面来为养宠做好准备：\n\n- 了解不同宠物的基本需求（如饮食、运动、护理）\n- 评估自己的生活方式是否适合养宠\n- 学习如何与宠物建立信任关系\n- 提前规划宠物的健康管理和日常开销\n\n【温馨提示】  \n如果您愿意，我可以根据您的居住环境、时间安排和性格特点，为您推荐最适合的宠物类型。欢迎随时告诉我您的具体情况，我会全力为您提供个性化建议！"', NULL, NULL, NULL, '2025-11-23 17:39:47', '2025-11-23 17:39:47', '2025-11-23 17:39:47', 0),
(5, '7560b3265c014b4e99d4f79569a93b4b', 1, 'assistant', '"很高兴您喜欢小狗！根据系统中的信息，我为您精选了几只性格各异、正在等待领养的可爱狗狗。每一只都经过健康检查，部分已完成疫苗接种和绝育手术。\n\n🐾 **拉拉 - 拉布拉多寻回犬**  \n性格温顺友善，对人友好，特别喜欢游泳，是优秀的家庭伴侣犬。已绝育并完成疫苗接种，非常适合有孩子的家庭。需要每天遛狗两次，最好有院子供其活动。\n\n🐾 **金金 - 金毛寻回犬**  \n性格温顺友善，聪明听话，会基本指令，对小孩很有耐心。已完成绝育和疫苗接种，训练有素，是孩子们的理想玩伴。适合有足够活动空间的家庭。\n\n🐾 **小黑 - 边境牧羊犬**  \n智商极高，学习能力强，精力充沛。已绝育并完成疫苗接种，需要大量脑力和体力活动。建议主人每天提供至少2小时户外运动，并能进行持续训练。\n\n🐾 **二哈 - 哈士奇**  \n性格活泼好动，精力旺盛，标志性的蓝眼睛非常迷人。已绝育并完成疫苗接种，虽然有点调皮，但性格友善。适合有经验的主人，需大量运动空间。\n\n🐾 **小柴 - 柴犬**  \n性格独立但忠诚，活泼好动，喜欢户外活动，警惕性高。已绝育并完成疫苗接种，标准赤色毛发，笑容治愈。需要每天至少1小时户外运动，适合喜欢户外活动的主人。\n\n🐾 **泰迪 - 泰迪犬**  \n性格活泼粘人，聪明伶俐，喜欢和主人互动。已绝育并接种疫苗，体型小巧，非常适合陪伴。需要定期美容修剪毛发，适合有时间照顾的家庭。\n\n🐾 **傻蛋小高 - 萨摩耶**  \n性格温顺，白色毛发蓬松可爱。虽然还未完成疫苗接种，但身体健康。需要定期梳理毛发，适合愿意花时间打理的主人。\n\n【温馨提示】  \n所有狗狗都已通过健康检查，建议您根据居住环境和生活方式选择最适合的伙伴。如果您对其中任何一位毛孩子感兴趣，请告诉我，我可以协助您提交领养申请。"', NULL, NULL, NULL, '2025-11-23 17:39:58', '2025-11-23 17:39:58', '2025-11-23 17:39:58', 0),
(6, '100780d55ae343a680d419c1dda86216', 1, 'assistant', '"您好！欢迎来到i宠园，很高兴您对养宠物感兴趣！\n\n作为新手家长，第一次养宠物确实会有很多需要了解的地方。虽然系统暂时没有检索到相关指南文章，但我可以为您提供一些基础建议：\n\n- **选择适合的宠物**：初次养宠建议从性格温顺、护理简单的品种开始，比如成年猫咪或性格稳定的犬种\n- **生活环境准备**：提前准备好宠物的生活空间，包括饮食用具、休息区域和卫生设施\n- **时间与精力投入**：考虑自己每天能陪伴宠物的时间，选择匹配生活节奏的伴侣\n- **医疗保健**：领养后应及时带宠物做健康检查，完成疫苗接种和驱虫\n\n【温馨提示】  \n如果您愿意分享更多关于您的生活方式（如居住环境、工作时间等），我可以为您推荐最适合的宠物类型。我们始终相信，每一只宠物都值得一个温暖的家，而每一个家庭也值得拥有最适合的毛孩子伴侣。"', NULL, NULL, NULL, '2025-11-23 17:52:38', '2025-11-23 17:52:38', '2025-11-23 17:52:38', 0),
(7, '100780d55ae343a680d419c1dda86216', 1, 'assistant', '"看到您喜欢狗狗，我非常高兴！根据我们i宠园当前可领养的犬只信息，为您精心挑选了几位可爱的毛孩子：\n\n🐾 **傻蛋小高 - 萨摩耶**\n性格温顺，5岁的成年犬，白色毛发非常可爱。萨摩耶以\"微笑天使\"著称，适合想要一个温柔伴侣的家庭。\n\n🐾 **拉拉 - 拉布拉多寻回犬**\n2岁的黄色拉布拉多，性格温顺友善，已绝育和接种疫苗。优秀的家庭伴侣犬，特别喜欢游泳，对小孩很有耐心。\n\n🐾 **金金 - 金毛寻回犬**\n1.5岁的帅气金毛，金黄色毛发，性格温顺聪明，已完成基本训练。非常适合家庭饲养，是孩子们的理想玩伴。\n\n🐾 **二哈 - 哈士奇**\n1.25岁的黑白哈士奇，蓝眼睛炯炯有神，精力充沛，需要大量运动。适合有经验、能陪伴户外活动的主人。\n\n🐾 **小柴 - 柴犬**\n1岁的赤色柴犬，活泼好动，独立忠诚，有着治愈系的笑容。需要每天足够的运动量。\n\n【温馨提示】\n每只狗狗都有独特的个性和需求。建议您根据自己的生活环境（如住房大小）、作息时间（能否定时遛狗）和养宠经验来选择最适合的伙伴。如果您愿意分享更多关于您的生活方式，我可以为您提供更精准的推荐。\n\n欢迎随时预约到园区与这些可爱的毛孩子互动，亲身感受哪一位最打动您的心！"', NULL, NULL, NULL, '2025-11-23 17:52:57', '2025-11-23 17:52:57', '2025-11-23 17:52:57', 0),
(8, 'e084d86ab45c4238b00e1b9253632462', 1, 'assistant', '"您好！欢迎来到i宠园，很高兴您对养宠物感兴趣。作为新手，了解相关知识是非常重要的一步。以下是为您准备的入门信息：\n\n【新手养宠须知】\n- 了解领养责任：宠物是家庭成员，需要长期关爱和照顾\n- 准备基本用品：食盆、水盆、猫砂盆/狗厕所、窝、玩具等\n- 了解基础护理：饮食管理、日常清洁、疫苗驱虫等\n- 学习行为训练：帮助宠物适应家庭生活\n- 预留时间和精力：陪伴是建立感情的关键\n\n以下是一些当前正在等待领养的可爱宠物，也许能给您一些启发：\n\n🐾 **傻蛋小高 - 萨摩耶**\n温顺可爱的性格，适合想要大型犬的家庭。虽然还未完成疫苗接种，但健康状况良好。\n\n🐾 **小狸 - 中华狸花猫**\n性格独立机警，适应能力强，是优秀的捕鼠能手，已绝育并完成疫苗接种。\n\n🐾 **布布 - 布偶猫**\n性格温顺甜美，喜欢被抱，叫声轻柔，是完美的家庭伴侣，需要定期毛发护理。\n\n🐾 **小暹 - 暹罗猫**\n性格活泼粘人，喜欢和主人交流，需要大量陪伴，适合时间充裕的家庭。\n\n🐾 **橘子 - 中华田园猫**\n性格亲人温顺，喜欢晒太阳，很会撒娇，需要注意饮食控制避免过胖。\n\n【温馨提示】\n如果您是首次养宠，建议从性格温顺、护理相对简单的宠物开始。我们推荐您先阅读新手养宠指南，并考虑自己的生活方式是否适合养宠。领养前可以预约见面，与工作人员详细沟通。\n\n有任何疑问都欢迎随时咨询，我们会全程为您提供专业指导和支持。"', NULL, NULL, NULL, '2025-11-23 17:56:08', '2025-11-23 17:56:08', '2025-11-23 17:56:08', 0),
(9, '7afabbff034745e39a983fbd632c1217', 1, 'user', '"我从来没养过宠物 有什么推荐的吗"', NULL, NULL, NULL, '2025-11-23 19:06:24', '2025-11-23 19:06:24', '2025-11-23 19:06:24', 0),
(10, '7afabbff034745e39a983fbd632c1217', 1, 'assistant', '"您好！作为第一次养宠物的新手，您选择领养是非常有爱心的决定。我为您精选了几只性格温顺、适合新手饲养的可领养宠物，它们都已经完成疫苗接种和绝育手术，健康状况良好。\n\n🐾 **圆圆 - 英国短毛猫**\n性格温顺安静，喜欢睡觉，不太爱动，非常适合公寓生活。\n适合希望拥有一个安静、不吵闹的伴侣的您。\n\n🐾 **布布 - 布偶猫**\n性格温顺甜美，喜欢被抱，叫声轻柔，是完美的家庭伴侣。\n适合想要一个亲人、温柔的猫咪朋友的新手主人。\n\n🐾 **橘子 - 中华田园猫（橘色）**\n性格亲人温顺，喜欢晒太阳，很会撒娇，食量较大。\n适合喜欢互动、愿意花时间陪伴猫咪的家庭。\n\n🐾 **拉拉 - 拉布拉多寻回犬**\n性格温顺友善，对人友好，特别是对小孩特别有耐心。\n适合有足够空间和时间遛狗的家庭，是优秀的家庭伴侣犬。\n\n🐾 **小狸 - 中华狸花猫**\n性格独立机警，适应能力强，是优秀的捕鼠能手，但也亲人。\n适合希望养一只既独立又能互动的猫咪的新手。\n\n【温馨提示】\n新手养宠建议从以下几点开始准备：\n- 准备好基础用品：猫砂盆/狗厕所、食盆水盆、舒适窝垫\n- 选择适合的宠物食品，避免突然更换食物\n- 定期进行健康检查，按时驱虫\n- 给予足够的耐心和关爱，帮助新成员适应新家\n\n如果您对某只宠物感兴趣，我可以为您提供更多详细信息或协助您提交领养申请。"', NULL, NULL, NULL, '2025-11-23 19:06:24', '2025-11-23 19:06:24', '2025-11-23 19:06:24', 0),
(11, 'beb671326596427eae5cf39d1fa20333', 1, 'assistant', '"您好！欢迎来到i宠园，很高兴您考虑领养人生中的第一只宠物。作为新手家长，选择一只性格温顺、易于照顾的宠物非常重要。\n\n以下是目前可领养的几只非常适合新手的宠物推荐：\n\n🐾 **布布 - 布偶猫**\n性格温顺甜美，喜欢被抱，叫声轻柔，是完美的家庭伴侣。已绝育并完成疫苗接种，适合初次养猫的家庭。\n\n🐾 **圆圆 - 英国短毛猫**\n性格温顺安静，喜欢睡觉，不太爱动，非常适合公寓饲养。已绝育，护理相对简单。\n\n🐾 **拉拉 - 拉布拉多寻回犬**\n性格温顺友善，对人友好，特别是对小孩特别有耐心，是优秀的家庭伴侣犬。已绝育并完成疫苗接种。\n\n【温馨提示】\n我们建议新手家长从猫咪开始，因为它们相对独立，护理需求较狗狗少。如果您选择养狗，请确保能每天抽出时间遛狗和陪伴。\n\n需要了解更多护理知识吗？我可以为您提供详细的《新手养宠指南》和各类宠物护理建议。"', NULL, NULL, NULL, '2025-11-23 19:11:24', '2025-11-23 19:11:24', '2025-11-23 19:11:24', 0),
(12, 'beb671326596427eae5cf39d1fa20333', 1, 'assistant', '"您好！欢迎来到i宠园，作为新手养宠家庭，选择一只性格温顺、容易相处的宠物非常重要。我已经为您筛选了一些非常适合新手的可领养宠物。\n\n根据您的情况，我特别推荐以下几只温顺友好的小伙伴：\n\n🐾 **布布 - 布偶猫**\n性格温顺甜美，喜欢被抱，叫声轻柔，是完美的家庭伴侣\n适合喜欢温柔猫咪、想要亲密互动的新手主人\n\n🐾 **橘子 - 中华田园猫**\n性格亲人温顺，食量较大，喜欢晒太阳，很会撒娇\n适合想要可爱陪伴、愿意给予关爱的家庭\n\n🐾 **拉拉 - 拉布拉多寻回犬**\n性格温顺友善，对人友好，喜欢游泳，是优秀的家庭伴侣犬\n适合有足够活动空间、能每天遛狗的家庭\n\n【温馨提示】\n作为新手，建议您优先考虑已绝育且完成疫苗接种的宠物，这样可以减少初期的医疗压力。同时，我们建议您先从性格温顺的品种开始，逐步学习宠物护理知识。\n\n如果您对任何一只小可爱感兴趣，我可以为您提供更详细的护理指南和领养流程说明。"', NULL, NULL, NULL, '2025-11-23 19:17:03', '2025-11-23 19:17:03', '2025-11-23 19:17:03', 0),
(13, 'beb671326596427eae5cf39d1fa20333', 1, 'assistant', '"很高兴您喜欢小狗！我已经为您筛选出几只性格各异、正在等待新家的可爱狗狗，它们都已绝育并完成疫苗接种，健康状况良好。\n\n🐾 **拉拉 - 拉布拉多寻回犬**\n性格温顺友善，对人友好，喜欢游泳，是优秀的家庭伴侣犬\n适合有院子、能每天遛狗的家庭，需要定期梳理毛发\n\n🐾 **金金 - 金毛寻回犬**\n性格温顺友善，聪明听话，喜欢和人互动，对小孩很有耐心\n适合家庭饲养，已完成基础训练，会基本指令\n\n🐾 **泰迪 - 泰迪犬**\n性格活泼粘人，聪明伶俐，喜欢和主人互动，适合陪伴\n体型小巧，适合公寓生活，但需要定期美容修剪毛发\n\n🐾 **小柴 - 柴犬**\n性格独立但忠诚，活泼好动，喜欢户外活动，警惕性高\n适合喜欢户外活动的主人，需要每天至少1小时运动\n\n🐾 **傻蛋小高 - 萨摩耶**\n性格温顺，一只可爱的萨摩耶，毛色洁白如雪\n需要大量毛发护理，适合有时间和精力照顾的家庭\n\n【温馨提示】\n建议新手主人优先考虑拉布拉多或金毛这类性格稳定、易于相处的犬种。这些狗狗都已经过基础社会化训练，更容易适应新家庭。\n\n如果您对某只狗狗特别感兴趣，我可以为您提供详细的领养流程说明和护理指南。"', NULL, NULL, NULL, '2025-11-23 19:17:17', '2025-11-23 19:17:17', '2025-11-23 19:17:17', 0),
(14, '6b1f0aee1fd84d05aaa904b9c0bb3c0f', 1, 'assistant', '"我能感受到你对养宠的谨慎和认真，这真的很棒！作为第一次养宠物的新手，确实有很多需要了解的地方。让我为你整理一些重要的注意事项，帮助你做好充分准备。\n\n【新手养宠核心要点】\n\n- **心理准备**：宠物是家庭成员，不是玩具，它们会依赖你10年甚至更久\n- **经济预算**：每月需预留食物、驱虫、疫苗等基础开销，以及应急医疗费用\n- **时间投入**：每天需要陪伴、喂食、清理，狗狗还需要定时遛弯\n- **居住环境**：考虑家中空间是否适合，是否有条件做好安全防护\n\n【温馨提示】\n建议先从性格温顺、护理相对简单的宠物开始，比如成年猫咪或小型犬。我们有专门针对新手的领养指导服务，可以为你匹配最适合的宠物伙伴。要不要先了解一下目前有哪些特别适合新手的可领养宠物呢？"', NULL, NULL, NULL, '2025-11-24 11:01:12', '2025-11-24 11:01:12', '2025-11-24 11:01:12', 0),
(15, '6b1f0aee1fd84d05aaa904b9c0bb3c0f', 1, 'assistant', '"太棒了！小猫确实是很好的伴侣，温柔又治愈。根据你的喜好，我为你筛选了几只性格温顺、适合新手的可领养猫咪，它们都已经完成绝育和疫苗接种，健康状况良好。\n\n🐾 **布布 - 布偶猫**\n性格温顺甜美，喜欢被抱，叫声轻柔，是完美的家庭伴侣\n适合喜欢亲密互动、能定期打理毛发的家庭\n\n🐾 **橘子 - 中华田园猫**\n性格亲人温顺，食量较大，喜欢晒太阳，很会撒娇\n适合想要活泼一点但又不失亲人的主人\n\n🐾 **圆圆 - 英国短毛猫**\n性格温顺安静，喜欢睡觉，不太爱动，非常适合公寓饲养\n适合生活节奏较慢、喜欢安静环境的主人\n\n【温馨提示】\n每只猫咪都有独特的个性和需求，建议先了解清楚再做决定。如果你对某只特别感兴趣，我可以为你提供更详细的护理建议和领养流程说明。想了解更多关于它们的信息吗？"', NULL, NULL, NULL, '2025-11-24 11:01:33', '2025-11-24 11:01:33', '2025-11-24 11:01:33', 0),
(16, '0a21213bfca643bd93fec283b29ea565', 1, 'assistant', '"看到您喜欢小狗，我为您精心挑选了几位正在等待新家的可爱毛孩子！每一只都性格温顺，健康状况良好，期待与您相遇。\n\n🐾 **小博 - 博美犬**\n性格温顺的小型犬，白色毛发蓬松可爱。体型小巧适合公寓生活，是贴心的陪伴伙伴。\n\n🐾 **耶耶 - 萨摩耶**\n一只拥有灿烂笑容的萨摩耶，白色毛发如雪般纯净。性格温顺友善，喜欢与人亲近，是\"微笑天使\"的代表。\n\n🐾 **拉拉 - 拉布拉多寻回犬**\n金黄色毛发的温柔大男孩，性格超好，对人友好，特别喜欢游泳。已绝育并完成疫苗接种，是优秀的家庭伴侣犬，尤其适合有孩子的家庭。\n\n🐾 **小黑 - 边境牧羊犬**\n黑白相间的聪明狗狗，智商极高，学习能力强。需要主人有足够时间进行训练和互动，适合喜欢运动和训练的主人。\n\n🐾 **二哈 - 哈士奇**\n帅气的黑白毛色配上标志性的蓝眼睛，精力充沛，喜欢奔跑。虽然有点调皮，但性格友善，适合有养犬经验的主人。\n\n🐾 **泰迪 - 泰迪犬**\n棕色卷毛的可爱小家伙，活泼粘人，聪明伶俐。非常适合需要陪伴的家庭，会给您的生活带来无尽欢乐。\n\n🐾 **小柴 - 柴犬**\n赤色毛发的治愈系狗狗，性格独立但忠诚，活泼好动。标志性的\"柴犬笑\"能瞬间融化您的心。\n\n🐾 **金金 - 金毛寻回犬**\n金黄色毛发的帅气大男孩，性格温顺友善，聪明听话。会基本指令，是孩子们的好伙伴，也是理想的家庭犬。\n\n【可领养状态】\n以上所有狗狗目前均为可领养状态，已完成必要的健康检查。\n\n【温馨提示】\n每只狗狗都有独特的个性和需求，建议您根据自己的生活方式和居住环境选择最适合的伙伴。如果您对某位小天使特别感兴趣，可以告诉我，我会为您提供更多详细信息。"', NULL, NULL, NULL, '2025-11-24 14:43:00', '2025-11-24 14:43:00', '2025-11-24 14:43:00', 0),
(17, '0a21213bfca643bd93fec283b29ea565', 1, 'assistant', '"很高兴您想要开启养宠之旅！作为首次养宠家庭，您需要做好以下准备：\n\n【领养基本条件】\n- 年满18周岁，有稳定收入和住所\n- 家庭成员均同意领养\n- 了解并承诺对宠物负责终身照顾\n- 接受领养回访\n\n【居家必备物品】\n- 食盆、水盆（建议选择防滑款式）\n- 适合宠物年龄的专用粮\n- 舒适的狗窝或猫爬架\n- 牵引绳（外出必备）\n- 猫砂盆及猫砂（如领养猫咪）\n- 宠物专用梳子和清洁工具\n- 安全的玩具（磨牙棒、逗猫棒等）\n\n【温馨提示】\n新手养宠建议从性格温顺、护理简单的品种开始。我们为每位新领养家庭提供《新手养宠指南》，包含喂养、训练、健康检查等全方位指导。\n\n如果您愿意，我可以为您安排一次免费的养宠咨询，帮助您更好地准备迎接新成员。"', NULL, NULL, NULL, '2025-11-24 14:43:38', '2025-11-24 14:43:38', '2025-11-24 14:43:38', 0),
(18, 'a2a2a226462f4e2cbf1d7d27f1f22c67', 1, 'assistant', '"目前在i宠园可领养的宠物种类丰富，涵盖了多种适合不同家庭和生活方式的小伙伴。以下是部分正在等待新家的可爱动物：\n\n🐾 **耶耶 - 萨摩耶**\n温顺、亲人，性格温和友善，是理想的家庭伴侣犬。\n适合喜欢中大型犬、有耐心陪伴狗狗的家庭。\n\n🐾 **小博 - 博美犬**\n温顺且活泼，体型小巧，非常适合公寓生活。\n适合初次养狗或居住空间有限的朋友。\n\n🐾 **边边 - 边牧犬**\n精力充沛、聪明伶俐，需要较多运动和互动。\n适合活跃家庭或有训练经验的主人。\n\n🐾 **金金 - 金毛**\n性格开朗、活泼友善，对儿童和其他宠物都很友好。\n是经典的“暖男”型家庭犬，适合有孩子的家庭。\n\n🐾 **布布 - 布偶猫**\n调皮可爱，外表甜美如娃娃，性格亲人粘人。\n适合喜欢互动、愿意花时间陪伴猫咪的人。\n\n🐾 **小狸 - 中华狸花猫**\n独立机警，身体强壮，适应力强，还是一位捕鼠小能手。\n已绝育并完成疫苗接种，省心好养。\n\n🐾 **仓仓 - 仓鼠**\n胆小谨慎，安静可爱，照顾简单。\n适合学生或生活节奏较快、时间有限的上班族。\n\n🐾 **小松 - 美洲红松鼠**\n活泼好动，充满野趣，观察性强。\n适合有自然庭院或对小型野生动物感兴趣的家庭。\n\n【温馨提示】  \n每一只宠物都有独特的性格和需求，我们鼓励您根据自己的生活环境、时间和精力选择最合适的伙伴。如果您想了解某只宠物的更多细节，或希望我们为您个性化推荐，请随时告诉我！"', NULL, NULL, NULL, '2025-11-24 22:49:31', '2025-11-24 22:49:31', '2025-11-24 22:49:31', 0),
(19, 'a2a2a226462f4e2cbf1d7d27f1f22c67', 1, 'assistant', '"目前在i宠园可领养的宠物主要包括以下几类：\n\n🐾 **狗狗**\n- 耶耶 - 萨摩耶：性格温顺，亲人友善\n- 小博 - 博美犬：体型小巧，活泼可爱\n- 边边 - 边牧犬：聪明伶俐，精力充沛\n- 金金 - 金毛：性格开朗，适合家庭饲养\n\n🐾 **猫咪**\n- 小狸 - 中华狸花猫：独立机警，适应力强，已绝育并完成疫苗接种\n- 布布 - 布偶猫：外表甜美，性格调皮粘人\n\n🐾 **松鼠**\n- 小松 - 美洲红松鼠：活泼好动，充满野趣\n- 大松 - 美洲红松鼠：谨慎安静，观察力强\n- 松松 - 松鼠：胆小谨慎，适合自然环境\n\n🐾 **仓鼠**\n- 仓仓 - 仓鼠：白色毛发，胆小谨慎，照顾简单\n\n【温馨提示】  \n所有宠物均处于“可领养”状态，健康状况良好。每只宠物都有独特的性格和需求，建议根据您的居住环境、生活方式和时间安排选择最适合的伴侣。\n\n如果您对某类宠物感兴趣，或想了解如何为特定宠物提供合适的照顾，请随时告诉我！"', NULL, NULL, NULL, '2025-11-24 22:49:55', '2025-11-24 22:49:55', '2025-11-24 22:49:55', 0);

SET FOREIGN_KEY_CHECKS = 1;
