-- MySQL dump 10.13  Distrib 8.0.37, for Linux (x86_64)
--
-- Host: localhost    Database: uniroom
-- ------------------------------------------------------
-- Server version	8.0.37

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `branch`
--

DROP TABLE IF EXISTS `branch`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `branch` (
  `id_branch` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `id_corporate` int NOT NULL,
  `id_user` int NOT NULL,
  `active` tinyint(1) NOT NULL DEFAULT '1',
  `cnpj` char(14) NOT NULL,
  PRIMARY KEY (`id_branch`),
  UNIQUE KEY `branch_unique` (`cnpj`),
  KEY `branch_corporate_FK` (`id_corporate`),
  KEY `branch_user_FK` (`id_user`),
  CONSTRAINT `branch_corporate_FK` FOREIGN KEY (`id_corporate`) REFERENCES `corporate` (`id_corporate`) ON DELETE SET DEFAULT ON UPDATE CASCADE,
  CONSTRAINT `branch_user_FK` FOREIGN KEY (`id_user`) REFERENCES `user` (`id_user`) ON DELETE SET DEFAULT ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `branch`
--

LOCK TABLES `branch` WRITE;
/*!40000 ALTER TABLE `branch` DISABLE KEYS */;
INSERT INTO `branch` VALUES (27,'Branch Brasil',3,44,1,'54248026000182'),(28,'Branch da Uniball',4,42,1,'80050657000100');
/*!40000 ALTER TABLE `branch` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `corporate`
--

DROP TABLE IF EXISTS `corporate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `corporate` (
  `id_corporate` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `cnpj` char(14) NOT NULL,
  `id_user` int NOT NULL,
  `active` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id_corporate`),
  UNIQUE KEY `corporate_unique` (`cnpj`),
  KEY `corporate_user_FK` (`id_user`),
  CONSTRAINT `corporate_user_FK` FOREIGN KEY (`id_user`) REFERENCES `user` (`id_user`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `corporate`
--

LOCK TABLES `corporate` WRITE;
/*!40000 ALTER TABLE `corporate` DISABLE KEYS */;
INSERT INTO `corporate` VALUES (3,'UniRoom','24786224000139',42,1),(4,'UniBall','80050657000100',46,1);
/*!40000 ALTER TABLE `corporate` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `guest`
--

DROP TABLE IF EXISTS `guest`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `guest` (
  `id_guest` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `cpf` char(11) NOT NULL,
  `id_room` int DEFAULT NULL,
  `hosted` tinyint(1) NOT NULL DEFAULT '0',
  `id_branch` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`id_guest`),
  UNIQUE KEY `guest_unique` (`name`),
  UNIQUE KEY `guest_unique_1` (`cpf`),
  KEY `guest_room_id_room_fk` (`id_room`),
  KEY `guest_branch_id_branch_fk` (`id_branch`),
  CONSTRAINT `guest_branch_id_branch_fk` FOREIGN KEY (`id_branch`) REFERENCES `branch` (`id_branch`),
  CONSTRAINT `guest_room_id_room_fk` FOREIGN KEY (`id_room`) REFERENCES `room` (`id_room`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `guest`
--

LOCK TABLES `guest` WRITE;
/*!40000 ALTER TABLE `guest` DISABLE KEYS */;
INSERT INTO `guest` VALUES (10,'Teste','03735655939',3,1,27),(13,'Teste da Silva','11490333983',3,1,27);
/*!40000 ALTER TABLE `guest` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inventory`
--

DROP TABLE IF EXISTS `inventory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `inventory` (
  `id_inventory` int NOT NULL AUTO_INCREMENT,
  `id_room` int NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `active` tinyint(1) NOT NULL DEFAULT '1',
  `id_branch` int NOT NULL,
  PRIMARY KEY (`id_inventory`),
  KEY `inventory_room_FK` (`id_room`),
  KEY `inventory_branch_FK` (`id_branch`),
  CONSTRAINT `inventory_branch_FK` FOREIGN KEY (`id_branch`) REFERENCES `branch` (`id_branch`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `inventory_room_FK` FOREIGN KEY (`id_room`) REFERENCES `room` (`id_room`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inventory`
--

LOCK TABLES `inventory` WRITE;
/*!40000 ALTER TABLE `inventory` DISABLE KEYS */;
INSERT INTO `inventory` VALUES (3,3,'Inventário novos',1,27),(4,3,'Inventário que não deve aparecer',1,28);
/*!40000 ALTER TABLE `inventory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inventory_item`
--

DROP TABLE IF EXISTS `inventory_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `inventory_item` (
  `id_inventory` int NOT NULL,
  `id_item` int NOT NULL,
  `quantity` int NOT NULL,
  UNIQUE KEY `inventory_item_id_inventory_IDX` (`id_inventory`,`id_item`) USING BTREE,
  KEY `inventory_item_item_FK` (`id_item`),
  CONSTRAINT `inventory_item_inventory_FK` FOREIGN KEY (`id_inventory`) REFERENCES `inventory` (`id_inventory`),
  CONSTRAINT `inventory_item_item_FK` FOREIGN KEY (`id_item`) REFERENCES `item` (`id_item`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inventory_item`
--

LOCK TABLES `inventory_item` WRITE;
/*!40000 ALTER TABLE `inventory_item` DISABLE KEYS */;
INSERT INTO `inventory_item` VALUES (3,4,9),(3,5,3);
/*!40000 ALTER TABLE `inventory_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `item`
--

DROP TABLE IF EXISTS `item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `item` (
  `id_item` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `price` float NOT NULL,
  `id_branch` int NOT NULL,
  `active` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id_item`),
  KEY `item_branch_FK` (`id_branch`),
  CONSTRAINT `item_branch_FK` FOREIGN KEY (`id_branch`) REFERENCES `branch` (`id_branch`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `item`
--

LOCK TABLES `item` WRITE;
/*!40000 ALTER TABLE `item` DISABLE KEYS */;
INSERT INTO `item` VALUES (4,'Coca-Colo',10,28,1),(5,'Pepsi',10.9,27,1);
/*!40000 ALTER TABLE `item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reservation`
--

DROP TABLE IF EXISTS `reservation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reservation` (
  `id_reservation` int NOT NULL AUTO_INCREMENT,
  `id_room` int NOT NULL,
  `id_user` int NOT NULL,
  `status` varchar(2) NOT NULL DEFAULT 'CI',
  `id_branch` int NOT NULL,
  `date_time_check_in` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `date_time_check_out` timestamp NULL DEFAULT NULL,
  `initial_date` date NOT NULL DEFAULT (curdate()),
  `final_date` date NOT NULL,
  PRIMARY KEY (`id_reservation`),
  KEY `reservation_room_FK` (`id_room`),
  KEY `reservation_user_FK` (`id_user`),
  KEY `reservation_branch_FK` (`id_branch`),
  CONSTRAINT `reservation_branch_FK` FOREIGN KEY (`id_branch`) REFERENCES `branch` (`id_branch`),
  CONSTRAINT `reservation_room_FK` FOREIGN KEY (`id_room`) REFERENCES `room` (`id_room`),
  CONSTRAINT `reservation_user_FK` FOREIGN KEY (`id_user`) REFERENCES `user` (`id_user`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reservation`
--

LOCK TABLES `reservation` WRITE;
/*!40000 ALTER TABLE `reservation` DISABLE KEYS */;
/*!40000 ALTER TABLE `reservation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `room`
--

DROP TABLE IF EXISTS `room`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `room` (
  `id_room` int NOT NULL AUTO_INCREMENT,
  `room_number` int NOT NULL,
  `id_room_type` int NOT NULL,
  `id_branch` int NOT NULL,
  `active` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id_room`),
  KEY `room_room_type_FK` (`id_room_type`),
  KEY `room_branch_FK` (`id_branch`),
  CONSTRAINT `room_branch_FK` FOREIGN KEY (`id_branch`) REFERENCES `branch` (`id_branch`),
  CONSTRAINT `room_room_type_FK` FOREIGN KEY (`id_room_type`) REFERENCES `room_type` (`id_room_type`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `room`
--

LOCK TABLES `room` WRITE;
/*!40000 ALTER TABLE `room` DISABLE KEYS */;
INSERT INTO `room` VALUES (3,1,2,27,1),(4,100000000,2,27,1);
/*!40000 ALTER TABLE `room` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `room_type`
--

DROP TABLE IF EXISTS `room_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `room_type` (
  `id_room_type` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `capacity` tinyint NOT NULL,
  `price` float NOT NULL,
  `id_branch` int NOT NULL,
  `active` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id_room_type`),
  KEY `roomt_type_branch_FK` (`id_branch`),
  CONSTRAINT `roomt_type_branch_FK` FOREIGN KEY (`id_branch`) REFERENCES `branch` (`id_branch`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `room_type`
--

LOCK TABLES `room_type` WRITE;
/*!40000 ALTER TABLE `room_type` DISABLE KEYS */;
INSERT INTO `room_type` VALUES (2,'Suíte',2,150,27,1),(4,'Simples',2,82,27,1),(5,'Luxo',2,200,27,1);
/*!40000 ALTER TABLE `room_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id_user` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `username` varchar(20) NOT NULL,
  `password` text NOT NULL,
  `role` char(1) NOT NULL DEFAULT 'E',
  `active` tinyint(1) NOT NULL DEFAULT '1',
  `secret_phrase` text NOT NULL,
  `secret_answer` text NOT NULL,
  `id_branch` int DEFAULT NULL,
  `id_corporate` int DEFAULT NULL,
  PRIMARY KEY (`id_user`),
  UNIQUE KEY `user_unique` (`username`),
  KEY `user_branch_id_branch_fk` (`id_branch`),
  CONSTRAINT `user_branch_id_branch_fk` FOREIGN KEY (`id_branch`) REFERENCES `branch` (`id_branch`)
) ENGINE=InnoDB AUTO_INCREMENT=49 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (42,'Corporate Manager da Silva','corporate','$2a$10$w99hujjmGxs5xe5r8qcCDu/i31ZYl8bMTmP08loNq1yiNZ8iRO2bi','C',1,'Qual é o nome do seu filme favorito?','olobodewallstreet',NULL,NULL),(43,'Employee dos Santos','employee','$2a$10$0GaEQuq0BLw.8EktxVmW7.JXBe4gejZ397Lxm18BtY34W23nRHsNm','A',1,'Qual é o nome do seu animal de estimação?','pingo',27,NULL),(44,'Branch Manager da Silva','branch','$2a$10$0GaEQuq0BLw.8EktxVmW7.JXBe4gejZ397Lxm18BtY34W23nRHsNm','B',1,'Sei lá?','pnis',NULL,NULL),(46,'Administrator','admin','$2a$10$Sw5NdkbxmHSEOrAtFhGe9u0rGC4RM33NTUrZOwFtsU3w4uku5Z/aS','A',1,'Why is my cock hard?','becauseyes',NULL,NULL),(47,'Teste','teste','$2a$10$1ZzpSiKuZef1ALpwAE9YkerXrPrO7S4OMGxCS2fyzrmpbtJ7ZwA1u','C',1,'Qual é o nome do seu animal de estimação?','teste',27,NULL),(48,'Helena Negrelle Ramos','eunaopeidoflor23','$2a$10$tA3.cm/LB1VVMeK2nnt8huF3vdKeNhMuJGQuUjX1yIwkehRIFf3Iq','C',1,'Qual é o nome do seu filme favorito?','enolaholmes',27,NULL);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'uniroom'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-06-12 17:39:16
