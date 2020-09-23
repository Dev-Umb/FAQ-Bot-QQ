/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 100411
 Source Host           : localhost:3306
 Source Schema         : fqa

 Target Server Type    : MySQL
 Target Server Version : 100411
 File Encoding         : 65001

 Date: 23/09/2020 19:49:48
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for question
-- ----------------------------
DROP TABLE IF EXISTS `question`;
CREATE TABLE `question`  (
  `id` int(3) NOT NULL AUTO_INCREMENT,
  `group_id` bigint(20) NOT NULL,
  `question` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '每个群问题只能有一个',
  `answer` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `last_edit_user` bigint(255) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `question`(`question`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2162 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of question
-- ----------------------------
INSERT INTO `question` VALUES (1, 1009647596, '草', '{\"imgList\":[\"{EEC6E590-996E-28CA-55FE-B2B56C4707F1}.mirai\",\"{8A8CE67B-9CFF-946A-CFE0-28E139815EBB}.mirai\"],\"atList\":[],\"text\":\"\"}', 1149558764);
INSERT INTO `question` VALUES (2, 1009647596, '图片测试', '{\"imgList\":[\"{EEC6E590-996E-28CA-55FE-B2B56C4707F1}.mirai\"],\"atList\":[],\"text\":\"\"}', 1149558764);
INSERT INTO `question` VALUES (3, 1009647596, '终极测试', '{\"imgList\":[\"{EEC6E590-996E-28CA-55FE-B2B56C4707F1}.mirai\"],\"atList\":[1227427939,3394886607],\"text\":\" 测试测试\"}', 1149558764);
INSERT INTO `question` VALUES (2156, 1009647596, '测试', '{\"imgList\":[\"{E5B3AB08-C842-B46A-8A0C-E7079A49FEF6}.mirai\"],\"atList\":[1227427929],\"text\":\" 出来测bug\"}', 1149558764);
INSERT INTO `question` VALUES (2157, 1009647596, '？？？', '{\"imgList\":[\"{E5B3AB08-C842-B46A-8A0C-E7079A49FEF6}.mirai\"],\"atList\":[],\"text\":\"\"}', 1149558764);
INSERT INTO `question` VALUES (2159, 1009647596, '李某是菜b', '{\"imgList\":[],\"atList\":[1227427929],\"text\":\" 菜比\"}', 1149558764);
INSERT INTO `question` VALUES (2160, 1009647596, '# 2', '{\"imgList\":[],\"atList\":[],\"text\":\"安装在\"}', 1227427929);
INSERT INTO `question` VALUES (2161, 1009647596, '添加问题', '{\"imgList\":[],\"atList\":[],\"text\":\"添加问题\"}', 1149558764);

SET FOREIGN_KEY_CHECKS = 1;
