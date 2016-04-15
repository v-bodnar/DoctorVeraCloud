-- MySQL dump 10.13  Distrib 5.7.9, for Win64 (x86_64)
--
-- Host: localhost    Database: drvera
-- ------------------------------------------------------
-- Server version	5.7.10-log

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
-- Table structure for table `address`
--

DROP TABLE IF EXISTS `address`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `address` (
  `AddressId` int(11) NOT NULL AUTO_INCREMENT,
  `Address` varchar(255) DEFAULT NULL,
  `City` varchar(255) DEFAULT NULL,
  `Country` varchar(255) DEFAULT NULL,
  `DateCreated` datetime NOT NULL,
  `Deleted` bit(1) NOT NULL,
  `PostIndex` int(11) DEFAULT NULL,
  `Region` varchar(255) DEFAULT NULL,
  `UserCreated` int(11) NOT NULL,
  PRIMARY KEY (`AddressId`),
  KEY `FK_461f5i84wjmt660hciiihqdnc` (`UserCreated`),
  CONSTRAINT `FK_461f5i84wjmt660hciiihqdnc` FOREIGN KEY (`UserCreated`) REFERENCES `users` (`UserId`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `address`
--

LOCK TABLES `address` WRITE;
/*!40000 ALTER TABLE `address` DISABLE KEYS */;
INSERT INTO `address` VALUES (1,'Клименко 10/17','Киев','Украина','2016-02-04 10:43:46',0x00,69096,'Киевская область',1);
/*!40000 ALTER TABLE `address` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `deliverygroup`
--

DROP TABLE IF EXISTS `deliverygroup`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `deliverygroup` (
  `DeliveryGroupId` int(11) NOT NULL AUTO_INCREMENT,
  `DateCreated` datetime NOT NULL,
  `Deleted` bit(1) NOT NULL,
  `Description` varchar(255) DEFAULT NULL,
  `Name` varchar(255) NOT NULL,
  `UserCreated` int(11) NOT NULL,
  PRIMARY KEY (`DeliveryGroupId`),
  KEY `FK_ixxdims2cfsh25docsl9697o` (`UserCreated`),
  CONSTRAINT `FK_ixxdims2cfsh25docsl9697o` FOREIGN KEY (`UserCreated`) REFERENCES `users` (`UserId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `deliverygroup`
--

LOCK TABLES `deliverygroup` WRITE;
/*!40000 ALTER TABLE `deliverygroup` DISABLE KEYS */;
/*!40000 ALTER TABLE `deliverygroup` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `deliverygrouphasusergroups`
--

DROP TABLE IF EXISTS `deliverygrouphasusergroups`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `deliverygrouphasusergroups` (
  `DeliveryGroup` int(11) NOT NULL,
  `UserGroup` int(11) NOT NULL,
  KEY `FK_dhvi9rralr4g55etq7g9hbtsj` (`UserGroup`),
  KEY `FK_ndtoxl447lo6vo94n7emvhtuo` (`DeliveryGroup`),
  CONSTRAINT `FK_dhvi9rralr4g55etq7g9hbtsj` FOREIGN KEY (`UserGroup`) REFERENCES `usergroups` (`UserGroupId`),
  CONSTRAINT `FK_ndtoxl447lo6vo94n7emvhtuo` FOREIGN KEY (`DeliveryGroup`) REFERENCES `deliverygroup` (`DeliveryGroupId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `deliverygrouphasusergroups`
--

LOCK TABLES `deliverygrouphasusergroups` WRITE;
/*!40000 ALTER TABLE `deliverygrouphasusergroups` DISABLE KEYS */;
/*!40000 ALTER TABLE `deliverygrouphasusergroups` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `deliverygrouphasusers`
--

DROP TABLE IF EXISTS `deliverygrouphasusers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `deliverygrouphasusers` (
  `DeliveryGroup` int(11) NOT NULL,
  `User` int(11) NOT NULL,
  KEY `FK_bcx0muwomlhdxsevvkhvj4whj` (`User`),
  KEY `FK_hg0hmncakgfjy2mknrp28ti54` (`DeliveryGroup`),
  CONSTRAINT `FK_bcx0muwomlhdxsevvkhvj4whj` FOREIGN KEY (`User`) REFERENCES `users` (`UserId`),
  CONSTRAINT `FK_hg0hmncakgfjy2mknrp28ti54` FOREIGN KEY (`DeliveryGroup`) REFERENCES `deliverygroup` (`DeliveryGroupId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `deliverygrouphasusers`
--

LOCK TABLES `deliverygrouphasusers` WRITE;
/*!40000 ALTER TABLE `deliverygrouphasusers` DISABLE KEYS */;
/*!40000 ALTER TABLE `deliverygrouphasusers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `doctorshasmethod`
--

DROP TABLE IF EXISTS `doctorshasmethod`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `doctorshasmethod` (
  `Doctor` int(11) NOT NULL,
  `Method` int(11) NOT NULL,
  KEY `FK_59eapy8p1ldvyx0y5s8tng439` (`Method`),
  KEY `FK_qwfnwwqoepoyld0ln715bbqc2` (`Doctor`),
  CONSTRAINT `FK_59eapy8p1ldvyx0y5s8tng439` FOREIGN KEY (`Method`) REFERENCES `methods` (`MethodId`),
  CONSTRAINT `FK_qwfnwwqoepoyld0ln715bbqc2` FOREIGN KEY (`Doctor`) REFERENCES `users` (`UserId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `doctorshasmethod`
--

LOCK TABLES `doctorshasmethod` WRITE;
/*!40000 ALTER TABLE `doctorshasmethod` DISABLE KEYS */;
/*!40000 ALTER TABLE `doctorshasmethod` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `filerepository`
--

DROP TABLE IF EXISTS `filerepository`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `filerepository` (
  `FileRepositoryId` int(11) NOT NULL AUTO_INCREMENT,
  `DateCreated` datetime NOT NULL,
  `Deleted` bit(1) NOT NULL,
  `Extension` int(11) NOT NULL,
  `File` longblob NOT NULL,
  `FileName` varchar(255) NOT NULL,
  `Size` bigint(20) NOT NULL,
  `UserCreated` int(11) NOT NULL,
  PRIMARY KEY (`FileRepositoryId`),
  KEY `FK_7jpdm5nod6hwnp3ypjuugroyo` (`UserCreated`),
  CONSTRAINT `FK_7jpdm5nod6hwnp3ypjuugroyo` FOREIGN KEY (`UserCreated`) REFERENCES `users` (`UserId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `filerepository`
--

LOCK TABLES `filerepository` WRITE;
/*!40000 ALTER TABLE `filerepository` DISABLE KEYS */;
/*!40000 ALTER TABLE `filerepository` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `locale`
--

DROP TABLE IF EXISTS `locale`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `locale` (
  `LocaleId` int(11) NOT NULL AUTO_INCREMENT,
  `CountryCode` varchar(255) DEFAULT NULL,
  `DateCreated` datetime NOT NULL,
  `Deleted` bit(1) NOT NULL,
  `Language` varchar(255) DEFAULT NULL,
  `LanguageCode` varchar(255) DEFAULT NULL,
  `LanguageNative` varchar(255) DEFAULT NULL,
  `UserCreated` int(11) NOT NULL,
  PRIMARY KEY (`LocaleId`),
  KEY `FK_dmuaa2iudujxs00ym7mwend81` (`UserCreated`),
  CONSTRAINT `FK_dmuaa2iudujxs00ym7mwend81` FOREIGN KEY (`UserCreated`) REFERENCES `users` (`UserId`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `locale`
--

LOCK TABLES `locale` WRITE;
/*!40000 ALTER TABLE `locale` DISABLE KEYS */;
INSERT INTO `locale` VALUES (1,'RU','2016-02-04 10:53:37',0x00,'Russian','ru','Русский',1),(2,'En','2016-02-04 10:53:37',0x00,'English','en','English',1),(3,'Pl','2016-02-10 18:05:24',0x00,'Polish','pl','Polski',1);
/*!40000 ALTER TABLE `locale` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `messagebundle`
--

DROP TABLE IF EXISTS `messagebundle`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `messagebundle` (
  `MessageBundleId` int(11) NOT NULL AUTO_INCREMENT,
  `DateCreated` datetime NOT NULL,
  `Deleted` bit(1) NOT NULL,
  `MessageKey` varchar(255) NOT NULL,
  `Type` int(11) NOT NULL,
  `Value` varchar(255) NOT NULL,
  `Locale` int(11) DEFAULT NULL,
  `UserCreated` int(11) NOT NULL,
  PRIMARY KEY (`MessageBundleId`),
  KEY `FK_13o1xo3wxkcesgntohjvkur2r` (`Locale`),
  KEY `FK_pqfyii7r6kgrrcoe8cx7uyoww` (`UserCreated`),
  CONSTRAINT `FK_13o1xo3wxkcesgntohjvkur2r` FOREIGN KEY (`Locale`) REFERENCES `locale` (`LocaleId`),
  CONSTRAINT `FK_pqfyii7r6kgrrcoe8cx7uyoww` FOREIGN KEY (`UserCreated`) REFERENCES `users` (`UserId`)
) ENGINE=InnoDB AUTO_INCREMENT=830 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `messagebundle`
--

LOCK TABLES `messagebundle` WRITE;
/*!40000 ALTER TABLE `messagebundle` DISABLE KEYS */;
INSERT INTO `messagebundle` VALUES (2,'2016-01-23 14:46:09',0x00,'ADDRESS_ADDRESS',0,'Адрес',1,1),(3,'2016-01-23 14:46:09',0x00,'ADDRESS_CITY',0,'Город',1,1),(4,'2016-01-23 14:46:09',0x00,'ADDRESS_COUNTRY',0,'Страна',1,1),(5,'2016-01-23 14:46:09',0x00,'ADDRESS_INDEX',0,'Индекс',1,1),(6,'2016-01-23 14:46:09',0x00,'ADDRESS_PLACEHOLDER_ADDRESS',0,'Улица, дом, квартира',1,1),(7,'2016-01-23 14:46:09',0x00,'ADDRESS_PLACEHOLDER_CITY',0,'Киев',1,1),(8,'2016-01-23 14:46:09',0x00,'ADDRESS_PLACEHOLDER_COUNTRY',0,'Украина',1,1),(9,'2016-01-23 14:46:09',0x00,'ADDRESS_PLACEHOLDER_INDEX',0,'69096',1,1),(10,'2016-01-23 14:46:09',0x00,'ADDRESS_PLACEHOLDER_REGION',0,'Киевская область',1,1),(11,'2016-01-23 14:46:09',0x00,'ADDRESS_REGION',0,'Область',1,1),(12,'2016-01-23 14:46:09',0x00,'ADD_USER_BUTTON_ADD',0,'Добавить пользователя',1,1),(13,'2016-01-23 14:46:09',0x00,'ADD_USER_FORM_LEGEND_ADDRESS',0,'Адрес',1,1),(14,'2016-01-23 14:46:09',0x00,'ADD_USER_FORM_LEGEND_DETAILS',0,'Данные о пользователе',1,1),(15,'2016-01-23 14:46:09',0x00,'ADD_USER_TITLE',0,'Добавить пользователя',1,1),(16,'2016-01-23 14:46:09',0x00,'ADMIN_BLOCK_HEADER',0,'Администрирование',1,1),(17,'2016-01-23 14:46:09',0x00,'APPLICATION_CURRENCY',0,'грн',1,1),(18,'2016-01-23 14:46:09',0x00,'APPLICATION_ERROR_TITLE',0,'Ошибка!',1,1),(19,'2016-01-23 14:46:09',0x00,'APPLICATION_MINUTES_SHORT',0,'мин.',1,1),(20,'2016-01-23 14:46:09',0x00,'APPLICATION_NO',0,'Нет',1,1),(21,'2016-01-23 14:46:09',0x00,'APPLICATION_NOT_IMPLEMENTED',0,'Еще не имплементировано',1,1),(22,'2016-01-23 14:46:09',0x00,'APPLICATION_PERMISSION_DENIED',0,'В доступе отказанно. Обратитесь к менеджеру системы',1,1),(23,'2016-01-23 14:46:09',0x00,'APPLICATION_SAVED',0,'Сохранено!',1,1),(24,'2016-01-23 14:46:09',0x00,'APPLICATION_SELECT_ONE',0,'Выберите',1,1),(25,'2016-01-23 14:46:09',0x00,'APPLICATION_SUCCESSFUL_TITLE',0,'Успешно!',1,1),(26,'2016-01-23 14:46:09',0x00,'APPLICATION_TITLE',0,'Doctor Vera Cloud Storage',1,1),(27,'2016-01-23 14:46:09',0x00,'APPLICATION_YES',0,'Да',1,1),(28,'2016-01-23 14:46:09',0x00,'CALENDAR_FRIDAY',0,'Пятница',1,1),(29,'2016-01-23 14:46:09',0x00,'CALENDAR_MONDAY',0,'Понедельник',1,1),(30,'2016-01-23 14:46:09',0x00,'CALENDAR_MONTH_1',0,'Январь',1,1),(31,'2016-01-23 14:46:09',0x00,'CALENDAR_MONTH_10',0,'Октябрь',1,1),(32,'2016-01-23 14:46:09',0x00,'CALENDAR_MONTH_11',0,'Ноябрь',1,1),(33,'2016-01-23 14:46:09',0x00,'CALENDAR_MONTH_12',0,'Декабрь',1,1),(34,'2016-01-23 14:46:09',0x00,'CALENDAR_MONTH_2',0,'Февраль',1,1),(35,'2016-01-23 14:46:09',0x00,'CALENDAR_MONTH_3',0,'Март',1,1),(36,'2016-01-23 14:46:09',0x00,'CALENDAR_MONTH_4',0,'Апрель',1,1),(37,'2016-01-23 14:46:09',0x00,'CALENDAR_MONTH_5',0,'Май',1,1),(38,'2016-01-23 14:46:09',0x00,'CALENDAR_MONTH_6',0,'Июнь',1,1),(39,'2016-01-23 14:46:09',0x00,'CALENDAR_MONTH_7',0,'Июль',1,1),(40,'2016-01-23 14:46:09',0x00,'CALENDAR_MONTH_8',0,'Август',1,1),(41,'2016-01-23 14:46:09',0x00,'CALENDAR_MONTH_9',0,'Сентябрь',1,1),(42,'2016-01-23 14:46:09',0x00,'CALENDAR_SATURDAY',0,'Суббота',1,1),(43,'2016-01-23 14:46:09',0x00,'CALENDAR_SUNDAY',0,'Воскресенье',1,1),(44,'2016-01-23 14:46:09',0x00,'CALENDAR_THURSDAY',0,'Четверг',1,1),(45,'2016-01-23 14:46:09',0x00,'CALENDAR_TUESDAY',0,'Вторник',1,1),(46,'2016-01-23 14:46:09',0x00,'CALENDAR_WEDNESDAY',0,'Среда',1,1),(47,'2016-01-23 14:46:09',0x00,'DELIVERY_GROUPS_ADD_BUTTON',0,'Добавить',1,1),(48,'2016-01-23 14:46:09',0x00,'DELIVERY_GROUPS_ADD_DIALOG_TITLE',0,'Создание группы для рассылок',1,1),(49,'2016-01-23 14:46:09',0x00,'DELIVERY_GROUPS_ADD_SUCCESS_END',0,'добавлены в группу',1,1),(50,'2016-01-23 14:46:09',0x00,'DELIVERY_GROUPS_ADD_SUCCESS_START',0,'Пользователли:',1,1),(51,'2016-01-23 14:46:09',0x00,'DELIVERY_GROUPS_ADD_USERS',0,'Добавить пользователей!',1,1),(52,'2016-01-23 14:46:09',0x00,'DELIVERY_GROUPS_ADD_USERS_SUCCESS_END',0,'добавлены для группы доставки',1,1),(53,'2016-01-23 14:46:09',0x00,'DELIVERY_GROUPS_ADD_USERS_SUCCESS_START',0,'Пользователи:',1,1),(54,'2016-01-23 14:46:09',0x00,'DELIVERY_GROUPS_ADD_USER_GROUPS',0,'Добавить группу пользователей!',1,1),(55,'2016-01-23 14:46:09',0x00,'DELIVERY_GROUPS_ADD_USER_GROUP_SUCCESS_END',0,'добавлены для группы доставки',1,1),(56,'2016-01-23 14:46:09',0x00,'DELIVERY_GROUPS_ADD_USER_GROUP_SUCCESS_START',0,'Группы пользователей:',1,1),(57,'2016-01-23 14:46:09',0x00,'DELIVERY_GROUPS_DELETED',0,'Группа удалена',1,1),(58,'2016-01-23 14:46:09',0x00,'DELIVERY_GROUPS_DELETE_BUTTON',0,'Удалить',1,1),(59,'2016-01-23 14:46:09',0x00,'DELIVERY_GROUPS_DELETE_CONFIRM_MESSAGE',0,'Вы действительно хотите удалить группу?',1,1),(60,'2016-01-23 14:46:09',0x00,'DELIVERY_GROUPS_DELETE_CONFIRM_NO',0,'Нет',1,1),(61,'2016-01-23 14:46:09',0x00,'DELIVERY_GROUPS_DELETE_CONFIRM_TITLE',0,'Внимание!',1,1),(62,'2016-01-23 14:46:09',0x00,'DELIVERY_GROUPS_DELETE_CONFIRM_YES',0,'Да',1,1),(63,'2016-01-23 14:46:09',0x00,'DELIVERY_GROUPS_EDITED',0,'Изменения внесены!',1,1),(64,'2016-01-23 14:46:09',0x00,'DELIVERY_GROUPS_EDIT_BUTTON',0,'Изменить группу',1,1),(65,'2016-01-23 14:46:09',0x00,'DELIVERY_GROUPS_EDIT_DIALOG_TITLE',0,'Изменение группы для рассылок',1,1),(66,'2016-01-23 14:46:09',0x00,'DELIVERY_GROUPS_REMOVE_SUCCESS_END',0,'удалены из группы: ',1,1),(67,'2016-01-23 14:46:09',0x00,'DELIVERY_GROUPS_REMOVE_SUCCESS_START',0,'Группы пользователей: ',1,1),(68,'2016-01-23 14:46:09',0x00,'DELIVERY_GROUPS_REMOVE_USERS_SUCCESS_END',0,'удалены для группы доставки: ',1,1),(69,'2016-01-23 14:46:09',0x00,'DELIVERY_GROUPS_REMOVE_USERS_SUCCESS_START',0,'Пользователи: ',1,1),(70,'2016-01-23 14:46:09',0x00,'DELIVERY_GROUPS_REMOVE_USER_GROUP_SUCCESS_END',0,'удалены для группы доставки: ',1,1),(71,'2016-01-23 14:46:09',0x00,'DELIVERY_GROUPS_SAVED',0,'Новая группа создана!',1,1),(73,'2016-01-23 14:46:09',0x00,'DELIVERY_GROUPS_TITLE',0,'Группы пользователей для рассылок',1,1),(74,'2016-01-23 14:46:09',0x00,'DELIVERY_GROUPS_USERS_PICKER_SOURCE',0,'Все пользователи',1,1),(75,'2016-01-23 14:46:09',0x00,'DELIVERY_GROUPS_USERS_PICKER_TITLE',0,'Добавление пользователей в группу',1,1),(76,'2016-01-23 14:46:09',0x00,'DELIVERY_GROUPS_USER_GROUP_PICKER_SOURCE',0,'Все права доступа',1,1),(77,'2016-01-23 14:46:09',0x00,'DELIVERY_GROUPS_USER_GROUP_PICKER_TITLE',0,'Добавление прав доступа для группы',1,1),(78,'2016-01-23 14:46:09',0x00,'EMAIL_TEMPLATES_TITLE',0,'EMAIL Шаблоны',1,1),(79,'2016-01-23 14:46:09',0x00,'ENTITY_DATE_CREATED',0,'Дата создания',1,1),(80,'2016-01-23 14:46:09',0x00,'ENTITY_DESCRIPTION',0,'Описание',1,1),(81,'2016-01-23 14:46:09',0x00,'ENTITY_ID',0,'№',1,1),(82,'2016-01-23 14:46:09',0x00,'ENTITY_NAME',0,'Название',1,1),(83,'2016-01-23 14:46:09',0x00,'ENTITY_USER_CREATED',0,'Создал',1,1),(84,'2016-01-23 14:46:09',0x00,'ERROR_BACK_TO_MAIN',0,'На главную',1,1),(85,'2016-01-23 14:46:09',0x00,'FINANCIAL_BLOCK_HEADER',0,'Финансы',1,1),(86,'2016-01-23 14:46:09',0x00,'INDEX_APPOINTMENTS_COUNT',0,'Количество приемов',1,1),(87,'2016-01-23 14:46:09',0x00,'INDEX_AVERAGE_APPOINTMENTS_PER_DAY',0,'Среднее количество приемов в день',1,1),(88,'2016-01-23 14:46:09',0x00,'INDEX_AVERAGE_APPOINTMENTS_PER_MONTH',0,'Среднее количество приемов в месяц',1,1),(89,'2016-01-23 14:46:09',0x00,'INDEX_AVERAGE_DAY_SALARY',0,'Средний зароботок в день',1,1),(90,'2016-01-23 14:46:09',0x00,'INDEX_AVERAGE_MONTH_SALARY',0,'Средний зароботок в месяц',1,1),(91,'2016-01-23 14:46:09',0x00,'INDEX_BEST_DAY',0,'Лучший день',1,1),(92,'2016-01-23 14:46:09',0x00,'INDEX_BEST_MONTH',0,'Лучший месяц',1,1),(93,'2016-01-23 14:46:09',0x00,'INDEX_DAY_OF_MONTH',0,'Дни',1,1),(94,'2016-01-23 14:46:09',0x00,'INDEX_MONTH_OF_YEAR',0,'Месяцы',1,1),(95,'2016-01-23 14:46:09',0x00,'INDEX_MONTH_SALARY',0,'Заработано за месяц',1,1),(96,'2016-01-23 14:46:09',0x00,'INDEX_MONTH_SALARY_CUR',0,'Заработано за месяц, грн',1,1),(97,'2016-01-23 14:46:09',0x00,'INDEX_MONTH_STATISTICS',0,'Статистика за месяц',1,1),(98,'2016-01-23 14:46:09',0x00,'INDEX_TOTAL_APPOINTMENTS',0,'Всего проведено приемов',1,1),(99,'2016-01-23 14:46:09',0x00,'INDEX_TOTAL_MONTH_WORK_DAYS',0,'Всего работали в этом месяце дней',1,1),(100,'2016-01-23 14:46:09',0x00,'INDEX_TOTAL_YEAR_WORK_DAYS',0,'Всего работали в этом году дней',1,1),(101,'2016-01-23 14:46:09',0x00,'INDEX_WORST_DAY',0,'Худший день',1,1),(102,'2016-01-23 14:46:09',0x00,'INDEX_WORST_MONTH',0,'Худший месяц',1,1),(103,'2016-01-23 14:46:09',0x00,'INDEX_YEAR_SALARY',0,'Заработано за год',1,1),(104,'2016-01-23 14:46:09',0x00,'INDEX_YEAR_SALARY_CUR',0,'Заработано за год, грн',1,1),(105,'2016-01-23 14:46:09',0x00,'INDEX_YEAR_STATISTICS',0,'Статистика за год',1,1),(106,'2016-01-23 14:46:09',0x00,'LOGIN_ERROR',0,'Неправильный логин или пароль',1,1),(107,'2016-01-23 14:46:09',0x00,'LOGIN_ERROR_TITLE',0,'Ошибка!',1,1),(108,'2016-01-23 14:46:09',0x00,'LOGIN_GOODBY',0,'Успешно!',1,1),(109,'2016-01-23 14:46:09',0x00,'LOGIN_GOODBY_TITLE',0,'Выход',1,1),(110,'2016-01-23 14:46:09',0x00,'LOGIN_LOGIN_BUTTON',0,'Вход',1,1),(111,'2016-01-23 14:46:09',0x00,'LOGIN_LOGOUT_BUTTON',0,'Выход',1,1),(112,'2016-01-23 14:46:09',0x00,'LOGIN_LOG_AS_OTHER_USER',0,'Зайти под другим пользователем',1,1),(113,'2016-01-23 14:46:09',0x00,'LOGIN_REGISTER',0,'Регистрация',1,1),(114,'2016-01-23 14:46:09',0x00,'LOGIN_TITLE',0,'Вход',1,1),(115,'2016-01-23 14:46:09',0x00,'LOGIN_USER_PROFILE',0,'Профиль',1,1),(116,'2016-01-23 14:46:09',0x00,'LOGIN_WELCOME',0,'Добро пожаловать, ',1,1),(117,'2016-01-23 14:46:09',0x00,'LOGIN_WELCOME_TITLE',0,'Параметры верны!',1,1),(118,'2016-01-23 14:46:09',0x00,'MANAGER_BLOCK_HEADER',0,'Управление',1,1),(119,'2016-01-23 14:46:09',0x00,'MENU_HEADER',0,'Главное меню',1,1),(120,'2016-01-23 14:46:09',0x00,'MENU_HEADER_DELIVERY',0,'Система доставки сообщений',1,1),(121,'2016-01-23 14:46:09',0x00,'MENU_HEADER_FINANCE',0,'Финансы',1,1),(122,'2016-01-23 14:46:09',0x00,'MENU_HEADER_MANAGER',0,'Управление',1,1),(123,'2016-01-23 14:46:09',0x00,'MENU_HEADER_SCHEDULE',0,'Расписание',1,1),(124,'2016-01-23 14:46:09',0x00,'MENU_ITEM_ACCESS_RIGHTS',0,'Права доступа',1,1),(125,'2016-01-23 14:46:09',0x00,'MENU_ITEM_ADD_PLAN',0,'Запланировать прием',1,1),(126,'2016-01-23 14:46:09',0x00,'MENU_ITEM_ADD_SCHEDULE',0,'Добавить прием',1,1),(127,'2016-01-23 14:46:09',0x00,'MENU_ITEM_ADD_USER',0,'Добавить пользователя',1,1),(128,'2016-01-23 14:46:09',0x00,'MENU_ITEM_APPOINTMENTS',0,'Статистика приемов',1,1),(129,'2016-01-23 14:46:09',0x00,'MENU_ITEM_CASH',0,'Касса',1,1),(130,'2016-01-23 14:46:09',0x00,'MENU_ITEM_CREATE_PAYMENT',0,'Создать платеж',1,1),(131,'2016-01-23 14:46:09',0x00,'MENU_ITEM_DELIVERY_GROUPS',0,'Группы для рассылок',1,1),(132,'2016-01-23 14:46:09',0x00,'MENU_ITEM_EMAIL_TEMPLATES',0,'E-MAIL шаблоны',1,1),(133,'2016-01-23 14:46:09',0x00,'MENU_ITEM_FINANCE',0,'Финансовая статистика',1,1),(134,'2016-01-23 14:46:09',0x00,'MENU_ITEM_MAIN',0,'Главная',1,1),(135,'2016-01-23 14:46:09',0x00,'MENU_ITEM_MESSAGE_SCHEDULER',0,'Рассылки',1,1),(136,'2016-01-23 14:46:09',0x00,'MENU_ITEM_METHODS',0,'Методы исследований',1,1),(137,'2016-01-23 14:46:09',0x00,'MENU_ITEM_METHOD_TYPES',0,'Типы методов исследований',1,1),(138,'2016-01-23 14:46:09',0x00,'MENU_ITEM_PAYMENTS',0,'Платежи',1,1),(139,'2016-01-23 14:46:09',0x00,'MENU_ITEM_PERSONAL_SCHEDULE',0,'Мое расписание',1,1),(140,'2016-01-23 14:46:09',0x00,'MENU_ITEM_PLAN',0,'План приемов',1,1),(141,'2016-01-23 14:46:09',0x00,'MENU_ITEM_PLAN_GENERAL',0,'Полный план приемов',1,1),(142,'2016-01-23 14:46:09',0x00,'MENU_ITEM_ROOMS',0,'Кабинеты',1,1),(143,'2016-01-23 14:46:09',0x00,'MENU_ITEM_SALARY',0,'Зар. Платы',1,1),(144,'2016-01-23 14:46:09',0x00,'MENU_ITEM_SCHEDULE',0,'Расписание',1,1),(145,'2016-01-23 14:46:09',0x00,'MENU_ITEM_SCHEDULE_GENERAL',0,'Все записи приемов',1,1),(146,'2016-01-23 14:46:09',0x00,'MENU_ITEM_SEND_SMS',0,'Отправить СМС',1,1),(147,'2016-01-23 14:46:09',0x00,'MENU_ITEM_SETTINGS',0,'Настройки',1,1),(148,'2016-01-23 14:46:09',0x00,'MENU_ITEM_SMS_TEMPLATES',0,'СМС шаблоны',1,1),(149,'2016-01-23 14:46:09',0x00,'MENU_ITEM_TIME',0,'Временная статистика',1,1),(150,'2016-01-23 14:46:09',0x00,'MENU_ITEM_USERS',0,'Пользователи',1,1),(151,'2016-01-23 14:46:09',0x00,'MENU_ITEM_USER_TYPES',0,'Группы пользователей',1,1),(152,'2016-01-23 14:46:09',0x00,'MESSAGE_SCHEDULER_ADD_DIALOG_TITLE',0,'Создние новой рассылки',1,1),(153,'2016-01-23 14:46:09',0x00,'MESSAGE_SCHEDULER_CREATE_BUTTON',0,'Добавить',1,1),(154,'2016-01-23 14:46:09',0x00,'MESSAGE_SCHEDULER_DATE_END',0,'Конец рассылки',1,1),(155,'2016-01-23 14:46:09',0x00,'MESSAGE_SCHEDULER_DATE_START',0,'Начало рассылки',1,1),(156,'2016-01-23 14:46:09',0x00,'MESSAGE_SCHEDULER_DAY_OF_WEEK',0,'Дни недели',1,1),(157,'2016-01-23 14:46:09',0x00,'MESSAGE_SCHEDULER_DELETED',0,'Рассылка удалена',1,1),(158,'2016-01-23 14:46:09',0x00,'MESSAGE_SCHEDULER_DELETE_BUTTON',0,'Удалить рассылку',1,1),(159,'2016-01-23 14:46:09',0x00,'MESSAGE_SCHEDULER_DELETE_CONFIRM_MESSAGE',0,'Вы действительно хотите удалить рассылку?',1,1),(160,'2016-01-23 14:46:09',0x00,'MESSAGE_SCHEDULER_DELETE_CONFIRM_TITLE',0,'Внимание!',1,1),(161,'2016-01-23 14:46:09',0x00,'MESSAGE_SCHEDULER_DELIVERY_GROUPS',0,'Группы для рассылки',1,1),(162,'2016-01-23 14:46:09',0x00,'MESSAGE_SCHEDULER_EDIT_DIALOG_TITLE',0,'Изменение рассылки',1,1),(163,'2016-01-23 14:46:09',0x00,'MESSAGE_SCHEDULER_NEXT',0,'Следующая рассылка',1,1),(164,'2016-01-23 14:46:09',0x00,'MESSAGE_SCHEDULER_SAVED',0,'Новая рассылка создана',1,1),(166,'2016-01-23 14:46:09',0x00,'MESSAGE_SCHEDULER_SEND_BUTTON',0,'Отправить сейчас',1,1),(167,'2016-01-23 14:46:09',0x00,'MESSAGE_SCHEDULER_TEMPLATE',0,'Текст сообщения ',1,1),(168,'2016-01-23 14:46:09',0x00,'MESSAGE_SCHEDULER_TIME',0,'Время рассылки',1,1),(169,'2016-01-23 14:46:09',0x00,'MESSAGE_SCHEDULER_TITLE',0,'Рассылки',1,1),(170,'2016-01-23 14:46:09',0x00,'MESSAGE_SCHEDULER_UPDATED',0,'Изменения внесены!',1,1),(171,'2016-01-23 14:46:09',0x00,'MESSAGE_SCHEDULER_UPDATE_BUTTON',0,'Изменить рассылку',1,1),(172,'2016-01-23 14:46:09',0x00,'MESSAGE_SCHEDULER_USER',0,'Адресат',1,1),(173,'2016-01-23 14:46:09',0x00,'MESSAGE_TEMPLATES_ADD_DIALOG_TITLE',0,'Создние нового шаблона',1,1),(174,'2016-01-23 14:46:09',0x00,'MESSAGE_TEMPLATES_CREATE_BUTTON',0,'Добавить',1,1),(175,'2016-01-23 14:46:09',0x00,'MESSAGE_TEMPLATES_DELETED',0,'Шаблон удален',1,1),(176,'2016-01-23 14:46:09',0x00,'MESSAGE_TEMPLATES_DELETE_BUTTON',0,'Удалить шаблон',1,1),(177,'2016-01-23 14:46:09',0x00,'MESSAGE_TEMPLATES_DELETE_CONFIRM_MESSAGE',0,'Вы действительно хотите удалить шаблон?',1,1),(178,'2016-01-23 14:46:09',0x00,'MESSAGE_TEMPLATES_DELETE_CONFIRM_TITLE',0,'Внимание!',1,1),(179,'2016-01-23 14:46:09',0x00,'MESSAGE_TEMPLATES_EDIT_DIALOG_TITLE',0,'Изменение шаблона',1,1),(180,'2016-01-23 14:46:09',0x00,'MESSAGE_TEMPLATES_SAVED',0,'Новый шаблон создан',1,1),(181,'2016-01-23 14:46:09',0x00,'MESSAGE_TEMPLATES_SAVE_AS_NEW_BUTTON',0,'Сохранить как новый',1,1),(183,'2016-01-23 14:46:09',0x00,'MESSAGE_TEMPLATES_SEND_BUTTON',0,'Отправить',1,1),(184,'2016-01-23 14:46:09',0x00,'MESSAGE_TEMPLATES_UPDATED',0,'Изменения внесены!',1,1),(185,'2016-01-23 14:46:09',0x00,'MESSAGE_TEMPLATES_UPDATE_BUTTON',0,'Изменить шаблон',1,1),(186,'2016-01-23 14:46:09',0x00,'METHODS_ADD_BUTTON',0,'Добавить',1,1),(187,'2016-01-23 14:46:09',0x00,'METHODS_ADD_DIALOG_TITLE',0,'Добавить метод',1,1),(188,'2016-01-23 14:46:09',0x00,'METHODS_ADD_DOCTORS',0,'Назначить докторов',1,1),(189,'2016-01-23 14:46:09',0x00,'METHODS_ADD_SUCCESS_END',0,'назначены на метод:',1,1),(190,'2016-01-23 14:46:09',0x00,'METHODS_ADD_SUCCESS_START',0,'Доктора:',1,1),(191,'2016-01-23 14:46:09',0x00,'METHODS_DELETED',0,'Метод успешно удален!',1,1),(192,'2016-01-23 14:46:09',0x00,'METHODS_DELETE_BUTTON',0,'Удалить',1,1),(193,'2016-01-23 14:46:09',0x00,'METHODS_DELETE_CONFIRM_MESSAGE',0,'Вы действительно хотите удалить метод исследования?',1,1),(194,'2016-01-23 14:46:09',0x00,'METHODS_DELETE_CONFIRM_NO',0,'Нет',1,1),(195,'2016-01-23 14:46:09',0x00,'METHODS_DELETE_CONFIRM_TITLE',0,'Внимание!',1,1),(196,'2016-01-23 14:46:09',0x00,'METHODS_DELETE_CONFIRM_YES',0,'Да',1,1),(197,'2016-01-23 14:46:09',0x00,'METHODS_DOCTORS_PICKER_SOURCE',0,'Все доктора',1,1),(198,'2016-01-23 14:46:09',0x00,'METHODS_EDITED',0,'Изменения внесены!',1,1),(199,'2016-01-23 14:46:09',0x00,'METHODS_EDIT_BUTTON',0,'Редактировать',1,1),(200,'2016-01-23 14:46:09',0x00,'METHODS_EDIT_DIALOG_TITLE',0,'Изменение данных о методе',1,1),(201,'2016-01-23 14:46:09',0x00,'METHODS_FULL_DESCRIPTION',0,'Полное описание',1,1),(202,'2016-01-23 14:46:09',0x00,'METHODS_FULL_NAME',0,'Полное название',1,1),(203,'2016-01-23 14:46:09',0x00,'METHODS_PICKER_SOURCE',0,'Все доктора',1,1),(204,'2016-01-23 14:46:09',0x00,'METHODS_PICKER_TITLE',0,'Назначение докторов для метода исследования',1,1),(205,'2016-01-23 14:46:09',0x00,'METHODS_PRICE',0,'Стоимость, грн',1,1),(206,'2016-01-23 14:46:09',0x00,'METHODS_PRICE_CHANGE_BUTTON',0,'Изменить стоимость',1,1),(207,'2016-01-23 14:46:09',0x00,'METHODS_PRICE_CREATED',0,'Новая цена внесена!',1,1),(208,'2016-01-23 14:46:09',0x00,'METHODS_PRICE_DATE',0,'Начало действия цены',1,1),(209,'2016-01-23 14:46:09',0x00,'METHODS_PRICE_HISTORY',0,'История изменения цен',1,1),(210,'2016-01-23 14:46:09',0x00,'METHODS_PRICE_LAST',0,'Актуальная стоимость',1,1),(211,'2016-01-23 14:46:09',0x00,'METHODS_REMOVE_SUCCESS_END',0,'сняты с метода: ',1,1),(212,'2016-01-23 14:46:09',0x00,'METHODS_REMOVE_SUCCESS_START',0,'Доктора: ',1,1),(213,'2016-01-23 14:46:09',0x00,'METHODS_SAVED',0,'Новый метод создан!',1,1),(215,'2016-01-23 14:46:09',0x00,'METHODS_SHORT_DESCRIPTION',0,'Краткое описание',1,1),(216,'2016-01-23 14:46:09',0x00,'METHODS_SHORT_NAME',0,'Аббревиатура',1,1),(217,'2016-01-23 14:46:09',0x00,'METHODS_TIME',0,'Длительность исследования',1,1),(218,'2016-01-23 14:46:09',0x00,'METHODS_TITLE',0,'Методы исследований',1,1),(219,'2016-01-23 14:46:09',0x00,'METHODS_TYPE',0,'Тип исследования',1,1),(220,'2016-01-23 14:46:09',0x00,'PAYMENTS_ADD_BUTTON',0,'Создать',1,1),(221,'2016-01-23 14:46:09',0x00,'PAYMENTS_ADD_PRINT_BUTTON',0,'Создать и распечатать',1,1),(222,'2016-01-23 14:46:09',0x00,'PAYMENTS_ADD_TITLE',0,'Создание платежа',1,1),(223,'2016-01-23 14:46:09',0x00,'PAYMENTS_CASHIER',0,'Кассир:',1,1),(224,'2016-01-23 14:46:09',0x00,'PAYMENTS_CREATED',0,'Платеж успешно создан!',1,1),(225,'2016-01-23 14:46:09',0x00,'PAYMENTS_DATE_TIME',0,'Дата:',1,1),(226,'2016-01-23 14:46:09',0x00,'PAYMENTS_TOTAL',0,'Сумма:',1,1),(227,'2016-01-23 14:46:09',0x00,'PAYMENTS_TYPE',0,'Тип:',1,1),(228,'2016-01-23 14:46:09',0x00,'PAYMENTS_TYPE_INCOMING',0,'Приходной',1,1),(229,'2016-01-23 14:46:09',0x00,'PAYMENTS_TYPE_OUTOMING',0,'Расходной',1,1),(230,'2016-01-23 14:46:09',0x00,'PLAN_ADD_BUTTON',0,'Добавить',1,1),(231,'2016-01-23 14:46:09',0x00,'PLAN_ADD_DIALOG_TITLE',0,'Запланировать прием',1,1),(232,'2016-01-23 14:46:09',0x00,'PLAN_CHOOSE',0,' ',1,1),(233,'2016-01-23 14:46:09',0x00,'PLAN_DELETED',0,'Запланированный прием успешно удален!',1,1),(234,'2016-01-23 14:46:09',0x00,'PLAN_DELETE_BUTTON',0,'Удалить',1,1),(235,'2016-01-23 14:46:09',0x00,'PLAN_DELETE_CONFIRM_MESSAGE',0,'Вы действительно хотите удалить запланированный прием?',1,1),(236,'2016-01-23 14:46:09',0x00,'PLAN_DELETE_CONFIRM_NO',0,'Нет',1,1),(237,'2016-01-23 14:46:09',0x00,'PLAN_DELETE_CONFIRM_TITLE',0,'Внимание!',1,1),(238,'2016-01-23 14:46:09',0x00,'PLAN_DELETE_CONFIRM_YES',0,'Да',1,1),(239,'2016-01-23 14:46:09',0x00,'PLAN_DOCTOR',0,'Доктор',1,1),(240,'2016-01-23 14:46:09',0x00,'PLAN_EDITED',0,'Изменения внесены!',1,1),(241,'2016-01-23 14:46:09',0x00,'PLAN_EDIT_BUTTON',0,'Редактировать',1,1),(242,'2016-01-23 14:46:09',0x00,'PLAN_EDIT_DIALOG_TITLE',0,'Изменение данных о запланированном приеме',1,1),(243,'2016-01-23 14:46:09',0x00,'PLAN_ROOM',0,'Кабинет',1,1),(244,'2016-01-23 14:46:09',0x00,'PLAN_SAVED',0,'Прием запланирован!',1,1),(246,'2016-01-23 14:46:09',0x00,'PLAN_TIME_END',0,'Конец приема',1,1),(247,'2016-01-23 14:46:09',0x00,'PLAN_TIME_START',0,'Начало приема',1,1),(248,'2016-01-23 14:46:09',0x00,'PLAN_TITLE',0,'План приемов',1,1),(249,'2016-01-23 14:46:09',0x00,'PLAN_TITLE_FULL',0,'Полный план приемов',1,1),(250,'2016-01-23 14:46:09',0x00,'PLAN_VALIDATE_DATE',0,'Конечная дата должна быть позже начальной!',1,1),(251,'2016-01-23 14:46:09',0x00,'PLAN_VALIDATE_SCHEDULE_DELETE',0,'Сначала удалите запланированные приемы: ',1,1),(252,'2016-01-23 14:46:09',0x00,'PLAN_VALIDATE_SCHEDULE_UPDATE',0,'Уже есть запланированные приемы на данное время: ',1,1),(253,'2016-01-23 14:46:09',0x00,'PROFILE_CROP_AVATAR_ERROR_MESSAGE',0,'Ошибка установки аватарки!',1,1),(254,'2016-01-23 14:46:09',0x00,'PROFILE_CROP_AVATAR_ERROR_TITLE',0,'Ошибка!',1,1),(255,'2016-01-23 14:46:09',0x00,'PROFILE_CROP_AVATAR_SUCCESS_MESSAGE',0,'Аватарка успешно установлена, обновите страницу!',1,1),(256,'2016-01-23 14:46:09',0x00,'PROFILE_CROP_AVATAR_SUCCESS_TITLE',0,'Успешно!',1,1),(257,'2016-01-23 14:46:09',0x00,'PROFILE_CROP_AVATAR_TITLE',0,'Обрезать загруженное фото',1,1),(258,'2016-01-23 14:46:09',0x00,'PROFILE_CROP_BUTTON',0,'Обрезать',1,1),(259,'2016-01-23 14:46:09',0x00,'PROFILE_PASSWORD_GOOD',0,'Нормальный пароль',1,1),(260,'2016-01-23 14:46:09',0x00,'PROFILE_PASSWORD_PROMT',0,'Введите пароль',1,1),(261,'2016-01-23 14:46:09',0x00,'PROFILE_PASSWORD_REPEAT',0,'Повторите пароль',1,1),(262,'2016-01-23 14:46:09',0x00,'PROFILE_PASSWORD_STRONG',0,'Хороший пароль',1,1),(263,'2016-01-23 14:46:09',0x00,'PROFILE_PASSWORD_WEAK',0,'Слабый пароль',1,1),(264,'2016-01-23 14:46:09',0x00,'PROFILE_RELOAD_BUTTON',0,'Обновить',1,1),(265,'2016-01-23 14:46:09',0x00,'PROFILE_TITLE',0,'Ваш профиль',1,1),(266,'2016-01-23 14:46:09',0x00,'PROFILE_UPLOAD_AVATAR_BUTTON',0,'Загрузить',1,1),(267,'2016-01-23 14:46:09',0x00,'REGISTRATION_TITLE',0,'Регистрация',1,1),(268,'2016-01-23 14:46:09',0x00,'ROOMS_ADD_BUTTON',0,'Добавить',1,1),(269,'2016-01-23 14:46:09',0x00,'ROOMS_ADD_DIALOG_TITLE',0,'Добавить кабинет',1,1),(270,'2016-01-23 14:46:09',0x00,'ROOMS_DELETED',0,'Кабинет успешно удален!',1,1),(271,'2016-01-23 14:46:09',0x00,'ROOMS_DELETE_BUTTON',0,'Удалить',1,1),(272,'2016-01-23 14:46:09',0x00,'ROOMS_DELETE_CONFIRM_MESSAGE',0,'Вы действительно хотите удалить кабинет?',1,1),(273,'2016-01-23 14:46:09',0x00,'ROOMS_DELETE_CONFIRM_NO',0,'Нет',1,1),(274,'2016-01-23 14:46:09',0x00,'ROOMS_DELETE_CONFIRM_TITLE',0,'Внимание!',1,1),(275,'2016-01-23 14:46:09',0x00,'ROOMS_DELETE_CONFIRM_YES',0,'Да',1,1),(276,'2016-01-23 14:46:09',0x00,'ROOMS_EDITED',0,'Изменения внесены!',1,1),(277,'2016-01-23 14:46:09',0x00,'ROOMS_EDIT_BUTTON',0,'Редактировать',1,1),(278,'2016-01-23 14:46:09',0x00,'ROOMS_EDIT_DIALOG_TITLE',0,'Изменение данных о кабинете',1,1),(279,'2016-01-23 14:46:09',0x00,'ROOMS_NAME',0,'Название кабинета',1,1),(280,'2016-01-23 14:46:09',0x00,'ROOMS_SAVED',0,'Новый кабинет создан!',1,1),(282,'2016-01-23 14:46:09',0x00,'ROOMS_TITLE',0,'Кабинеты',1,1),(283,'2016-01-23 14:46:09',0x00,'SCHEDULE_ADD_BUTTON',0,'Добавить',1,1),(284,'2016-01-23 14:46:09',0x00,'SCHEDULE_ADD_DIALOG_TITLE',0,'Изменить прием доктора: ',1,1),(285,'2016-01-23 14:46:09',0x00,'SCHEDULE_ALL_ROOMS',0,'Все кабинеты',1,1),(286,'2016-01-23 14:46:09',0x00,'SCHEDULE_ASSISTENT',0,'Ассистент',1,1),(287,'2016-01-23 14:46:09',0x00,'SCHEDULE_BREAK_TIME',0,'Перерыв:',1,1),(288,'2016-01-23 14:46:09',0x00,'SCHEDULE_CHOOSE',0,' ',1,1),(289,'2016-01-23 14:46:09',0x00,'SCHEDULE_DELETED',0,'Прием отменен!',1,1),(290,'2016-01-23 14:46:09',0x00,'SCHEDULE_DELETE_BUTTON',0,'Удалить',1,1),(291,'2016-01-23 14:46:09',0x00,'SCHEDULE_DELETE_CONFIRM_MESSAGE',0,'Вы действительно хотите отменить этот прием?',1,1),(292,'2016-01-23 14:46:09',0x00,'SCHEDULE_DELETE_CONFIRM_NO',0,'Нет',1,1),(293,'2016-01-23 14:46:09',0x00,'SCHEDULE_DELETE_CONFIRM_TITLE',0,'Внимание!',1,1),(294,'2016-01-23 14:46:09',0x00,'SCHEDULE_DELETE_CONFIRM_YES',0,'Да',1,1),(295,'2016-01-23 14:46:09',0x00,'SCHEDULE_DOCTOR_TITLE',0,'Ваше расписание',1,1),(296,'2016-01-23 14:46:09',0x00,'SCHEDULE_EDITED',0,'Изменения внесены!',1,1),(297,'2016-01-23 14:46:09',0x00,'SCHEDULE_EDIT_BUTTON',0,'Редактировать',1,1),(298,'2016-01-23 14:46:09',0x00,'SCHEDULE_EDIT_DIALOG_TITLE',0,'Изменение данных о приеме',1,1),(299,'2016-01-23 14:46:09',0x00,'SCHEDULE_METHOD',0,'Исследования',1,1),(300,'2016-01-23 14:46:09',0x00,'SCHEDULE_METHOD_ADD_SUCCESS_END',0,'добавлены',1,1),(301,'2016-01-23 14:46:09',0x00,'SCHEDULE_METHOD_ADD_SUCCESS_START',0,'Методы:',1,1),(302,'2016-01-23 14:46:09',0x00,'SCHEDULE_METHOD_AVAILABLE',0,'Доступные исследования',1,1),(303,'2016-01-23 14:46:09',0x00,'SCHEDULE_METHOD_REMOVE_SUCCESS_END',0,'удалены',1,1),(304,'2016-01-23 14:46:09',0x00,'SCHEDULE_METHOD_REMOVE_SUCCESS_START',0,'Методы: ',1,1),(305,'2016-01-23 14:46:09',0x00,'SCHEDULE_METHOD_SELECTED',0,'Выбранные исследования',1,1),(306,'2016-01-23 14:46:09',0x00,'SCHEDULE_METHOD_TYPE',0,'Тип исследований',1,1),(307,'2016-01-23 14:46:09',0x00,'SCHEDULE_MINUTES',0,'мин',1,1),(308,'2016-01-23 14:46:09',0x00,'SCHEDULE_PARTNER',0,'Направил',1,1),(309,'2016-01-23 14:46:09',0x00,'SCHEDULE_PRINT_FORM',0,'Распечатать анкету',1,1),(310,'2016-01-23 14:46:09',0x00,'SCHEDULE_ADD_PAYMENT',0,'Провести платеж',1,1),(311,'2016-01-23 14:46:09',0x00,'SCHEDULE_PRINT_SECURE_AGREEMENT',0,'Распечатать соглашение на обработку данных',1,1),(312,'2016-01-23 14:46:09',0x00,'SCHEDULE_SAVED',0,'Прием добавлен!',1,1),(314,'2016-01-23 14:46:09',0x00,'SCHEDULE_TAB_APPOINTMENT',0,'Прием',1,1),(315,'2016-01-23 14:46:09',0x00,'SCHEDULE_TAB_DOCUMENTS',0,'Документы',1,1),(316,'2016-01-23 14:46:09',0x00,'SCHEDULE_TAB_PERSONAL',0,'Пациент',1,1),(317,'2016-01-23 14:46:09',0x00,'SCHEDULE_TIME_END',0,'Время окончания приема ',1,1),(318,'2016-01-23 14:46:09',0x00,'SCHEDULE_TIME_START',0,'Время начала приема',1,1),(319,'2016-01-23 14:46:09',0x00,'SCHEDULE_TITLE',0,'Расписание приемов',1,1),(320,'2016-01-23 14:46:09',0x00,'SCHEDULE_TITLE_FULL',0,'Все записи',1,1),(321,'2016-01-23 14:46:09',0x00,'SCHEDULE_TOTAL_PRICE',0,'полная стоимость',1,1),(322,'2016-01-23 14:46:09',0x00,'SCHEDULE_TOTAL_TIME_LENGTH',0,'длительность',1,1),(323,'2016-01-23 14:46:09',0x00,'SCHEDULE_VALIDATE_DATE',0,'Конечная дата должна быть позже начальной!',1,1),(324,'2016-01-23 14:46:09',0x00,'SCHEDULE_VALIDATE_NOT_IN_PLAN',0,'На данное время нет запланированного приема, либо длительность приема превышает запланированную',1,1),(325,'2016-01-23 14:46:09',0x00,'SCHEDULE_VALIDATE_NOT_IN_PLAN_START',0,'На данное время нет запланированного приема!',1,1),(326,'2016-01-23 14:46:09',0x00,'SCHEDULE_VALIDATE_SCHEDULE_DELETE',0,'Сначала удалите назначенные приемы: ',1,1),(327,'2016-01-23 14:46:09',0x00,'SCHEDULE_VALIDATE_SCHEDULE_UPDATE',0,'Уже есть назначенные приемы на данное время: ',1,1),(328,'2016-01-23 14:46:09',0x00,'SCHEDULE_WITHOUT_ASSISTENT',0,'Без ассистента',1,1),(329,'2016-01-23 14:46:09',0x00,'SCHEDULE_WITHOUT_DOCTOR_DIRECTED',0,'Самообращение',1,1),(330,'2016-01-23 14:46:09',0x00,'SEND_SMS_PHONE',0,'Номер телефона',1,1),(331,'2016-01-23 14:46:09',0x00,'SEND_SMS_SEND',0,'Отправить',1,1),(332,'2016-01-23 14:46:09',0x00,'SEND_SMS_TEXT',0,'Текст СМС',1,1),(333,'2016-01-23 14:46:09',0x00,'SEND_SMS_TITLE',0,'Отправка СМС',1,1),(337,'2016-01-23 14:46:09',0x00,'SETTINGS_PAGE_TITLE',0,'Настройки',1,1),(338,'2016-01-23 14:46:09',0x00,'SMS_TEMPLATES_TITLE',0,'СМС Шаблоны',1,1),(339,'2016-01-23 14:46:09',0x00,'SMS_TEMPLATE_CONTENT',0,'Текст сообщения ',1,1),(340,'2016-01-23 14:46:09',0x00,'SMS_TEMPLATE_SYSTEM',0,'Системный',1,1),(341,'2016-01-23 14:46:09',0x00,'SMS_TEMPLATE_TYPE',0,'Тип сообщения',1,1),(342,'2016-01-23 14:46:09',0x00,'STATISTICS_BLOCK_HEADER',0,'Статистика',1,1),(343,'2016-01-23 14:46:09',0x00,'USERS_ADDRESS',0,'Адрес',1,1),(344,'2016-01-23 14:46:09',0x00,'USERS_ADD_BUTTON',0,'Добавить',1,1),(345,'2016-01-23 14:46:09',0x00,'USERS_ADD_SUCCESS_END',0,'добавлен в группы: ',1,1),(346,'2016-01-23 14:46:09',0x00,'USERS_ADD_SUCCESS_START',0,'Пользователь: ',1,1),(347,'2016-01-23 14:46:09',0x00,'USERS_ADD_USERS',0,'Добавить в группу',1,1),(348,'2016-01-23 14:46:09',0x00,'USERS_BIRTH_DATE',0,'Дата рождения',1,1),(349,'2016-01-23 14:46:09',0x00,'USERS_CLEAR_FILTERS',0,'Очистить все фильтры',1,1),(350,'2016-01-23 14:46:09',0x00,'USERS_COLOR',0,'Цвет',1,1),(351,'2016-01-23 14:46:09',0x00,'USERS_DELETE',0,'Удалить',1,1),(352,'2016-01-23 14:46:09',0x00,'USERS_DELETED',0,'Пользователь ',1,1),(353,'2016-01-23 14:46:09',0x00,'USERS_DELETE_CONFIRM_MESSAGE',0,'Вы действительно хотите удалить пользователя?',1,1),(354,'2016-01-23 14:46:09',0x00,'USERS_DELETE_CONFIRM_NO',0,'Нет',1,1),(355,'2016-01-23 14:46:09',0x00,'USERS_DELETE_CONFIRM_TITLE',0,'Внимание!',1,1),(356,'2016-01-23 14:46:09',0x00,'USERS_DELETE_CONFIRM_YES',0,'Да',1,1),(357,'2016-01-23 14:46:09',0x00,'USERS_EMAIL',0,'Адрес эл. почты',1,1),(358,'2016-01-23 14:46:09',0x00,'USERS_FIRST_NAME',0,'Имя',1,1),(359,'2016-01-23 14:46:09',0x00,'USERS_GLOBAL_SEARCH',0,'Поиск',1,1),(360,'2016-01-23 14:46:09',0x00,'USERS_IS_FOREIGNER',0,'Иностранец',1,1),(361,'2016-01-23 14:46:09',0x00,'USERS_LAST_NAME',0,'Фамилия',1,1),(362,'2016-01-23 14:46:09',0x00,'USERS_LOGIN',0,'Логин',1,1),(363,'2016-01-23 14:46:09',0x00,'USERS_MESSAGES_ACCEPTED',0,'Согласие на уведомления',1,1),(364,'2016-01-23 14:46:09',0x00,'USERS_MIDDLE_NAME',0,'Отчество',1,1),(365,'2016-01-23 14:46:09',0x00,'USERS_NO_GROUPS',0,'Не состоит ни в одной группе',1,1),(366,'2016-01-23 14:46:09',0x00,'USERS_PASSWORD',0,'Пароль',1,1),(367,'2016-01-23 14:46:09',0x00,'USERS_PHONE_NUMBER_HOME',0,'Номер телефона дом.',1,1),(368,'2016-01-23 14:46:09',0x00,'USERS_PHONE_NUMBER_MOBILE',0,'Номер телефона моб.',1,1),(369,'2016-01-23 14:46:09',0x00,'USERS_PICKER_SOURCE',0,'Все группы',1,1),(370,'2016-01-23 14:46:09',0x00,'USERS_PICKER_TITLE',0,'Добавление пользователя в группы',1,1),(371,'2016-01-23 14:46:09',0x00,'USERS_PLACEHOLDER_BIRTH_DATE',0,'05-12-1985',1,1),(372,'2016-01-23 14:46:09',0x00,'USERS_PLACEHOLDER_EMAIL',0,'i.sidorov@gmail.com',1,1),(373,'2016-01-23 14:46:09',0x00,'USERS_PLACEHOLDER_FIRST_NAME',0,'Иван',1,1),(374,'2016-01-23 14:46:09',0x00,'USERS_PLACEHOLDER_LAST_NAME',0,'Иванов',1,1),(375,'2016-01-23 14:46:09',0x00,'USERS_PLACEHOLDER_LOGIN',0,'[a-z][A-Z][0-9]',1,1),(376,'2016-01-23 14:46:09',0x00,'USERS_PLACEHOLDER_MIDDLE_NAME',0,'Иванович',1,1),(377,'2016-01-23 14:46:09',0x00,'USERS_PLACEHOLDER_PASSWORD',0,'![a-z]![A-Z]![0-9] не менее 6 символов',1,1),(378,'2016-01-23 14:46:09',0x00,'USERS_PLACEHOLDER_PHONE_NUMBER_HOME',0,'+380441234567',1,1),(379,'2016-01-23 14:46:09',0x00,'USERS_PLACEHOLDER_PHONE_NUMBER_MOBILE',0,'+380501234567',1,1),(380,'2016-01-23 14:46:09',0x00,'USERS_PROFILE',0,'Профиль',1,1),(381,'2016-01-23 14:46:09',0x00,'USERS_REMOVE_SUCCESS_END',0,'удален из групп: ',1,1),(382,'2016-01-23 14:46:09',0x00,'USERS_REMOVE_SUCCESS_START',0,'Пользователь: ',1,1),(383,'2016-01-23 14:46:09',0x00,'USERS_TITLE',0,'Пользователи',1,1),(384,'2016-01-23 14:46:09',0x00,'USERS_USER_GROUPS',0,'Группы',1,1),(385,'2016-01-23 14:46:09',0x00,'USER_TYPES_ADD_BUTTON',0,'Добавить',1,1),(386,'2016-01-23 14:46:09',0x00,'USER_TYPES_ADD_DIALOG_TITLE',0,'Добавить группу пользователей',1,1),(387,'2016-01-23 14:46:09',0x00,'USER_TYPES_ADD_POLICIES',0,'Добавить права доступа!',1,1),(388,'2016-01-23 14:46:09',0x00,'USER_TYPES_ADD_SUCCESS_END',0,'добавлены в группу',1,1),(389,'2016-01-23 14:46:09',0x00,'USER_TYPES_ADD_SUCCESS_START',0,'Пользователли:',1,1),(390,'2016-01-23 14:46:09',0x00,'USER_TYPES_ADD_USERS',0,'Добавить пользователей!',1,1),(391,'2016-01-23 14:46:09',0x00,'USER_TYPES_DELETED',0,'Группа успешно удалена!',1,1),(392,'2016-01-23 14:46:09',0x00,'USER_TYPES_DELETE_BUTTON',0,'Удалить',1,1),(393,'2016-01-23 14:46:09',0x00,'USER_TYPES_DELETE_CONFIRM_MESSAGE',0,'Вы действительно хотите удалить группу?',1,1),(394,'2016-01-23 14:46:09',0x00,'USER_TYPES_DELETE_CONFIRM_NO',0,'Нет',1,1),(395,'2016-01-23 14:46:09',0x00,'USER_TYPES_DELETE_CONFIRM_TITLE',0,'Внимание!',1,1),(396,'2016-01-23 14:46:09',0x00,'USER_TYPES_DELETE_CONFIRM_YES',0,'Да',1,1),(397,'2016-01-23 14:46:09',0x00,'USER_TYPES_EDITED',0,'Изменения внесены!',1,1),(398,'2016-01-23 14:46:09',0x00,'USER_TYPES_EDIT_BUTTON',0,'Редактировать',1,1),(399,'2016-01-23 14:46:09',0x00,'USER_TYPES_EDIT_DIALOG_TITLE',0,'Изменение данных о группе',1,1),(400,'2016-01-23 14:46:09',0x00,'USER_TYPES_NAME',0,'Название',1,1),(401,'2016-01-23 14:46:09',0x00,'USER_TYPES_PICKER_SOURCE',0,'Все пользователи',1,1),(402,'2016-01-23 14:46:09',0x00,'USER_TYPES_PICKER_TITLE',0,'Добавление пользователей в группу',1,1),(403,'2016-01-23 14:46:09',0x00,'USER_TYPES_POLICIES_ADD_SUCCESS_END',0,'добавлены для группы',1,1),(404,'2016-01-23 14:46:09',0x00,'USER_TYPES_POLICIES_ADD_SUCCESS_START',0,'Права доступа:',1,1),(405,'2016-01-23 14:46:09',0x00,'USER_TYPES_POLICIES_PICKER_SOURCE',0,'Все права доступа',1,1),(406,'2016-01-23 14:46:09',0x00,'USER_TYPES_POLICIES_PICKER_TITLE',0,'Добавление прав доступа для группы',1,1),(407,'2016-01-23 14:46:09',0x00,'USER_TYPES_POLICIES_REMOVE_SUCCESS_END',0,'удалены для группы: ',1,1),(408,'2016-01-23 14:46:09',0x00,'USER_TYPES_POLICIES_REMOVE_SUCCESS_START',0,'Права доступа: ',1,1),(409,'2016-01-23 14:46:09',0x00,'USER_TYPES_REMOVE_SUCCESS_END',0,'удалены из группы: ',1,1),(410,'2016-01-23 14:46:09',0x00,'USER_TYPES_REMOVE_SUCCESS_START',0,'Пользователли: ',1,1),(411,'2016-01-23 14:46:09',0x00,'USER_TYPES_SAVED',0,'Новая группа создана!',1,1),(413,'2016-01-23 14:46:09',0x00,'USER_TYPES_TITLE',0,'Группы пользователей',1,1),(414,'2016-01-23 14:46:09',0x00,'VALIDATOR_CYRILLIC_ONLY',0,'Поле может содержать только символы русского алфавита!',1,1),(415,'2016-01-23 14:46:09',0x00,'VALIDATOR_DATE_RANGE',0,'Конечная дата должна быть позже начальной',1,1),(416,'2016-01-23 14:46:09',0x00,'VALIDATOR_EMAIL',0,'Проверьте правильность ввода электронного адреса!',1,1),(417,'2016-01-23 14:46:09',0x00,'VALIDATOR_ERROR_TITLE',0,'Ошибка!',1,1),(418,'2016-01-23 14:46:09',0x00,'VALIDATOR_LATIN',0,'Поле должно содержать символы латинского алфавита!',1,1),(419,'2016-01-23 14:46:09',0x00,'VALIDATOR_LATIN_ONLY',0,'Поле может содержать только символы латинскогоалфавита!',1,1),(420,'2016-01-23 14:46:09',0x00,'VALIDATOR_LITERAL_ONLY',0,'Поле может содержать только символы русского или латинского алфавита!',1,1),(421,'2016-01-23 14:46:09',0x00,'VALIDATOR_LOGIN_IN_USE',0,'Данный логин уже занят!',1,1),(422,'2016-01-23 14:46:09',0x00,'VALIDATOR_METHOD_DUPLICATED_SHORT_NAME',0,'Метод с такой аббревиатурой уже существует!',1,1),(423,'2016-01-23 14:46:09',0x00,'VALIDATOR_METHOD_TIMEINMINUTES',0,'Время должно быть между 1 - 180 минут! ',1,1),(424,'2016-01-23 14:46:09',0x00,'VALIDATOR_NOT_CYRILLIC',0,'Поле не может содержать символы русского алфавита!',1,1),(425,'2016-01-23 14:46:09',0x00,'VALIDATOR_NOT_LATIN',0,'Поле не может содержать символы латинского алфавита!',1,1),(426,'2016-01-23 14:46:09',0x00,'VALIDATOR_NOT_NUMBERS',0,'Поле не может содержать цифры!',1,1),(427,'2016-01-23 14:46:09',0x00,'VALIDATOR_NOT_PUNCT',0,'Поле не может содержать спец символы или знакипунктуации!',1,1),(428,'2016-01-23 14:46:09',0x00,'VALIDATOR_NUMBERS',0,'Поле должно содержать цифры!',1,1),(429,'2016-01-23 14:46:09',0x00,'VALIDATOR_NUMBERS_ONLY',0,'Поле должно содержать только цифры!',1,1),(430,'2016-01-23 14:46:09',0x00,'VALIDATOR_PASSWORD_LESS_SIX',0,'Пароль должен содержать как минимум 6 символов!',1,1),(431,'2016-01-23 14:46:09',0x00,'VALIDATOR_PHONE',0,'Проверьте правильность ввода номера телефона!',1,1),(432,'2016-01-23 14:46:09',0x00,'VALIDATOR_POST_INDEX',0,'Поле должно содержать цифры! 5 символов! ',1,1),(433,'2016-01-23 14:46:09',0x00,'VALIDATOR_PRICE_AFTER_NOW',0,'Дата не может быть в прошлом',1,1),(434,'2016-01-23 14:46:09',0x00,'VALIDATOR_PRICE_ZERO',0,'Цена не может быть равна 0',1,1),(435,'2016-01-23 14:46:09',0x00,'VALIDATOR_PUNCT',0,'Поле должно содержать спец символы или знакипунктуации!',1,1),(436,'2016-01-23 14:46:09',0x00,'VALIDATOR_REQUIRED',0,'Поле обязательно для заполнения!',1,1),(437,'2016-01-23 14:46:09',0x00,'VALIDATOR_SUCCESS_TITLE',0,'Успешно!',1,1),(438,'2016-01-23 18:34:07',0x00,'ASSISTANTS_USER_GROUP_ID',1,'5',NULL,1),(441,'2016-03-06 12:29:01',0x00,'DOCTORS_USER_GROUP_ID',1,'4',NULL,1),(442,'2016-01-23 18:34:07',0x00,'EMAIL_FROM',1,'v.bodnar85@gmail.com',NULL,1),(443,'2016-01-23 18:34:07',0x00,'EMAIL_MESSAGE_TEMPLATE_FOR_SCHEDULE',1,'1',NULL,1),(446,'2016-01-23 18:34:07',0x00,'MDS_MAXIMUM_CYRILLIC_SYMBOLS_IN_SMS',1,'100',NULL,1),(447,'2016-01-23 18:34:07',0x00,'MDS_MAXIMUM_LATIN_SYMBOLS_IN_SMS',1,'175',NULL,1),(448,'2016-01-23 18:34:07',0x00,'METHOD_BREAK_ID',1,'1',NULL,1),(449,'2016-01-23 18:34:07',0x00,'PATIENTS_USER_GROUP_ID',1,'2',NULL,1),(452,'2016-01-23 18:34:07',0x00,'SMS_MESSAGE_TEMPLATE_FOR_SCHEDULE',1,'2',NULL,1),(453,'2016-01-23 18:34:07',0x00,'SMTP_AUTH',1,'true',NULL,1),(454,'2016-01-23 18:34:07',0x00,'SMTP_LOGIN',1,'v.bodnar85@gmail.com',NULL,1),(455,'2016-01-23 18:34:07',0x00,'SMTP_PASSWORD',1,'nenimdada6215891',NULL,1),(456,'2016-01-23 18:34:07',0x00,'SMTP_PORT',1,'587',NULL,1),(457,'2016-01-23 18:34:07',0x00,'SMTP_SERVER',1,'smtp.google.com',NULL,1),(458,'2016-01-23 18:34:07',0x00,'SMTP_SSL',1,'false',NULL,1),(459,'2016-01-23 18:34:07',0x00,'SMTP_STARTTLS',1,'true',NULL,1),(460,'2016-01-23 18:34:07',0x00,'SUPER_ADMIN_ID',1,'1',NULL,1),(461,'2016-01-23 18:34:07',0x00,'SUPER_ADMIN_USER_GROUP_ID',1,'1',NULL,1),(462,'2016-01-23 18:34:07',0x00,'USERS_BREAK_ID',1,'2',NULL,1),(463,'2016-01-23 14:46:09',0x00,'SECURITY_DELIVERY_GROUPS_GROUP_DESCRIPTION',0,'Страница группы для рассылок',1,1),(464,'2016-01-23 14:46:09',0x00,'SECURITY_EMAIL_TEMPLATES_GROUP_DESCRIPTION',0,'Страница E-MAIL шаблоны',1,1),(465,'2016-01-23 14:46:09',0x00,'SECURITY_EMPTY',0,'EMPTY',1,1),(466,'2016-01-23 14:46:09',0x00,'SECURITY_MAIN_PAGE_GROUP_DESCRIPTION',0,'Страница \"Главная\"',1,1),(467,'2016-01-23 14:46:09',0x00,'SECURITY_MENU_GROUP_DESCRIPTION',0,'Все пункты меню',1,1),(468,'2016-01-23 14:46:09',0x00,'SECURITY_MESSAGE_SCHEDULER_GROUP_DESCRIPTION',0,'Страница Рассылки',1,1),(469,'2016-01-23 14:46:09',0x00,'SECURITY_METHODS_GROUP_DESCRIPTION',0,'Страница \"Методы исследований\"',1,1),(470,'2016-01-23 14:46:09',0x00,'SECURITY_PAYMENTS_GROUP_DESCRIPTION',0,'Страница \"Платежи\"',1,1),(471,'2016-01-23 14:46:09',0x00,'SECURITY_PLAN_GROUP_DESCRIPTION',0,'Страницы c планом приемов',1,1),(472,'2016-01-23 14:46:09',0x00,'SECURITY_POLICY_DELIVERY_GROUPS_ADD_SECURITY_POLICIES',0,'Пункт \"Добавить группу польз\" в к. меню',1,1),(473,'2016-01-23 14:46:09',0x00,'SECURITY_POLICY_DELIVERY_GROUPS_ADD_USER',0,'Пункт \"Добавить пользователей\" в к. меню',1,1),(474,'2016-01-23 14:46:09',0x00,'SECURITY_POLICY_DELIVERY_GROUPS_ADD_USER_GROUP',0,'Кнопка \"Добавить группу\"',1,1),(475,'2016-01-23 14:46:09',0x00,'SECURITY_POLICY_DELIVERY_GROUPS_DELETE_USER_GROUP',0,'Пункт \"Удалить\" в контекстном меню',1,1),(476,'2016-01-23 14:46:09',0x00,'SECURITY_POLICY_DELIVERY_GROUPS_EDIT_USER_GROUP',0,'Пункт \"Редактировать\" в контекстном меню',1,1),(477,'2016-01-23 14:46:09',0x00,'SECURITY_POLICY_EMAIL_TEMPLATES_ADD',0,'Кнопка \"Добавить\"',1,1),(478,'2016-01-23 14:46:09',0x00,'SECURITY_POLICY_EMAIL_TEMPLATES_DELETE',0,'Пункт \"Удалить\" в контекстном меню',1,1),(479,'2016-01-23 14:46:09',0x00,'SECURITY_POLICY_EMAIL_TEMPLATES_SEND',0,'Пункт \"Отправить\" в контекстном меню',1,1),(480,'2016-01-23 14:46:09',0x00,'SECURITY_POLICY_EMAIL_TEMPLATES_UPDATE',0,'Пункт \"Редактировать\" в контекстном меню',1,1),(481,'2016-01-23 14:46:09',0x00,'SECURITY_POLICY_MAIN_PAGE_MONTH_APPOINTMENTS',0,'Блок \"Статистика за месяц (кол-во приемов)\"',1,1),(482,'2016-01-23 14:46:09',0x00,'SECURITY_POLICY_MAIN_PAGE_MONTH_SALARY',0,'Блок \"Статистика за месяц (прибыль)\"',1,1),(483,'2016-01-23 14:46:09',0x00,'SECURITY_POLICY_MAIN_PAGE_YEAR_APPOINTMENTS',0,'Блок \"Статистика за год (кол-во приемов)\"',1,1),(484,'2016-01-23 14:46:09',0x00,'SECURITY_POLICY_MAIN_PAGE_YEAR_SALARY',0,'Блок \"Статистика за год (прибыль)\"',1,1),(485,'2016-01-23 14:46:09',0x00,'SECURITY_POLICY_MENU_HEADER_FINANCE',0,'Пункт меню \"Финансы\"',1,1),(486,'2016-01-23 14:46:09',0x00,'SECURITY_POLICY_MENU_HEADER_MANAGER',0,'Пункт меню \"Управление\"',1,1),(487,'2016-01-23 14:46:09',0x00,'SECURITY_POLICY_MENU_HEADER_SCHEDULE',0,'Пункт меню \"Расписание\"',1,1),(488,'2016-01-23 14:46:09',0x00,'SECURITY_POLICY_MENU_ITEM_ACCESS_RIGHTS',0,'Пункт меню \"Права доступа\"',1,1),(489,'2016-01-23 14:46:09',0x00,'SECURITY_POLICY_MENU_ITEM_ADD_PLAN',0,'Пункт меню \"Запланировать прием\"',1,1),(490,'2016-01-23 14:46:09',0x00,'SECURITY_POLICY_MENU_ITEM_ADD_SCHEDULE',0,'Пункт меню \"Добавить прием\"',1,1),(491,'2016-01-23 14:46:09',0x00,'SECURITY_POLICY_MENU_ITEM_ADD_USER',0,'Пункт меню \"Добавить пользователя\"',1,1),(492,'2016-01-23 14:46:09',0x00,'SECURITY_POLICY_MENU_ITEM_APPOINTMENTS',0,'Пункт меню \"Статистика приемов\"',1,1),(493,'2016-01-23 14:46:09',0x00,'SECURITY_POLICY_MENU_ITEM_CASH',0,'Пункт меню \"Касса\"',1,1),(494,'2016-01-23 14:46:09',0x00,'SECURITY_POLICY_MENU_ITEM_CREATE_PAYMENT',0,'Пункт меню \"Создать платеж\"',1,1),(495,'2016-01-23 14:46:09',0x00,'SECURITY_POLICY_MENU_ITEM_DELIVERY_GROUPS',0,'Пункт меню \"Группы для рассылок\"',1,1),(496,'2016-01-23 14:46:09',0x00,'SECURITY_POLICY_MENU_ITEM_EMAIL_TEMPLATES',0,'Пункт меню \"E-MAIL шаблоны\"',1,1),(497,'2016-01-23 14:46:09',0x00,'SECURITY_POLICY_MENU_ITEM_FINANCE',0,'Пункт меню \"Финансовая статистика\"',1,1),(498,'2016-01-23 14:46:09',0x00,'SECURITY_POLICY_MENU_ITEM_MAIN',0,'Пункт меню \"Главная\"',1,1),(499,'2016-01-23 14:46:09',0x00,'SECURITY_POLICY_MENU_ITEM_MESSAGE_SCHEDULER',0,'Пункт меню \"Рассылки\"',1,1),(500,'2016-01-23 14:46:09',0x00,'SECURITY_POLICY_MENU_ITEM_METHODS',0,'Пункт меню \"Методы исследований\"',1,1),(501,'2016-01-23 14:46:09',0x00,'SECURITY_POLICY_MENU_ITEM_METHOD_TYPES',0,'Пункт меню \"Типы методов исследований\"',1,1),(502,'2016-01-23 14:46:09',0x00,'SECURITY_POLICY_MENU_ITEM_PAYMENTS',0,'Пункт меню \"Платежи\"',1,1),(503,'2016-01-23 14:46:09',0x00,'SECURITY_POLICY_MENU_ITEM_PERSONAL_SCHEDULE',0,'Пункт меню \"Мое расписание\"',1,1),(504,'2016-01-23 14:46:09',0x00,'SECURITY_POLICY_MENU_ITEM_PLAN',0,'Пункт меню \"План приемов\"',1,1),(505,'2016-01-23 14:46:09',0x00,'SECURITY_POLICY_MENU_ITEM_PLAN_GENERAL',0,'Пункт меню \"Полный план приемов\"',1,1),(506,'2016-01-23 14:46:09',0x00,'SECURITY_POLICY_MENU_ITEM_ROOMS',0,'Пункт меню \"Кабинеты\"',1,1),(507,'2016-01-23 14:46:09',0x00,'SECURITY_POLICY_MENU_ITEM_SALARY',0,'Пункт меню \"Зар. Платы\"',1,1),(508,'2016-01-23 14:46:09',0x00,'SECURITY_POLICY_MENU_ITEM_SCHEDULE',0,'Пункт меню \"Расписание\"',1,1),(509,'2016-01-23 14:46:09',0x00,'SECURITY_POLICY_MENU_ITEM_SCHEDULE_GENERAL',0,'Пункт меню \"Все записи приемов\"',1,1),(510,'2016-01-23 14:46:09',0x00,'SECURITY_POLICY_MENU_ITEM_SEND_SMS',0,'Пункт меню \"Отправить СМС\"',1,1),(511,'2016-01-23 14:46:09',0x00,'SECURITY_POLICY_MENU_ITEM_SETTINGS',0,'Пункт меню \"Настройки\"',1,1),(512,'2016-01-23 14:46:09',0x00,'SECURITY_POLICY_MENU_ITEM_SMS_TEMPLATES',0,'Пункт меню \"СМС шаблоны\"',1,1),(513,'2016-01-23 14:46:09',0x00,'SECURITY_POLICY_MENU_ITEM_TIME',0,'Пункт меню \"Временная статистика\"',1,1),(514,'2016-01-23 14:46:09',0x00,'SECURITY_POLICY_MENU_ITEM_USERS',0,'Пункт меню \"Пользователи\"',1,1),(515,'2016-01-23 14:46:09',0x00,'SECURITY_POLICY_MENU_ITEM_USER_GROUPS',0,'Пункт меню \"Группы пользователей\"',1,1),(516,'2016-01-23 14:46:09',0x00,'SECURITY_POLICY_MESSAGE_SCHEDULER_ADD',0,'Кнопка \"Добавить\"',1,1),(517,'2016-01-23 14:46:09',0x00,'SECURITY_POLICY_MESSAGE_SCHEDULER_DELETE',0,'Пункт \"Удалить\" в контекстном меню',1,1),(518,'2016-01-23 14:46:09',0x00,'SECURITY_POLICY_MESSAGE_SCHEDULER_UPDATE',0,'Пункт \"Редактировать\" в контекстном меню',1,1),(519,'2016-01-23 14:46:09',0x00,'SECURITY_POLICY_METHODS_ADD',0,'Кнопка \"Добавить\"',1,1),(520,'2016-01-23 14:46:09',0x00,'SECURITY_POLICY_METHODS_ADD_DOCTORS',0,'Пункт \"Назначить докторов\" в к. меню',1,1),(521,'2016-01-23 14:46:09',0x00,'SECURITY_POLICY_METHODS_CHANGE_PRICE',0,'Пункт \"Изменить стоимость\" в к. меню',1,1),(522,'2016-01-23 14:46:09',0x00,'SECURITY_POLICY_METHODS_DELETE',0,'Пункт \"Удалить\" в к. меню',1,1),(523,'2016-01-23 14:46:09',0x00,'SECURITY_POLICY_METHODS_EDIT',0,'Пункт \"Редактировать\" в к. меню',1,1),(524,'2016-01-23 14:46:09',0x00,'SECURITY_POLICY_PAYMENTS_ADD',0,'Кнопка \"Создать\"',1,1),(525,'2016-01-23 14:46:09',0x00,'SECURITY_POLICY_PLAN_CREATE',0,'Создание записи в плане приемов',1,1),(526,'2016-01-23 14:46:09',0x00,'SECURITY_POLICY_PLAN_DELETE',0,'Удаление записи в плане приемов',1,1),(527,'2016-01-23 14:46:09',0x00,'SECURITY_POLICY_PLAN_READ',0,'Просмотр записи в плане приемов',1,1),(528,'2016-01-23 14:46:09',0x00,'SECURITY_POLICY_PLAN_UPDATE',0,'Изменение записи в плане приемов',1,1),(529,'2016-01-23 14:46:09',0x00,'SECURITY_POLICY_PROFILE_EDIT_ADDRESS',0,'Изменение адреса',1,1),(530,'2016-01-23 14:46:09',0x00,'SECURITY_POLICY_PROFILE_EDIT_ANY_USER',0,'Любой пользователь с правами',1,1),(531,'2016-01-23 14:46:09',0x00,'SECURITY_POLICY_PROFILE_EDIT_AVATAR',0,'Изменение аватарки',1,1),(532,'2016-01-23 14:46:09',0x00,'SECURITY_POLICY_PROFILE_EDIT_BIRTH_DATE',0,'Изменение даты рождения',1,1),(533,'2016-01-23 14:46:09',0x00,'SECURITY_POLICY_PROFILE_EDIT_COLOR',0,'Изменение цвета в расписании',1,1),(534,'2016-01-23 14:46:09',0x00,'SECURITY_POLICY_PROFILE_EDIT_EMAIL',0,'Изменение адреса эл. почты',1,1),(535,'2016-01-23 14:46:09',0x00,'SECURITY_POLICY_PROFILE_EDIT_FIRST_NAME',0,'Изменение имени',1,1),(536,'2016-01-23 14:46:09',0x00,'SECURITY_POLICY_PROFILE_EDIT_LAST_NAME',0,'Изменение фамилии',1,1),(537,'2016-01-23 14:46:09',0x00,'SECURITY_POLICY_PROFILE_EDIT_LOGIN',0,'Изменение логина',1,1),(538,'2016-01-23 14:46:09',0x00,'SECURITY_POLICY_PROFILE_EDIT_MESSAGING_ACCEPTED',0,'Изменение разрешения на рассылку',1,1),(539,'2016-01-23 14:46:09',0x00,'SECURITY_POLICY_PROFILE_EDIT_MIDDLE_NAME',0,'Изменение отччества',1,1),(540,'2016-01-23 14:46:09',0x00,'SECURITY_POLICY_PROFILE_EDIT_PASSWORD',0,'Изменение пароля\"',1,1),(541,'2016-01-23 14:46:09',0x00,'SECURITY_POLICY_PROFILE_EDIT_PHONE_NUMBER',0,'Изменение номера телефона',1,1),(542,'2016-01-23 14:46:09',0x00,'SECURITY_POLICY_ROOMS_ADD',0,'Кнопка \"Добавить\"',1,1),(543,'2016-01-23 14:46:09',0x00,'SECURITY_POLICY_ROOMS_DELETE',0,'Пункт \"Удалить\" в к. меню',1,1),(544,'2016-01-23 14:46:09',0x00,'SECURITY_POLICY_ROOMS_EDIT',0,'Пункт \"Редактировать\" в к. меню',1,1),(545,'2016-01-23 14:46:09',0x00,'SECURITY_POLICY_SCHEDULE_CREATE',0,'Создание записи приема',1,1),(546,'2016-01-23 14:46:09',0x00,'SECURITY_POLICY_SCHEDULE_DELETE',0,'Удаление записи о приеме',1,1),(547,'2016-01-23 14:46:09',0x00,'SECURITY_POLICY_SCHEDULE_READ',0,'Просмотр записи о приеме',1,1),(548,'2016-01-23 14:46:09',0x00,'SECURITY_POLICY_SCHEDULE_UPDATE',0,'Изменение записи о приеме',1,1),(549,'2016-01-23 14:46:09',0x00,'SECURITY_POLICY_SMS_TEMPLATES_ADD',0,'Кнопка \"Добавить\"',1,1),(550,'2016-01-23 14:46:09',0x00,'SECURITY_POLICY_SMS_TEMPLATES_DELETE',0,'Пункт \"Удалить\" в контекстном меню',1,1),(551,'2016-01-23 14:46:09',0x00,'SECURITY_POLICY_SMS_TEMPLATES_SEND',0,'Пункт \"Отправить\" в контекстном меню',1,1),(552,'2016-01-23 14:46:09',0x00,'SECURITY_POLICY_SMS_TEMPLATES_UPDATE',0,'Пункт \"Редактировать\" в контекстном меню',1,1),(553,'2016-01-23 14:46:09',0x00,'SECURITY_POLICY_USERS_ADD_USER',0,'Кнопка \"Добавить пользователя\"',1,1),(554,'2016-01-23 14:46:09',0x00,'SECURITY_POLICY_USERS_ADD_USER_TO_USER_GROUP',0,'Пункт \"Добавить в группу\" в к. меню',1,1),(555,'2016-01-23 14:46:09',0x00,'SECURITY_POLICY_USERS_DELETE_USER',0,'Пункт \"Удалить\" в контекстном меню',1,1),(556,'2016-01-23 14:46:09',0x00,'SECURITY_POLICY_USERS_SEND_SMS',0,'Пункт \"Отправить СМС\" в контекстном меню',1,1),(557,'2016-01-23 14:46:09',0x00,'SECURITY_POLICY_USERS_USERS_PROFILE',0,'Пункт \"Профиль\" в контекстном меню',1,1),(558,'2016-01-23 14:46:09',0x00,'SECURITY_POLICY_USER_GROUPS_ADD_SECURITY_POLICIES',0,'Пункт \"Добавить права доступа\" в к. меню',1,1),(559,'2016-01-23 14:46:09',0x00,'SECURITY_POLICY_USER_GROUPS_ADD_USERS',0,'Пункт \"Добавить пользователей\" в к. меню',1,1),(560,'2016-01-23 14:46:09',0x00,'SECURITY_POLICY_USER_GROUPS_ADD_USER_GROUP',0,'Кнопка \"Добавить группу пользователей\"',1,1),(561,'2016-01-23 14:46:09',0x00,'SECURITY_POLICY_USER_GROUPS_DELETE_USER_GROUP',0,'Пункт \"Удалить\" в контекстном меню',1,1),(562,'2016-01-23 14:46:09',0x00,'SECURITY_POLICY_USER_GROUPS_EDIT_USER_GROUP',0,'Пункт \"Редактировать\" в контекстном меню',1,1),(563,'2016-01-23 14:46:09',0x00,'SECURITY_PROFILE_GROUP_DESCRIPTION',0,'Страница \"Профиль\"',1,1),(564,'2016-01-23 14:46:09',0x00,'SECURITY_ROOMS_GROUP_DESCRIPTION',0,'Страница \"Кабинеты\"',1,1),(565,'2016-01-23 14:46:09',0x00,'SECURITY_SCHEDULE_GROUP_DESCRIPTION',0,'Страницы расписания приемов',1,1),(566,'2016-01-23 14:46:09',0x00,'SECURITY_SMS_TEMPLATES_GROUP_DESCRIPTION',0,'Страница CMC шаблоны',1,1),(567,'2016-01-23 14:46:09',0x00,'SECURITY_SYSTEM_DESCRIPTION',0,'Системные права',1,1),(568,'2016-01-23 14:46:09',0x00,'SECURITY_USERS_GROUP_DESCRIPTION',0,'Страница \"Пользователи\"',1,1),(569,'2016-01-23 14:46:09',0x00,'SECURITY_USER_GROUPS_GROUP_DESCRIPTION',0,'Страница \"Группы пользователей\"',1,1),(570,'2016-01-23 14:46:09',0x00,'SECURITY_VIOLATION_MESSAGE_END',0,'не имеет доступа к защищенному материалу',1,1),(571,'2016-01-23 14:46:09',0x00,'SECURITY_VIOLATION_MESSAGE_START',0,'Пользователь',1,1),(572,'2016-01-23 14:46:09',0x00,'SECURITY_VIOLATION_MESSAGE_TITLE',0,'Отказано в доступе',1,1),(573,'2016-01-23 18:34:07',0x00,'APPLICATION_AVATAR_IMAGES_PATH',2,'error.png',NULL,1),(574,'2016-01-23 18:34:07',0x00,'APPLICATION_ERROR_ICON',2,'default_male_avatar.png<',NULL,1),(575,'2016-01-23 18:34:07',0x00,'APPLICATION_IMAGES_PATH',2,'/resources/images/',NULL,1),(576,'2016-02-24 17:39:26',0x00,'APPLICATION_LOGO',2,'/logo.png',NULL,1),(577,'2016-01-23 18:34:07',0x00,'APPLICATION_ROOT_PATH',2,'/',NULL,1),(578,'2016-01-23 18:34:07',0x00,'AVATAR_CROP_DIALOG',2,'/private/crop_image.xhtml',NULL,1),(579,'2016-01-23 18:34:07',0x00,'DELIVERY_GROUPS_PAGE',2,'/private/delivery_groups.xhtml',NULL,1),(580,'2016-01-23 18:34:07',0x00,'EMAIL_TEMPLATES_PAGE',2,'/private/message_templates.xhtml?EMAIL=true',NULL,1),(581,'2016-01-23 18:34:07',0x00,'LOGIN_PAGE',2,'/login.xhtml',NULL,1),(582,'2016-01-23 18:34:07',0x00,'MAIN_PAGE',2,'/private/index.xhtml',NULL,1),(583,'2016-01-23 18:34:07',0x00,'MESSAGE_SCHEDULER_PAGE',2,'/private/message_scheduler.xhtml',NULL,1),(584,'2016-01-23 18:34:07',0x00,'METHODS_PAGE',2,'/private/methods.xhtml',NULL,1),(585,'2016-01-23 18:34:07',0x00,'PAYMENTS_PAGE',2,'/private/payments.xhtml',NULL,1),(586,'2016-01-23 18:34:07',0x00,'PERMISSION_DENIED_PAGE',2,'/access_denied.xhtml',NULL,1),(587,'2016-01-23 18:34:07',0x00,'PLAN_GENERAL_PAGE',2,'/private/plan_general.xhtml',NULL,1),(588,'2016-01-23 18:34:07',0x00,'PLAN_PAGE',2,'/private/plan.xhtml',NULL,1),(589,'2016-01-23 18:34:07',0x00,'REGISTER_PAGE',2,'/register.xhtml',NULL,1),(590,'2016-01-23 18:34:07',0x00,'ROOMS_PAGE',2,'/private/rooms.xhtml',NULL,1),(591,'2016-01-23 18:34:07',0x00,'SCHEDULE_GENERAL_PAGE',2,'/private/schedule_general.xhtml',NULL,1),(592,'2016-01-23 18:34:07',0x00,'SCHEDULE_PAGE',2,'/private/schedule.xhtml',NULL,1),(593,'2016-01-23 18:34:07',0x00,'SCHEDULE_PERSONAL_PAGE',2,'/private/schedule_doctor.xhtml',NULL,1),(594,'2016-01-23 18:34:07',0x00,'SEND_SMS_PAGE',2,'/private/send_sms.xhtml',NULL,1),(595,'2016-01-23 18:34:07',0x00,'SMS_TEMPLATES_PAGE',2,'/private/message_templates.xhtml?SMS=true',NULL,1),(596,'2016-01-23 18:34:07',0x00,'USERS_PAGE',2,'/private/users.xhtml',NULL,1),(597,'2016-01-23 18:34:07',0x00,'USER_ADD_PAGE',2,'/private/add_user.xhtml',NULL,1),(598,'2016-01-23 18:34:07',0x00,'USER_PROFILE_PAGE',2,'/private/profile.xhtml',NULL,1),(599,'2016-01-23 18:34:07',0x00,'USER_TYPES_PAGE',2,'/private/user_groups.xhtml',NULL,1),(600,'2016-02-07 21:30:36',0x00,'MENU_HEADER',0,'Main Menu',2,1),(601,'2016-02-08 16:36:44',0x00,'MENU_HEADER_FINANCE',0,'Finances',2,1),(602,'2016-02-13 14:57:10',0x00,'ADDRESS_ADDRESS',0,'Address',2,1),(603,'2016-02-10 18:17:57',0x00,'APPLICATION_NO',0,'No',2,1),(604,'2016-02-10 18:58:10',0x00,'LOCALIZATION_ADD_LOCALE_BUTTON',0,'Добавить язык',1,1),(605,'2016-02-10 18:58:35',0x00,'LOCALIZATION_ADD_MESSAGE_BUTTON',0,'Добавить константу',1,1),(606,'2016-02-10 19:00:09',0x00,'LOCALIZATION_MESSAGE_SAVE_ERROR',0,'Константа не сохранена',1,1),(607,'2016-02-10 19:00:32',0x00,'LOCALIZATION_MESSAGE_SAVE_SUCCESS',0,'Константа успешно сохранена',1,1),(608,'2016-02-10 19:06:14',0x00,'LOCALIZATION_MESSAGE_ADD_ERROR_DUPLICATION',0,'Данный ключ уже существует в системе',1,1),(609,'2016-02-10 19:07:10',0x00,'LOCALIZATION_NEW_MESSAGE_CREATE_SUCCESS',0,'Новая константа успешно добавлена',1,1),(610,'2016-02-10 19:08:01',0x00,'MESSAGE_BUNDLE_MESSAGE_KEY',0,'Ключ',1,1),(611,'2016-02-10 19:08:22',0x00,'MESSAGE_BUNDLE_MESSAGE_VALUE',0,'Значение',1,1),(612,'2016-02-10 19:08:51',0x00,'LOCALIZATION_ADD_LOCALE_DIALOG_TITLE',0,'Добавление нового языка',1,1),(613,'2016-02-10 19:10:38',0x00,'LOCALE_LANGUAGE_CODE',0,'Код языка в стандарте  ISO 639-1',1,1),(614,'2016-02-10 19:14:06',0x00,'LOCALE_COUNTRY_CODE',0,'Код страны в стандарте ISO 3166 2(символа)',1,1),(615,'2016-02-10 19:14:40',0x00,'LOCALE_LANGUAGE',0,'Название языка',1,1),(616,'2016-02-10 19:15:38',0x00,'LOCALE_LANGUDAGE_NATIVE',0,'Название на языке оригинала',1,1),(617,'2016-02-10 19:17:41',0x00,'SECURITY_POLICY_LOCALIZATION_ADD_LOCALE',0,'Кнопка \"Добавить язык\"',1,1),(618,'2016-02-10 19:18:18',0x00,'SECURITY_POLICY_LOCALIZATION_ADD_CONSTANT',0,'Кнопка \"Добавить константу\"',1,1),(619,'2016-02-10 19:18:44',0x00,'LOCALIZATION_ADD_MESSAGE_DIALOG_TITLE',0,'Добавление новой константы',1,1),(620,'2016-02-10 19:18:44',0x00,'SECURITY_LOCALIZATION_GROUP_DESCRIPTION',0,'Страница \"Локализации\"',1,1),(621,'2016-02-10 19:18:44',0x00,'SECURITY_POLICY_LOCALIZATION_EDIT_CONSTANT',0,'Редактирование константы',1,1),(622,'2016-02-10 19:18:44',0x00,'LOCALIZATION_PAGE_TITLE',0,'Локализации',1,1),(623,'2016-02-10 19:18:44',0x00,'LOCALIZATION_NEW_LOCALE_CREATE_SUCCESS',0,'Новый язык успешно добавлен',1,1),(624,'2016-02-08 16:36:44',0x00,'MENU_ITEM_LOCALIZATION',0,'Локализация',1,1),(625,'2016-02-08 16:36:44',0x00,'SECURITY_POLICY_MENU_ITEM_LOCALIZATION',0,'Пункт меню \"Локализация\"',1,1),(626,'2016-01-23 18:34:07',0x00,'LOCALIZATION_PAGE',2,'/private/localization.xhtml',NULL,1),(627,'2016-02-08 16:36:44',0x00,'MENU_HEADER_SETTINGS',0,'Настройки',1,1),(639,'2016-02-13 15:02:16',0x00,'MENU_ITEM_MAIN',0,'Main Page',2,1),(640,'2016-02-13 15:41:06',0x00,'MENU_ITEM_PERSONAL_SCHEDULE',0,'My schedule',2,1),(641,'2016-02-13 15:50:12',0x00,'MENU_ITEM_USERS',0,'Users',2,1),(642,'2016-02-13 16:11:57',0x00,'MENU_ITEM_USER_TYPES',0,'User groups',2,1),(643,'2016-02-13 16:58:22',0x00,'MENU_ITEM_ROOMS',0,'Rooms',2,1),(644,'2016-02-13 16:59:30',0x00,'MENU_ITEM_METHODS',0,'Research Methods',2,1),(645,'2016-02-14 11:13:55',0x00,'INDEX_MONTH_STATISTICS',0,'Month statistics',2,1),(646,'2016-02-08 16:36:44',0x00,'SECURITY_POLICY_MENU_ITEM_SETTINGS',0,'Пункт меню \"Настройки\"',1,1),(647,'2016-02-08 16:36:44',0x00,'SECURITY_POLICY_SETTINGS_MDS_TAB',0,'Вкладка \"Настройка системы рассылок\"',1,1),(648,'2016-02-08 16:36:44',0x00,'SECURITY_POLICY_SETTINGS_MDS_SAVE',0,'Возможность редактировать значения на вкладке \"Система рассылок\"',1,1),(649,'2016-02-08 16:36:44',0x00,'SECURITY_SETTINGS_GROUP_DESCRIPTION',0,'Страница \"Настройки\"',1,1),(650,'2016-02-08 16:36:44',0x00,'SETTINGS_MDS_TAB_TITLE',0,'Система рассылок',1,1),(651,'2016-02-08 16:36:44',0x00,'APPLICATION_SAVE_BUTTON',0,'Сохранить',1,1),(652,'2016-02-08 16:36:44',0x00,'MDS_SMTP_SERVER',0,'SMTP Сервер',1,1),(653,'2016-01-23 18:34:07',0x00,'SETTINGS_PAGE',2,'/private/settings.xhtml',NULL,1),(654,'2016-01-23 18:34:07',0x00,'SETTINGS_MDS_CONSTANTS_SAVE_SUCCESS',0,'Настройки системы рассылок успешно сохранены',1,1),(655,'2016-02-20 10:16:13',0x00,'MDS_SMTP_PORT',0,'SMTP порт',1,1),(656,'2016-02-20 11:09:22',0x00,'MDS_SMTP_AUTH',0,'SMTP авторизация',1,1),(657,'2016-02-20 11:17:25',0x00,'MDS_SMTP_LOGIN',0,'SMTP логин',1,1),(658,'2016-02-20 11:17:52',0x00,'MDS_SMTP_PASSWORD',0,'SMTP пароль',1,1),(659,'2016-02-20 11:21:21',0x00,'MDS_SMTP_SSL',0,'SMTP SSL',1,1),(660,'2016-02-20 11:21:39',0x00,'MDS_SMTP_STARTTLS',0,'SMTP STARTTLS',1,1),(661,'2016-02-20 12:23:40',0x00,'MDS_SMS_TEMPLATE',0,'Шаблон для отправки СМС при записи пациента',1,1),(662,'2016-02-20 12:24:17',0x00,'MDS_EMAIL_TEMPLATE',0,'Шаблон для отправки EMAIL при записи пациента',1,1),(663,'2016-01-23 18:34:07',0x00,'MDS_MAXIMUM_CYRILLIC_SYMBOLS_IN_SMS',0,'Максимальное количество кириллических символов в одной смс',1,1),(664,'2016-01-23 18:34:07',0x00,'MDS_MAXIMUM_LATIN_SYMBOLS_IN_SMS',0,'Максимальное количество латинских символов в одной смс',1,1),(665,'2016-01-23 18:34:07',0x00,'MDS_EMAIL_SERVER',0,'Адрес с которого будет отсылаться почта',1,1),(666,'2016-01-23 18:34:07',0x00,'SETTINGS_APPLICATION_TAB_TITLE',0,'Настройки системы',1,1),(667,'2016-01-23 18:34:07',0x00,'SETTINGS_ASSISTANTS_GROUP',0,'Группа пользователей \"Ассистенты\"',1,1),(668,'2016-01-23 18:34:07',0x00,'SETTINGS_PATIENTS_GROUP',0,'Группа пользователей \"Пациенты\"',1,1),(669,'2016-01-23 18:34:07',0x00,'SETTINGS_DOCTORS_GROUP',0,'Группа пользователей \"Доктора\"',1,1),(670,'2016-01-23 18:34:07',0x00,'SETTINGS_SUPER_ADMIN_GROUP',0,'Группа пользователей \"Супер админ\"',1,1),(671,'2016-01-23 18:34:07',0x00,'SETTINGS_SUPER_ADMIN_USER',0,'Супер админ',1,1),(672,'2016-01-23 18:34:07',0x00,'SETTINGS_BREAK_USER',0,'Служебный пользователь для перерыва',1,1),(673,'2016-01-23 18:34:07',0x00,'SETTINGS_METHOD_BREAK',0,'Служебный метод для перерыва',1,1),(674,'2016-01-23 18:34:07',0x00,'SECURITY_POLICY_SETTINGS_APPLICATION_TAB',0,'Вкладка \"Настройка системы\"',1,1),(675,'2016-01-23 18:34:07',0x00,'SECURITY_POLICY_SETTINGS_APPLICATION_SAVE',0,'Возможность редактировать значения на вкладке \"Настройка системы\"',1,1),(676,'2016-01-23 18:34:07',0x00,'SECURITY_POLICY_SETTINGS_PATHS_TAB',0,'Вкладка \"Системные пути\"',1,1),(677,'2016-01-23 18:34:07',0x00,'SECURITY_POLICY_SETTINGS_PATHS_SAVE',0,'Возможность редактировать значения на вкладке \"Системные пути\"',1,1),(678,'2016-01-23 18:34:07',0x00,'SETTINGS_PATHS_TAB_TITLE',0,'Cистемные пути',1,1),(679,'2016-03-20 16:34:34',0x00,'SMS_GATEWAY',1,'https://api.life.com.ua/ip2sms/',NULL,1),(680,'2016-01-23 18:34:07',0x00,'SMS_GATEWAY_LOGIN',1,'cloud',NULL,1),(681,'2016-02-24 18:00:24',0x00,'SMS_GATEWAY_PASSWORD',1,'88Zcy7nmhzeoutmn',NULL,1),(682,'2016-01-23 18:34:07',0x00,'SMS_GATEWAY_ALPHA_NAME',1,'Doctor Vera',NULL,1),(683,'2016-01-23 18:34:07',0x00,'MDS_SMS_GATEWAY',0,'Смс шлюз',1,1),(684,'2016-01-23 18:34:07',0x00,'MDS_SMS_GATEWAY_ALPHA_NAME',0,'Альфа Имя',1,1),(685,'2016-01-23 18:34:07',0x00,'MDS_SMS_GATEWAY_LOGIN',0,'Логин от смс шлюза',1,1),(686,'2016-01-23 18:34:07',0x00,'MDS_SMS_GATEWAY_PASSWORD',0,'Пароль от смс шлюза',1,1),(687,'2016-01-23 18:34:07',0x00,'SECURITY_POLICY_SETTINGS_TEMPLATES_TAB',0,'Вкладка \"Документы\"',1,1),(688,'2016-01-23 18:34:07',0x00,'SECURITY_POLICY_SETTINGS_TEMPLATES_SAVE',0,'Возможность редактировать значения на вкладке \"Документы\"',1,1),(689,'2016-01-23 18:34:07',0x00,'SETTINGS_TEMPLATES_TAB_TITLE',0,'Документы',1,1),(690,'2016-01-23 18:34:07',0x00,'PERSONAL_DATA_SECURITY_TEMPLATE',1,'5',NULL,1),(691,'2016-01-23 18:34:07',0x00,'PERSONAL_DATA_FORM_TEMPLATE',1,'2',NULL,1),(692,'2016-01-23 18:34:07',0x00,'INCOME_FORM_TEMPLATE',1,'3',NULL,1),(693,'2016-01-23 18:34:07',0x00,'OUTCOME_FORM_TEMPLATE',1,'4',NULL,1),(694,'2016-01-23 18:34:07',0x00,'SETTINGS_PERSONAL_DATA_SECURITY_TEMPLATE',0,'Соглашения об обработке персональных данных  (Шаблон)',1,1),(695,'2016-01-23 18:34:07',0x00,'SETTINGS_PERSONAL_DATA_FORM_TEMPLATE',0,'Анкета пользователя  (Шаблон)',1,1),(696,'2016-01-23 18:34:07',0x00,'SETTINGS_INCOME_FORM_TEMPLATE',0,'Приходная накладная (Шаблон)',1,1),(697,'2016-01-23 18:34:07',0x00,'SETTINGS_OUTCOME_FORM_TEMPLATE',0,'Расходная накладная (Шаблон)',1,1),(698,'2016-01-23 18:34:07',0x00,'APPLICATION_UPLOAD',0,'Загрузить на сервер',1,1),(699,'2016-01-23 18:34:07',0x00,'APPLICATION_DOWNLOAD',0,'Открыть',1,1),(701,'2016-01-23 18:34:07',0x00,'FINANCIAL_SETTINGS_TITLE',0,'Финансовые Настройки',1,1),(702,'2016-01-23 18:34:07',0x00,'FINANCIAL_SETTINGS_CREATE_BUTTON',0,'Создать конфигурацию',1,1),(703,'2016-01-23 18:34:07',0x00,'FINANCIAL_SETTINGS_ADD_DIALOG_TITLE',0,'Создание новой конфигурации',1,1),(704,'2016-01-23 18:34:07',0x00,'FINANCIAL_SETTINGS_METHODS_SOURCE',0,'Все методы исследований',1,1),(705,'2016-01-23 18:34:07',0x00,'FINANCIAL_SETTINGS_METHODS_TARGET',0,'Выбранные методы',1,1),(706,'2016-01-23 18:34:07',0x00,'SHARE_DOCTORS_PART',0,'Доля доктора',1,1),(707,'2016-01-23 18:34:07',0x00,'SHARE_ASSISTANTS_PART',0,'Доля ассистента',1,1),(708,'2016-01-23 18:34:07',0x00,'SHARE_DATE',0,'Дата с которой начнет действовать',1,1),(709,'2016-01-23 18:34:07',0x00,'SHARE_PART_TYPE',0,'Тип',1,1),(710,'2016-01-23 18:34:07',0x00,'APPLICATION_PERCENTS',0,'%',1,1),(711,'2016-01-23 18:34:07',0x00,'SHARE_METHODS',0,'Методы',1,1),(712,'2016-01-23 18:34:07',0x00,'SHARE_DOCTORS',0,'Доктора',1,1),(713,'2016-01-23 18:34:07',0x00,'SHARE_ASSISTANTS',0,'Ассистенты',1,1),(714,'2016-01-23 18:34:07',0x00,'FINANCIAL_SETTINGS_PAGE',2,'/private/financial_settings.xhtml',NULL,1),(715,'2016-01-23 18:34:07',0x00,'MENU_ITEM_FINANCIAL_SETTINGS',0,'Финансовые Настройки',1,1),(716,'2016-01-23 14:46:09',0x00,'SECURITY_FINANCIAL_SETTINGS_GROUP_DESCRIPTION',0,'Страница Финансовые Настройки',1,1),(717,'2016-01-23 14:46:09',0x00,'SECURITY_POLICY_FINANCIAL_SETTINGS_CREATE',0,'Кнопка \"Добавить конфигурацию\"',1,1),(718,'2016-01-23 14:46:09',0x00,'SECURITY_POLICY_FINANCIAL_SETTINGS_READ',0,'Доступ к данным',1,1),(719,'2016-01-23 14:46:09',0x00,'SECURITY_POLICY_FINANCIAL_SETTINGS_UPDATE',0,'Возможность изменить конфигурацию',1,1),(720,'2016-01-23 14:46:09',0x00,'SECURITY_POLICY_FINANCIAL_SETTINGS_DELETE',0,'Возможность удалить конфигурацию',1,1),(721,'2016-01-23 14:46:09',0x00,'FINANCIAL_SETTINGS_ADD_METHOD_SUCCESS',0,'Добавлены методы:',1,1),(722,'2016-01-23 14:46:09',0x00,'FINANCIAL_SETTINGS_REMOVE_METHOD_SUCCESS',0,'Удалены методы:',1,1),(723,'2016-01-23 14:46:09',0x00,'FINANCIAL_SETTINGS_ADD_DOCTOR_SUCCESS',0,'Добавлены доктора:',1,1),(724,'2016-01-23 14:46:09',0x00,'FINANCIAL_SETTINGS_REMOVE_DOCTOR_SUCCESS',0,'Удалены доктора:',1,1),(725,'2016-01-23 14:46:09',0x00,'FINANCIAL_SETTINGS_ADD_ASSISTANT_SUCCESS',0,'Добавлены ассистенты:',1,1),(726,'2016-01-23 14:46:09',0x00,'FINANCIAL_SETTINGS_REMOVE_ASSISTANT_SUCCESS',0,'Удалены ассистенты:',1,1),(727,'2016-01-23 18:34:07',0x00,'FINANCIAL_SETTINGS_REMOVED',0,'Настройка удалена',1,1),(728,'2016-01-23 18:34:07',0x00,'APPLICATION_MENU_ITEM_DELETE',0,'Удалить',1,1),(729,'2016-01-23 18:34:07',0x00,'APPLICATION_MENU_ITEM_EDIT',0,'Изменить',1,1),(730,'2016-01-23 18:34:07',0x00,'FINANCIAL_SETTINGS_SAVE_BUTTON',0,'Сохранить настройку',1,1),(731,'2016-01-23 14:46:09',0x00,'FINANCIAL_SETTINGS_DOCTORS_SOURCE',0,'Все доктора:',1,1),(732,'2016-01-23 14:46:09',0x00,'FINANCIAL_SETTINGS_DOCTORS_TARGET',0,'Выбранные доктора:',1,1),(733,'2016-01-23 14:46:09',0x00,'FINANCIAL_SETTINGS_ASSISTANTS_SOURCE',0,'Все ассистенты:',1,1),(734,'2016-01-23 14:46:09',0x00,'FINANCIAL_SETTINGS_ASSISTANTS_TARGET',0,'Выбранные ассистенты:',1,1),(735,'2016-01-23 18:34:07',0x00,'APPLICATION_ATTENTION',0,'Внимание',1,1),(736,'2016-01-23 14:46:09',0x00,'FINANCIAL_SETTINGS_DELETE_CONFIRM_MESSAGE',0,'Вы действительно хотите удалить настройку?',1,1),(737,'2016-01-23 18:34:07',0x00,'SCHEDULE_PATIENT',0,'Пациент',1,1),(738,'2016-01-23 18:34:07',0x00,'SALARY_COST',0,'Цена',1,1),(739,'2016-01-23 18:34:07',0x00,'SALARY_PAID',0,'Оплачено',1,1),(740,'2016-01-23 18:34:07',0x00,'SALARY_DOCTOR',0,'Доктору',1,1),(741,'2016-01-23 18:34:07',0x00,'SALARY_ASSISTANT',0,'Ассистенту',1,1),(742,'2016-01-23 18:34:07',0x00,'SALARY_DOCTOR_DIRECTED',0,'Доктору Н',1,1),(743,'2016-01-23 18:34:07',0x00,'SALARY_CENTER',0,'В центр',1,1),(744,'2016-01-23 18:34:07',0x00,'SALARY_FROM',0,'От',1,1),(745,'2016-01-23 18:34:07',0x00,'SALARY_TO',0,'До',1,1),(746,'2016-01-23 18:34:07',0x00,'EMPLOYEE_USER_GROUP_ID',1,'3',NULL,1),(747,'2016-01-23 18:34:07',0x00,'SALARY_CALCULATE',0,'Расчитать',1,1),(748,'2016-01-23 18:34:07',0x00,'SALARY_TOTAL',0,'Всего',1,1),(749,'2016-01-23 18:34:07',0x00,'SALARY_TITLE',0,'Финансовый расчет',1,1),(750,'2016-01-23 18:34:07',0x00,'SECURITY_SALARY_GROUP_DESCRIPTION',0,'Страница \"Финансовый расчет\"',1,1),(751,'2016-01-23 18:34:07',0x00,'SECURITY_POLICY_SALARY_UPDATE',0,'Пункт \"Редактировать\" в контекстном меню',1,1),(752,'2016-01-23 18:34:07',0x00,'SECURITY_POLICY_SALARY_DELETE',0,'Пункт \"Удалить\" в контекстном меню',1,1),(753,'2016-01-23 14:46:09',0x00,'SECURITY_POLICY_MENU_ITEM_SALARY',0,'Пункт меню \"Финансовый расчет\"',1,1),(754,'2016-01-23 14:46:09',0x00,'MENU_ITEM_SALARY',0,'Финансовый расчет',1,1),(755,'2016-01-23 18:34:07',0x00,'SALARY_PAGE',2,'/private/salary.xhtml',NULL,1),(756,'2016-01-23 14:46:09',0x00,'SALARY_PAYMENT_FORM_DESCRIPTION_START',0,'Зар. плата ',1,1),(757,'2016-01-23 14:46:09',0x00,'SALARY_PAYMENT_FORM_DESCRIPTION_END',0,'За приемы',1,1),(762,'2016-01-23 14:46:09',0x00,'SALARY_NO_EMPLOYEE_SELECTED',0,'Выберите даты и работника для выдачи зар. платы',1,1),(763,'2016-01-23 14:46:09',0x00,'SALARY_CONFIGURATION_NOT_FOUND',0,'Проверьте финансовую конфигурацию для данного работника',1,1),(764,'2016-01-23 14:46:09',0x00,'SALARY_ADD_PAYMENT',0,'Провести платеж',1,1),(765,'2016-01-23 14:46:09',0x00,'SALARY_NO_EMPLOYEE_SELECTED',0,'Выберите даты и работника для выдачи зар. платы',1,1),(766,'2016-01-23 14:46:09',0x00,'SALARY_CONFIGURATION_NOT_FOUND',0,'Проверьте финансовую конфигурацию для данного работника',1,1),(767,'2016-01-23 14:46:09',0x00,'SALARY_ADD_PAYMENT',0,'Провести платеж',1,1),(768,'2016-01-23 18:34:07',0x00,'SCHEDULE_PAYMENT_DESCRIPTION_START',0,'За исследование',1,1),(769,'2016-01-23 18:34:07',0x00,'SCHEDULE_PAYMENT_DESCRIPTION_END',0,'.',1,1),(770,'2016-01-23 18:34:07',0x00,'SCHEDULE_METHOD_NOT_CHOSEN',0,'Выберите метод исследования',1,1),(771,'2016-01-23 14:46:09',0x00,'SCHEDULE_PAYMENT_DESCRIPTION',0,'Оплата за исследование',1,1),(772,'2016-03-13 17:04:07',0x00,'SERVICE_METHOD_TYPE_ID',1,'1',NULL,1),(773,'2016-01-23 14:46:09',0x00,'SETTINGS_EMPLOYEE_USER_GROUP',0,'Группа пользователей \"Персонал\"',1,1),(774,'2016-01-23 14:46:09',0x00,'SETTINGS_SERVICE_METHOD_TYPE',0,'Тип исследований \"Служебные\"',1,1),(775,'2016-01-23 14:46:09',0x00,'SECURITY_POLICY_MENU_ITEM_FINANCIAL_SETTINGS',0,'Пункт меню \"Финансовые настройки\"',1,1),(776,'2016-01-23 18:34:07',0x00,'STATISTICS_PAGE',2,'/private/statistics.xhtml',NULL,1),(777,'2016-01-23 14:46:09',0x00,'MENU_ITEM_STATISTICS',0,'Статистика',1,1),(778,'2016-01-23 14:46:09',0x00,'INDEX_WEATHER',0,'Погода сегодня',1,1),(779,'2016-01-23 14:46:09',0x00,'SECURITY_STATISTICS_GROUP_DESCRIPTION',0,'Пункт меню \"Статистика\"',1,1),(780,'2016-01-23 14:46:09',0x00,'SHARE_NOT_FOUND',0,'В разделе Финансовые настройки не найдена запись для приема №:',1,1),(781,'2016-01-23 14:46:09',0x00,'STATISTICS_PAGE_TITLE',0,'Статистика',1,1),(782,'2016-01-23 18:34:07',0x00,'STATISTICS_CALCULATE',0,'Показать статистику',1,1),(783,'2016-01-23 18:34:07',0x00,'STATISTICS_APPOINTMENTS',0,'Статистика приемов',1,1),(784,'2016-01-23 18:34:07',0x00,'STATISTICS_WORKING_DAYS_PER_PERIOD',0,'Рабочих дней за период',1,1),(785,'2016-01-23 18:34:07',0x00,'STATISTICS_SALARY',0,'Финансовая статистика',1,1),(786,'2016-01-23 18:34:07',0x00,'STATISTICS_SALARY_SUM',0,'Прибыль сотрудника',1,1),(787,'2016-01-23 18:34:07',0x00,'STATISTICS_TOTAL_PAID',0,'Прибыль c приемов за выбранный период',1,1),(788,'2016-01-23 18:34:07',0x00,'STATISTICS_CLINICS_INCOME',0,'Прибыль клиники c приемов',1,1),(789,'2016-01-23 18:34:07',0x00,'STATISTICS_TOTAL_PAID_AVERAGE_PER_DAY',0,'Средняя прибыль c приемов в день за период',1,1),(790,'2016-01-23 18:34:07',0x00,'STATISTICS_CLINICS_INCOME_AVERAGE_PER_DAY',0,'Средняя прибыль клиники  c приемов в день',1,1),(791,'2016-01-23 18:34:07',0x00,'STATISTICS_APPOINTMENTS_CHART_TITLE',0,'Приемы',1,1),(792,'2016-01-23 18:34:07',0x00,'STATISTICS_EMPLOYEE_SALARY_CHART_TITLE',0,'Прибыль выбранного сотрудника',1,1),(793,'2016-01-23 18:34:07',0x00,'STATISTICS_CLINICS_INCOME_CHART_TITLE',0,'Прибыль клиники c приемов',1,1),(794,'2016-01-23 18:34:07',0x00,'STATISTICS_TOTAL_INCOME_CHART_TITLE',0,'Полная прибыль c приемов',1,1),(795,'2016-01-23 18:34:07',0x00,'STATISTICS_METHODS_CHART_TITLE',0,'Методы исследований',1,1),(796,'2016-01-23 18:34:07',0x00,'STATISTICS_EMPLOYEE_PIE_CHART_TITLE',0,'Сотрудники',1,1),(797,'2016-01-23 18:34:07',0x00,'SCHEDULE_SEND_NOTIFICATION',0,'Отправить уведомление',1,1),(798,'2016-01-23 18:34:07',0x00,'SECURITY_POLICY_MENU_ITEM_STATISTICS',0,'Пункт меню \"Статистика\"',1,1),(799,'2016-01-23 18:34:07',0x00,'STATISTICS_FINANCIAL_FULL_BALANCE',0,'Баланс за все время',1,1),(800,'2016-01-23 18:34:07',0x00,'STATISTICS_FINANCIAL_PERIOD_BALANCE',0,'Баланс за выбранный период',1,1),(801,'2016-01-23 18:34:07',0x00,'STATISTICS_FINANCIAL_PERIOD_INCOME',0,'Прибыль за выбранный период',1,1),(802,'2016-01-23 18:34:07',0x00,'STATISTICS_FINANCIAL_PERIOD_OUTCOME',0,'Расходы за выбранный период',1,1),(803,'2016-01-23 18:34:07',0x00,'DELIVERY_LOGS_TITLE',0,'Лог рассылок',1,1),(804,'2016-01-23 18:34:07',0x00,'DELIVERY_LOG_ENTITY_MESSAGE_TEMPLATE',0,'Шаблон',1,1),(805,'2016-01-23 18:34:07',0x00,'DELIVERY_LOG_ENTITY_STATUS',0,'Статус',1,1),(806,'2016-01-23 18:34:07',0x00,'DELIVERY_LOG_ENTITY_RECIPIENTS_COUNT',0,'Получателей',1,1),(807,'2016-01-23 18:34:07',0x00,'DELIVERY_LOG_ENTITY_MESSAGE_TEMPLATE_TYPE',0,'Тип',1,1),(808,'2016-01-23 18:34:07',0x00,'DELIVERY_LOGS_PAGE',2,'/private/delivery_logs.xhtml',NULL,1),(809,'2016-01-23 18:34:07',0x00,'MENU_ITEM_DELIVERY_LOGS',0,'Лог рассылок',1,1),(810,'2016-01-23 18:34:07',0x00,'SECURITY_POLICY_MENU_ITEM_DELIVERY_LOGS',0,'Страница \"Лог рассылок\"',1,1),(812,'2016-01-23 18:34:07',0x00,'MESSAGE_LOG_USER',0,'Пользователь',1,1),(813,'2016-01-23 18:34:07',0x00,'MESSAGE_LOG_RECIPIENT',0,'Emal/Телефон',1,1),(819,'2016-01-23 18:34:07',0x00,'SETTINGS_DATABASE_TAB_TITLE',0,'Настройки Базы данных',1,1),(820,'2016-01-23 18:34:07',0x00,'SECURITY_POLICY_SETTINGS_DATABASE_TAB',0,'Вкладка \"Настройки Базы данных\"',1,1),(821,'2016-01-23 18:34:07',0x00,'SETTINGS_DATABASE_BACKUP',0,'Загрузить новый дамп Базы Данных',1,1),(822,'2016-01-23 18:34:07',0x00,'SETTINGS_DROP_DATABASE',0,'Очистить Базу Данных',1,1),(823,'2016-01-23 18:34:07',0x00,'SETTINGS_CREATE_NEW_DUMP',0,'Создать новый дамп',1,1),(824,'2016-01-23 18:34:07',0x00,'SETTINGS_GET_EXISTING_DUMP',0,'Скачать старый дамп',1,1),(825,'2016-01-23 18:34:07',0x00,'SETTINGS_DROP_DATABASE_CONFIRMATION',0,'Вы уверены что хотите уничтожить базу данных? Все данные будут безвозвратно утеряны!',1,1),(826,'2016-04-12 14:50:23',0x00,'SMS_MESSAGE_TEMPLATE_FOR_DOCTOR',1,'7',NULL,1),(827,'2016-04-12 14:50:29',0x00,'EMAIL_MESSAGE_TEMPLATE_FOR_DOCTOR',1,'8',NULL,1),(828,'2016-01-23 18:34:07',0x00,'MDS_DOCTORS_SMS_TEMPLATE',0,'Шаблон для рассылки sms уведомлений докторам',1,1),(829,'2016-01-23 18:34:07',0x00,'MDS_DOCTORS_EMAIL_TEMPLATE',0,'Шаблон для рассылки email уведомлений докторам',1,1);
/*!40000 ALTER TABLE `messagebundle` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `messagelog`
--

DROP TABLE IF EXISTS `messagelog`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `messagelog` (
  `MessageLogId` int(11) NOT NULL AUTO_INCREMENT,
  `DateCreated` datetime NOT NULL,
  `Deleted` bit(1) NOT NULL,
  `Details` varchar(200) DEFAULT NULL,
  `Status` int(11) NOT NULL,
  `UId` varchar(255) DEFAULT NULL,
  `Recipient` int(11) NOT NULL,
  `Transaction` int(11) NOT NULL,
  `UserCreated` int(11) NOT NULL,
  PRIMARY KEY (`MessageLogId`),
  KEY `FK_6ukx5ar7wegru8hrpvtijysuu` (`Recipient`),
  KEY `FK_dvg6kvq3jpqt2hdwbi6sq5mhk` (`Transaction`),
  KEY `FK_pq6w9e4y6mbneb4n0sup63d23` (`UserCreated`),
  CONSTRAINT `FK_6ukx5ar7wegru8hrpvtijysuu` FOREIGN KEY (`Recipient`) REFERENCES `users` (`UserId`),
  CONSTRAINT `FK_dvg6kvq3jpqt2hdwbi6sq5mhk` FOREIGN KEY (`Transaction`) REFERENCES `transactionlog` (`TransactionLogId`),
  CONSTRAINT `FK_pq6w9e4y6mbneb4n0sup63d23` FOREIGN KEY (`UserCreated`) REFERENCES `users` (`UserId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `messagelog`
--

LOCK TABLES `messagelog` WRITE;
/*!40000 ALTER TABLE `messagelog` DISABLE KEYS */;
/*!40000 ALTER TABLE `messagelog` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `messagescheduler`
--

DROP TABLE IF EXISTS `messagescheduler`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `messagescheduler` (
  `MessageSchedulerId` int(11) NOT NULL AUTO_INCREMENT,
  `DateCreated` datetime NOT NULL,
  `DateEnd` date NOT NULL,
  `DateStart` date NOT NULL,
  `Deleted` bit(1) NOT NULL,
  `Time` datetime NOT NULL,
  `MessageTemplate` int(11) NOT NULL,
  `User` int(11) DEFAULT NULL,
  `UserCreated` int(11) NOT NULL,
  PRIMARY KEY (`MessageSchedulerId`),
  KEY `FK_o8va07vrcgise0icn4rqmlwrn` (`MessageTemplate`),
  KEY `FK_la3lm4ibepiirdu633w4lm0fr` (`User`),
  KEY `FK_iu74us04y2gb007j4ktpssxas` (`UserCreated`),
  CONSTRAINT `FK_iu74us04y2gb007j4ktpssxas` FOREIGN KEY (`UserCreated`) REFERENCES `users` (`UserId`),
  CONSTRAINT `FK_la3lm4ibepiirdu633w4lm0fr` FOREIGN KEY (`User`) REFERENCES `users` (`UserId`),
  CONSTRAINT `FK_o8va07vrcgise0icn4rqmlwrn` FOREIGN KEY (`MessageTemplate`) REFERENCES `messagetemplate` (`MessageTemplateId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `messagescheduler`
--

LOCK TABLES `messagescheduler` WRITE;
/*!40000 ALTER TABLE `messagescheduler` DISABLE KEYS */;
/*!40000 ALTER TABLE `messagescheduler` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `messageschedulerhasdeliverygroup`
--

DROP TABLE IF EXISTS `messageschedulerhasdeliverygroup`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `messageschedulerhasdeliverygroup` (
  `MessageSchedulerId` int(11) NOT NULL,
  `DeliveryGroupId` int(11) NOT NULL,
  KEY `FK_48xi2dwhw85jgl7472ab4b3v8` (`DeliveryGroupId`),
  KEY `FK_5mgfjxlg7irn7jmu9wu7q35qo` (`MessageSchedulerId`),
  CONSTRAINT `FK_48xi2dwhw85jgl7472ab4b3v8` FOREIGN KEY (`DeliveryGroupId`) REFERENCES `deliverygroup` (`DeliveryGroupId`),
  CONSTRAINT `FK_5mgfjxlg7irn7jmu9wu7q35qo` FOREIGN KEY (`MessageSchedulerId`) REFERENCES `messagescheduler` (`MessageSchedulerId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `messageschedulerhasdeliverygroup`
--

LOCK TABLES `messageschedulerhasdeliverygroup` WRITE;
/*!40000 ALTER TABLE `messageschedulerhasdeliverygroup` DISABLE KEYS */;
/*!40000 ALTER TABLE `messageschedulerhasdeliverygroup` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `messagetemplate`
--

DROP TABLE IF EXISTS `messagetemplate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `messagetemplate` (
  `MessageTemplateId` int(11) NOT NULL AUTO_INCREMENT,
  `Content` longtext NOT NULL,
  `DateCreated` datetime NOT NULL,
  `Deleted` bit(1) NOT NULL,
  `Description` varchar(255) DEFAULT NULL,
  `Name` varchar(255) NOT NULL,
  `System` bit(1) NOT NULL,
  `Type` int(11) NOT NULL,
  `UserCreated` int(11) NOT NULL,
  PRIMARY KEY (`MessageTemplateId`),
  KEY `FK_9tviq1y6rswcb3fsmnq2do5rw` (`UserCreated`),
  CONSTRAINT `FK_9tviq1y6rswcb3fsmnq2do5rw` FOREIGN KEY (`UserCreated`) REFERENCES `users` (`UserId`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `messagetemplate`
--

LOCK TABLES `messagetemplate` WRITE;
/*!40000 ALTER TABLE `messagetemplate` DISABLE KEYS */;
INSERT INTO `messagetemplate` VALUES (1,'<div id=\"header\" style=\"\nbackground: -moz-linear-gradient(-45deg,  #006400 0%, #61c419 98%);\nbackground: -webkit-linear-gradient(-45deg,  #006400 0%,#61c419 98%);\nbackground: linear-gradient(135deg,  #006400 0%,#61c419 98%);\nfilter: progid:DXImageTransform.Microsoft.gradient( startColorstr=\'#006400\', endColorstr=\'#61c419\',GradientType=1 );\nwidth:100%; height:125px;\n-webkit-border-top-left-radius: 5px;\n-webkit-border-top-right-radius: 5px;\n-moz-border-radius-topleft: 5px;\n-moz-border-radius-topright: 5px;\nborder-top-left-radius: 5px;\nborder-top-right-radius: 5px;\n\"><div style=\"float:left\"><h1 style=\"color:gold; margin-left:50px;\">DoctorVera</h1></div>\n<div style=\"float:right;text-align:center\">\n	<span style=\"color:#ffcc33;\">www.doctorvera.kiev.ua<br>\n	info@doctorvera.kiev.ua</span><br>\n	<span style=\"color:darkgreen;\">Адрес:03037, Украина,<br>\n	&nbsp;г. Киев, ул. И. Клименка, 10/17<br>\n	Тел.: (044)332-86-17</span>\n</div>\n</div>\n<div style=\"color:#312E25;\">\n<p><span style=\"font-weight: bold;\">Добрый день, $usersFirstName $usersLastName!</span></p>\n<p>Вы были записаны на прием к доктору: $doctorsFirstName $doctorsLastName</p>\n<p>Прием назначен на $appointmentStartDate на $appointmentStartTime</p>\n<p>Исследование: $appointmentMethodName</p>\n<p>Стандартная стоимость: $appointmentMethodPrice грн.<br></p>\n</div><br>\n<div style=\"color:darkgreen; font-size:8pt\">\nДля того чтобы больше не получать эту рассылку <a style=\"color:gold;\" href=\"http://cloud.doctorvera.kiev.ua/unsubscribe.xhtml?email=$usersEmail?transactionId=$transactionId\">пройдите по ссылке\n</a></div>\n','2016-02-20 12:00:51',0x00,'При записи пациента на прием ему будет отправлено письмо с данным контентом','Стандартный шаблон при записи пациента',0x01,1,1),(2,'Добрый день, $usersFirstName $usersLastName! Вы были записаны на прием к доктору: $doctorsFirstName $doctorsLastName Прием назначен на $appointmentStartDate на $appointmentStartTime Исследование: $appointmentMethodName Стандартная стоимость: $appointmentMethodPrice грн.','2016-02-20 12:00:52',0x00,'При записи пациента на прием ему будет отправлена смс с данным контентом','Стандартный шаблон СМС при записи пациента',0x01,0,1);
/*!40000 ALTER TABLE `messagetemplate` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `methods`
--

DROP TABLE IF EXISTS `methods`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `methods` (
  `MethodId` int(11) NOT NULL AUTO_INCREMENT,
  `DateCreated` datetime NOT NULL,
  `Deleted` bit(1) NOT NULL,
  `FullDescription` longtext,
  `FullName` varchar(100) NOT NULL,
  `ShortDescription` varchar(100) DEFAULT NULL,
  `ShortName` varchar(45) NOT NULL,
  `TimeInMinutes` int(11) NOT NULL,
  `MethodType` int(11) NOT NULL,
  `UserCreated` int(11) NOT NULL,
  PRIMARY KEY (`MethodId`),
  KEY `FK_ren7dldijjhbe13cvnbtyqewu` (`MethodType`),
  KEY `FK_dbm6wfod0cfcct4vqbd8g7tur` (`UserCreated`),
  CONSTRAINT `FK_dbm6wfod0cfcct4vqbd8g7tur` FOREIGN KEY (`UserCreated`) REFERENCES `users` (`UserId`),
  CONSTRAINT `FK_ren7dldijjhbe13cvnbtyqewu` FOREIGN KEY (`MethodType`) REFERENCES `methodtypes` (`MethodTypeId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `methods`
--

LOCK TABLES `methods` WRITE;
/*!40000 ALTER TABLE `methods` DISABLE KEYS */;
/*!40000 ALTER TABLE `methods` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `methodtypes`
--

DROP TABLE IF EXISTS `methodtypes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `methodtypes` (
  `MethodTypeId` int(11) NOT NULL AUTO_INCREMENT,
  `DateCreated` datetime NOT NULL,
  `Deleted` bit(1) NOT NULL,
  `FullName` varchar(100) NOT NULL,
  `ShortName` varchar(25) NOT NULL,
  `UserCreated` int(11) NOT NULL,
  PRIMARY KEY (`MethodTypeId`),
  KEY `FK_k49aelc7kxgg6t62lbano2rlt` (`UserCreated`),
  CONSTRAINT `FK_k49aelc7kxgg6t62lbano2rlt` FOREIGN KEY (`UserCreated`) REFERENCES `users` (`UserId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `methodtypes`
--

LOCK TABLES `methodtypes` WRITE;
/*!40000 ALTER TABLE `methodtypes` DISABLE KEYS */;
/*!40000 ALTER TABLE `methodtypes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payments`
--

DROP TABLE IF EXISTS `payments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `payments` (
  `PaymentId` int(11) NOT NULL AUTO_INCREMENT,
  `DateCreated` datetime NOT NULL,
  `DateTime` datetime NOT NULL,
  `Deleted` bit(1) NOT NULL,
  `Description` varchar(200) DEFAULT NULL,
  `Total` float NOT NULL,
  `Recipient` int(11) DEFAULT NULL,
  `Schedule` int(11) DEFAULT NULL,
  `UserCreated` int(11) NOT NULL,
  PRIMARY KEY (`PaymentId`),
  KEY `FK_548q92yti31a5r4mybanigggf` (`Recipient`),
  KEY `FK_avmcigtqy6ywpq5k949mtb7hq` (`Schedule`),
  KEY `FK_mvgdvblvcgcy5iaylldt3m54u` (`UserCreated`),
  CONSTRAINT `FK_548q92yti31a5r4mybanigggf` FOREIGN KEY (`Recipient`) REFERENCES `users` (`UserId`),
  CONSTRAINT `FK_avmcigtqy6ywpq5k949mtb7hq` FOREIGN KEY (`Schedule`) REFERENCES `schedule` (`ScheduleId`),
  CONSTRAINT `FK_mvgdvblvcgcy5iaylldt3m54u` FOREIGN KEY (`UserCreated`) REFERENCES `users` (`UserId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payments`
--

LOCK TABLES `payments` WRITE;
/*!40000 ALTER TABLE `payments` DISABLE KEYS */;
/*!40000 ALTER TABLE `payments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `plan`
--

DROP TABLE IF EXISTS `plan`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `plan` (
  `PlanId` int(11) NOT NULL AUTO_INCREMENT,
  `DateCreated` datetime NOT NULL,
  `DateTimeEnd` datetime NOT NULL,
  `DateTimeStart` datetime NOT NULL,
  `Deleted` bit(1) NOT NULL,
  `Description` varchar(200) DEFAULT NULL,
  `Doctor` int(11) NOT NULL,
  `Room` int(11) NOT NULL,
  `UserCreated` int(11) NOT NULL,
  PRIMARY KEY (`PlanId`),
  KEY `FK_ib2klopo4qy661v1v4mw0dn4r` (`Doctor`),
  KEY `FK_70w3sm4pjvisuf82qr5c9ihm2` (`Room`),
  KEY `FK_beueyycr25akb8jmqyab2vmao` (`UserCreated`),
  CONSTRAINT `FK_70w3sm4pjvisuf82qr5c9ihm2` FOREIGN KEY (`Room`) REFERENCES `rooms` (`RoomId`),
  CONSTRAINT `FK_beueyycr25akb8jmqyab2vmao` FOREIGN KEY (`UserCreated`) REFERENCES `users` (`UserId`),
  CONSTRAINT `FK_ib2klopo4qy661v1v4mw0dn4r` FOREIGN KEY (`Doctor`) REFERENCES `users` (`UserId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `plan`
--

LOCK TABLES `plan` WRITE;
/*!40000 ALTER TABLE `plan` DISABLE KEYS */;
/*!40000 ALTER TABLE `plan` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `policy`
--

DROP TABLE IF EXISTS `policy`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `policy` (
  `PolicyId` int(11) NOT NULL AUTO_INCREMENT,
  `DateCreated` datetime NOT NULL,
  `Deleted` bit(1) NOT NULL,
  `Description` varchar(200) DEFAULT NULL,
  `PolicyGroup` varchar(100) NOT NULL,
  `StringId` varchar(255) NOT NULL,
  `UserCreated` int(11) NOT NULL,
  PRIMARY KEY (`PolicyId`),
  KEY `FK_2che4eujhvnvm01src6imjaiw` (`UserCreated`),
  CONSTRAINT `FK_2che4eujhvnvm01src6imjaiw` FOREIGN KEY (`UserCreated`) REFERENCES `users` (`UserId`)
) ENGINE=InnoDB AUTO_INCREMENT=105 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `policy`
--

LOCK TABLES `policy` WRITE;
/*!40000 ALTER TABLE `policy` DISABLE KEYS */;
INSERT INTO `policy` VALUES (1,'2016-04-14 19:27:22',0x00,NULL,'SYSTEM','EMPTY',1),(2,'2016-04-14 19:27:23',0x00,NULL,'MENU_GROUP','MENU_ITEM_MAIN',1),(3,'2016-04-14 19:27:23',0x00,NULL,'MENU_GROUP','MENU_ITEM_USERS',1),(4,'2016-04-14 19:27:23',0x00,NULL,'MENU_GROUP','MENU_ITEM_USER_GROUPS',1),(5,'2016-04-14 19:27:23',0x00,NULL,'MENU_GROUP','MENU_ITEM_ROOMS',1),(6,'2016-04-14 19:27:23',0x00,NULL,'MENU_GROUP','MENU_ITEM_SEND_SMS',1),(7,'2016-04-14 19:27:23',0x00,NULL,'MENU_GROUP','MENU_ITEM_METHODS',1),(8,'2016-04-14 19:27:23',0x00,NULL,'MENU_GROUP','MENU_ITEM_PAYMENTS',1),(9,'2016-04-14 19:27:23',0x00,NULL,'MENU_GROUP','MENU_ITEM_PLAN_GENERAL',1),(10,'2016-04-14 19:27:23',0x00,NULL,'MENU_GROUP','MENU_ITEM_PLAN',1),(11,'2016-04-14 19:27:23',0x00,NULL,'MENU_GROUP','MENU_ITEM_SCHEDULE_GENERAL',1),(12,'2016-04-14 19:27:23',0x00,NULL,'MENU_GROUP','MENU_ITEM_SCHEDULE',1),(13,'2016-04-14 19:27:23',0x00,NULL,'MENU_GROUP','MENU_ITEM_PERSONAL_SCHEDULE',1),(14,'2016-04-14 19:27:23',0x00,NULL,'MENU_GROUP','MENU_ITEM_DELIVERY_GROUPS',1),(15,'2016-04-14 19:27:23',0x00,NULL,'MENU_GROUP','MENU_ITEM_SMS_TEMPLATES',1),(16,'2016-04-14 19:27:23',0x00,NULL,'MENU_GROUP','MENU_ITEM_EMAIL_TEMPLATES',1),(17,'2016-04-14 19:27:23',0x00,NULL,'MENU_GROUP','MENU_ITEM_MESSAGE_SCHEDULER',1),(18,'2016-04-14 19:27:23',0x00,NULL,'MENU_GROUP','MENU_ITEM_LOCALIZATION',1),(19,'2016-04-14 19:27:23',0x00,NULL,'MENU_GROUP','MENU_ITEM_SETTINGS',1),(20,'2016-04-14 19:27:23',0x00,NULL,'MENU_GROUP','MENU_ITEM_FINANCIAL_SETTINGS',1),(21,'2016-04-14 19:27:23',0x00,NULL,'MENU_GROUP','MENU_ITEM_SALARY',1),(22,'2016-04-14 19:27:23',0x00,NULL,'MENU_GROUP','MENU_ITEM_STATISTICS',1),(23,'2016-04-14 19:27:23',0x00,NULL,'MENU_GROUP','MENU_ITEM_DELIVERY_LOGS',1),(24,'2016-04-14 19:27:23',0x00,NULL,'USERS_GROUP','USERS_ADD_USER',1),(25,'2016-04-14 19:27:23',0x00,NULL,'USERS_GROUP','USERS_DELETE_USER',1),(26,'2016-04-14 19:27:23',0x00,NULL,'USERS_GROUP','USERS_USERS_PROFILE',1),(27,'2016-04-14 19:27:23',0x00,NULL,'USERS_GROUP','USERS_ADD_USER_TO_USER_GROUP',1),(28,'2016-04-14 19:27:23',0x00,NULL,'USERS_GROUP','USERS_SEND_SMS',1),(29,'2016-04-14 19:27:23',0x00,NULL,'USER_GROUPS_GROUP','USER_GROUPS_ADD_USER_GROUP',1),(30,'2016-04-14 19:27:23',0x00,NULL,'USER_GROUPS_GROUP','USER_GROUPS_DELETE_USER_GROUP',1),(31,'2016-04-14 19:27:23',0x00,NULL,'USER_GROUPS_GROUP','USER_GROUPS_EDIT_USER_GROUP',1),(32,'2016-04-14 19:27:23',0x00,NULL,'USER_GROUPS_GROUP','USER_GROUPS_ADD_USERS',1),(33,'2016-04-14 19:27:23',0x00,NULL,'USER_GROUPS_GROUP','USER_GROUPS_ADD_SECURITY_POLICIES',1),(34,'2016-04-14 19:27:23',0x00,NULL,'MAIN_PAGE_GROUP','MAIN_PAGE_MONTH_SALARY',1),(35,'2016-04-14 19:27:23',0x00,NULL,'MAIN_PAGE_GROUP','MAIN_PAGE_MONTH_APPOINTMENTS',1),(36,'2016-04-14 19:27:23',0x00,NULL,'MAIN_PAGE_GROUP','MAIN_PAGE_YEAR_SALARY',1),(37,'2016-04-14 19:27:23',0x00,NULL,'MAIN_PAGE_GROUP','MAIN_PAGE_YEAR_APPOINTMENTS',1),(38,'2016-04-14 19:27:23',0x00,NULL,'ROOMS_GROUP','ROOMS_EDIT',1),(39,'2016-04-14 19:27:23',0x00,NULL,'ROOMS_GROUP','ROOMS_ADD',1),(40,'2016-04-14 19:27:23',0x00,NULL,'ROOMS_GROUP','ROOMS_DELETE',1),(41,'2016-04-14 19:27:23',0x00,NULL,'METHODS_GROUP','METHODS_ADD',1),(42,'2016-04-14 19:27:23',0x00,NULL,'METHODS_GROUP','METHODS_EDIT',1),(43,'2016-04-14 19:27:23',0x00,NULL,'METHODS_GROUP','METHODS_DELETE',1),(44,'2016-04-14 19:27:23',0x00,NULL,'METHODS_GROUP','METHODS_CHANGE_PRICE',1),(45,'2016-04-14 19:27:23',0x00,NULL,'METHODS_GROUP','METHODS_ADD_DOCTORS',1),(46,'2016-04-14 19:27:23',0x00,NULL,'PAYMENTS_GROUP','PAYMENTS_ADD',1),(47,'2016-04-14 19:27:23',0x00,NULL,'PROFILE_GROUP','PROFILE_EDIT_ANY_USER',1),(48,'2016-04-14 19:27:23',0x00,NULL,'PROFILE_GROUP','PROFILE_EDIT_AVATAR',1),(49,'2016-04-14 19:27:23',0x00,NULL,'PROFILE_GROUP','PROFILE_EDIT_FIRST_NAME',1),(50,'2016-04-14 19:27:23',0x00,NULL,'PROFILE_GROUP','PROFILE_EDIT_MIDDLE_NAME',1),(51,'2016-04-14 19:27:23',0x00,NULL,'PROFILE_GROUP','PROFILE_EDIT_LAST_NAME',1),(52,'2016-04-14 19:27:23',0x00,NULL,'PROFILE_GROUP','PROFILE_EDIT_BIRTH_DATE',1),(53,'2016-04-14 19:27:23',0x00,NULL,'PROFILE_GROUP','PROFILE_EDIT_LOGIN',1),(54,'2016-04-14 19:27:23',0x00,NULL,'PROFILE_GROUP','PROFILE_EDIT_PASSWORD',1),(55,'2016-04-14 19:27:23',0x00,NULL,'PROFILE_GROUP','PROFILE_EDIT_PHONE_NUMBER',1),(56,'2016-04-14 19:27:23',0x00,NULL,'PROFILE_GROUP','PROFILE_EDIT_ADDRESS',1),(57,'2016-04-14 19:27:23',0x00,NULL,'PROFILE_GROUP','PROFILE_EDIT_COLOR',1),(58,'2016-04-14 19:27:23',0x00,NULL,'PROFILE_GROUP','PROFILE_EDIT_EMAIL',1),(59,'2016-04-14 19:27:23',0x00,NULL,'PROFILE_GROUP','PROFILE_EDIT_MESSAGING_ACCEPTED',1),(60,'2016-04-14 19:27:23',0x00,NULL,'PLAN_GROUP','PLAN_CREATE',1),(61,'2016-04-14 19:27:23',0x00,NULL,'PLAN_GROUP','PLAN_READ',1),(62,'2016-04-14 19:27:23',0x00,NULL,'PLAN_GROUP','PLAN_UPDATE',1),(63,'2016-04-14 19:27:23',0x00,NULL,'PLAN_GROUP','PLAN_DELETE',1),(64,'2016-04-14 19:27:23',0x00,NULL,'SCHEDULE_GROUP','SCHEDULE_CREATE',1),(65,'2016-04-14 19:27:23',0x00,NULL,'SCHEDULE_GROUP','SCHEDULE_READ',1),(66,'2016-04-14 19:27:23',0x00,NULL,'SCHEDULE_GROUP','SCHEDULE_UPDATE',1),(67,'2016-04-14 19:27:23',0x00,NULL,'SCHEDULE_GROUP','SCHEDULE_DELETE',1),(68,'2016-04-14 19:27:23',0x00,NULL,'DELIVERY_GROUPS_GROUP','DELIVERY_GROUPS_ADD_USER_GROUP',1),(69,'2016-04-14 19:27:23',0x00,NULL,'DELIVERY_GROUPS_GROUP','DELIVERY_GROUPS_DELETE_USER_GROUP',1),(70,'2016-04-14 19:27:23',0x00,NULL,'DELIVERY_GROUPS_GROUP','DELIVERY_GROUPS_EDIT_USER_GROUP',1),(71,'2016-04-14 19:27:23',0x00,NULL,'DELIVERY_GROUPS_GROUP','DELIVERY_GROUPS_ADD_USERS',1),(72,'2016-04-14 19:27:23',0x00,NULL,'DELIVERY_GROUPS_GROUP','DELIVERY_GROUPS_ADD_SECURITY_POLICIES',1),(73,'2016-04-14 19:27:23',0x00,NULL,'SMS_TEMPLATES_GROUP','SMS_TEMPLATES_ADD',1),(74,'2016-04-14 19:27:23',0x00,NULL,'SMS_TEMPLATES_GROUP','SMS_TEMPLATES_DELETE',1),(75,'2016-04-14 19:27:23',0x00,NULL,'SMS_TEMPLATES_GROUP','SMS_TEMPLATES_UPDATE',1),(76,'2016-04-14 19:27:23',0x00,NULL,'SMS_TEMPLATES_GROUP','SMS_TEMPLATES_SEND',1),(77,'2016-04-14 19:27:23',0x00,NULL,'EMAIL_TEMPLATES_GROUP','EMAIL_TEMPLATES_ADD',1),(78,'2016-04-14 19:27:23',0x00,NULL,'EMAIL_TEMPLATES_GROUP','EMAIL_TEMPLATES_DELETE',1),(79,'2016-04-14 19:27:23',0x00,NULL,'EMAIL_TEMPLATES_GROUP','EMAIL_TEMPLATES_UPDATE',1),(80,'2016-04-14 19:27:23',0x00,NULL,'EMAIL_TEMPLATES_GROUP','EMAIL_TEMPLATES_SEND',1),(81,'2016-04-14 19:27:23',0x00,NULL,'MESSAGE_SCHEDULER_GROUP','MESSAGE_SCHEDULER_ADD',1),(82,'2016-04-14 19:27:23',0x00,NULL,'MESSAGE_SCHEDULER_GROUP','MESSAGE_SCHEDULER_DELETE',1),(83,'2016-04-14 19:27:23',0x00,NULL,'MESSAGE_SCHEDULER_GROUP','MESSAGE_SCHEDULER_UPDATE',1),(84,'2016-04-14 19:27:23',0x00,NULL,'LOCALIZATION_GROUP','LOCALIZATION_ADD_LOCALE',1),(85,'2016-04-14 19:27:23',0x00,NULL,'LOCALIZATION_GROUP','LOCALIZATION_ADD_CONSTANT',1),(86,'2016-04-14 19:27:23',0x00,NULL,'LOCALIZATION_GROUP','LOCALIZATION_EDIT_CONSTANT',1),(87,'2016-04-14 19:27:23',0x00,NULL,'SETTINGS_GROUP','SETTINGS_MDS_TAB',1),(88,'2016-04-14 19:27:23',0x00,NULL,'SETTINGS_GROUP','SETTINGS_MDS_SAVE',1),(89,'2016-04-14 19:27:23',0x00,NULL,'SETTINGS_GROUP','SETTINGS_APPLICATION_TAB',1),(90,'2016-04-14 19:27:23',0x00,NULL,'SETTINGS_GROUP','SETTINGS_APPLICATION_SAVE',1),(91,'2016-04-14 19:27:23',0x00,NULL,'SETTINGS_GROUP','SETTINGS_PATHS_TAB',1),(92,'2016-04-14 19:27:23',0x00,NULL,'SETTINGS_GROUP','SETTINGS_PATHS_SAVE',1),(93,'2016-04-14 19:27:23',0x00,NULL,'SETTINGS_GROUP','SETTINGS_TEMPLATES_TAB',1),(94,'2016-04-14 19:27:23',0x00,NULL,'SETTINGS_GROUP','SETTINGS_TEMPLATES_SAVE',1),(95,'2016-04-14 19:27:23',0x00,NULL,'SETTINGS_GROUP','SETTINGS_DATABASE_TAB',1),(96,'2016-04-14 19:27:23',0x00,NULL,'FINANCIAL_SETTINGS_GROUP','FINANCIAL_SETTINGS_CREATE',1),(97,'2016-04-14 19:27:23',0x00,NULL,'FINANCIAL_SETTINGS_GROUP','FINANCIAL_SETTINGS_READ',1),(98,'2016-04-14 19:27:23',0x00,NULL,'FINANCIAL_SETTINGS_GROUP','FINANCIAL_SETTINGS_UPDATE',1),(99,'2016-04-14 19:27:23',0x00,NULL,'FINANCIAL_SETTINGS_GROUP','FINANCIAL_SETTINGS_DELETE',1),(100,'2016-04-14 19:27:23',0x00,NULL,'SALARY_GROUP','SALARY_UPDATE',1),(101,'2016-04-14 19:27:23',0x00,NULL,'SALARY_GROUP','SALARY_DELETE',1),(102,'2016-04-14 19:27:23',0x00,NULL,'STATISTICS_GROUP','STATISTICS_FINANCIAL',1),(103,'2016-04-14 19:27:23',0x00,NULL,'STATISTICS_GROUP','STATISTICS_APPOINTMENTS',1),(104,'2016-04-14 19:27:23',0x00,NULL,'STATISTICS_GROUP','STATISTICS_METHODS',1);
/*!40000 ALTER TABLE `policy` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `policyhasusergroups`
--

DROP TABLE IF EXISTS `policyhasusergroups`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `policyhasusergroups` (
  `Policy` int(11) NOT NULL,
  `UserGroup` int(11) NOT NULL,
  KEY `FK_ed45tma5egjso9fhc5309ttxp` (`UserGroup`),
  KEY `FK_etbr1qimkst80qgpbw36rcm76` (`Policy`),
  CONSTRAINT `FK_ed45tma5egjso9fhc5309ttxp` FOREIGN KEY (`UserGroup`) REFERENCES `usergroups` (`UserGroupId`),
  CONSTRAINT `FK_etbr1qimkst80qgpbw36rcm76` FOREIGN KEY (`Policy`) REFERENCES `policy` (`PolicyId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `policyhasusergroups`
--

LOCK TABLES `policyhasusergroups` WRITE;
/*!40000 ALTER TABLE `policyhasusergroups` DISABLE KEYS */;
INSERT INTO `policyhasusergroups` VALUES (1,1),(2,1),(3,1),(4,1),(5,1),(6,1),(7,1),(8,1),(9,1),(10,1),(11,1),(12,1),(13,1),(14,1),(15,1),(16,1),(17,1),(18,1),(19,1),(20,1),(21,1),(22,1),(23,1),(24,1),(25,1),(26,1),(27,1),(28,1),(29,1),(30,1),(31,1),(32,1),(33,1),(34,1),(35,1),(36,1),(37,1),(38,1),(39,1),(40,1),(41,1),(42,1),(43,1),(44,1),(45,1),(46,1),(47,1),(48,1),(49,1),(50,1),(51,1),(52,1),(53,1),(54,1),(55,1),(56,1),(57,1),(58,1),(59,1),(60,1),(61,1),(62,1),(63,1),(64,1),(65,1),(66,1),(67,1),(68,1),(69,1),(70,1),(71,1),(72,1),(73,1),(74,1),(75,1),(76,1),(77,1),(78,1),(79,1),(80,1),(81,1),(82,1),(83,1),(84,1),(85,1),(86,1),(87,1),(88,1),(89,1),(90,1),(91,1),(92,1),(93,1),(94,1),(95,1),(96,1),(97,1),(98,1),(99,1),(100,1),(101,1),(102,1),(103,1),(104,1);
/*!40000 ALTER TABLE `policyhasusergroups` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prices`
--

DROP TABLE IF EXISTS `prices`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `prices` (
  `PriceId` int(11) NOT NULL AUTO_INCREMENT,
  `DateCreated` datetime NOT NULL,
  `DateTime` datetime DEFAULT NULL,
  `Deleted` bit(1) NOT NULL,
  `Total` float NOT NULL,
  `Method` int(11) NOT NULL,
  `UserCreated` int(11) NOT NULL,
  PRIMARY KEY (`PriceId`),
  KEY `FK_b6bb409qhqt4bhpgrgtdalpey` (`Method`),
  KEY `FK_rgsnubqgx78qro0vgnvpgq876` (`UserCreated`),
  CONSTRAINT `FK_b6bb409qhqt4bhpgrgtdalpey` FOREIGN KEY (`Method`) REFERENCES `methods` (`MethodId`),
  CONSTRAINT `FK_rgsnubqgx78qro0vgnvpgq876` FOREIGN KEY (`UserCreated`) REFERENCES `users` (`UserId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prices`
--

LOCK TABLES `prices` WRITE;
/*!40000 ALTER TABLE `prices` DISABLE KEYS */;
/*!40000 ALTER TABLE `prices` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rooms`
--

DROP TABLE IF EXISTS `rooms`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rooms` (
  `RoomId` int(11) NOT NULL AUTO_INCREMENT,
  `DateCreated` datetime NOT NULL,
  `Deleted` bit(1) NOT NULL,
  `Description` varchar(200) DEFAULT NULL,
  `Name` varchar(45) NOT NULL,
  `UserCreated` int(11) NOT NULL,
  PRIMARY KEY (`RoomId`),
  KEY `FK_qo2vc7ni3aqigl5tgkvd5uehc` (`UserCreated`),
  CONSTRAINT `FK_qo2vc7ni3aqigl5tgkvd5uehc` FOREIGN KEY (`UserCreated`) REFERENCES `users` (`UserId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rooms`
--

LOCK TABLES `rooms` WRITE;
/*!40000 ALTER TABLE `rooms` DISABLE KEYS */;
/*!40000 ALTER TABLE `rooms` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `schedule`
--

DROP TABLE IF EXISTS `schedule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `schedule` (
  `ScheduleId` int(11) NOT NULL AUTO_INCREMENT,
  `DateCreated` datetime NOT NULL,
  `DateTimeEnd` datetime NOT NULL,
  `DateTimeStart` datetime NOT NULL,
  `Deleted` bit(1) NOT NULL,
  `Description` varchar(200) DEFAULT NULL,
  `Assistant` int(11) DEFAULT NULL,
  `Doctor` int(11) NOT NULL,
  `DoctorDirected` int(11) DEFAULT NULL,
  `Method` int(11) NOT NULL,
  `ParentSchedule` int(11) DEFAULT NULL,
  `Patient` int(11) NOT NULL,
  `Room` int(11) NOT NULL,
  `UserCreated` int(11) NOT NULL,
  PRIMARY KEY (`ScheduleId`),
  KEY `FK_fup1s07cg72gbo6j3401ptogl` (`Assistant`),
  KEY `FK_87wj9hvu1v26q8n5canvaj48c` (`Doctor`),
  KEY `FK_2nc7tu5ni8wxdmrpjtgmg10vk` (`DoctorDirected`),
  KEY `FK_c45vs6hm9wsb1t9njqncj8jq` (`Method`),
  KEY `FK_h9bka29auhup9rcxe8x6y1j6i` (`ParentSchedule`),
  KEY `FK_84mxhiutauq87p8pixusb5us6` (`Patient`),
  KEY `FK_1d7hovca4kr6fn1a74ne5rcaj` (`Room`),
  KEY `FK_6fx9vo9nacw4u4rlkyngbbfgj` (`UserCreated`),
  CONSTRAINT `FK_1d7hovca4kr6fn1a74ne5rcaj` FOREIGN KEY (`Room`) REFERENCES `rooms` (`RoomId`),
  CONSTRAINT `FK_2nc7tu5ni8wxdmrpjtgmg10vk` FOREIGN KEY (`DoctorDirected`) REFERENCES `users` (`UserId`),
  CONSTRAINT `FK_6fx9vo9nacw4u4rlkyngbbfgj` FOREIGN KEY (`UserCreated`) REFERENCES `users` (`UserId`),
  CONSTRAINT `FK_84mxhiutauq87p8pixusb5us6` FOREIGN KEY (`Patient`) REFERENCES `users` (`UserId`),
  CONSTRAINT `FK_87wj9hvu1v26q8n5canvaj48c` FOREIGN KEY (`Doctor`) REFERENCES `users` (`UserId`),
  CONSTRAINT `FK_c45vs6hm9wsb1t9njqncj8jq` FOREIGN KEY (`Method`) REFERENCES `methods` (`MethodId`),
  CONSTRAINT `FK_fup1s07cg72gbo6j3401ptogl` FOREIGN KEY (`Assistant`) REFERENCES `users` (`UserId`),
  CONSTRAINT `FK_h9bka29auhup9rcxe8x6y1j6i` FOREIGN KEY (`ParentSchedule`) REFERENCES `schedule` (`ScheduleId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `schedule`
--

LOCK TABLES `schedule` WRITE;
/*!40000 ALTER TABLE `schedule` DISABLE KEYS */;
/*!40000 ALTER TABLE `schedule` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `schedulerhasdaysofweek`
--

DROP TABLE IF EXISTS `schedulerhasdaysofweek`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `schedulerhasdaysofweek` (
  `SchedulerId` int(11) NOT NULL,
  `DayOfWeekId` int(11) NOT NULL,
  KEY `FK_lu8m9ojj5wfu8d9tg4x0xvj5a` (`SchedulerId`),
  CONSTRAINT `FK_lu8m9ojj5wfu8d9tg4x0xvj5a` FOREIGN KEY (`SchedulerId`) REFERENCES `messagescheduler` (`MessageSchedulerId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `schedulerhasdaysofweek`
--

LOCK TABLES `schedulerhasdaysofweek` WRITE;
/*!40000 ALTER TABLE `schedulerhasdaysofweek` DISABLE KEYS */;
/*!40000 ALTER TABLE `schedulerhasdaysofweek` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `share`
--

DROP TABLE IF EXISTS `share`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `share` (
  `ShareId` int(11) NOT NULL AUTO_INCREMENT,
  `date` date NOT NULL,
  `DateCreated` datetime NOT NULL,
  `Deleted` bit(1) NOT NULL,
  `PercentageAssistant` float DEFAULT NULL,
  `PercentageDoctor` float DEFAULT NULL,
  `SalaryAssistant` float DEFAULT NULL,
  `SalaryDoctor` float DEFAULT NULL,
  `UserCreated` int(11) NOT NULL,
  PRIMARY KEY (`ShareId`),
  KEY `FK_sfcr30n2qawoyjeh0mpjwic0e` (`UserCreated`),
  CONSTRAINT `FK_sfcr30n2qawoyjeh0mpjwic0e` FOREIGN KEY (`UserCreated`) REFERENCES `users` (`UserId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `share`
--

LOCK TABLES `share` WRITE;
/*!40000 ALTER TABLE `share` DISABLE KEYS */;
/*!40000 ALTER TABLE `share` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sharehasassistants`
--

DROP TABLE IF EXISTS `sharehasassistants`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sharehasassistants` (
  `Share` int(11) NOT NULL,
  `Assistant` int(11) NOT NULL,
  KEY `FK_hr4b7vkss8kd98q5v1utioaj9` (`Assistant`),
  KEY `FK_qvu5d0nfqstrp5olryeestj1g` (`Share`),
  CONSTRAINT `FK_hr4b7vkss8kd98q5v1utioaj9` FOREIGN KEY (`Assistant`) REFERENCES `users` (`UserId`),
  CONSTRAINT `FK_qvu5d0nfqstrp5olryeestj1g` FOREIGN KEY (`Share`) REFERENCES `share` (`ShareId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sharehasassistants`
--

LOCK TABLES `sharehasassistants` WRITE;
/*!40000 ALTER TABLE `sharehasassistants` DISABLE KEYS */;
/*!40000 ALTER TABLE `sharehasassistants` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sharehasdoctors`
--

DROP TABLE IF EXISTS `sharehasdoctors`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sharehasdoctors` (
  `Share` int(11) NOT NULL,
  `Doctor` int(11) NOT NULL,
  KEY `FK_q753agiv9g2qdfivut8bxvlop` (`Doctor`),
  KEY `FK_eywe9envxh1l2st71916dcv7` (`Share`),
  CONSTRAINT `FK_eywe9envxh1l2st71916dcv7` FOREIGN KEY (`Share`) REFERENCES `share` (`ShareId`),
  CONSTRAINT `FK_q753agiv9g2qdfivut8bxvlop` FOREIGN KEY (`Doctor`) REFERENCES `users` (`UserId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sharehasdoctors`
--

LOCK TABLES `sharehasdoctors` WRITE;
/*!40000 ALTER TABLE `sharehasdoctors` DISABLE KEYS */;
/*!40000 ALTER TABLE `sharehasdoctors` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sharehasmethods`
--

DROP TABLE IF EXISTS `sharehasmethods`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sharehasmethods` (
  `Share` int(11) NOT NULL,
  `Method` int(11) NOT NULL,
  KEY `FK_imjpfv7yscaytxb928a95m9bj` (`Method`),
  KEY `FK_25crkiyqlb776iap47fo4695r` (`Share`),
  CONSTRAINT `FK_25crkiyqlb776iap47fo4695r` FOREIGN KEY (`Share`) REFERENCES `share` (`ShareId`),
  CONSTRAINT `FK_imjpfv7yscaytxb928a95m9bj` FOREIGN KEY (`Method`) REFERENCES `methods` (`MethodId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sharehasmethods`
--

LOCK TABLES `sharehasmethods` WRITE;
/*!40000 ALTER TABLE `sharehasmethods` DISABLE KEYS */;
/*!40000 ALTER TABLE `sharehasmethods` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `transactionlog`
--

DROP TABLE IF EXISTS `transactionlog`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `transactionlog` (
  `TransactionLogId` int(11) NOT NULL AUTO_INCREMENT,
  `DateCreated` datetime NOT NULL,
  `Deleted` bit(1) NOT NULL,
  `Details` varchar(255) DEFAULT NULL,
  `RecipientsCount` int(11) DEFAULT NULL,
  `Status` int(11) NOT NULL,
  `UId` varchar(255) DEFAULT NULL,
  `MessageTemplate` int(11) NOT NULL,
  `UserCreated` int(11) NOT NULL,
  PRIMARY KEY (`TransactionLogId`),
  KEY `FK_h0q0mjrq28gm412nt5ttb8pt2` (`MessageTemplate`),
  KEY `FK_6n102vystclt0ibkh8f0m3yhk` (`UserCreated`),
  CONSTRAINT `FK_6n102vystclt0ibkh8f0m3yhk` FOREIGN KEY (`UserCreated`) REFERENCES `users` (`UserId`),
  CONSTRAINT `FK_h0q0mjrq28gm412nt5ttb8pt2` FOREIGN KEY (`MessageTemplate`) REFERENCES `messagetemplate` (`MessageTemplateId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `transactionlog`
--

LOCK TABLES `transactionlog` WRITE;
/*!40000 ALTER TABLE `transactionlog` DISABLE KEYS */;
/*!40000 ALTER TABLE `transactionlog` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usergroups`
--

DROP TABLE IF EXISTS `usergroups`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `usergroups` (
  `UserGroupId` int(11) NOT NULL AUTO_INCREMENT,
  `DateCreated` datetime NOT NULL,
  `Deleted` bit(1) NOT NULL,
  `Description` varchar(200) DEFAULT NULL,
  `Name` varchar(45) NOT NULL,
  `UserCreated` int(11) NOT NULL,
  PRIMARY KEY (`UserGroupId`),
  KEY `FK_q72w5d8nw3r34jjw0nrr6ulq0` (`UserCreated`),
  CONSTRAINT `FK_q72w5d8nw3r34jjw0nrr6ulq0` FOREIGN KEY (`UserCreated`) REFERENCES `users` (`UserId`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usergroups`
--

LOCK TABLES `usergroups` WRITE;
/*!40000 ALTER TABLE `usergroups` DISABLE KEYS */;
INSERT INTO `usergroups` VALUES (1,'2016-02-04 10:43:46',0x00,'Эта группа пользователей имеет доступ ко всем ресурсам системы','Супер Админ',1),(2,'2016-02-04 10:43:46',0x00,'Эта группа пользователей не должна иметь доступ к страницам этого сайта','Пациенты',1),(3,'2016-03-19 08:36:26',0x00,'Эта группа пользователей идентифицирует пользователя, как работника медицинского центра','Персонал',1),(4,'2016-03-19 08:36:28',0x00,'Эта группа пользователей может вести прием в медицинском центре','Доктор',1),(5,'2016-03-06 12:40:39',0x00,'Эта группа пользователей может ассестировать Доктору во время приема','Ассистент',1),(6,'2016-02-04 10:43:46',0x00,'Эта группа пользователей может осуществлять прием и выдачу наличных средств, а также учет соответствующей документации','Кассир',1),(7,'2016-02-04 10:43:46',0x00,'Эта группа пользователей имеет доступ ко всей финансовой информации','Бухгалтер',1),(8,'2016-02-04 10:43:46',0x00,'Эта группа пользователей имеет полный доступ ко всем функциям системы!','Директор',1),(9,'2016-02-04 10:43:46',0x00,'Группа для обеспечения работы системы','Служебные',1);
/*!40000 ALTER TABLE `usergroups` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `UserId` int(11) NOT NULL AUTO_INCREMENT,
  `AvatarImage` longblob,
  `BirthDate` date DEFAULT NULL,
  `Color` varchar(150) NOT NULL DEFAULT 'ffffff',
  `DateCreated` datetime NOT NULL,
  `Deleted` bit(1) NOT NULL,
  `Description` varchar(150) DEFAULT NULL,
  `Email` varchar(45) DEFAULT NULL,
  `FirstName` varchar(45) NOT NULL,
  `Foreigner` bit(1) NOT NULL,
  `LastName` varchar(45) NOT NULL,
  `MessagingAccepted` bit(1) NOT NULL,
  `MiddleName` varchar(45) DEFAULT NULL,
  `Password` varchar(45) NOT NULL,
  `PhoneNumberHome` varchar(45) DEFAULT NULL,
  `PhoneNumberMobile` varchar(45) DEFAULT NULL,
  `Username` varchar(45) NOT NULL,
  `Address` int(11) DEFAULT NULL,
  `Locale` int(11) DEFAULT NULL,
  `UserCreated` int(11) DEFAULT NULL,
  PRIMARY KEY (`UserId`),
  KEY `FK_ikxg9koctpmqub6afe9tlal14` (`Address`),
  KEY `FK_ne5lv5wrdtarss8t9wepp7knw` (`Locale`),
  KEY `FK_o4wqeb9phjq02qo7eh4uqhxyr` (`UserCreated`),
  CONSTRAINT `FK_ikxg9koctpmqub6afe9tlal14` FOREIGN KEY (`Address`) REFERENCES `address` (`AddressId`),
  CONSTRAINT `FK_ne5lv5wrdtarss8t9wepp7knw` FOREIGN KEY (`Locale`) REFERENCES `locale` (`LocaleId`),
  CONSTRAINT `FK_o4wqeb9phjq02qo7eh4uqhxyr` FOREIGN KEY (`UserCreated`) REFERENCES `users` (`UserId`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,NULL,'1985-12-06','ffffff','2016-02-04 10:43:46',0x00,NULL,NULL,'Админ',0x00,'Админов',0x00,'Админович','61f22ec3c233b35b784fc2719d5cb0871abeefee',NULL,'+30635744511','root',1,1,1),(2,NULL,'2016-02-04','ffc400','2016-02-04 10:43:46',0x00,NULL,NULL,'Перерыв',0x00,'Бутербродник',0x00,'Кофеинов','f497806b3e565dbb6f0ab7e3431e3a115f186a64',NULL,'+382222222222','Buterbrodnik',1,1,1);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usershasusergroups`
--

DROP TABLE IF EXISTS `usershasusergroups`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `usershasusergroups` (
  `User` int(11) NOT NULL,
  `UserGroup` int(11) NOT NULL,
  KEY `FK_df26e0rgddgjd0d1fwcr5xf6u` (`UserGroup`),
  KEY `FK_68s9ited6gn962enlf1ucvfbl` (`User`),
  CONSTRAINT `FK_68s9ited6gn962enlf1ucvfbl` FOREIGN KEY (`User`) REFERENCES `users` (`UserId`),
  CONSTRAINT `FK_df26e0rgddgjd0d1fwcr5xf6u` FOREIGN KEY (`UserGroup`) REFERENCES `usergroups` (`UserGroupId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usershasusergroups`
--

LOCK TABLES `usershasusergroups` WRITE;
/*!40000 ALTER TABLE `usershasusergroups` DISABLE KEYS */;
INSERT INTO `usershasusergroups` VALUES (1,1),(2,9);
/*!40000 ALTER TABLE `usershasusergroups` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-04-15  9:54:48
