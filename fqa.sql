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

 Date: 15/10/2020 22:08:55
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for question
-- ----------------------------
DROP TABLE IF EXISTS `question`;
CREATE TABLE `question`  (
  `id` int(2) UNSIGNED ZEROFILL NOT NULL AUTO_INCREMENT,
  `group_id` bigint(20) NOT NULL,
  `question` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '每个群问题只能有一个',
  `answer` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `last_edit_user` bigint(255) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `question`(`question`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for welcome
-- ----------------------------
DROP TABLE IF EXISTS `welcome`;
CREATE TABLE `welcome`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `group` bigint(255) NOT NULL,
  `talk` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
