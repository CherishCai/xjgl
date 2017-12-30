/*
Navicat MySQL Data Transfer

Source Server         : 127.0.0.1
Source Server Version : 50711
Source Host           : 127.0.0.1:3306
Source Database       : xjgl

Target Server Type    : MYSQL
Target Server Version : 50711
File Encoding         : 65001

Date: 2017-12-31 00:42:18
*/

SET FOREIGN_KEY_CHECKS=0;

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
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_student
-- ----------------------------
INSERT INTO `t_student` VALUES ('1', '2017-12-31 00:40:11', '2017-12-31 00:40:11', 'CHW', 'S0001');

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
INSERT INTO `t_user` VALUES ('1', '2017-12-31 00:32:16', '2017-12-31 00:32:14', ' 蔡大哥', '342401bf3afbe8b5f7c742f99b4759bf4a21a933', '18826137274', 'cherish');
