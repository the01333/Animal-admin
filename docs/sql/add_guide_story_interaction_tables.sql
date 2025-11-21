-- 创建指南点赞记录表
CREATE TABLE IF NOT EXISTS `t_guide_like` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `guide_id` bigint NOT NULL COMMENT '指南ID',
  `created_at` timestamp DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_guide` (`user_id`, `guide_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_guide_id` (`guide_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='指南点赞记录表';

-- 创建指南浏览记录表
CREATE TABLE IF NOT EXISTS `t_guide_view` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint COMMENT '用户ID（可为空，支持游客）',
  `guide_id` bigint NOT NULL COMMENT '指南ID',
  `ip_address` varchar(50) COMMENT 'IP地址',
  `created_at` timestamp DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_guide_id` (`guide_id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='指南浏览记录表';

-- 创建故事点赞记录表
CREATE TABLE IF NOT EXISTS `t_story_like` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `story_id` bigint NOT NULL COMMENT '故事ID',
  `created_at` timestamp DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_story` (`user_id`, `story_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_story_id` (`story_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='故事点赞记录表';

-- 修改指南表，移除views字段，改为通过查询t_guide_view表计算
-- ALTER TABLE `t_guide` DROP COLUMN `views`;

-- 修改故事表，移除likes字段，改为通过查询t_story_like表计算
-- ALTER TABLE `t_story` DROP COLUMN `likes`;
