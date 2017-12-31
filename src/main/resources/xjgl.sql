/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50720
 Source Host           : localhost:3306
 Source Schema         : xjgl

 Target Server Type    : MySQL
 Target Server Version : 50720
 File Encoding         : 65001

 Date: 31/12/2017 23:36:24
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_score
-- ----------------------------
DROP TABLE IF EXISTS `t_score`;
CREATE TABLE `t_score` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_time` datetime NOT NULL,
  `modified_time` datetime DEFAULT NULL,
  `num` float NOT NULL,
  `sno` varchar(32) NOT NULL,
  `subject` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_score
-- ----------------------------
BEGIN;
INSERT INTO `t_score` VALUES (1, '2017-12-31 22:42:56', '2017-12-31 22:51:44', 96, 'S666', 2);
INSERT INTO `t_score` VALUES (3, '2017-12-31 23:00:35', '2017-12-31 23:00:35', 88, 'S666', 1);
INSERT INTO `t_score` VALUES (4, '2017-12-31 23:00:45', '2017-12-31 23:00:45', 93, 'S666', 3);
INSERT INTO `t_score` VALUES (5, '2017-12-31 23:35:37', '2017-12-31 23:35:37', 78, 'S001', 1);
COMMIT;

-- ----------------------------
-- Table structure for t_student
-- ----------------------------
DROP TABLE IF EXISTS `t_student`;
CREATE TABLE `t_student` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_time` datetime NOT NULL,
  `modified_time` datetime DEFAULT NULL,
  `nickname` varchar(32) NOT NULL,
  `sno` varchar(32) NOT NULL,
  `status` smallint(6) NOT NULL DEFAULT '1',
  `register_date` date DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_student
-- ----------------------------
BEGIN;
INSERT INTO `t_student` VALUES (1, '2017-12-31 00:40:11', '2017-12-31 23:32:13', 'CHW', 'S001', 2, '2017-12-31');
INSERT INTO `t_student` VALUES (2, '2017-12-31 09:13:46', '2017-12-31 23:01:15', 'caixiaowen', 'S666', 1, '2017-12-31');
COMMIT;

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_time` datetime NOT NULL,
  `modified_time` datetime NOT NULL,
  `nickname` varchar(16) NOT NULL,
  `password` varchar(40) NOT NULL,
  `telephone` varchar(16) NOT NULL,
  `username` varchar(16) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_user
-- ----------------------------
BEGIN;
INSERT INTO `t_user` VALUES (1, '2017-12-31 00:32:16', '2017-12-31 00:32:14', ' 蔡大哥', '342401bf3afbe8b5f7c742f99b4759bf4a21a933', '18826137274', 'cherish');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
