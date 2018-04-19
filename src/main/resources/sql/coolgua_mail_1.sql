/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50716
Source Host           : localhost:3306
Source Database       : coolgua_mail_1

Target Server Type    : MYSQL
Target Server Version : 50716
File Encoding         : 65001

Date: 2018-02-08 18:36:22
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for m_mail_detail_0
-- ----------------------------
DROP TABLE IF EXISTS `m_mail_detail_0`;
CREATE TABLE `m_mail_detail_0` (
  `id` varchar(40) NOT NULL,
  `mail_id` varchar(40) NOT NULL,
  `provider_id` tinyint(1) NOT NULL,
  `status` tinyint(4) DEFAULT NULL COMMENT '邮址状态',
  `event` char(20) DEFAULT NULL COMMENT '事件',
  `scheduled` tinyint(1) DEFAULT '0' COMMENT '是否是定时邮件',
  `sharding_category` tinyint(4) DEFAULT NULL COMMENT '分片策略',
  `recipient` varchar(40) DEFAULT NULL COMMENT '接收人',
  `email_id` varchar(200) DEFAULT NULL COMMENT 'sendcloud返回邮址唯一标识',
  `remote_data` text COMMENT '远程推送数据',
  `open` tinyint(4) DEFAULT '0' COMMENT '打开次数',
  `last_open_time` datetime DEFAULT NULL COMMENT '最近一次打开时间',
  `replace_json` text COMMENT '替换文本',
  `unsubscribe` tinyint(1) DEFAULT '0' COMMENT '是否退订',
  `unsubscribe_time` datetime DEFAULT NULL COMMENT '退订时间',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_md_mid` (`mail_id`,`provider_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for m_mail_detail_1
-- ----------------------------
DROP TABLE IF EXISTS `m_mail_detail_1`;
CREATE TABLE `m_mail_detail_1` (
  `id` varchar(40) NOT NULL,
  `mail_id` varchar(40) NOT NULL,
  `provider_id` tinyint(1) NOT NULL,
  `status` tinyint(4) DEFAULT NULL COMMENT '邮址状态',
  `event` char(20) DEFAULT NULL COMMENT '事件',
  `scheduled` tinyint(1) DEFAULT '0' COMMENT '是否是定时邮件',
  `sharding_category` tinyint(4) DEFAULT NULL COMMENT '分片策略',
  `recipient` varchar(40) DEFAULT NULL COMMENT '接收人',
  `email_id` varchar(200) DEFAULT NULL COMMENT 'sendcloud返回邮址唯一标识',
  `remote_data` text COMMENT '远程推送数据',
  `open` tinyint(4) DEFAULT '0' COMMENT '打开次数',
  `last_open_time` datetime DEFAULT NULL COMMENT '最近一次打开时间',
  `replace_json` text COMMENT '替换文本',
  `unsubscribe` tinyint(1) DEFAULT '0' COMMENT '是否退订',
  `unsubscribe_time` datetime DEFAULT NULL COMMENT '退订时间',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_md_mid` (`mail_id`,`provider_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for m_mail_detail_2
-- ----------------------------
DROP TABLE IF EXISTS `m_mail_detail_2`;
CREATE TABLE `m_mail_detail_2` (
  `id` varchar(40) NOT NULL,
  `mail_id` varchar(40) NOT NULL,
  `provider_id` tinyint(1) NOT NULL,
  `status` tinyint(4) DEFAULT NULL COMMENT '邮址状态',
  `event` char(20) DEFAULT NULL COMMENT '事件',
  `scheduled` tinyint(1) DEFAULT '0' COMMENT '是否是定时邮件',
  `sharding_category` tinyint(4) DEFAULT NULL COMMENT '分片策略',
  `recipient` varchar(40) DEFAULT NULL COMMENT '接收人',
  `email_id` varchar(200) DEFAULT NULL COMMENT 'sendcloud返回邮址唯一标识',
  `remote_data` text COMMENT '远程推送数据',
  `open` tinyint(4) DEFAULT '0' COMMENT '打开次数',
  `last_open_time` datetime DEFAULT NULL COMMENT '最近一次打开时间',
  `replace_json` text COMMENT '替换文本',
  `unsubscribe` tinyint(1) DEFAULT '0' COMMENT '是否退订',
  `unsubscribe_time` datetime DEFAULT NULL COMMENT '退订时间',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_md_mid` (`mail_id`,`provider_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for m_mail_detail_3
-- ----------------------------
DROP TABLE IF EXISTS `m_mail_detail_3`;
CREATE TABLE `m_mail_detail_3` (
  `id` varchar(40) NOT NULL,
  `mail_id` varchar(40) NOT NULL,
  `provider_id` tinyint(1) NOT NULL,
  `status` tinyint(4) DEFAULT NULL COMMENT '邮址状态',
  `event` char(20) DEFAULT NULL COMMENT '事件',
  `scheduled` tinyint(1) DEFAULT '0' COMMENT '是否是定时邮件',
  `sharding_category` tinyint(4) DEFAULT NULL COMMENT '分片策略',
  `recipient` varchar(40) DEFAULT NULL COMMENT '接收人',
  `email_id` varchar(200) DEFAULT NULL COMMENT 'sendcloud返回邮址唯一标识',
  `remote_data` text COMMENT '远程推送数据',
  `open` tinyint(4) DEFAULT '0' COMMENT '打开次数',
  `last_open_time` datetime DEFAULT NULL COMMENT '最近一次打开时间',
  `replace_json` text COMMENT '替换文本',
  `unsubscribe` tinyint(1) DEFAULT '0' COMMENT '是否退订',
  `unsubscribe_time` datetime DEFAULT NULL COMMENT '退订时间',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_md_mid` (`mail_id`,`provider_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for m_mail_detail_4
-- ----------------------------
DROP TABLE IF EXISTS `m_mail_detail_4`;
CREATE TABLE `m_mail_detail_4` (
  `id` varchar(40) NOT NULL,
  `mail_id` varchar(40) NOT NULL,
  `provider_id` tinyint(1) NOT NULL,
  `status` tinyint(4) DEFAULT NULL COMMENT '邮址状态',
  `event` char(20) DEFAULT NULL COMMENT '事件',
  `scheduled` tinyint(1) DEFAULT '0' COMMENT '是否是定时邮件',
  `sharding_category` tinyint(4) DEFAULT NULL COMMENT '分片策略',
  `recipient` varchar(40) DEFAULT NULL COMMENT '接收人',
  `email_id` varchar(200) DEFAULT NULL COMMENT 'sendcloud返回邮址唯一标识',
  `remote_data` text COMMENT '远程推送数据',
  `open` tinyint(4) DEFAULT '0' COMMENT '打开次数',
  `last_open_time` datetime DEFAULT NULL COMMENT '最近一次打开时间',
  `replace_json` text COMMENT '替换文本',
  `unsubscribe` tinyint(1) DEFAULT '0' COMMENT '是否退订',
  `unsubscribe_time` datetime DEFAULT NULL COMMENT '退订时间',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_md_mid` (`mail_id`,`provider_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
