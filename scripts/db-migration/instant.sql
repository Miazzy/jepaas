-- MySQL dump 10.13  Distrib 5.7.26, for Win64 (x86_64)
--
-- Host: localhost    Database: instant
-- ------------------------------------------------------
-- Server version	5.7.26

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `instant_actual`
--

DROP TABLE IF EXISTS `instant_actual`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `instant_actual` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `LEFT_ID` varchar(100) DEFAULT NULL COMMENT '我的ID',
  `LEFT_NAME` varchar(100) DEFAULT NULL COMMENT '我的名称-->按照正常是从friend中获取的',
  `TYPE` varchar(20) DEFAULT NULL COMMENT '类型1:对话消息,2:推送消息3.组消息',
  `CREATE_TIME` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `FRIEND_ID` bigint(20) DEFAULT NULL COMMENT '好友主键-->主要可以查询到一些好友的备注等信息',
  `RIGHT_ID` varchar(255) DEFAULT NULL COMMENT '好友ID/群组/推送ID',
  `RIGHT_NAME` varchar(255) DEFAULT NULL COMMENT '好友名称/群组名称/推送名称',
  `UPDATE_TIME` timestamp NULL DEFAULT NULL COMMENT '最后更新时间',
  `LAST_MSG` varchar(255) DEFAULT NULL COMMENT '最后一条消息',
  `MSG_TYPE` varchar(10) DEFAULT NULL COMMENT '最后一个消息的类型',
  `LAST_USER_NAME` varchar(50) CHARACTER SET utf32 DEFAULT NULL COMMENT '最后发送人姓名',
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `FK_REF_ACTUAL_FRIEND` (`FRIEND_ID`) USING BTREE
) ENGINE=MyISAM AUTO_INCREMENT=12358 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='会话表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `instant_actual`
--

