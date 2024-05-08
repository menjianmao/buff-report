/*
 Navicat MySQL Data Transfer

 Source Server         : 121.41.2.141
 Source Server Type    : MySQL
 Source Server Version : 50718
 Source Host           : 121.41.2.141:3307
 Source Schema         : ry

 Target Server Type    : MySQL
 Target Server Version : 50718
 File Encoding         : 65001

 Date: 08/05/2024 23:58:07
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for cs_need_get_goods
-- ----------------------------
DROP TABLE IF EXISTS `cs_need_get_goods`;
CREATE TABLE `cs_need_get_goods`  (
  `id` int(32) NOT NULL AUTO_INCREMENT,
  `goods_id` int(16) NULL DEFAULT NULL COMMENT '网易buff平台的商品id',
  `igxe_goods_id` int(16) NULL DEFAULT NULL COMMENT 'igxe平台的商品id',
  `c5_goods_id` bigint(32) NULL DEFAULT NULL COMMENT 'c5平台商品id',
  `goods_name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品名称',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `base_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '设置底价',
  `base_mark` tinyint(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '底价标记',
  `level` tinyint(1) UNSIGNED NOT NULL DEFAULT 3 COMMENT '关注等级',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 76 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cs_need_get_goods
-- ----------------------------
INSERT INTO `cs_need_get_goods` VALUES (26, 921456, 672355, NULL, 'AK-47 | 一发入魂 (略有磨损)', '2024-04-10 10:58:21', 200.00, 1, 7);
INSERT INTO `cs_need_get_goods` VALUES (27, 763400, 618614, NULL, 'AWP | 黑色魅影 (略有磨损)', '2024-04-18 09:35:51', 170.00, 1, 8);
INSERT INTO `cs_need_get_goods` VALUES (36, 781660, 678496, NULL, '沙漠之鹰 | 印花集 (崭新出厂)', '2024-04-18 16:07:31', 500.00, 1, 5);
INSERT INTO `cs_need_get_goods` VALUES (37, 769563, 624860, NULL, '锯齿爪刀（★） | 虎牙 (崭新出厂)', '2024-04-18 16:07:31', 4050.00, 1, 3);
INSERT INTO `cs_need_get_goods` VALUES (38, 43046, 3794, NULL, '爪子刀（★） | 致命紫罗兰 (略有磨损)', '2024-04-18 16:07:31', 5000.00, 1, 3);
INSERT INTO `cs_need_get_goods` VALUES (51, 769910, 634975, NULL, '锯齿爪刀（★ StatTrak™） | 虎牙 (崭新出厂)', '2024-04-19 08:54:47', 4100.00, 1, 3);
INSERT INTO `cs_need_get_goods` VALUES (52, 769540, 628593, NULL, '短剑（★） | 致命紫罗兰 (久经沙场)', '2024-04-19 08:54:47', 1200.00, 1, 3);
INSERT INTO `cs_need_get_goods` VALUES (53, 776911, 651250, NULL, '骷髅匕首（★） | 表面淬火 (久经沙场)', '2024-04-19 08:54:47', 3200.00, 1, 3);
INSERT INTO `cs_need_get_goods` VALUES (54, 776881, 651386, NULL, '骷髅匕首（★） | 表面淬火 (略有磨损)', '2024-04-19 11:11:15', 3800.00, 1, 3);
INSERT INTO `cs_need_get_goods` VALUES (55, 42389, NULL, NULL, '刺刀（★） | 多普勒 (崭新出厂)', '2024-04-19 11:11:15', 4000.00, 1, 3);
INSERT INTO `cs_need_get_goods` VALUES (56, 776917, NULL, NULL, '骷髅匕首（★） | 深红之网 (略有磨损)', '2024-04-19 11:11:15', 6500.00, 1, 5);
INSERT INTO `cs_need_get_goods` VALUES (57, 42990, NULL, NULL, '爪子刀（★） | 深红之网 (久经沙场)', '2024-04-19 11:11:15', 5000.00, 1, 3);
INSERT INTO `cs_need_get_goods` VALUES (58, 42590, NULL, NULL, '蝴蝶刀（★） | 致命紫罗兰 (久经沙场)', '2024-04-19 11:11:15', 4600.00, 1, 3);
INSERT INTO `cs_need_get_goods` VALUES (59, 33961, NULL, NULL, 'AK-47 | 红线 (略有磨损)', '2024-04-22 00:57:46', 440.00, 1, 10);
INSERT INTO `cs_need_get_goods` VALUES (60, 34066, 3760, NULL, 'AWP | 二西莫夫 (久经沙场)', '2024-04-22 00:57:47', 740.00, 1, 10);
INSERT INTO `cs_need_get_goods` VALUES (61, 763469, NULL, NULL, 'AK-47 | 二西莫夫 (崭新出厂)', '2024-04-22 00:57:48', 1150.00, 1, 10);
INSERT INTO `cs_need_get_goods` VALUES (62, 33971, NULL, NULL, 'AK-47 | 皇后 (略有磨损)', '2024-04-22 08:33:46', 400.00, 1, 7);
INSERT INTO `cs_need_get_goods` VALUES (63, 42964, NULL, NULL, '爪子刀（★） | 自动化 (久经沙场)', '2024-04-22 08:33:47', 0.00, 0, 3);
INSERT INTO `cs_need_get_goods` VALUES (64, 33960, NULL, NULL, 'AK-47 | 红线 (久经沙场)', '2024-04-24 18:54:20', 0.00, 0, 10);
INSERT INTO `cs_need_get_goods` VALUES (65, 35197, NULL, NULL, 'M4A1 消音型 | 毁灭者 2000 (崭新出厂)', '2024-04-24 18:54:30', 0.00, 0, 5);
INSERT INTO `cs_need_get_goods` VALUES (66, 33868, NULL, NULL, 'AK-47 | 血腥运动 (崭新出厂)', '2024-04-24 18:54:40', 0.00, 0, 8);
INSERT INTO `cs_need_get_goods` VALUES (67, 42569, NULL, NULL, '蝴蝶刀（★） | 外表生锈 (战痕累累)', '2024-04-24 18:54:50', 0.00, 0, 3);
INSERT INTO `cs_need_get_goods` VALUES (68, 759517, NULL, NULL, '锯齿爪刀（★） | 深红之网 (略有磨损)', '2024-04-28 09:00:40', 0.00, 0, 3);
INSERT INTO `cs_need_get_goods` VALUES (69, 759638, NULL, NULL, '锯齿爪刀（★ StatTrak™） | 表面淬火 (略有磨损)', '2024-04-28 09:00:41', 0.00, 0, 3);
INSERT INTO `cs_need_get_goods` VALUES (70, 774738, NULL, NULL, '爱娃特工 | 联邦调查局（FBI）', '2024-04-28 09:00:42', 0.00, 0, 3);
INSERT INTO `cs_need_get_goods` VALUES (71, 43050, NULL, NULL, '爪子刀（★） | 都市伪装 (略有磨损)', '2024-04-28 09:00:42', 0.00, 0, 3);
INSERT INTO `cs_need_get_goods` VALUES (72, 835613, NULL, NULL, '残酷的达里尔爵士（皇家）| 专业人士', '2024-04-28 09:00:43', 0.00, 0, 3);
INSERT INTO `cs_need_get_goods` VALUES (73, 759742, NULL, NULL, '锯齿爪刀（★） | 屠夫 (略有磨损)', '2024-04-28 09:00:44', 0.00, 0, 3);
INSERT INTO `cs_need_get_goods` VALUES (74, 769581, NULL, NULL, '锯齿爪刀（★） | 致命紫罗兰 (略有磨损)', '2024-04-28 09:00:44', 0.00, 0, 3);
INSERT INTO `cs_need_get_goods` VALUES (75, 42544, NULL, NULL, '蝴蝶刀（★） | 表面淬火 (略有磨损)', '2024-05-07 09:44:51', 0.00, 0, 3);

SET FOREIGN_KEY_CHECKS = 1;
