-- MariaDB dump 10.19-11.1.2-MariaDB, for osx10.19 (arm64)
--
-- Host: localhost    Database: gravitygather
-- ------------------------------------------------------
-- Server version	11.1.2-MariaDB

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
-- Table structure for table `chatlog`
--

DROP TABLE IF EXISTS `chatlog`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `chatlog` (
  `seq` int(11) NOT NULL AUTO_INCREMENT,
  `type` varchar(255) NOT NULL,
  `room_seq` int(11) NOT NULL,
  `msg` text NOT NULL,
  `sender_seq` int(11) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`seq`)
) ENGINE=InnoDB AUTO_INCREMENT=123 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chatlog`
--

LOCK TABLES `chatlog` WRITE;
/*!40000 ALTER TABLE `chatlog` DISABLE KEYS */;
INSERT INTO `chatlog` VALUES
(1,'enter',14,'티이모님이 입장하셨습니다.',1,'2023-11-09 12:03:09'),
(2,'leave',14,'티이모님이 방을 떠났습니다.',1,'2023-11-09 12:03:19'),
(3,'enter',19,'티이모님이 입장하셨습니다.',1,'2023-11-09 12:03:33'),
(4,'chat',19,'테스트1',1,'2023-11-09 12:04:08'),
(5,'chat',19,'ㅎㅇㅎㄴ',1,'2023-11-09 12:04:09'),
(6,'chat',19,'하가핸ㄹ',1,'2023-11-09 12:04:10'),
(7,'chat',19,'ㅏㄱ라',1,'2023-11-09 12:04:11'),
(8,'chat',19,'램ㄹ',1,'2023-11-09 12:04:11'),
(9,'chat',19,'ㅇ',1,'2023-11-09 12:04:11'),
(10,'chat',19,'ㅁㄴ',1,'2023-11-09 12:04:12'),
(11,'chat',19,'ㅇ',1,'2023-11-09 12:04:12'),
(12,'chat',19,'ㄹㄴ',1,'2023-11-09 12:04:13'),
(13,'chat',19,'ㄹ',1,'2023-11-09 12:04:13'),
(14,'chat',19,'ㅁㄴㅇ',1,'2023-11-09 12:04:13'),
(15,'chat',19,'ㄹ',1,'2023-11-09 12:04:13'),
(16,'chat',19,'ㄴㄹ',1,'2023-11-09 12:04:13'),
(17,'chat',19,'ㄴ',1,'2023-11-09 12:04:14'),
(18,'chat',19,'ㄴㅇㄹ',1,'2023-11-09 12:04:14'),
(19,'leave',19,'티이모님이 방을 떠났습니다.',1,'2023-11-09 12:04:48'),
(20,'enter',19,'티이모님이 입장하셨습니다.',1,'2023-11-09 12:04:53'),
(21,'enter',19,'귀여미님이 입장하셨습니다.',2,'2023-11-09 12:05:09'),
(22,'chat',19,'ㅎㅇ 반가워요',1,'2023-11-09 12:05:23'),
(23,'chat',19,'반갑습니다.',2,'2023-11-09 12:05:28'),
(24,'chat',19,'ㅎㅎ..',2,'2023-11-09 12:05:29'),
(25,'leave',19,'귀여미님이 방을 떠났습니다.',2,'2023-11-09 12:06:23'),
(26,'leave',19,'티이모님이 방을 떠났습니다.',1,'2023-11-09 12:06:23'),
(27,'enter',19,'귀여미님이 입장하셨습니다.',2,'2023-11-09 12:06:23'),
(28,'enter',19,'티이모님이 입장하셨습니다.',1,'2023-11-09 12:06:23'),
(29,'leave',19,'귀여미님이 방을 떠났습니다.',2,'2023-11-09 12:06:25'),
(30,'leave',19,'티이모님이 방을 떠났습니다.',1,'2023-11-09 12:06:25'),
(31,'enter',19,'티이모님이 입장하셨습니다.',1,'2023-11-09 12:06:38'),
(32,'enter',19,'귀여미님이 입장하셨습니다.',2,'2023-11-09 12:06:47'),
(33,'chat',19,'ㅎㅎ',1,'2023-11-09 12:06:50'),
(34,'chat',19,'ㅎㅎ',2,'2023-11-09 12:06:53'),
(35,'chat',19,'ㅎㅎ',1,'2023-11-09 12:06:55'),
(36,'chat',19,'ㅎㅎ',1,'2023-11-09 12:06:55'),
(37,'chat',19,'ㅎㅎ',1,'2023-11-09 12:06:55'),
(38,'chat',19,'ㅎㅎ',2,'2023-11-09 12:06:57'),
(39,'chat',19,'ㅎㅎ',2,'2023-11-09 12:06:57'),
(40,'chat',19,'ㅎ',2,'2023-11-09 12:06:57'),
(41,'enter',19,'귀여미님이 입장하셨습니다.',2,'2023-11-09 12:07:25'),
(42,'leave',19,'귀여미님이 방을 떠났습니다.',2,'2023-11-09 12:07:25'),
(43,'leave',19,'티이모님이 방을 떠났습니다.',1,'2023-11-09 12:07:25'),
(44,'enter',19,'티이모님이 입장하셨습니다.',1,'2023-11-09 12:07:25'),
(45,'chat',19,'gd',2,'2023-11-09 12:07:26'),
(46,'chat',19,'gd',2,'2023-11-09 12:07:27'),
(47,'chat',19,'gd',2,'2023-11-09 12:07:27'),
(48,'leave',19,'티이모님이 방을 떠났습니다.',1,'2023-11-09 12:08:26'),
(49,'leave',19,'귀여미님이 방을 떠났습니다.',2,'2023-11-09 12:08:26'),
(50,'enter',19,'티이모님이 입장하셨습니다.',1,'2023-11-09 12:08:26'),
(51,'enter',19,'귀여미님이 입장하셨습니다.',2,'2023-11-09 12:08:26'),
(52,'leave',19,'티이모님이 방을 떠났습니다.',1,'2023-11-09 12:08:56'),
(53,'leave',19,'귀여미님이 방을 떠났습니다.',2,'2023-11-09 12:08:57'),
(54,'enter',14,'티이모님이 입장하셨습니다.',1,'2023-11-09 12:09:06'),
(55,'enter',14,'귀여미님이 입장하셨습니다.',2,'2023-11-09 12:10:48'),
(56,'leave',14,'귀여미님이 방을 떠났습니다.',2,'2023-11-09 12:10:59'),
(57,'enter',14,'티이모님이 입장하셨습니다.',1,'2023-11-09 12:11:04'),
(58,'leave',14,'티이모님이 방을 떠났습니다.',1,'2023-11-09 12:11:09'),
(59,'leave',14,'티이모님이 방을 떠났습니다.',1,'2023-11-09 12:11:55'),
(60,'enter',14,'귀여미님이 입장하셨습니다.',2,'2023-11-09 12:11:55'),
(61,'enter',14,'티이모님이 입장하셨습니다.',1,'2023-11-09 12:11:55'),
(62,'leave',14,'티이모님이 방을 떠났습니다.',1,'2023-11-09 12:12:00'),
(63,'enter',14,'티이모님이 입장하셨습니다.',1,'2023-11-09 12:12:00'),
(64,'leave',14,'귀여미님이 방을 떠났습니다.',2,'2023-11-09 12:12:00'),
(65,'leave',14,'티이모님이 방을 떠났습니다.',1,'2023-11-09 12:18:09'),
(66,'enter',14,'귀여미님이 입장하셨습니다.',2,'2023-11-09 12:18:30'),
(67,'enter',14,'티이모님이 입장하셨습니다.',1,'2023-11-09 12:18:33'),
(68,'chat',14,'asd',2,'2023-11-09 12:19:36'),
(69,'leave',14,'귀여미님이 방을 떠났습니다.',2,'2023-11-09 12:19:37'),
(70,'enter',19,'귀여미님이 입장하셨습니다.',2,'2023-11-09 12:19:44'),
(71,'chat',19,'asd',2,'2023-11-09 12:19:45'),
(72,'chat',19,'qwe',2,'2023-11-09 12:19:46'),
(73,'leave',14,'티이모님이 방을 떠났습니다.',1,'2023-11-09 12:19:48'),
(74,'enter',19,'티이모님이 입장하셨습니다.',1,'2023-11-09 12:19:52'),
(75,'chat',19,'asd',1,'2023-11-09 12:19:54'),
(76,'chat',19,'xc',1,'2023-11-09 12:19:55'),
(77,'chat',19,'qwe',2,'2023-11-09 12:20:00'),
(78,'leave',19,'티이모님이 방을 떠났습니다.',1,'2023-11-09 12:20:33'),
(79,'enter',19,'티이모님이 입장하셨습니다.',1,'2023-11-09 12:20:33'),
(80,'leave',19,'귀여미님이 방을 떠났습니다.',2,'2023-11-09 12:20:33'),
(81,'enter',19,'귀여미님이 입장하셨습니다.',2,'2023-11-09 12:20:33'),
(82,'leave',19,'티이모님이 방을 떠났습니다.',1,'2023-11-09 12:20:38'),
(83,'leave',19,'귀여미님이 방을 떠났습니다.',2,'2023-11-09 12:20:38'),
(84,'enter',19,'귀여미님이 입장하셨습니다.',2,'2023-11-09 12:20:38'),
(85,'enter',19,'티이모님이 입장하셨습니다.',1,'2023-11-09 12:20:38'),
(86,'leave',19,'귀여미님이 방을 떠났습니다.',2,'2023-11-09 12:21:06'),
(87,'enter',19,'귀여미님이 입장하셨습니다.',2,'2023-11-09 12:21:06'),
(88,'leave',19,'티이모님이 방을 떠났습니다.',1,'2023-11-09 12:21:06'),
(89,'enter',19,'티이모님이 입장하셨습니다.',1,'2023-11-09 12:21:06'),
(90,'leave',19,'귀여미님이 방을 떠났습니다.',2,'2023-11-09 12:21:14'),
(91,'enter',19,'귀여미님이 입장하셨습니다.',2,'2023-11-09 12:21:14'),
(92,'leave',19,'티이모님이 방을 떠났습니다.',1,'2023-11-09 12:21:14'),
(93,'leave',19,'귀여미님이 방을 떠났습니다.',2,'2023-11-09 12:21:14'),
(94,'enter',19,'티이모님이 입장하셨습니다.',1,'2023-11-09 12:21:14'),
(95,'enter',19,'귀여미님이 입장하셨습니다.',2,'2023-11-09 12:21:26'),
(96,'leave',19,'귀여미님이 방을 떠났습니다.',2,'2023-11-09 12:21:54'),
(97,'enter',19,'귀여미님이 입장하셨습니다.',2,'2023-11-09 12:21:54'),
(98,'leave',19,'티이모님이 방을 떠났습니다.',1,'2023-11-09 12:21:54'),
(99,'enter',19,'티이모님이 입장하셨습니다.',1,'2023-11-09 12:21:54'),
(100,'chat',19,'qwe',2,'2023-11-09 12:22:03'),
(101,'chat',19,'as',2,'2023-11-09 12:22:03'),
(102,'chat',19,'gg',1,'2023-11-09 12:22:06'),
(103,'leave',19,'티이모님이 방을 떠났습니다.',1,'2023-11-09 12:22:10'),
(104,'enter',19,'티이모님이 입장하셨습니다.',1,'2023-11-09 12:22:14'),
(105,'leave',19,'귀여미님이 방을 떠났습니다.',2,'2023-11-09 12:22:18'),
(106,'leave',19,'티이모님이 방을 떠났습니다.',1,'2023-11-09 12:22:36'),
(107,'enter',14,'티이모님이 입장하셨습니다.',1,'2023-11-09 12:22:40'),
(108,'chat',14,'ㅂㅈㄷ',1,'2023-11-09 12:23:37'),
(109,'chat',14,'ㅁㄴㅇ',1,'2023-11-09 12:23:38'),
(110,'chat',14,'ㅊㅌ',1,'2023-11-09 12:23:38'),
(111,'leave',14,'티이모님이 방을 떠났습니다.',1,'2023-11-09 12:24:46'),
(112,'enter',14,'티이모님이 입장하셨습니다.',1,'2023-11-09 12:24:46'),
(113,'leave',14,'티이모님이 방을 떠났습니다.',1,'2023-11-09 12:24:49'),
(114,'enter',14,'티이모님이 입장하셨습니다.',1,'2023-11-09 12:24:49'),
(115,'enter',14,'귀여미님이 입장하셨습니다.',2,'2023-11-09 12:25:09'),
(116,'chat',14,'ㅎㅎ',1,'2023-11-09 12:25:11'),
(117,'chat',14,'ㅎ',2,'2023-11-09 12:25:13'),
(118,'chat',14,'ㅈㄷㄱ',2,'2023-11-09 12:25:14'),
(119,'chat',14,'ㅈㄷ',2,'2023-11-09 12:25:14'),
(120,'chat',14,'ㄴㅇㄹ',2,'2023-11-09 12:25:15'),
(121,'chat',14,'ㅎㅎ',2,'2023-11-09 12:25:35'),
(122,'chat',14,'ㅎㅎ',2,'2023-11-09 12:26:03');
/*!40000 ALTER TABLE `chatlog` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `failedloginattempt`
--

DROP TABLE IF EXISTS `failedloginattempt`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `failedloginattempt` (
  `seq` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `status` enum('SUCCESS','FAIL','ERROR') DEFAULT NULL,
  `timestamp` timestamp NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`seq`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `failedloginattempts_FK` FOREIGN KEY (`seq`) REFERENCES `users` (`seq`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='로그인_실패_횟수';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `failedloginattempt`
--

LOCK TABLES `failedloginattempt` WRITE;
/*!40000 ALTER TABLE `failedloginattempt` DISABLE KEYS */;
/*!40000 ALTER TABLE `failedloginattempt` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rooms`
--

