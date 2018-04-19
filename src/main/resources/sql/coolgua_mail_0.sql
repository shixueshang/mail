/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50716
Source Host           : localhost:3306
Source Database       : coolgua_mail_0

Target Server Type    : MYSQL
Target Server Version : 50716
File Encoding         : 65001

Date: 2018-02-08 18:36:12
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for m_attachment
-- ----------------------------
DROP TABLE IF EXISTS `m_attachment`;
CREATE TABLE `m_attachment` (
  `id` varchar(40) NOT NULL,
  `template_id` varchar(40) DEFAULT NULL,
  `file_name` varchar(200) DEFAULT NULL,
  `file_path` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for m_black_list
-- ----------------------------
DROP TABLE IF EXISTS `m_black_list`;
CREATE TABLE `m_black_list` (
  `id` varchar(40) NOT NULL,
  `org_id` int(11) NOT NULL,
  `recipient` varchar(40) NOT NULL,
  `event` char(20) DEFAULT NULL,
  `creator` varchar(40) DEFAULT NULL,
  `creator_name` varchar(40) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for m_data_source
-- ----------------------------
DROP TABLE IF EXISTS `m_data_source`;
CREATE TABLE `m_data_source` (
  `id` varchar(40) NOT NULL,
  `org_id` int(11) NOT NULL,
  `name` varchar(40) NOT NULL,
  `ds_type` tinyint(4) DEFAULT '1' COMMENT '数据源类型',
  `fields` varchar(2000) DEFAULT NULL COMMENT '字段',
  `deleted` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for m_mail
-- ----------------------------
DROP TABLE IF EXISTS `m_mail`;
CREATE TABLE `m_mail` (
  `id` varchar(40) NOT NULL,
  `org_id` int(8) NOT NULL,
  `sender` varchar(40) DEFAULT NULL COMMENT '发件人地址',
  `sender_name` varchar(40) DEFAULT NULL COMMENT '发件人姓名',
  `reply_address` varchar(40) DEFAULT NULL COMMENT '默认回复地址',
  `send_to` text NOT NULL COMMENT '收件人',
  `subject` varchar(200) NOT NULL COMMENT '邮件主题',
  `content` text NOT NULL COMMENT '邮件内容',
  `has_attachment` tinyint(4) DEFAULT NULL COMMENT '是否有附件',
  `template_id` varchar(40) DEFAULT NULL COMMENT '模板id',
  `scheduled` tinyint(4) DEFAULT NULL COMMENT '是否定时',
  `status` tinyint(1) DEFAULT NULL COMMENT '状态',
  `total` int(8) DEFAULT '0' COMMENT '总量',
  `request` int(8) DEFAULT '0' COMMENT '已寄出',
  `channel_request` int(8) DEFAULT '0' COMMENT '通道寄出',
  `deliver` int(8) DEFAULT '0' COMMENT '已送达',
  `bounce` int(8) DEFAULT '0' COMMENT '硬退信',
  `soft_bounce` int(8) DEFAULT '0' COMMENT '软退信',
  `invalid_address` int(8) DEFAULT '0' COMMENT '无效邮址',
  `spam` int(8) DEFAULT '0' COMMENT '垃圾邮件',
  `repeat_address` int(8) DEFAULT '0' COMMENT '重复邮址',
  `open_total` int(8) DEFAULT '0' COMMENT '打开总数',
  `unique_open` int(8) DEFAULT '0' COMMENT '独立打开数',
  `click_total` int(8) DEFAULT '0' COMMENT '点击总数',
  `click_unique` int(8) DEFAULT '0' COMMENT '独立点击数',
  `channel_exclusion` int(8) DEFAULT '0' COMMENT '通道排除',
  `has_exclusion` tinyint(1) DEFAULT '0' COMMENT '是否已经在排除列表',
  `unsubscribe` tinyint(1) DEFAULT NULL COMMENT '是否添加退订链接',
  `unsubscribe_language` char(10) DEFAULT NULL COMMENT '退订语言',
  `unsubscribe_count` int(8) DEFAULT '0' COMMENT '退订总数',
  `replace_module` varchar(40) DEFAULT NULL COMMENT '替换模块',
  `provider_id` tinyint(1) NOT NULL COMMENT '邮件提供商',
  `campaign_id` varchar(40) DEFAULT NULL COMMENT '活动id(webpower必填)',
  `mailing_id` varchar(40) DEFAULT NULL COMMENT '邮件id(第三方提供)',
  `send_time` datetime DEFAULT NULL COMMENT '发送时间',
  `creator` varchar(40) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

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

-- ----------------------------
-- Table structure for m_mail_url_click
-- ----------------------------
DROP TABLE IF EXISTS `m_mail_url_click`;
CREATE TABLE `m_mail_url_click` (
  `id` varchar(40) NOT NULL,
  `mai_id` varchar(40) NOT NULL,
  `mail_detail_id` varchar(40) NOT NULL,
  `url` varchar(255) DEFAULT NULL,
  `recipient` varchar(40) DEFAULT NULL,
  `click_count` tinyint(4) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for m_provider_config
-- ----------------------------
DROP TABLE IF EXISTS `m_provider_config`;
CREATE TABLE `m_provider_config` (
  `id` varchar(40) NOT NULL,
  `org_id` int(11) NOT NULL,
  `provider_id` tinyint(4) NOT NULL,
  `account_name` varchar(200) NOT NULL COMMENT '帐号',
  `account_pass` varchar(40) NOT NULL COMMENT '密码',
  `creator` varchar(40) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for m_template
-- ----------------------------
DROP TABLE IF EXISTS `m_template`;
CREATE TABLE `m_template` (
  `id` varchar(40) NOT NULL,
  `org_id` int(11) NOT NULL,
  `provider_id` tinyint(4) DEFAULT NULL,
  `name` varchar(100) NOT NULL COMMENT '模板名称',
  `ds_id` varchar(40) DEFAULT NULL COMMENT '数据源',
  `ds_type` tinyint(4) DEFAULT NULL COMMENT '数据源类型',
  `mail_field` varchar(40) DEFAULT NULL COMMENT '邮址字段',
  `mail_type` tinyint(4) DEFAULT NULL COMMENT '邮件类型(标准,个性)',
  `replace_field` varchar(255) DEFAULT NULL,
  `sender_name` varchar(40) DEFAULT NULL COMMENT '发件人姓名',
  `sender_address` varchar(40) DEFAULT NULL COMMENT '发件人地址',
  `reply_address` varchar(40) DEFAULT NULL COMMENT '回复地址',
  `subject` varchar(100) DEFAULT NULL COMMENT '邮件主题',
  `content` text NOT NULL COMMENT '模板内容',
  `status` tinyint(4) DEFAULT NULL COMMENT '状态(进行中,关闭,删除)',
  `scheduled` tinyint(1) DEFAULT NULL,
  `send_time` datetime DEFAULT NULL,
  `unsubscribe` tinyint(1) DEFAULT NULL,
  `unsubscribe_language` char(10) DEFAULT NULL,
  `campaign_id` varchar(40) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `creator` varchar(40) DEFAULT NULL,
  `creator_name` varchar(40) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `mender` varchar(40) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
