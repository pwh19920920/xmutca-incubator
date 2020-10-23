/*
 Navicat Premium Data Transfer

 Source Server         : local-mac
 Source Server Type    : MySQL
 Source Server Version : 50732
 Source Host           : localhost:3306
 Source Schema         : xmutca-incubator-passport

 Target Server Type    : MySQL
 Target Server Version : 50732
 File Encoding         : 65001

 Date: 23/10/2020 17:56:12
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for passport_client_info
-- ----------------------------
DROP TABLE IF EXISTS `passport_client_info`;
CREATE TABLE `passport_client_info` (
  `id` bigint(20) NOT NULL,
  `client_id` varchar(255) DEFAULT NULL,
  `secret` varchar(255) DEFAULT NULL,
  `grant_types` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of passport_client_info
-- ----------------------------
BEGIN;
INSERT INTO `passport_client_info` VALUES (1, '1', '1', 'login_password');
COMMIT;

-- ----------------------------
-- Table structure for passport_token_secret
-- ----------------------------
DROP TABLE IF EXISTS `passport_token_secret`;
CREATE TABLE `passport_token_secret` (
  `id` bigint(20) NOT NULL,
  `client_id` varchar(255) DEFAULT NULL,
  `access_token_id` varchar(255) DEFAULT NULL,
  `access_token_secret` varchar(255) DEFAULT NULL,
  `access_expire_time` datetime DEFAULT NULL,
  `refresh_token_id` varchar(255) DEFAULT NULL,
  `refresh_token_secret` varchar(255) DEFAULT NULL,
  `refresh_expire_time` datetime DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `enable` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of passport_token_secret
-- ----------------------------
BEGIN;
INSERT INTO `passport_token_secret` VALUES (351048412633759744, '562c08ee-4e6e-4229-9574-a5f9ea6fa59d', '48118637-2619-4a6b-b2a2-c7df534b884e', '5d0c3507-97bb-47ae-8f18-d4f43d1d5ffc', '2020-10-23 08:14:01', '0fb64579-2ff8-4276-b726-b061c9d71afb', '0beb735a-529c-4d0d-9479-6079ea9f7073', '2020-11-22 07:14:01', 1, 1);
INSERT INTO `passport_token_secret` VALUES (351048598319792128, 'c3995648-c4b6-4f13-8aa2-632dbf6fd87f', 'b54b7b5e-e42e-47b0-be1d-758ceb5053ae', 'd4417538-d9ea-41d9-993e-ce13d78ce8da', '2020-10-23 08:14:46', '1bc016b7-d6c0-44c4-84a6-750f9b5a77c5', 'd38408ed-d73c-41ee-a5a2-d5b9b3624cd1', '2020-11-22 07:14:46', 1, 1);
INSERT INTO `passport_token_secret` VALUES (351048811969249280, '823da6a5-f6ef-4907-a442-4e67eff358e0', 'd0e4d518-da04-40db-969c-defd1be684b0', '8f779f15-5aa6-4519-aa4e-d89315abac87', '2020-10-23 08:15:37', '71de324a-a952-4c57-a9d6-fcc572767d8c', 'db7a001c-acba-4287-a904-949e2a0215f1', '2020-11-22 07:15:37', 1, 1);
INSERT INTO `passport_token_secret` VALUES (351049615564341248, '60536750-85c6-432c-844b-55c690b78d21', '8fca9fb8-7936-4b28-a468-0c02715ce08c', '5214ed32-2be8-45ef-9e2c-290de04c10d8', '2020-10-23 08:18:48', '55c25596-0c3f-4051-a84a-c0f44249ae6a', '69d38007-a3dc-4a49-921d-46e554b3c030', '2020-11-22 07:18:48', 1, 1);
INSERT INTO `passport_token_secret` VALUES (351049742341373952, '5d4cb450-9681-415b-bc3b-1651c8df8d8c', '1724bc45-f48a-44e1-9eff-c8a84c70a467', '3309331e-c60a-454d-b8f9-b46f6772b4ca', '2020-10-23 08:19:19', '01cd0011-28c0-4197-b1a8-e59231e57d0d', '4bc205fa-63e4-497d-914e-79f887c16cbc', '2020-11-22 07:19:19', 1, 1);
INSERT INTO `passport_token_secret` VALUES (351077143377084416, 'fb2e15fb-ae0b-437a-8976-825613e2417b', '4cc13ccf-16e2-4820-b138-597fd6ad4989', '3e3c0f09-9cbb-4e92-8a24-dcde0b4f3a9d', '2020-10-23 10:08:11', 'df83ab33-1963-476e-90c6-51e0417a68d0', 'b4cac445-e2d9-4c29-9b9d-9f70b4099bf5', '2020-11-22 09:08:11', 1, 1);
COMMIT;

-- ----------------------------
-- Table structure for passport_user_local_auth
-- ----------------------------
DROP TABLE IF EXISTS `passport_user_local_auth`;
CREATE TABLE `passport_user_local_auth` (
  `id` bigint(20) NOT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `mobile` varchar(11) DEFAULT NULL,
  `salt` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of passport_user_local_auth
-- ----------------------------
BEGIN;
INSERT INTO `passport_user_local_auth` VALUES (1, 1, 'admin', '1e191d851b3b49a248f4ea62f6b06410', '1775xx36719', '123456');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
