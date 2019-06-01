CREATE DATABASE  IF NOT EXISTS `plotfinder` /*!40100 DEFAULT CHARACTER SET utf8mb4  DEFAULT COLLATE utf8mb4_unicode_ci */;
USE `plotfinder`;
-- MySQL dump 10.16  Distrib 10.1.37-MariaDB, for Win32 (AMD64)
--
-- Host: 127.0.0.1    Database: plotfinder
-- ------------------------------------------------------
-- Server version	10.1.37-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `flags`
--

DROP TABLE IF EXISTS `flags`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `flags` (
  `flag_id` int(11) NOT NULL AUTO_INCREMENT,
  `plot_id` int(11) NOT NULL,
  `flag` varchar(10) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`flag_id`),
  UNIQUE KEY `flag_id` (`flag_id`),
  KEY `CountryIndex` (`plot_id`,`flag`),
  CONSTRAINT `fk_plot_foreign_key` FOREIGN KEY (`plot_id`) REFERENCES `plot` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=45;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `flags`
--

LOCK TABLES `flags` WRITE;
/*!40000 ALTER TABLE `flags` DISABLE KEYS */;
INSERT INTO `flags` (`flag_id`, `plot_id`, `flag`) VALUES (22,10,'garage'),(25,10,'house'),(20,10,'power'),(21,10,'sale'),(23,10,'sewer'),(24,10,'water'),(35,13,'forest'),(36,13,'rent'),(43,15,'forest'),(44,15,'parking'),(42,15,'rent');
/*!40000 ALTER TABLE `flags` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `plot`
--

DROP TABLE IF EXISTS `plot`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `plot` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `polygon` varchar(150) NOT NULL,
  `user_id` int(11) NOT NULL,
  `title` varchar(45) NOT NULL,
  `description` text NOT NULL,
  `address1` varchar(45) NOT NULL,
  `address2` varchar(45) DEFAULT NULL,
  `district` varchar(45) DEFAULT NULL,
  `city` varchar(45) DEFAULT NULL,
  `country` varchar(45) NOT NULL,
  `price` int(11) NOT NULL,
  `currency` varchar(3) NOT NULL,
  `size` int(3) NOT NULL,
  `added` date NOT NULL,
  `expires` date NOT NULL,
  `size_unit` varchar(3) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `my_foreign_key_user_idx` (`user_id`),
  KEY `PriceIndex` (`price`),
  KEY `SizeIndex` (`size`),
  KEY `CityIndex` (`city`),
  KEY `DistrictIndex` (`district`),
  CONSTRAINT `my_foreign_key_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=16;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `plot`
--

LOCK TABLES `plot` WRITE;
/*!40000 ALTER TABLE `plot` DISABLE KEYS */;
INSERT INTO `plot` (`id`, `polygon`, `user_id`, `title`, `description`, `address1`, `address2`, `district`, `city`, `country`, `price`, `currency`, `size`, `added`, `expires`, `size_unit`) VALUES (10,'41.883907#-85.30149@41.638058#-85.31248@41.613422#-84.99387@41.843#-84.94993@',1,'tttt','rwtrtsf','hdgdgsh',NULL,NULL,'hdhrgdh','hdfgf',12556754,'EUR',42,'2019-02-23','2019-04-02','m2'),(13,'42.69014#-85.83543@42.374405#-85.91233@42.38252#-85.47288@42.66591#-85.56077@',1,'Usvojite me !!!','yyhsetg','dsdfgds',NULL,NULL,'tgghdfg','Serbia',364,'EUR',2100,'2019-04-18','2019-05-18','Ar'),(15,'42.360603#-86.10789@42.21431#-86.12986@42.222446#-85.91013@42.352486#-85.91013@',1,'Latest plot','fhdfgdfg','asdfsd',NULL,'gdsgsdfg','gdfsghsdfg','Srbija',364,'EUR',4300,'2019-04-30','2019-05-30','Ar');
/*!40000 ALTER TABLE `plot` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `roles` (
  `role_id` int(11) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(45) NOT NULL,
  PRIMARY KEY (`role_id`),
  UNIQUE KEY `idroles_UNIQUE` (`role_id`),
  UNIQUE KEY `role_name_UNIQUE` (`role_name`)
) ENGINE=InnoDB AUTO_INCREMENT=4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` (`role_id`, `role_name`) VALUES (2,'ROLE_ADMIN'),(3,'ROLE_SUPERADMIN'),(1,'ROLE_USER');
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(45) NOT NULL,
  `password` varchar(100) NOT NULL,
  `first_name` varchar(45) NOT NULL,
  `last_name` varchar(45) NOT NULL,
  `email` varchar(100) NOT NULL,
  `phone1` varchar(15) NOT NULL,
  `phone2` varchar(15) DEFAULT NULL,
  `active` tinyint(1) NOT NULL,
  `not_locked` tinyint(1) NOT NULL,
  `reg_date` datetime NOT NULL,
  `last_login` datetime NOT NULL,
  `last_password_change` datetime NOT NULL,
  `last_update` datetime NOT NULL,
  `identifier` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `user_id_UNIQUE` (`user_id`),
  UNIQUE KEY `username_UNIQUE` (`username`),
  UNIQUE KEY `identifier` (`identifier`)
) ENGINE=InnoDB AUTO_INCREMENT=5;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` (`user_id`, `username`, `password`, `first_name`, `last_name`, `email`, `phone1`, `phone2`, `active`, `not_locked`, `reg_date`, `last_login`, `last_password_change`, `last_update`, `identifier`) VALUES (1,'Lazaruss','$2a$10$AjG7dmyZbFW8mIcHVCoJAet62tmMvygjatoT0dsGZInPAiiKc4XK6','Bojan','Milojkovic','lord_lazaruss@yahoo.com','0603771245',NULL,1,1,'2019-02-04 22:26:08','2019-05-17 21:20:57','2019-02-04 22:26:08','2019-02-04 22:26:08',NULL),(3,'Kale01','$2a$10$S8.heFA539GoDdf/aVtsRezwdidrATzUZ2kWTOczOwO8pq/Y4iAF2','Davor','Milojkovic','lord_lazaruss@gmail.com','0605718733',NULL,1,1,'2019-03-10 13:43:29','2019-03-10 13:43:29','2019-03-10 13:43:29','2019-03-17 11:59:22',NULL),(4,'kanlic','$2a$10$0uKzUSjpgqy02OBvbovJ7eu0syZ2edUSnwN/p25h4WBXxNpzdRKha','Dusan','Kanlic','lord.lazaruss@gmail.com','0603771247',NULL,1,1,'2019-04-24 20:27:00','2019-04-24 20:27:00','2019-04-24 20:27:00','2019-04-24 20:27:00','f7c3efea-5d09-4f63-b27c-a0a0ad1849c7');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_has_roles`
--

DROP TABLE IF EXISTS `user_has_roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_has_roles` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `role_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `fk_user_idx` (`user_id`),
  KEY `fk_roles_idx` (`role_id`),
  CONSTRAINT `fk_roles` FOREIGN KEY (`role_id`) REFERENCES `roles` (`role_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=6;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_has_roles`
--

LOCK TABLES `user_has_roles` WRITE;
/*!40000 ALTER TABLE `user_has_roles` DISABLE KEYS */;
INSERT INTO `user_has_roles` (`id`, `user_id`, `role_id`) VALUES (1,1,1),(2,1,2),(3,1,3),(4,3,1),(5,4,1);
/*!40000 ALTER TABLE `user_has_roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `watched_area`
--

DROP TABLE IF EXISTS `watched_area`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `watched_area` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ll_x` decimal(9,6) NOT NULL,
  `ll_y` decimal(9,6) NOT NULL,
  `ur_x` decimal(9,6) NOT NULL,
  `ur_y` decimal(9,6) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `plot_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `ll_x_index` (`ll_x`),
  KEY `ll_y_index` (`ll_y`),
  KEY `ur_x_index` (`ur_x`),
  KEY `ur_y_index` (`ur_y`),
  KEY `user_table_fk` (`user_id`),
  KEY `plot_table_fk` (`plot_id`),
  CONSTRAINT `plot_table_fk` FOREIGN KEY (`plot_id`) REFERENCES `plot` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `user_table_fk` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `watched_area`
--

LOCK TABLES `watched_area` WRITE;
/*!40000 ALTER TABLE `watched_area` DISABLE KEYS */;
INSERT INTO `watched_area` (`id`, `ll_x`, `ll_y`, `ur_x`, `ur_y`, `user_id`, `plot_id`) VALUES (1,-92.234110,31.242740,-72.234110,46.900265,1,NULL),(2,-85.312480,41.613422,-84.949930,41.883907,NULL,10),(5,-85.912330,42.374405,-85.472880,42.690140,NULL,13),(7,-86.129860,42.214310,-85.910130,42.360603,NULL,15);
/*!40000 ALTER TABLE `watched_area` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-05-29 20:11:15
