-- ============================================
-- 宠物领养管理系统 - 数据库初始化脚本
-- 版本: 1.0.0
-- 创建时间: 2025-10-30
-- 数据库: MySQL 8.0+
-- 字符集: UTF-8
-- ============================================

-- 创建数据库
CREATE DATABASE IF NOT EXISTS `animal_adopt` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `animal_adopt`;

-- ============================================
-- 核心业务表
-- ============================================

-- ----------------------------
-- 1. 用户表 t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `username` VARCHAR(50) NOT NULL COMMENT '用户名',
    `password` VARCHAR(255) NOT NULL COMMENT '密码(加密)',
    `nickname` VARCHAR(50) DEFAULT NULL COMMENT '昵称',
    `real_name` VARCHAR(50) DEFAULT NULL COMMENT '真实姓名',
    `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
    `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    `avatar` VARCHAR(500) DEFAULT NULL COMMENT '头像URL',
    `gender` TINYINT DEFAULT 0 COMMENT '性别 (0:未知 1:男 2:女)',
    `age` INT DEFAULT NULL COMMENT '年龄',
    `address` VARCHAR(255) DEFAULT NULL COMMENT '地址',
    `id_card` VARCHAR(20) DEFAULT NULL COMMENT '身份证号',
    `occupation` VARCHAR(100) DEFAULT NULL COMMENT '职业',
    `housing` VARCHAR(100) DEFAULT NULL COMMENT '住房情况',
    `has_experience` TINYINT(1) DEFAULT 0 COMMENT '是否有养宠经验 (0:否 1:是)',
    `role` VARCHAR(50) NOT NULL DEFAULT 'user' COMMENT '用户角色 (user:普通用户 super_admin:超级管理员 application_auditor:审核员 housekeeper:管家)',
    `status` TINYINT DEFAULT 1 COMMENT '用户状态 (0:禁用 1:正常)',
    `certified` TINYINT(1) DEFAULT 0 COMMENT '是否已认证 (0:未认证 1:已认证)',
    `certification_files` TEXT DEFAULT NULL COMMENT '认证资料URL（JSON数组）',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除 (0:未删除 1:已删除)',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`),
    UNIQUE KEY `uk_phone` (`phone`),
    UNIQUE KEY `uk_email` (`email`),
    KEY `idx_role` (`role`),
    KEY `idx_status` (`status`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- ----------------------------
-- 2. 宠物表 t_pet
-- ----------------------------
DROP TABLE IF EXISTS `t_pet`;
CREATE TABLE `t_pet` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '宠物ID',
    `name` VARCHAR(100) NOT NULL COMMENT '宠物名称',
    `category` VARCHAR(50) NOT NULL COMMENT '宠物种类 (dog:狗 cat:猫 bird:鸟 rabbit:兔子 other:其他)',
    `breed` VARCHAR(100) DEFAULT NULL COMMENT '品种',
    `gender` TINYINT DEFAULT 0 COMMENT '性别 (0:未知 1:公 2:母)',
    `age` INT DEFAULT NULL COMMENT '年龄（月）',
    `weight` DECIMAL(5,2) DEFAULT NULL COMMENT '体重（kg）',
    `color` VARCHAR(100) DEFAULT NULL COMMENT '毛色',
    `neutered` TINYINT(1) DEFAULT 0 COMMENT '是否绝育 (0:否 1:是)',
    `vaccinated` TINYINT(1) DEFAULT 0 COMMENT '是否接种疫苗 (0:否 1:是)',
    `health_status` VARCHAR(50) DEFAULT 'healthy' COMMENT '健康状态 (healthy:健康 sick:生病 recovering:康复中)',
    `health_description` TEXT DEFAULT NULL COMMENT '健康描述',
    `personality` TEXT DEFAULT NULL COMMENT '性格特点',
    `description` TEXT DEFAULT NULL COMMENT '宠物描述',
    `images` TEXT DEFAULT NULL COMMENT '宠物图片URL（JSON数组）',
    `cover_image` VARCHAR(500) DEFAULT NULL COMMENT '封面图片URL',
    `rescue_date` DATE DEFAULT NULL COMMENT '救助时间',
    `rescue_location` VARCHAR(255) DEFAULT NULL COMMENT '救助地点',
    `adoption_requirements` TEXT DEFAULT NULL COMMENT '领养要求',
    `adoption_status` VARCHAR(50) NOT NULL DEFAULT 'available' COMMENT '领养状态 (available:可领养 pending:待审核 adopted:已领养 reserved:已预定)',
    `shelf_status` TINYINT DEFAULT 1 COMMENT '上架状态 (0:下架 1:上架)',
    `view_count` INT DEFAULT 0 COMMENT '浏览次数',
    `favorite_count` INT DEFAULT 0 COMMENT '收藏次数',
    `application_count` INT DEFAULT 0 COMMENT '申请次数',
    `sort_order` INT DEFAULT 0 COMMENT '排序号',
    `create_by` BIGINT DEFAULT NULL COMMENT '创建人ID',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除 (0:未删除 1:已删除)',
    PRIMARY KEY (`id`),
    KEY `idx_category` (`category`),
    KEY `idx_adoption_status` (`adoption_status`),
    KEY `idx_shelf_status` (`shelf_status`),
    KEY `idx_create_time` (`create_time`),
    KEY `idx_sort_order` (`sort_order`),
    KEY `idx_create_by` (`create_by`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='宠物表';

-- ----------------------------
-- 3. 领养申请表 t_adoption_application
-- ----------------------------
DROP TABLE IF EXISTS `t_adoption_application`;
CREATE TABLE `t_adoption_application` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '申请ID',
    `application_no` VARCHAR(50) NOT NULL COMMENT '申请编号',
    `user_id` BIGINT NOT NULL COMMENT '申请人ID',
    `pet_id` BIGINT NOT NULL COMMENT '宠物ID',
    `reason` TEXT DEFAULT NULL COMMENT '申请原因',
    `family_info` TEXT DEFAULT NULL COMMENT '家庭成员情况',
    `careplan` TEXT DEFAULT NULL COMMENT '养宠计划',
    `additional_info` TEXT DEFAULT NULL COMMENT '补充说明',
    `contact_phone` VARCHAR(20) NOT NULL COMMENT '联系电话',
    `contact_address` VARCHAR(255) NOT NULL COMMENT '联系地址',
    `status` VARCHAR(50) NOT NULL DEFAULT 'pending' COMMENT '申请状态 (pending:待审核 approved:已通过 rejected:已拒绝 cancelled:已撤销)',
    `reviewer_id` BIGINT DEFAULT NULL COMMENT '审核人ID',
    `review_time` DATETIME DEFAULT NULL COMMENT '审核时间',
    `review_comment` TEXT DEFAULT NULL COMMENT '审核意见',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '申请时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除 (0:未删除 1:已删除)',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_application_no` (`application_no`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_pet_id` (`pet_id`),
    KEY `idx_status` (`status`),
    KEY `idx_create_time` (`create_time`),
    CONSTRAINT `fk_adoption_user` FOREIGN KEY (`user_id`) REFERENCES `t_user` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT `fk_adoption_pet` FOREIGN KEY (`pet_id`) REFERENCES `t_pet` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='领养申请表';

-- ----------------------------
-- 4. 收藏表 t_favorite
-- ----------------------------
DROP TABLE IF EXISTS `t_favorite`;
CREATE TABLE `t_favorite` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '收藏ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `pet_id` BIGINT NOT NULL COMMENT '宠物ID',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '收藏时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除 (0:未删除 1:已删除)',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_pet` (`user_id`, `pet_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_pet_id` (`pet_id`),
    KEY `idx_create_time` (`create_time`),
    CONSTRAINT `fk_favorite_user` FOREIGN KEY (`user_id`) REFERENCES `t_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `fk_favorite_pet` FOREIGN KEY (`pet_id`) REFERENCES `t_pet` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='收藏表';

-- ----------------------------
-- 5. 文章表 t_article
-- ----------------------------
DROP TABLE IF EXISTS `t_article`;
CREATE TABLE `t_article` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '文章ID',
    `title` VARCHAR(200) NOT NULL COMMENT '文章标题',
    `category` VARCHAR(50) NOT NULL COMMENT '文章分类 (guide:养宠指南 story:领养故事 news:新闻动态)',
    `cover_image` VARCHAR(500) DEFAULT NULL COMMENT '封面图片URL',
    `summary` VARCHAR(500) DEFAULT NULL COMMENT '文章摘要',
    `content` LONGTEXT NOT NULL COMMENT '文章内容',
    `author` VARCHAR(100) DEFAULT NULL COMMENT '作者',
    `view_count` INT DEFAULT 0 COMMENT '浏览次数',
    `like_count` INT DEFAULT 0 COMMENT '点赞次数',
    `status` TINYINT DEFAULT 1 COMMENT '发布状态 (0:草稿 1:已发布 2:已下架)',
    `publish_time` DATETIME DEFAULT NULL COMMENT '发布时间',
    `sort_order` INT DEFAULT 0 COMMENT '排序号',
    `create_by` BIGINT DEFAULT NULL COMMENT '创建人ID',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除 (0:未删除 1:已删除)',
    PRIMARY KEY (`id`),
    KEY `idx_category` (`category`),
    KEY `idx_status` (`status`),
    KEY `idx_create_time` (`create_time`),
    KEY `idx_publish_time` (`publish_time`),
    KEY `idx_sort_order` (`sort_order`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文章表';

-- ============================================
-- 沟通交互表
-- ============================================

-- ----------------------------
-- 6. 会话表 t_chat_session
-- ----------------------------
DROP TABLE IF EXISTS `t_chat_session`;
CREATE TABLE `t_chat_session` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '会话ID',
    `session_type` VARCHAR(50) NOT NULL COMMENT '会话类型 (ai:AI助手 housekeeper:管家咨询)',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `housekeeper_id` BIGINT DEFAULT NULL COMMENT '管家ID（仅housekeeper类型）',
    `pet_id` BIGINT DEFAULT NULL COMMENT '关联宠物ID（可选）',
    `last_message` TEXT DEFAULT NULL COMMENT '最后一条消息',
    `last_message_time` DATETIME DEFAULT NULL COMMENT '最后消息时间',
    `unread_count` INT DEFAULT 0 COMMENT '未读消息数',
    `status` TINYINT DEFAULT 1 COMMENT '会话状态 (0:已关闭 1:进行中)',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除 (0:未删除 1:已删除)',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_housekeeper_id` (`housekeeper_id`),
    KEY `idx_session_type` (`session_type`),
    KEY `idx_create_time` (`create_time`),
    CONSTRAINT `fk_session_user` FOREIGN KEY (`user_id`) REFERENCES `t_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='会话表';

-- ----------------------------
-- 7. 聊天消息表 t_chat_message
-- ----------------------------
DROP TABLE IF EXISTS `t_chat_message`;
CREATE TABLE `t_chat_message` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '消息ID',
    `session_id` BIGINT NOT NULL COMMENT '会话ID',
    `sender_id` BIGINT NOT NULL COMMENT '发送者ID (0表示系统/AI)',
    `sender_type` VARCHAR(50) NOT NULL COMMENT '发送者类型 (user:用户 ai:AI system:系统 housekeeper:管家)',
    `content` TEXT NOT NULL COMMENT '消息内容',
    `message_type` VARCHAR(50) DEFAULT 'text' COMMENT '消息类型 (text:文本 image:图片 file:文件)',
    `is_read` TINYINT(1) DEFAULT 0 COMMENT '是否已读 (0:未读 1:已读)',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发送时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除 (0:未删除 1:已删除)',
    PRIMARY KEY (`id`),
    KEY `idx_session_id` (`session_id`),
    KEY `idx_sender_id` (`sender_id`),
    KEY `idx_create_time` (`create_time`),
    CONSTRAINT `fk_message_session` FOREIGN KEY (`session_id`) REFERENCES `t_chat_session` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='聊天消息表';

-- ============================================
-- 系统管理表
-- ============================================

-- ----------------------------
-- 8. 操作日志表 t_operation_log
-- ----------------------------
DROP TABLE IF EXISTS `t_operation_log`;
CREATE TABLE `t_operation_log` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '日志ID',
    `user_id` BIGINT DEFAULT NULL COMMENT '操作用户ID',
    `username` VARCHAR(50) DEFAULT NULL COMMENT '操作用户名',
    `operation` VARCHAR(200) DEFAULT NULL COMMENT '操作描述',
    `method` VARCHAR(200) DEFAULT NULL COMMENT '请求方法',
    `params` TEXT DEFAULT NULL COMMENT '请求参数',
    `result` TEXT DEFAULT NULL COMMENT '返回结果',
    `ip` VARCHAR(50) DEFAULT NULL COMMENT 'IP地址',
    `location` VARCHAR(200) DEFAULT NULL COMMENT '操作地点',
    `time` INT DEFAULT NULL COMMENT '执行时长（毫秒）',
    `status` TINYINT DEFAULT 1 COMMENT '操作状态 (0:失败 1:成功)',
    `error_msg` TEXT DEFAULT NULL COMMENT '错误信息',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_create_time` (`create_time`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='操作日志表';

-- ----------------------------
-- 9. 系统配置表 t_system_config
-- ----------------------------
DROP TABLE IF EXISTS `t_system_config`;
CREATE TABLE `t_system_config` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '配置ID',
    `config_key` VARCHAR(100) NOT NULL COMMENT '配置键',
    `config_value` TEXT DEFAULT NULL COMMENT '配置值',
    `config_desc` VARCHAR(255) DEFAULT NULL COMMENT '配置描述',
    `config_type` VARCHAR(50) DEFAULT 'string' COMMENT '配置类型 (string:字符串 number:数字 boolean:布尔 json:JSON)',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_config_key` (`config_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统配置表';

-- ----------------------------
-- 10. 通知消息表 t_notification
-- ----------------------------
DROP TABLE IF EXISTS `t_notification`;
CREATE TABLE `t_notification` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '通知ID',
    `user_id` BIGINT NOT NULL COMMENT '接收用户ID',
    `title` VARCHAR(200) NOT NULL COMMENT '通知标题',
    `content` TEXT NOT NULL COMMENT '通知内容',
    `type` VARCHAR(50) NOT NULL COMMENT '通知类型 (system:系统通知 adoption:领养通知 comment:评论通知)',
    `related_id` BIGINT DEFAULT NULL COMMENT '关联ID（如申请ID、宠物ID等）',
    `is_read` TINYINT(1) DEFAULT 0 COMMENT '是否已读 (0:未读 1:已读)',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除 (0:未删除 1:已删除)',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_is_read` (`is_read`),
    KEY `idx_create_time` (`create_time`),
    CONSTRAINT `fk_notification_user` FOREIGN KEY (`user_id`) REFERENCES `t_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='通知消息表';

-- ============================================
-- 初始化数据
-- ============================================

-- 插入超级管理员账户 (密码: admin123 需要加密)
INSERT INTO `t_user` (`username`, `password`, `nickname`, `role`, `status`, `certified`) 
VALUES ('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '超级管理员', 'super_admin', 1, 1);

-- 插入管家账户 (密码: housekeeper123)
INSERT INTO `t_user` (`username`, `password`, `nickname`, `role`, `status`, `certified`) 
VALUES ('housekeeper', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '系统管家', 'housekeeper', 1, 1);

-- 插入审核员账户 (密码: auditor123)
INSERT INTO `t_user` (`username`, `password`, `nickname`, `role`, `status`, `certified`) 
VALUES ('auditor', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '审核员', 'application_auditor', 1, 1);

-- 插入测试用户 (密码: user123)
INSERT INTO `t_user` (`username`, `password`, `nickname`, `phone`, `email`, `role`, `status`) 
VALUES ('testuser', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '测试用户', '13800138000', 'test@example.com', 'user', 1);

-- 插入示例宠物数据
INSERT INTO `t_pet` (`name`, `category`, `breed`, `gender`, `age`, `weight`, `color`, `neutered`, `vaccinated`, 
    `health_status`, `personality`, `description`, `adoption_status`, `shelf_status`, `create_by`) 
VALUES 
('小白', 'cat', '中华田园猫', 2, 6, 3.50, '白色', 1, 1, 'healthy', '性格温顺，喜欢撒娇', '一只可爱的小白猫，非常亲人', 'available', 1, 1),
('大黄', 'dog', '中华田园犬', 1, 12, 15.00, '黄色', 1, 1, 'healthy', '忠诚勇敢，看家护院', '健康活泼的狗狗，适合家庭饲养', 'available', 1, 1),
('小灰', 'rabbit', '垂耳兔', 2, 4, 1.80, '灰色', 0, 1, 'healthy', '温柔安静，适合陪伴', '可爱的小兔子，很适合小朋友', 'available', 1, 1);

-- 插入系统配置
INSERT INTO `t_system_config` (`config_key`, `config_value`, `config_desc`, `config_type`) 
VALUES 
('system.name', '宠物领养管理系统', '系统名称', 'string'),
('adoption.auto_approve', 'false', '是否自动审核通过领养申请', 'boolean'),
('upload.max_size', '10485760', '文件上传最大大小（字节）', 'number'),
('ai.enabled', 'true', 'AI功能是否启用', 'boolean');

-- 插入示例文章
INSERT INTO `t_article` (`title`, `category`, `summary`, `content`, `author`, `status`, `publish_time`, `create_by`) 
VALUES 
('新手养猫指南', 'guide', '给新手铲屎官的养猫建议', '养猫前需要准备的物品：猫粮、猫砂、猫砂盆、水碗、猫抓板等...', '系统管理员', 1, NOW(), 1),
('我和小白的故事', 'story', '一个温暖的领养故事', '那天我在救助站遇见了小白，它用那双清澈的眼睛看着我...', '爱心用户', 1, NOW(), 1);

-- ============================================
-- 创建视图
-- ============================================

-- 领养申请详情视图
CREATE OR REPLACE VIEW `v_adoption_application_detail` AS
SELECT 
    a.id,
    a.application_no,
    a.user_id,
    u.username,
    u.nickname AS user_nickname,
    u.phone AS user_phone,
    a.pet_id,
    p.name AS pet_name,
    p.category AS pet_category,
    p.breed AS pet_breed,
    a.reason,
    a.family_info,
    a.careplan,
    a.additional_info,
    a.contact_phone,
    a.contact_address,
    a.status,
    a.reviewer_id,
    r.username AS reviewer_username,
    r.nickname AS reviewer_nickname,
    a.review_time,
    a.review_comment,
    a.create_time,
    a.update_time
FROM t_adoption_application a
LEFT JOIN t_user u ON a.user_id = u.id
LEFT JOIN t_pet p ON a.pet_id = p.id
LEFT JOIN t_user r ON a.reviewer_id = r.id
WHERE a.deleted = 0;

-- 宠物统计视图
CREATE OR REPLACE VIEW `v_pet_statistics` AS
SELECT 
    category,
    COUNT(*) AS total_count,
    SUM(CASE WHEN adoption_status = 'available' THEN 1 ELSE 0 END) AS available_count,
    SUM(CASE WHEN adoption_status = 'adopted' THEN 1 ELSE 0 END) AS adopted_count,
    SUM(view_count) AS total_views,
    SUM(favorite_count) AS total_favorites
FROM t_pet
WHERE deleted = 0 AND shelf_status = 1
GROUP BY category;

-- ============================================
-- 创建存储过程
-- ============================================

DELIMITER $$

-- 生成领养申请编号
DROP PROCEDURE IF EXISTS `generate_application_no`$$
CREATE PROCEDURE `generate_application_no`(OUT app_no VARCHAR(50))
BEGIN
    DECLARE current_date VARCHAR(8);
    DECLARE seq_num INT;
    
    SET current_date = DATE_FORMAT(NOW(), '%Y%m%d');
    
    SELECT IFNULL(MAX(CAST(SUBSTRING(application_no, 10) AS UNSIGNED)), 0) + 1 
    INTO seq_num
    FROM t_adoption_application
    WHERE application_no LIKE CONCAT('AP', current_date, '%');
    
    SET app_no = CONCAT('AP', current_date, LPAD(seq_num, 4, '0'));
END$$

DELIMITER ;

-- ============================================
-- 创建触发器
-- ============================================

DELIMITER $$

-- 宠物浏览次数更新触发器（实际使用中可通过应用层控制）
-- 收藏表插入时更新宠物收藏次数
DROP TRIGGER IF EXISTS `tr_favorite_insert`$$
CREATE TRIGGER `tr_favorite_insert` AFTER INSERT ON `t_favorite`
FOR EACH ROW
BEGIN
    UPDATE t_pet SET favorite_count = favorite_count + 1 WHERE id = NEW.pet_id;
END$$

-- 收藏表删除时更新宠物收藏次数
DROP TRIGGER IF EXISTS `tr_favorite_delete`$$
CREATE TRIGGER `tr_favorite_delete` AFTER UPDATE ON `t_favorite`
FOR EACH ROW
BEGIN
    IF NEW.deleted = 1 AND OLD.deleted = 0 THEN
        UPDATE t_pet SET favorite_count = favorite_count - 1 WHERE id = OLD.pet_id AND favorite_count > 0;
    END IF;
END$$

-- 申请表插入时更新宠物申请次数
DROP TRIGGER IF EXISTS `tr_application_insert`$$
CREATE TRIGGER `tr_application_insert` AFTER INSERT ON `t_adoption_application`
FOR EACH ROW
BEGIN
    UPDATE t_pet SET application_count = application_count + 1 WHERE id = NEW.pet_id;
END$$

DELIMITER ;

-- ============================================
-- 创建索引（补充性能优化索引）
-- ============================================

-- 复合索引优化查询
CREATE INDEX `idx_pet_status_category` ON `t_pet` (`adoption_status`, `category`, `shelf_status`);
CREATE INDEX `idx_application_user_status` ON `t_adoption_application` (`user_id`, `status`);
CREATE INDEX `idx_application_pet_status` ON `t_adoption_application` (`pet_id`, `status`);
CREATE INDEX `idx_chat_session_user_type` ON `t_chat_session` (`user_id`, `session_type`, `status`);

-- ============================================
-- 数据库权限配置建议
-- ============================================
-- 生产环境建议：
-- 1. 创建专用数据库用户，不使用root
-- 2. 限制数据库用户权限（SELECT, INSERT, UPDATE, DELETE）
-- 3. 定期备份数据库
-- 4. 开启慢查询日志监控性能
-- 5. 配置主从复制提高可用性

-- ============================================
-- 完成
-- ============================================

