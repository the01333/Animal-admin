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

 Date: 21/12/2025 00:11:00
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
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '领养申请表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_adoption_application
-- ----------------------------
INSERT INTO `t_adoption_application` VALUES (1, 'AP202511275167', 1, 20, '我喜欢可爱的小狗', '1. 川川 (21岁)', '居住类型：宿舍；住房面积：10㎡；无院子；暂无养宠经验；宠物知识：暂无', '', '13000000000', '中国福建省漳州市xx县', 'approved', 1, '2025-11-27 19:50:25', '通过了 要好好对待耶耶', '2025-11-27 19:33:13', '2025-11-27 19:33:13', 0);
INSERT INTO `t_adoption_application` VALUES (2, 'AP20251127015A', 1, 11, '我已经领养了一只小狗了 还想养一只小猫', '1. 川川 (21岁)', '居住类型：宿舍；住房面积：30㎡；无院子；有养宠经验；宠物知识：暂无', '一只萨摩耶', '13000000000', '中国福建省漳州市xx县', 'rejected', 1, '2025-11-28 21:56:32', '最近橘子打疫苗 需要静养几天后再来', '2025-11-27 20:08:17', '2025-11-27 20:08:17', 0);
INSERT INTO `t_adoption_application` VALUES (3, 'AP202511283CD7', 1, 11, '我已经养了一只小狗了 还想养一只小猫', '1. 林憨憨 (21岁)', '居住类型：宿舍；住房面积：30㎡；无院子；有养宠经验；宠物知识：暂无了解', '萨摩耶', '13000000000', '中国福建省漳州市xx县', 'cancelled', NULL, NULL, NULL, '2025-11-28 21:59:17', '2025-11-28 21:59:17', 0);
INSERT INTO `t_adoption_application` VALUES (4, 'AP20251205719A', 4, 22, '喜欢可爱小猫', '1. test2 (25岁)', '居住类型：公寓；住房面积：20㎡；无院子；暂无养宠经验；宠物知识：暂无', '', '13063184972', '中国福建省', 'cancelled', NULL, NULL, NULL, '2025-12-05 20:55:56', '2025-12-05 20:55:56', 0);
INSERT INTO `t_adoption_application` VALUES (5, 'AP20251205E8AD', 4, 22, '我喜欢可爱小猫', '1. test2 (25岁)', '居住类型：公寓；住房面积：20㎡；无院子；暂无养宠经验；宠物知识：暂无', '', '13063184972', '中国福建省', 'rejected', 1, '2025-12-06 22:00:11', '改天再来 最近店面暂时关门', '2025-12-05 21:42:25', '2025-12-05 21:42:25', 0);
INSERT INTO `t_adoption_application` VALUES (6, 'AP202512063749', 4, 6, '我喜欢小狗', '1. test2 (25岁)', '居住类型：公寓；住房面积：20㎡；无院子；暂无养宠经验；宠物知识：暂无', '', '13063184972', '中国福建省', 'pending', NULL, NULL, NULL, '2025-12-06 21:59:28', '2025-12-06 21:59:28', 0);

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
) ENGINE = InnoDB AUTO_INCREMENT = 351 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '聊天消息表' ROW_FORMAT = DYNAMIC;

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
) ENGINE = InnoDB AUTO_INCREMENT = 28 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'AI客服对话历史表' ROW_FORMAT = Dynamic;

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
) ENGINE = InnoDB AUTO_INCREMENT = 34 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'AI客服会话表' ROW_FORMAT = Dynamic;

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
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '人工客服会话表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_cs_session
-- ----------------------------
INSERT INTO `t_cs_session` VALUES (1, 4, 1, 'OPEN', '好的，我们已为您记录需求，如有合适的宠物会第一时间联系您。', '2025-12-10 23:11:43', 0, 0, '2025-12-08 21:56:02', '2025-12-08 21:56:02', 0);
INSERT INTO `t_cs_session` VALUES (2, 1, 1, 'OPEN', NULL, NULL, 0, 0, '2025-12-09 00:15:59', '2025-12-09 00:15:59', 0);
INSERT INTO `t_cs_session` VALUES (3, 5, 1, 'OPEN', '你好', '2025-12-11 19:06:35', 0, 0, '2025-12-11 19:05:49', '2025-12-11 19:05:49', 0);
INSERT INTO `t_cs_session` VALUES (4, 6, 1, 'OPEN', '1111', '2025-12-15 21:15:21', 2, 0, '2025-12-11 19:22:02', '2025-12-11 19:22:02', 0);
INSERT INTO `t_cs_session` VALUES (5, 7, 1, 'OPEN', '50', '2025-12-20 21:29:30', 0, 0, '2025-12-14 22:43:24', '2025-12-14 22:43:24', 0);

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
) ENGINE = InnoDB AUTO_INCREMENT = 30 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_dict_item
-- ----------------------------
INSERT INTO `t_dict_item` VALUES (1, 'pet_category', 'dog', '狗狗', 1, 1, '该宠物类型为狗狗', '2025-12-07 16:13:58', '2025-12-07 16:13:58', 0);
INSERT INTO `t_dict_item` VALUES (2, 'pet_category', 'cat', '猫咪', 2, 1, '小猫', '2025-12-07 16:13:58', '2025-12-07 16:13:58', 0);
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
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '领养指南表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_guide
-- ----------------------------
INSERT INTO `t_guide` VALUES (1, '领养前的准备工作', '详细介绍领养宠物前需要做的各项准备工作，包括心理准备、物质准备和环境准备。', '# 领养前的准备工作\r\r\n\r\r领养宠物是一项长期的责任，在决定领养之前，需要做好充分的准备工作。\r\r\n\r\r## 心理准备\r\r\n\r\r领养宠物意味着承担起一个生命的责任，需要有长期照顾它的心理准备。宠物会陪伴您很多年，期间可能会遇到各种问题，如生病、行为问题等，都需要您耐心处理。\r\r\n\r\r## 物质准备\r\r\n\r\r在宠物到家之前，需要准备好基本的生活用品：\r\r\n\r\r- 食物和饮水器\r\r\n- 舒适的窝或笼子\r\r\n- 玩具和磨爪板（猫咪）\r\r\n- 牵引绳和项圈（狗狗）\r\r\n- 猫砂盆和猫砂（猫咪）\r\r\n- 基础药品和急救包\r\r\n\r\r## 环境准备\r\r\n\r\r确保家中环境对宠物安全友好：\r\r\n\r\r- 收好易碎物品和危险品\r\r\n- 封堵可能的逃跑通道\r\r\n- 准备一个安静的适应空间\r\r\n- 了解附近的宠物医院\r\r\n\r\r做好这些准备工作，可以帮助宠物更快适应新家，也能让您更好地享受与宠物相伴的快乐时光。', 'https://192.168.99.228:9000/animal-adopt/1.png', '领养准备', 1294, 2, 2, '2025-11-20 22:53:07', '2025-12-08 00:05:00');
INSERT INTO `t_guide` VALUES (2, '新宠物到家的第一周', '帮助新领养的宠物适应新环境的实用指南，包括饮食、作息和互动建议。', '# 新宠物到家的第一周\r\r\n\r\r新宠物到家的第一周是适应期，需要特别的关注和照顾。\r\r\n\r\r## 第一天：安静适应\r\r\n\r\r第一天不要过度互动，给宠物一个安静的空间适应环境。准备好食物和水，但不要强迫进食。\r\r\n\r\r## 第2-3天：建立信任\r\r\n\r\r开始轻声说话和温和互动，用手喂食建立信任关系。观察宠物的行为和健康状况。\r\r\n\r\r## 第4-7天：逐步熟悉\r\r\n\r\r逐步增加互动时间，引导宠物熟悉家中各个区域。开始建立基本的作息规律。\r\r\n\r\r记住，每只宠物的适应速度不同，要有耐心，不要急于求成。', 'https://192.168.99.228:9000/animal-adopt/2.png', '日常护理', 1029, 1, 1, '2025-11-20 22:53:07', '2025-12-08 01:02:15');
INSERT INTO `t_guide` VALUES (3, '基础服从训练技巧', '教授基本的服从指令训练方法，帮助建立良好的人宠关系。', '# 基础服从训练技巧\r\r\n\r\r训练宠物不仅能让它们更好地融入家庭生活，还能增进与主人的感情。\r\r\n\r\r## 基本原则\r\r\n\r\r训练要遵循正向强化原则，多鼓励少惩罚。每次训练时间不宜过长，保持在10-15分钟为宜。\r\r\n\r\r## 常用指令\r\r\n\r\r- **坐下：** 手持零食，引导宠物坐下，给予奖励\r\r\n- **趴下：** 在坐下的基础上，将零食往地面移动\r\r\n- **过来：** 用欢快的语气叫名字，宠物靠近时给予奖励\r\r\n- **握手：** 轻轻抬起宠物的爪子，说\"握手\"并奖励\r\r\n\r\r## 注意事项\r\r\n\r\r训练要有耐心和一致性，全家人使用相同的指令和手势。训练后要及时奖励，让宠物建立正确的条件反射。', 'https://192.168.99.228:9000/animal-adopt/3.png', '训练教育', 770, 2, 1, '2025-11-20 22:53:07', '2025-12-07 23:45:00');
INSERT INTO `t_guide` VALUES (4, '常见疾病预防与处理', '介绍宠物常见疾病的症状识别、预防措施和应急处理方法。', '# 常见疾病预防与处理\r\r\n\r\r了解宠物常见疾病，及早发现和预防，是每个宠物主人的必备知识。\r\r\n\r\r## 预防接种\r\r\n\r\r按时接种疫苗是预防疾病的最佳方式。幼年宠物需要接种多联疫苗和狂犬病疫苗，成年后每年加强免疫。\r\r\n\r\r## 常见症状\r\r\n\r\r- 食欲不振、精神萎靡\r\r\n- 呕吐、腹泻\r\r\n- 咳嗽、流鼻涕\r\r\n- 皮肤瘙痒、脱毛\r\r\n- 频繁舔爪或某个部位\r\r\n\r\r## 日常保健\r\r\n\r\r定期驱虫，保持环境清洁，提供均衡营养，适量运动。发现异常及时就医，不要自行用药。', 'https://192.168.99.228:9000/animal-adopt/4.png', '健康管理', 1110, 1, 1, '2025-11-20 22:53:07', '2025-12-05 21:40:00');
INSERT INTO `t_guide` VALUES (5, '分离焦虑症的应对', '分析宠物分离焦虑的原因和表现，并提供有效的缓解方法。', '# 分离焦虑症的应对\r\r\n\r\r分离焦虑是许多宠物常见的行为问题，表现为主人离开后出现焦虑和破坏行为。\r\r\n\r\r## 症状表现\r\r\n\r\r- 主人离开时过度吠叫或哀鸣\r\r\n- 破坏家具或物品\r\r\n- 在不该的地方大小便\r\r\n- 过度舔毛或咬爪子\r\r\n- 主人回来时过度兴奋\r\r\n\r\r## 缓解方法\r\r\n\r\r渐进式脱敏训练：开始时短时间离开，逐步延长时间。离开和回来时保持平静，不要过度安抚。\r\r\n\r\r## 其他建议\r\r\n\r\r提供足够的玩具和活动空间，确保宠物在独处时有事可做。考虑使用安抚玩具或背景音乐。严重情况建议咨询专业训练师或兽医。', 'https://192.168.99.228:9000/animal-adopt/5.png', '行为问题', 832, 1, 1, '2025-11-20 22:53:07', '2025-12-20 20:55:00');
INSERT INTO `t_guide` VALUES (6, '营养饮食搭配指南', '根据不同年龄和健康状况的宠物，提供科学的饮食搭配建议。', '# 营养饮食搭配指南\r\r\n\r\r合理的饮食搭配是宠物健康的基础，不同年龄段的宠物有不同的营养需求。\r\r\n\r\r## 幼年期（0-1岁）\r\r\n\r\r需要高蛋白、高能量的食物支持快速生长。每天3-4餐，选择专门的幼年宠物粮。\r\r\n\r\r## 成年期（1-7岁）\r\r\n\r\r每天1-2餐，保持营养均衡。蛋白质占30-40%，脂肪10-20%，碳水化合物30-50%。\r\r\n\r\r## 老年期（7岁以上）\r\r\n\r\r降低热量摄入，增加易消化的优质蛋白。考虑添加关节保健成分和抗氧化剂。\r\r\n\r\r## 禁忌食物\r\r\n\r\r- 巧克力、咖啡因\r\r\n- 葡萄、洋葱、大蒜\r\r\n- 生肉、生鸡蛋\r\r\n- 高盐、高糖食物\r\r\n\r\r记得提供充足的清洁饮水，根据宠物的活动量调整食物份量。', 'https://192.168.99.228:9000/animal-adopt/6.png', '日常护理', 669, 1, 1, '2025-11-20 22:53:07', '2025-12-07 23:50:00');

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
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '指南收藏记录表' ROW_FORMAT = Dynamic;

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
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '指南点赞记录表' ROW_FORMAT = Dynamic;

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
) ENGINE = InnoDB AUTO_INCREMENT = 37 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '宠物表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_pet
-- ----------------------------
INSERT INTO `t_pet` VALUES (1, '小刺', '刺猬', '刺猬', 1, 6, 0.50, '棕色', 0, 1, 'healthy', NULL, '性格温顺, 喜欢安静的环境, 晚上比较活跃', '这是一只可爱的小刺猬, 身体健康, 适应能力强。喜欢吃水果和昆虫, 需要提供温暖干燥的环境。', '[\"http://localhost:9000/animal-adopt/刺猬1.png\"]', 'http://localhost:9000/animal-adopt/刺猬1.png', NULL, '城市公园', '需要有养刺猬经验, 能提供适宜的温度环境（20-25℃）, 定期清理笼舍', 'available', NULL, 1, 0, 0, 0, 0, 1, NULL, '2025-11-19 13:44:14', '2025-12-06 19:51:45', 0);
INSERT INTO `t_pet` VALUES (2, '小白', '兔子', '白兔', 2, 8, 2.50, '纯白色', 1, 1, 'healthy', NULL, '性格活泼可爱, 喜欢吃胡萝卜和青菜, 很亲人', '这是一只纯白色的可爱兔子, 毛发柔软, 眼睛明亮。已绝育并接种疫苗, 身体健康。喜欢在草地上奔跑, 适合有院子的家庭。', '[\"http://localhost:9000/animal-adopt/兔子1.jpg\"]', 'http://localhost:9000/animal-adopt/兔子1.jpg', NULL, '宠物救助站', '需要有足够的活动空间, 定期提供新鲜蔬菜和干草, 每天至少1小时的户外活动时间', 'available', NULL, 1, 0, 0, 0, 0, 2, NULL, '2025-11-19 13:44:14', '2025-12-06 19:51:45', 0);
INSERT INTO `t_pet` VALUES (3, '金金', '小狗', '金毛寻回犬', 1, 18, 28.50, '金黄色', 1, 1, 'healthy', NULL, '性格温顺友善, 聪明听话, 喜欢和人互动, 对小孩很有耐心', '这是一只帅气的金毛犬, 毛色金黄, 性格温顺。已完成绝育和疫苗接种, 训练有素, 会基本指令。非常适合家庭饲养, 是孩子们的好伙伴。', '[\"http://localhost:9000/animal-adopt/小狗1.png\"]', 'http://localhost:9000/animal-adopt/小狗1.png', NULL, '市区流浪犬救助中心', '需要有足够的活动空间, 每天遛狗2次以上, 定期梳理毛发, 有养大型犬经验者优先', 'available', NULL, 1, 0, 0, 0, 0, 3, NULL, '2025-11-19 13:44:14', '2025-12-06 19:51:45', 0);
INSERT INTO `t_pet` VALUES (4, '小柴', '小狗', '柴犬', 2, 12, 9.80, '赤色', 1, 1, 'healthy', NULL, '性格独立但忠诚, 活泼好动, 喜欢户外活动, 警惕性高', '这是一只可爱的柴犬, 标准的赤色毛发, 笑容治愈。已绝育并完成疫苗接种, 身体健康。性格活泼, 需要足够的运动量。', '[\"http://localhost:9000/animal-adopt/小狗2.jpg\"]', 'http://localhost:9000/animal-adopt/小狗2.jpg', NULL, '郊区宠物救助站', '需要每天至少1小时户外运动, 定期梳理毛发, 有养中型犬经验', 'available', NULL, 1, 1, 0, 0, 0, 4, NULL, '2025-11-19 13:44:14', '2025-12-06 19:51:45', 0);
INSERT INTO `t_pet` VALUES (5, '泰迪', '小狗', '泰迪犬', 1, 10, 4.20, '棕色', 1, 1, 'healthy', NULL, '性格活泼粘人, 聪明伶俐, 喜欢和主人互动, 适合陪伴', '这是一只可爱的泰迪犬, 毛发卷曲蓬松, 非常可爱。已绝育并接种疫苗, 身体健康。性格粘人, 适合需要陪伴的家庭。', '[\"http://localhost:9000/animal-adopt/小狗3.jpg\"]', 'http://localhost:9000/animal-adopt/小狗3.jpg', NULL, '市区宠物医院', '需要定期美容修剪毛发, 每天陪伴玩耍, 适合有时间照顾的家庭', 'available', NULL, 1, 0, 0, 0, 0, 5, NULL, '2025-11-19 13:44:14', '2025-12-06 19:51:45', 0);
INSERT INTO `t_pet` VALUES (6, '二哈', '小狗', '哈士奇', 1, 15, 22.00, '黑白色', 1, 1, 'healthy', NULL, '性格活泼好动, 精力充沛, 喜欢奔跑, 有时会有点调皮', '这是一只帅气的哈士奇, 标志性的蓝眼睛和黑白毛色。已绝育并完成疫苗接种, 精力旺盛, 需要大量运动。性格友善但有点调皮, 适合有经验的主人。', '[\"http://localhost:9000/animal-adopt/小狗4.png\"]', 'http://localhost:9000/animal-adopt/小狗4.png', NULL, '郊区救助站', '需要大量运动空间, 每天至少2小时户外活动, 有养大型犬经验, 能接受它的调皮性格', 'available', NULL, 1, 6, 0, 0, 1, 6, NULL, '2025-11-19 13:44:14', '2025-12-10 23:15:00', 0);
INSERT INTO `t_pet` VALUES (7, '小黑', '小狗', '边境牧羊犬', 1, 20, 18.50, '黑白色', 1, 1, 'healthy', NULL, '智商极高, 学习能力强, 精力充沛, 需要大量脑力和体力活动', '这是一只聪明的边牧, 黑白相间的毛色, 眼神专注。已绝育并完成疫苗接种, 智商很高, 学东西很快。需要主人有足够时间陪伴和训练。', '[\"http://localhost:9000/animal-adopt/小狗5.jpg\"]', 'http://localhost:9000/animal-adopt/小狗5.jpg', NULL, '训练基地', '需要大量运动和智力游戏, 每天至少2小时户外活动, 有养犬经验, 能提供足够的训练和刺激', 'available', NULL, 1, 0, 0, 0, 0, 7, NULL, '2025-11-19 13:44:14', '2025-12-06 19:51:45', 0);
INSERT INTO `t_pet` VALUES (8, '拉拉', '小狗', '拉布拉多寻回犬', 2, 24, 26.00, '黄色', 1, 1, 'healthy', NULL, '性格温顺友善, 对人友好, 喜欢游泳, 是优秀的家庭伴侣犬', '这是一只温柔的拉布拉多, 毛色金黄, 性格超好。已绝育并完成疫苗接种, 非常适合家庭饲养。喜欢和人互动, 对小孩特别有耐心。', '[\"http://localhost:9000/animal-adopt/小狗6.jpg\"]', 'http://localhost:9000/animal-adopt/小狗6.jpg', NULL, '市区救助中心', '需要足够的活动空间, 每天遛狗2次, 定期梳理毛发, 适合有院子的家庭', 'available', NULL, 1, 0, 0, 0, 0, 8, NULL, '2025-11-19 13:44:14', '2025-12-06 19:51:45', 0);
INSERT INTO `t_pet` VALUES (9, '圆圆', '小猫', '英国短毛猫', 2, 14, 4.50, '蓝灰色', 1, 1, 'healthy', NULL, '性格温顺安静, 喜欢睡觉, 不太爱动, 适合公寓饲养', '这是一只可爱的英短猫, 圆圆的脸蛋, 蓝灰色的毛发。已绝育并完成疫苗接种, 性格温顺, 不吵不闹, 非常适合公寓饲养。', '[\"http://localhost:9000/animal-adopt/小猫1.jpg\"]', 'http://localhost:9000/animal-adopt/小猫1.jpg', NULL, '宠物店', '需要定期梳理毛发, 提供猫砂盆和猫爬架, 定期体检', 'available', NULL, 1, 2, 0, 0, 0, 9, NULL, '2025-11-19 13:44:14', '2025-12-06 19:51:45', 0);
INSERT INTO `t_pet` VALUES (10, '虎斑', '小猫', '美国短毛猫', 1, 16, 5.20, '银色虎斑', 1, 1, 'healthy', NULL, '性格活泼好动, 喜欢玩耍, 好奇心强, 适应能力强', '这是一只帅气的美短猫, 银色虎斑纹路清晰。已绝育并完成疫苗接种, 性格活泼, 喜欢和人互动。身体健壮, 适应能力强。', '[\"http://localhost:9000/animal-adopt/小猫2.jpg\"]', 'http://localhost:9000/animal-adopt/小猫2.jpg', NULL, '流浪猫救助站', '需要提供玩具和猫爬架, 定期陪伴玩耍, 适合有时间的家庭', 'available', NULL, 1, 2, 1, 1, 0, 10, NULL, '2025-11-19 13:44:14', '2025-12-06 19:51:45', 0);
INSERT INTO `t_pet` VALUES (11, '橘子', '小猫', '中华田园猫', 1, 12, 6.00, '橘色', 1, 1, 'healthy', NULL, '性格亲人温顺, 食量较大, 喜欢晒太阳, 很会撒娇', '这是一只可爱的橘猫, 毛色橙黄, 圆滚滚的很可爱。已绝育并完成疫苗接种, 性格亲人, 喜欢和主人互动。食量比较大, 需要控制饮食。', '[\"http://localhost:9000/animal-adopt/小猫3.png\"]', 'http://localhost:9000/animal-adopt/小猫3.png', NULL, '小区救助', '需要控制饮食避免过胖, 提供猫砂盆和猫爬架, 定期体检', 'available', NULL, 1, 14, 0, 0, 2, 11, NULL, '2025-11-19 13:44:14', '2025-12-06 19:51:45', 0);
INSERT INTO `t_pet` VALUES (12, '小暹', '小猫', '暹罗猫', 2, 18, 3.80, '重点色', 1, 1, 'healthy', NULL, '性格活泼粘人, 喜欢和主人交流, 叫声独特, 需要陪伴', '这是一只优雅的暹罗猫, 蓝色眼睛, 重点色毛发。已绝育并完成疫苗接种, 性格粘人, 喜欢和主人说话。需要主人有足够时间陪伴。', '[\"http://localhost:9000/animal-adopt/小猫4.jpg\"]', 'http://localhost:9000/animal-adopt/小猫4.jpg', NULL, '宠物医院', '需要大量陪伴和互动, 不适合长时间独处, 适合在家办公或时间充裕的家庭', 'available', NULL, 1, 4, 0, 0, 0, 12, NULL, '2025-11-19 13:44:14', '2025-12-06 19:51:45', 0);
INSERT INTO `t_pet` VALUES (13, '布布', '小猫', '布偶猫', 2, 20, 5.50, '海豹双色', 1, 1, 'healthy', NULL, '性格温顺甜美, 喜欢被抱, 叫声轻柔, 是完美的家庭伴侣', '这是一只漂亮的布偶猫, 蓝色眼睛, 海豹双色毛发。已绝育并完成疫苗接种, 性格超级温顺, 喜欢被抱。毛发需要定期打理。', '[\"http://localhost:9000/animal-adopt/小猫5.jpg\"]', 'http://localhost:9000/animal-adopt/小猫5.jpg', NULL, '繁育基地', '需要每天梳理毛发, 提供高质量猫粮, 定期体检, 适合有养猫经验的家庭', 'available', NULL, 1, 18, 0, 0, 0, 13, NULL, '2025-11-19 13:44:14', '2025-12-06 19:51:45', 0);
INSERT INTO `t_pet` VALUES (14, '小狸', '小猫', '中华狸花猫', 1, 10, 4.80, '棕色虎斑', 1, 1, 'healthy', NULL, '性格独立机警, 身体强壮, 适应能力强, 是优秀的捕鼠能手', '这是一只帅气的狸花猫, 棕色虎斑纹路清晰。已绝育并完成疫苗接种, 性格独立但也亲人。身体强壮, 适应能力很强。', '[\"http://localhost:9000/animal-adopt/小猫6.jpg\"]', 'http://localhost:9000/animal-adopt/小猫6.jpg', NULL, '街道救助', '需要提供猫砂盆和猫爬架, 定期体检, 适合各类家庭', 'available', NULL, 1, 8, 0, 0, 0, 14, NULL, '2025-11-19 13:44:14', '2025-12-06 19:51:45', 0);
INSERT INTO `t_pet` VALUES (20, '耶耶', '小狗', '萨摩耶', 2, 5, 15.00, '白色', 0, 0, 'healthy', NULL, '温顺', '一只可爱的萨摩耶', '[\"http://localhost:9000/animal-adopt/pet-images/8962033cf5c44e629554661ac480f271.png\"]', 'http://localhost:9000/animal-adopt/pet-covers/5f372549aeed448590d9fc4e41e20c6b.png', NULL, NULL, NULL, 'adopted', 1, 1, 26, 1, 1, 1, 0, 1, '2025-11-23 00:30:33', '2025-12-06 19:51:45', 0);
INSERT INTO `t_pet` VALUES (21, '小博', '小狗', '博美犬', 1, 3, 10.00, '白色', 0, 0, 'healthy', NULL, '温顺', '一只可爱温顺的博美犬', '[\"http://localhost:9000/animal-adopt/pet-images/08352439a0244cd6aa188ec7b290f653.png\"]', 'http://localhost:9000/animal-adopt/pet-covers/0b0f079552b04de3952ea0e27c895a84.png', NULL, NULL, NULL, 'available', NULL, 1, 5, 0, 1, 0, 0, 1, '2025-11-24 14:13:34', '2025-12-06 19:51:45', 0);
INSERT INTO `t_pet` VALUES (22, '布布', '小猫', '布偶猫', 2, 3, 6.00, '白色', 0, 0, 'healthy', NULL, '调皮', '一只和小高一样调皮的可爱布偶猫', '[\"http://localhost:9000/animal-adopt/pet-images/c9a740e1c6b743b0b8930343a26678b9.png\"]', 'http://localhost:9000/animal-adopt/pet-covers/cd04017645314c5ca9a64e5d95e2600f.png', NULL, NULL, NULL, 'available', NULL, 1, 8, 0, 0, 2, 0, 1, '2025-11-24 15:05:08', '2025-12-06 22:00:00', 0);
INSERT INTO `t_pet` VALUES (23, '边边', '小狗', '边牧犬', 1, 4, 14.00, '黑白相见', 0, 0, 'healthy', NULL, '活泼', '和小高一样皮', '[\"http://localhost:9000/animal-adopt/pet-images/39d3242eb0a84cdf8c55199a95a24840.png\"]', 'http://localhost:9000/animal-adopt/pet-covers/c68f5065d8a84819a75a0019c9d04927.png', NULL, NULL, NULL, 'available', NULL, 1, 15, 1, 1, 0, 0, 1, '2025-11-24 15:06:49', '2025-12-10 23:15:00', 0);
INSERT INTO `t_pet` VALUES (24, '金金', '小狗', '金毛', 1, 4, 15.00, '棕色', 0, 0, 'healthy', NULL, '活泼', '一只金毛', '[\"http://localhost:9000/animal-adopt/pet-images/8f9b25c437a54df2927d477b46535406.png\"]', 'http://localhost:9000/animal-adopt/pet-covers/412005b4bf8c43b89c398a3e4b8160e9.png', NULL, NULL, NULL, 'available', NULL, 1, 5, 0, 0, 0, 0, 1, '2025-11-24 15:11:43', '2025-12-06 19:51:45', 0);
INSERT INTO `t_pet` VALUES (25, '松松', '松鼠', '松鼠', 1, 3, 4.00, '黑棕色', 0, 0, 'healthy', NULL, '谨慎', '胆小的松鼠', '[\"http://localhost:9000/animal-adopt/pet-images/a3f842d804d14893a24f776594f76dc0.png\"]', 'http://localhost:9000/animal-adopt/pet-covers/a097fdcf5c2e43bf895465c537c2125f.png', NULL, NULL, NULL, 'available', NULL, 1, 11, 1, 1, 0, 0, 1, '2025-11-24 15:27:05', '2025-12-20 12:06:50', 0);
INSERT INTO `t_pet` VALUES (26, '小松', '松鼠', '美洲红松鼠', 2, 3, 6.00, '红棕色', 0, 0, 'healthy', NULL, '活泼', '活泼的松鼠', '[\"http://localhost:9000/animal-adopt/pet-images/32cce10b71bd4c35b7a40053bbc6c894.png\"]', 'http://localhost:9000/animal-adopt/pet-covers/9eebd6cf4f3d47f1acaf6c8fedae6587.png', NULL, NULL, NULL, 'available', NULL, 1, 11, 1, 1, 0, 0, 1, '2025-11-24 15:45:43', '2025-12-07 23:50:00', 0);
INSERT INTO `t_pet` VALUES (27, '大松', '松鼠', '美洲红松鼠', 1, 2, 2.00, '红棕色', 0, 0, 'healthy', NULL, '活泼', '松鼠', '[\"http://localhost:9000/animal-adopt/pet-images/d755515cc94b4185ac57897f5a056431.png\"]', 'http://localhost:9000/animal-adopt/pet-covers/53695e368c5e4024a39b8a2f220d89bb.png', NULL, NULL, NULL, 'available', NULL, 1, 0, 0, 0, 0, 0, 1, '2025-11-24 15:49:12', '2025-11-24 15:55:56', 1);
INSERT INTO `t_pet` VALUES (28, '大松', '松鼠', '美洲红松鼠', 1, 2, 2.00, '红棕色', 0, 0, 'healthy', NULL, '胆小', '胆小的松鼠', '[\"http://localhost:9000/animal-adopt/pet-images/97c11e865e914da691a4700f6751db27.png\"]', 'http://localhost:9000/animal-adopt/pet-covers/c06a6676216847af8c1794ba188c63f8.png', NULL, NULL, NULL, 'available', NULL, 1, 0, 0, 0, 0, 0, 1, '2025-11-24 15:56:49', '2025-11-24 19:15:02', 1);
INSERT INTO `t_pet` VALUES (29, '大松', '松鼠', '美洲红松鼠', 1, 3, 4.00, '红棕色', 0, 0, 'healthy', NULL, '谨慎', '谨慎的松鼠', '[\"http://localhost:9000/animal-adopt/pet-images/ab25cef1966448e6ab9d15c5f3c2f703.png\"]', 'http://localhost:9000/animal-adopt/pet-covers/50e686e36754405497e7ca53f4e6954e.png', NULL, NULL, NULL, 'available', NULL, 1, 0, 0, 0, 0, 0, 1, '2025-11-24 19:15:56', '2025-11-26 14:54:34', 1);
INSERT INTO `t_pet` VALUES (33, '仓仓', '仓鼠', '仓鼠', 1, 2, 2.00, '白色', 0, 0, 'healthy', NULL, '胆小、谨慎', '小心的仓鼠', '[\"http://localhost:9000/animal-adopt/pet-images/5dbea0668a4342de8ddf71da8b9bc530.png\"]', 'http://localhost:9000/animal-adopt/pet-covers/5af36d9a6bcf4fbe983d78da208e0b19.png', NULL, NULL, NULL, 'available', NULL, 1, 15, 1, 1, 0, 0, 1, '2025-11-24 22:38:51', '2025-12-21 00:10:00', 0);
INSERT INTO `t_pet` VALUES (34, '熊大', 'cat', '熊', 1, 6, 200.00, '棕白色', 0, 0, 'healthy', NULL, '凶猛', '熊大', '[\"http://localhost:9000/animal-adopt/pet-images/68b633d3d7ef4421a402e64111aa34eb.jpg\"]', 'http://localhost:9000/animal-adopt/pet-covers/95f1b8e2488a4762b99007aea0d6c25b.jpg', NULL, NULL, NULL, 'available', NULL, 1, 0, 0, 0, 0, 0, 1, '2025-12-07 19:27:27', '2025-12-07 19:34:06', 1);
INSERT INTO `t_pet` VALUES (35, '熊大2', '熊', '熊', 1, 6, 300.00, '棕白色', 0, 0, 'healthy', NULL, '凶猛', '熊大', '[\"http://localhost:9000/animal-adopt/pet-images/cf122c4211714b58a3a8418d31a17346.jpg\"]', 'http://localhost:9000/animal-adopt/pet-covers/b14528717e1d4d1c81b4ca25025b5798.jpg', NULL, NULL, NULL, 'available', NULL, 1, 0, 0, 0, 0, 0, 1, '2025-12-07 19:29:42', '2025-12-07 19:33:54', 1);
INSERT INTO `t_pet` VALUES (36, '熊大', 'bear', '狗熊', 1, 7, 300.00, '棕白色', 0, 0, 'healthy', NULL, '凶猛', '凶猛的熊大', '[\"http://localhost:9000/animal-adopt/pet-images/7b7044333d374cf79424eb58d3d4370a.jpg\"]', 'http://localhost:9000/animal-adopt/pet-covers/e54fce360dc14b64bc3c3ffefa782165.jpg', NULL, NULL, NULL, 'available', NULL, 1, 0, 0, 0, 0, 0, 1, '2025-12-07 20:31:37', '2025-12-07 23:34:47', 1);