DROP TABLE IF EXISTS `rooms`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rooms` (
  `seq` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) DEFAULT NULL,
  `topic` varchar(255) DEFAULT NULL,
  `is_locked` tinyint(1) DEFAULT NULL,
  `is_delete` tinyint(1) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `password_salt` varchar(255) DEFAULT NULL,
  `max_participant` int(11) DEFAULT NULL,
  `current_participant` int(11) DEFAULT NULL,
  `owner_seq` int(11) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`seq`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rooms`
--

LOCK TABLES `rooms` WRITE;
/*!40000 ALTER TABLE `rooms` DISABLE KEYS */;
INSERT INTO `rooms` VALUES
(1,'테스트입니다','토픽입니다',1,0,'ㅁㄴㅇ',NULL,5,0,3,'2023-10-29 01:50:43'),
(2,'ㅁㅁㅁ','ㄴㄴㄴㄴ',0,0,'ㅁㄴㅇ',NULL,100,0,3,'2023-10-29 01:50:43'),
(3,'귀요미 모임','귀여미만 모여라 ㅡㅡ',0,0,'ㅁㄴㅇ',NULL,1,0,3,'2023-10-29 01:50:43'),
(4,'gd','gd',0,0,NULL,NULL,5,0,3,'2023-11-06 08:04:22'),
(5,'gd123','gd123',1,0,'123',NULL,1,0,3,'2023-11-06 08:04:40'),
(6,'헬로','이것봐',1,0,'1c4a1d90bb5d40e4367f18c437adf9acdab4daf3abf981cdc171bc765131a26b',NULL,2,0,3,'2023-11-08 13:15:07'),
(7,'gg','id',1,0,'4a1d3f3a4c75cfa25c91e7db87bd99a474c22118b7bb9381cd58715c5b66f451','77ac8363b176e9cb3e697adfeac6ad3f',4,0,3,'2023-11-08 13:21:05'),
(8,'ㅂㅂㅂㅂㅂ','ㅂㅂㅂㅂ',0,0,NULL,NULL,5,0,3,'2023-11-08 13:22:09'),
(9,'id','id',1,0,'7e8b684b64dc8ffd4dc79ef5225b2bdb50c2eee2874fa3233f96b749cd320788','aa09f0f3b72103fcf0a8cea83630acf6',5,0,3,'2023-11-08 13:27:34'),
(10,'gg','id',1,0,'e251b0728c201173879e4517fe61550da565fb17eaa0e5c0642fa5f474b8f2eb','88fdc02b68f87bee81bc984dca8b38cb',5,0,3,'2023-11-08 13:34:48'),
(11,'q','id',1,0,'61c0a77ee07ea988e4e8f131bf71185232c53926beb7548dc3666886143a7156','fd7da990001607ffebdff5c0fb6f1627',5,0,1,'2023-11-08 13:35:37'),
(12,'qwe','id',1,0,'daede91131c745e5d6aa8f5eafec7fffcb901bcc6dd3e37e03b89245ae78e9bc','e9a652ebaa0e467af934c92ca66f4ef0',5,0,1,'2023-11-08 13:36:53'),
(13,'qwe','id',1,0,'1ed324507d63ec8f25d00e7bd18f5cc23364382832c9e651bbc10d6da756ab2d','06dc1a91228edb3d0393977eb0d9f527',5,0,1,'2023-11-08 13:37:33'),
(14,'qwe','id',1,0,'4a4bf01ff4100e1c3438125b47d13622adbf152cf91c38b6830d17ac0908e89e','fce8942bbf14f0a8a6c33c77193d4aec',5,2,2,'2023-11-08 13:37:56'),
(15,'qwe','id',1,0,'0bdee6405bf9e3788b4fb17a9b76ef6ac7063d7e719b3253eb9451615e803c71','911926f6d3849fdba098e6cfc9eeb0e3',5,0,3,'2023-11-08 13:40:16'),
(16,'gd','id',1,0,'013393f36c4c90ec8f38c0953eb8ecf87076f146264e2c8bbef2e419b816ffc4','5a4fc148097a6577a3eb64dee4c43a43',5,0,3,'2023-11-08 13:41:17'),
(17,'asd','qwe',0,0,NULL,NULL,5,0,3,'2023-11-08 13:43:15'),
(18,'zxc','id',1,0,'bbbc7ad47f74d3e616a35fcc125ab6c13d17c8ed03873e72ba17c8ec71f16cdc','cf23118b2ddfb3dec1706261d5aaae1f',5,0,3,'2023-11-08 13:44:21'),
(19,'g','id',1,0,'de74c8dad05145f779c4e280fe01fcce6bbaccc73f5b1a648257660c201f5b17','377819e808a7520e39c2773a3fd16b2e',5,0,2,'2023-11-08 14:17:01');
/*!40000 ALTER TABLE `rooms` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `seq` int(11) NOT NULL,
  `id` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `password_salt` varchar(255) DEFAULT NULL,
  `photo` varchar(200) DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL,
  `nickname` varchar(100) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `status` enum('ACTIVE','UNVERIFIED','DELETED','INACTIVE','LOCK') DEFAULT 'UNVERIFIED',
  `fail_login_cnt` int(11) DEFAULT 0,
  `created_at` timestamp NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`seq`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='사용자_정보';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES
(1,'id','qwe',NULL,'https://cso-studio-kr.dn.nexoncdn.co.kr/images/01511608942513017001.vmg','안녕','티이모','park_wj7269@naver.com','ACTIVE',0,'2023-10-21 07:22:14','2023-10-21 07:22:14'),
(2,'test','test',NULL,NULL,'ㅎㅎㅎ','ㅎㅎ',NULL,'ACTIVE',0,'2023-10-29 03:22:10','2023-10-29 03:22:10');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'gravitygather'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-11-09 21:27:57
