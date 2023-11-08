-- MariaDB dump 10.19  Distrib 10.10.3-MariaDB, for Win64 (AMD64)
--
-- Host: localhost    Database: gravitygather
-- ------------------------------------------------------
-- Server version	10.10.3-MariaDB

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
(13,'qwe','id',1,0,'1ed324507d63ec8f25d00e7bd18f5cc23364382832c9e651bbc10d6da756ab2d','06dc1a91228edb3d0393977eb0d9f527',5,1,1,'2023-11-08 13:37:33'),
(14,'qwe','id',1,0,'4a4bf01ff4100e1c3438125b47d13622adbf152cf91c38b6830d17ac0908e89e','fce8942bbf14f0a8a6c33c77193d4aec',5,0,2,'2023-11-08 13:37:56'),
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
(2,'test','test',NULL,'https://s3.orbi.kr/data/file/united2/a67cee2e6d7a465fbf3bc425f85a02b0.jpg','최한솔','귀여미',NULL,'ACTIVE',0,'2023-10-29 03:22:10','2023-10-29 03:22:10');
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

-- Dump completed on 2023-11-08 23:21:35