LOCK TABLES `instant_actual` WRITE;
/*!40000 ALTER TABLE `instant_actual` DISABLE KEYS */;
/*!40000 ALTER TABLE `instant_actual` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `instant_actual_info`
--

DROP TABLE IF EXISTS `instant_actual_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `instant_actual_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `type` varchar(10) DEFAULT NULL COMMENT '1:移除,2置顶,3,勿扰',
  `up_time` datetime DEFAULT NULL COMMENT '更新时间',
  `user_id` varchar(100) DEFAULT NULL COMMENT '创建人',
  `actual_id` bigint(20) DEFAULT NULL COMMENT '会话id',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `type` (`type`,`user_id`,`actual_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='会话自定义';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `instant_actual_info`
--

LOCK TABLES `instant_actual_info` WRITE;
/*!40000 ALTER TABLE `instant_actual_info` DISABLE KEYS */;
/*!40000 ALTER TABLE `instant_actual_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `instant_file`
--

DROP TABLE IF EXISTS `instant_file`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `instant_file` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(255) DEFAULT NULL,
  `LAST_NAME` varchar(255) DEFAULT NULL,
  `FILE_PATH` varchar(255) DEFAULT NULL,
  `FILE_MIME` varchar(255) DEFAULT NULL,
  `FILE_OSS_ID` varchar(255) DEFAULT NULL,
  `FILE_SIZE` varchar(100) DEFAULT NULL,
  `CREATE_ID` varchar(255) DEFAULT NULL,
  `CREATE_DATE` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=MyISAM AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `instant_file`
--

LOCK TABLES `instant_file` WRITE;
/*!40000 ALTER TABLE `instant_file` DISABLE KEYS */;
/*!40000 ALTER TABLE `instant_file` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `instant_friend`
--

DROP TABLE IF EXISTS `instant_friend`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `instant_friend` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `NAME` varchar(100) DEFAULT NULL COMMENT '昵称',
  `CODE` varchar(100) DEFAULT NULL COMMENT '好友编码',
  `NOT_READ` int(11) DEFAULT NULL COMMENT '未读消息数',
  `ORG_CODE` varchar(100) DEFAULT NULL COMMENT '所属组织编码',
  `ORG_NAME` varchar(100) DEFAULT NULL COMMENT '所属组织名称',
  `ORG_ID` varchar(100) DEFAULT NULL COMMENT '所属组织ID',
  `CONVERSATION` tinyint(1) DEFAULT NULL COMMENT '是否正在会话',
  `SMALL_HEAD_IMAGE` varchar(255) DEFAULT NULL COMMENT '头像小图',
  `BIG_HEAD_IMAGE` varchar(255) DEFAULT NULL COMMENT '头像大图',
  `USER_ID` varchar(100) DEFAULT NULL COMMENT '用户ID',
  `USER_NAME` varchar(100) DEFAULT NULL COMMENT '用户名称',
  `ONLINE` tinyint(1) DEFAULT NULL COMMENT '是否在线',
  `PROHIBITION_SPEECH` tinyint(1) DEFAULT NULL COMMENT '是否禁言',
  `TYPE` varchar(20) DEFAULT NULL COMMENT '好友类型',
  `SYNC_TIME` datetime DEFAULT NULL COMMENT '同步时间',
  `LAST_TIME` datetime DEFAULT NULL COMMENT '最后会话时间',
  `IDENTITY` varchar(20) DEFAULT NULL COMMENT '好友身份',
  `DESCRIPTION` varchar(500) DEFAULT NULL COMMENT '好友说明',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '更新时间',
  `BAK_NAME` varchar(100) DEFAULT NULL COMMENT '备注名称',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=MyISAM AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='好友关系表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `instant_friend`
--

LOCK TABLES `instant_friend` WRITE;
/*!40000 ALTER TABLE `instant_friend` DISABLE KEYS */;
/*!40000 ALTER TABLE `instant_friend` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `instant_group`
--

DROP TABLE IF EXISTS `instant_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `instant_group` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `NAME` varchar(1000) DEFAULT NULL COMMENT '成员名称',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '加入时间',
  `CREATE_ID` varchar(255) DEFAULT NULL COMMENT '创建人',
  `TYPE` varchar(255) DEFAULT NULL COMMENT '类型0:普通群,1同事群(组织架构里面的id)',
  `ORG_ID` varchar(300) DEFAULT NULL COMMENT '组织架构id',
  `PHOTO` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=MyISAM AUTO_INCREMENT=1265 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='群组成员';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `instant_group`
--

LOCK TABLES `instant_group` WRITE;
/*!40000 ALTER TABLE `instant_group` DISABLE KEYS */;
/*!40000 ALTER TABLE `instant_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `instant_group_detail`
--

DROP TABLE IF EXISTS `instant_group_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `instant_group_detail` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `GROUP_ID` int(11) DEFAULT NULL COMMENT '组ID',
  `USER_ID` varchar(255) DEFAULT NULL COMMENT '用户ID',
  `MANAGER` varchar(255) DEFAULT NULL COMMENT '是否管理员0:创建人1:管理员2:普通',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '进群时间',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=MyISAM AUTO_INCREMENT=11555 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `instant_group_detail`
--

LOCK TABLES `instant_group_detail` WRITE;
/*!40000 ALTER TABLE `instant_group_detail` DISABLE KEYS */;
/*!40000 ALTER TABLE `instant_group_detail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `instant_his_actual`
--

DROP TABLE IF EXISTS `instant_his_actual`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `instant_his_actual` (
  `ID` bigint(20) NOT NULL COMMENT '主键ID',
  `NAME` varchar(100) DEFAULT NULL COMMENT '实际好友名称',
  `CODE` varchar(100) DEFAULT NULL COMMENT '实际好友编码',
  `TYPE` varchar(20) DEFAULT NULL COMMENT '好友类型',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  `FRIEND_ID` bigint(20) DEFAULT NULL COMMENT '好友主键',
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `FK_Reference_5` (`FRIEND_ID`) USING BTREE
) ENGINE=MyISAM DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='实际好友';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `instant_his_actual`
--

LOCK TABLES `instant_his_actual` WRITE;
/*!40000 ALTER TABLE `instant_his_actual` DISABLE KEYS */;
/*!40000 ALTER TABLE `instant_his_actual` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `instant_his_news`
--

DROP TABLE IF EXISTS `instant_his_news`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `instant_his_news` (
  `ID` bigint(20) NOT NULL COMMENT '主键ID',
  `SPEAKER_NAME` varchar(100) DEFAULT NULL COMMENT '发言人名称',
  `SPEAKER_CODE` varchar(100) DEFAULT NULL COMMENT '发言人编码',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  `CONTENT` varchar(200) DEFAULT NULL COMMENT '发言内容',
  `TYPE` varchar(20) DEFAULT NULL COMMENT '消息类型',
  `ACTUAL_ID` bigint(20) DEFAULT NULL COMMENT '实际好友ID',
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `FK_Reference_7` (`ACTUAL_ID`) USING BTREE
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='消息记录';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `instant_his_news`
--

LOCK TABLES `instant_his_news` WRITE;
/*!40000 ALTER TABLE `instant_his_news` DISABLE KEYS */;
/*!40000 ALTER TABLE `instant_his_news` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `instant_mobile_push`
--

DROP TABLE IF EXISTS `instant_mobile_push`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `instant_mobile_push` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `appId` varchar(100) DEFAULT NULL,
  `userIds` varchar(1000) DEFAULT NULL,
  `cIds` text,
  `title` varchar(255) DEFAULT NULL,
  `content` varchar(1000) DEFAULT NULL,
  `params` varchar(1000) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `instant_mobile_push`
--

LOCK TABLES `instant_mobile_push` WRITE;
/*!40000 ALTER TABLE `instant_mobile_push` DISABLE KEYS */;
/*!40000 ALTER TABLE `instant_mobile_push` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `instant_news`
--

DROP TABLE IF EXISTS `instant_news`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `instant_news` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `USER_NAME` varchar(100) DEFAULT NULL COMMENT '发言人名称',
  `USER_ID` varchar(100) DEFAULT NULL COMMENT '发言人编码',
  `CONTENT` text COMMENT '发言内容',
  `CONTENT_TYPE` varchar(20) DEFAULT NULL COMMENT '消息内容1:文本,2:图片.3:文件',
  `CREATE_TIME` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `ACTUAL_ID` bigint(20) DEFAULT NULL COMMENT '会话表',
  `FAST_LEVEL` int(5) DEFAULT NULL COMMENT '消息优先级',
  `NEW_STATUS` int(5) DEFAULT NULL COMMENT '消息状态0:未发送,1正在发送,2:已发送,3:已接收',
  `NEW_TYPE` varchar(10) DEFAULT NULL COMMENT '消息类型1:对话,2:推送,3聊天,4jeplus推送',
  `RECEIVE_TIME` timestamp NULL DEFAULT NULL COMMENT '接收时间',
  `SEND_USER_ID` varchar(100) DEFAULT NULL COMMENT '发送用户id',
  `FILE_NAME` varchar(255) DEFAULT NULL COMMENT '文件名',
  `MSG_UID` varchar(100) DEFAULT NULL COMMENT 'msg的uuid',
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `FK_Reference_6` (`ACTUAL_ID`) USING BTREE
) ENGINE=MyISAM AUTO_INCREMENT=39884 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='消息记录';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `instant_news`
--

LOCK TABLES `instant_news` WRITE;
/*!40000 ALTER TABLE `instant_news` DISABLE KEYS */;
/*!40000 ALTER TABLE `instant_news` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `instant_news_detail`
--

DROP TABLE IF EXISTS `instant_news_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `instant_news_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `news_id` bigint(20) DEFAULT NULL COMMENT '消息id',
  `user_id` varchar(100) DEFAULT NULL COMMENT '用户id',
  `status` varchar(10) DEFAULT NULL COMMENT 'T:已读,F:未读,S:正在发送中',
  `receive_time` datetime DEFAULT NULL COMMENT '接收时间',
  `actual_id` bigint(20) DEFAULT NULL COMMENT '会话id',
  `num` int(11) DEFAULT NULL COMMENT '未读数量',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `user_id` (`user_id`,`actual_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `instant_news_detail`
--

LOCK TABLES `instant_news_detail` WRITE;
/*!40000 ALTER TABLE `instant_news_detail` DISABLE KEYS */;
/*!40000 ALTER TABLE `instant_news_detail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `instant_push_news`
--

DROP TABLE IF EXISTS `instant_push_news`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `instant_push_news` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `USER_NAME` varchar(100) DEFAULT NULL COMMENT '发言人名称',
  `USER_ID` varchar(100) DEFAULT NULL COMMENT '发言人编码',
  `CONTENT` text COMMENT '发言内容',
  `CONTENT_TYPE` varchar(20) DEFAULT NULL COMMENT '消息内容1:文本,2:图片.3:文件',
  `CREATE_TIME` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `ACTUAL_ID` bigint(20) DEFAULT NULL COMMENT '会话表',
  `FAST_LEVEL` int(5) DEFAULT NULL COMMENT '消息优先级',
  `NEW_STATUS` int(5) DEFAULT NULL COMMENT '消息状态0:未发送,1正在发送,2:已发送,3:已接收',
  `NEW_TYPE` varchar(10) DEFAULT NULL COMMENT '消息类型1:对话,2:推送,3聊天,4jeplus推送',
  `RECEIVE_TIME` timestamp NULL DEFAULT NULL COMMENT '接收时间',
  `SEND_USER_ID` varchar(100) DEFAULT NULL COMMENT '发送用户id',
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `FK_Reference_6` (`ACTUAL_ID`) USING BTREE
) ENGINE=MyISAM AUTO_INCREMENT=6561 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='消息记录';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `instant_push_news`
--

LOCK TABLES `instant_push_news` WRITE;
/*!40000 ALTER TABLE `instant_push_news` DISABLE KEYS */;
/*!40000 ALTER TABLE `instant_push_news` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `instant_push_news_detail`
--

DROP TABLE IF EXISTS `instant_push_news_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `instant_push_news_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `news_id` bigint(20) DEFAULT NULL COMMENT '消息id',
  `user_id` varchar(100) DEFAULT NULL COMMENT '用户id',
  `status` varchar(10) DEFAULT NULL COMMENT 'T:已读,F:未读,S:正在发送中',
  `receive_time` datetime DEFAULT NULL COMMENT '接收时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `news_id` (`news_id`) USING BTREE,
  KEY `user_id` (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `instant_push_news_detail`
--

LOCK TABLES `instant_push_news_detail` WRITE;
/*!40000 ALTER TABLE `instant_push_news_detail` DISABLE KEYS */;
/*!40000 ALTER TABLE `instant_push_news_detail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `instant_thirdapp_config`
--

DROP TABLE IF EXISTS `instant_thirdapp_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `instant_thirdapp_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `app_id` varchar(200) DEFAULT NULL COMMENT 'appid',
  `app_name` varchar(200) DEFAULT NULL COMMENT 'app名称',
  `third_type` varchar(10) DEFAULT NULL COMMENT '1:默认个推',
  `third_app_id` varchar(200) DEFAULT NULL COMMENT '第三方app_id',
  `third_app_key` varchar(100) DEFAULT NULL COMMENT '第三方app_key',
  `third_app_secret` varchar(200) DEFAULT NULL COMMENT '第三方app_secret',
  `third_app_mast_secret` varchar(200) DEFAULT NULL COMMENT '第三方mast_secret',
  `third_app_host` varchar(200) DEFAULT NULL COMMENT '第三方推送的地址',
  `default_value` varchar(10) DEFAULT NULL COMMENT '默认值1:是默认值',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `third_app_key` (`third_app_key`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `instant_thirdapp_config`
--

LOCK TABLES `instant_thirdapp_config` WRITE;
/*!40000 ALTER TABLE `instant_thirdapp_config` DISABLE KEYS */;
/*!40000 ALTER TABLE `instant_thirdapp_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `instant_thirdapp_user`
--

DROP TABLE IF EXISTS `instant_thirdapp_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `instant_thirdapp_user` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `GTPUSH_TYPE` varchar(50) DEFAULT NULL,
  `GTPUSH_TOKEN` varchar(100) DEFAULT NULL,
  `GTPUSH_CID` varchar(100) DEFAULT NULL,
  `DETAIL_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `instant_thirdapp_user`
--

LOCK TABLES `instant_thirdapp_user` WRITE;
/*!40000 ALTER TABLE `instant_thirdapp_user` DISABLE KEYS */;
/*!40000 ALTER TABLE `instant_thirdapp_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `instant_thirdapp_user_detail`
--

DROP TABLE IF EXISTS `instant_thirdapp_user_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `instant_thirdapp_user_detail` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `GTYH_APKID` varchar(50) DEFAULT NULL,
  `GTYH_APKMC` varchar(50) DEFAULT NULL,
  `GTYH_APKKEY` varchar(50) DEFAULT NULL,
  `GTYH_USERID` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE,
  UNIQUE KEY `GTYH_APKKEY` (`GTYH_APKKEY`,`GTYH_USERID`) USING BTREE,
  KEY `GTYH_USERID` (`GTYH_USERID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `instant_thirdapp_user_detail`
--

LOCK TABLES `instant_thirdapp_user_detail` WRITE;
/*!40000 ALTER TABLE `instant_thirdapp_user_detail` DISABLE KEYS */;
/*!40000 ALTER TABLE `instant_thirdapp_user_detail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `instant_user`
--

DROP TABLE IF EXISTS `instant_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `instant_user` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `USER_NAME` varchar(255) DEFAULT NULL,
  `PASSWORD` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT COMMENT='租户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `instant_user`
--

LOCK TABLES `instant_user` WRITE;
/*!40000 ALTER TABLE `instant_user` DISABLE KEYS */;
INSERT INTO `instant_user` VALUES (1,'jeplus','123456');
/*!40000 ALTER TABLE `instant_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `instant_user_group`
--

DROP TABLE IF EXISTS `instant_user_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `instant_user_group` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `USER_ID` bigint(20) DEFAULT NULL COMMENT '租户id',
  `GROUP_ID` varchar(255) DEFAULT NULL COMMENT '租户自己传入的组id',
  `GROUP_NAME` varchar(255) DEFAULT NULL COMMENT '组名称',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT COMMENT='租户组';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `instant_user_group`
--

LOCK TABLES `instant_user_group` WRITE;
/*!40000 ALTER TABLE `instant_user_group` DISABLE KEYS */;
INSERT INTO `instant_user_group` VALUES (6,1,'SYSTEM',NULL,'2019-01-24 16:03:30','2019-01-24 16:03:30');
/*!40000 ALTER TABLE `instant_user_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `instant_user_group_detail`
--

DROP TABLE IF EXISTS `instant_user_group_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `instant_user_group_detail` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `GROUP_ID` bigint(20) DEFAULT NULL COMMENT '组id',
  `USER_ID` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '用户id(租户自己传入的用户)',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='租户组详细信息';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `instant_user_group_detail`
--

LOCK TABLES `instant_user_group_detail` WRITE;
/*!40000 ALTER TABLE `instant_user_group_detail` DISABLE KEYS */;
/*!40000 ALTER TABLE `instant_user_group_detail` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-07-23  9:56:45
