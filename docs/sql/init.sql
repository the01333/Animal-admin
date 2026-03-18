/*
 Navicat Premium Dump SQL

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80034 (8.0.34)
 Source Host           : localhost:3306
 Source Schema         : animal_adopt_v2

 Target Server Type    : MySQL
 Target Server Version : 80034 (8.0.34)
 File Encoding         : 65001

 Date: 09/02/2026 22:04:14
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_adoption_application
-- ----------------------------
DROP TABLE IF EXISTS `t_adoption_application`;
CREATE TABLE `t_adoption_application`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `application_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_id` bigint NOT NULL,
  `pet_id` bigint NOT NULL,
  `reason` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `family_info` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `careplan` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `additional_info` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `contact_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `contact_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `status` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'pending',
  `reviewer_id` bigint NULL DEFAULT NULL,
  `review_time` datetime NULL DEFAULT NULL,
  `review_comment` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_application_no`(`application_no` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_pet_id`(`pet_id` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '领养申请表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_adoption_application
-- ----------------------------
INSERT INTO `t_adoption_application` VALUES (1, 'AP202511275167', 1, 20, '我喜欢可爱的小狗', '1. 川川 (21岁)', '居住类型：宿舍；住房面积：10㎡；无院子；暂无养宠经验；宠物知识：暂无', '', '13000000000', '中国福建省漳州市xx县', 'approved', 1, '2025-11-27 19:50:25', '通过了 要好好对待耶耶', '2025-11-27 19:33:13', '2025-11-27 19:33:13', 0);
INSERT INTO `t_adoption_application` VALUES (2, 'AP20251127015A', 1, 11, '我已经领养了一只小狗了 还想养一只小猫', '1. 川川 (21岁)', '居住类型：宿舍；住房面积：30㎡；无院子；有养宠经验；宠物知识：暂无', '一只萨摩耶', '13000000000', '中国福建省漳州市xx县', 'rejected', 1, '2025-11-28 21:56:32', '最近橘子打疫苗 需要静养几天后再来', '2025-11-27 20:08:17', '2025-11-27 20:08:17', 0);
INSERT INTO `t_adoption_application` VALUES (3, 'AP202511283CD7', 1, 11, '我已经养了一只小狗了 还想养一只小猫', '1. 林憨憨 (21岁)', '居住类型：宿舍；住房面积：30㎡；无院子；有养宠经验；宠物知识：暂无了解', '萨摩耶', '13000000000', '中国福建省漳州市xx县', 'cancelled', NULL, NULL, NULL, '2025-11-28 21:59:17', '2025-11-28 21:59:17', 0);
INSERT INTO `t_adoption_application` VALUES (4, 'AP20251205719A', 4, 22, '喜欢可爱小猫', '1. test2 (25岁)', '居住类型：公寓；住房面积：20㎡；无院子；暂无养宠经验；宠物知识：暂无', '', '13063184972', '中国福建省', 'cancelled', NULL, NULL, NULL, '2025-12-05 20:55:56', '2025-12-05 20:55:56', 0);
INSERT INTO `t_adoption_application` VALUES (5, 'AP20251205E8AD', 4, 22, '我喜欢可爱小猫', '1. test2 (25岁)', '居住类型：公寓；住房面积：20㎡；无院子；暂无养宠经验；宠物知识：暂无', '', '13063184972', '中国福建省', 'rejected', 1, '2025-12-06 22:00:11', '改天再来 最近店面暂时关门', '2025-12-05 21:42:25', '2025-12-05 21:42:25', 0);
INSERT INTO `t_adoption_application` VALUES (6, 'AP202512063749', 4, 6, '我喜欢小狗', '1. test2 (25岁)', '居住类型：公寓；住房面积：20㎡；无院子；暂无养宠经验；宠物知识：暂无', '', '13063184972', '中国福建省', 'pending', NULL, NULL, NULL, '2025-12-06 21:59:28', '2025-12-06 21:59:28', 0);
INSERT INTO `t_adoption_application` VALUES (7, 'AP202601013BD6', 1, 26, '喜欢小动物', '1. test (21岁)', '居住类型：别墅/独栋；住房面积：60㎡；无院子；暂无养宠经验；宠物知识：无', '', '13000000000', '中国福建省漳州市xx县', 'pending', NULL, NULL, NULL, '2026-01-01 11:20:25', '2026-01-01 11:20:25', 0);
INSERT INTO `t_adoption_application` VALUES (8, 'AP20260128FFB4', 1, 33, '喜欢仓鼠', '1. 小林 (21岁)', '居住类型：别墅/独栋；住房面积：100㎡；有院子；暂无养宠经验；宠物知识：无', '', '13000000000', '中国福建省漳州市xx县', 'pending', NULL, NULL, NULL, '2026-01-28 21:50:39', '2026-01-28 21:50:39', 0);
INSERT INTO `t_adoption_application` VALUES (9, 'AP20260131F260', 29, 22, '喜欢小猫', '1. 小林222 (21岁)', '居住类型：公寓；住房面积：30㎡；无院子；暂无养宠经验；宠物知识：无', '', '13000000006', '中国福建省', 'approved', 1, '2026-01-31 22:11:04', '可以', '2026-01-31 22:10:33', '2026-01-31 22:10:33', 0);
INSERT INTO `t_adoption_application` VALUES (10, 'AP202602018ED4', 32, 23, '我比较喜欢小狗', '1. 小林同学 (21岁)', '居住类型：宿舍；住房面积：30㎡；无院子；暂无养宠经验；宠物知识：无', '', '13000000007', '中国福建省', 'approved', 1, '2026-02-01 21:03:42', '好好对待它！', '2026-02-01 21:02:51', '2026-02-01 21:02:51', 0);
INSERT INTO `t_adoption_application` VALUES (11, 'AP20260201395C', 33, 24, '无', '1. test (21岁)', '居住类型：公寓；住房面积：30㎡；无院子；暂无养宠经验；宠物知识：无', '', '13000000009', '中国', 'rejected', 1, '2026-02-01 22:39:34', '', '2026-02-01 22:21:17', '2026-02-01 22:21:17', 0);
INSERT INTO `t_adoption_application` VALUES (12, 'AP202602018871', 34, 24, '无', '1. 神秘人1 (21岁)', '居住类型：宿舍；住房面积：30㎡；无院子；暂无养宠经验；宠物知识：无', '', '13000000010', '中国', 'pending', NULL, NULL, NULL, '2026-02-01 22:39:54', '2026-02-01 22:39:54', 0);

-- ----------------------------
-- Table structure for t_chat_message
-- ----------------------------
DROP TABLE IF EXISTS `t_chat_message`;
CREATE TABLE `t_chat_message`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '消息主键ID',
  `session_id` bigint NOT NULL COMMENT '会话ID(关联会话表主键)',
  `sender_id` bigint NOT NULL COMMENT '发送者ID',
  `sender_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '发送者类型(旧字段, 兼容历史数据)',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '消息内容',
  `message_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'text' COMMENT '消息类型',
  `is_read` tinyint(1) NULL DEFAULT 0 COMMENT '是否已读',
  `receiver_id` bigint NULL DEFAULT NULL COMMENT '接收者ID',
  `read_time` datetime NULL DEFAULT NULL COMMENT '已读时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `deleted` tinyint NULL DEFAULT 0 COMMENT '逻辑删除 0-正常 1-删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_session_id`(`session_id` ASC) USING BTREE,
  INDEX `idx_sender_id`(`sender_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 424 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '聊天消息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_chat_message
-- ----------------------------
INSERT INTO `t_chat_message` VALUES (1, 1, 4, NULL, '你好', 'text', 0, NULL, NULL, '2025-12-08 21:56:05', 0);
INSERT INTO `t_chat_message` VALUES (2, 1, 1, NULL, '你好 请问有什么可以为您服务的', 'text', 0, 4, NULL, '2025-12-08 21:56:24', 0);
INSERT INTO `t_chat_message` VALUES (3, 1, 1, NULL, '你好啊', 'text', 0, 4, NULL, '2025-12-08 22:24:23', 0);
INSERT INTO `t_chat_message` VALUES (4, 1, 4, NULL, '我想咨询一下宠物的相关事项', 'text', 0, NULL, NULL, '2025-12-08 22:25:10', 0);
INSERT INTO `t_chat_message` VALUES (5, 1, 1, NULL, 'OK 喜欢什么宠物呢', 'text', 0, 4, NULL, '2025-12-08 22:26:15', 0);
INSERT INTO `t_chat_message` VALUES (6, 1, 4, NULL, '我喜欢小狗', 'text', 0, NULL, NULL, '2025-12-08 22:27:59', 0);
INSERT INTO `t_chat_message` VALUES (7, 1, 1, NULL, '好的', 'text', 0, 4, NULL, '2025-12-08 22:33:17', 0);
INSERT INTO `t_chat_message` VALUES (8, 1, 1, NULL, '之前有养过宠物吗？', 'text', 0, 4, NULL, '2025-12-08 22:34:58', 0);
INSERT INTO `t_chat_message` VALUES (9, 1, 4, NULL, '没有', 'text', 0, NULL, NULL, '2025-12-08 22:36:11', 0);
INSERT INTO `t_chat_message` VALUES (10, 1, 1, NULL, '好的 没关系', 'text', 0, 4, NULL, '2025-12-08 22:39:00', 0);
INSERT INTO `t_chat_message` VALUES (11, 1, 1, NULL, '那你希望狗狗的性格是怎么样的呢？\n比如：活泼一点、安静一点、可爱灵动一点呢？', 'text', 0, 4, NULL, '2025-12-08 22:39:55', 0);
INSERT INTO `t_chat_message` VALUES (12, 1, 4, NULL, '我喜欢活泼一点的', 'text', 0, NULL, NULL, '2025-12-08 22:51:04', 0);
INSERT INTO `t_chat_message` VALUES (13, 1, 1, NULL, '欧克欧克', 'text', 0, 4, NULL, '2025-12-08 22:51:12', 0);
INSERT INTO `t_chat_message` VALUES (14, 1, 1, NULL, '活泼的小狗很多，你可以自己去宠物列表看一下每只宠物的具体详情', 'text', 0, 4, NULL, '2025-12-08 22:59:17', 0);
INSERT INTO `t_chat_message` VALUES (15, 1, 1, NULL, '有合适的再来沟通', 'text', 0, 4, NULL, '2025-12-08 22:59:32', 0);
INSERT INTO `t_chat_message` VALUES (16, 1, 4, NULL, '欧克欧克', 'text', 0, NULL, NULL, '2025-12-08 23:43:58', 0);
INSERT INTO `t_chat_message` VALUES (17, 1, 4, NULL, '那我看看', 'text', 0, NULL, NULL, '2025-12-08 23:44:06', 0);
INSERT INTO `t_chat_message` VALUES (18, 1, 4, NULL, '我觉得耶耶不错', 'text', 0, NULL, NULL, '2025-12-08 23:44:34', 0);
INSERT INTO `t_chat_message` VALUES (19, 1, 1, NULL, '可以呀', 'text', 0, 4, NULL, '2025-12-08 23:51:04', 0);
INSERT INTO `t_chat_message` VALUES (20, 1, 1, NULL, '耶耶很可爱 也很活泼', 'text', 0, 4, NULL, '2025-12-08 23:51:45', 0);
INSERT INTO `t_chat_message` VALUES (21, 1, 4, NULL, '那太棒了 我先暂时选中这只了吧', 'text', 0, NULL, NULL, '2025-12-08 23:52:06', 0);
INSERT INTO `t_chat_message` VALUES (22, 1, 1, NULL, '可以呀 后续还有其他的想咨询的欢迎再来哈', 'text', 0, 4, NULL, '2025-12-09 11:24:40', 0);
INSERT INTO `t_chat_message` VALUES (23, 1, 1, NULL, '你好', 'text', 0, 4, NULL, '2025-12-10 17:20:31', 0);
INSERT INTO `t_chat_message` VALUES (24, 1, 1, NULL, '你好', 'text', 0, 4, NULL, '2025-12-10 17:42:41', 0);
INSERT INTO `t_chat_message` VALUES (25, 1, 1, NULL, '哈哈哈哈', 'text', 0, 4, NULL, '2025-12-10 17:42:46', 0);
INSERT INTO `t_chat_message` VALUES (26, 1, 1, NULL, '你好', 'text', 0, 4, NULL, '2025-12-10 17:43:06', 0);
INSERT INTO `t_chat_message` VALUES (27, 1, 1, NULL, '你好啊', 'text', 0, 4, NULL, '2025-12-10 17:43:39', 0);
INSERT INTO `t_chat_message` VALUES (28, 1, 1, NULL, '你好', 'text', 0, 4, NULL, '2025-12-10 17:44:25', 0);
INSERT INTO `t_chat_message` VALUES (29, 1, 1, NULL, '1111', 'text', 0, 4, NULL, '2025-12-10 18:32:03', 0);
INSERT INTO `t_chat_message` VALUES (30, 1, 1, NULL, 'nihao', 'text', 0, 4, NULL, '2025-12-10 18:33:22', 0);
INSERT INTO `t_chat_message` VALUES (31, 1, 1, NULL, '1111111', 'text', 0, 4, NULL, '2025-12-10 18:34:21', 0);
INSERT INTO `t_chat_message` VALUES (32, 1, 1, NULL, 'nsjaikjhsikqhjskqa', 'text', 0, 4, NULL, '2025-12-10 20:48:59', 0);
INSERT INTO `t_chat_message` VALUES (33, 1, 1, NULL, '哈哈哈哈哈', 'text', 0, 4, NULL, '2025-12-10 21:01:46', 0);
INSERT INTO `t_chat_message` VALUES (34, 1, 1, NULL, '11111', 'text', 0, 4, NULL, '2025-12-10 21:28:30', 0);
INSERT INTO `t_chat_message` VALUES (35, 1, 1, NULL, '1111', 'text', 0, 4, NULL, '2025-12-10 21:29:18', 0);
INSERT INTO `t_chat_message` VALUES (36, 1, 1, NULL, '11111111', 'text', 0, 4, NULL, '2025-12-10 21:29:59', 0);
INSERT INTO `t_chat_message` VALUES (37, 1, 1, NULL, '啦啦啦啦啦啦啦啦', 'text', 0, 4, NULL, '2025-12-10 21:41:29', 0);
INSERT INTO `t_chat_message` VALUES (38, 1, 1, NULL, '手机卡健身卡健身卡尽快·1', 'text', 0, 4, NULL, '2025-12-10 21:41:35', 0);
INSERT INTO `t_chat_message` VALUES (39, 1, 1, NULL, '你好', 'text', 0, 4, NULL, '2025-12-10 21:59:29', 0);
INSERT INTO `t_chat_message` VALUES (40, 1, 1, NULL, '你好呀', 'text', 0, 4, NULL, '2025-12-10 22:05:22', 0);
INSERT INTO `t_chat_message` VALUES (41, 1, 1, NULL, '花时间安徽省晋安河睡觉啊', 'text', 0, 4, NULL, '2025-12-10 22:18:25', 0);
INSERT INTO `t_chat_message` VALUES (42, 1, 1, NULL, '杀菌时间暗黑圣经', 'text', 0, 4, NULL, '2025-12-10 22:20:48', 0);
INSERT INTO `t_chat_message` VALUES (43, 1, 1, NULL, '高', 'text', 0, 4, NULL, '2025-12-10 22:27:54', 0);
INSERT INTO `t_chat_message` VALUES (44, 1, 1, NULL, '楚', 'text', 0, 4, NULL, '2025-12-10 22:27:59', 0);
INSERT INTO `t_chat_message` VALUES (45, 1, 1, NULL, '燕', 'text', 0, 4, NULL, '2025-12-10 22:28:02', 0);
INSERT INTO `t_chat_message` VALUES (46, 1, 1, NULL, '是', 'text', 0, 4, NULL, '2025-12-10 22:28:08', 0);
INSERT INTO `t_chat_message` VALUES (47, 1, 1, NULL, '🐷', 'text', 0, 4, NULL, '2025-12-10 22:28:20', 0);
INSERT INTO `t_chat_message` VALUES (48, 1, 1, NULL, '！', 'text', 0, 4, NULL, '2025-12-10 22:28:28', 0);
INSERT INTO `t_chat_message` VALUES (49, 1, 1, NULL, '！！', 'text', 0, 4, NULL, '2025-12-10 22:28:30', 0);
INSERT INTO `t_chat_message` VALUES (50, 1, 1, NULL, '！！！', 'text', 0, 4, NULL, '2025-12-10 22:28:31', 0);
INSERT INTO `t_chat_message` VALUES (51, 1, 1, NULL, '！！！！', 'text', 0, 4, NULL, '2025-12-10 22:28:34', 0);
INSERT INTO `t_chat_message` VALUES (52, 1, 1, NULL, '！！！！！', 'text', 0, 4, NULL, '2025-12-10 22:28:37', 0);
INSERT INTO `t_chat_message` VALUES (53, 1, 4, NULL, '我觉得你说得对', 'text', 0, NULL, NULL, '2025-12-10 22:29:52', 0);
INSERT INTO `t_chat_message` VALUES (54, 1, 4, NULL, '👍', 'text', 0, NULL, NULL, '2025-12-10 22:31:05', 0);
INSERT INTO `t_chat_message` VALUES (55, 1, 4, NULL, '哈哈哈', 'text', 0, NULL, NULL, '2025-12-10 22:44:27', 0);
INSERT INTO `t_chat_message` VALUES (56, 1, 4, NULL, '哈哈哈', 'text', 0, NULL, NULL, '2025-12-10 22:44:49', 0);
INSERT INTO `t_chat_message` VALUES (57, 1, 1, NULL, '好的，我们已为您记录需求，如有合适的宠物会第一时间联系您。', 'text', 0, 4, NULL, '2025-12-10 23:11:43', 0);
INSERT INTO `t_chat_message` VALUES (58, 3, 5, NULL, '我没有养过宠物，请问需要注意什么吗', 'text', 0, NULL, NULL, '2025-12-11 19:06:11', 0);
INSERT INTO `t_chat_message` VALUES (59, 3, 5, NULL, '你好', 'text', 0, NULL, NULL, '2025-12-11 19:06:34', 0);
INSERT INTO `t_chat_message` VALUES (60, 4, 6, NULL, '你好', 'text', 0, NULL, NULL, '2025-12-11 19:22:40', 0);
INSERT INTO `t_chat_message` VALUES (61, 5, 7, NULL, '你好', 'text', 0, 1, NULL, '2025-12-14 22:43:28', 0);
INSERT INTO `t_chat_message` VALUES (62, 5, 7, NULL, '你好', 'text', 0, 1, NULL, '2025-12-14 22:43:32', 0);
INSERT INTO `t_chat_message` VALUES (63, 5, 7, NULL, '1111', 'text', 0, 1, NULL, '2025-12-14 22:43:50', 0);
INSERT INTO `t_chat_message` VALUES (64, 5, 7, NULL, '哈哈哈', 'text', 0, 1, NULL, '2025-12-14 22:43:59', 0);
INSERT INTO `t_chat_message` VALUES (65, 5, 7, NULL, 'aaa', 'text', 0, 1, NULL, '2025-12-14 23:40:37', 0);
INSERT INTO `t_chat_message` VALUES (66, 5, 7, NULL, 'aaaaaaaq', 'text', 0, 1, NULL, '2025-12-14 23:42:01', 0);
INSERT INTO `t_chat_message` VALUES (67, 5, 7, NULL, '1111', 'text', 0, 1, NULL, '2025-12-14 23:43:17', 0);
INSERT INTO `t_chat_message` VALUES (68, 5, 7, NULL, '2222', 'text', 0, 1, NULL, '2025-12-14 23:43:38', 0);
INSERT INTO `t_chat_message` VALUES (69, 5, 1, NULL, '你好', 'text', 0, 7, NULL, '2025-12-15 15:26:55', 0);
INSERT INTO `t_chat_message` VALUES (70, 5, 7, NULL, '哈哈哈', 'text', 0, 1, NULL, '2025-12-15 15:28:05', 0);
INSERT INTO `t_chat_message` VALUES (71, 5, 1, NULL, '你好', 'text', 0, 7, NULL, '2025-12-15 20:29:25', 0);
INSERT INTO `t_chat_message` VALUES (72, 5, 1, NULL, '你好', 'text', 0, 7, NULL, '2025-12-15 20:30:06', 0);
INSERT INTO `t_chat_message` VALUES (73, 5, 1, NULL, '111', 'text', 0, 7, NULL, '2025-12-15 20:30:18', 0);
INSERT INTO `t_chat_message` VALUES (74, 5, 1, NULL, '111\naaa\n111', 'text', 0, 7, NULL, '2025-12-15 20:30:23', 0);
INSERT INTO `t_chat_message` VALUES (75, 5, 7, NULL, '你好', 'text', 0, 1, NULL, '2025-12-15 20:30:30', 0);
INSERT INTO `t_chat_message` VALUES (76, 5, 7, NULL, '111', 'text', 0, 1, NULL, '2025-12-15 20:32:01', 0);
INSERT INTO `t_chat_message` VALUES (77, 5, 7, NULL, '11', 'text', 0, 1, NULL, '2025-12-15 20:32:13', 0);
INSERT INTO `t_chat_message` VALUES (78, 5, 7, NULL, '1', 'text', 0, 1, NULL, '2025-12-15 20:32:13', 0);
INSERT INTO `t_chat_message` VALUES (79, 5, 7, NULL, '1', 'text', 0, 1, NULL, '2025-12-15 20:32:14', 0);
INSERT INTO `t_chat_message` VALUES (80, 5, 7, NULL, '1', 'text', 0, 1, NULL, '2025-12-15 20:32:14', 0);
INSERT INTO `t_chat_message` VALUES (81, 5, 7, NULL, '1', 'text', 0, 1, NULL, '2025-12-15 20:32:14', 0);
INSERT INTO `t_chat_message` VALUES (82, 5, 7, NULL, '1', 'text', 0, 1, NULL, '2025-12-15 20:32:17', 0);
INSERT INTO `t_chat_message` VALUES (83, 5, 1, NULL, '外网', 'text', 0, 7, NULL, '2025-12-15 20:32:46', 0);
INSERT INTO `t_chat_message` VALUES (84, 5, 1, NULL, '111', 'text', 0, 7, NULL, '2025-12-15 20:32:47', 0);
INSERT INTO `t_chat_message` VALUES (85, 5, 1, NULL, '1', 'text', 0, 7, NULL, '2025-12-15 20:32:53', 0);
INSERT INTO `t_chat_message` VALUES (86, 5, 1, NULL, '1', 'text', 0, 7, NULL, '2025-12-15 20:32:54', 0);
INSERT INTO `t_chat_message` VALUES (87, 5, 1, NULL, '1', 'text', 0, 7, NULL, '2025-12-15 20:32:56', 0);
INSERT INTO `t_chat_message` VALUES (88, 5, 1, NULL, '111111', 'text', 0, 7, NULL, '2025-12-15 20:33:03', 0);
INSERT INTO `t_chat_message` VALUES (89, 5, 1, NULL, '你好', 'text', 0, 7, NULL, '2025-12-15 21:14:34', 0);
INSERT INTO `t_chat_message` VALUES (90, 5, 1, NULL, '1111111', 'text', 0, 7, NULL, '2025-12-15 21:14:41', 0);
INSERT INTO `t_chat_message` VALUES (91, 4, 1, NULL, '111', 'text', 0, 6, NULL, '2025-12-15 21:14:51', 0);
INSERT INTO `t_chat_message` VALUES (92, 4, 1, NULL, '1111', 'text', 0, 6, NULL, '2025-12-15 21:15:21', 0);
INSERT INTO `t_chat_message` VALUES (93, 5, 1, NULL, '111111', 'text', 0, 7, NULL, '2025-12-15 21:15:42', 0);
INSERT INTO `t_chat_message` VALUES (94, 5, 1, NULL, '11', 'text', 0, 7, NULL, '2025-12-15 21:15:45', 0);
INSERT INTO `t_chat_message` VALUES (95, 5, 1, NULL, '111111', 'text', 0, 7, NULL, '2025-12-15 21:15:47', 0);
INSERT INTO `t_chat_message` VALUES (96, 5, 1, NULL, 'sasas', 'text', 0, 7, NULL, '2025-12-15 21:15:51', 0);
INSERT INTO `t_chat_message` VALUES (97, 5, 7, NULL, '111', 'text', 0, 1, NULL, '2025-12-15 21:16:39', 0);
INSERT INTO `t_chat_message` VALUES (98, 5, 7, NULL, '11111', 'text', 0, 1, NULL, '2025-12-15 21:16:43', 0);
INSERT INTO `t_chat_message` VALUES (99, 5, 7, NULL, '111', 'text', 0, 1, NULL, '2025-12-15 21:17:53', 0);
INSERT INTO `t_chat_message` VALUES (100, 5, 1, NULL, '你好', 'text', 0, 7, NULL, '2025-12-16 14:09:20', 0);
INSERT INTO `t_chat_message` VALUES (101, 5, 7, NULL, '哈哈哈哈', 'text', 0, 1, NULL, '2025-12-16 14:09:25', 0);
INSERT INTO `t_chat_message` VALUES (102, 5, 7, NULL, '1111', 'text', 0, 1, NULL, '2025-12-16 14:11:53', 0);
INSERT INTO `t_chat_message` VALUES (103, 5, 1, NULL, '你好', 'text', 0, 7, NULL, '2025-12-16 21:00:40', 0);
INSERT INTO `t_chat_message` VALUES (104, 5, 1, NULL, '11111', 'text', 0, 7, NULL, '2025-12-16 21:00:52', 0);
INSERT INTO `t_chat_message` VALUES (105, 5, 7, NULL, '111111', 'text', 0, 1, NULL, '2025-12-16 21:01:00', 0);
INSERT INTO `t_chat_message` VALUES (106, 5, 7, NULL, 'abc', 'text', 0, 1, NULL, '2025-12-16 21:01:20', 0);
INSERT INTO `t_chat_message` VALUES (107, 5, 7, NULL, '666', 'text', 0, 1, NULL, '2025-12-16 21:03:17', 0);
INSERT INTO `t_chat_message` VALUES (108, 5, 1, NULL, '121212', 'text', 0, 7, NULL, '2025-12-16 21:04:15', 0);
INSERT INTO `t_chat_message` VALUES (109, 5, 1, NULL, '12133131', 'text', 0, 7, NULL, '2025-12-16 21:04:32', 0);
INSERT INTO `t_chat_message` VALUES (110, 5, 7, NULL, '我真的是服了', 'text', 0, 1, NULL, '2025-12-16 21:05:48', 0);
INSERT INTO `t_chat_message` VALUES (111, 5, 7, NULL, '咋了', 'text', 0, 1, NULL, '2025-12-16 21:05:57', 0);
INSERT INTO `t_chat_message` VALUES (112, 5, 7, NULL, '777', 'text', 0, 1, NULL, '2025-12-16 21:06:01', 0);
INSERT INTO `t_chat_message` VALUES (113, 5, 1, NULL, '好的，我们已为您记录需求，如有合适的宠物会第一时间联系您。', 'text', 0, 7, NULL, '2025-12-16 21:09:15', 0);
INSERT INTO `t_chat_message` VALUES (114, 5, 7, NULL, '啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊', 'text', 0, 1, NULL, '2025-12-16 21:10:32', 0);
INSERT INTO `t_chat_message` VALUES (115, 5, 1, NULL, '你是咋了孩子', 'text', 0, 7, NULL, '2025-12-16 21:10:43', 0);
INSERT INTO `t_chat_message` VALUES (116, 5, 1, NULL, '没事吧', 'text', 0, 7, NULL, '2025-12-16 21:10:48', 0);
INSERT INTO `t_chat_message` VALUES (117, 5, 7, NULL, '1212121', 'text', 0, 1, NULL, '2025-12-16 21:20:42', 0);
INSERT INTO `t_chat_message` VALUES (118, 5, 7, NULL, '121212121', 'text', 0, 1, NULL, '2025-12-16 21:22:37', 0);
INSERT INTO `t_chat_message` VALUES (119, 5, 7, NULL, '你好', 'text', 0, 1, NULL, '2025-12-16 21:28:59', 0);
INSERT INTO `t_chat_message` VALUES (120, 5, 7, NULL, 'abc', 'text', 0, 1, NULL, '2025-12-17 11:47:04', 0);
INSERT INTO `t_chat_message` VALUES (121, 5, 7, NULL, '11111', 'text', 0, 1, NULL, '2025-12-19 11:56:07', 0);
INSERT INTO `t_chat_message` VALUES (122, 5, 1, NULL, '11111', 'text', 0, 7, NULL, '2025-12-19 12:02:58', 0);
INSERT INTO `t_chat_message` VALUES (123, 5, 1, NULL, '2222', 'text', 0, 7, NULL, '2025-12-19 12:03:20', 0);
INSERT INTO `t_chat_message` VALUES (124, 5, 7, NULL, '111', 'text', 0, 1, NULL, '2025-12-19 12:03:28', 0);
INSERT INTO `t_chat_message` VALUES (125, 5, 7, NULL, '1111', 'text', 0, 1, NULL, '2025-12-19 12:03:37', 0);
INSERT INTO `t_chat_message` VALUES (126, 5, 1, NULL, '111', 'text', 0, 7, NULL, '2025-12-19 12:11:36', 0);
INSERT INTO `t_chat_message` VALUES (127, 5, 7, NULL, '11111', 'text', 0, 1, NULL, '2025-12-19 12:11:42', 0);
INSERT INTO `t_chat_message` VALUES (128, 5, 7, NULL, '1222', 'text', 0, 1, NULL, '2025-12-19 12:11:50', 0);
INSERT INTO `t_chat_message` VALUES (129, 5, 1, NULL, '哈哈哈哈哈', 'text', 0, 7, NULL, '2025-12-19 12:15:49', 0);
INSERT INTO `t_chat_message` VALUES (130, 5, 1, NULL, '2121', 'text', 0, 7, NULL, '2025-12-19 12:15:53', 0);
INSERT INTO `t_chat_message` VALUES (131, 5, 1, NULL, '1111', 'text', 0, 7, NULL, '2025-12-19 12:23:09', 0);
INSERT INTO `t_chat_message` VALUES (132, 5, 7, NULL, '222', 'text', 0, 1, NULL, '2025-12-19 12:23:18', 0);
INSERT INTO `t_chat_message` VALUES (133, 5, 1, NULL, '12121', 'text', 0, 7, NULL, '2025-12-19 13:10:08', 0);
INSERT INTO `t_chat_message` VALUES (134, 5, 7, NULL, '1212121', 'text', 0, 1, NULL, '2025-12-19 13:10:13', 0);
INSERT INTO `t_chat_message` VALUES (135, 5, 7, NULL, '12121', 'text', 0, 1, NULL, '2025-12-19 13:10:17', 0);
INSERT INTO `t_chat_message` VALUES (136, 5, 7, NULL, '1221112121', 'text', 0, 1, NULL, '2025-12-19 13:10:22', 0);
INSERT INTO `t_chat_message` VALUES (137, 5, 7, NULL, '1', 'text', 0, 1, NULL, '2025-12-19 13:10:28', 0);
INSERT INTO `t_chat_message` VALUES (138, 5, 7, NULL, '测试', 'text', 0, 1, NULL, '2025-12-19 13:15:04', 0);
INSERT INTO `t_chat_message` VALUES (139, 5, 7, NULL, '是否有红点', 'text', 0, 1, NULL, '2025-12-19 13:15:15', 0);
INSERT INTO `t_chat_message` VALUES (140, 5, 7, NULL, '112', 'text', 0, 1, NULL, '2025-12-19 13:15:25', 0);
INSERT INTO `t_chat_message` VALUES (141, 5, 1, NULL, '111', 'text', 0, 7, NULL, '2025-12-19 13:15:58', 0);
INSERT INTO `t_chat_message` VALUES (142, 5, 7, NULL, '你好', 'text', 0, 1, NULL, '2025-12-19 13:16:01', 0);
INSERT INTO `t_chat_message` VALUES (143, 5, 1, NULL, '你好', 'text', 0, 7, NULL, '2025-12-19 13:17:37', 0);
INSERT INTO `t_chat_message` VALUES (144, 5, 7, NULL, '你好啊', 'text', 0, 1, NULL, '2025-12-19 13:17:42', 0);
INSERT INTO `t_chat_message` VALUES (145, 5, 7, NULL, '你好', 'text', 0, 1, NULL, '2025-12-19 13:19:18', 0);
INSERT INTO `t_chat_message` VALUES (146, 5, 1, NULL, '111', 'text', 0, 7, NULL, '2025-12-19 13:19:25', 0);
INSERT INTO `t_chat_message` VALUES (147, 5, 1, NULL, '222', 'text', 0, 7, NULL, '2025-12-19 13:19:29', 0);
INSERT INTO `t_chat_message` VALUES (148, 5, 1, NULL, '333', 'text', 0, 7, NULL, '2025-12-19 13:19:32', 0);
INSERT INTO `t_chat_message` VALUES (149, 5, 7, NULL, '44', 'text', 0, 1, NULL, '2025-12-19 13:19:38', 0);
INSERT INTO `t_chat_message` VALUES (150, 5, 7, NULL, '你好', 'text', 0, 1, NULL, '2025-12-19 13:22:24', 0);
INSERT INTO `t_chat_message` VALUES (151, 5, 7, NULL, '11', 'text', 0, 1, NULL, '2025-12-19 13:22:36', 0);
INSERT INTO `t_chat_message` VALUES (152, 5, 7, NULL, '1', 'text', 0, 1, NULL, '2025-12-19 13:22:42', 0);
INSERT INTO `t_chat_message` VALUES (153, 5, 7, NULL, '123', 'text', 0, 1, NULL, '2025-12-19 13:22:46', 0);
INSERT INTO `t_chat_message` VALUES (154, 5, 1, NULL, '123', 'text', 0, 7, NULL, '2025-12-19 13:23:36', 0);
INSERT INTO `t_chat_message` VALUES (155, 5, 7, NULL, '哈哈哈', 'text', 0, 1, NULL, '2025-12-19 13:23:39', 0);
INSERT INTO `t_chat_message` VALUES (156, 5, 7, NULL, '哈哈哈', 'text', 0, 1, NULL, '2025-12-19 13:23:43', 0);
INSERT INTO `t_chat_message` VALUES (157, 5, 7, NULL, '1212', 'text', 0, 1, NULL, '2025-12-19 13:23:47', 0);
INSERT INTO `t_chat_message` VALUES (158, 5, 1, NULL, '有短语是', 'text', 0, 7, NULL, '2025-12-19 13:23:57', 0);
INSERT INTO `t_chat_message` VALUES (159, 5, 7, NULL, '1212', 'text', 0, 1, NULL, '2025-12-19 13:23:59', 0);
INSERT INTO `t_chat_message` VALUES (160, 5, 7, NULL, '111', 'text', 0, 1, NULL, '2025-12-19 13:24:02', 0);
INSERT INTO `t_chat_message` VALUES (161, 5, 7, NULL, '121', 'text', 0, 1, NULL, '2025-12-19 13:24:03', 0);
INSERT INTO `t_chat_message` VALUES (162, 5, 7, NULL, '22', 'text', 0, 1, NULL, '2025-12-19 13:24:04', 0);
INSERT INTO `t_chat_message` VALUES (163, 5, 7, NULL, '33', 'text', 0, 1, NULL, '2025-12-19 13:24:05', 0);
INSERT INTO `t_chat_message` VALUES (164, 5, 7, NULL, '44', 'text', 0, 1, NULL, '2025-12-19 13:24:06', 0);
INSERT INTO `t_chat_message` VALUES (165, 5, 7, NULL, '121', 'text', 0, 1, NULL, '2025-12-19 13:24:12', 0);
INSERT INTO `t_chat_message` VALUES (166, 5, 7, NULL, '111', 'text', 0, 1, NULL, '2025-12-19 13:26:27', 0);
INSERT INTO `t_chat_message` VALUES (167, 5, 7, NULL, '1111', 'text', 0, 1, NULL, '2025-12-19 13:26:39', 0);
INSERT INTO `t_chat_message` VALUES (168, 5, 7, NULL, '嗯嗯', 'text', 0, 1, NULL, '2025-12-19 13:27:07', 0);
INSERT INTO `t_chat_message` VALUES (169, 5, 7, NULL, '22', 'text', 0, 1, NULL, '2025-12-19 13:29:16', 0);
INSERT INTO `t_chat_message` VALUES (170, 5, 7, NULL, '33', 'text', 0, 1, NULL, '2025-12-19 13:29:26', 0);
INSERT INTO `t_chat_message` VALUES (171, 5, 7, NULL, '44', 'text', 0, 1, NULL, '2025-12-19 13:29:33', 0);
INSERT INTO `t_chat_message` VALUES (172, 5, 7, NULL, '11', 'text', 0, 1, NULL, '2025-12-19 13:29:48', 0);
INSERT INTO `t_chat_message` VALUES (173, 5, 7, NULL, '222', 'text', 0, 1, NULL, '2025-12-19 13:30:35', 0);
INSERT INTO `t_chat_message` VALUES (174, 5, 7, NULL, '121', 'text', 0, 1, NULL, '2025-12-19 13:30:48', 0);
INSERT INTO `t_chat_message` VALUES (175, 5, 1, NULL, '111', 'text', 0, 7, NULL, '2025-12-19 13:31:11', 0);
INSERT INTO `t_chat_message` VALUES (176, 5, 1, NULL, '123', 'text', 0, 7, NULL, '2025-12-19 13:32:23', 0);
INSERT INTO `t_chat_message` VALUES (177, 5, 7, NULL, '1234', 'text', 0, 1, NULL, '2025-12-19 13:34:04', 0);
INSERT INTO `t_chat_message` VALUES (178, 5, 7, NULL, '111', 'text', 0, 1, NULL, '2025-12-19 13:34:16', 0);
INSERT INTO `t_chat_message` VALUES (179, 5, 1, NULL, '111', 'text', 0, 7, NULL, '2025-12-19 23:04:42', 0);
INSERT INTO `t_chat_message` VALUES (180, 5, 7, NULL, '22', 'text', 0, 1, NULL, '2025-12-19 23:04:49', 0);
INSERT INTO `t_chat_message` VALUES (181, 5, 7, NULL, '111', 'text', 0, 1, NULL, '2025-12-19 23:05:15', 0);
INSERT INTO `t_chat_message` VALUES (182, 5, 1, NULL, '111', 'text', 0, 7, NULL, '2025-12-19 23:47:06', 0);
INSERT INTO `t_chat_message` VALUES (183, 5, 7, NULL, '222', 'text', 0, 1, NULL, '2025-12-19 23:47:10', 0);
INSERT INTO `t_chat_message` VALUES (184, 5, 1, NULL, '111', 'text', 0, 7, NULL, '2025-12-20 11:30:43', 0);
INSERT INTO `t_chat_message` VALUES (185, 5, 7, NULL, '111', 'text', 0, 1, NULL, '2025-12-20 11:30:47', 0);
INSERT INTO `t_chat_message` VALUES (186, 5, 7, NULL, '2222', 'text', 0, 1, NULL, '2025-12-20 11:31:10', 0);
INSERT INTO `t_chat_message` VALUES (187, 5, 1, NULL, '111', 'text', 0, 7, NULL, '2025-12-20 11:35:26', 0);
INSERT INTO `t_chat_message` VALUES (188, 5, 7, NULL, '1', 'text', 0, 1, NULL, '2025-12-20 11:35:31', 0);
INSERT INTO `t_chat_message` VALUES (189, 5, 7, NULL, '2', 'text', 0, 1, NULL, '2025-12-20 11:35:34', 0);
INSERT INTO `t_chat_message` VALUES (190, 5, 1, NULL, '1', 'text', 0, 7, NULL, '2025-12-20 12:49:26', 0);
INSERT INTO `t_chat_message` VALUES (191, 5, 7, NULL, '111', 'text', 0, 1, NULL, '2025-12-20 12:49:29', 0);
INSERT INTO `t_chat_message` VALUES (192, 5, 7, NULL, '111', 'text', 0, 1, NULL, '2025-12-20 12:49:51', 0);
INSERT INTO `t_chat_message` VALUES (193, 5, 7, NULL, '121213', 'text', 0, 1, NULL, '2025-12-20 12:51:02', 0);
INSERT INTO `t_chat_message` VALUES (194, 5, 7, NULL, '111', 'text', 0, 1, NULL, '2025-12-20 12:55:02', 0);
INSERT INTO `t_chat_message` VALUES (195, 5, 7, NULL, '111', 'text', 0, 1, NULL, '2025-12-20 12:55:49', 0);
INSERT INTO `t_chat_message` VALUES (196, 5, 1, NULL, '111', 'text', 0, 7, NULL, '2025-12-20 12:58:51', 0);
INSERT INTO `t_chat_message` VALUES (197, 5, 7, NULL, '222', 'text', 0, 1, NULL, '2025-12-20 12:58:55', 0);
INSERT INTO `t_chat_message` VALUES (198, 5, 7, NULL, '1212121', 'text', 0, 1, NULL, '2025-12-20 12:58:57', 0);
INSERT INTO `t_chat_message` VALUES (199, 5, 1, NULL, '2121', 'text', 0, 7, NULL, '2025-12-20 12:59:00', 0);
INSERT INTO `t_chat_message` VALUES (200, 5, 7, NULL, '1212', 'text', 0, 1, NULL, '2025-12-20 13:04:59', 0);
INSERT INTO `t_chat_message` VALUES (201, 5, 7, NULL, '212121', 'text', 0, 1, NULL, '2025-12-20 13:05:04', 0);
INSERT INTO `t_chat_message` VALUES (202, 5, 1, NULL, '111', 'text', 0, 7, NULL, '2025-12-20 13:05:27', 0);
INSERT INTO `t_chat_message` VALUES (203, 5, 7, NULL, '1212121', 'text', 0, 1, NULL, '2025-12-20 13:05:29', 0);
INSERT INTO `t_chat_message` VALUES (204, 5, 7, NULL, '111', 'text', 0, 1, NULL, '2025-12-20 13:07:35', 0);
INSERT INTO `t_chat_message` VALUES (205, 5, 7, NULL, '12121', 'text', 0, 1, NULL, '2025-12-20 13:07:44', 0);
INSERT INTO `t_chat_message` VALUES (206, 5, 7, NULL, '1212121', 'text', 0, 1, NULL, '2025-12-20 13:08:00', 0);
INSERT INTO `t_chat_message` VALUES (207, 5, 7, NULL, '12121', 'text', 0, 1, NULL, '2025-12-20 13:08:17', 0);
INSERT INTO `t_chat_message` VALUES (208, 5, 1, NULL, '211', 'text', 0, 7, NULL, '2025-12-20 13:08:25', 0);
INSERT INTO `t_chat_message` VALUES (209, 5, 1, NULL, '111', 'text', 0, 7, NULL, '2025-12-20 13:09:54', 0);
INSERT INTO `t_chat_message` VALUES (210, 5, 7, NULL, '222', 'text', 0, 1, NULL, '2025-12-20 13:09:57', 0);
INSERT INTO `t_chat_message` VALUES (211, 5, 7, NULL, '1111', 'text', 0, 1, NULL, '2025-12-20 13:11:45', 0);
INSERT INTO `t_chat_message` VALUES (212, 5, 7, NULL, '111', 'text', 0, 1, NULL, '2025-12-20 13:16:49', 0);
INSERT INTO `t_chat_message` VALUES (213, 5, 7, NULL, '111', 'text', 0, 1, NULL, '2025-12-20 13:19:22', 0);
INSERT INTO `t_chat_message` VALUES (214, 5, 7, NULL, '111', 'text', 0, 1, NULL, '2025-12-20 13:23:45', 0);
INSERT INTO `t_chat_message` VALUES (215, 5, 7, NULL, '121', 'text', 0, 1, NULL, '2025-12-20 13:28:08', 0);
INSERT INTO `t_chat_message` VALUES (216, 5, 1, NULL, '11', 'text', 0, 7, NULL, '2025-12-20 13:28:14', 0);
INSERT INTO `t_chat_message` VALUES (217, 5, 7, NULL, '111', 'text', 0, 1, NULL, '2025-12-20 13:28:33', 0);
INSERT INTO `t_chat_message` VALUES (218, 5, 7, NULL, '111', 'text', 0, 1, NULL, '2025-12-20 13:28:37', 0);
INSERT INTO `t_chat_message` VALUES (219, 5, 7, NULL, '111', 'text', 0, 1, NULL, '2025-12-20 13:29:23', 0);
INSERT INTO `t_chat_message` VALUES (220, 5, 1, NULL, '111', 'text', 0, 7, NULL, '2025-12-20 13:29:26', 0);
INSERT INTO `t_chat_message` VALUES (221, 5, 7, NULL, '你好啊', 'text', 0, 1, NULL, '2025-12-20 13:32:49', 0);
INSERT INTO `t_chat_message` VALUES (222, 5, 7, NULL, '你好', 'text', 0, 1, NULL, '2025-12-20 13:55:15', 0);
INSERT INTO `t_chat_message` VALUES (223, 5, 7, NULL, '21111', 'text', 0, 1, NULL, '2025-12-20 13:55:19', 0);
INSERT INTO `t_chat_message` VALUES (224, 5, 7, NULL, '1111', 'text', 0, 1, NULL, '2025-12-20 13:56:01', 0);
INSERT INTO `t_chat_message` VALUES (225, 5, 1, NULL, '1111', 'text', 0, 7, NULL, '2025-12-20 13:56:04', 0);
INSERT INTO `t_chat_message` VALUES (226, 5, 7, NULL, '好的', 'text', 0, 1, NULL, '2025-12-20 13:56:08', 0);
INSERT INTO `t_chat_message` VALUES (227, 5, 7, NULL, '好的', 'text', 0, 1, NULL, '2025-12-20 13:56:15', 0);
INSERT INTO `t_chat_message` VALUES (228, 5, 1, NULL, '11111', 'text', 0, 7, NULL, '2025-12-20 13:57:47', 0);
INSERT INTO `t_chat_message` VALUES (229, 5, 7, NULL, '111', 'text', 0, 1, NULL, '2025-12-20 13:58:05', 0);
INSERT INTO `t_chat_message` VALUES (230, 5, 7, NULL, '12121', 'text', 0, 1, NULL, '2025-12-20 13:58:09', 0);
INSERT INTO `t_chat_message` VALUES (231, 5, 7, NULL, '212121', 'text', 0, 1, NULL, '2025-12-20 13:58:31', 0);
INSERT INTO `t_chat_message` VALUES (232, 5, 7, NULL, '12121', 'text', 0, 1, NULL, '2025-12-20 14:03:21', 0);
INSERT INTO `t_chat_message` VALUES (233, 5, 1, NULL, '111', 'text', 0, 7, NULL, '2025-12-20 14:04:46', 0);
INSERT INTO `t_chat_message` VALUES (234, 5, 7, NULL, '11', 'text', 0, 1, NULL, '2025-12-20 14:04:48', 0);
INSERT INTO `t_chat_message` VALUES (235, 5, 1, NULL, '好', 'text', 0, 7, NULL, '2025-12-20 14:04:52', 0);
INSERT INTO `t_chat_message` VALUES (236, 5, 7, NULL, '嗯额', 'text', 0, 1, NULL, '2025-12-20 14:05:08', 0);
INSERT INTO `t_chat_message` VALUES (237, 5, 7, NULL, '嗯额', 'text', 0, 1, NULL, '2025-12-20 14:05:30', 0);
INSERT INTO `t_chat_message` VALUES (238, 5, 7, NULL, '111', 'text', 0, 1, NULL, '2025-12-20 14:06:24', 0);
INSERT INTO `t_chat_message` VALUES (239, 5, 7, NULL, '111', 'text', 0, 1, NULL, '2025-12-20 14:06:39', 0);
INSERT INTO `t_chat_message` VALUES (240, 5, 7, NULL, '111', 'text', 0, 1, NULL, '2025-12-20 14:07:08', 0);
INSERT INTO `t_chat_message` VALUES (241, 5, 1, NULL, '11111', 'text', 0, 7, NULL, '2025-12-20 14:07:53', 0);
INSERT INTO `t_chat_message` VALUES (242, 5, 7, NULL, '1111', 'text', 0, 1, NULL, '2025-12-20 14:07:55', 0);
INSERT INTO `t_chat_message` VALUES (243, 5, 7, NULL, '21313121', 'text', 0, 1, NULL, '2025-12-20 14:08:04', 0);
INSERT INTO `t_chat_message` VALUES (244, 5, 7, NULL, '1111', 'text', 0, 1, NULL, '2025-12-20 14:11:00', 0);
INSERT INTO `t_chat_message` VALUES (245, 5, 7, NULL, '11111', 'text', 0, 1, NULL, '2025-12-20 14:11:22', 0);
INSERT INTO `t_chat_message` VALUES (246, 5, 7, NULL, '11111212122', 'text', 0, 1, NULL, '2025-12-20 14:11:28', 0);
INSERT INTO `t_chat_message` VALUES (247, 5, 1, NULL, '212121', 'text', 0, 7, NULL, '2025-12-20 14:11:35', 0);
INSERT INTO `t_chat_message` VALUES (248, 5, 7, NULL, '好啊', 'text', 0, 1, NULL, '2025-12-20 14:11:37', 0);
INSERT INTO `t_chat_message` VALUES (249, 5, 7, NULL, '嗯呢', 'text', 0, 1, NULL, '2025-12-20 14:12:43', 0);
INSERT INTO `t_chat_message` VALUES (250, 5, 1, NULL, '好呀', 'text', 0, 7, NULL, '2025-12-20 14:12:51', 0);
INSERT INTO `t_chat_message` VALUES (251, 5, 7, NULL, '你好', 'text', 0, 1, NULL, '2025-12-20 14:12:55', 0);
INSERT INTO `t_chat_message` VALUES (252, 5, 1, NULL, '你好', 'text', 0, 7, NULL, '2025-12-20 14:15:17', 0);
INSERT INTO `t_chat_message` VALUES (253, 5, 7, NULL, '嗯嗯', 'text', 0, 1, NULL, '2025-12-20 14:15:22', 0);
INSERT INTO `t_chat_message` VALUES (254, 5, 7, NULL, '你好', 'text', 0, 1, NULL, '2025-12-20 14:16:58', 0);
INSERT INTO `t_chat_message` VALUES (255, 5, 1, NULL, '你好', 'text', 0, 7, NULL, '2025-12-20 15:08:01', 0);
INSERT INTO `t_chat_message` VALUES (256, 5, 7, NULL, '你好啊', 'text', 0, 1, NULL, '2025-12-20 15:08:13', 0);
INSERT INTO `t_chat_message` VALUES (257, 5, 7, NULL, '你好', 'text', 0, 1, NULL, '2025-12-20 15:08:21', 0);
INSERT INTO `t_chat_message` VALUES (258, 5, 1, NULL, '哈哈哈哈', 'text', 0, 7, NULL, '2025-12-20 15:30:08', 0);
INSERT INTO `t_chat_message` VALUES (259, 5, 7, NULL, '1', 'text', 0, 1, NULL, '2025-12-20 15:30:27', 0);
INSERT INTO `t_chat_message` VALUES (260, 5, 7, NULL, '2', 'text', 0, 1, NULL, '2025-12-20 15:42:23', 0);
INSERT INTO `t_chat_message` VALUES (261, 5, 1, NULL, '3', 'text', 0, 7, NULL, '2025-12-20 15:42:28', 0);
INSERT INTO `t_chat_message` VALUES (262, 5, 1, NULL, '11111', 'text', 0, 7, NULL, '2025-12-20 15:47:23', 0);
INSERT INTO `t_chat_message` VALUES (263, 5, 7, NULL, '2222', 'text', 0, 1, NULL, '2025-12-20 15:47:25', 0);
INSERT INTO `t_chat_message` VALUES (264, 5, 1, NULL, '你好', 'text', 0, 7, NULL, '2025-12-20 15:51:47', 0);
INSERT INTO `t_chat_message` VALUES (265, 5, 7, NULL, '你好', 'text', 0, 1, NULL, '2025-12-20 15:51:49', 0);
INSERT INTO `t_chat_message` VALUES (266, 5, 1, NULL, '1111', 'text', 0, 7, NULL, '2025-12-20 15:52:03', 0);
INSERT INTO `t_chat_message` VALUES (267, 5, 7, NULL, '222', 'text', 0, 1, NULL, '2025-12-20 15:52:06', 0);
INSERT INTO `t_chat_message` VALUES (268, 5, 1, NULL, '3', 'text', 0, 7, NULL, '2025-12-20 16:10:31', 0);
INSERT INTO `t_chat_message` VALUES (269, 5, 7, NULL, '4', 'text', 0, 1, NULL, '2025-12-20 16:10:34', 0);
INSERT INTO `t_chat_message` VALUES (270, 5, 7, NULL, '5', 'text', 0, 1, NULL, '2025-12-20 16:10:41', 0);
INSERT INTO `t_chat_message` VALUES (271, 5, 7, NULL, '6', 'text', 0, 1, NULL, '2025-12-20 16:10:41', 0);
INSERT INTO `t_chat_message` VALUES (272, 5, 7, NULL, '7', 'text', 0, 1, NULL, '2025-12-20 16:10:42', 0);
INSERT INTO `t_chat_message` VALUES (273, 5, 7, NULL, '8', 'text', 0, 1, NULL, '2025-12-20 16:10:42', 0);
INSERT INTO `t_chat_message` VALUES (274, 5, 7, NULL, '9', 'text', 0, 1, NULL, '2025-12-20 16:10:43', 0);
INSERT INTO `t_chat_message` VALUES (275, 5, 1, NULL, '1', 'text', 0, 7, NULL, '2025-12-20 16:19:38', 0);
INSERT INTO `t_chat_message` VALUES (276, 5, 7, NULL, '222', 'text', 0, 1, NULL, '2025-12-20 16:19:40', 0);
INSERT INTO `t_chat_message` VALUES (277, 5, 7, NULL, '3', 'text', 0, 1, NULL, '2025-12-20 16:19:42', 0);
INSERT INTO `t_chat_message` VALUES (278, 5, 7, NULL, '4', 'text', 0, 1, NULL, '2025-12-20 16:19:43', 0);
INSERT INTO `t_chat_message` VALUES (279, 5, 7, NULL, '45', 'text', 0, 1, NULL, '2025-12-20 16:19:43', 0);
INSERT INTO `t_chat_message` VALUES (280, 5, 7, NULL, '5', 'text', 0, 1, NULL, '2025-12-20 16:19:43', 0);
INSERT INTO `t_chat_message` VALUES (281, 5, 7, NULL, '56', 'text', 0, 1, NULL, '2025-12-20 16:19:43', 0);
INSERT INTO `t_chat_message` VALUES (282, 5, 7, NULL, '56', 'text', 0, 1, NULL, '2025-12-20 16:19:44', 0);
INSERT INTO `t_chat_message` VALUES (283, 5, 7, NULL, '576', 'text', 0, 1, NULL, '2025-12-20 16:19:44', 0);
INSERT INTO `t_chat_message` VALUES (284, 5, 7, NULL, '5', 'text', 0, 1, NULL, '2025-12-20 16:19:44', 0);
INSERT INTO `t_chat_message` VALUES (285, 5, 7, NULL, '765', 'text', 0, 1, NULL, '2025-12-20 16:19:44', 0);
INSERT INTO `t_chat_message` VALUES (286, 5, 7, NULL, '76', 'text', 0, 1, NULL, '2025-12-20 16:19:44', 0);
INSERT INTO `t_chat_message` VALUES (287, 5, 7, NULL, '57', 'text', 0, 1, NULL, '2025-12-20 16:19:44', 0);
INSERT INTO `t_chat_message` VALUES (288, 5, 7, NULL, '5', 'text', 0, 1, NULL, '2025-12-20 16:19:45', 0);
INSERT INTO `t_chat_message` VALUES (289, 5, 7, NULL, '7', 'text', 0, 1, NULL, '2025-12-20 16:19:46', 0);
INSERT INTO `t_chat_message` VALUES (290, 5, 1, NULL, 'okok别发了', 'text', 0, 7, NULL, '2025-12-20 16:19:51', 0);
INSERT INTO `t_chat_message` VALUES (291, 5, 7, NULL, '1', 'text', 0, 1, NULL, '2025-12-20 16:19:57', 0);
INSERT INTO `t_chat_message` VALUES (292, 5, 7, NULL, '1', 'text', 0, 1, NULL, '2025-12-20 16:29:09', 0);
INSERT INTO `t_chat_message` VALUES (293, 5, 1, NULL, '2', 'text', 0, 7, NULL, '2025-12-20 16:29:11', 0);
INSERT INTO `t_chat_message` VALUES (294, 5, 1, NULL, '11', 'text', 0, 7, NULL, '2025-12-20 16:29:16', 0);
INSERT INTO `t_chat_message` VALUES (295, 5, 7, NULL, '111', 'text', 0, 1, NULL, '2025-12-20 16:29:21', 0);
INSERT INTO `t_chat_message` VALUES (296, 5, 7, NULL, '222', 'text', 0, 1, NULL, '2025-12-20 16:29:22', 0);
INSERT INTO `t_chat_message` VALUES (297, 5, 7, NULL, '3333', 'text', 0, 1, NULL, '2025-12-20 16:29:23', 0);
INSERT INTO `t_chat_message` VALUES (298, 5, 1, NULL, '你好', 'text', 0, 7, NULL, '2025-12-20 19:52:58', 0);
INSERT INTO `t_chat_message` VALUES (299, 5, 7, NULL, 'hello', 'text', 0, 1, NULL, '2025-12-20 19:53:04', 0);
INSERT INTO `t_chat_message` VALUES (300, 5, 7, NULL, '哈哈哈哈', 'text', 0, 1, NULL, '2025-12-20 19:53:11', 0);
INSERT INTO `t_chat_message` VALUES (301, 5, 1, NULL, '11', 'text', 0, 7, NULL, '2025-12-20 20:05:16', 0);
INSERT INTO `t_chat_message` VALUES (302, 5, 7, NULL, '22', 'text', 0, 1, NULL, '2025-12-20 20:05:18', 0);
INSERT INTO `t_chat_message` VALUES (303, 5, 1, NULL, '33', 'text', 0, 7, NULL, '2025-12-20 20:05:23', 0);
INSERT INTO `t_chat_message` VALUES (304, 5, 1, NULL, '44', 'text', 0, 7, NULL, '2025-12-20 20:14:09', 0);
INSERT INTO `t_chat_message` VALUES (305, 5, 7, NULL, '55', 'text', 0, 1, NULL, '2025-12-20 20:17:33', 0);
INSERT INTO `t_chat_message` VALUES (306, 5, 1, NULL, '66', 'text', 0, 7, NULL, '2025-12-20 20:17:36', 0);
INSERT INTO `t_chat_message` VALUES (307, 5, 1, NULL, '77', 'text', 0, 7, NULL, '2025-12-20 20:19:30', 0);
INSERT INTO `t_chat_message` VALUES (308, 5, 7, NULL, '88', 'text', 0, 1, NULL, '2025-12-20 20:19:42', 0);
INSERT INTO `t_chat_message` VALUES (309, 5, 1, NULL, '99', 'text', 0, 7, NULL, '2025-12-20 20:19:50', 0);
INSERT INTO `t_chat_message` VALUES (310, 5, 7, NULL, '10', 'text', 0, 1, NULL, '2025-12-20 20:21:48', 0);
INSERT INTO `t_chat_message` VALUES (311, 5, 7, NULL, '11', 'text', 0, 1, NULL, '2025-12-20 20:22:02', 0);
INSERT INTO `t_chat_message` VALUES (312, 5, 7, NULL, '12', 'text', 0, 1, NULL, '2025-12-20 20:22:05', 0);
INSERT INTO `t_chat_message` VALUES (313, 5, 1, NULL, '13', 'text', 0, 7, NULL, '2025-12-20 20:23:33', 0);
INSERT INTO `t_chat_message` VALUES (314, 5, 7, NULL, '14', 'text', 0, 1, NULL, '2025-12-20 20:24:28', 0);
INSERT INTO `t_chat_message` VALUES (315, 5, 7, NULL, '15', 'text', 0, 1, NULL, '2025-12-20 20:27:36', 0);
INSERT INTO `t_chat_message` VALUES (316, 5, 1, NULL, '16', 'text', 0, 7, NULL, '2025-12-20 20:42:11', 0);
INSERT INTO `t_chat_message` VALUES (317, 5, 7, NULL, '17', 'text', 0, 1, NULL, '2025-12-20 20:42:30', 0);
INSERT INTO `t_chat_message` VALUES (318, 5, 1, NULL, '18', 'text', 0, 7, NULL, '2025-12-20 20:42:36', 0);
INSERT INTO `t_chat_message` VALUES (319, 5, 7, NULL, '19', 'text', 0, 1, NULL, '2025-12-20 20:42:39', 0);
INSERT INTO `t_chat_message` VALUES (320, 5, 7, NULL, '20', 'text', 0, 1, NULL, '2025-12-20 20:42:44', 0);
INSERT INTO `t_chat_message` VALUES (321, 5, 7, NULL, '21', 'text', 0, 1, NULL, '2025-12-20 20:43:36', 0);
INSERT INTO `t_chat_message` VALUES (322, 5, 7, NULL, '22', 'text', 0, 1, NULL, '2025-12-20 20:44:39', 0);
INSERT INTO `t_chat_message` VALUES (323, 5, 7, NULL, '23', 'text', 0, 1, NULL, '2025-12-20 20:44:42', 0);
INSERT INTO `t_chat_message` VALUES (324, 5, 7, NULL, '24', 'text', 0, 1, NULL, '2025-12-20 20:44:48', 0);
INSERT INTO `t_chat_message` VALUES (325, 5, 7, NULL, '25', 'text', 0, 1, NULL, '2025-12-20 20:45:28', 0);
INSERT INTO `t_chat_message` VALUES (326, 5, 7, NULL, '26', 'text', 0, 1, NULL, '2025-12-20 20:45:44', 0);
INSERT INTO `t_chat_message` VALUES (327, 5, 7, NULL, '27', 'text', 0, 1, NULL, '2025-12-20 20:45:48', 0);
INSERT INTO `t_chat_message` VALUES (328, 5, 7, NULL, '28', 'text', 0, 1, NULL, '2025-12-20 20:45:54', 0);
INSERT INTO `t_chat_message` VALUES (329, 5, 7, NULL, '29', 'text', 0, 1, NULL, '2025-12-20 20:47:34', 0);
INSERT INTO `t_chat_message` VALUES (330, 5, 7, NULL, '30', 'text', 0, 1, NULL, '2025-12-20 20:48:10', 0);
INSERT INTO `t_chat_message` VALUES (331, 5, 1, NULL, '31', 'text', 0, 7, NULL, '2025-12-20 20:55:59', 0);
INSERT INTO `t_chat_message` VALUES (332, 5, 7, NULL, '32', 'text', 0, 1, NULL, '2025-12-20 20:56:34', 0);
INSERT INTO `t_chat_message` VALUES (333, 5, 7, NULL, '33', 'text', 0, 1, NULL, '2025-12-20 20:56:38', 0);
INSERT INTO `t_chat_message` VALUES (334, 5, 1, NULL, '34', 'text', 0, 7, NULL, '2025-12-20 20:56:41', 0);
INSERT INTO `t_chat_message` VALUES (335, 5, 7, NULL, '35', 'text', 0, 1, NULL, '2025-12-20 20:56:45', 0);
INSERT INTO `t_chat_message` VALUES (336, 5, 7, NULL, '36', 'text', 0, 1, NULL, '2025-12-20 21:04:28', 0);
INSERT INTO `t_chat_message` VALUES (337, 5, 7, NULL, '37', 'text', 0, 1, NULL, '2025-12-20 21:10:14', 0);
INSERT INTO `t_chat_message` VALUES (338, 5, 7, NULL, '38', 'text', 0, 1, NULL, '2025-12-20 21:12:00', 0);
INSERT INTO `t_chat_message` VALUES (339, 5, 7, NULL, '39', 'text', 0, 1, NULL, '2025-12-20 21:12:19', 0);
INSERT INTO `t_chat_message` VALUES (340, 5, 7, NULL, '40', 'text', 0, 1, NULL, '2025-12-20 21:12:36', 0);
INSERT INTO `t_chat_message` VALUES (341, 5, 7, NULL, '41', 'text', 0, 1, NULL, '2025-12-20 21:13:06', 0);
INSERT INTO `t_chat_message` VALUES (342, 5, 1, NULL, '42', 'text', 0, 7, NULL, '2025-12-20 21:13:13', 0);
INSERT INTO `t_chat_message` VALUES (343, 5, 7, NULL, '43', 'text', 0, 1, NULL, '2025-12-20 21:13:16', 0);
INSERT INTO `t_chat_message` VALUES (344, 5, 7, NULL, '44', 'text', 0, 1, NULL, '2025-12-20 21:13:22', 0);
INSERT INTO `t_chat_message` VALUES (345, 5, 1, NULL, '45', 'text', 0, 7, NULL, '2025-12-20 21:21:52', 0);
INSERT INTO `t_chat_message` VALUES (346, 5, 7, NULL, '46', 'text', 0, 1, NULL, '2025-12-20 21:29:00', 0);
INSERT INTO `t_chat_message` VALUES (347, 5, 1, NULL, '47', 'text', 0, 7, NULL, '2025-12-20 21:29:04', 0);
INSERT INTO `t_chat_message` VALUES (348, 5, 7, NULL, '48', 'text', 0, 1, NULL, '2025-12-20 21:29:07', 0);
INSERT INTO `t_chat_message` VALUES (349, 5, 7, NULL, '49', 'text', 0, 1, NULL, '2025-12-20 21:29:14', 0);
INSERT INTO `t_chat_message` VALUES (350, 5, 1, NULL, '50', 'text', 0, 7, NULL, '2025-12-20 21:29:30', 0);
INSERT INTO `t_chat_message` VALUES (351, 7, 8, NULL, '😀👍💔', 'text', 0, 1, NULL, '2025-12-21 20:58:08', 0);
INSERT INTO `t_chat_message` VALUES (352, 7, 1, NULL, '怎么啦', 'text', 0, 8, NULL, '2025-12-21 20:59:16', 0);
INSERT INTO `t_chat_message` VALUES (353, 7, 1, NULL, '欢迎咨询', 'text', 0, 8, NULL, '2025-12-21 20:59:25', 0);
INSERT INTO `t_chat_message` VALUES (354, 7, 8, NULL, '好的👌', 'text', 0, 1, NULL, '2025-12-21 20:59:31', 0);
INSERT INTO `t_chat_message` VALUES (355, 7, 8, NULL, '有问题我再问你', 'text', 0, 1, NULL, '2025-12-21 20:59:41', 0);
INSERT INTO `t_chat_message` VALUES (356, 7, 1, NULL, '嗯嗯', 'text', 0, 8, NULL, '2025-12-21 20:59:48', 0);
INSERT INTO `t_chat_message` VALUES (357, 7, 8, NULL, '😭', 'text', 0, 1, NULL, '2025-12-21 21:20:59', 0);
INSERT INTO `t_chat_message` VALUES (358, 7, 1, NULL, '您好 请问怎么啦', 'text', 0, 8, NULL, '2025-12-21 21:21:09', 0);
INSERT INTO `t_chat_message` VALUES (359, 7, 8, NULL, '就是', 'text', 0, 1, NULL, '2025-12-21 21:21:16', 0);
INSERT INTO `t_chat_message` VALUES (360, 7, 8, NULL, '我好纠结', 'text', 0, 1, NULL, '2025-12-21 21:21:24', 0);
INSERT INTO `t_chat_message` VALUES (361, 7, 8, NULL, '都好可爱 小动物们', 'text', 0, 1, NULL, '2025-12-21 21:21:35', 0);
INSERT INTO `t_chat_message` VALUES (362, 7, 1, NULL, '哈哈哈', 'text', 0, 8, NULL, '2025-12-21 21:21:41', 0);
INSERT INTO `t_chat_message` VALUES (363, 5, 7, NULL, '51', 'text', 0, 1, NULL, '2025-12-21 22:46:32', 0);
INSERT INTO `t_chat_message` VALUES (364, 5, 7, NULL, '525252525252', 'text', 0, 1, NULL, '2025-12-21 22:49:44', 0);
INSERT INTO `t_chat_message` VALUES (365, 5, 7, NULL, 'http://localhost:9000/animal-adopt/images/42c460179ad6412da9e2cb91fd53d401.jpg', 'image', 0, 1, NULL, '2025-12-21 23:05:54', 0);
INSERT INTO `t_chat_message` VALUES (366, 5, 1, NULL, '111', 'text', 0, 7, NULL, '2025-12-21 23:17:52', 0);
INSERT INTO `t_chat_message` VALUES (367, 5, 7, NULL, 'http://localhost:9000/animal-adopt/images/6dc99af6e65548998ba5c5048d4a1590.png', 'image', 0, 1, NULL, '2025-12-21 23:21:15', 0);
INSERT INTO `t_chat_message` VALUES (368, 5, 7, NULL, 'http://localhost:9000/animal-adopt/images/276607b15c724febab73bf77d27e7807.jpg', 'image', 0, 1, NULL, '2025-12-22 00:09:54', 0);
INSERT INTO `t_chat_message` VALUES (369, 5, 1, NULL, 'http://localhost:9000/animal-adopt/images/98585b4a9a9e44999c8f4c7b25fdac8b.jpg', 'image', 0, 7, NULL, '2025-12-22 09:08:48', 0);
INSERT INTO `t_chat_message` VALUES (370, 5, 1, NULL, '😂', 'text', 0, 7, NULL, '2025-12-22 09:11:27', 0);
INSERT INTO `t_chat_message` VALUES (371, 5, 1, NULL, '😊', 'text', 0, 7, NULL, '2025-12-22 09:11:29', 0);
INSERT INTO `t_chat_message` VALUES (372, 5, 1, NULL, '🌟', 'text', 0, 7, NULL, '2025-12-22 09:11:30', 0);
INSERT INTO `t_chat_message` VALUES (373, 5, 7, NULL, '🙏🙏🙏', 'text', 0, 1, NULL, '2025-12-22 09:11:37', 0);
INSERT INTO `t_chat_message` VALUES (374, 5, 7, NULL, 'http://localhost:9000/animal-adopt/images/e3141f88ca93461a9ea3b15c2d885b49.jpg', 'image', 0, 1, NULL, '2025-12-22 09:22:21', 0);
INSERT INTO `t_chat_message` VALUES (375, 5, 1, NULL, 'http://localhost:9000/animal-adopt/images/deea7b52723a402f9eaba3a3d96981e5.jpg', 'image', 0, 7, NULL, '2025-12-22 09:22:35', 0);
INSERT INTO `t_chat_message` VALUES (376, 5, 1, NULL, 'http://localhost:9000/animal-adopt/images/3d72295d6cc54fc482028097057f6621.jpg', 'image', 0, 7, NULL, '2025-12-22 09:25:11', 0);
INSERT INTO `t_chat_message` VALUES (377, 5, 7, NULL, 'http://localhost:9000/animal-adopt/images/bf11c736faf146219536ee535148d5b5.jpg', 'image', 0, 1, NULL, '2025-12-22 09:25:34', 0);
INSERT INTO `t_chat_message` VALUES (378, 5, 1, NULL, '12121', 'text', 0, 7, NULL, '2025-12-22 09:28:26', 0);
INSERT INTO `t_chat_message` VALUES (379, 5, 1, NULL, '🤣🤣🤣🤣', 'text', 0, 7, NULL, '2025-12-22 10:35:45', 0);
INSERT INTO `t_chat_message` VALUES (380, 5, 7, NULL, '😂😂😂', 'text', 0, 1, NULL, '2025-12-22 10:35:52', 0);
INSERT INTO `t_chat_message` VALUES (381, 5, 1, NULL, '😁😁😁😁😁', 'text', 0, 7, NULL, '2025-12-22 10:35:56', 0);
INSERT INTO `t_chat_message` VALUES (382, 5, 7, NULL, '😀😀😀😀😀', 'text', 0, 1, NULL, '2025-12-22 10:35:59', 0);
INSERT INTO `t_chat_message` VALUES (383, 5, 1, NULL, 'http://localhost:9000/animal-adopt/images/aca3ce43b2274f11a6548debf21ed779.png', 'image', 0, 7, NULL, '2025-12-22 10:37:27', 0);
INSERT INTO `t_chat_message` VALUES (384, 5, 7, NULL, '111😄', 'text', 0, 1, NULL, '2025-12-22 14:01:47', 0);
INSERT INTO `t_chat_message` VALUES (385, 5, 1, NULL, '😂', 'text', 0, 7, NULL, '2025-12-22 14:01:52', 0);
INSERT INTO `t_chat_message` VALUES (386, 5, 7, NULL, 'http://localhost:9000/animal-adopt/images/5e1c4ad06f4148889779fa0332f03ff8.jpg', 'image', 0, 1, NULL, '2025-12-22 14:01:59', 0);
INSERT INTO `t_chat_message` VALUES (387, 5, 1, NULL, 'http://localhost:9000/animal-adopt/images/f06c982d6f194a05a83daa1f1a2f6851.png', 'image', 0, 7, NULL, '2025-12-22 14:02:51', 0);
INSERT INTO `t_chat_message` VALUES (388, 5, 1, NULL, 'http://localhost:9000/animal-adopt/images/35eee121333940389db55a3e14c9484b.png', 'image', 0, 7, NULL, '2025-12-22 14:05:00', 0);
INSERT INTO `t_chat_message` VALUES (389, 5, 1, NULL, 'http://localhost:9000/animal-adopt/images/a8f5844dc71f4348976938fe4bde956f.png', 'image', 0, 7, NULL, '2025-12-22 14:05:24', 0);
INSERT INTO `t_chat_message` VALUES (390, 7, 8, NULL, 'http://localhost:9000/animal-adopt/images/7a73827f6a8f49c9bf170dd4c17d08ec.jpg', 'image', 0, 1, NULL, '2025-12-22 14:15:44', 0);
INSERT INTO `t_chat_message` VALUES (391, 7, 8, NULL, '请问这只是什么动物？', 'text', 0, 1, NULL, '2025-12-22 14:15:51', 0);
INSERT INTO `t_chat_message` VALUES (392, 7, 1, NULL, '哦 这只是镇园之宝“熊高老师“ 您眼光真好~', 'text', 0, 8, NULL, '2025-12-22 14:16:38', 0);
INSERT INTO `t_chat_message` VALUES (393, 7, 8, NULL, '😁😁😁😁', 'text', 0, 1, NULL, '2025-12-22 14:16:45', 0);
INSERT INTO `t_chat_message` VALUES (394, 7, 8, NULL, 'http://localhost:9000/animal-adopt/images/b92e5d68193b455fbc4daf7f951cb0b3.jpg', 'image', 0, 1, NULL, '2025-12-24 15:49:07', 0);
INSERT INTO `t_chat_message` VALUES (395, 7, 8, NULL, 'http://localhost:9000/animal-adopt/images/e3732bb6e0d947dda1432917f58f5ff4.png', 'image', 0, 1, NULL, '2025-12-24 15:49:25', 0);
INSERT INTO `t_chat_message` VALUES (396, 7, 1, NULL, '😁', 'text', 0, 8, NULL, '2025-12-24 15:49:34', 0);
INSERT INTO `t_chat_message` VALUES (397, 7, 8, NULL, '这是什么品种的动物呢', 'text', 0, 1, NULL, '2025-12-24 15:49:51', 0);
INSERT INTO `t_chat_message` VALUES (398, 7, 1, NULL, '第一只是熊大，第二只是哈士奇', 'text', 0, 8, NULL, '2025-12-24 15:50:11', 0);
INSERT INTO `t_chat_message` VALUES (399, 7, 8, NULL, '👌', 'text', 0, 1, NULL, '2025-12-24 15:50:16', 0);
INSERT INTO `t_chat_message` VALUES (400, 7, 1, NULL, '嗯嗯', 'text', 0, 8, NULL, '2025-12-24 15:50:20', 0);
INSERT INTO `t_chat_message` VALUES (401, 7, 8, NULL, '你好', 'text', 0, 1, NULL, '2025-12-24 16:03:07', 0);
INSERT INTO `t_chat_message` VALUES (402, 7, 1, NULL, '你好，有什么我可以帮助您的吗', 'text', 0, 8, NULL, '2025-12-24 16:03:16', 0);
INSERT INTO `t_chat_message` VALUES (403, 7, 8, NULL, '我想问一下', 'text', 0, 1, NULL, '2025-12-24 16:03:24', 0);
INSERT INTO `t_chat_message` VALUES (404, 7, 8, NULL, 'http://localhost:9000/animal-adopt/images/fcc8a2ea788c4c65be6e083771cb6c68.png', 'image', 0, 1, NULL, '2025-12-24 16:03:29', 0);
INSERT INTO `t_chat_message` VALUES (405, 7, 8, NULL, '这只哈士奇还有吗', 'text', 0, 1, NULL, '2025-12-24 16:03:39', 0);
INSERT INTO `t_chat_message` VALUES (406, 7, 1, NULL, '不好意思哦 这是镇店之宝 不提供领养服务哦~', 'text', 0, 8, NULL, '2025-12-24 16:03:59', 0);
INSERT INTO `t_chat_message` VALUES (407, 7, 1, NULL, '😊', 'text', 0, 8, NULL, '2025-12-24 16:04:05', 0);
INSERT INTO `t_chat_message` VALUES (408, 7, 8, NULL, '哦哦 好👌', 'text', 0, 1, NULL, '2025-12-24 16:04:18', 0);
INSERT INTO `t_chat_message` VALUES (409, 2, 1, NULL, '😀😀😀😀😀😀😀', 'text', 0, 1, NULL, '2026-01-03 00:47:23', 0);
INSERT INTO `t_chat_message` VALUES (410, 19, 29, NULL, '你好', 'text', 0, 1, NULL, '2026-01-31 22:12:32', 0);
INSERT INTO `t_chat_message` VALUES (411, 19, 1, NULL, '你好呀', 'text', 0, 29, NULL, '2026-01-31 22:13:10', 0);
INSERT INTO `t_chat_message` VALUES (412, 19, 29, NULL, '😁', 'text', 0, 1, NULL, '2026-01-31 22:13:14', 0);
INSERT INTO `t_chat_message` VALUES (413, 19, 1, NULL, '😡😡😡', 'text', 0, 29, NULL, '2026-01-31 22:13:18', 0);
INSERT INTO `t_chat_message` VALUES (414, 19, 1, NULL, '👍👍👍👍', 'text', 0, 29, NULL, '2026-01-31 22:13:22', 0);
INSERT INTO `t_chat_message` VALUES (415, 19, 29, NULL, 'http://localhost:9000/animal-adopt/images/87fbf37850004a55a04639391cc2e9a3.jpg', 'image', 0, 1, NULL, '2026-01-31 22:13:31', 0);
INSERT INTO `t_chat_message` VALUES (416, 19, 29, NULL, '111', 'text', 0, 1, NULL, '2026-01-31 22:14:23', 0);
INSERT INTO `t_chat_message` VALUES (417, 22, 32, NULL, '你好😁', 'text', 0, 1, NULL, '2026-02-01 20:58:18', 0);
INSERT INTO `t_chat_message` VALUES (418, 22, 1, NULL, '😀', 'text', 0, 32, NULL, '2026-02-01 21:05:07', 0);
INSERT INTO `t_chat_message` VALUES (419, 22, 1, NULL, 'http://localhost:9000/animal-adopt/images/e4262224146b4791a21cfc89d408121f.jpg', 'image', 0, 32, NULL, '2026-02-01 21:05:18', 0);
INSERT INTO `t_chat_message` VALUES (420, 22, 1, NULL, '你好呀', 'text', 0, 32, NULL, '2026-02-01 21:12:27', 0);
INSERT INTO `t_chat_message` VALUES (421, 22, 32, NULL, '😁', 'text', 0, 1, NULL, '2026-02-01 21:12:35', 0);
INSERT INTO `t_chat_message` VALUES (422, 22, 32, NULL, 'http://localhost:9000/animal-adopt/images/99b72c05ad8d44749fc12d67b9087233.jpg', 'image', 0, 1, NULL, '2026-02-01 21:12:40', 0);
INSERT INTO `t_chat_message` VALUES (423, 23, 33, NULL, '😀', 'text', 0, 1, NULL, '2026-02-01 22:20:12', 0);

-- ----------------------------
-- Table structure for t_conversation_history
-- ----------------------------
DROP TABLE IF EXISTS `t_conversation_history`;
CREATE TABLE `t_conversation_history`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `session_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '会话ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `role` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '角色 user/assistant',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '消息内容',
  `tool_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `tool_params` json NULL,
  `tool_result` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `timestamp` datetime NULL DEFAULT NULL COMMENT '时间戳',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_session_id_create_time`(`session_id` ASC, `create_time` ASC) USING BTREE,
  INDEX `idx_user_id_role`(`user_id` ASC, `role` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 45 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'AI客服对话历史表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_conversation_history
-- ----------------------------
INSERT INTO `t_conversation_history` VALUES (1, '26274b7e741a4b9ba56820c27f28a3af', 1, 'assistant', '\"您好！欢迎来到i宠园，很高兴您想要开启养宠之旅 🐾\n\n虽然目前系统中暂无专门针对新手的入门指南文章，但我们可以为您提供一些关键建议来帮助您做好准备：\n\n- 了解养宠的基本责任：包括日常喂养、清洁、医疗保健和情感陪伴\n- 不同宠物有不同的需求，例如狗狗需要每天遛，猫咪相对独立但也需要互动\n- 领养前请评估自己的时间、经济能力和居住环境是否适合\n\n我可以为您推荐最适合新手的宠物类型，或者介绍当前正在等待领养的可爱毛孩子们。\n\n您更想先了解哪方面呢？比如“适合新手的宠物推荐”或“领养流程说明”？我很乐意继续协助您 ❤️\"', NULL, NULL, NULL, '2025-11-23 17:27:33', '2025-11-23 17:27:33', '2025-11-23 17:27:33', 0);
INSERT INTO `t_conversation_history` VALUES (2, '26274b7e741a4b9ba56820c27f28a3af', 1, 'assistant', '\"当然可以！以下是i宠园目前正在等待领养的几只可爱小狗，它们性格各异，总有一款适合您：\n\n🐾 **傻蛋小高 - 萨摩耶**\n温顺可爱的萨摩耶，白色毛发如雪般纯净，性格温和亲人，喜欢安静陪伴。适合想要一个温柔伴侣的家庭。\n\n🐾 **拉拉 - 拉布拉多寻回犬**\n性格温顺友善，对人热情，特别喜欢游泳和户外活动。已绝育并完成疫苗接种，是理想的家庭伴侣犬，尤其适合有孩子的家庭。\n\n🐾 **小黑 - 边境牧羊犬**\n智商极高、学习能力强，精力充沛且专注力好。需要主人有足够时间进行训练和互动，适合有养犬经验、能提供丰富智力刺激的家庭。\n\n🐾 **二哈 - 哈士奇**\n活泼好动，蓝眼睛帅气迷人，黑白毛色经典醒目。精力旺盛，喜欢奔跑跳跃，需要大量运动空间，适合热爱户外活动的主人。\n\n🐾 **泰迪 - 泰迪犬**\n活泼粘人，聪明伶俐，棕色卷毛非常可爱。已绝育打针，身体健康，喜欢与主人亲密互动，非常适合城市公寓生活。\n\n🐾 **小柴 - 柴犬**\n独立忠诚，赤色毛发配上标志笑容治愈力满分。性格活泼但不过分依赖，警惕性高，适合喜欢中型犬又希望有一定独立性的主人。\n\n🐾 **金金 - 金毛寻回犬**\n性格温顺听话，金黄色毛发闪耀动人，会基本指令，对小孩极有耐心。已完成绝育和疫苗接种，是公认的“国民级”家庭宠物首选。\n\n【温馨提示】\n每只狗狗都有独特的个性和需求，请根据您的居住环境、时间和生活方式选择最适合的伙伴。如果您对某位小朋友感兴趣，欢迎查看详情或提交领养申请，我们的工作人员将协助您完成后续流程 ❤️\"', NULL, NULL, NULL, '2025-11-23 17:28:57', '2025-11-23 17:28:57', '2025-11-23 17:28:57', 0);
INSERT INTO `t_conversation_history` VALUES (3, 'c913f669867b42c7a68203b8f4a1033e', 1, 'assistant', '\"您好！我注意到您提到了“刚才推荐的那些”，但目前我们的对话中还没有进行过具体的宠物推荐。可能是之前的对话记录没有显示完整。\n\n为了更好地帮助您，我可以根据您的偏好重新为您推荐一些优秀的宠物候选：\n\n- 您想要什么类型的宠物？（如猫、狗等）\n- 您希望它有什么样的性格？（如活泼、温顺、独立等）\n- 您的生活环境是怎样的？（如公寓、有院子、是否有小孩等）\n\n或者，我可以先为您展示当前最受欢迎的几只可领养宠物，这些都是非常可爱且适合家庭的好伙伴。\n\n【温馨提示】  \n每只宠物都是独特的个体，我们建议您根据自己的生活方式选择最适合的伴侣，而不是仅仅基于 popularity 做决定。我很乐意为您提供个性化建议！\"', NULL, NULL, NULL, '2025-11-23 17:29:59', '2025-11-23 17:29:59', '2025-11-23 17:29:59', 0);
INSERT INTO `t_conversation_history` VALUES (4, '7560b3265c014b4e99d4f79569a93b4b', 1, 'assistant', '\"我理解您是第一次考虑养宠物，这是一件非常值得期待的事！为了帮助您更好地准备，我为您查找了一些适合新手的入门指南和相关文章。\n\n目前系统中暂无直接匹配的入门指南内容，但您可以关注以下几个方面来为养宠做好准备：\n\n- 了解不同宠物的基本需求（如饮食、运动、护理）\n- 评估自己的生活方式是否适合养宠\n- 学习如何与宠物建立信任关系\n- 提前规划宠物的健康管理和日常开销\n\n【温馨提示】  \n如果您愿意，我可以根据您的居住环境、时间安排和性格特点，为您推荐最适合的宠物类型。欢迎随时告诉我您的具体情况，我会全力为您提供个性化建议！\"', NULL, NULL, NULL, '2025-11-23 17:39:47', '2025-11-23 17:39:47', '2025-11-23 17:39:47', 0);
INSERT INTO `t_conversation_history` VALUES (5, '7560b3265c014b4e99d4f79569a93b4b', 1, 'assistant', '\"很高兴您喜欢小狗！根据系统中的信息，我为您精选了几只性格各异、正在等待领养的可爱狗狗。每一只都经过健康检查，部分已完成疫苗接种和绝育手术。\n\n🐾 **拉拉 - 拉布拉多寻回犬**  \n性格温顺友善，对人友好，特别喜欢游泳，是优秀的家庭伴侣犬。已绝育并完成疫苗接种，非常适合有孩子的家庭。需要每天遛狗两次，最好有院子供其活动。\n\n🐾 **金金 - 金毛寻回犬**  \n性格温顺友善，聪明听话，会基本指令，对小孩很有耐心。已完成绝育和疫苗接种，训练有素，是孩子们的理想玩伴。适合有足够活动空间的家庭。\n\n🐾 **小黑 - 边境牧羊犬**  \n智商极高，学习能力强，精力充沛。已绝育并完成疫苗接种，需要大量脑力和体力活动。建议主人每天提供至少2小时户外运动，并能进行持续训练。\n\n🐾 **二哈 - 哈士奇**  \n性格活泼好动，精力旺盛，标志性的蓝眼睛非常迷人。已绝育并完成疫苗接种，虽然有点调皮，但性格友善。适合有经验的主人，需大量运动空间。\n\n🐾 **小柴 - 柴犬**  \n性格独立但忠诚，活泼好动，喜欢户外活动，警惕性高。已绝育并完成疫苗接种，标准赤色毛发，笑容治愈。需要每天至少1小时户外运动，适合喜欢户外活动的主人。\n\n🐾 **泰迪 - 泰迪犬**  \n性格活泼粘人，聪明伶俐，喜欢和主人互动。已绝育并接种疫苗，体型小巧，非常适合陪伴。需要定期美容修剪毛发，适合有时间照顾的家庭。\n\n🐾 **傻蛋小高 - 萨摩耶**  \n性格温顺，白色毛发蓬松可爱。虽然还未完成疫苗接种，但身体健康。需要定期梳理毛发，适合愿意花时间打理的主人。\n\n【温馨提示】  \n所有狗狗都已通过健康检查，建议您根据居住环境和生活方式选择最适合的伙伴。如果您对其中任何一位毛孩子感兴趣，请告诉我，我可以协助您提交领养申请。\"', NULL, NULL, NULL, '2025-11-23 17:39:58', '2025-11-23 17:39:58', '2025-11-23 17:39:58', 0);
INSERT INTO `t_conversation_history` VALUES (6, '100780d55ae343a680d419c1dda86216', 1, 'assistant', '\"您好！欢迎来到i宠园，很高兴您对养宠物感兴趣！\n\n作为新手家长，第一次养宠物确实会有很多需要了解的地方。虽然系统暂时没有检索到相关指南文章，但我可以为您提供一些基础建议：\n\n- **选择适合的宠物**：初次养宠建议从性格温顺、护理简单的品种开始，比如成年猫咪或性格稳定的犬种\n- **生活环境准备**：提前准备好宠物的生活空间，包括饮食用具、休息区域和卫生设施\n- **时间与精力投入**：考虑自己每天能陪伴宠物的时间，选择匹配生活节奏的伴侣\n- **医疗保健**：领养后应及时带宠物做健康检查，完成疫苗接种和驱虫\n\n【温馨提示】  \n如果您愿意分享更多关于您的生活方式（如居住环境、工作时间等），我可以为您推荐最适合的宠物类型。我们始终相信，每一只宠物都值得一个温暖的家，而每一个家庭也值得拥有最适合的毛孩子伴侣。\"', NULL, NULL, NULL, '2025-11-23 17:52:38', '2025-11-23 17:52:38', '2025-11-23 17:52:38', 0);
INSERT INTO `t_conversation_history` VALUES (7, '100780d55ae343a680d419c1dda86216', 1, 'assistant', '\"看到您喜欢狗狗，我非常高兴！根据我们i宠园当前可领养的犬只信息，为您精心挑选了几位可爱的毛孩子：\n\n🐾 **傻蛋小高 - 萨摩耶**\n性格温顺，5岁的成年犬，白色毛发非常可爱。萨摩耶以\"微笑天使\"著称，适合想要一个温柔伴侣的家庭。\n\n🐾 **拉拉 - 拉布拉多寻回犬**\n2岁的黄色拉布拉多，性格温顺友善，已绝育和接种疫苗。优秀的家庭伴侣犬，特别喜欢游泳，对小孩很有耐心。\n\n🐾 **金金 - 金毛寻回犬**\n1.5岁的帅气金毛，金黄色毛发，性格温顺聪明，已完成基本训练。非常适合家庭饲养，是孩子们的理想玩伴。\n\n🐾 **二哈 - 哈士奇**\n1.25岁的黑白哈士奇，蓝眼睛炯炯有神，精力充沛，需要大量运动。适合有经验、能陪伴户外活动的主人。\n\n🐾 **小柴 - 柴犬**\n1岁的赤色柴犬，活泼好动，独立忠诚，有着治愈系的笑容。需要每天足够的运动量。\n\n【温馨提示】\n每只狗狗都有独特的个性和需求。建议您根据自己的生活环境（如住房大小）、作息时间（能否定时遛狗）和养宠经验来选择最适合的伙伴。如果您愿意分享更多关于您的生活方式，我可以为您提供更精准的推荐。\n\n欢迎随时预约到园区与这些可爱的毛孩子互动，亲身感受哪一位最打动您的心！\"', NULL, NULL, NULL, '2025-11-23 17:52:57', '2025-11-23 17:52:57', '2025-11-23 17:52:57', 0);
INSERT INTO `t_conversation_history` VALUES (8, 'e084d86ab45c4238b00e1b9253632462', 1, 'assistant', '\"您好！欢迎来到i宠园，很高兴您对养宠物感兴趣。作为新手，了解相关知识是非常重要的一步。以下是为您准备的入门信息：\n\n【新手养宠须知】\n- 了解领养责任：宠物是家庭成员，需要长期关爱和照顾\n- 准备基本用品：食盆、水盆、猫砂盆/狗厕所、窝、玩具等\n- 了解基础护理：饮食管理、日常清洁、疫苗驱虫等\n- 学习行为训练：帮助宠物适应家庭生活\n- 预留时间和精力：陪伴是建立感情的关键\n\n以下是一些当前正在等待领养的可爱宠物，也许能给您一些启发：\n\n🐾 **傻蛋小高 - 萨摩耶**\n温顺可爱的性格，适合想要大型犬的家庭。虽然还未完成疫苗接种，但健康状况良好。\n\n🐾 **小狸 - 中华狸花猫**\n性格独立机警，适应能力强，是优秀的捕鼠能手，已绝育并完成疫苗接种。\n\n🐾 **布布 - 布偶猫**\n性格温顺甜美，喜欢被抱，叫声轻柔，是完美的家庭伴侣，需要定期毛发护理。\n\n🐾 **小暹 - 暹罗猫**\n性格活泼粘人，喜欢和主人交流，需要大量陪伴，适合时间充裕的家庭。\n\n🐾 **橘子 - 中华田园猫**\n性格亲人温顺，喜欢晒太阳，很会撒娇，需要注意饮食控制避免过胖。\n\n【温馨提示】\n如果您是首次养宠，建议从性格温顺、护理相对简单的宠物开始。我们推荐您先阅读新手养宠指南，并考虑自己的生活方式是否适合养宠。领养前可以预约见面，与工作人员详细沟通。\n\n有任何疑问都欢迎随时咨询，我们会全程为您提供专业指导和支持。\"', NULL, NULL, NULL, '2025-11-23 17:56:08', '2025-11-23 17:56:08', '2025-11-23 17:56:08', 0);
INSERT INTO `t_conversation_history` VALUES (9, '7afabbff034745e39a983fbd632c1217', 1, 'user', '\"我从来没养过宠物 有什么推荐的吗\"', NULL, NULL, NULL, '2025-11-23 19:06:24', '2025-11-23 19:06:24', '2025-11-23 19:06:24', 0);
INSERT INTO `t_conversation_history` VALUES (10, '7afabbff034745e39a983fbd632c1217', 1, 'assistant', '\"您好！作为第一次养宠物的新手，您选择领养是非常有爱心的决定。我为您精选了几只性格温顺、适合新手饲养的可领养宠物，它们都已经完成疫苗接种和绝育手术，健康状况良好。\n\n🐾 **圆圆 - 英国短毛猫**\n性格温顺安静，喜欢睡觉，不太爱动，非常适合公寓生活。\n适合希望拥有一个安静、不吵闹的伴侣的您。\n\n🐾 **布布 - 布偶猫**\n性格温顺甜美，喜欢被抱，叫声轻柔，是完美的家庭伴侣。\n适合想要一个亲人、温柔的猫咪朋友的新手主人。\n\n🐾 **橘子 - 中华田园猫（橘色）**\n性格亲人温顺，喜欢晒太阳，很会撒娇，食量较大。\n适合喜欢互动、愿意花时间陪伴猫咪的家庭。\n\n🐾 **拉拉 - 拉布拉多寻回犬**\n性格温顺友善，对人友好，特别是对小孩特别有耐心。\n适合有足够空间和时间遛狗的家庭，是优秀的家庭伴侣犬。\n\n🐾 **小狸 - 中华狸花猫**\n性格独立机警，适应能力强，是优秀的捕鼠能手，但也亲人。\n适合希望养一只既独立又能互动的猫咪的新手。\n\n【温馨提示】\n新手养宠建议从以下几点开始准备：\n- 准备好基础用品：猫砂盆/狗厕所、食盆水盆、舒适窝垫\n- 选择适合的宠物食品，避免突然更换食物\n- 定期进行健康检查，按时驱虫\n- 给予足够的耐心和关爱，帮助新成员适应新家\n\n如果您对某只宠物感兴趣，我可以为您提供更多详细信息或协助您提交领养申请。\"', NULL, NULL, NULL, '2025-11-23 19:06:24', '2025-11-23 19:06:24', '2025-11-23 19:06:24', 0);
INSERT INTO `t_conversation_history` VALUES (11, 'beb671326596427eae5cf39d1fa20333', 1, 'assistant', '\"您好！欢迎来到i宠园，很高兴您考虑领养人生中的第一只宠物。作为新手家长，选择一只性格温顺、易于照顾的宠物非常重要。\n\n以下是目前可领养的几只非常适合新手的宠物推荐：\n\n🐾 **布布 - 布偶猫**\n性格温顺甜美，喜欢被抱，叫声轻柔，是完美的家庭伴侣。已绝育并完成疫苗接种，适合初次养猫的家庭。\n\n🐾 **圆圆 - 英国短毛猫**\n性格温顺安静，喜欢睡觉，不太爱动，非常适合公寓饲养。已绝育，护理相对简单。\n\n🐾 **拉拉 - 拉布拉多寻回犬**\n性格温顺友善，对人友好，特别是对小孩特别有耐心，是优秀的家庭伴侣犬。已绝育并完成疫苗接种。\n\n【温馨提示】\n我们建议新手家长从猫咪开始，因为它们相对独立，护理需求较狗狗少。如果您选择养狗，请确保能每天抽出时间遛狗和陪伴。\n\n需要了解更多护理知识吗？我可以为您提供详细的《新手养宠指南》和各类宠物护理建议。\"', NULL, NULL, NULL, '2025-11-23 19:11:24', '2025-11-23 19:11:24', '2025-11-23 19:11:24', 0);
INSERT INTO `t_conversation_history` VALUES (12, 'beb671326596427eae5cf39d1fa20333', 1, 'assistant', '\"您好！欢迎来到i宠园，作为新手养宠家庭，选择一只性格温顺、容易相处的宠物非常重要。我已经为您筛选了一些非常适合新手的可领养宠物。\n\n根据您的情况，我特别推荐以下几只温顺友好的小伙伴：\n\n🐾 **布布 - 布偶猫**\n性格温顺甜美，喜欢被抱，叫声轻柔，是完美的家庭伴侣\n适合喜欢温柔猫咪、想要亲密互动的新手主人\n\n🐾 **橘子 - 中华田园猫**\n性格亲人温顺，食量较大，喜欢晒太阳，很会撒娇\n适合想要可爱陪伴、愿意给予关爱的家庭\n\n🐾 **拉拉 - 拉布拉多寻回犬**\n性格温顺友善，对人友好，喜欢游泳，是优秀的家庭伴侣犬\n适合有足够活动空间、能每天遛狗的家庭\n\n【温馨提示】\n作为新手，建议您优先考虑已绝育且完成疫苗接种的宠物，这样可以减少初期的医疗压力。同时，我们建议您先从性格温顺的品种开始，逐步学习宠物护理知识。\n\n如果您对任何一只小可爱感兴趣，我可以为您提供更详细的护理指南和领养流程说明。\"', NULL, NULL, NULL, '2025-11-23 19:17:03', '2025-11-23 19:17:03', '2025-11-23 19:17:03', 0);
INSERT INTO `t_conversation_history` VALUES (13, 'beb671326596427eae5cf39d1fa20333', 1, 'assistant', '\"很高兴您喜欢小狗！我已经为您筛选出几只性格各异、正在等待新家的可爱狗狗，它们都已绝育并完成疫苗接种，健康状况良好。\n\n🐾 **拉拉 - 拉布拉多寻回犬**\n性格温顺友善，对人友好，喜欢游泳，是优秀的家庭伴侣犬\n适合有院子、能每天遛狗的家庭，需要定期梳理毛发\n\n🐾 **金金 - 金毛寻回犬**\n性格温顺友善，聪明听话，喜欢和人互动，对小孩很有耐心\n适合家庭饲养，已完成基础训练，会基本指令\n\n🐾 **泰迪 - 泰迪犬**\n性格活泼粘人，聪明伶俐，喜欢和主人互动，适合陪伴\n体型小巧，适合公寓生活，但需要定期美容修剪毛发\n\n🐾 **小柴 - 柴犬**\n性格独立但忠诚，活泼好动，喜欢户外活动，警惕性高\n适合喜欢户外活动的主人，需要每天至少1小时运动\n\n🐾 **傻蛋小高 - 萨摩耶**\n性格温顺，一只可爱的萨摩耶，毛色洁白如雪\n需要大量毛发护理，适合有时间和精力照顾的家庭\n\n【温馨提示】\n建议新手主人优先考虑拉布拉多或金毛这类性格稳定、易于相处的犬种。这些狗狗都已经过基础社会化训练，更容易适应新家庭。\n\n如果您对某只狗狗特别感兴趣，我可以为您提供详细的领养流程说明和护理指南。\"', NULL, NULL, NULL, '2025-11-23 19:17:17', '2025-11-23 19:17:17', '2025-11-23 19:17:17', 0);
INSERT INTO `t_conversation_history` VALUES (14, '6b1f0aee1fd84d05aaa904b9c0bb3c0f', 1, 'assistant', '\"我能感受到你对养宠的谨慎和认真，这真的很棒！作为第一次养宠物的新手，确实有很多需要了解的地方。让我为你整理一些重要的注意事项，帮助你做好充分准备。\n\n【新手养宠核心要点】\n\n- **心理准备**：宠物是家庭成员，不是玩具，它们会依赖你10年甚至更久\n- **经济预算**：每月需预留食物、驱虫、疫苗等基础开销，以及应急医疗费用\n- **时间投入**：每天需要陪伴、喂食、清理，狗狗还需要定时遛弯\n- **居住环境**：考虑家中空间是否适合，是否有条件做好安全防护\n\n【温馨提示】\n建议先从性格温顺、护理相对简单的宠物开始，比如成年猫咪或小型犬。我们有专门针对新手的领养指导服务，可以为你匹配最适合的宠物伙伴。要不要先了解一下目前有哪些特别适合新手的可领养宠物呢？\"', NULL, NULL, NULL, '2025-11-24 11:01:12', '2025-11-24 11:01:12', '2025-11-24 11:01:12', 0);
INSERT INTO `t_conversation_history` VALUES (15, '6b1f0aee1fd84d05aaa904b9c0bb3c0f', 1, 'assistant', '\"太棒了！小猫确实是很好的伴侣，温柔又治愈。根据你的喜好，我为你筛选了几只性格温顺、适合新手的可领养猫咪，它们都已经完成绝育和疫苗接种，健康状况良好。\n\n🐾 **布布 - 布偶猫**\n性格温顺甜美，喜欢被抱，叫声轻柔，是完美的家庭伴侣\n适合喜欢亲密互动、能定期打理毛发的家庭\n\n🐾 **橘子 - 中华田园猫**\n性格亲人温顺，食量较大，喜欢晒太阳，很会撒娇\n适合想要活泼一点但又不失亲人的主人\n\n🐾 **圆圆 - 英国短毛猫**\n性格温顺安静，喜欢睡觉，不太爱动，非常适合公寓饲养\n适合生活节奏较慢、喜欢安静环境的主人\n\n【温馨提示】\n每只猫咪都有独特的个性和需求，建议先了解清楚再做决定。如果你对某只特别感兴趣，我可以为你提供更详细的护理建议和领养流程说明。想了解更多关于它们的信息吗？\"', NULL, NULL, NULL, '2025-11-24 11:01:33', '2025-11-24 11:01:33', '2025-11-24 11:01:33', 0);
INSERT INTO `t_conversation_history` VALUES (16, '0a21213bfca643bd93fec283b29ea565', 1, 'assistant', '\"看到您喜欢小狗，我为您精心挑选了几位正在等待新家的可爱毛孩子！每一只都性格温顺，健康状况良好，期待与您相遇。\n\n🐾 **小博 - 博美犬**\n性格温顺的小型犬，白色毛发蓬松可爱。体型小巧适合公寓生活，是贴心的陪伴伙伴。\n\n🐾 **耶耶 - 萨摩耶**\n一只拥有灿烂笑容的萨摩耶，白色毛发如雪般纯净。性格温顺友善，喜欢与人亲近，是\"微笑天使\"的代表。\n\n🐾 **拉拉 - 拉布拉多寻回犬**\n金黄色毛发的温柔大男孩，性格超好，对人友好，特别喜欢游泳。已绝育并完成疫苗接种，是优秀的家庭伴侣犬，尤其适合有孩子的家庭。\n\n🐾 **小黑 - 边境牧羊犬**\n黑白相间的聪明狗狗，智商极高，学习能力强。需要主人有足够时间进行训练和互动，适合喜欢运动和训练的主人。\n\n🐾 **二哈 - 哈士奇**\n帅气的黑白毛色配上标志性的蓝眼睛，精力充沛，喜欢奔跑。虽然有点调皮，但性格友善，适合有养犬经验的主人。\n\n🐾 **泰迪 - 泰迪犬**\n棕色卷毛的可爱小家伙，活泼粘人，聪明伶俐。非常适合需要陪伴的家庭，会给您的生活带来无尽欢乐。\n\n🐾 **小柴 - 柴犬**\n赤色毛发的治愈系狗狗，性格独立但忠诚，活泼好动。标志性的\"柴犬笑\"能瞬间融化您的心。\n\n🐾 **金金 - 金毛寻回犬**\n金黄色毛发的帅气大男孩，性格温顺友善，聪明听话。会基本指令，是孩子们的好伙伴，也是理想的家庭犬。\n\n【可领养状态】\n以上所有狗狗目前均为可领养状态，已完成必要的健康检查。\n\n【温馨提示】\n每只狗狗都有独特的个性和需求，建议您根据自己的生活方式和居住环境选择最适合的伙伴。如果您对某位小天使特别感兴趣，可以告诉我，我会为您提供更多详细信息。\"', NULL, NULL, NULL, '2025-11-24 14:43:00', '2025-11-24 14:43:00', '2025-11-24 14:43:00', 0);
INSERT INTO `t_conversation_history` VALUES (17, '0a21213bfca643bd93fec283b29ea565', 1, 'assistant', '\"很高兴您想要开启养宠之旅！作为首次养宠家庭，您需要做好以下准备：\n\n【领养基本条件】\n- 年满18周岁，有稳定收入和住所\n- 家庭成员均同意领养\n- 了解并承诺对宠物负责终身照顾\n- 接受领养回访\n\n【居家必备物品】\n- 食盆、水盆（建议选择防滑款式）\n- 适合宠物年龄的专用粮\n- 舒适的狗窝或猫爬架\n- 牵引绳（外出必备）\n- 猫砂盆及猫砂（如领养猫咪）\n- 宠物专用梳子和清洁工具\n- 安全的玩具（磨牙棒、逗猫棒等）\n\n【温馨提示】\n新手养宠建议从性格温顺、护理简单的品种开始。我们为每位新领养家庭提供《新手养宠指南》，包含喂养、训练、健康检查等全方位指导。\n\n如果您愿意，我可以为您安排一次免费的养宠咨询，帮助您更好地准备迎接新成员。\"', NULL, NULL, NULL, '2025-11-24 14:43:38', '2025-11-24 14:43:38', '2025-11-24 14:43:38', 0);
INSERT INTO `t_conversation_history` VALUES (18, 'a2a2a226462f4e2cbf1d7d27f1f22c67', 1, 'assistant', '\"目前在i宠园可领养的宠物种类丰富，涵盖了多种适合不同家庭和生活方式的小伙伴。以下是部分正在等待新家的可爱动物：\n\n🐾 **耶耶 - 萨摩耶**\n温顺、亲人，性格温和友善，是理想的家庭伴侣犬。\n适合喜欢中大型犬、有耐心陪伴狗狗的家庭。\n\n🐾 **小博 - 博美犬**\n温顺且活泼，体型小巧，非常适合公寓生活。\n适合初次养狗或居住空间有限的朋友。\n\n🐾 **边边 - 边牧犬**\n精力充沛、聪明伶俐，需要较多运动和互动。\n适合活跃家庭或有训练经验的主人。\n\n🐾 **金金 - 金毛**\n性格开朗、活泼友善，对儿童和其他宠物都很友好。\n是经典的“暖男”型家庭犬，适合有孩子的家庭。\n\n🐾 **布布 - 布偶猫**\n调皮可爱，外表甜美如娃娃，性格亲人粘人。\n适合喜欢互动、愿意花时间陪伴猫咪的人。\n\n🐾 **小狸 - 中华狸花猫**\n独立机警，身体强壮，适应力强，还是一位捕鼠小能手。\n已绝育并完成疫苗接种，省心好养。\n\n🐾 **仓仓 - 仓鼠**\n胆小谨慎，安静可爱，照顾简单。\n适合学生或生活节奏较快、时间有限的上班族。\n\n🐾 **小松 - 美洲红松鼠**\n活泼好动，充满野趣，观察性强。\n适合有自然庭院或对小型野生动物感兴趣的家庭。\n\n【温馨提示】  \n每一只宠物都有独特的性格和需求，我们鼓励您根据自己的生活环境、时间和精力选择最合适的伙伴。如果您想了解某只宠物的更多细节，或希望我们为您个性化推荐，请随时告诉我！\"', NULL, NULL, NULL, '2025-11-24 22:49:31', '2025-11-24 22:49:31', '2025-11-24 22:49:31', 0);
INSERT INTO `t_conversation_history` VALUES (19, 'a2a2a226462f4e2cbf1d7d27f1f22c67', 1, 'assistant', '\"目前在i宠园可领养的宠物主要包括以下几类：\n\n🐾 **狗狗**\n- 耶耶 - 萨摩耶：性格温顺，亲人友善\n- 小博 - 博美犬：体型小巧，活泼可爱\n- 边边 - 边牧犬：聪明伶俐，精力充沛\n- 金金 - 金毛：性格开朗，适合家庭饲养\n\n🐾 **猫咪**\n- 小狸 - 中华狸花猫：独立机警，适应力强，已绝育并完成疫苗接种\n- 布布 - 布偶猫：外表甜美，性格调皮粘人\n\n🐾 **松鼠**\n- 小松 - 美洲红松鼠：活泼好动，充满野趣\n- 大松 - 美洲红松鼠：谨慎安静，观察力强\n- 松松 - 松鼠：胆小谨慎，适合自然环境\n\n🐾 **仓鼠**\n- 仓仓 - 仓鼠：白色毛发，胆小谨慎，照顾简单\n\n【温馨提示】  \n所有宠物均处于“可领养”状态，健康状况良好。每只宠物都有独特的性格和需求，建议根据您的居住环境、生活方式和时间安排选择最适合的伴侣。\n\n如果您对某类宠物感兴趣，或想了解如何为特定宠物提供合适的照顾，请随时告诉我！\"', NULL, NULL, NULL, '2025-11-24 22:49:55', '2025-11-24 22:49:55', '2025-11-24 22:49:55', 0);
INSERT INTO `t_conversation_history` VALUES (20, 'a2a2a226462f4e2cbf1d7d27f1f22c67', 1, 'assistant', '\"很高兴您喜欢小狗！根据您的偏好，我为您筛选了几只性格活泼、正在等待领养的可爱狗狗：\\n\\n🐾 **金金 - 金毛**\\n活泼开朗，对人友善，是理想的家庭伴侣犬。\\n适合有孩子或希望养一只温暖“暖男”型狗狗的家庭。\\n\\n🐾 **边边 - 边牧犬**\\n精力充沛、聪明伶俐，喜欢互动和挑战。\\n适合有一定养犬经验、能提供足够运动和训练的主人。\\n\\n🐾 **二哈 - 哈士奇**\\n性格活泼好动，精力旺盛，标志性的蓝眼睛非常迷人。\\n需要每天至少2小时户外活动，适合有院子或热爱户外运动的家庭。\\n\\n🐾 **泰迪 - 泰迪犬**\\n活泼粘人，聪明伶俐，体型小巧，非常适合公寓生活。\\n需要定期美容护理，但非常适合作为陪伴型宠物。\\n\\n【温馨提示】  \\n所有推荐的小狗均已接种疫苗并完成健康检查，处于可领养状态。如果您对某只狗狗感兴趣，我可以为您提供更详细的照顾指南或安排领养咨询。\\n\\n想要了解如何与这些活泼的小狗建立良好关系？也可以告诉我您的生活环境，我会进一步为您匹配最合适的伙伴！\"', NULL, NULL, NULL, '2025-11-26 14:57:27', '2025-11-26 14:57:27', '2025-11-26 14:57:27', 0);
INSERT INTO `t_conversation_history` VALUES (21, '2c5c47d006f64a2ba708dd21c21a9022', 1, 'assistant', '\"您好！根据您喜欢体型小巧的动物这一偏好，我为您精心筛选了几只非常适合的可领养宠物。这些小可爱们都期待着找到温暖的家。\\n\\n🐾 **小博 - 博美犬**\\n温顺可爱的小伙伴，3岁的小型犬，体重仅10公斤。白色毛发蓬松漂亮，性格温顺友善，非常适合公寓生活，是理想的伴侣犬。\\n\\n🐾 **布布 - 布偶猫**\\n3岁的布偶猫小姐姐，拥有迷人的蓝色眼睛和柔软的白色毛发。虽然性格调皮但非常亲人，体型适中，适合室内饲养。\\n\\n🐾 **小暹 - 暹罗猫**\\n18个月大的优雅暹罗猫，重点色毛发配上蓝色眼睛格外迷人。性格活泼粘人，喜欢与主人交流互动，体型纤细小巧。\\n\\n🐾 **小白 - 白兔**\\n8个月大的可爱白兔，体重2.5公斤，体型娇小。性格活泼亲人，喜欢新鲜蔬菜，需要一定的活动空间，适合有院子的家庭。\\n\\n【温馨提示】\\n如果您对以上任何一位小天使感兴趣，可以随时查看详细信息或预约见面。我们建议您考虑自己的居住环境、时间安排和生活方式，选择最适合您的宠物伙伴。\\n\\n需要了解更多关于小型宠物的护理知识，也可以告诉我，我会为您提供专业的指导建议。\"', NULL, NULL, NULL, '2025-11-27 13:51:54', '2025-11-27 13:51:54', '2025-11-27 13:51:54', 0);
INSERT INTO `t_conversation_history` VALUES (22, '2c5c47d006f64a2ba708dd21c21a9022', 1, 'assistant', '\"您好！欢迎来到i宠园，作为新手领养人，您将开启一段充满爱与陪伴的美妙旅程。我们特别为您推荐一些性格温顺、易于照顾的宠物，帮助您顺利过渡到宠物主人的角色。\\n\\n您好！作为首次领养宠物的新手，选择性格温顺、容易相处的小伙伴非常重要。我已经为您筛选了一些非常适合新手的可领养宠物。\\n\\n🐾 **小布 - 布偶猫**\\n3岁的温柔小姐姐，性格极其温顺亲人，喜欢安静地依偎在主人身边。适应力强，容易与人建立感情，是新手养猫的理想选择。\\n\\n🐾 **豆豆 - 金毛寻回犬幼犬**\\n6个月大的阳光小男孩，性格开朗友善，聪明好学。金毛犬以温顺乖巧著称，非常适合家庭饲养，容易训练。\\n\\n🐾 **小白 - 英国短毛猫**\\n2岁的英短蓝猫，性格稳定，独立性强但又不失亲人性。生活习性规律，护理简单，非常适合初次养猫的家庭。\\n\\n🐾 **团团 - 荷兰猪**\\n1岁的可爱小宠，体型小巧，性格温和。不需要太多空间，喂养简单，是体验宠物照顾的好选择。\\n\\n【新手养宠指南】\\n我们特别为您准备了新手养宠入门资料，涵盖：\\n- 宠物到家前的准备工作\\n- 饮食和日常护理要点\\n- 如何建立信任关系\\n- 基础训练技巧\\n\\n【温馨提示】\\n建议您从性格温顺、护理相对简单的宠物开始。每只宠物都有详细的档案和照护说明，您可以随时查看或预约见面。\\n\\n如果需要了解更多关于特定宠物的护理知识，我很乐意为您提供专业建议。\"', NULL, NULL, NULL, '2025-11-28 15:15:04', '2025-11-28 15:15:04', '2025-11-28 15:15:04', 0);
INSERT INTO `t_conversation_history` VALUES (23, 'f8bad93fa74648ed9cac83a435e524ca', 4, 'assistant', '\"作为第一次养宠物的新手，您需要了解以下几个关键方面：\\n\\n【心理准备】\\n领养宠物是一项长期的承诺，通常意味着10-20年的陪伴责任。请确保您已经准备好投入时间、精力和情感来照顾一个生命。\\n\\n【物质准备】\\n在迎接宠物回家前，建议准备好以下基本用品：\\n- 适合宠物的食物和饮水器\\n- 舒适的休息空间（窝或笼子）\\n- 必要的卫生用品（如猫砂盆、牵引绳等）\\n- 玩具和磨牙/磨爪工具\\n- 基础护理工具（梳子、指甲剪等）\\n\\n【健康管理】\\n新宠物到家后，建议尽快安排首次健康检查，并注意：\\n- 按时接种疫苗\\n- 定期进行体内外驱虫\\n- 建立健康档案\\n- 了解附近可靠的宠物医院\\n\\n【适应期关怀】\\n宠物刚到新家可能会感到紧张，建议：\\n- 给予安静的适应空间\\n- 逐步建立信任关系\\n- 保持规律的作息\\n- 耐心观察行为变化\\n\\n【温馨提示】\\n我们建议您先从阅读《领养前的准备工作》和《新宠物到家的第一周》这两篇指南开始，它们能为您提供更详细的实用建议。如果您已经有心仪的宠物类型，也可以告诉我，我可以为您提供更具体的护理建议。\"', NULL, NULL, NULL, '2025-12-06 00:08:53', '2025-12-06 00:08:53', '2025-12-06 00:08:53', 0);
INSERT INTO `t_conversation_history` VALUES (24, 'af66b498a4e84820a44502181b0cb73c', 4, 'assistant', '\"目前我们系统中暂未找到标记为\\\"可爱\\\"性格的可领养小狗。不过，\\\"可爱\\\"是很多狗狗的普遍特质，我可以为您推荐一些适合家庭、性格温顺且外貌讨喜的小型犬种。\\n\\n让我为您重新查询一些适合新手、性格友善的可领养小狗：\\n\\n您好！看到您喜欢可爱的小狗，我很高兴为您推荐几位正在等待温暖家庭的毛孩子。他们都性格各异，但都拥有可爱的特质。\\n\\n🐾 **拉拉 - 拉布拉多寻回犬**\\n活泼友善的黄色小天使，性格温顺，特别喜欢和人互动，对小孩特别有耐心。已绝育并完成疫苗接种，非常适合家庭饲养，是优秀的家庭伴侣犬。\\n\\n🐾 **泰迪 - 泰迪犬**\\n一只毛发卷曲蓬松的棕色小可爱，聪明伶俐又粘人，非常适合作为陪伴犬。已绝育并接种疫苗，身体健康，需要主人每天给予陪伴和玩耍时间。\\n\\n🐾 **小柴 - 柴犬**\\n赤色毛发的标准柴犬，笑容治愈人心。性格独立但忠诚，活泼好动，喜欢户外活动。已绝育并完成疫苗接种，需要每天至少1小时的户外运动。\\n\\n🐾 **二哈 - 哈士奇**\\n拥有标志性蓝眼睛的黑白帅气小伙，精力充沛，喜欢奔跑。虽然有点调皮，但性格友善。已绝育并完成疫苗接种，适合有经验的主人。\\n\\n🐾 **金金 - 金毛**\\n四岁的温柔大男孩，性格活泼，体重15公斤左右，健康状况良好。是一只典型的金毛暖男，喜欢与人亲近。\\n\\n🐾 **边边 - 边牧犬**\\n四岁的聪明边牧，黑白相间的毛色，精力旺盛。和小高一样皮实，需要主人有足够时间陪伴和训练。\\n\\n🐾 **小博 - 博美犬**\\n三岁的白色小萌物，性格温顺可爱，体重10公斤，是一只标准的\\\"小狐狸\\\"模样，非常讨人喜爱。\\n\\n【温馨提示】\\n每只狗狗都有独特的个性和需求，建议您根据自己的生活方式选择最适合的伙伴。领养前可以预约见面，让我们共同为这些可爱的毛孩子找到最温暖的家！\\n\\n如果您对某位小伙伴特别感兴趣，我可以为您提供更多详细信息或安排见面哦！\"', NULL, NULL, NULL, '2025-12-06 12:08:02', '2025-12-06 12:08:02', '2025-12-06 12:08:02', 0);
INSERT INTO `t_conversation_history` VALUES (25, 'af66b498a4e84820a44502181b0cb73c', 4, 'assistant', '\"您好！看到您在寻找粘人的小猫，我特别为您找到了一位非常合适的小伙伴：\\n\\n🐾 **小暹 - 暹罗猫**\\n性格活泼粘人，喜欢和主人交流，叫声独特，需要陪伴。这是一只优雅的蓝色眼睛重点色猫咪，已绝育并完成疫苗接种。\\n适合喜欢互动、有时间陪伴的主人，不适合长时间独处。\\n\\n【温馨提示】\\n暹罗猫是非常社交化的品种，他们渴望主人的关注和互动。建议每天至少安排30分钟的玩耍和陪伴时间，为他们提供丰富的玩具和活动空间。\\n\\n如果您想了解更多关于小暹的信息，或者想要预约见面，请随时告诉我！让我们一起为这位可爱的小天使找到温暖的家~\"', NULL, NULL, NULL, '2025-12-06 12:08:24', '2025-12-06 12:08:24', '2025-12-06 12:08:24', 0);
INSERT INTO `t_conversation_history` VALUES (26, '107df6e5c49144dd8861801503d80332', 4, 'assistant', '\"您好！我正在为您查找热情活泼且正在等待领养的小狗，稍等片刻...\\n\\n看起来系统暂时没有返回相关结果，可能是筛选条件需要调整。让我为您扩大搜索范围，查找更多可领养的狗狗：\\n\\n目前系统中暂无明确标注为\\\"热情\\\"性格的小狗信息。不过，我很乐意为您推荐一些正在等待领养的活泼可爱的小狗！\\n\\n我理解您想要一只热情的小狗，虽然目前系统中没有明确标注\\\"热情\\\"性格的小狗，但我为您找到了几只性格活泼、非常适合互动的可领养小狗：\\n\\n🐾 **金金 - 金毛**\\n活泼开朗的金毛犬，天性友善亲人，喜欢与人互动和玩耍。金毛犬以热情好客著称，是非常理想的家庭伴侣犬。\\n\\n🐾 **边边 - 边牧犬**\\n活泼聪明的边境牧羊犬，精力充沛且极具互动性。边牧需要较多的运动和智力刺激，适合喜欢户外活动的主人。\\n\\n【温馨提示】\\n狗狗的\\\"活泼\\\"性格往往意味着它们需要较多的陪伴和运动时间。建议您考虑每天是否有足够的时间陪狗狗散步、玩耍和训练。\\n\\n如果您希望了解更多关于这些小狗的信息，或者想了解其他可领养的宠物，我很乐意为您提供帮助！\"', NULL, NULL, NULL, '2025-12-06 22:01:51', '2025-12-06 22:01:51', '2025-12-06 22:01:51', 0);
INSERT INTO `t_conversation_history` VALUES (27, 'b2005e12fe164ea98ba181dc8af40760', 7, 'assistant', '\"我正在为您查找性格安静、适合安静生活环境的可领养宠物。请稍等片刻，我将为您提供最合适的推荐。\\n\\n🐾 **米米 - 英国短毛猫**  \\n性格温顺安静，喜欢安静地陪伴在主人身边，不需要过多吵闹的互动。  \\n适合喜欢宁静生活、希望有个温柔伴侣的您。\\n\\n🐾 **小灰 - 安哥拉兔**  \\n性情温和，动作轻柔，很少发出声音，是非常理想的室内安静宠物。  \\n适合居住在公寓或需要低噪音环境的家庭。\\n\\n🐾 **布丁 - 彭布罗克威尔士柯基（幼犬）**  \\n虽然柯基通常较活跃，但布丁性格沉稳，经过初步训练后表现得非常安静有礼。  \\n适合希望拥有小狗但又偏好安静氛围的领养人。\\n\\n🐾 **墨墨 - 美国短毛猫**  \\n独立性强，不会频繁叫唤，日常活动安静，自我娱乐能力强。  \\n特别适合工作繁忙或喜欢安静独处的人士。\\n\\n【温馨提示】  \\n安静的性格也需良好的环境来维持，建议为宠物提供稳定的生活节奏和舒适的空间。如果您希望进一步了解某只宠物的详细情况，我可以为您获取更多资讯。\"', NULL, NULL, NULL, '2025-12-19 12:16:16', '2025-12-19 12:16:16', '2025-12-19 12:16:16', 0);
INSERT INTO `t_conversation_history` VALUES (28, '6a353491c5e440938f5f25bbd927bc4e', 8, 'assistant', '\"[{\\\"id\\\":9,\\\"category\\\":\\\"STORY\\\",\\\"title\\\":\\\"被遗弃的希望\\\",\\\"summary\\\":\\\"暴雨天被弃桥洞的小狗小黑遭外卖小哥救助后重拾信任，如今成骑手身边的小明星，以经历诠释温暖能抵过伤害。\\\",\\\"content\\\":\\\"# 从绝望到新生\\\\n\\\\n小黑是一只在暴雨天被遗弃在桥洞下的小狗。那天，它全身湿透，瑟瑟发抖，眼神中满是绝望与无助。\\\\n\\\\n## 意外的温暖\\\\n\\\\n一位路过的外卖小哥发现了小黑。他撑起伞，小心地抱起它，用身上的外套为它取暖。虽然小黑已经几乎失去了力气，但它依然努力摇着尾巴回应善意。\\\\n\\\\n## 重拾信任\\\\n\\\\n在随后的一个月里，小哥带着小黑去看医生，给它洗澡、梳毛，陪它散步。起初，小黑害怕接触人类，每次看到陌生人都会躲进桌子底下。但一次次温柔的抚摸与耐心的鼓励，让它重新感受到了安全。\\\\n\\\\n## 新的家\\\\n\\\\n如今，小黑已经成为骑手中的小明星。它每天都跟着主人跑单，见到熟悉的店家会主动摇尾巴问好。它的故事告诉我们：*世界上再多伤害，也抵不过一次真正的温暖。*\\\\n\\\",\\\"coverImage\\\":\\\"http://localhost:9000/animal-adopt/images/f4cfb61a6f934691ab36699fd35885b8.png\\\",\\\"guideCategory\\\":null,\\\"viewCount\\\":20,\\\"author\\\":\\\"外卖小哥\\\",\\\"tags\\\":[\\\"温暖救助\\\",\\\"生命救赎\\\"],\\\"likeCount\\\":2,\\\"favoriteCount\\\":1,\\\"liked\\\":false,\\\"favorited\\\":false,\\\"publishDate\\\":\\\"2025-11-26\\\",\\\"publishTime\\\":[2025,11,26,21,22,41],\\\"relationTime\\\":null}]\"', NULL, NULL, NULL, '2025-12-21 20:57:47', '2025-12-21 20:57:47', '2025-12-21 20:57:47', 0);
INSERT INTO `t_conversation_history` VALUES (29, '6a353491c5e440938f5f25bbd927bc4e', 8, 'assistant', '\"您好！欢迎来到i宠园，很高兴您对宠物领养感兴趣。作为首次领养者，选择一只性格温顺、易于照顾的宠物会更容易建立信心和亲密关系。\\n\\n根据系统中当前最受欢迎且适合新手的可领养宠物，我为您精选了以下几位可爱的小家伙：\\n\\n🐾 **小博 - 小狗**\\n温顺可爱的博美犬，性格温和亲人，适应力强，非常适合初次养宠的家庭。\\n\\n🐾 **布布 - 小猫**\\n调皮但可爱的布偶猫，虽然活泼但很聪明，容易与人建立感情，是理想的伴侣宠物。\\n\\n🐾 **小狸 - 中华狸花猫**\\n已绝育并完成疫苗接种的帅气狸花猫，性格独立但也亲人，身体强壮，适应能力强，是优秀的家庭成员。\\n\\n🐾 **仓仓 - 仓鼠**\\n胆小谨慎的白色仓鼠，体型小巧，所需空间小，适合居住在公寓或空间有限的环境。\\n\\n【温馨提示】  \\n我们建议您先阅读《领养前的准备工作》和《新宠物到家的第一周》这两篇指南，帮助您更好地迎接新成员。每一份生命都值得被认真对待，感谢您选择用爱为它们点亮未来。\\n\\n如果您对某位小伙伴特别感兴趣，我可以为您提供更详细的资料或安排见面哦！\"', NULL, NULL, NULL, '2025-12-21 21:17:44', '2025-12-21 21:17:44', '2025-12-21 21:17:44', 0);
INSERT INTO `t_conversation_history` VALUES (30, '6a353491c5e440938f5f25bbd927bc4e', 8, 'assistant', '\"您的担心非常理解，房子面积确实会影响宠物的舒适度和幸福感，但并不意味着小房子就不能养宠物。关键在于「匹配」——选择适合您居住环境的宠物。\\n\\n我可以为您查询系统中适合公寓或小户型家庭的宠物推荐列表，比如性格温顺、活动量适中的猫或小型犬。\\n\\n【温馨提示】  \\n我们始终倡导「合适比喜欢更重要」。i宠园的每一只待领养宠物都经过行为评估，我们会根据您的生活方式（如住房类型、作息时间等）来推荐最合适的伴侣，确保它们能真正拥有一个稳定的家。\\n\\n如果您愿意，可以告诉我：\\n- 您的住房类型（如：单身公寓 / 一居室 / 有院子的房子 等）\\n- 日常在家的时间长短\\n\\n我将为您精准匹配几位适合的小可爱 ❤️\"', NULL, NULL, NULL, '2025-12-21 21:19:31', '2025-12-21 21:19:31', '2025-12-21 21:19:31', 0);
INSERT INTO `t_conversation_history` VALUES (31, '6a353491c5e440938f5f25bbd927bc4e', 8, 'assistant', '\"不客气！如果您有任何关于宠物领养的问题，比如想了解可领养的宠物、申请流程、或者如何为新成员做准备，随时都可以问我。我会全力为您解答！\\n\\n愿每一位毛孩子都能遇见温暖的家，也祝您早日找到那个与您心灵契合的小伙伴 ❤️\"', NULL, NULL, NULL, '2025-12-21 21:20:53', '2025-12-21 21:20:53', '2025-12-21 21:20:53', 0);
INSERT INTO `t_conversation_history` VALUES (32, 'c6db7872f7644f32b2182e9f91c2a4cc', 7, 'assistant', '\"您好！欢迎来到i宠园，我是您的智能客服助手。  \\n很高兴为您服务！\\n\\n如果您正在寻找一个可爱的宠物伙伴，或者有关于领养、护理、训练的任何疑问，都可以告诉我哦～  \\n我可以帮您推荐最适合的宠物，解答新手常见问题，或指导您完成领养流程。\\n\\n🐾 您想了解些什么呢？\"', NULL, NULL, NULL, '2025-12-21 22:46:10', '2025-12-21 22:46:10', '2025-12-21 22:46:10', 0);
INSERT INTO `t_conversation_history` VALUES (33, 'c6db7872f7644f32b2182e9f91c2a4cc', 7, 'assistant', '\"当然可以！对于第一次养宠物的朋友，了解一些基础知识非常重要。以下是新手领养人需要掌握的核心内容：\\n\\n- **基础护理知识**：包括日常饮食管理、清洁梳理、定期驱虫和疫苗接种\\n- **行为习惯理解**：学习如何识别宠物的情绪（如害怕、兴奋、不适），建立良好的互动方式\\n- **环境准备**：为宠物准备好安全舒适的生活空间，例如猫砂盆、狗窝、活动区域等\\n- **时间与责任投入**：宠物需要长期陪伴和照顾，确保您有足够的时间遛狗、陪玩或处理突发情况\\n- **经济预算规划**：除了食物开销，还需考虑医疗、美容、玩具等年度支出\\n\\n【温馨提示】  \\n建议在正式领养前，先通过我们的《新手养宠入门指南》系统文章深入学习。我可以为您查询相关内容。\\n\\n是否需要我为您推荐一篇图文并茂的新手指南？\"', NULL, NULL, NULL, '2025-12-21 23:53:45', '2025-12-21 23:53:45', '2025-12-21 23:53:45', 0);
INSERT INTO `t_conversation_history` VALUES (34, '88453c2e04f54a53a28f2bca2eada630', 7, 'assistant', '\"当然可以！欢迎你迈出了解宠物领养的第一步，这是一件非常温暖且有意义的事。在决定领养之前，以下是一些新手需要提前了解的重要事项：\\n\\n- **时间投入**：宠物需要陪伴和照顾，每天都要花时间喂食、清理、遛狗（如果是狗）、互动玩耍等。即使是最独立的猫咪，也需要定期关注其健康和情绪。\\n\\n- **经济成本**：养宠物并非一次性支出。你需要准备日常开销（如食物、猫砂）、定期医疗（疫苗、驱虫、体检）以及可能的突发治疗费用。建议每月预留一定的宠物养护预算。\\n\\n- **居住环境**：评估你的住所是否适合养宠物。比如，高层公寓是否便于遛狗？是否有足够的空间让宠物活动？房东是否允许养宠？\\n\\n- **长期承诺**：宠物不是短期陪伴，一只猫可能陪你15年以上，狗狗也通常有10-15年的寿命。请确保你能为它的一生负责，不因搬家、结婚或工作变动而轻易放弃。\\n\\n- **学习基础护理知识**：了解如何正确喂养、清洁、识别疾病征兆、进行基础训练等，是每位新手主人的必修课。\\n\\n- **心理准备**：宠物可能会拆家、半夜跑酷、乱尿标记……这些行为都需要耐心引导，而不是责罚。科学训练和正向激励更重要。\\n\\n【温馨提示】  \\n如果你希望系统提供更详细的图文指南，我可以为你查找《新手养宠入门手册》等相关资料。是否需要我为你获取这份指南？\"', NULL, NULL, NULL, '2025-12-22 14:12:12', '2025-12-22 14:12:12', '2025-12-22 14:12:12', 0);
INSERT INTO `t_conversation_history` VALUES (35, '88453c2e04f54a53a28f2bca2eada630', 7, 'assistant', '\"谢谢你对宠物领养的关注！作为i宠园的客服助手，我很高兴为你提供支持。\\n\\n虽然你之前提到没有养过宠物，但你已经迈出了负责任的第一步——提前了解相关知识。这非常重要！\\n\\n根据系统资料，我们有一篇非常实用的指南可以帮助你更好地准备：\\n\\n📄 **《基础服从训练技巧》**  \\n这篇图文指南介绍了如何通过正向激励的方式，教会宠物基本指令（如“坐下”、“过来”等），帮助你未来与毛孩子建立良好的互动关系。\\n\\n如果你正在考虑领养第一只宠物，我建议可以从性格温顺、适应力强的成年猫狗开始，它们通常比幼崽更容易照顾，行为也更稳定。\\n\\n【温馨提示】  \\n当你准备好进一步了解可领养的宠物时，可以告诉我你的生活方式（例如：住公寓还是带院子的房子？平时工作忙吗？家里有小孩吗？），我会为你推荐最合适的毛孩子。\\n\\n愿每一位主人都能与宠物彼此陪伴，温暖同行 🐾\"', NULL, NULL, NULL, '2025-12-22 14:12:23', '2025-12-22 14:12:23', '2025-12-22 14:12:23', 0);
INSERT INTO `t_conversation_history` VALUES (36, '88453c2e04f54a53a28f2bca2eada630', 7, 'assistant', '\"谢谢你分享你的生活环境和期待！你住在公寓、工作较忙但希望有一只热情的小动物陪伴，这样的需求很常见，也非常重要。\\n\\n虽然系统当前暂无完全匹配的推荐宠物列表，但我依然可以给你一些适合你情况的专业建议：\\n\\n- **推荐宠物类型**：对于公寓生活且工作忙碌的人来说，成年猫咪或小型犬（如比熊、柯基、法国斗牛犬）通常是不错的选择。它们对空间要求不高，且经过一段时间磨合后能成为非常贴心的情绪伴侣。\\n\\n- **情绪价值小贴士**：热情又粘人的宠物往往更懂得“察言观色”，比如回家时迎接你、趴在身边陪你加班等。选择性格标签为【亲人】【活泼】【适应力强】的宠物会更容易带来温暖感受。\\n\\n- **时间管理建议**：即使工作忙，每天抽出15-30分钟专心陪宠（玩耍、抚摸、互动），也能大大增强感情联结，同时帮助宠物释放压力。\\n\\n【温馨提示】  \\n建议你定期关注i宠园的可领养名单，新宠物会持续更新。如果你愿意，我可以为你开启“匹配提醒”服务——一旦有适合你生活方式的热情型宠物上线，第一时间通知你！\\n\\n愿你在忙碌生活中，早日遇见那个等你回家的小身影 🐾\"', NULL, NULL, NULL, '2025-12-22 14:13:06', '2025-12-22 14:13:06', '2025-12-22 14:13:06', 0);
INSERT INTO `t_conversation_history` VALUES (37, 'c52ab023ff664d28807fbd8cbe7e7a63', 1, 'assistant', '\"您好！欢迎来到i宠园，很高兴您正在考虑领养人生中的第一只宠物 🐾  \\n作为新手，选择一只性格温顺、容易照顾的宠物会更有利于建立信心和良好的互动关系。\\n\\n为了给您提供最合适的推荐，我将为您查询系统中**适合新手的可领养宠物**，优先筛选性格稳定、护理难度较低的小伙伴。\\n\\n您好！欢迎来到i宠园，很高兴您愿意开启养宠之旅。作为新手，选择一只性格温顺、易于照顾的宠物非常重要。\\n\\n根据系统推荐，这里有几只非常适合新手的可爱伙伴：\\n\\n🐾 **小博 - 博美犬**\\n性格温顺，体型小巧，对主人非常依赖且容易建立亲密关系，是理想的家庭伴侣犬。\\n\\n🐾 **布布 - 布偶猫**\\n调皮但亲人，布偶猫以“温柔如布偶”著称，适应力强，喜欢与人互动，适合室内饲养。\\n\\n🐾 **仓仓 - 仓鼠**\\n胆小谨慎但生活需求简单，适合想先从小型宠物开始尝试的新手，便于观察和学习照顾技巧。\\n\\n🐾 **小狸 - 中华狸花猫**\\n已绝育并完成疫苗接种，性格独立但也亲人，身体强壮、适应能力强，是优秀的家庭捕鼠能手。\\n\\n【温馨提示】  \\n首次养宠建议优先考虑已接种疫苗、健康状况良好的宠物，并确保有足够的时间和精力陪伴它们适应新环境。\\n\\n\"', NULL, NULL, NULL, '2026-01-03 00:47:19', '2026-01-03 00:47:19', '2026-01-03 00:47:19', 0);
INSERT INTO `t_conversation_history` VALUES (38, 'c52ab023ff664d28807fbd8cbe7e7a63', 1, 'assistant', '\"您好！欢迎来到i宠园，很高兴您正在考虑领养人生中的第一只宠物 🐾  \\n作为新手，选择一只性格温顺、容易照顾的宠物会更有利于建立信心和良好的互动关系。\\n\\n根据系统中当前可领养的宠物数据，我为您精选了以下几位非常适合新手的小伙伴：\\n\\n🐾 **小博 - 博美犬**  \\n性格温顺，体型小巧，对主人非常依赖且容易建立亲密关系，是理想的家庭伴侣犬。\\n\\n🐾 **布布 - 布偶猫**  \\n调皮但亲人，布偶猫以“温柔如布偶”著称，适应力强，喜欢与人互动，适合室内饲养。\\n\\n🐾 **仓仓 - 仓鼠**  \\n胆小谨慎但生活需求简单，适合想先从小型宠物开始尝试的新手，便于观察和学习照顾技巧。\\n\\n🐾 **小狸 - 中华狸花猫**  \\n已绝育并完成疫苗接种，性格独立但也亲人，身体强壮、适应能力强，是优秀的家庭捕鼠能手。\\n\\n【温馨提示】  \\n首次养宠建议优先考虑健康状况良好、已接种疫苗或已完成绝育的宠物。同时，我们建议您先阅读系统内的《基础服从训练技巧》等新手指南，帮助您更快上手照顾新伙伴。\\n\\n如果您对某位宠物感兴趣，可以查看详情并提交领养申请，我们的工作人员将尽快与您联系。祝您早日找到心仪的毛孩子！\"', NULL, NULL, NULL, '2026-01-03 01:07:21', '2026-01-03 01:07:21', '2026-01-03 01:07:21', 0);
INSERT INTO `t_conversation_history` VALUES (39, '9c993653047b4eb19342bfaa63d2de9c', 1, 'assistant', '\"👋 欢迎来到i宠园AI客服！\\n\\n我是您的宠物领养顾问，很高兴为您服务。我可以帮助您：\\n\\n🐱 **宠物推荐** - 根据您的需求推荐合适的宠物\\n  例如：\\\"有什么推荐的宠物吗？\\\"\\n  例如：\\\"我想要一只活泼的小狗\\\"\\n\\n👤 **性格匹配** - 根据您的性格推荐宠物\\n  例如：\\\"我性格内向，适合养什么宠物？\\\"\\n  例如：\\\"我是个很忙的上班族\\\"\\n\\n🏠 **生活方式** - 根据您的生活环境推荐宠物\\n  例如：\\\"我住在公寓里，有什么推荐吗？\\\"\\n  例如：\\\"我有一个小孩，想养一只宠物\\\"\\n\\n📚 **护理指南** - 提供宠物护理建议\\n  例如：\\\"怎样照顾小猫？\\\"\\n  例如：\\\"新手养狗要注意什么？\\\"\\n\\n❓ **其他问题** - 回答关于宠物的各种问题\\n  例如：\\\"宠物需要打疫苗吗？\\\"\\n  例如：\\\"宠物生病了怎么办？\\\"\\n\\n请告诉我您的需求，我会为您提供最好的建议！\"', NULL, NULL, NULL, '2026-01-28 21:55:21', '2026-01-28 21:55:21', '2026-01-28 21:55:21', 0);
INSERT INTO `t_conversation_history` VALUES (40, '9c993653047b4eb19342bfaa63d2de9c', 1, 'assistant', '\"很高兴你考虑开启养宠之旅！作为新手，选择一只性格温顺、适应力强、护理需求相对简单的宠物，能大大提升领养体验和成功率 🌟\\n\\n我来为你推荐几只**当前系统中可领养、特别适合新手的宠物**（已筛选：性格稳定、健康状况良好、已有基础社会化训练）：\\n\\n很高兴你愿意开启养宠之旅！作为新手，选择一只性格温和、适应力强、护理需求相对简单的宠物，能大大提升你的信心和幸福感 🌟\\n\\n以下是当前系统中**特别适合新手领养的宠物推荐**（已综合考虑性格稳定性、日常护理难度、互动友好度等因素）：\\n\\n🐾 **小狸 - 中华狸花猫**  \\n性格独立机警，但也亲人；已绝育+完成疫苗接种，健康有保障；身体强壮、适应力强，对环境变化耐受度高，非常适合首次养猫的家庭。  \\n适合：希望拥有安静陪伴、生活节奏较规律、能提供基础猫具（猫砂盆/爬架）的新手。\\n\\n🐾 **小博 - 博美犬**  \\n温顺亲人、体型小巧、运动需求适中；毛发打理相对简单（定期梳理即可），对公寓生活友好。  \\n适合：有固定陪伴时间、喜欢轻度互动、居住空间有限但希望有狗狗陪伴的新手。\\n\\n🐾 **仓仓 - 仓鼠**  \\n胆小谨慎、饲养成本低、空间需求极小（一个合适笼子即可）、日常照料简单（清洁+喂食）。  \\n适合：想先体验责任与陪伴、时间或空间受限、或希望从“低压力”开始建立养宠信心的新手。\\n\\n🐾 **布布 - 布偶猫**  \\n调皮可爱、社交性强、对人类信任度高；虽活泼但不具攻击性，幼龄期已社会化良好，易建立亲密关系。  \\n适合：有稳定居家时间、喜欢温柔互动、愿意学习猫咪行为语言的新手家庭。\\n\\n🐾 **金金 - 金毛寻回犬**  \\n天生友善、耐心包容、学习能力强；虽然体型较大，但性格极其稳定，是公认的新手友好犬种。  \\n适合：有户外活动条件（如附近有公园）、家人共同参与照顾、重视长期情感陪伴的新手家庭。\\n\\n【温馨提示】  \\n✅ 所有推荐宠物均为「可领养」状态，信息真实有效（数据截至今日）  \\n✅ 领养前我们会安排「新手养宠指导课」+「家庭环境评估」，全程免费支持  \\n✅ 若你更倾向某类动物（如只想养猫/狗），或有具体生活情况（如租房、有小孩、工作忙碌等），欢迎告诉我，我可以为你进一步精准筛选 ❤️  \\n\\n需要我帮你查看某只宠物的详细资料、预约见面，或发送《新手养宠入门指南》图文版吗？ 😊\"', NULL, NULL, NULL, '2026-01-28 21:55:21', '2026-01-28 21:55:21', '2026-01-28 21:55:21', 0);
INSERT INTO `t_conversation_history` VALUES (41, '91ecdef2699f47f38bc3d4dcd0f795d4', 29, 'assistant', '\"👋 欢迎来到i宠园AI客服！\\n\\n我是您的宠物领养顾问，很高兴为您服务。我可以帮助您：\\n\\n🐱 **宠物推荐** - 根据您的需求推荐合适的宠物\\n  例如：\\\"有什么推荐的宠物吗？\\\"\\n  例如：\\\"我想要一只活泼的小狗\\\"\\n\\n👤 **性格匹配** - 根据您的性格推荐宠物\\n  例如：\\\"我性格内向，适合养什么宠物？\\\"\\n  例如：\\\"我是个很忙的上班族\\\"\\n\\n🏠 **生活方式** - 根据您的生活环境推荐宠物\\n  例如：\\\"我住在公寓里，有什么推荐吗？\\\"\\n  例如：\\\"我有一个小孩，想养一只宠物\\\"\\n\\n📚 **护理指南** - 提供宠物护理建议\\n  例如：\\\"怎样照顾小猫？\\\"\\n  例如：\\\"新手养狗要注意什么？\\\"\\n\\n❓ **其他问题** - 回答关于宠物的各种问题\\n  例如：\\\"宠物需要打疫苗吗？\\\"\\n  例如：\\\"宠物生病了怎么办？\\\"\\n\\n请告诉我您的需求，我会为您提供最好的建议！\"', NULL, NULL, NULL, '2026-01-31 22:12:08', '2026-01-31 22:12:08', '2026-01-31 22:12:08', 0);
INSERT INTO `t_conversation_history` VALUES (42, '91ecdef2699f47f38bc3d4dcd0f795d4', 29, 'assistant', '\"很高兴你考虑开启养宠之旅！作为新手，选择一只性格温顺、适应力强、护理需求相对简单的宠物，确实能让这段关系更温暖、更顺利 🌟\\n\\n我来为你推荐几只目前**系统中可领养、适合新手、亲人度高、易于照顾**的宠物（已筛选「available」状态，性格关键词含「温顺」「亲人」「适应力强」）：\\n\\n看起来当前系统中暂时没有完全匹配“温顺、亲人、适应力强”且标记为「可领养」的宠物数据。不过别担心——这并不意味着没有适合新手的好伙伴！作为i宠园的领养顾问，我很乐意为你提供专业、温暖的建议：\\n\\n🐾 **新手友好型宠物推荐（基于普遍饲养经验与动物行为学）**  \\n✅ **成年已绝育的猫咪（2岁以上）**  \\n- 通常性格稳定、独立但不冷漠，会主动亲近信任的人  \\n- 日常护理相对简单：猫砂盆+定期梳毛+基础疫苗驱虫即可  \\n- 比幼猫更少突发行为问题（如过度啃咬、夜嚎），更适合零经验家庭  \\n\\n✅ **小型犬中的“稳重型”品种（如柯基、比熊、巴哥）**  \\n- 若选择已社会化训练过的成年犬（1–3岁），对新手更友好  \\n- 建议优先考虑【曾与儿童/老人共处过】或【有寄养记录】的个体——说明其情绪稳定、适应力强  \\n\\n✅ **兔子/豚鼠（需注意：需专用笼舍+草粮+定期剪指甲）**  \\n- 性情温和、安静，适合公寓生活  \\n- 虽不如猫狗粘人，但通过每日轻柔互动，也能建立明显依恋关系  \\n\\n📌 **特别提醒新手的3个关键准备**：  \\n1️⃣ 不要只看“可爱”，优先关注宠物是否已完成【基础健康检查+疫苗+驱虫+绝育】（i宠园所有待领养宠物均满足此标准）  \\n2️⃣ 领养前务必确认自己能承诺【至少10–15年的陪伴】——这是对生命最基本的尊重  \\n3️⃣ i宠园提供【首月免费线上咨询+新手礼包+社区互助群】，全程支持你平稳过渡  \\n\\n需要我帮你：  \\n🔹 查看当前最新可领养的成年猫/狗名单？  \\n🔹 获取《新手养猫/养狗第一步》图文指南？  \\n🔹 或根据你的居住环境（如：有无阳台、是否常出差、家中是否有小孩）做个性化推荐？  \\n\\n随时告诉我，我会为你精准匹配 ❤️\"', NULL, NULL, NULL, '2026-01-31 22:12:08', '2026-01-31 22:12:08', '2026-01-31 22:12:08', 0);
INSERT INTO `t_conversation_history` VALUES (43, '5d49edac6af3497f85eafd32ab0bb7f0', 32, 'assistant', '\"👋 欢迎来到i宠园AI客服！\\n\\n我是您的宠物领养顾问，很高兴为您服务。我可以帮助您：\\n\\n🐱 **宠物推荐** - 根据您的需求推荐合适的宠物\\n  例如：\\\"有什么推荐的宠物吗？\\\"\\n  例如：\\\"我想要一只活泼的小狗\\\"\\n\\n👤 **性格匹配** - 根据您的性格推荐宠物\\n  例如：\\\"我性格内向，适合养什么宠物？\\\"\\n  例如：\\\"我是个很忙的上班族\\\"\\n\\n🏠 **生活方式** - 根据您的生活环境推荐宠物\\n  例如：\\\"我住在公寓里，有什么推荐吗？\\\"\\n  例如：\\\"我有一个小孩，想养一只宠物\\\"\\n\\n📚 **护理指南** - 提供宠物护理建议\\n  例如：\\\"怎样照顾小猫？\\\"\\n  例如：\\\"新手养狗要注意什么？\\\"\\n\\n❓ **其他问题** - 回答关于宠物的各种问题\\n  例如：\\\"宠物需要打疫苗吗？\\\"\\n  例如：\\\"宠物生病了怎么办？\\\"\\n\\n请告诉我您的需求，我会为您提供最好的建议！\"', NULL, NULL, NULL, '2026-02-01 20:57:12', '2026-02-01 20:57:12', '2026-02-01 20:57:12', 0);
INSERT INTO `t_conversation_history` VALUES (44, '5d49edac6af3497f85eafd32ab0bb7f0', 32, 'assistant', '\"你好呀～欢迎来到i宠园！🐾  \\n很高兴你愿意开启养宠之旅，这份初心特别温暖。作为新手，选择一只性格温和、适应力强、护理需求相对简单的宠物，确实能让你更轻松地建立信任与陪伴关系。\\n\\n我来为你推荐几位目前**系统中可领养、特别适合新手的毛孩子**（已筛选：性格温顺/亲人/低攻击性/基础护理友好）：\\n\\n你好！非常感谢你对领养宠物的认真态度——作为新手，主动思考“什么宠物更适合我”，这已经是负责任养宠的第一步 🌟\\n\\n从系统当前可领养的宠物中，我们为你筛选出几只**性格温和、护理需求适中、适合新手入门**的可爱伙伴。结合它们的真实信息，为你推荐如下：\\n\\n🐾 **仓仓 - 仓鼠**  \\n性格胆小、谨慎，适应安静环境，日常互动温和不具攻击性；体型小、空间需求低（一个合适笼子即可），饮食简单（专用鼠粮+少量蔬果），非常适合首次养宠、居住空间有限或想先建立照顾责任感的朋友。  \\n适合：公寓生活、希望低门槛体验养宠、喜欢观察型陪伴。\\n\\n🐾 **test - 兔子（多只同名待选）**  \\n虽然详情描述较简略，但兔子普遍温顺、爱干净、可训练如厕，成年后性格稳定；相比狗狗，运动量和时间投入更可控，适合规律作息的新手。建议选择已绝育、2岁以上个体，性格更沉稳。  \\n适合：有固定活动区域（如围栏式兔区）、愿意学习草粮搭配与牙齿护理的新手。\\n\\n🐾 **1 - 小鸟（品种未详，待补充）**  \\n体型小巧、鸣声悦耳，互动趣味性强；若为文鸟、玄凤等常见入门鸟种，通常亲人性好、易建立信任。需注意笼具安全、避免油烟与空调直吹。  \\n适合：喜欢轻量互动、有窗台/阳台空间、愿花时间耐心陪伴的新手。\\n\\n⚠️ 温馨提示：  \\n目前系统中部分宠物信息（如名字、品种、性格）尚待志愿者完善，我们正积极优化数据质量。如果你对某只宠物特别心动，欢迎点击查看详情 → 提交【预咨询】，我们的领养顾问将为你同步最新情况并提供一对一指导！\\n\\n📌 新手小贴士（无需工具，直接送你）：  \\n✅ 养宠前必做三件事：评估每日可陪伴时间、确认住所是否允许养宠、预留首年基础开支（疫苗/驱虫/口粮/基础用品约2000–4000元）  \\n✅ 推荐先阅读《新手养宠完全指南》→ 我可立刻为你调取图文版  \\n✅ 所有领养均需签署《责任承诺书》，我们会全程陪伴你完成科学过渡  \\n\\n需要我为你：\\n🔹 调取《新手养宠完全指南》图文版？  \\n🔹 查询「仓鼠/兔子」专属护理要点？  \\n🔹 了解领养全流程与审核要求？  \\n\\n随时告诉我，我在这里，陪你一起迈出温暖又踏实的第一步 🐾\"', NULL, NULL, NULL, '2026-02-01 20:57:12', '2026-02-01 20:57:12', '2026-02-01 20:57:12', 0);

-- ----------------------------
-- Table structure for t_conversation_session
-- ----------------------------
DROP TABLE IF EXISTS `t_conversation_session`;
CREATE TABLE `t_conversation_session`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `session_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '会话ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '会话标题',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '会话描述',
  `message_count` int NULL DEFAULT 0 COMMENT '消息数量',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'active' COMMENT '状态',
  `last_message` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '最后一条消息',
  `last_message_time` datetime NULL DEFAULT NULL COMMENT '最后消息时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `closed_time` datetime NULL DEFAULT NULL COMMENT '关闭时间',
  `deleted` tinyint NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_session_id`(`session_id` ASC) USING BTREE,
  INDEX `idx_user_id_status`(`user_id` ASC, `status` ASC, `update_time` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 41 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'AI客服会话表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_conversation_session
-- ----------------------------
INSERT INTO `t_conversation_session` VALUES (1, '6ad414bd2da544f4b070317cce91193a', 1, '新对话', NULL, 0, 'active', NULL, NULL, '2025-11-23 11:50:15', '2025-11-23 11:50:15', NULL, 0);
INSERT INTO `t_conversation_session` VALUES (2, '7b46d056fc7f4d05ae6c2eb87b8399d6', 1, '新对话', NULL, 0, 'active', NULL, NULL, '2025-11-23 11:55:01', '2025-11-23 11:55:01', NULL, 0);
INSERT INTO `t_conversation_session` VALUES (3, 'e32ba0c74c574a978d53c779aa7ab329', 1, '新对话', NULL, 0, 'active', NULL, NULL, '2025-11-23 11:59:51', '2025-11-23 11:59:51', NULL, 0);
INSERT INTO `t_conversation_session` VALUES (4, '6cf8eff39da545c78df3de8c2b1ebe24', 1, '新对话', NULL, 0, 'active', NULL, NULL, '2025-11-23 12:04:20', '2025-11-23 12:04:20', NULL, 0);
INSERT INTO `t_conversation_session` VALUES (5, 'b7f0555fcc244f77b2a2e8ab55843a80', 1, '新对话', NULL, 0, 'active', NULL, NULL, '2025-11-23 12:15:33', '2025-11-23 12:15:33', NULL, 0);
INSERT INTO `t_conversation_session` VALUES (6, 'c8a52268d5084fccaf6d2633cc5d78cd', 1, '新对话', NULL, 0, 'active', NULL, NULL, '2025-11-23 12:21:06', '2025-11-23 12:21:06', NULL, 0);
INSERT INTO `t_conversation_session` VALUES (7, 'c19a9554998c4f37885d637326d90341', 1, '新对话', NULL, 0, 'active', NULL, NULL, '2025-11-23 12:22:42', '2025-11-23 12:22:42', NULL, 0);
INSERT INTO `t_conversation_session` VALUES (8, '664222a15fe94df4ab263a6f98618aa1', 1, '新对话', NULL, 0, 'active', NULL, NULL, '2025-11-23 12:24:30', '2025-11-23 12:24:30', NULL, 0);
INSERT INTO `t_conversation_session` VALUES (9, '91f9451c0bc04670b55538234f95781b', 1, '新对话', NULL, 0, 'active', NULL, NULL, '2025-11-23 12:26:16', '2025-11-23 12:26:16', NULL, 0);
INSERT INTO `t_conversation_session` VALUES (10, 'a4f9b2e83a9e4a809b1173b9e41614ce', 1, '新对话', NULL, 0, 'active', NULL, NULL, '2025-11-23 12:27:52', '2025-11-23 12:27:52', NULL, 0);
INSERT INTO `t_conversation_session` VALUES (11, 'f1aef5fe047a45ad80eaa04da0e904c6', 1, '新对话', NULL, 0, 'active', NULL, NULL, '2025-11-23 12:32:53', '2025-11-23 12:32:53', NULL, 0);
INSERT INTO `t_conversation_session` VALUES (12, '15776eeecf2e43ccb629a5c88ab1ec2f', 1, '新对话', NULL, 0, 'active', NULL, NULL, '2025-11-23 12:34:35', '2025-11-23 12:34:35', NULL, 0);
INSERT INTO `t_conversation_session` VALUES (13, '5e3d7fec817140a68f3e77a551f00719', 1, '新对话', NULL, 0, 'active', NULL, NULL, '2025-11-23 12:39:28', '2025-11-23 12:39:28', NULL, 0);
INSERT INTO `t_conversation_session` VALUES (14, 'cea28f9a97804499af28f7a2c2d5eeb2', 1, '新对话', NULL, 0, 'active', NULL, NULL, '2025-11-23 13:31:26', '2025-11-23 13:31:26', NULL, 0);
INSERT INTO `t_conversation_session` VALUES (15, 'fc0d8b8fc0844a0997af6cc6864ab5d4', 1, '新对话', NULL, 0, 'active', NULL, NULL, '2025-11-23 13:47:56', '2025-11-23 13:47:56', NULL, 0);
INSERT INTO `t_conversation_session` VALUES (16, '01adf03ac957459a97564b37676f3aa7', 1, '新对话', NULL, 0, 'active', NULL, NULL, '2025-11-23 17:20:49', '2025-11-23 17:20:49', NULL, 0);
INSERT INTO `t_conversation_session` VALUES (17, '26274b7e741a4b9ba56820c27f28a3af', 1, '新对话', NULL, 2, 'active', '当然可以！以下是i宠园目前正在等待领养的几只可爱小狗，它们性格各异，总有一款适合您：\n\n🐾 **傻蛋小高 - 萨摩耶**\n温顺可爱的萨摩耶...', '2025-11-23 17:28:57', '2025-11-23 17:27:24', '2025-11-23 17:28:57', NULL, 0);
INSERT INTO `t_conversation_session` VALUES (18, 'c913f669867b42c7a68203b8f4a1033e', 1, '新对话', NULL, 1, 'active', '您好！我注意到您提到了“刚才推荐的那些”，但目前我们的对话中还没有进行过具体的宠物推荐。可能是之前的对话记录没有显示完整。...', '2025-11-23 17:29:59', '2025-11-23 17:29:54', '2025-11-23 17:29:59', NULL, 0);
INSERT INTO `t_conversation_session` VALUES (19, '7560b3265c014b4e99d4f79569a93b4b', 1, '新对话', NULL, 2, 'active', '很高兴您喜欢小狗！根据系统中的信息，我为您精选了几只性格各异、正在等待领养的可爱狗狗...', '2025-11-23 17:40:30', '2025-11-23 17:39:39', '2025-11-23 17:40:30', NULL, 0);
INSERT INTO `t_conversation_session` VALUES (20, '100780d55ae343a680d419c1dda86216', 1, '新对话', NULL, 2, 'active', '看到您喜欢狗狗，我非常高兴！根据我们i宠园当前可领养的犬只信息，为您精心挑选了几位可爱的毛孩子...', '2025-11-23 17:52:57', '2025-11-23 17:52:29', '2025-11-23 17:52:57', NULL, 0);
INSERT INTO `t_conversation_session` VALUES (21, 'e084d86ab45c4238b00e1b9253632462', 1, '新对话', NULL, 1, 'active', '您好！欢迎来到i宠园，很高兴您对养宠物感兴趣。作为新手，了解相关知识是非常重要的一步...', '2025-11-23 17:56:08', '2025-11-23 17:55:50', '2025-11-23 17:56:08', NULL, 0);
INSERT INTO `t_conversation_session` VALUES (22, '7afabbff034745e39a983fbd632c1217', 1, '新对话', NULL, 2, 'active', '您好！作为第一次养宠物的新手，您选择领养是非常有爱心的决定。我为您精选了几只性格温顺、适合新手饲养的可领养宠物...', '2025-11-23 19:06:24', '2025-11-23 19:06:00', '2025-11-23 19:06:24', NULL, 0);
INSERT INTO `t_conversation_session` VALUES (23, 'beb671326596427eae5cf39d1fa20333', 1, '新对话', NULL, 3, 'active', '很高兴您喜欢小狗！我已经为您筛选出几只性格各异、正在等待新家的可爱狗狗，它们都已绝育并完成疫苗接种...', '2025-11-23 19:17:17', '2025-11-23 19:11:11', '2025-11-23 19:17:17', NULL, 0);
INSERT INTO `t_conversation_session` VALUES (24, '6b1f0aee1fd84d05aaa904b9c0bb3c0f', 1, '新对话', NULL, 2, 'active', '太棒了！小猫确实是很好的伴侣，温柔又治愈。根据你的喜好，我为你筛选了几只性格温顺、适合新手的可领养猫咪...', '2025-11-24 11:01:33', '2025-11-24 11:01:00', '2025-11-24 11:01:33', NULL, 0);
INSERT INTO `t_conversation_session` VALUES (25, '9aeeafe151544d248cd740165e83fe4d', 1, '新对话', NULL, 0, 'active', NULL, NULL, '2025-11-24 14:38:08', '2025-11-24 14:38:08', NULL, 0);
INSERT INTO `t_conversation_session` VALUES (26, '0a21213bfca643bd93fec283b29ea565', 1, '新对话', NULL, 2, 'active', '很高兴您想要开启养宠之旅！作为首次养宠家庭，您需要做好以下准备：...', '2025-11-24 14:43:38', '2025-11-24 14:42:45', '2025-11-24 14:43:38', NULL, 0);
INSERT INTO `t_conversation_session` VALUES (27, 'a2a2a226462f4e2cbf1d7d27f1f22c67', 1, '新对话', NULL, 3, 'active', '很高兴您喜欢小狗！根据您的偏好，我为您筛选了几只性格活泼、正在等待领养的可爱狗狗：\n\n🐾 **金金 - 金毛**\n活泼开朗，对人友善，是理想的家庭伴侣犬。\n适合有孩子或希望养一只温暖“暖男”型狗狗的家庭。\n\n🐾 **边边 - 边牧犬**\n精力充沛、聪明伶俐，喜欢互动和挑战。\n适合有一定养犬经验、能提供足够运动和训练的主人。\n\n🐾 **二哈 - 哈士奇**\n性格活泼好动，精力旺盛，标志性的蓝眼睛非常迷人。\n需要每天至少2小时户外活动，适合有院子或热爱户外运动的家庭。\n\n🐾 **泰迪 - 泰迪犬**\n活泼粘人，聪明伶俐，体型小巧，非常适合公寓生活。\n需要定期美容护理，但非常适合作为陪伴型宠物。\n\n【温馨提示】  \n所有推荐的小狗均已接种疫苗并完成健康检查，处于可领养状态。如果您对某只狗狗感兴趣，我可以为您提供更详细的照顾指南或安排领养咨询。\n\n想要了解如何与这些活泼的小狗建立良好关系？也可以告诉我您的生活环境，我会进一步为您匹配最合适的伙伴！', '2025-11-26 14:57:27', '2025-11-24 22:49:19', '2025-11-26 14:57:27', NULL, 0);
INSERT INTO `t_conversation_session` VALUES (28, '2c5c47d006f64a2ba708dd21c21a9022', 1, '新对话', NULL, 2, 'active', '您好！欢迎来到i宠园，作为新手领养人，您将开启一段充满爱与陪伴的美妙旅程。我们特别为您推荐一些性格温顺、易于照顾的宠物，帮助您顺利过渡到宠物主人的角色。\n\n您好！作为首次领养宠物的新手，选择性格温顺、容易相处的小伙伴非常重要。我已经为您筛选了一些非常适合新手的可领养宠物。\n\n🐾 **小布 - 布偶猫**\n3岁的温柔小姐姐，性格极其温顺亲人，喜欢安静地依偎在主人身边。适应力强，容易与人建立感情，是新手养猫的理想选择。\n\n🐾 **豆豆 - 金毛寻回犬幼犬**\n6个月大的阳光小男孩，性格开朗友善，聪明好学。金毛犬以温顺乖巧著称，非常适合家庭饲养，容易训练。\n\n🐾 **小白 - 英国短毛猫**\n2岁的英短蓝猫，性格稳定，独立性强但又不失亲人性。生活习性规律，护理简单，非常适合初次养猫的家庭。\n\n🐾 **团团 - 荷兰猪**\n1岁的可爱小宠，体型小巧，性格温和。不需要太多空间，喂养简单，是体验宠物照顾的好选择。\n\n【新手养宠指南】\n我们特别为您准备了新手养宠入门资料，涵盖：\n- 宠物到家前的准备工作\n- 饮食和日常护理要点\n- 如何建立信任关系\n- 基础训练技巧\n\n【温馨提示】\n建议您从性格温顺、护理相对简单的宠物开始。每只宠物都有详细的档案和照护说明，您可以随时查看或预约见面。\n\n如果需要了解更多关于特定宠物的护理知识，我很乐意为您提供专业建议。', '2025-11-28 15:15:04', '2025-11-27 13:51:39', '2025-11-28 15:15:04', NULL, 0);
INSERT INTO `t_conversation_session` VALUES (29, 'ab116c895c7e42ed9e4ac03fc4d0c5ac', 4, '新对话', NULL, 0, 'active', NULL, NULL, '2025-12-05 17:22:51', '2025-12-05 17:22:51', NULL, 0);
INSERT INTO `t_conversation_session` VALUES (30, 'f8bad93fa74648ed9cac83a435e524ca', 4, '新对话', NULL, 1, 'active', '作为第一次养宠物的新手，您需要了解以下几个关键方面：\n\n【心理准备】\n领养宠物是一项长期的承诺，通常意味着10-20年的陪伴责任。请确保您已经准备好投入时间、精力和情感来照顾一个生命。\n\n【物质准备】\n在迎接宠物回家前，建议准备好以下基本用品：\n- 适合宠物的食物和饮水器\n- 舒适的休息空间（窝或笼子）\n- 必要的卫生用品（如猫砂盆、牵引绳等）\n- 玩具和磨牙/磨爪工具\n- 基础护理工具（梳子、指甲剪等）\n\n【健康管理】\n新宠物到家后，建议尽快安排首次健康检查，并注意：\n- 按时接种疫苗\n- 定期进行体内外驱虫\n- 建立健康档案\n- 了解附近可靠的宠物医院\n\n【适应期关怀】\n宠物刚到新家可能会感到紧张，建议：\n- 给予安静的适应空间\n- 逐步建立信任关系\n- 保持规律的作息\n- 耐心观察行为变化\n\n【温馨提示】\n我们建议您先从阅读《领养前的准备工作》和《新宠物到家的第一周》这两篇指南开始，它们能为您提供更详细的实用建议。如果您已经有心仪的宠物类型，也可以告诉我，我可以为您提供更具体的护理建议。', '2025-12-06 00:08:53', '2025-12-06 00:08:36', '2025-12-06 00:08:53', NULL, 0);
INSERT INTO `t_conversation_session` VALUES (31, 'af66b498a4e84820a44502181b0cb73c', 4, '新对话', NULL, 2, 'active', '您好！看到您在寻找粘人的小猫，我特别为您找到了一位非常合适的小伙伴：\n\n🐾 **小暹 - 暹罗猫**\n性格活泼粘人，喜欢和主人交流，叫声独特，需要陪伴。这是一只优雅的蓝色眼睛重点色猫咪，已绝育并完成疫苗接种。\n适合喜欢互动、有时间陪伴的主人，不适合长时间独处。\n\n【温馨提示】\n暹罗猫是非常社交化的品种，他们渴望主人的关注和互动。建议每天至少安排30分钟的玩耍和陪伴时间，为他们提供丰富的玩具和活动空间。\n\n如果您想了解更多关于小暹的信息，或者想要预约见面，请随时告诉我！让我们一起为这位可爱的小天使找到温暖的家~', '2025-12-06 12:08:24', '2025-12-06 12:07:47', '2025-12-06 12:08:24', NULL, 0);
INSERT INTO `t_conversation_session` VALUES (32, '107df6e5c49144dd8861801503d80332', 4, '新对话', NULL, 1, 'active', '您好！我正在为您查找热情活泼且正在等待领养的小狗，稍等片刻...\n\n看起来系统暂时没有返回相关结果，可能是筛选条件需要调整。让我为您扩大搜索范围，查找更多可领养的狗狗：\n\n目前系统中暂无明确标注为\"热情\"性格的小狗信息。不过，我很乐意为您推荐一些正在等待领养的活泼可爱的小狗！\n\n我理解您想要一只热情的小狗，虽然目前系统中没有明确标注\"热情\"性格的小狗，但我为您找到了几只性格活泼、非常适合互动的可领养小狗：\n\n🐾 **金金 - 金毛**\n活泼开朗的金毛犬，天性友善亲人，喜欢与人互动和玩耍。金毛犬以热情好客著称，是非常理想的家庭伴侣犬。\n\n🐾 **边边 - 边牧犬**\n活泼聪明的边境牧羊犬，精力充沛且极具互动性。边牧需要较多的运动和智力刺激，适合喜欢户外活动的主人。\n\n【温馨提示】\n狗狗的\"活泼\"性格往往意味着它们需要较多的陪伴和运动时间。建议您考虑每天是否有足够的时间陪狗狗散步、玩耍和训练。\n\n如果您希望了解更多关于这些小狗的信息，或者想了解其他可领养的宠物，我很乐意为您提供帮助！', '2025-12-06 22:01:52', '2025-12-06 22:01:39', '2025-12-06 22:01:52', NULL, 0);
INSERT INTO `t_conversation_session` VALUES (33, 'b2005e12fe164ea98ba181dc8af40760', 7, '新对话', NULL, 1, 'active', '我正在为您查找性格安静、适合安静生活环境的可领养宠物。请稍等片刻，我将为您提供最合适的推荐。\n\n🐾 **米米 - 英国短毛猫**  \n性格温顺安静，喜欢安静地陪伴在主人身边，不需要过多吵闹的互动。  \n适合喜欢宁静生活、希望有个温柔伴侣的您。\n\n🐾 **小灰 - 安哥拉兔**  \n性情温和，动作轻柔，很少发出声音，是非常理想的室内安静宠物。  \n适合居住在公寓或需要低噪音环境的家庭。\n\n🐾 **布丁 - 彭布罗克威尔士柯基（幼犬）**  \n虽然柯基通常较活跃，但布丁性格沉稳，经过初步训练后表现得非常安静有礼。  \n适合希望拥有小狗但又偏好安静氛围的领养人。\n\n🐾 **墨墨 - 美国短毛猫**  \n独立性强，不会频繁叫唤，日常活动安静，自我娱乐能力强。  \n特别适合工作繁忙或喜欢安静独处的人士。\n\n【温馨提示】  \n安静的性格也需良好的环境来维持，建议为宠物提供稳定的生活节奏和舒适的空间。如果您希望进一步了解某只宠物的详细情况，我可以为您获取更多资讯。', '2025-12-19 12:16:16', '2025-12-19 12:16:06', '2025-12-19 12:16:16', NULL, 0);
INSERT INTO `t_conversation_session` VALUES (34, '6a353491c5e440938f5f25bbd927bc4e', 8, '新对话', NULL, 4, 'active', '不客气！如果您有任何关于宠物领养的问题，比如想了解可领养的宠物、申请流程、或者如何为新成员做准备，随时都可以问我。我会全力为您解答！\n\n愿每一位毛孩子都能遇见温暖的家，也祝您早日找到那个与您心灵契合的小伙伴 ❤️', '2025-12-21 21:20:53', '2025-12-21 20:57:40', '2025-12-21 21:20:53', NULL, 0);
INSERT INTO `t_conversation_session` VALUES (35, 'c6db7872f7644f32b2182e9f91c2a4cc', 7, '新对话', NULL, 2, 'active', '当然可以！对于第一次养宠物的朋友，了解一些基础知识非常重要。以下是新手领养人需要掌握的核心内容：\n\n- **基础护理知识**：包括日常饮食管理、清洁梳理、定期驱虫和疫苗接种\n- **行为习惯理解**：学习如何识别宠物的情绪（如害怕、兴奋、不适），建立良好的互动方式\n- **环境准备**：为宠物准备好安全舒适的生活空间，例如猫砂盆、狗窝、活动区域等\n- **时间与责任投入**：宠物需要长期陪伴和照顾，确保您有足够的时间遛狗、陪玩或处理突发情况\n- **经济预算规划**：除了食物开销，还需考虑医疗、美容、玩具等年度支出\n\n【温馨提示】  \n建议在正式领养前，先通过我们的《新手养宠入门指南》系统文章深入学习。我可以为您查询相关内容。\n\n是否需要我为您推荐一篇图文并茂的新手指南？', '2025-12-21 23:53:45', '2025-12-21 22:45:56', '2025-12-21 23:53:45', NULL, 0);
INSERT INTO `t_conversation_session` VALUES (36, '88453c2e04f54a53a28f2bca2eada630', 7, '新对话', NULL, 3, 'active', '谢谢你分享你的生活环境和期待！你住在公寓、工作较忙但希望有一只热情的小动物陪伴，这样的需求很常见，也非常重要。\n\n虽然系统当前暂无完全匹配的推荐宠物列表，但我依然可以给你一些适合你情况的专业建议：\n\n- **推荐宠物类型**：对于公寓生活且工作忙碌的人来说，成年猫咪或小型犬（如比熊、柯基、法国斗牛犬）通常是不错的选择。它们对空间要求不高，且经过一段时间磨合后能成为非常贴心的情绪伴侣。\n\n- **情绪价值小贴士**：热情又粘人的宠物往往更懂得“察言观色”，比如回家时迎接你、趴在身边陪你加班等。选择性格标签为【亲人】【活泼】【适应力强】的宠物会更容易带来温暖感受。\n\n- **时间管理建议**：即使工作忙，每天抽出15-30分钟专心陪宠（玩耍、抚摸、互动），也能大大增强感情联结，同时帮助宠物释放压力。\n\n【温馨提示】  \n建议你定期关注i宠园的可领养名单，新宠物会持续更新。如果你愿意，我可以为你开启“匹配提醒”服务——一旦有适合你生活方式的热情型宠物上线，第一时间通知你！\n\n愿你在忙碌生活中，早日遇见那个等你回家的小身影 🐾', '2025-12-22 14:13:06', '2025-12-22 14:12:01', '2025-12-22 14:13:06', NULL, 0);
INSERT INTO `t_conversation_session` VALUES (37, 'c52ab023ff664d28807fbd8cbe7e7a63', 1, '新对话', NULL, 2, 'active', '您好！欢迎来到i宠园，很高兴您正在考虑领养人生中的第一只宠物 🐾  \n作为新手，选择一只性格温顺、容易照顾的宠物会更有利于建立信心和良好的互动关系。\n\n根据系统中当前可领养的宠物数据，我为您精选了以下几位非常适合新手的小伙伴：\n\n🐾 **小博 - 博美犬**  \n性格温顺，体型小巧，对主人非常依赖且容易建立亲密关系，是理想的家庭伴侣犬。\n\n🐾 **布布 - 布偶猫**  \n调皮但亲人，布偶猫以“温柔如布偶”著称，适应力强，喜欢与人互动，适合室内饲养。\n\n🐾 **仓仓 - 仓鼠**  \n胆小谨慎但生活需求简单，适合想先从小型宠物开始尝试的新手，便于观察和学习照顾技巧。\n\n🐾 **小狸 - 中华狸花猫**  \n已绝育并完成疫苗接种，性格独立但也亲人，身体强壮、适应能力强，是优秀的家庭捕鼠能手。\n\n【温馨提示】  \n首次养宠建议优先考虑健康状况良好、已接种疫苗或已完成绝育的宠物。同时，我们建议您先阅读系统内的《基础服从训练技巧》等新手指南，帮助您更快上手照顾新伙伴。\n\n如果您对某位宠物感兴趣，可以查看详情并提交领养申请，我们的工作人员将尽快与您联系。祝您早日找到心仪的毛孩子！', '2026-01-03 01:07:21', '2026-01-03 00:47:06', '2026-01-03 01:07:21', NULL, 0);
INSERT INTO `t_conversation_session` VALUES (38, '9c993653047b4eb19342bfaa63d2de9c', 1, '新对话', NULL, 2, 'active', '很高兴你考虑开启养宠之旅！作为新手，选择一只性格温顺、适应力强、护理需求相对简单的宠物，能大大提升领养体验和成功率 🌟\n\n我来为你推荐几只**当前系统中可领养、特别适合新手的宠物**（已筛选：性格稳定、健康状况良好、已有基础社会化训练）：\n\n很高兴你愿意开启养宠之旅！作为新手，选择一只性格温和、适应力强、护理需求相对简单的宠物，能大大提升你的信心和幸福感 🌟\n\n以下是当前系统中**特别适合新手领养的宠物推荐**（已综合考虑性格稳定性、日常护理难度、互动友好度等因素）：\n\n🐾 **小狸 - 中华狸花猫**  \n性格独立机警，但也亲人；已绝育+完成疫苗接种，健康有保障；身体强壮、适应力强，对环境变化耐受度高，非常适合首次养猫的家庭。  \n适合：希望拥有安静陪伴、生活节奏较规律、能提供基础猫具（猫砂盆/爬架）的新手。\n\n🐾 **小博 - 博美犬**  \n温顺亲人、体型小巧、运动需求适中；毛发打理相对简单（定期梳理即可），对公寓生活友好。  \n适合：有固定陪伴时间、喜欢轻度互动、居住空间有限但希望有狗狗陪伴的新手。\n\n🐾 **仓仓 - 仓鼠**  \n胆小谨慎、饲养成本低、空间需求极小（一个合适笼子即可）、日常照料简单（清洁+喂食）。  \n适合：想先体验责任与陪伴、时间或空间受限、或希望从“低压力”开始建立养宠信心的新手。\n\n🐾 **布布 - 布偶猫**  \n调皮可爱、社交性强、对人类信任度高；虽活泼但不具攻击性，幼龄期已社会化良好，易建立亲密关系。  \n适合：有稳定居家时间、喜欢温柔互动、愿意学习猫咪行为语言的新手家庭。\n\n🐾 **金金 - 金毛寻回犬**  \n天生友善、耐心包容、学习能力强；虽然体型较大，但性格极其稳定，是公认的新手友好犬种。  \n适合：有户外活动条件（如附近有公园）、家人共同参与照顾、重视长期情感陪伴的新手家庭。\n\n【温馨提示】  \n✅ 所有推荐宠物均为「可领养」状态，信息真实有效（数据截至今日）  \n✅ 领养前我们会安排「新手养宠指导课」+「家庭环境评估」，全程免费支持  \n✅ 若你更倾向某类动物（如只想养猫/狗），或有具体生活情况（如租房、有小孩、工作忙碌等），欢迎告诉我，我可以为你进一步精准筛选 ❤️  \n\n需要我帮你查看某只宠物的详细资料、预约见面，或发送《新手养宠入门指南》图文版吗？ 😊', '2026-01-28 21:55:21', '2026-01-28 21:55:07', '2026-01-28 21:55:21', NULL, 0);
INSERT INTO `t_conversation_session` VALUES (39, '91ecdef2699f47f38bc3d4dcd0f795d4', 29, '新对话', NULL, 2, 'active', '很高兴你考虑开启养宠之旅！作为新手，选择一只性格温顺、适应力强、护理需求相对简单的宠物，确实能让这段关系更温暖、更顺利 🌟\n\n我来为你推荐几只目前**系统中可领养、适合新手、亲人度高、易于照顾**的宠物（已筛选「available」状态，性格关键词含「温顺」「亲人」「适应力强」）：\n\n看起来当前系统中暂时没有完全匹配“温顺、亲人、适应力强”且标记为「可领养」的宠物数据。不过别担心——这并不意味着没有适合新手的好伙伴！作为i宠园的领养顾问，我很乐意为你提供专业、温暖的建议：\n\n🐾 **新手友好型宠物推荐（基于普遍饲养经验与动物行为学）**  \n✅ **成年已绝育的猫咪（2岁以上）**  \n- 通常性格稳定、独立但不冷漠，会主动亲近信任的人  \n- 日常护理相对简单：猫砂盆+定期梳毛+基础疫苗驱虫即可  \n- 比幼猫更少突发行为问题（如过度啃咬、夜嚎），更适合零经验家庭  \n\n✅ **小型犬中的“稳重型”品种（如柯基、比熊、巴哥）**  \n- 若选择已社会化训练过的成年犬（1–3岁），对新手更友好  \n- 建议优先考虑【曾与儿童/老人共处过】或【有寄养记录】的个体——说明其情绪稳定、适应力强  \n\n✅ **兔子/豚鼠（需注意：需专用笼舍+草粮+定期剪指甲）**  \n- 性情温和、安静，适合公寓生活  \n- 虽不如猫狗粘人，但通过每日轻柔互动，也能建立明显依恋关系  \n\n📌 **特别提醒新手的3个关键准备**：  \n1️⃣ 不要只看“可爱”，优先关注宠物是否已完成【基础健康检查+疫苗+驱虫+绝育】（i宠园所有待领养宠物均满足此标准）  \n2️⃣ 领养前务必确认自己能承诺【至少10–15年的陪伴】——这是对生命最基本的尊重  \n3️⃣ i宠园提供【首月免费线上咨询+新手礼包+社区互助群】，全程支持你平稳过渡  \n\n需要我帮你：  \n🔹 查看当前最新可领养的成年猫/狗名单？  \n🔹 获取《新手养猫/养狗第一步》图文指南？  \n🔹 或根据你的居住环境（如：有无阳台、是否常出差、家中是否有小孩）做个性化推荐？  \n\n随时告诉我，我会为你精准匹配 ❤️', '2026-01-31 22:12:08', '2026-01-31 22:11:55', '2026-01-31 22:12:08', NULL, 0);
INSERT INTO `t_conversation_session` VALUES (40, '5d49edac6af3497f85eafd32ab0bb7f0', 32, '新对话', NULL, 2, 'active', '你好呀～欢迎来到i宠园！🐾  \n很高兴你愿意开启养宠之旅，这份初心特别温暖。作为新手，选择一只性格温和、适应力强、护理需求相对简单的宠物，确实能让你更轻松地建立信任与陪伴关系。\n\n我来为你推荐几位目前**系统中可领养、特别适合新手的毛孩子**（已筛选：性格温顺/亲人/低攻击性/基础护理友好）：\n\n你好！非常感谢你对领养宠物的认真态度——作为新手，主动思考“什么宠物更适合我”，这已经是负责任养宠的第一步 🌟\n\n从系统当前可领养的宠物中，我们为你筛选出几只**性格温和、护理需求适中、适合新手入门**的可爱伙伴。结合它们的真实信息，为你推荐如下：\n\n🐾 **仓仓 - 仓鼠**  \n性格胆小、谨慎，适应安静环境，日常互动温和不具攻击性；体型小、空间需求低（一个合适笼子即可），饮食简单（专用鼠粮+少量蔬果），非常适合首次养宠、居住空间有限或想先建立照顾责任感的朋友。  \n适合：公寓生活、希望低门槛体验养宠、喜欢观察型陪伴。\n\n🐾 **test - 兔子（多只同名待选）**  \n虽然详情描述较简略，但兔子普遍温顺、爱干净、可训练如厕，成年后性格稳定；相比狗狗，运动量和时间投入更可控，适合规律作息的新手。建议选择已绝育、2岁以上个体，性格更沉稳。  \n适合：有固定活动区域（如围栏式兔区）、愿意学习草粮搭配与牙齿护理的新手。\n\n🐾 **1 - 小鸟（品种未详，待补充）**  \n体型小巧、鸣声悦耳，互动趣味性强；若为文鸟、玄凤等常见入门鸟种，通常亲人性好、易建立信任。需注意笼具安全、避免油烟与空调直吹。  \n适合：喜欢轻量互动、有窗台/阳台空间、愿花时间耐心陪伴的新手。\n\n⚠️ 温馨提示：  \n目前系统中部分宠物信息（如名字、品种、性格）尚待志愿者完善，我们正积极优化数据质量。如果你对某只宠物特别心动，欢迎点击查看详情 → 提交【预咨询】，我们的领养顾问将为你同步最新情况并提供一对一指导！\n\n📌 新手小贴士（无需工具，直接送你）：  \n✅ 养宠前必做三件事：评估每日可陪伴时间、确认住所是否允许养宠、预留首年基础开支（疫苗/驱虫/口粮/基础用品约2000–4000元）  \n✅ 推荐先阅读《新手养宠完全指南》→ 我可立刻为你调取图文版  \n✅ 所有领养均需签署《责任承诺书》，我们会全程陪伴你完成科学过渡  \n\n需要我为你：\n🔹 调取《新手养宠完全指南》图文版？  \n🔹 查询「仓鼠/兔子」专属护理要点？  \n🔹 了解领养全流程与审核要求？  \n\n随时告诉我，我在这里，陪你一起迈出温暖又踏实的第一步 🐾', '2026-02-01 20:57:12', '2026-02-01 20:56:55', '2026-02-01 20:57:12', NULL, 0);

-- ----------------------------
-- Table structure for t_cs_session
-- ----------------------------
DROP TABLE IF EXISTS `t_cs_session`;
CREATE TABLE `t_cs_session`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '会话主键ID',
  `user_id` bigint NOT NULL COMMENT '用户ID（普通用户）',
  `agent_id` bigint NULL DEFAULT NULL COMMENT '客服ID（管理员账号，可为空表示未分配）',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '会话状态：OPEN / CLOSED',
  `last_message` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '最近一条消息内容',
  `last_time` datetime NULL DEFAULT NULL COMMENT '最近一条消息时间',
  `unread_for_user` int NOT NULL DEFAULT 0 COMMENT '用户端未读消息数',
  `unread_for_agent` int NOT NULL DEFAULT 0 COMMENT '客服端未读消息数',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NULL DEFAULT 0 COMMENT '逻辑删除标记：0-正常 1-删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_cs_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_cs_agent_id`(`agent_id` ASC) USING BTREE,
  INDEX `idx_cs_status_time`(`status` ASC, `last_time` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 25 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '人工客服会话表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_cs_session
-- ----------------------------
INSERT INTO `t_cs_session` VALUES (1, 4, 1, 'OPEN', '好的，我们已为您记录需求，如有合适的宠物会第一时间联系您。', '2025-12-10 23:11:43', 0, 0, '2025-12-08 21:56:02', '2025-12-08 21:56:02', 0);
INSERT INTO `t_cs_session` VALUES (2, 1, 1, 'OPEN', '😀😀😀😀😀😀😀', '2026-01-03 00:47:23', 0, 1, '2025-12-09 00:15:59', '2025-12-09 00:15:59', 0);
INSERT INTO `t_cs_session` VALUES (3, 5, 1, 'OPEN', '你好', '2025-12-11 19:06:35', 0, 0, '2025-12-11 19:05:49', '2025-12-11 19:05:49', 0);
INSERT INTO `t_cs_session` VALUES (4, 6, 1, 'OPEN', '1111', '2025-12-15 21:15:21', 2, 0, '2025-12-11 19:22:02', '2025-12-11 19:22:02', 0);
INSERT INTO `t_cs_session` VALUES (5, 7, 1, 'OPEN', 'http://localhost:9000/animal-adopt/images/a8f5844dc71f4348976938fe4bde956f.png', '2025-12-22 14:05:24', 0, 0, '2025-12-14 22:43:24', '2025-12-14 22:43:24', 0);
INSERT INTO `t_cs_session` VALUES (6, 3, 1, 'OPEN', NULL, NULL, 0, 0, '2025-12-21 01:09:44', '2025-12-21 01:09:44', 0);
INSERT INTO `t_cs_session` VALUES (7, 8, 1, 'OPEN', '哦哦 好👌', '2025-12-24 16:04:18', 0, 0, '2025-12-21 20:56:33', '2025-12-21 20:56:33', 0);
INSERT INTO `t_cs_session` VALUES (8, 9, 1, 'OPEN', NULL, NULL, 0, 0, '2026-01-01 11:03:30', '2026-01-01 11:03:30', 0);
INSERT INTO `t_cs_session` VALUES (9, 10, 1, 'OPEN', NULL, NULL, 0, 0, '2026-01-01 11:05:27', '2026-01-01 11:05:27', 0);
INSERT INTO `t_cs_session` VALUES (10, 11, 1, 'OPEN', NULL, NULL, 0, 0, '2026-01-01 11:52:14', '2026-01-01 11:52:14', 0);
INSERT INTO `t_cs_session` VALUES (11, 12, 1, 'OPEN', NULL, NULL, 0, 0, '2026-01-20 22:12:00', '2026-01-20 22:12:00', 0);
INSERT INTO `t_cs_session` VALUES (12, 13, 1, 'OPEN', NULL, NULL, 0, 0, '2026-01-28 21:03:27', '2026-01-28 21:03:27', 0);
INSERT INTO `t_cs_session` VALUES (13, 14, 1, 'OPEN', NULL, NULL, 0, 0, '2026-01-28 21:04:32', '2026-01-28 21:04:32', 0);
INSERT INTO `t_cs_session` VALUES (14, 15, 1, 'OPEN', NULL, NULL, 0, 0, '2026-01-28 21:32:43', '2026-01-28 21:32:43', 0);
INSERT INTO `t_cs_session` VALUES (15, 16, 1, 'OPEN', NULL, NULL, 0, 0, '2026-01-31 17:06:47', '2026-01-31 17:06:47', 0);
INSERT INTO `t_cs_session` VALUES (16, 26, 1, 'OPEN', NULL, NULL, 0, 0, '2026-01-31 20:48:43', '2026-01-31 20:48:43', 0);
INSERT INTO `t_cs_session` VALUES (17, 27, 1, 'OPEN', NULL, NULL, 0, 0, '2026-01-31 20:52:56', '2026-01-31 20:52:56', 0);
INSERT INTO `t_cs_session` VALUES (18, 28, 1, 'OPEN', NULL, NULL, 0, 0, '2026-01-31 21:52:32', '2026-01-31 21:52:32', 0);
INSERT INTO `t_cs_session` VALUES (19, 29, 1, 'OPEN', '111', '2026-01-31 22:14:23', 0, 0, '2026-01-31 22:05:09', '2026-01-31 22:05:09', 0);
INSERT INTO `t_cs_session` VALUES (20, 30, 1, 'OPEN', NULL, NULL, 0, 0, '2026-02-01 19:49:07', '2026-02-01 19:49:07', 0);
INSERT INTO `t_cs_session` VALUES (21, 31, 1, 'OPEN', NULL, NULL, 0, 0, '2026-02-01 20:49:30', '2026-02-01 20:49:30', 0);
INSERT INTO `t_cs_session` VALUES (22, 32, 1, 'OPEN', 'http://localhost:9000/animal-adopt/images/99b72c05ad8d44749fc12d67b9087233.jpg', '2026-02-01 21:12:40', 0, 0, '2026-02-01 20:56:15', '2026-02-01 20:56:15', 0);
INSERT INTO `t_cs_session` VALUES (23, 33, 1, 'OPEN', '😀', '2026-02-01 22:20:12', 0, 0, '2026-02-01 22:18:14', '2026-02-01 22:18:14', 0);
INSERT INTO `t_cs_session` VALUES (24, 34, 1, 'OPEN', NULL, NULL, 0, 0, '2026-02-01 22:38:04', '2026-02-01 22:38:04', 0);

-- ----------------------------
-- Table structure for t_dict_item
-- ----------------------------
DROP TABLE IF EXISTS `t_dict_item`;
CREATE TABLE `t_dict_item`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `dict_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '字典类型',
  `dict_key` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '字典键',
  `dict_label` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '字典值',
  `sort_order` int NULL DEFAULT 0 COMMENT '排序',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint NULL DEFAULT 0 COMMENT '是否删除（逻辑删除）',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_dict_type_key`(`dict_type` ASC, `dict_key` ASC) USING BTREE,
  INDEX `idx_dict_type`(`dict_type` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 46 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_dict_item
-- ----------------------------
INSERT INTO `t_dict_item` VALUES (1, 'pet_category', 'dog', '小狗', 1, 1, '该宠物类型为狗狗', '2025-12-07 16:13:58', '2026-01-31 21:05:52', 0);
INSERT INTO `t_dict_item` VALUES (2, 'pet_category', 'cat', '小猫', 2, 1, '小猫', '2025-12-07 16:13:58', '2026-01-31 21:13:56', 0);
INSERT INTO `t_dict_item` VALUES (3, 'pet_category', 'bird', '鸟类', 3, 1, '小鸟', '2025-12-07 16:13:58', '2025-12-07 16:13:58', 0);
INSERT INTO `t_dict_item` VALUES (4, 'pet_category', 'rabbit', '兔子', 4, 1, '小兔子', '2025-12-07 16:13:58', '2025-12-07 16:13:58', 0);
INSERT INTO `t_dict_item` VALUES (5, 'adoption_status', 'available', '可领养', 1, 1, '默认领养状态', '2025-12-07 16:13:58', '2025-12-07 16:13:58', 0);
INSERT INTO `t_dict_item` VALUES (6, 'adoption_status', 'reserved', '已预订', 2, 1, '已经被其他人领养了', '2025-12-07 16:13:58', '2025-12-07 16:13:58', 0);
INSERT INTO `t_dict_item` VALUES (7, 'adoption_status', 'pending', '待审核', 3, 1, '申请领养后等待管理员审核', '2025-12-07 16:13:58', '2025-12-07 16:13:58', 0);
INSERT INTO `t_dict_item` VALUES (8, 'adoption_status', 'adopted', '已领养', 4, 1, '管理员通过提交的申请', '2025-12-07 16:13:58', '2025-12-07 16:13:58', 0);
INSERT INTO `t_dict_item` VALUES (9, 'health_status', 'healthy', '健康', 1, 1, '默认健康状态', '2025-12-07 16:13:58', '2025-12-07 16:13:58', 0);
INSERT INTO `t_dict_item` VALUES (10, 'health_status', 'sick', '生病', 2, 1, '生病了', '2025-12-07 16:13:58', '2025-12-07 16:13:58', 0);
INSERT INTO `t_dict_item` VALUES (11, 'health_status', 'injured', '受伤', 3, 1, '受伤了', '2025-12-07 16:13:58', '2025-12-07 16:13:58', 0);
INSERT INTO `t_dict_item` VALUES (12, 'health_status', 'recovering', '康复中', 4, 1, '痊愈了', '2025-12-07 16:13:58', '2025-12-07 16:13:58', 0);
INSERT INTO `t_dict_item` VALUES (13, 'article_category', 'GUIDE', '领养指南', 1, 1, '领养指南', '2025-12-07 16:13:58', '2025-12-07 16:13:58', 0);
INSERT INTO `t_dict_item` VALUES (14, 'article_category', 'STORY', '领养故事', 2, 1, '领养故事', '2025-12-07 16:13:58', '2025-12-07 16:13:58', 0);
INSERT INTO `t_dict_item` VALUES (15, 'pet_category', 'squirrel', '松鼠', 5, 1, '松鼠', '2025-12-07 19:09:30', '2025-12-07 19:09:30', 0);
INSERT INTO `t_dict_item` VALUES (16, 'pet_category', 'hamster', '仓鼠', 6, 1, '仓鼠', '2025-12-07 19:12:55', '2025-12-07 19:12:55', 0);
INSERT INTO `t_dict_item` VALUES (17, 'pet_category', '熊', '熊', 7, 1, NULL, '2025-12-07 19:29:03', '2025-12-07 19:45:23', 1);
INSERT INTO `t_dict_item` VALUES (18, 'pet_category', 'bear', '熊', 7, 1, NULL, '2025-12-07 20:30:33', '2025-12-07 23:34:47', 1);
INSERT INTO `t_dict_item` VALUES (19, 'guide_category', '行为问题', '行为问题', 1, 1, NULL, '2025-12-20 20:53:57', '2025-12-20 20:53:57', 0);
INSERT INTO `t_dict_item` VALUES (20, 'guide_category', '领养准备', '领养准备', 2, 1, NULL, '2025-12-20 20:53:57', '2025-12-20 20:53:57', 0);
INSERT INTO `t_dict_item` VALUES (21, 'guide_category', '日常护理', '日常护理', 3, 1, NULL, '2025-12-20 20:53:57', '2025-12-20 20:53:57', 0);
INSERT INTO `t_dict_item` VALUES (22, 'guide_category', '训练教育', '训练教育', 4, 1, NULL, '2025-12-20 20:53:57', '2025-12-20 20:53:57', 0);
INSERT INTO `t_dict_item` VALUES (23, 'guide_category', '健康管理', '健康管理', 5, 1, NULL, '2025-12-20 20:53:57', '2025-12-20 20:53:57', 0);
INSERT INTO `t_dict_item` VALUES (24, 'user_role', 'user', '普通用户', 1, 1, '普通前台用户角色', '2025-12-22 00:00:00', '2025-12-22 00:00:00', 0);
INSERT INTO `t_dict_item` VALUES (25, 'user_role', 'admin', '管理员', 2, 1, '后台管理员角色', '2025-12-22 00:00:00', '2025-12-22 00:00:00', 0);
INSERT INTO `t_dict_item` VALUES (26, 'user_role', 'super_admin', '超级管理员', 3, 1, '系统唯一超级管理员角色', '2025-12-22 00:00:00', '2025-12-22 00:00:00', 0);
INSERT INTO `t_dict_item` VALUES (30, 'pet_category', '1', '11', 6, 0, '', '2026-01-31 21:03:34', '2026-01-31 21:15:19', 1);
INSERT INTO `t_dict_item` VALUES (32, 'pet_category', '鳄鱼', '鳄鱼', 7, 0, '', '2026-01-31 21:04:38', '2026-01-31 21:15:24', 1);
INSERT INTO `t_dict_item` VALUES (34, 'pet_category', 'hedgehog', '刺猬', 7, 1, '刺猬', '2026-01-31 21:25:18', '2026-01-31 21:25:18', 0);
INSERT INTO `t_dict_item` VALUES (35, 'pet_category', 'snake', '蛇', 8, 1, '蛇', '2026-01-31 21:40:44', '2026-01-31 21:40:44', 0);
INSERT INTO `t_dict_item` VALUES (36, 'story_tag', 'Test', 'Test', 1, 1, NULL, '2026-02-01 21:11:39', '2026-02-01 21:11:39', 0);
INSERT INTO `t_dict_item` VALUES (37, 'pet_category', 'tiger', '老虎', 9, 0, '老虎', '2026-02-01 21:15:21', '2026-02-01 21:15:21', 0);
INSERT INTO `t_dict_item` VALUES (38, 'story_tag', '成功领养', '成功领养', 1, 1, '故事标签', '2026-02-09 21:57:18', '2026-02-09 21:57:18', 0);
INSERT INTO `t_dict_item` VALUES (39, 'story_tag', '狗狗', '狗狗', 2, 1, '故事标签', '2026-02-09 21:57:18', '2026-02-09 21:57:18', 0);
INSERT INTO `t_dict_item` VALUES (40, 'story_tag', '失散重聚', '失散重聚', 3, 1, '故事标签', '2026-02-09 21:57:18', '2026-02-09 21:57:18', 0);
INSERT INTO `t_dict_item` VALUES (41, 'story_tag', '猫咪', '猫咪', 4, 1, '故事标签', '2026-02-09 21:57:18', '2026-02-09 21:57:18', 0);
INSERT INTO `t_dict_item` VALUES (42, 'story_tag', '救助故事', '救助故事', 5, 1, '故事标签', '2026-02-09 21:57:18', '2026-02-09 21:57:18', 0);
INSERT INTO `t_dict_item` VALUES (43, 'story_tag', '温暖救助', '温暖救助', 6, 1, '故事标签', '2026-02-09 21:57:18', '2026-02-09 21:57:18', 0);
INSERT INTO `t_dict_item` VALUES (44, 'story_tag', '生命救赎', '生命救赎', 7, 1, '故事标签', '2026-02-09 21:57:18', '2026-02-09 21:57:18', 0);

-- ----------------------------
-- Table structure for t_guide
-- ----------------------------
DROP TABLE IF EXISTS `t_guide`;
CREATE TABLE `t_guide`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '标题',
  `excerpt` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '摘要',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'Markdown内容',
  `image` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '封面图',
  `category` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '分类',
  `views` int NULL DEFAULT 0 COMMENT '浏览次数',
  `like_count` int NULL DEFAULT 0 COMMENT '点赞数',
  `favorite_count` int NULL DEFAULT 0 COMMENT '收藏数',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_category`(`category` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '领养指南表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_guide
-- ----------------------------
INSERT INTO `t_guide` VALUES (1, '领养前的准备工作', '详细介绍领养宠物前需要做的各项准备工作，包括心理准备、物质准备和环境准备。', '# 领养前的准备工作\r\r\n\r\r领养宠物是一项长期的责任，在决定领养之前，需要做好充分的准备工作。\r\r\n\r\r## 心理准备\r\r\n\r\r领养宠物意味着承担起一个生命的责任，需要有长期照顾它的心理准备。宠物会陪伴您很多年，期间可能会遇到各种问题，如生病、行为问题等，都需要您耐心处理。\r\r\n\r\r## 物质准备\r\r\n\r\r在宠物到家之前，需要准备好基本的生活用品：\r\r\n\r\r- 食物和饮水器\r\r\n- 舒适的窝或笼子\r\r\n- 玩具和磨爪板（猫咪）\r\r\n- 牵引绳和项圈（狗狗）\r\r\n- 猫砂盆和猫砂（猫咪）\r\r\n- 基础药品和急救包\r\r\n\r\r## 环境准备\r\r\n\r\r确保家中环境对宠物安全友好：\r\r\n\r\r- 收好易碎物品和危险品\r\r\n- 封堵可能的逃跑通道\r\r\n- 准备一个安静的适应空间\r\r\n- 了解附近的宠物医院\r\r\n\r\r做好这些准备工作，可以帮助宠物更快适应新家，也能让您更好地享受与宠物相伴的快乐时光。', 'https://192.168.99.228:9000/animal-adopt/1.png', '领养准备', 1307, 3, 3, '2025-11-20 22:53:07', '2026-02-09 21:45:00');
INSERT INTO `t_guide` VALUES (2, '新宠物到家的第一周', '帮助新领养的宠物适应新环境的实用指南，包括饮食、作息和互动建议。', '# 新宠物到家的第一周\r\r\n\r\r新宠物到家的第一周是适应期，需要特别的关注和照顾。\r\r\n\r\r## 第一天：安静适应\r\r\n\r\r第一天不要过度互动，给宠物一个安静的空间适应环境。准备好食物和水，但不要强迫进食。\r\r\n\r\r## 第2-3天：建立信任\r\r\n\r\r开始轻声说话和温和互动，用手喂食建立信任关系。观察宠物的行为和健康状况。\r\r\n\r\r## 第4-7天：逐步熟悉\r\r\n\r\r逐步增加互动时间，引导宠物熟悉家中各个区域。开始建立基本的作息规律。\r\r\n\r\r记住，每只宠物的适应速度不同，要有耐心，不要急于求成。', 'https://192.168.99.228:9000/animal-adopt/2.png', '日常护理', 1042, 3, 2, '2025-11-20 22:53:07', '2026-02-01 21:50:00');
INSERT INTO `t_guide` VALUES (3, '基础服从训练技巧', '教授基本的服从指令训练方法，帮助建立良好的人宠关系。', '# 基础服从训练技巧\r\r\n\r\r训练宠物不仅能让它们更好地融入家庭生活，还能增进与主人的感情。\r\r\n\r\r## 基本原则\r\r\n\r\r训练要遵循正向强化原则，多鼓励少惩罚。每次训练时间不宜过长，保持在10-15分钟为宜。\r\r\n\r\r## 常用指令\r\r\n\r\r- **坐下：** 手持零食，引导宠物坐下，给予奖励\r\r\n- **趴下：** 在坐下的基础上，将零食往地面移动\r\r\n- **过来：** 用欢快的语气叫名字，宠物靠近时给予奖励\r\r\n- **握手：** 轻轻抬起宠物的爪子，说\"握手\"并奖励\r\r\n\r\r## 注意事项\r\r\n\r\r训练要有耐心和一致性，全家人使用相同的指令和手势。训练后要及时奖励，让宠物建立正确的条件反射。', 'https://192.168.99.228:9000/animal-adopt/3.png', '训练教育', 776, 2, 1, '2025-11-20 22:53:07', '2026-02-09 21:45:00');
INSERT INTO `t_guide` VALUES (4, '常见疾病预防与处理', '介绍宠物常见疾病的症状识别、预防措施和应急处理方法。', '# 常见疾病预防与处理\r\r\n\r\r了解宠物常见疾病，及早发现和预防，是每个宠物主人的必备知识。\r\r\n\r\r## 预防接种\r\r\n\r\r按时接种疫苗是预防疾病的最佳方式。幼年宠物需要接种多联疫苗和狂犬病疫苗，成年后每年加强免疫。\r\r\n\r\r## 常见症状\r\r\n\r\r- 食欲不振、精神萎靡\r\r\n- 呕吐、腹泻\r\r\n- 咳嗽、流鼻涕\r\r\n- 皮肤瘙痒、脱毛\r\r\n- 频繁舔爪或某个部位\r\r\n\r\r## 日常保健\r\r\n\r\r定期驱虫，保持环境清洁，提供均衡营养，适量运动。发现异常及时就医，不要自行用药。', 'https://192.168.99.228:9000/animal-adopt/4.png', '健康管理', 1114, 1, 1, '2025-11-20 22:53:07', '2026-02-09 21:45:00');
INSERT INTO `t_guide` VALUES (5, '分离焦虑症的应对', '分析宠物分离焦虑的原因和表现，并提供有效的缓解方法。', '# 分离焦虑症的应对\r\r\n\r\r分离焦虑是许多宠物常见的行为问题，表现为主人离开后出现焦虑和破坏行为。\r\r\n\r\r## 症状表现\r\r\n\r\r- 主人离开时过度吠叫或哀鸣\r\r\n- 破坏家具或物品\r\r\n- 在不该的地方大小便\r\r\n- 过度舔毛或咬爪子\r\r\n- 主人回来时过度兴奋\r\r\n\r\r## 缓解方法\r\r\n\r\r渐进式脱敏训练：开始时短时间离开，逐步延长时间。离开和回来时保持平静，不要过度安抚。\r\r\n\r\r## 其他建议\r\r\n\r\r提供足够的玩具和活动空间，确保宠物在独处时有事可做。考虑使用安抚玩具或背景音乐。严重情况建议咨询专业训练师或兽医。', 'https://192.168.99.228:9000/animal-adopt/5.png', '行为问题', 836, 1, 1, '2025-11-20 22:53:07', '2026-02-09 21:45:00');
INSERT INTO `t_guide` VALUES (6, '营养饮食搭配指南', '根据不同年龄和健康状况的宠物，提供科学的饮食搭配建议。', '# 营养饮食搭配指南\r\r\n\r\r合理的饮食搭配是宠物健康的基础，不同年龄段的宠物有不同的营养需求。\r\r\n\r\r## 幼年期（0-1岁）\r\r\n\r\r需要高蛋白、高能量的食物支持快速生长。每天3-4餐，选择专门的幼年宠物粮。\r\r\n\r\r## 成年期（1-7岁）\r\r\n\r\r每天1-2餐，保持营养均衡。蛋白质占30-40%，脂肪10-20%，碳水化合物30-50%。\r\r\n\r\r## 老年期（7岁以上）\r\r\n\r\r降低热量摄入，增加易消化的优质蛋白。考虑添加关节保健成分和抗氧化剂。\r\r\n\r\r## 禁忌食物\r\r\n\r\r- 巧克力、咖啡因\r\r\n- 葡萄、洋葱、大蒜\r\r\n- 生肉、生鸡蛋\r\r\n- 高盐、高糖食物\r\r\n\r\r记得提供充足的清洁饮水，根据宠物的活动量调整食物份量。', 'https://192.168.99.228:9000/animal-adopt/6.png', '日常护理', 674, 1, 1, '2025-11-20 22:53:07', '2026-02-09 21:45:00');
INSERT INTO `t_guide` VALUES (8, 'test', '111', '# Test', 'http://localhost:9000/animal-adopt/images/1cc100210c864637bfbf7f4cbf7f6f70.jpg', '行为问题', 6, 0, 0, '2026-01-31 22:23:11', '2026-02-09 21:45:00');

-- ----------------------------
-- Table structure for t_guide_favorite
-- ----------------------------
DROP TABLE IF EXISTS `t_guide_favorite`;
CREATE TABLE `t_guide_favorite`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `guide_id` bigint NOT NULL COMMENT '指南ID',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_user_guide`(`user_id` ASC, `guide_id` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_guide_id`(`guide_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '指南收藏记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_guide_favorite
-- ----------------------------
INSERT INTO `t_guide_favorite` VALUES (2, 1, 6, '2025-11-23 16:46:17');
INSERT INTO `t_guide_favorite` VALUES (4, 1, 1, '2025-11-23 17:14:21');
INSERT INTO `t_guide_favorite` VALUES (5, 1, 4, '2025-11-24 22:11:13');
INSERT INTO `t_guide_favorite` VALUES (6, 1, 5, '2025-11-24 22:11:17');
INSERT INTO `t_guide_favorite` VALUES (7, 4, 2, '2025-12-05 13:54:26');
INSERT INTO `t_guide_favorite` VALUES (8, 4, 3, '2025-12-05 14:38:34');
INSERT INTO `t_guide_favorite` VALUES (9, 4, 1, '2025-12-05 14:38:45');
INSERT INTO `t_guide_favorite` VALUES (10, 29, 1, '2026-01-31 22:07:03');
INSERT INTO `t_guide_favorite` VALUES (11, 32, 2, '2026-02-01 20:59:14');

-- ----------------------------
-- Table structure for t_guide_like
-- ----------------------------
DROP TABLE IF EXISTS `t_guide_like`;
CREATE TABLE `t_guide_like`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `guide_id` bigint NOT NULL COMMENT '指南ID',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_user_guide`(`user_id` ASC, `guide_id` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_guide_id`(`guide_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '指南点赞记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_guide_like
-- ----------------------------
INSERT INTO `t_guide_like` VALUES (5, 1, 3, '2025-11-21 23:08:48');
INSERT INTO `t_guide_like` VALUES (9, 1, 1, '2025-11-23 17:13:43');
INSERT INTO `t_guide_like` VALUES (10, 1, 4, '2025-11-24 22:11:10');
INSERT INTO `t_guide_like` VALUES (11, 1, 5, '2025-11-24 22:11:17');
INSERT INTO `t_guide_like` VALUES (12, 1, 6, '2025-11-26 14:56:10');
INSERT INTO `t_guide_like` VALUES (13, 4, 2, '2025-12-05 13:54:27');
INSERT INTO `t_guide_like` VALUES (14, 4, 3, '2025-12-05 14:38:36');
INSERT INTO `t_guide_like` VALUES (15, 4, 1, '2025-12-05 14:38:44');
INSERT INTO `t_guide_like` VALUES (16, 1, 2, '2026-01-01 22:50:38');
INSERT INTO `t_guide_like` VALUES (17, 29, 1, '2026-01-31 22:07:03');
INSERT INTO `t_guide_like` VALUES (18, 32, 2, '2026-02-01 20:59:13');

-- ----------------------------
-- Table structure for t_notification
-- ----------------------------
DROP TABLE IF EXISTS `t_notification`;
CREATE TABLE `t_notification`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `related_id` bigint NULL DEFAULT NULL,
  `is_read` tinyint(1) NULL DEFAULT 0,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `deleted` tinyint NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_is_read`(`is_read` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '通知消息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_notification
-- ----------------------------

-- ----------------------------
-- Table structure for t_operation_log
-- ----------------------------
DROP TABLE IF EXISTS `t_operation_log`;
CREATE TABLE `t_operation_log`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NULL DEFAULT NULL,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `operation` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `method` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `params` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `result` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `ip` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `location` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `time` int NULL DEFAULT NULL,
  `status` tinyint NULL DEFAULT 1,
  `error_msg` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_create_time`(`create_time` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '操作日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_operation_log
-- ----------------------------

-- ----------------------------
-- Table structure for t_pet
-- ----------------------------
DROP TABLE IF EXISTS `t_pet`;
CREATE TABLE `t_pet`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `category` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `breed` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `gender` tinyint NULL DEFAULT 0,
  `age` int NULL DEFAULT NULL,
  `weight` decimal(5, 2) NULL DEFAULT NULL,
  `color` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `neutered` tinyint(1) NULL DEFAULT 0,
  `vaccinated` tinyint(1) NULL DEFAULT 0,
  `health_status` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'healthy',
  `health_description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `personality` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `images` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `cover_image` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `rescue_date` date NULL DEFAULT NULL,
  `rescue_location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `adoption_requirements` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `adoption_status` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'available',
  `adopted_by` bigint NULL DEFAULT NULL COMMENT '领养者ID',
  `shelf_status` tinyint NULL DEFAULT 1,
  `view_count` int NULL DEFAULT 0,
  `like_count` int NULL DEFAULT 0,
  `favorite_count` int NULL DEFAULT 0,
  `application_count` int NULL DEFAULT 0,
  `sort_order` int NULL DEFAULT 0,
  `create_by` bigint NULL DEFAULT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_category`(`category` ASC) USING BTREE,
  INDEX `idx_adoption_status`(`adoption_status` ASC) USING BTREE,
  INDEX `idx_name`(`name` ASC) USING BTREE,
  INDEX `idx_adopted_by`(`adopted_by` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 46 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '宠物表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_pet
-- ----------------------------
INSERT INTO `t_pet` VALUES (1, '小刺', 'hedgehog', '刺猬', 1, 6, 0.50, '棕色', 0, 1, 'healthy', NULL, '性格温顺, 喜欢安静的环境, 晚上比较活跃', '这是一只可爱的小刺猬, 身体健康, 适应能力强。喜欢吃水果和昆虫, 需要提供温暖干燥的环境。', '[\"http://localhost:9000/animal-adopt/刺猬1.png\"]', 'http://localhost:9000/animal-adopt/刺猬1.png', NULL, '城市公园', '需要有养刺猬经验, 能提供适宜的温度环境（20-25℃）, 定期清理笼舍', 'available', NULL, 1, 0, 0, 0, 0, 1, NULL, '2025-11-19 13:44:14', '2026-01-31 21:38:10', 0);
INSERT INTO `t_pet` VALUES (2, '小白', 'rabbit', '白兔', 2, 8, 2.50, '纯白色', 1, 1, 'healthy', NULL, '性格活泼可爱, 喜欢吃胡萝卜和青菜, 很亲人', '这是一只纯白色的可爱兔子, 毛发柔软, 眼睛明亮。已绝育并接种疫苗, 身体健康。喜欢在草地上奔跑, 适合有院子的家庭。', '[\"http://localhost:9000/animal-adopt/兔子1.jpg\"]', 'http://localhost:9000/animal-adopt/兔子1.jpg', NULL, '宠物救助站', '需要有足够的活动空间, 定期提供新鲜蔬菜和干草, 每天至少1小时的户外活动时间', 'available', NULL, 1, 0, 0, 0, 0, 2, NULL, '2025-11-19 13:44:14', '2026-01-31 21:38:10', 0);
INSERT INTO `t_pet` VALUES (3, '金金', 'dog', '金毛寻回犬', 1, 18, 28.50, '金黄色', 1, 1, 'healthy', NULL, '性格温顺友善, 聪明听话, 喜欢和人互动, 对小孩很有耐心', '这是一只帅气的金毛犬, 毛色金黄, 性格温顺。已完成绝育和疫苗接种, 训练有素, 会基本指令。非常适合家庭饲养, 是孩子们的好伙伴。', '[\"http://localhost:9000/animal-adopt/小狗1.png\"]', 'http://localhost:9000/animal-adopt/小狗1.png', NULL, '市区流浪犬救助中心', '需要有足够的活动空间, 每天遛狗2次以上, 定期梳理毛发, 有养大型犬经验者优先', 'available', NULL, 1, 0, 0, 0, 0, 3, NULL, '2025-11-19 13:44:14', '2026-01-31 21:38:10', 0);
INSERT INTO `t_pet` VALUES (4, '小柴', 'dog', '柴犬', 2, 12, 9.80, '赤色', 1, 1, 'healthy', NULL, '性格独立但忠诚, 活泼好动, 喜欢户外活动, 警惕性高', '这是一只可爱的柴犬, 标准的赤色毛发, 笑容治愈。已绝育并完成疫苗接种, 身体健康。性格活泼, 需要足够的运动量。', '[\"http://localhost:9000/animal-adopt/小狗2.jpg\"]', 'http://localhost:9000/animal-adopt/小狗2.jpg', NULL, '郊区宠物救助站', '需要每天至少1小时户外运动, 定期梳理毛发, 有养中型犬经验', 'available', NULL, 1, 2, 0, 0, 0, 4, NULL, '2025-11-19 13:44:14', '2026-02-01 21:10:00', 0);
INSERT INTO `t_pet` VALUES (5, '泰迪', 'dog', '泰迪犬', 1, 10, 4.20, '棕色', 1, 1, 'healthy', NULL, '性格活泼粘人, 聪明伶俐, 喜欢和主人互动, 适合陪伴', '这是一只可爱的泰迪犬, 毛发卷曲蓬松, 非常可爱。已绝育并接种疫苗, 身体健康。性格粘人, 适合需要陪伴的家庭。', '[\"http://localhost:9000/animal-adopt/小狗3.jpg\"]', 'http://localhost:9000/animal-adopt/小狗3.jpg', NULL, '市区宠物医院', '需要定期美容修剪毛发, 每天陪伴玩耍, 适合有时间照顾的家庭', 'available', NULL, 1, 0, 0, 0, 0, 5, NULL, '2025-11-19 13:44:14', '2026-01-31 21:38:10', 0);
INSERT INTO `t_pet` VALUES (6, '二哈', 'dog', '哈士奇', 1, 15, 22.00, '黑白色', 1, 1, 'healthy', NULL, '性格活泼好动, 精力充沛, 喜欢奔跑, 有时会有点调皮', '这是一只帅气的哈士奇, 标志性的蓝眼睛和黑白毛色。已绝育并完成疫苗接种, 精力旺盛, 需要大量运动。性格友善但有点调皮, 适合有经验的主人。', '[\"http://localhost:9000/animal-adopt/小狗4.png\"]', 'http://localhost:9000/animal-adopt/小狗4.png', NULL, '郊区救助站', '需要大量运动空间, 每天至少2小时户外活动, 有养大型犬经验, 能接受它的调皮性格', 'available', NULL, 1, 6, 0, 0, 1, 6, NULL, '2025-11-19 13:44:14', '2026-01-31 21:38:10', 0);
INSERT INTO `t_pet` VALUES (7, '小黑', 'dog', '边境牧羊犬', 1, 20, 18.50, '黑白色', 1, 1, 'healthy', NULL, '智商极高, 学习能力强, 精力充沛, 需要大量脑力和体力活动', '这是一只聪明的边牧, 黑白相间的毛色, 眼神专注。已绝育并完成疫苗接种, 智商很高, 学东西很快。需要主人有足够时间陪伴和训练。', '[\"http://localhost:9000/animal-adopt/小狗5.jpg\"]', 'http://localhost:9000/animal-adopt/小狗5.jpg', NULL, '训练基地', '需要大量运动和智力游戏, 每天至少2小时户外活动, 有养犬经验, 能提供足够的训练和刺激', 'available', NULL, 1, 1, 0, 0, 0, 7, NULL, '2025-11-19 13:44:14', '2026-01-31 21:45:00', 0);
INSERT INTO `t_pet` VALUES (8, '拉拉', 'dog', '拉布拉多寻回犬', 2, 24, 26.00, '黄色', 1, 1, 'healthy', NULL, '性格温顺友善, 对人友好, 喜欢游泳, 是优秀的家庭伴侣犬', '这是一只温柔的拉布拉多, 毛色金黄, 性格超好。已绝育并完成疫苗接种, 非常适合家庭饲养。喜欢和人互动, 对小孩特别有耐心。', '[\"http://localhost:9000/animal-adopt/小狗6.jpg\"]', 'http://localhost:9000/animal-adopt/小狗6.jpg', NULL, '市区救助中心', '需要足够的活动空间, 每天遛狗2次, 定期梳理毛发, 适合有院子的家庭', 'available', NULL, 1, 0, 0, 0, 0, 8, NULL, '2025-11-19 13:44:14', '2026-01-31 21:38:10', 0);
INSERT INTO `t_pet` VALUES (9, '圆圆', 'cat', '英国短毛猫', 2, 14, 4.50, '蓝灰色', 1, 1, 'healthy', NULL, '性格温顺安静, 喜欢睡觉, 不太爱动, 适合公寓饲养', '这是一只可爱的英短猫, 圆圆的脸蛋, 蓝灰色的毛发。已绝育并完成疫苗接种, 性格温顺, 不吵不闹, 非常适合公寓饲养。', '[\"http://localhost:9000/animal-adopt/小猫1.jpg\"]', 'http://localhost:9000/animal-adopt/小猫1.jpg', NULL, '宠物店', '需要定期梳理毛发, 提供猫砂盆和猫爬架, 定期体检', 'available', NULL, 1, 2, 0, 0, 0, 9, NULL, '2025-11-19 13:44:14', '2026-01-31 21:38:10', 0);
INSERT INTO `t_pet` VALUES (10, '虎斑', 'cat', '美国短毛猫', 1, 16, 5.20, '银色虎斑', 1, 1, 'healthy', NULL, '性格活泼好动, 喜欢玩耍, 好奇心强, 适应能力强', '这是一只帅气的美短猫, 银色虎斑纹路清晰。已绝育并完成疫苗接种, 性格活泼, 喜欢和人互动。身体健壮, 适应能力强。', '[\"http://localhost:9000/animal-adopt/小猫2.jpg\"]', 'http://localhost:9000/animal-adopt/小猫2.jpg', NULL, '流浪猫救助站', '需要提供玩具和猫爬架, 定期陪伴玩耍, 适合有时间的家庭', 'available', NULL, 1, 3, 1, 1, 0, 10, NULL, '2025-11-19 13:44:14', '2026-01-31 21:38:10', 0);
INSERT INTO `t_pet` VALUES (11, '橘子', 'cat', '中华田园猫', 1, 12, 6.00, '橘色', 1, 1, 'healthy', NULL, '性格亲人温顺, 食量较大, 喜欢晒太阳, 很会撒娇', '这是一只可爱的橘猫, 毛色橙黄, 圆滚滚的很可爱。已绝育并完成疫苗接种, 性格亲人, 喜欢和主人互动。食量比较大, 需要控制饮食。', '[\"http://localhost:9000/animal-adopt/小猫3.png\"]', 'http://localhost:9000/animal-adopt/小猫3.png', NULL, '小区救助', '需要控制饮食避免过胖, 提供猫砂盆和猫爬架, 定期体检', 'available', NULL, 1, 15, 0, 0, 2, 11, NULL, '2025-11-19 13:44:14', '2026-01-31 21:38:10', 0);
INSERT INTO `t_pet` VALUES (12, '小暹', 'cat', '暹罗猫', 2, 18, 3.80, '重点色', 1, 1, 'healthy', NULL, '性格活泼粘人, 喜欢和主人交流, 叫声独特, 需要陪伴', '这是一只优雅的暹罗猫, 蓝色眼睛, 重点色毛发。已绝育并完成疫苗接种, 性格粘人, 喜欢和主人说话。需要主人有足够时间陪伴。', '[\"http://localhost:9000/animal-adopt/小猫4.jpg\"]', 'http://localhost:9000/animal-adopt/小猫4.jpg', NULL, '宠物医院', '需要大量陪伴和互动, 不适合长时间独处, 适合在家办公或时间充裕的家庭', 'available', NULL, 1, 4, 0, 0, 0, 12, NULL, '2025-11-19 13:44:14', '2026-01-31 21:38:10', 0);
INSERT INTO `t_pet` VALUES (13, '布布', 'cat', '布偶猫', 2, 20, 5.50, '海豹双色', 1, 1, 'healthy', NULL, '性格温顺甜美, 喜欢被抱, 叫声轻柔, 是完美的家庭伴侣', '这是一只漂亮的布偶猫, 蓝色眼睛, 海豹双色毛发。已绝育并完成疫苗接种, 性格超级温顺, 喜欢被抱。毛发需要定期打理。', '[\"http://localhost:9000/animal-adopt/小猫5.jpg\"]', 'http://localhost:9000/animal-adopt/小猫5.jpg', NULL, '繁育基地', '需要每天梳理毛发, 提供高质量猫粮, 定期体检, 适合有养猫经验的家庭', 'available', NULL, 1, 19, 0, 0, 0, 13, NULL, '2025-11-19 13:44:14', '2026-01-31 21:38:10', 0);
INSERT INTO `t_pet` VALUES (14, '小狸', 'cat', '中华狸花猫', 1, 10, 4.80, '棕色虎斑', 1, 1, 'healthy', NULL, '性格独立机警, 身体强壮, 适应能力强, 是优秀的捕鼠能手', '这是一只帅气的狸花猫, 棕色虎斑纹路清晰。已绝育并完成疫苗接种, 性格独立但也亲人。身体强壮, 适应能力很强。', '[\"http://localhost:9000/animal-adopt/小猫6.jpg\"]', 'http://localhost:9000/animal-adopt/小猫6.jpg', NULL, '街道救助', '需要提供猫砂盆和猫爬架, 定期体检, 适合各类家庭', 'available', NULL, 1, 8, 0, 0, 0, 14, NULL, '2025-11-19 13:44:14', '2026-01-31 21:38:10', 0);
INSERT INTO `t_pet` VALUES (20, '耶耶', 'dog', '萨摩耶', 2, 5, 15.00, '白色', 0, 0, 'healthy', NULL, '温顺', '一只可爱的萨摩耶', '[\"http://localhost:9000/animal-adopt/pet-images/8962033cf5c44e629554661ac480f271.png\"]', 'http://localhost:9000/animal-adopt/pet-covers/5f372549aeed448590d9fc4e41e20c6b.png', NULL, NULL, NULL, 'adopted', 1, 1, 31, 1, 1, 1, 0, 1, '2025-11-23 00:30:33', '2026-01-31 22:10:00', 0);
INSERT INTO `t_pet` VALUES (21, '小博', 'dog', '博美犬', 1, 3, 10.00, '白色', 0, 0, 'healthy', NULL, '温顺', '一只可爱温顺的博美犬', '[\"http://localhost:9000/animal-adopt/pet-images/08352439a0244cd6aa188ec7b290f653.png\"]', 'http://localhost:9000/animal-adopt/pet-covers/0b0f079552b04de3952ea0e27c895a84.png', NULL, NULL, NULL, 'available', NULL, 1, 5, 0, 1, 0, 0, 1, '2025-11-24 14:13:34', '2026-01-31 21:38:10', 0);
INSERT INTO `t_pet` VALUES (22, '布布', 'cat', '布偶猫', 2, 3, 6.00, '白色', 0, 0, 'healthy', NULL, '调皮', '一只和小高一样调皮的可爱布偶猫', '[\"http://localhost:9000/animal-adopt/pet-images/c9a740e1c6b743b0b8930343a26678b9.png\"]', 'http://localhost:9000/animal-adopt/pet-covers/cd04017645314c5ca9a64e5d95e2600f.png', NULL, NULL, NULL, 'adopted', 29, 1, 13, 2, 3, 3, 0, 1, '2025-11-24 15:05:08', '2026-01-31 22:15:00', 0);
INSERT INTO `t_pet` VALUES (23, '边边', 'dog', '边牧犬', 1, 4, 14.00, '黑白相见', 0, 0, 'healthy', NULL, '活泼', '和小高一样皮', '[\"http://localhost:9000/animal-adopt/pet-images/39d3242eb0a84cdf8c55199a95a24840.png\"]', 'http://localhost:9000/animal-adopt/pet-covers/c68f5065d8a84819a75a0019c9d04927.png', NULL, NULL, NULL, 'adopted', 32, 1, 16, 2, 2, 1, 0, 1, '2025-11-24 15:06:49', '2026-02-01 21:02:50', 0);
INSERT INTO `t_pet` VALUES (24, '金金', 'dog', '金毛', 1, 4, 15.00, '棕色', 0, 0, 'healthy', NULL, '活泼', '一只金毛', '[\"http://localhost:9000/animal-adopt/pet-images/8f9b25c437a54df2927d477b46535406.png\"]', 'http://localhost:9000/animal-adopt/pet-covers/412005b4bf8c43b89c398a3e4b8160e9.png', NULL, NULL, NULL, 'available', NULL, 1, 10, 0, 0, 2, 0, 1, '2025-11-24 15:11:43', '2026-02-01 22:40:00', 0);
INSERT INTO `t_pet` VALUES (25, '松松', 'squirrel', '松鼠', 1, 3, 4.00, '黑棕色', 0, 0, 'healthy', NULL, '谨慎', '胆小的松鼠', '[\"http://localhost:9000/animal-adopt/pet-images/a3f842d804d14893a24f776594f76dc0.png\"]', 'http://localhost:9000/animal-adopt/pet-covers/a097fdcf5c2e43bf895465c537c2125f.png', NULL, NULL, NULL, 'available', NULL, 1, 23, 1, 1, 0, 0, 1, '2025-11-24 15:27:05', '2026-01-31 21:38:10', 0);
INSERT INTO `t_pet` VALUES (26, '小松', 'squirrel', '美洲红松鼠', 2, 3, 6.00, '红棕色', 0, 0, 'healthy', NULL, '活泼', '活泼的松鼠', '[\"http://localhost:9000/animal-adopt/pet-images/32cce10b71bd4c35b7a40053bbc6c894.png\"]', 'http://localhost:9000/animal-adopt/pet-covers/9eebd6cf4f3d47f1acaf6c8fedae6587.png', NULL, NULL, NULL, 'available', NULL, 1, 19, 1, 1, 1, 0, 1, '2025-11-24 15:45:43', '2026-01-31 22:05:00', 0);
INSERT INTO `t_pet` VALUES (27, '大松', 'squirrel', '美洲红松鼠', 1, 2, 2.00, '红棕色', 0, 0, 'healthy', NULL, '活泼', '松鼠', '[\"http://localhost:9000/animal-adopt/pet-images/d755515cc94b4185ac57897f5a056431.png\"]', 'http://localhost:9000/animal-adopt/pet-covers/53695e368c5e4024a39b8a2f220d89bb.png', NULL, NULL, NULL, 'available', NULL, 1, 0, 0, 0, 0, 0, 1, '2025-11-24 15:49:12', '2026-01-31 21:38:10', 1);
INSERT INTO `t_pet` VALUES (28, '大松', 'squirrel', '美洲红松鼠', 1, 2, 2.00, '红棕色', 0, 0, 'healthy', NULL, '胆小', '胆小的松鼠', '[\"http://localhost:9000/animal-adopt/pet-images/97c11e865e914da691a4700f6751db27.png\"]', 'http://localhost:9000/animal-adopt/pet-covers/c06a6676216847af8c1794ba188c63f8.png', NULL, NULL, NULL, 'available', NULL, 1, 0, 0, 0, 0, 0, 1, '2025-11-24 15:56:49', '2026-01-31 21:38:10', 1);
INSERT INTO `t_pet` VALUES (29, '大松', 'squirrel', '美洲红松鼠', 1, 3, 4.00, '红棕色', 0, 0, 'healthy', NULL, '谨慎', '谨慎的松鼠', '[\"http://localhost:9000/animal-adopt/pet-images/ab25cef1966448e6ab9d15c5f3c2f703.png\"]', 'http://localhost:9000/animal-adopt/pet-covers/50e686e36754405497e7ca53f4e6954e.png', NULL, NULL, NULL, 'available', NULL, 1, 0, 0, 0, 0, 0, 1, '2025-11-24 19:15:56', '2026-01-31 21:38:10', 1);
INSERT INTO `t_pet` VALUES (33, '仓仓', 'hamster', '仓鼠', 1, 2, 2.00, '白色', 0, 0, 'healthy', NULL, '胆小、谨慎', '小心的仓鼠', '[\"http://localhost:9000/animal-adopt/pet-images/5dbea0668a4342de8ddf71da8b9bc530.png\"]', 'http://localhost:9000/animal-adopt/pet-covers/5af36d9a6bcf4fbe983d78da208e0b19.png', NULL, NULL, NULL, 'available', NULL, 1, 25, 3, 3, 1, 0, 1, '2025-11-24 22:38:51', '2026-01-31 21:55:00', 0);
INSERT INTO `t_pet` VALUES (34, '熊大', 'cat', '熊', 1, 6, 200.00, '棕白色', 0, 0, 'healthy', NULL, '凶猛', '熊大', '[\"http://localhost:9000/animal-adopt/pet-images/68b633d3d7ef4421a402e64111aa34eb.jpg\"]', 'http://localhost:9000/animal-adopt/pet-covers/95f1b8e2488a4762b99007aea0d6c25b.jpg', NULL, NULL, NULL, 'available', NULL, 1, 0, 0, 0, 0, 0, 1, '2025-12-07 19:27:27', '2025-12-07 19:34:06', 1);
INSERT INTO `t_pet` VALUES (35, '熊大2', '熊', '熊', 1, 6, 300.00, '棕白色', 0, 0, 'healthy', NULL, '凶猛', '熊大', '[\"http://localhost:9000/animal-adopt/pet-images/cf122c4211714b58a3a8418d31a17346.jpg\"]', 'http://localhost:9000/animal-adopt/pet-covers/b14528717e1d4d1c81b4ca25025b5798.jpg', NULL, NULL, NULL, 'available', NULL, 1, 0, 0, 0, 0, 0, 1, '2025-12-07 19:29:42', '2025-12-07 19:33:54', 1);
INSERT INTO `t_pet` VALUES (36, '熊大', 'bear', '狗熊', 1, 7, 300.00, '棕白色', 0, 0, 'healthy', NULL, '凶猛', '凶猛的熊大', '[\"http://localhost:9000/animal-adopt/pet-images/7b7044333d374cf79424eb58d3d4370a.jpg\"]', 'http://localhost:9000/animal-adopt/pet-covers/e54fce360dc14b64bc3c3ffefa782165.jpg', NULL, NULL, NULL, 'available', NULL, 1, 0, 0, 0, 0, 0, 1, '2025-12-07 20:31:37', '2025-12-07 23:34:47', 1);
INSERT INTO `t_pet` VALUES (37, 'test', 'cat', 'test', 1, 3, 5.00, '白色', 0, 0, 'healthy', NULL, 'test', 'test', '[\"http://localhost:9000/animal-adopt/pet-images/41f4b664bd5340828a0eefc247528579.jpg\"]', 'http://localhost:9000/animal-adopt/pet-covers/027b86e72d314fe69751bb55be394347.jpg', NULL, NULL, NULL, 'available', NULL, 1, 0, 0, 0, 0, 0, 1, '2026-01-31 16:48:24', '2026-01-31 16:48:53', 1);
INSERT INTO `t_pet` VALUES (38, '1', 'bird', '1', 1, 1, 1.00, '1', 0, 0, 'healthy', NULL, '1', '1', '', '', NULL, NULL, NULL, 'available', NULL, 1, 2, 0, 0, 0, 0, 1, '2026-01-31 21:27:45', '2026-01-31 22:20:00', 0);
INSERT INTO `t_pet` VALUES (39, 'test', 'rabbit', '111', 1, 2, 2.00, '1111', 0, 0, 'healthy', NULL, '12121', '21212121test', '', '', NULL, NULL, NULL, 'available', NULL, 1, 0, 0, 0, 0, 0, 1, '2026-01-31 22:15:54', '2026-01-31 22:15:54', 0);
INSERT INTO `t_pet` VALUES (40, 'test', 'rabbit', '111', 1, 2, 2.00, '1111', 0, 0, 'healthy', NULL, '12121', '21212121test', '', '', NULL, NULL, NULL, 'available', NULL, 1, 0, 0, 0, 0, 0, 1, '2026-01-31 22:16:05', '2026-01-31 22:16:05', 0);
INSERT INTO `t_pet` VALUES (41, 'test', 'rabbit', '111', 1, 2, 2.00, '1111', 0, 0, 'healthy', NULL, '12121', '21212121test', '', '', NULL, NULL, NULL, 'available', NULL, 1, 0, 0, 0, 0, 0, 1, '2026-01-31 22:16:50', '2026-01-31 22:16:50', 0);
INSERT INTO `t_pet` VALUES (42, '111', 'dog', '11111', 1, 2, 2.00, '111', 0, 0, 'healthy', NULL, '111', '111', '', '', NULL, NULL, NULL, 'available', NULL, 1, 1, 0, 0, 0, 0, 1, '2026-01-31 22:18:51', '2026-02-01 21:00:00', 0);
INSERT INTO `t_pet` VALUES (43, '111', 'dog', '11111', 1, 2, 2.00, '111', 0, 0, 'healthy', NULL, '111', '111', '', '', NULL, NULL, NULL, 'available', NULL, 1, 1, 0, 0, 0, 0, 1, '2026-01-31 22:19:06', '2026-02-01 21:50:00', 0);
INSERT INTO `t_pet` VALUES (44, '111', 'dog', '11111', 1, 2, 2.00, '111', 0, 0, 'healthy', NULL, '111', '111', '', '', NULL, NULL, NULL, 'available', NULL, 1, 0, 0, 0, 0, 0, 1, '2026-01-31 22:19:46', '2026-01-31 22:19:46', 0);
INSERT INTO `t_pet` VALUES (45, '2', 'dog', '2', 1, 1, 1.00, '2', 0, 0, 'healthy', NULL, '2', '2', '[\"http://localhost:9000/animal-adopt/pet-images/32e6be8e734f4977be6de5ccc80a4ae0.jpg\"]', 'http://localhost:9000/animal-adopt/pet-covers/14c6d1c53ec64a2aaaaf7e29e2f272b6.jpg', NULL, NULL, NULL, 'available', NULL, 1, 3, 1, 1, 0, 0, 1, '2026-02-01 21:07:36', '2026-02-01 22:05:00', 0);

-- ----------------------------
-- Table structure for t_pet_favorite
-- ----------------------------
DROP TABLE IF EXISTS `t_pet_favorite`;
CREATE TABLE `t_pet_favorite`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `pet_id` bigint NOT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_user_pet`(`user_id` ASC, `pet_id` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_pet_id`(`pet_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 61 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '宠物收藏表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_pet_favorite
-- ----------------------------
INSERT INTO `t_pet_favorite` VALUES (1, 1, 14, '2025-11-19 11:27:35');
INSERT INTO `t_pet_favorite` VALUES (8, 1, 13, '2025-11-20 23:21:28');
INSERT INTO `t_pet_favorite` VALUES (9, 1, 10, '2025-11-21 20:20:32');
INSERT INTO `t_pet_favorite` VALUES (19, 1, 20, '2025-11-23 16:44:24');
INSERT INTO `t_pet_favorite` VALUES (21, 1, 21, '2025-11-28 16:12:31');
INSERT INTO `t_pet_favorite` VALUES (22, 4, 23, '2025-12-05 15:33:32');
INSERT INTO `t_pet_favorite` VALUES (23, 7, 33, '2025-12-20 12:05:36');
INSERT INTO `t_pet_favorite` VALUES (24, 7, 25, '2025-12-20 12:05:43');
INSERT INTO `t_pet_favorite` VALUES (25, 7, 26, '2025-12-22 08:57:31');
INSERT INTO `t_pet_favorite` VALUES (49, 1, 22, '2026-01-02 00:38:14');
INSERT INTO `t_pet_favorite` VALUES (55, 11, 22, '2026-01-02 00:46:55');
INSERT INTO `t_pet_favorite` VALUES (56, 11, 33, '2026-01-02 00:47:17');
INSERT INTO `t_pet_favorite` VALUES (57, 28, 33, '2026-01-31 21:52:41');
INSERT INTO `t_pet_favorite` VALUES (58, 29, 22, '2026-01-31 22:07:37');
INSERT INTO `t_pet_favorite` VALUES (59, 32, 23, '2026-02-01 20:59:26');
INSERT INTO `t_pet_favorite` VALUES (60, 32, 45, '2026-02-01 21:07:49');

-- ----------------------------
-- Table structure for t_pet_like
-- ----------------------------
DROP TABLE IF EXISTS `t_pet_like`;
CREATE TABLE `t_pet_like`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `pet_id` bigint NOT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_user_pet`(`user_id` ASC, `pet_id` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_pet_id`(`pet_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 59 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '宠物点赞表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_pet_like
-- ----------------------------
INSERT INTO `t_pet_like` VALUES (1, 1, 14, '2025-11-19 11:27:33');
INSERT INTO `t_pet_like` VALUES (8, 1, 13, '2025-11-20 23:21:27');
INSERT INTO `t_pet_like` VALUES (9, 1, 10, '2025-11-21 20:20:32');
INSERT INTO `t_pet_like` VALUES (17, 1, 20, '2025-11-23 16:44:25');
INSERT INTO `t_pet_like` VALUES (20, 4, 23, '2025-12-05 15:33:29');
INSERT INTO `t_pet_like` VALUES (21, 7, 33, '2025-12-20 12:05:36');
INSERT INTO `t_pet_like` VALUES (22, 7, 25, '2025-12-20 12:05:44');
INSERT INTO `t_pet_like` VALUES (23, 7, 26, '2025-12-22 08:57:31');
INSERT INTO `t_pet_like` VALUES (53, 11, 22, '2026-01-02 00:46:55');
INSERT INTO `t_pet_like` VALUES (54, 1, 33, '2026-01-02 00:47:15');
INSERT INTO `t_pet_like` VALUES (55, 28, 33, '2026-01-31 21:52:40');
INSERT INTO `t_pet_like` VALUES (56, 29, 22, '2026-01-31 22:07:38');
INSERT INTO `t_pet_like` VALUES (57, 32, 23, '2026-02-01 20:59:27');
INSERT INTO `t_pet_like` VALUES (58, 32, 45, '2026-02-01 21:07:49');

-- ----------------------------
-- Table structure for t_story
-- ----------------------------
DROP TABLE IF EXISTS `t_story`;
CREATE TABLE `t_story`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '标题',
  `excerpt` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '摘要',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'Markdown内容',
  `image` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '封面图片URL',
  `author` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '作者',
  `tags` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '标签，逗号分隔',
  `views` int NULL DEFAULT 0 COMMENT '浏览次数',
  `likes` int NULL DEFAULT 0 COMMENT '点赞数',
  `favorite_count` int NULL DEFAULT 0 COMMENT '收藏数',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_author`(`author` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '领养故事表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_story
-- ----------------------------
INSERT INTO `t_story` VALUES (1, '从小流浪到幸福家庭', '小黄在街头流浪了两年，直到遇到了爱心志愿者...', '# 从小流浪到幸福家庭\r\r\n\r\r小黄是一只金毛犬，在街头流浪了整整两年。它的毛发变得脏乱，身体也瘦弱不堪，但它依然保持着对人类的友善。\r\r\n\r\r## 遇见希望\r\r\n\r\r2023年的冬天，志愿者小王在巡查时发现了蜷缩在街角的小黄。看到它瑟瑟发抖的样子，小王毫不犹豫地将它带回了救助站。\r\r\n\r\r在救助站，兽医为小黄做了全面检查，发现它除了营养不良外，身体健康状况良好。经过一个月的精心照料，小黄恢复了往日的活力。\r\r\n\r\r## 寻找新家\r\r\n\r\r救助站工作人员开始为小黄寻找合适的领养家庭。他们收到了许多领养申请，经过仔细筛选和家访，最终选择了一家有爱心且有养犬经验的家庭。\r\r\n\r\r新主人是一对年轻夫妇，他们有一个宽敞的院子，非常适合小黄活动。第一次见面时，小黄就表现出了极大的热情，摇着尾巴围着他们转圈。\r\r\n\r\r## 幸福新生活\r\r\n\r\r如今的小黄已经完全融入了新家庭，它有了自己的小窝、玩具和定期的散步时间。主人说，小黄给他们带来了无尽的欢乐，是家庭不可缺少的一员。\r\r\n\r\r这个故事告诉我们，每一只流浪动物都值得被关爱，它们只是缺少一个机会去展现自己的美好。', 'https://192.168.99.228:9000/animal-adopt/7.png', '志愿者小王', '成功领养,狗狗', 147, 1, 1, '2025-11-20 22:53:07', '2026-02-09 21:45:00');
INSERT INTO `t_story` VALUES (2, '失散三年终重聚', '猫咪咪咪走失三年后，通过救助站的帮助终于与主人团聚...', '# 失散三年终重聚\r\r\n\r\r咪咪是一只英短蓝猫，三年前在一次搬家过程中意外走失。主人李先生和家人四处寻找，但始终没有找到它的踪迹。\r\r\n\r\r## 意外发现\r\r\n\r\r三年后的某天，救助站接收了一只流浪猫，经过检查发现它脖子上有一个模糊的 tattoo，经过仔细辨认，工作人员确认这就是走失的咪咪。\r\r\n\r\r通过 tattoo 上的信息，救助站联系到了李先生。当李先生听到这个消息时，简直不敢相信自己的耳朵。三年了，他从未放弃过寻找咪咪的希望。\r\r\n\r\r## 重逢时刻\r\r\n\r\r在救助站的重逢现场，李先生激动得热泪盈眶。而咪咪在看到主人的那一刻，也发出了熟悉的叫声，仿佛在说：\"我终于找到你了！\"\r\r\n\r\r经过检查，咪咪虽然瘦了一些，但身体健康状况良好。三年的流浪生活并没有磨灭它对主人的记忆和感情。\r\r\n\r\r## 珍惜当下\r\r\n\r\r回到家中，咪咪很快就适应了环境，它重新占据了自己最喜欢的沙发位置，仿佛从未离开过。\r\r\n\r\r李先生表示，这次重逢让他们更加珍惜彼此。他们为咪咪做了全面的健康检查，并决定以后无论去哪里都要更加小心，绝不再让它走失。', 'https://192.168.99.228:9000/animal-adopt/8.png', '救助站', '失散重聚,猫咪', 112, 1, 1, '2025-11-20 22:53:07', '2026-02-01 21:15:00');
INSERT INTO `t_story` VALUES (3, '从病重到康复', '受伤的流浪狗豆豆被救助后，经过精心治疗重获新生...', '# 从病重到康复\r\r\n\r\r豆豆是一只三岁的混血狗，被发现时已经奄奄一息。它的后腿被车撞伤，伤口感染严重，几乎没有人认为它能活下来。\r\r\n\r\r## 紧急救治\r\r\n\r\r救助站的医生为豆豆进行了紧急手术。手术过程中，医生们发现豆豆的伤情比预想的更严重，但他们没有放弃。经过6小时的手术，豆豆终于脱离了危险。\r\r\n\r\r## 康复之路\r\r\n\r\r康复期间，豆豆经历了巨大的痛苦。但在医护人员和志愿者的精心照料下，它逐渐恢复了精神。虽然后腿永久性受损，但豆豆学会了用前腿行走。\r\r\n\r\r## 新的开始\r\r\n\r\r一个月后，豆豆被一个充满爱心的家庭领养了。新主人为它配备了特殊的轮椅，豆豆现在可以像其他狗狗一样快乐地奔跑。', 'https://192.168.99.228:9000/animal-adopt/9.png', '宠物医生李医生', '救助故事,狗狗', 163, 1, 1, '2025-11-20 22:53:07', '2026-02-09 21:45:00');
INSERT INTO `t_story` VALUES (4, '高龄猫咪的新家', '八岁的老猫花花在救助站等待了半年，终于遇到了理解它的新主人...', '# 高龄猫咪的新家\r\r\n\r\r花花是一只八岁的波斯猫，因为主人搬家无法照顾而被送到了救助站。高龄的它在救助站里显得有些孤独，很少有人愿意领养一只老猫。\r\r\n\r\r## 漫长的等待\r\r\n\r\r半年过去了，花花在救助站里度过了它最孤独的日子。它每天都趴在窗边，看着来来往往的访客，希望有人能带它回家。\r\r\n\r\r## 命运的相遇\r\r\n\r\r直到有一天，一位退休的老奶奶来到了救助站。她说她也很孤独，需要一个伴侣。当她看到花花时，两个孤独的灵魂瞬间产生了共鸣。\r\r\n\r\r## 完美的匹配\r\r\n\r\r现在，花花和老奶奶每天都在一起。老奶奶为花花准备了最舒适的窝，花花则用温暖的陪伴填补了老奶奶心中的空缺。这是一个完美的匹配。', 'https://192.168.99.228:9000/animal-adopt/10.png', '领养者张女士', '成功领养,猫咪', 94, 1, 1, '2025-11-20 22:53:07', '2026-02-09 21:45:00');
INSERT INTO `t_story` VALUES (5, '残疾狗狗的逆袭', '三条腿的狗狗小黑通过训练成为了一只优秀的治疗犬...', '# 残疾狗狗的逆袭\r\r\n\r\r小黑原本是一只流浪狗，在一次车祸中失去了一条前腿。人们都认为它的人生已经完结，但小黑用行动证明了生命的韧性。\r\r\n\r\r## 不放弃的精神\r\r\n\r\r被救助后，小黑虽然失去了一条腿，但它从未放弃过。它用三条腿学会了走路、跑步，甚至可以跳跃。它的坚强感动了每一个见到它的人。\r\r\n\r\r## 特殊的训练\r\r\n\r\r一位专业的训练师看到了小黑的潜力，决定将它训练成一只治疗犬。经过六个月的训练，小黑成功了。\r\r\n\r\r## 新的使命\r\r\n\r\r现在，小黑在医院里工作，为患者们带去欢乐和治疗。它用自己的故事告诉每一个人：残疾不是终点，而是新的开始。', 'https://192.168.99.228:9000/animal-adopt/11.png', '训练师刘老师', '救助故事,狗狗', 225, 0, 1, '2025-11-20 22:53:07', '2026-02-09 21:45:00');
INSERT INTO `t_story` VALUES (6, '从恐惧到信任', '受过伤害的猫咪小白如何在新主人的关爱下重新学会信任...', '# 从恐惧到信任\r\r\n\r\r小白是一只曾经被虐待的猫咪。当它被救助站救出时，它对人类充满了恐惧和不信任。它躲在角落里，拒绝任何人的接近。\r\r\n\r\r## 漫长的治愈\r\r\n\r\r救助站的工作人员没有放弃。他们每天都坐在小白身边，用耐心和爱心慢慢接近它。几个月过去了，小白终于敢于接受抚摸。\r\r\n\r\r## 遇见真爱\r\r\n\r\r一位心理咨询师来到救助站，她看到了小白眼中的恐惧，也看到了它渴望被爱的渴望。她决定领养小白。\r\r\n\r\r## 重生\r\r\n\r\r在新主人的关爱下，小白逐渐打开了心扉。它学会了信任，学会了撒娇，学会了享受被爱的感觉。现在，小白是一只充满活力和爱心的猫咪，它用自己的故事告诉我们：爱可以治愈一切。', 'https://192.168.99.228:9000/animal-adopt/12.png', '心理咨询师赵老师', '成功领养,猫咪', 153, 0, 1, '2025-11-20 22:53:07', '2026-02-01 20:55:00');
INSERT INTO `t_story` VALUES (9, '被遗弃的希望', '暴雨天被弃桥洞的小狗小黑遭外卖小哥救助后重拾信任，如今成骑手身边的小明星，以经历诠释温暖能抵过伤害。', '# 从绝望到新生\n\n小黑是一只在暴雨天被遗弃在桥洞下的小狗。那天，它全身湿透，瑟瑟发抖，眼神中满是绝望与无助。\n\n## 意外的温暖\n\n一位路过的外卖小哥发现了小黑。他撑起伞，小心地抱起它，用身上的外套为它取暖。虽然小黑已经几乎失去了力气，但它依然努力摇着尾巴回应善意。\n\n## 重拾信任\n\n在随后的一个月里，小哥带着小黑去看医生，给它洗澡、梳毛，陪它散步。起初，小黑害怕接触人类，每次看到陌生人都会躲进桌子底下。但一次次温柔的抚摸与耐心的鼓励，让它重新感受到了安全。\n\n## 新的家\n\n如今，小黑已经成为骑手中的小明星。它每天都跟着主人跑单，见到熟悉的店家会主动摇尾巴问好。它的故事告诉我们：*世界上再多伤害，也抵不过一次真正的温暖。*\n', 'images/f4cfb61a6f934691ab36699fd35885b8.png', '外卖小哥', '温暖救助,生命救赎', 31, 3, 2, '2025-11-26 21:22:41', '2026-02-09 21:45:00');
INSERT INTO `t_story` VALUES (10, 'Test', 'Test', '# Test\n\n## 我是测试二级标题\n\n### 我是三级标题\n\n```java\nSystem.out\n```', 'http://localhost:9000/animal-adopt/images/53ac91d08e7245b69521cb03428b6ded.jpg', '无名氏', 'Test', 4, 1, 1, '2026-02-01 21:11:39', '2026-02-09 21:45:00');

-- ----------------------------
-- Table structure for t_story_favorite
-- ----------------------------
DROP TABLE IF EXISTS `t_story_favorite`;
CREATE TABLE `t_story_favorite`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `story_id` bigint NOT NULL COMMENT '故事ID',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_user_story`(`user_id` ASC, `story_id` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_story_id`(`story_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '故事收藏记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_story_favorite
-- ----------------------------
INSERT INTO `t_story_favorite` VALUES (1, 1, 6, '2025-11-21 23:08:58');
INSERT INTO `t_story_favorite` VALUES (2, 1, 2, '2025-11-23 17:16:52');
INSERT INTO `t_story_favorite` VALUES (3, 1, 3, '2025-11-26 14:58:01');
INSERT INTO `t_story_favorite` VALUES (4, 1, 7, '2025-11-26 20:12:47');
INSERT INTO `t_story_favorite` VALUES (5, 1, 9, '2025-11-26 21:23:11');
INSERT INTO `t_story_favorite` VALUES (6, 4, 4, '2025-12-05 13:54:32');
INSERT INTO `t_story_favorite` VALUES (7, 11, 5, '2026-01-01 22:50:54');
INSERT INTO `t_story_favorite` VALUES (8, 28, 1, '2026-01-31 21:52:47');
INSERT INTO `t_story_favorite` VALUES (9, 29, 9, '2026-01-31 22:07:23');
INSERT INTO `t_story_favorite` VALUES (10, 32, 10, '2026-02-01 21:11:54');

-- ----------------------------
-- Table structure for t_story_like
-- ----------------------------
DROP TABLE IF EXISTS `t_story_like`;
CREATE TABLE `t_story_like`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `story_id` bigint NOT NULL COMMENT '故事ID',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_user_story`(`user_id` ASC, `story_id` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_story_id`(`story_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '故事点赞记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_story_like
-- ----------------------------
INSERT INTO `t_story_like` VALUES (3, 1, 3, '2025-11-20 22:55:23');
INSERT INTO `t_story_like` VALUES (7, 1, 6, '2025-11-21 23:09:05');
INSERT INTO `t_story_like` VALUES (8, 1, 2, '2025-11-23 17:16:51');
INSERT INTO `t_story_like` VALUES (9, 1, 7, '2025-11-26 20:12:47');
INSERT INTO `t_story_like` VALUES (10, 1, 9, '2025-11-26 21:23:12');
INSERT INTO `t_story_like` VALUES (11, 4, 4, '2025-12-05 13:54:34');
INSERT INTO `t_story_like` VALUES (12, 4, 9, '2025-12-05 14:38:53');
INSERT INTO `t_story_like` VALUES (13, 28, 1, '2026-01-31 21:52:48');
INSERT INTO `t_story_like` VALUES (14, 29, 9, '2026-01-31 22:07:22');
INSERT INTO `t_story_like` VALUES (15, 32, 10, '2026-02-01 21:11:53');

-- ----------------------------
-- Table structure for t_system_config
-- ----------------------------
DROP TABLE IF EXISTS `t_system_config`;
CREATE TABLE `t_system_config`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `config_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `config_value` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `config_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `config_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'string',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_config_key`(`config_key` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '系统配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_system_config
-- ----------------------------

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '密码',
  `nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '昵称',
  `real_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '真实姓名',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '手机号',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '邮箱',
  `avatar` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '头像URL',
  `gender` tinyint NULL DEFAULT 0 COMMENT '性别',
  `age` int NULL DEFAULT NULL COMMENT '年龄',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '地址',
  `id_card` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '身份证号',
  `occupation` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '职业',
  `housing` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '住房情况',
  `personality` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '性格特点',
  `has_experience` tinyint(1) NULL DEFAULT 0 COMMENT '是否有养宠经验',
  `role` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'user' COMMENT '用户角色',
  `status` tinyint NULL DEFAULT 1 COMMENT '用户状态',
  `certified` tinyint(1) NULL DEFAULT 0 COMMENT '是否已认证',
  `certification_files` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '认证资料URL',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_username`(`username` ASC) USING BTREE,
  UNIQUE INDEX `uk_phone`(`phone` ASC) USING BTREE,
  UNIQUE INDEX `uk_email`(`email` ASC) USING BTREE,
  INDEX `idx_role`(`role` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 35 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES (1, '普信小林', '$2a$10$jCsFThvTK3KCnCXmaRR1te4xQpRjfpM0A9wsxCEhA0hg31YupQB8O', '超级管理员', NULL, '13000000000', '3140000000@qq.com', 'avatars/e01c38ad0612481a8c5d8940da5a8c29.jpg', 0, NULL, '中国福建省漳州市xx县', NULL, NULL, NULL, NULL, 0, 'super_admin', 1, 1, NULL, '2025-11-19 11:16:41', '2025-12-16 14:39:35', 0);
INSERT INTO `t_user` VALUES (2, 'auditor', '$2a$10$smWyW1Z7pZq.nzzYNaCLdOrzUE6p3Pz3vZAKnkv4pshEX8jebulQi', '审核员', NULL, NULL, NULL, 'https://192.168.99.228:9000/animal-adopt/微信图片_20250929211436_125_10.jpg', 0, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'application_auditor', 1, 1, NULL, '2025-11-19 11:16:41', '2025-11-19 22:19:40', 0);
INSERT INTO `t_user` VALUES (3, 'admin', '$2a$10$smWyW1Z7pZq.nzzYNaCLdOrzUE6p3Pz3vZAKnkv4pshEX8jebulQi', '管理员', NULL, NULL, NULL, 'https://192.168.99.228:9000/animal-adopt/微信图片_20250929211439_126_10.jpg', 0, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'admin', 1, 1, NULL, '2025-11-19 11:16:41', '2025-12-16 14:39:35', 0);
INSERT INTO `t_user` VALUES (4, '林林1', '$2a$10$SIIObX9KGifpHzdPg52ruuzfx3DdJmzyDqK427UzJiNYkj6EA8Mq.', NULL, NULL, '13000000001', '3140000001@qq.com', 'http://localhost:9000/animal-adopt/avatars/b940a95eafa84210a7fe780d005ae7d7.jpg', 0, NULL, '中国福建省', NULL, NULL, NULL, NULL, 0, 'user', 1, 1, NULL, '2025-12-05 13:51:40', '2025-12-11 19:21:44', 0);
INSERT INTO `t_user` VALUES (5, '林林2', '$2a$10$MIKswZrzCIjZqn/d0ZNXYeHW164rjbSGpVRH2p3xJi2mnERNxaIJC', NULL, NULL, NULL, '3140000002@qq.com', NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'user', 1, 0, NULL, '2025-12-11 19:05:40', '2025-12-11 19:05:40', 0);
INSERT INTO `t_user` VALUES (6, '林林3', '$2a$10$6C8zq6T.hZiFQh9zxWv9KumqXFWOI7Oatms7PxYGShqbKOH5gP5IK', NULL, NULL, '13000000002', NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'user', 1, 0, NULL, '2025-12-11 19:22:01', '2025-12-11 19:22:01', 0);
INSERT INTO `t_user` VALUES (7, '林林4', '$2a$10$Evz3oLjVn6UoHPhwy4P17uoJjJzA/pdaNqSVXOh9LNoOVNWWnt2oO', NULL, NULL, NULL, '3140000003@qq.com', 'http://localhost:9000/animal-adopt/avatars/c72fb1627b004dd0bc004399fc97cb56.jpg', 0, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'user', 1, 0, NULL, '2025-12-14 22:43:18', '2025-12-14 22:43:18', 0);
INSERT INTO `t_user` VALUES (8, '林林5', '$2a$10$9BB.Gv49TXVqPvPYqWuLbOOsUrWD1zhoFd/ImCQFTjsxgOYTuhhFm', NULL, NULL, NULL, '3140000004@qq.com', NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'user', 1, 0, NULL, '2025-12-21 20:56:33', '2025-12-21 20:56:33', 0);
INSERT INTO `t_user` VALUES (9, '13000000003', '$2a$10$4Lvwz7JHR4F9PyVRMCfC9OqHCTNaiAxMpx9l8SnZqkpdeLSIyioSW', NULL, NULL, '13000000003', NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'user', 1, 0, NULL, '2026-01-01 11:03:30', '2026-01-01 11:03:30', 0);
INSERT INTO `t_user` VALUES (10, '3140000005@qq.com', '$2a$10$E68i3mTL2TQwmj3r.ZuFpOXxgfmPf4aqVQyccUUUApgGdHCoLTo9e', NULL, NULL, NULL, '3140000005@qq.com', NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'user', 1, 0, NULL, '2026-01-01 11:05:27', '2026-01-01 11:05:27', 0);
INSERT INTO `t_user` VALUES (11, '3140000006@qq.com', NULL, NULL, NULL, NULL, '3140000006@qq.com', NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'user', 1, 0, NULL, '2026-01-01 11:52:14', '2026-01-28 21:02:42', 0);
INSERT INTO `t_user` VALUES (12, '13000000004', NULL, NULL, NULL, '13000000004', NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'user', 1, 0, NULL, '2026-01-20 22:12:00', '2026-01-31 16:28:00', 0);
INSERT INTO `t_user` VALUES (15, '3140000007@qq.com', NULL, '用户.com', NULL, NULL, '3140000007@qq.com', NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'user', 1, 0, NULL, '2026-01-28 21:32:43', '2026-01-31 16:28:18', 0);
INSERT INTO `t_user` VALUES (16, '3149696140@qq.com_deleted_1769863723', NULL, '用户0279e7f7', NULL, NULL, '3149696140@qq.com_deleted_1769863723', NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'user', 1, 0, NULL, '2026-01-31 17:06:47', '2026-01-31 20:48:43', 1);
INSERT INTO `t_user` VALUES (26, '3140000008@qq.com', NULL, '用户13501137', NULL, NULL, '3140000008@qq.com', NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'user', 1, 0, NULL, '2026-01-31 20:48:43', '2026-01-31 20:52:54', 0);
INSERT INTO `t_user` VALUES (27, '3140000009@qq.com', NULL, '用户3eaa4349', NULL, NULL, '3140000009@qq.com', NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'user', 1, 0, NULL, '2026-01-31 20:52:56', '2026-01-31 21:52:20', 0);
INSERT INTO `t_user` VALUES (28, '3140000010@qq.com', NULL, '用户71aada55', NULL, '13000000005', '3140000010@qq.com', NULL, 0, NULL, '中国福建省', NULL, NULL, NULL, NULL, 0, 'user', 1, 1, NULL, '2026-01-31 21:52:32', '2026-01-31 21:57:26', 0);
INSERT INTO `t_user` VALUES (29, '3140000011@qq.com', '$2a$10$FSkzYloqa46RThBr5AIVe.WJ.LNRYCUetfTue71Y67eDk9Re2cCcC', '用户e287faa4', NULL, '13000000006', '3140000011@qq.com', NULL, 0, NULL, '中国福建省', NULL, NULL, NULL, NULL, 0, 'user', 1, 1, NULL, '2026-01-31 22:05:09', '2026-02-01 19:42:49', 0);
INSERT INTO `t_user` VALUES (30, '3140000012@qq.com', NULL, '用户f2364fc8', NULL, NULL, '3140000012@qq.com', NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'user', 1, 0, NULL, '2026-02-01 19:49:07', '2026-02-01 20:55:35', 0);
INSERT INTO `t_user` VALUES (31, '13000000008', NULL, '用户e51f4da2', NULL, '13000000008', NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'user', 1, 0, NULL, '2026-02-01 20:49:30', '2026-02-01 22:17:59', 0);
INSERT INTO `t_user` VALUES (32, '林林6', '$2a$10$gHUHe7PCqvOZGzrppkht1e9jvdzOyzYURGxKPehAkEBZFgnn2gVVC', '用户19301c4f', NULL, '13000000007', '3140000013@qq.com', 'http://localhost:9000/animal-adopt/avatars/d9a794e5ec4840c999af1be71961e0d7.jpg', 0, NULL, '中国福建省', NULL, NULL, NULL, NULL, 0, 'admin', 1, 1, NULL, '2026-02-01 20:56:15', '2026-02-01 20:56:15', 0);
INSERT INTO `t_user` VALUES (33, '用户2504f264A', NULL, '管理员', NULL, '13000000009', '3140000014@qq.com', NULL, 0, NULL, '中国', NULL, NULL, NULL, NULL, 0, 'admin', 1, 1, NULL, '2026-02-01 22:18:14', '2026-02-01 22:18:14', 0);
INSERT INTO `t_user` VALUES (34, '神秘人1', NULL, '普通用户', NULL, '13000000010', '3140000015@qq.com', NULL, 0, NULL, '中国', NULL, NULL, NULL, NULL, 0, 'user', 1, 1, NULL, '2026-02-01 22:38:04', '2026-02-01 22:38:04', 0);

-- ----------------------------
-- Table structure for t_user_certification
-- ----------------------------
DROP TABLE IF EXISTS `t_user_certification`;
CREATE TABLE `t_user_certification`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '认证ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `id_card` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '身份证号',
  `id_card_front_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '身份证正面URL',
  `id_card_back_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '身份证反面URL',
  `status` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'not_submitted' COMMENT '认证状态',
  `reject_reason` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '拒绝原因',
  `reviewer_id` bigint NULL DEFAULT NULL COMMENT '审核人ID',
  `review_time` datetime NULL DEFAULT NULL COMMENT '审核时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE,
  INDEX `idx_create_time`(`create_time` ASC) USING BTREE,
  INDEX `idx_user_id_create_time`(`user_id` ASC, `create_time` DESC) USING BTREE,
  INDEX `idx_status_create_time`(`status` ASC, `create_time` DESC) USING BTREE,
  INDEX `idx_id_card`(`id_card` ASC) USING BTREE,
  CONSTRAINT `fk_user_cert_user` FOREIGN KEY (`user_id`) REFERENCES `t_user` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户认证表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_user_certification
-- ----------------------------
INSERT INTO `t_user_certification` VALUES (1, 1, '350626200000000000', 'certification/21f88ea991604506bd370feda551bc21.jpg', 'certification/51452f10a84a4900a05cbf90e0f12f49.jpg', 'approved', NULL, 1, '2025-11-27 01:14:26', '2025-11-26 23:53:40', '2025-11-26 23:53:40', 0);
INSERT INTO `t_user_certification` VALUES (2, 4, '350626200000000001', 'certification/5d12ee189e96411fa5f389634cf1037d.jpg', 'certification/b172f056febe406490baafc2a81ff984.jpg', 'approved', '今天没空', 1, '2025-12-05 17:21:57', '2025-12-05 15:30:34', '2025-12-05 17:21:52', 0);
INSERT INTO `t_user_certification` VALUES (3, 28, '350626200000000002', 'http://localhost:9000/animal-adopt/certification/fcc3a2bc61da4267b34a550379c5a63c.jpg', 'http://localhost:9000/animal-adopt/certification/6fcdd2637d5a432db93381adcc5e6e51.jpg', 'approved', NULL, 1, '2026-01-31 21:55:37', '2026-01-31 21:54:54', '2026-01-31 21:54:54', 0);
INSERT INTO `t_user_certification` VALUES (4, 29, '350626200000000003', 'http://localhost:9000/animal-adopt/certification/ef332a4294844ce9bde5a89a690794be.jpg', 'http://localhost:9000/animal-adopt/certification/5295749eb9234e33955826bb4d54c018.jpg', 'approved', NULL, 1, '2026-01-31 22:09:42', '2026-01-31 22:09:21', '2026-01-31 22:09:21', 0);
INSERT INTO `t_user_certification` VALUES (5, 32, '350626200000000004', 'http://localhost:9000/animal-adopt/certification/dea9f509666f465bb2f29584bcf3c6b6.jpg', 'http://localhost:9000/animal-adopt/certification/5b667a8872ec4651a40afeec628db0ac.jpg', 'approved', NULL, 1, '2026-02-01 21:01:55', '2026-02-01 21:01:17', '2026-02-01 21:01:17', 0);
INSERT INTO `t_user_certification` VALUES (6, 33, '350626200000000005', 'http://localhost:9000/animal-adopt/certification/efd5c8312e45459aa4440a96cfa38cd2.jpg', 'http://localhost:9000/animal-adopt/certification/2863bf017c724846a4f80d2d677d4117.jpg', 'approved', NULL, 1, '2026-02-01 22:20:52', '2026-02-01 22:19:03', '2026-02-01 22:19:03', 0);
INSERT INTO `t_user_certification` VALUES (7, 34, '350626200000000006', 'http://localhost:9000/animal-adopt/certification/0d57525231fc4eb3aa67db65b1b15290.jpg', 'http://localhost:9000/animal-adopt/certification/c00f9173da874032b734bba863ee705f.jpg', 'approved', NULL, 1, '2026-02-01 22:39:13', '2026-02-01 22:39:00', '2026-02-01 22:39:00', 0);

-- ----------------------------
-- Table structure for t_verification_code
-- ----------------------------
DROP TABLE IF EXISTS `t_verification_code`;
CREATE TABLE `t_verification_code`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `code_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'email/phone',
  `target` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `code` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `purpose` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'register/login/reset_password',
  `expire_time` datetime NOT NULL,
  `is_used` tinyint(1) NULL DEFAULT 0,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_target_type`(`target` ASC, `code_type` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 58 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '验证码表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_verification_code
-- ----------------------------
INSERT INTO `t_verification_code` VALUES (1, 'phone', '13063184972', '169097', 'login', '2025-12-05 13:56:23', 1, '2025-12-05 13:51:23');
INSERT INTO `t_verification_code` VALUES (2, 'phone', '13063184972', '865767', 'login', '2025-12-05 14:36:08', 1, '2025-12-05 14:31:08');
INSERT INTO `t_verification_code` VALUES (3, 'email', '3149696140@qq.com', '264774', 'login', '2025-12-11 19:10:13', 0, '2025-12-11 19:05:13');
INSERT INTO `t_verification_code` VALUES (4, 'email', '3149696140@qq.com', '721443', 'login', '2025-12-11 19:10:22', 1, '2025-12-11 19:05:22');
INSERT INTO `t_verification_code` VALUES (5, 'phone', '13063184972', '666439', 'login', '2025-12-11 19:26:46', 1, '2025-12-11 19:21:46');
INSERT INTO `t_verification_code` VALUES (6, 'email', '3149696140@qq.com', '507016', 'login', '2025-12-14 22:47:09', 1, '2025-12-14 22:42:09');
INSERT INTO `t_verification_code` VALUES (7, 'phone', '13000000000', '309574', 'login', '2025-12-21 21:00:30', 0, '2025-12-21 20:55:30');
INSERT INTO `t_verification_code` VALUES (8, 'email', '3149696140@qq.com', '398394', 'login', '2025-12-21 21:00:53', 1, '2025-12-21 20:55:53');
INSERT INTO `t_verification_code` VALUES (9, 'email', '3149696140@qq.com', '782073', 'login', '2025-12-21 21:38:22', 0, '2025-12-21 21:33:22');
INSERT INTO `t_verification_code` VALUES (10, 'email', '3140000004@qq.com', '006171', 'login', '2025-12-22 14:19:02', 1, '2025-12-22 14:14:02');
INSERT INTO `t_verification_code` VALUES (11, 'phone', '13063184972', '756490', 'login', '2026-01-01 11:08:13', 1, '2026-01-01 11:03:13');
INSERT INTO `t_verification_code` VALUES (12, 'email', '3149696140@qq.com', '552551', 'login', '2026-01-01 11:10:07', 1, '2026-01-01 11:05:07');
INSERT INTO `t_verification_code` VALUES (13, 'email', '3149696140@qq.com', '775600', 'login', '2026-01-01 11:56:54', 1, '2026-01-01 11:51:54');
INSERT INTO `t_verification_code` VALUES (14, 'phone', '13063184972', '108850', 'login', '2026-01-20 22:15:11', 1, '2026-01-20 22:10:11');
INSERT INTO `t_verification_code` VALUES (15, 'phone', '13063184972', '003167', 'login', '2026-01-20 22:17:53', 0, '2026-01-20 22:12:53');
INSERT INTO `t_verification_code` VALUES (16, 'phone', '13063184972', '121694', 'login', '2026-01-20 22:18:55', 1, '2026-01-20 22:13:55');
INSERT INTO `t_verification_code` VALUES (17, 'phone', '13063184972', '697027', 'login', '2026-01-20 22:25:46', 1, '2026-01-20 22:20:46');
INSERT INTO `t_verification_code` VALUES (18, 'phone', '13063184972', '470245', 'login', '2026-01-20 22:26:55', 0, '2026-01-20 22:21:55');
INSERT INTO `t_verification_code` VALUES (19, 'email', '3149696140@qq.com', '243137', 'login', '2026-01-28 19:54:39', 1, '2026-01-28 19:49:39');
INSERT INTO `t_verification_code` VALUES (20, 'email', '3149696140@qq.com', '655272', 'register', '2026-01-28 20:49:21', 0, '2026-01-28 20:44:21');
INSERT INTO `t_verification_code` VALUES (21, 'email', '3149696140@qq.com', '164055', 'register', '2026-01-28 20:55:04', 0, '2026-01-28 20:50:04');
INSERT INTO `t_verification_code` VALUES (22, 'email', '3149696140@qq.com', '636206', 'register', '2026-01-28 20:57:40', 0, '2026-01-28 20:52:40');
INSERT INTO `t_verification_code` VALUES (23, 'email', '3149696140@qq.com', '592062', 'register', '2026-01-28 21:01:56', 0, '2026-01-28 20:56:56');
INSERT INTO `t_verification_code` VALUES (24, 'email', '3149696140@qq.com', '106910', 'register', '2026-01-28 21:08:08', 1, '2026-01-28 21:03:08');
INSERT INTO `t_verification_code` VALUES (25, 'email', '3149696140@qq.com', '209525', 'register', '2026-01-28 21:09:09', 1, '2026-01-28 21:04:09');
INSERT INTO `t_verification_code` VALUES (26, 'phone', '13063184972', '701538', 'register', '2026-01-28 21:13:23', 0, '2026-01-28 21:08:23');
INSERT INTO `t_verification_code` VALUES (27, 'phone', '13063184972', '111501', 'register', '2026-01-28 21:24:19', 0, '2026-01-28 21:19:19');
INSERT INTO `t_verification_code` VALUES (28, 'email', '3149696140@qq.com', '703947', 'register', '2026-01-28 21:25:26', 0, '2026-01-28 21:20:26');
INSERT INTO `t_verification_code` VALUES (29, 'phone', '13063184972', '192730', 'login', '2026-01-28 21:28:33', 1, '2026-01-28 21:23:33');
INSERT INTO `t_verification_code` VALUES (30, 'phone', '13063184972', '779210', 'login', '2026-01-28 21:29:41', 0, '2026-01-28 21:24:41');
INSERT INTO `t_verification_code` VALUES (31, 'phone', '13063184972', '334357', 'login', '2026-01-28 21:31:52', 0, '2026-01-28 21:26:52');
INSERT INTO `t_verification_code` VALUES (32, 'email', '3149696140@qq.com', '528539', 'register', '2026-01-28 21:37:25', 1, '2026-01-28 21:32:25');
INSERT INTO `t_verification_code` VALUES (33, 'email', '3149696140@qq.com', '575767', 'login', '2026-01-28 21:38:26', 1, '2026-01-28 21:33:26');
INSERT INTO `t_verification_code` VALUES (34, 'email', '3149696140@qq.com', '100242', 'login', '2026-01-28 21:39:30', 0, '2026-01-28 21:34:30');
INSERT INTO `t_verification_code` VALUES (35, 'email', '3149696140@qq.com', '602191', 'register', '2026-01-31 17:11:23', 1, '2026-01-31 17:06:23');
INSERT INTO `t_verification_code` VALUES (36, 'email', '3149696140@qq.com', '821414', 'register', '2026-01-31 17:30:38', 0, '2026-01-31 17:25:38');
INSERT INTO `t_verification_code` VALUES (37, 'email', '3149696140@qq.com', '990178', 'register', '2026-01-31 20:36:14', 0, '2026-01-31 20:31:14');
INSERT INTO `t_verification_code` VALUES (38, 'email', '3149696140@qq.com', '370167', 'register', '2026-01-31 20:40:58', 0, '2026-01-31 20:35:58');
INSERT INTO `t_verification_code` VALUES (39, 'email', '3149696140@qq.com', '719419', 'register', '2026-01-31 20:42:49', 0, '2026-01-31 20:37:49');
INSERT INTO `t_verification_code` VALUES (40, 'email', '3149696140@qq.com', '320846', 'register', '2026-01-31 20:53:33', 1, '2026-01-31 20:48:33');
INSERT INTO `t_verification_code` VALUES (41, 'email', '3149696140@qq.com', '692912', 'register', '2026-01-31 20:54:44', 0, '2026-01-31 20:49:44');
INSERT INTO `t_verification_code` VALUES (42, 'email', '3149696140@qq.com', '220386', 'register', '2026-01-31 20:57:01', 1, '2026-01-31 20:52:01');
INSERT INTO `t_verification_code` VALUES (43, 'email', '3149696140@qq.com', '934234', 'register', '2026-01-31 21:56:20', 0, '2026-01-31 21:51:20');
INSERT INTO `t_verification_code` VALUES (44, 'email', '3149696140@qq.com', '687713', 'register', '2026-01-31 21:57:23', 1, '2026-01-31 21:52:23');
INSERT INTO `t_verification_code` VALUES (45, 'email', '3140000010@qq.com', '092083', 'login', '2026-01-31 22:01:16', 0, '2026-01-31 21:56:16');
INSERT INTO `t_verification_code` VALUES (46, 'email', '3149696140@qq.com', '187540', 'register', '2026-01-31 22:09:49', 1, '2026-01-31 22:04:49');
INSERT INTO `t_verification_code` VALUES (47, 'email', '3149696140@qq.com', '984801', 'register', '2026-02-01 19:53:31', 1, '2026-02-01 19:48:31');
INSERT INTO `t_verification_code` VALUES (48, 'email', '3149696140@qq.com', '282278', 'register', '2026-02-01 20:11:36', 0, '2026-02-01 20:06:36');
INSERT INTO `t_verification_code` VALUES (49, 'email', '3149696140@qq.com', '304994', 'register', '2026-02-01 20:37:06', 0, '2026-02-01 20:32:06');
INSERT INTO `t_verification_code` VALUES (50, 'email', '3149696140@qq.com', '533191', 'register', '2026-02-01 20:48:24', 1, '2026-02-01 20:43:24');
INSERT INTO `t_verification_code` VALUES (51, 'email', '3149696140@qq.com', '383941', 'register', '2026-02-01 20:51:52', 1, '2026-02-01 20:46:52');
INSERT INTO `t_verification_code` VALUES (52, 'email', '3149696140@qq.com', '965413', 'register', '2026-02-01 20:53:32', 0, '2026-02-01 20:48:32');
INSERT INTO `t_verification_code` VALUES (53, 'phone', '13063184972', '171299', 'register', '2026-02-01 20:54:14', 1, '2026-02-01 20:49:14');
INSERT INTO `t_verification_code` VALUES (54, 'email', '3149696140@qq.com', '448819', 'register', '2026-02-01 20:59:49', 1, '2026-02-01 20:54:49');
INSERT INTO `t_verification_code` VALUES (55, 'email', '3149696140@qq.com', '583141', 'register', '2026-02-01 21:00:54', 1, '2026-02-01 20:55:54');
INSERT INTO `t_verification_code` VALUES (56, 'phone', '13063184972', '438701', 'register', '2026-02-01 22:23:02', 1, '2026-02-01 22:18:02');
INSERT INTO `t_verification_code` VALUES (57, 'phone', '13063184972', '342105', 'register', '2026-02-01 22:42:53', 1, '2026-02-01 22:37:53');

-- ----------------------------
-- Table structure for t_visit_log
-- ----------------------------
DROP TABLE IF EXISTS `t_visit_log`;
CREATE TABLE `t_visit_log`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `visit_date` date NOT NULL COMMENT '访问日期（按天唯一）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_user_date`(`user_id` ASC, `visit_date` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 54 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '每日 uv 记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_visit_log
-- ----------------------------
INSERT INTO `t_visit_log` VALUES (1, 1, '2025-12-06', '2025-12-06 20:04:51');
INSERT INTO `t_visit_log` VALUES (4, 4, '2025-12-06', '2025-12-06 20:14:16');
INSERT INTO `t_visit_log` VALUES (9, 1, '2025-12-07', '2025-12-07 00:08:39');
INSERT INTO `t_visit_log` VALUES (10, 4, '2025-12-07', '2025-12-07 19:19:26');
INSERT INTO `t_visit_log` VALUES (11, 1, '2025-12-08', '2025-12-08 00:02:22');
INSERT INTO `t_visit_log` VALUES (12, 4, '2025-12-08', '2025-12-08 01:01:49');
INSERT INTO `t_visit_log` VALUES (13, 1, '2025-12-09', '2025-12-09 00:03:10');
INSERT INTO `t_visit_log` VALUES (14, 4, '2025-12-09', '2025-12-09 00:04:11');
INSERT INTO `t_visit_log` VALUES (15, 4, '2025-12-10', '2025-12-10 17:01:34');
INSERT INTO `t_visit_log` VALUES (16, 1, '2025-12-10', '2025-12-10 18:07:43');
INSERT INTO `t_visit_log` VALUES (17, 1, '2025-12-11', '2025-12-11 19:03:43');
INSERT INTO `t_visit_log` VALUES (18, 4, '2025-12-11', '2025-12-11 19:03:55');
INSERT INTO `t_visit_log` VALUES (19, 6, '2025-12-14', '2025-12-14 22:41:37');
INSERT INTO `t_visit_log` VALUES (20, 1, '2025-12-14', '2025-12-14 23:15:48');
INSERT INTO `t_visit_log` VALUES (21, 7, '2025-12-14', '2025-12-14 23:15:49');
INSERT INTO `t_visit_log` VALUES (22, 7, '2025-12-15', '2025-12-15 00:55:01');
INSERT INTO `t_visit_log` VALUES (23, 1, '2025-12-15', '2025-12-15 00:56:08');
INSERT INTO `t_visit_log` VALUES (24, 1, '2025-12-16', '2025-12-16 00:32:25');
INSERT INTO `t_visit_log` VALUES (25, 7, '2025-12-16', '2025-12-16 00:32:42');
INSERT INTO `t_visit_log` VALUES (26, 1, '2025-12-17', '2025-12-17 11:47:27');
INSERT INTO `t_visit_log` VALUES (27, 1, '2025-12-19', '2025-12-19 11:55:41');
INSERT INTO `t_visit_log` VALUES (28, 7, '2025-12-19', '2025-12-19 11:55:41');
INSERT INTO `t_visit_log` VALUES (29, 1, '2025-12-20', '2025-12-20 12:06:21');
INSERT INTO `t_visit_log` VALUES (30, 7, '2025-12-20', '2025-12-20 12:49:12');
INSERT INTO `t_visit_log` VALUES (31, 7, '2025-12-21', '2025-12-21 00:48:16');
INSERT INTO `t_visit_log` VALUES (32, 1, '2025-12-21', '2025-12-21 00:59:40');
INSERT INTO `t_visit_log` VALUES (33, 8, '2025-12-21', '2025-12-21 21:17:24');
INSERT INTO `t_visit_log` VALUES (34, 1, '2025-12-22', '2025-12-22 00:06:29');
INSERT INTO `t_visit_log` VALUES (35, 7, '2025-12-22', '2025-12-22 08:54:12');
INSERT INTO `t_visit_log` VALUES (36, 1, '2025-12-24', '2025-12-24 13:52:45');
INSERT INTO `t_visit_log` VALUES (37, 8, '2025-12-24', '2025-12-24 13:52:49');
INSERT INTO `t_visit_log` VALUES (38, 1, '2026-01-01', '2026-01-01 14:41:16');
INSERT INTO `t_visit_log` VALUES (39, 11, '2026-01-01', '2026-01-01 14:41:20');
INSERT INTO `t_visit_log` VALUES (40, 1, '2026-01-02', '2026-01-02 00:22:26');
INSERT INTO `t_visit_log` VALUES (41, 11, '2026-01-02', '2026-01-02 00:40:25');
INSERT INTO `t_visit_log` VALUES (42, 1, '2026-01-03', '2026-01-03 00:34:45');
INSERT INTO `t_visit_log` VALUES (43, 11, '2026-01-28', '2026-01-28 19:57:26');
INSERT INTO `t_visit_log` VALUES (44, 1, '2026-01-31', '2026-01-31 11:36:40');
INSERT INTO `t_visit_log` VALUES (45, 16, '2026-01-31', '2026-01-31 17:07:06');
INSERT INTO `t_visit_log` VALUES (46, 27, '2026-01-31', '2026-01-31 20:59:11');
INSERT INTO `t_visit_log` VALUES (47, 29, '2026-01-31', '2026-01-31 22:09:45');
INSERT INTO `t_visit_log` VALUES (48, 1, '2026-02-01', '2026-02-01 19:37:06');
INSERT INTO `t_visit_log` VALUES (49, 29, '2026-02-01', '2026-02-01 19:37:16');
INSERT INTO `t_visit_log` VALUES (50, 32, '2026-02-01', '2026-02-01 21:03:51');
INSERT INTO `t_visit_log` VALUES (51, 33, '2026-02-01', '2026-02-01 22:20:56');
INSERT INTO `t_visit_log` VALUES (52, 34, '2026-02-01', '2026-02-01 22:39:20');
INSERT INTO `t_visit_log` VALUES (53, 1, '2026-02-09', '2026-02-09 21:11:56');

SET FOREIGN_KEY_CHECKS = 1;
