/*
 Navicat Premium Data Transfer

 Source Server         : local-mac
 Source Server Type    : MySQL
 Source Server Version : 50732
 Source Host           : localhost:3306
 Source Schema         : xmutca-incubator-auth

 Target Server Type    : MySQL
 Target Server Version : 50732
 File Encoding         : 65001

 Date: 23/10/2020 17:56:04
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for gateway_route_definition
-- ----------------------------
DROP TABLE IF EXISTS `gateway_route_definition`;
CREATE TABLE `gateway_route_definition` (
  `id` bigint(20) NOT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `route_id` varchar(255) DEFAULT NULL,
  `uri` varchar(255) DEFAULT NULL,
  `predicates` text,
  `filters` text,
  `status` char(2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

SET FOREIGN_KEY_CHECKS = 1;