-- ----------------------------
-- Table structure for t_pet_favorite
-- ----------------------------
DROP TABLE IF EXISTS `t_pet_favorite`;
CREATE TABLE `t_pet_favorite`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `pet_id` bigint NOT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `deleted` tinyint NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_user_pet`(`user_id` ASC, `pet_id` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_pet_id`(`pet_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 25 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '宠物收藏表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_pet_favorite
-- ----------------------------
INSERT INTO `t_pet_favorite` VALUES (1, 1, 14, '2025-11-19 11:27:35', 0);
INSERT INTO `t_pet_favorite` VALUES (2, 1, 11, '2025-11-19 14:31:55', 1);
INSERT INTO `t_pet_favorite` VALUES (8, 1, 13, '2025-11-20 23:21:28', 0);
INSERT INTO `t_pet_favorite` VALUES (9, 1, 10, '2025-11-21 20:20:32', 0);
INSERT INTO `t_pet_favorite` VALUES (19, 1, 20, '2025-11-23 16:44:24', 0);
INSERT INTO `t_pet_favorite` VALUES (20, 1, 26, '2025-11-28 15:12:51', 0);
INSERT INTO `t_pet_favorite` VALUES (21, 1, 21, '2025-11-28 16:12:31', 0);
INSERT INTO `t_pet_favorite` VALUES (22, 4, 23, '2025-12-05 15:33:32', 0);
INSERT INTO `t_pet_favorite` VALUES (23, 7, 33, '2025-12-20 12:05:36', 0);
INSERT INTO `t_pet_favorite` VALUES (24, 7, 25, '2025-12-20 12:05:43', 0);

-- ----------------------------
-- Table structure for t_pet_like
-- ----------------------------
DROP TABLE IF EXISTS `t_pet_like`;
CREATE TABLE `t_pet_like`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `pet_id` bigint NOT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `deleted` tinyint NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_user_pet`(`user_id` ASC, `pet_id` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_pet_id`(`pet_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 23 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '宠物点赞表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_pet_like
-- ----------------------------
INSERT INTO `t_pet_like` VALUES (1, 1, 14, '2025-11-19 11:27:33', 0);
INSERT INTO `t_pet_like` VALUES (2, 1, 11, '2025-11-19 14:31:54', 1);
INSERT INTO `t_pet_like` VALUES (8, 1, 13, '2025-11-20 23:21:27', 0);
INSERT INTO `t_pet_like` VALUES (9, 1, 10, '2025-11-21 20:20:32', 0);
INSERT INTO `t_pet_like` VALUES (17, 1, 20, '2025-11-23 16:44:25', 0);
INSERT INTO `t_pet_like` VALUES (18, 1, 26, '2025-11-28 15:12:52', 0);
INSERT INTO `t_pet_like` VALUES (19, 1, 21, '2025-11-28 16:12:29', 1);
INSERT INTO `t_pet_like` VALUES (20, 4, 23, '2025-12-05 15:33:29', 0);
INSERT INTO `t_pet_like` VALUES (21, 7, 33, '2025-12-20 12:05:36', 0);
INSERT INTO `t_pet_like` VALUES (22, 7, 25, '2025-12-20 12:05:44', 0);

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
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '领养故事表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_story
-- ----------------------------
INSERT INTO `t_story` VALUES (1, '从小流浪到幸福家庭', '小黄在街头流浪了两年，直到遇到了爱心志愿者...', '# 从小流浪到幸福家庭\r\r\n\r\r小黄是一只金毛犬，在街头流浪了整整两年。它的毛发变得脏乱，身体也瘦弱不堪，但它依然保持着对人类的友善。\r\r\n\r\r## 遇见希望\r\r\n\r\r2023年的冬天，志愿者小王在巡查时发现了蜷缩在街角的小黄。看到它瑟瑟发抖的样子，小王毫不犹豫地将它带回了救助站。\r\r\n\r\r在救助站，兽医为小黄做了全面检查，发现它除了营养不良外，身体健康状况良好。经过一个月的精心照料，小黄恢复了往日的活力。\r\r\n\r\r## 寻找新家\r\r\n\r\r救助站工作人员开始为小黄寻找合适的领养家庭。他们收到了许多领养申请，经过仔细筛选和家访，最终选择了一家有爱心且有养犬经验的家庭。\r\r\n\r\r新主人是一对年轻夫妇，他们有一个宽敞的院子，非常适合小黄活动。第一次见面时，小黄就表现出了极大的热情，摇着尾巴围着他们转圈。\r\r\n\r\r## 幸福新生活\r\r\n\r\r如今的小黄已经完全融入了新家庭，它有了自己的小窝、玩具和定期的散步时间。主人说，小黄给他们带来了无尽的欢乐，是家庭不可缺少的一员。\r\r\n\r\r这个故事告诉我们，每一只流浪动物都值得被关爱，它们只是缺少一个机会去展现自己的美好。', 'https://192.168.99.228:9000/animal-adopt/7.png', '志愿者小王', '成功领养,狗狗', 136, 0, 0, '2025-11-20 22:53:07', '2025-12-08 00:05:00');
INSERT INTO `t_story` VALUES (2, '失散三年终重聚', '猫咪咪咪走失三年后，通过救助站的帮助终于与主人团聚...', '# 失散三年终重聚\r\r\n\r\r咪咪是一只英短蓝猫，三年前在一次搬家过程中意外走失。主人李先生和家人四处寻找，但始终没有找到它的踪迹。\r\r\n\r\r## 意外发现\r\r\n\r\r三年后的某天，救助站接收了一只流浪猫，经过检查发现它脖子上有一个模糊的 tattoo，经过仔细辨认，工作人员确认这就是走失的咪咪。\r\r\n\r\r通过 tattoo 上的信息，救助站联系到了李先生。当李先生听到这个消息时，简直不敢相信自己的耳朵。三年了，他从未放弃过寻找咪咪的希望。\r\r\n\r\r## 重逢时刻\r\r\n\r\r在救助站的重逢现场，李先生激动得热泪盈眶。而咪咪在看到主人的那一刻，也发出了熟悉的叫声，仿佛在说：\"我终于找到你了！\"\r\r\n\r\r经过检查，咪咪虽然瘦了一些，但身体健康状况良好。三年的流浪生活并没有磨灭它对主人的记忆和感情。\r\r\n\r\r## 珍惜当下\r\r\n\r\r回到家中，咪咪很快就适应了环境，它重新占据了自己最喜欢的沙发位置，仿佛从未离开过。\r\r\n\r\r李先生表示，这次重逢让他们更加珍惜彼此。他们为咪咪做了全面的健康检查，并决定以后无论去哪里都要更加小心，绝不再让它走失。', 'https://192.168.99.228:9000/animal-adopt/8.png', '救助站', '失散重聚,猫咪', 107, 1, 1, '2025-11-20 22:53:07', '2025-12-21 00:10:00');
INSERT INTO `t_story` VALUES (3, '从病重到康复', '受伤的流浪狗豆豆被救助后，经过精心治疗重获新生...', '# 从病重到康复\r\r\n\r\r豆豆是一只三岁的混血狗，被发现时已经奄奄一息。它的后腿被车撞伤，伤口感染严重，几乎没有人认为它能活下来。\r\r\n\r\r## 紧急救治\r\r\n\r\r救助站的医生为豆豆进行了紧急手术。手术过程中，医生们发现豆豆的伤情比预想的更严重，但他们没有放弃。经过6小时的手术，豆豆终于脱离了危险。\r\r\n\r\r## 康复之路\r\r\n\r\r康复期间，豆豆经历了巨大的痛苦。但在医护人员和志愿者的精心照料下，它逐渐恢复了精神。虽然后腿永久性受损，但豆豆学会了用前腿行走。\r\r\n\r\r## 新的开始\r\r\n\r\r一个月后，豆豆被一个充满爱心的家庭领养了。新主人为它配备了特殊的轮椅，豆豆现在可以像其他狗狗一样快乐地奔跑。', 'https://192.168.99.228:9000/animal-adopt/9.png', '宠物医生李医生', '救助故事,狗狗', 157, 1, 1, '2025-11-20 22:53:07', '2025-12-06 19:25:00');
INSERT INTO `t_story` VALUES (4, '高龄猫咪的新家', '八岁的老猫花花在救助站等待了半年，终于遇到了理解它的新主人...', '# 高龄猫咪的新家\r\r\n\r\r花花是一只八岁的波斯猫，因为主人搬家无法照顾而被送到了救助站。高龄的它在救助站里显得有些孤独，很少有人愿意领养一只老猫。\r\r\n\r\r## 漫长的等待\r\r\n\r\r半年过去了，花花在救助站里度过了它最孤独的日子。它每天都趴在窗边，看着来来往往的访客，希望有人能带它回家。\r\r\n\r\r## 命运的相遇\r\r\n\r\r直到有一天，一位退休的老奶奶来到了救助站。她说她也很孤独，需要一个伴侣。当她看到花花时，两个孤独的灵魂瞬间产生了共鸣。\r\r\n\r\r## 完美的匹配\r\r\n\r\r现在，花花和老奶奶每天都在一起。老奶奶为花花准备了最舒适的窝，花花则用温暖的陪伴填补了老奶奶心中的空缺。这是一个完美的匹配。', 'https://192.168.99.228:9000/animal-adopt/10.png', '领养者张女士', '成功领养,猫咪', 89, 1, 1, '2025-11-20 22:53:07', '2025-12-05 13:55:00');
INSERT INTO `t_story` VALUES (5, '残疾狗狗的逆袭', '三条腿的狗狗小黑通过训练成为了一只优秀的治疗犬...', '# 残疾狗狗的逆袭\r\r\n\r\r小黑原本是一只流浪狗，在一次车祸中失去了一条前腿。人们都认为它的人生已经完结，但小黑用行动证明了生命的韧性。\r\r\n\r\r## 不放弃的精神\r\r\n\r\r被救助后，小黑虽然失去了一条腿，但它从未放弃过。它用三条腿学会了走路、跑步，甚至可以跳跃。它的坚强感动了每一个见到它的人。\r\r\n\r\r## 特殊的训练\r\r\n\r\r一位专业的训练师看到了小黑的潜力，决定将它训练成一只治疗犬。经过六个月的训练，小黑成功了。\r\r\n\r\r## 新的使命\r\r\n\r\r现在，小黑在医院里工作，为患者们带去欢乐和治疗。它用自己的故事告诉每一个人：残疾不是终点，而是新的开始。', 'https://192.168.99.228:9000/animal-adopt/11.png', '训练师刘老师', '救助故事,狗狗', 215, 0, 0, '2025-11-20 22:53:07', '2025-12-08 01:05:00');
INSERT INTO `t_story` VALUES (6, '从恐惧到信任', '受过伤害的猫咪小白如何在新主人的关爱下重新学会信任...', '# 从恐惧到信任\r\r\n\r\r小白是一只曾经被虐待的猫咪。当它被救助站救出时，它对人类充满了恐惧和不信任。它躲在角落里，拒绝任何人的接近。\r\r\n\r\r## 漫长的治愈\r\r\n\r\r救助站的工作人员没有放弃。他们每天都坐在小白身边，用耐心和爱心慢慢接近它。几个月过去了，小白终于敢于接受抚摸。\r\r\n\r\r## 遇见真爱\r\r\n\r\r一位心理咨询师来到救助站，她看到了小白眼中的恐惧，也看到了它渴望被爱的渴望。她决定领养小白。\r\r\n\r\r## 重生\r\r\n\r\r在新主人的关爱下，小白逐渐打开了心扉。它学会了信任，学会了撒娇，学会了享受被爱的感觉。现在，小白是一只充满活力和爱心的猫咪，它用自己的故事告诉我们：爱可以治愈一切。', 'https://192.168.99.228:9000/animal-adopt/12.png', '心理咨询师赵老师', '成功领养,猫咪', 148, 0, 1, '2025-11-20 22:53:07', '2025-12-06 22:05:00');
INSERT INTO `t_story` VALUES (9, '被遗弃的希望', '暴雨天被弃桥洞的小狗小黑遭外卖小哥救助后重拾信任，如今成骑手身边的小明星，以经历诠释温暖能抵过伤害。', '# 从绝望到新生\n\n小黑是一只在暴雨天被遗弃在桥洞下的小狗。那天，它全身湿透，瑟瑟发抖，眼神中满是绝望与无助。\n\n## 意外的温暖\n\n一位路过的外卖小哥发现了小黑。他撑起伞，小心地抱起它，用身上的外套为它取暖。虽然小黑已经几乎失去了力气，但它依然努力摇着尾巴回应善意。\n\n## 重拾信任\n\n在随后的一个月里，小哥带着小黑去看医生，给它洗澡、梳毛，陪它散步。起初，小黑害怕接触人类，每次看到陌生人都会躲进桌子底下。但一次次温柔的抚摸与耐心的鼓励，让它重新感受到了安全。\n\n## 新的家\n\n如今，小黑已经成为骑手中的小明星。它每天都跟着主人跑单，见到熟悉的店家会主动摇尾巴问好。它的故事告诉我们：*世界上再多伤害，也抵不过一次真正的温暖。*\n', 'images/f4cfb61a6f934691ab36699fd35885b8.png', '外卖小哥', '温暖救助,生命救赎', 20, 2, 1, '2025-11-26 21:22:41', '2025-12-20 21:00:00');

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
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '故事收藏记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_story_favorite
-- ----------------------------
INSERT INTO `t_story_favorite` VALUES (1, 1, 6, '2025-11-21 23:08:58');
INSERT INTO `t_story_favorite` VALUES (2, 1, 2, '2025-11-23 17:16:52');
INSERT INTO `t_story_favorite` VALUES (3, 1, 3, '2025-11-26 14:58:01');
INSERT INTO `t_story_favorite` VALUES (4, 1, 7, '2025-11-26 20:12:47');
INSERT INTO `t_story_favorite` VALUES (5, 1, 9, '2025-11-26 21:23:11');
INSERT INTO `t_story_favorite` VALUES (6, 4, 4, '2025-12-05 13:54:32');

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
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '故事点赞记录表' ROW_FORMAT = Dynamic;

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
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES (1, 'super_admin', '$2a$10$jCsFThvTK3KCnCXmaRR1te4xQpRjfpM0A9wsxCEhA0hg31YupQB8O', '超级管理员', NULL, '13000000000', '3140000000@qq.com', 'avatars/e01c38ad0612481a8c5d8940da5a8c29.jpg', 0, NULL, '中国福建省漳州市xx县', NULL, NULL, NULL, NULL, 0, 'super_admin', 1, 1, NULL, '2025-11-19 11:16:41', '2025-12-16 14:39:35', 0);
INSERT INTO `t_user` VALUES (2, 'auditor', '$2a$10$smWyW1Z7pZq.nzzYNaCLdOrzUE6p3Pz3vZAKnkv4pshEX8jebulQi', '审核员', NULL, NULL, NULL, 'https://192.168.99.228:9000/animal-adopt/微信图片_20250929211436_125_10.jpg', 0, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'application_auditor', 1, 1, NULL, '2025-11-19 11:16:41', '2025-11-19 22:19:40', 0);
INSERT INTO `t_user` VALUES (3, 'admin', '$2a$10$smWyW1Z7pZq.nzzYNaCLdOrzUE6p3Pz3vZAKnkv4pshEX8jebulQi', '管理员', NULL, NULL, NULL, 'https://192.168.99.228:9000/animal-adopt/微信图片_20250929211439_126_10.jpg', 0, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'admin', 1, 1, NULL, '2025-11-19 11:16:41', '2025-12-16 14:39:35', 0);
INSERT INTO `t_user` VALUES (4, '林林1', '$2a$10$SIIObX9KGifpHzdPg52ruuzfx3DdJmzyDqK427UzJiNYkj6EA8Mq.', NULL, NULL, '13000000001', '3140000001@qq.com', 'http://localhost:9000/animal-adopt/avatars/b940a95eafa84210a7fe780d005ae7d7.jpg', 0, NULL, '中国福建省', NULL, NULL, NULL, NULL, 0, 'user', 1, 1, NULL, '2025-12-05 13:51:40', '2025-12-11 19:21:44', 0);
INSERT INTO `t_user` VALUES (5, '林林2', '$2a$10$MIKswZrzCIjZqn/d0ZNXYeHW164rjbSGpVRH2p3xJi2mnERNxaIJC', NULL, NULL, NULL, '3140000002@qq.com', NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'user', 1, 0, NULL, '2025-12-11 19:05:40', '2025-12-11 19:05:40', 0);
INSERT INTO `t_user` VALUES (6, '林林3', '$2a$10$6C8zq6T.hZiFQh9zxWv9KumqXFWOI7Oatms7PxYGShqbKOH5gP5IK', NULL, NULL, '13000000002', NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'user', 1, 0, NULL, '2025-12-11 19:22:01', '2025-12-11 19:22:01', 0);
INSERT INTO `t_user` VALUES (7, '林林4', '$2a$10$Evz3oLjVn6UoHPhwy4P17uoJjJzA/pdaNqSVXOh9LNoOVNWWnt2oO', NULL, NULL, NULL, '3140000003@qq.com', 'http://localhost:9000/animal-adopt/avatars/c72fb1627b004dd0bc004399fc97cb56.jpg', 0, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'user', 1, 0, NULL, '2025-12-14 22:43:18', '2025-12-14 22:43:18', 0);

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
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户认证表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_user_certification
-- ----------------------------
INSERT INTO `t_user_certification` VALUES (1, 1, '350626200000000000', 'certification/21f88ea991604506bd370feda551bc21.jpg', 'certification/51452f10a84a4900a05cbf90e0f12f49.jpg', 'approved', NULL, 1, '2025-11-27 01:14:26', '2025-11-26 23:53:40', '2025-11-26 23:53:40', 0);
INSERT INTO `t_user_certification` VALUES (2, 4, '350626200000000001', 'certification/5d12ee189e96411fa5f389634cf1037d.jpg', 'certification/b172f056febe406490baafc2a81ff984.jpg', 'approved', '今天没空', 1, '2025-12-05 17:21:57', '2025-12-05 15:30:34', '2025-12-05 17:21:52', 0);

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
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '验证码表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_verification_code
-- ----------------------------
INSERT INTO `t_verification_code` VALUES (1, 'phone', '13063184972', '169097', 'login', '2025-12-05 13:56:23', 1, '2025-12-05 13:51:23');
INSERT INTO `t_verification_code` VALUES (2, 'phone', '13063184972', '865767', 'login', '2025-12-05 14:36:08', 1, '2025-12-05 14:31:08');
INSERT INTO `t_verification_code` VALUES (3, 'email', '3149696140@qq.com', '264774', 'login', '2025-12-11 19:10:13', 0, '2025-12-11 19:05:13');
INSERT INTO `t_verification_code` VALUES (4, 'email', '3149696140@qq.com', '721443', 'login', '2025-12-11 19:10:22', 1, '2025-12-11 19:05:22');
INSERT INTO `t_verification_code` VALUES (5, 'phone', '13063184972', '666439', 'login', '2025-12-11 19:26:46', 1, '2025-12-11 19:21:46');
INSERT INTO `t_verification_code` VALUES (6, 'email', '3149696140@qq.com', '507016', 'login', '2025-12-14 22:47:09', 1, '2025-12-14 22:42:09');

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
) ENGINE = InnoDB AUTO_INCREMENT = 31 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '每日 uv 记录表' ROW_FORMAT = Dynamic;

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

SET FOREIGN_KEY_CHECKS = 1;
