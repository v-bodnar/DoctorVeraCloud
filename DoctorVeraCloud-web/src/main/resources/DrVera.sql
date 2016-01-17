-- MySQL Script generated by MySQL Workbench
-- 01/17/16 15:59:45
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema DrVera
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema DrVera
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `DrVera` DEFAULT CHARACTER SET utf8 ;
USE `DrVera` ;

-- -----------------------------------------------------
-- Table `DrVera`.`Users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `DrVera`.`Users` (
  `UserId` INT(11) NOT NULL AUTO_INCREMENT,
  `Username` VARCHAR(45) NOT NULL,
  `Password` VARCHAR(45) NOT NULL,
  `FirstName` VARCHAR(45) NOT NULL,
  `MiddleName` VARCHAR(45) NULL,
  `LastName` VARCHAR(45) NOT NULL,
  `Address` INT(11) NULL,
  `BirthDate` DATE NULL DEFAULT NULL,
  `PhoneNumberHome` VARCHAR(45) NULL DEFAULT NULL,
  `PhoneNumberMobile` VARCHAR(45) NULL DEFAULT NULL COMMENT 'Describes User Entity',
  `Email` VARCHAR(255) NULL,
  `Color` VARCHAR(6) NOT NULL DEFAULT 'ffffff',
  `AvatarImage` BLOB NULL,
  `Description` VARCHAR(150) NULL,
  `Foreigner` TINYINT(1) NOT NULL DEFAULT '0',
  `MessagingAccepted` TINYINT(1) NOT NULL DEFAULT '0',
  `UserCreated` INT(11) NOT NULL,
  `DateCreated` DATETIME NOT NULL,
  `Deleted` TINYINT(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`UserId`),
  UNIQUE INDEX `id_UNIQUE` (`UserId` ASC),
  INDEX `fk_Users_Address1_idx` (`Address` ASC),
  CONSTRAINT `fk_Users_Address1`
    FOREIGN KEY (`Address`)
    REFERENCES `DrVera`.`Address` (`AddressId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 14
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `DrVera`.`Address`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `DrVera`.`Address` (
  `AddressId` INT(11) NOT NULL AUTO_INCREMENT,
  `Country` VARCHAR(100) NULL DEFAULT NULL,
  `Region` VARCHAR(100) NULL DEFAULT NULL,
  `City` VARCHAR(100) NULL DEFAULT NULL,
  `Address` VARCHAR(100) NULL DEFAULT NULL,
  `PostIndex` INT(5) NULL DEFAULT NULL,
  `UserCreated` INT(11) NOT NULL,
  `DateCreated` DATETIME NOT NULL,
  `Deleted` TINYINT(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`AddressId`),
  UNIQUE INDEX `AddressId_UNIQUE` (`AddressId` ASC),
  INDEX `fk_Address_Users_idx` (`UserCreated` ASC),
  CONSTRAINT `fk_Address_Users`
    FOREIGN KEY (`UserCreated`)
    REFERENCES `DrVera`.`Users` (`UserId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 43
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `DrVera`.`MethodTypes`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `DrVera`.`MethodTypes` (
  `MethodTypeId` INT(11) NOT NULL AUTO_INCREMENT,
  `ShortName` VARCHAR(25) CHARACTER SET 'utf8' NOT NULL,
  `FullName` VARCHAR(100) CHARACTER SET 'utf8' NOT NULL,
  `UserCreated` INT(11) NOT NULL,
  `DateCreated` DATETIME NOT NULL,
  `Deleted` TINYINT(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`MethodTypeId`),
  INDEX `fk_MethodTypes_Users_idx` (`UserCreated` ASC),
  CONSTRAINT `fk_MethodTypes_Users`
    FOREIGN KEY (`UserCreated`)
    REFERENCES `DrVera`.`Users` (`UserId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `DrVera`.`Methods`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `DrVera`.`Methods` (
  `MethodId` INT(11) NOT NULL AUTO_INCREMENT,
  `MethodType` INT(11) NOT NULL,
  `ShortName` VARCHAR(45) NOT NULL,
  `FullName` VARCHAR(100) NOT NULL,
  `ShortDescription` VARCHAR(100) NULL DEFAULT NULL,
  `FullDescription` VARCHAR(300) NULL DEFAULT NULL,
  `TimeInMinutes` INT(11) NOT NULL,
  `UserCreated` INT(11) NOT NULL,
  `DateCreated` DATETIME NOT NULL,
  `Deleted` TINYINT(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`MethodId`),
  UNIQUE INDEX `ShortName_UNIQUE` (`ShortName` ASC),
  UNIQUE INDEX `MethodId_UNIQUE` (`MethodId` ASC),
  INDEX `fk_Method_Users1_idx` (`UserCreated` ASC),
  INDEX `fk_Method_MethodTypes_idx` (`MethodType` ASC),
  CONSTRAINT `fk_Method_MethodTypes`
    FOREIGN KEY (`MethodType`)
    REFERENCES `DrVera`.`MethodTypes` (`MethodTypeId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Method_Users1`
    FOREIGN KEY (`UserCreated`)
    REFERENCES `DrVera`.`Users` (`UserId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `DrVera`.`DoctorsHasMethod`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `DrVera`.`DoctorsHasMethod` (
  `DoctorsHasMethodId` INT(11) NOT NULL AUTO_INCREMENT,
  `Method` INT(11) NOT NULL,
  `Doctor` INT(11) NOT NULL,
  PRIMARY KEY (`DoctorsHasMethodId`),
  INDEX `fk_Doctors_has_Method_Method1_idx` (`Method` ASC),
  INDEX `fk_DoctorsHasMethod_Users1_idx` (`Doctor` ASC),
  CONSTRAINT `fk_DoctorsHasMethod_Users1`
    FOREIGN KEY (`Doctor`)
    REFERENCES `DrVera`.`Users` (`UserId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Doctors_has_Method_Method1`
    FOREIGN KEY (`Method`)
    REFERENCES `DrVera`.`Methods` (`MethodId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `DrVera`.`Rooms`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `DrVera`.`Rooms` (
  `RoomId` INT(11) NOT NULL AUTO_INCREMENT,
  `Name` VARCHAR(45) NOT NULL,
  `Description` VARCHAR(200) NULL DEFAULT NULL,
  `UserCreated` INT(11) NOT NULL,
  `DateCreated` DATETIME NOT NULL,
  `Deleted` TINYINT(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`RoomId`),
  UNIQUE INDEX `RoomId_UNIQUE` (`RoomId` ASC),
  INDEX `fk_Rooms_Users1_idx` (`UserCreated` ASC),
  CONSTRAINT `fk_Rooms_Users1`
    FOREIGN KEY (`UserCreated`)
    REFERENCES `DrVera`.`Users` (`UserId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `DrVera`.`Schedule`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `DrVera`.`Schedule` (
  `ScheduleId` INT(11) NOT NULL AUTO_INCREMENT,
  `Doctor` INT(11) NOT NULL,
  `Patient` INT(11) NOT NULL,
  `Assistant` INT(11) NULL,
  `DoctorDirected` INT(11) NULL DEFAULT NULL,
  `Room` INT(11) NOT NULL,
  `Method` INT(11) NOT NULL,
  `DateTimeStart` DATETIME NOT NULL,
  `DateTimeEnd` DATETIME NOT NULL,
  `ParentSchedule` INT(11) NULL,
  `Description` VARCHAR(200) NULL DEFAULT NULL,
  `UserCreated` INT(11) NOT NULL,
  `DateCreated` DATETIME NOT NULL,
  `Deleted` TINYINT(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`ScheduleId`),
  UNIQUE INDEX `ScheduleId_UNIQUE` (`ScheduleId` ASC),
  INDEX `fk_Schedule_Users1_idx` (`Patient` ASC),
  INDEX `fk_Schedule_Rooms1_idx` (`Room` ASC),
  INDEX `fk_Schedule_Method1_idx` (`Method` ASC),
  INDEX `fk_Schedule_Users2_idx` (`Assistant` ASC),
  INDEX `fk_Schedule_Users3_idx` (`Doctor` ASC),
  INDEX `fk_Schedule_Users4_idx` (`DoctorDirected` ASC),
  INDEX `fk_Schedule_Users5_idx` (`UserCreated` ASC),
  INDEX `fk_Schedule_Schedule_idx` (`ParentSchedule` ASC),
  CONSTRAINT `fk_Schedule_Method1`
    FOREIGN KEY (`Method`)
    REFERENCES `DrVera`.`Methods` (`MethodId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Schedule_Rooms1`
    FOREIGN KEY (`Room`)
    REFERENCES `DrVera`.`Rooms` (`RoomId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Schedule_Users1`
    FOREIGN KEY (`Patient`)
    REFERENCES `DrVera`.`Users` (`UserId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Schedule_Users2`
    FOREIGN KEY (`Assistant`)
    REFERENCES `DrVera`.`Users` (`UserId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Schedule_Users3`
    FOREIGN KEY (`Doctor`)
    REFERENCES `DrVera`.`Users` (`UserId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Schedule_Users4`
    FOREIGN KEY (`DoctorDirected`)
    REFERENCES `DrVera`.`Users` (`UserId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Schedule_Users5`
    FOREIGN KEY (`UserCreated`)
    REFERENCES `DrVera`.`Users` (`UserId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Schedule_Schedule`
    FOREIGN KEY (`ParentSchedule`)
    REFERENCES `DrVera`.`Schedule` (`ScheduleId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `DrVera`.`Payments`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `DrVera`.`Payments` (
  `PaymentId` INT(11) NOT NULL AUTO_INCREMENT,
  `DataTime` DATETIME NOT NULL,
  `Total` FLOAT NOT NULL,
  `Description` VARCHAR(200) NULL DEFAULT NULL,
  `Schedule` INT(11) NULL DEFAULT NULL,
  `Recipient` INT(11) NULL DEFAULT NULL,
  `UserCreated` INT(11) NOT NULL,
  `DateCreated` DATETIME NOT NULL,
  `Deleted` TINYINT(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`PaymentId`),
  UNIQUE INDEX `PaymentId_UNIQUE` (`PaymentId` ASC),
  INDEX `fk_Payments_Users1_idx` (`UserCreated` ASC),
  INDEX `fk_Payments_Schedule1_idx` (`Schedule` ASC),
  INDEX `fk_Payments_Users2_idx` (`Recipient` ASC),
  CONSTRAINT `fk_Payments_Schedule1`
    FOREIGN KEY (`Schedule`)
    REFERENCES `DrVera`.`Schedule` (`ScheduleId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Payments_Users1`
    FOREIGN KEY (`UserCreated`)
    REFERENCES `DrVera`.`Users` (`UserId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Payments_Users2`
    FOREIGN KEY (`Recipient`)
    REFERENCES `DrVera`.`Users` (`UserId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `DrVera`.`Plan`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `DrVera`.`Plan` (
  `PlanId` INT(11) NOT NULL AUTO_INCREMENT,
  `DateTimeStart` DATETIME NOT NULL,
  `DateTimeEnd` DATETIME NOT NULL,
  `Description` VARCHAR(200) NULL DEFAULT NULL,
  `Room` INT(11) NOT NULL,
  `Doctor` INT(11) NOT NULL,
  `UserCreated` INT(11) NOT NULL,
  `DateCreated` DATETIME NOT NULL,
  `Deleted` TINYINT(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`PlanId`),
  UNIQUE INDEX `PlanId_UNIQUE` (`PlanId` ASC),
  INDEX `fk_Plan_Rooms1_idx` (`Room` ASC),
  INDEX `fk_Plan_Users1_idx` (`Doctor` ASC),
  INDEX `fk_Plan_Users2_idx` (`UserCreated` ASC),
  CONSTRAINT `fk_Plan_Rooms1`
    FOREIGN KEY (`Room`)
    REFERENCES `DrVera`.`Rooms` (`RoomId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Plan_Users1`
    FOREIGN KEY (`Doctor`)
    REFERENCES `DrVera`.`Users` (`UserId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Plan_Users2`
    FOREIGN KEY (`UserCreated`)
    REFERENCES `DrVera`.`Users` (`UserId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `DrVera`.`Policy`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `DrVera`.`Policy` (
  `PolicyId` INT(11) NOT NULL AUTO_INCREMENT,
  `Name` VARCHAR(45) NOT NULL,
  `StringId` VARCHAR(45) NOT NULL,
  `PolicyGroup` VARCHAR(100) NULL,
  `Description` VARCHAR(200) NULL DEFAULT NULL,
  `UserCreated` INT(11) NOT NULL,
  `DateCreated` DATETIME NOT NULL,
  `Deleted` TINYINT(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`PolicyId`),
  UNIQUE INDEX `PolicyId_UNIQUE` (`PolicyId` ASC),
  INDEX `fk_Policy_Users1_idx` (`UserCreated` ASC),
  CONSTRAINT `fk_Policy_Users1`
    FOREIGN KEY (`UserCreated`)
    REFERENCES `DrVera`.`Users` (`UserId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `DrVera`.`UserGroups`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `DrVera`.`UserGroups` (
  `UserGroupId` INT(11) NOT NULL AUTO_INCREMENT,
  `Name` VARCHAR(45) NOT NULL,
  `Description` VARCHAR(200) NULL DEFAULT NULL,
  `UserCreated` INT(11) NOT NULL,
  `DateCreated` DATETIME NOT NULL,
  `Deleted` TINYINT(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`UserGroupId`),
  UNIQUE INDEX `UserTypeId_UNIQUE` (`UserGroupId` ASC),
  INDEX `fk_UserTypes_Users1_idx` (`UserCreated` ASC),
  CONSTRAINT `fk_UserTypes_Users1`
    FOREIGN KEY (`UserCreated`)
    REFERENCES `DrVera`.`Users` (`UserId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 2
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `DrVera`.`PolicyHasUserGroups`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `DrVera`.`PolicyHasUserGroups` (
  `PolicyHasUserGroupId` INT NOT NULL AUTO_INCREMENT,
  `UserGroup` INT(11) NOT NULL,
  `Policy` INT(11) NOT NULL,
  PRIMARY KEY (`PolicyHasUserGroupId`),
  UNIQUE INDEX `PolicyHasUserTypesId_UNIQUE` (`PolicyHasUserGroupId` ASC),
  INDEX `fk_Groups_has_Policy_Policy1_idx` (`Policy` ASC),
  INDEX `fk_Groups_has_Policy_Groups1_idx` (`UserGroup` ASC),
  CONSTRAINT `fk_Groups_has_Policy_Groups1`
    FOREIGN KEY (`UserGroup`)
    REFERENCES `DrVera`.`UserGroups` (`UserGroupId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Groups_has_Policy_Policy1`
    FOREIGN KEY (`Policy`)
    REFERENCES `DrVera`.`Policy` (`PolicyId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `DrVera`.`Prices`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `DrVera`.`Prices` (
  `PriceId` INT(11) NOT NULL AUTO_INCREMENT,
  `Total` FLOAT NOT NULL,
  `DateTime` DATETIME NULL DEFAULT NULL,
  `Method` INT(11) NOT NULL,
  `UserCreated` INT(11) NOT NULL,
  `DateCreated` DATETIME NOT NULL,
  `Deleted` TINYINT(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`PriceId`),
  UNIQUE INDEX `PriceId_UNIQUE` (`PriceId` ASC),
  INDEX `fk_Price_Method1_idx` (`Method` ASC),
  INDEX `fk_Price_Users1_idx` (`UserCreated` ASC),
  CONSTRAINT `fk_Price_Method1`
    FOREIGN KEY (`Method`)
    REFERENCES `DrVera`.`Methods` (`MethodId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Price_Users1`
    FOREIGN KEY (`UserCreated`)
    REFERENCES `DrVera`.`Users` (`UserId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `DrVera`.`Share`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `DrVera`.`Share` (
  `ShareId` INT(11) NOT NULL AUTO_INCREMENT,
  `SalaryDoctor` FLOAT NULL DEFAULT NULL,
  `SalaryAssistant` FLOAT NULL DEFAULT NULL,
  `PersentageDoctor` FLOAT NULL DEFAULT NULL,
  `PercentageAssistant` FLOAT NULL DEFAULT NULL,
  `DataTime` DATETIME NOT NULL,
  `Method` INT(11) NOT NULL,
  `Doctor` INT(11) NOT NULL,
  `Assistant` INT(11) NOT NULL,
  `UserCreated` INT(11) NOT NULL,
  `DateCreated` DATETIME NOT NULL,
  `Deleted` TINYINT(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`ShareId`),
  UNIQUE INDEX `SalaryId_UNIQUE` (`ShareId` ASC),
  INDEX `fk_Salary_Method1_idx` (`Method` ASC),
  INDEX `fk_Share_Users1_idx` (`Doctor` ASC),
  INDEX `fk_Share_Users2_idx` (`Assistant` ASC),
  INDEX `fk_Share_Users3_idx` (`UserCreated` ASC),
  CONSTRAINT `fk_Salary_Method1`
    FOREIGN KEY (`Method`)
    REFERENCES `DrVera`.`Methods` (`MethodId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Share_Users1`
    FOREIGN KEY (`Doctor`)
    REFERENCES `DrVera`.`Users` (`UserId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Share_Users2`
    FOREIGN KEY (`Assistant`)
    REFERENCES `DrVera`.`Users` (`UserId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Share_Users3`
    FOREIGN KEY (`UserCreated`)
    REFERENCES `DrVera`.`Users` (`UserId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `DrVera`.`UsersHasUserGroups`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `DrVera`.`UsersHasUserGroups` (
  `UsersHasUserGroupsId` INT(11) NOT NULL AUTO_INCREMENT,
  `User` INT(11) NOT NULL,
  `UserGroup` INT(11) NOT NULL,
  INDEX `fk_Users_has_UserTypes_UserTypes1_idx` (`UserGroup` ASC),
  INDEX `fk_Users_has_UserTypes_Users1_idx` (`User` ASC),
  PRIMARY KEY (`UsersHasUserGroupsId`),
  CONSTRAINT `fk_Users_has_UserTypes_Users1`
    FOREIGN KEY (`User`)
    REFERENCES `DrVera`.`Users` (`UserId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Users_has_UserTypes_UserTypes1`
    FOREIGN KEY (`UserGroup`)
    REFERENCES `DrVera`.`UserGroups` (`UserGroupId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `DrVera`.`DeliveryGroup`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `DrVera`.`DeliveryGroup` (
  `DeliveryGroupId` INT(11) NOT NULL AUTO_INCREMENT,
  `Name` VARCHAR(45) NULL,
  `Description` VARCHAR(200) NULL,
  `UserCreated` INT(11) NOT NULL,
  `DateCreated` DATETIME NOT NULL,
  `Deleted` TINYINT(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`DeliveryGroupId`),
  INDEX `fk_DeliveryGroups_Users1_idx` (`UserCreated` ASC),
  CONSTRAINT `fk_DeliveryGroups_Users1`
    FOREIGN KEY (`UserCreated`)
    REFERENCES `DrVera`.`Users` (`UserId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `DrVera`.`MessageTemplate`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `DrVera`.`MessageTemplate` (
  `MessageTemplateId` INT(11) NOT NULL AUTO_INCREMENT,
  `Name` VARCHAR(45) NULL,
  `Content` VARCHAR(5000) NULL,
  `Description` VARCHAR(200) NULL,
  `Type` TINYINT NOT NULL,
  `System` TINYINT(1) NOT NULL DEFAULT 0,
  `UserCreated` INT(11) NOT NULL,
  `DateCreated` DATETIME NOT NULL,
  `Deleted` TINYINT(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`MessageTemplateId`),
  INDEX `fk_DeliveryGroups_Users1_idx` (`UserCreated` ASC),
  CONSTRAINT `fk_DeliveryGroups_Users10`
    FOREIGN KEY (`UserCreated`)
    REFERENCES `DrVera`.`Users` (`UserId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `DrVera`.`MessageScheduler`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `DrVera`.`MessageScheduler` (
  `MessageSchedulerId` INT(11) NOT NULL AUTO_INCREMENT,
  `MessageTemplate` INT(11) NOT NULL,
  `DeliveryGroup` INT(11) NULL,
  `User` INT(11) NULL,
  `DateStart` DATE NOT NULL,
  `DateEnd` DATE NOT NULL,
  `Time` DATETIME NOT NULL,
  `UserCreated` INT(11) NOT NULL,
  `DateCreated` DATE NOT NULL,
  `Deleted` TINYINT(1) NOT NULL DEFAULT '0',
  INDEX `fk_DeliveryGroups_Users1_idx` (`UserCreated` ASC),
  INDEX `fk_SmsTemplates_copy1_DeliveryGroups1_idx` (`DeliveryGroup` ASC),
  INDEX `fk_SmsTemplates_copy1_Users1_idx` (`User` ASC),
  PRIMARY KEY (`MessageSchedulerId`),
  INDEX `fk_MessageScheduler_MessageTemplate1_idx` (`MessageTemplate` ASC),
  CONSTRAINT `fk_DeliveryGroups_Users102`
    FOREIGN KEY (`UserCreated`)
    REFERENCES `DrVera`.`Users` (`UserId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_SmsTemplates_copy1_DeliveryGroups1`
    FOREIGN KEY (`DeliveryGroup`)
    REFERENCES `DrVera`.`DeliveryGroup` (`DeliveryGroupId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_SmsTemplates_copy1_Users1`
    FOREIGN KEY (`User`)
    REFERENCES `DrVera`.`Users` (`UserId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_MessageScheduler_MessageTemplate1`
    FOREIGN KEY (`MessageTemplate`)
    REFERENCES `DrVera`.`MessageTemplate` (`MessageTemplateId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `DrVera`.`TransactionLog`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `DrVera`.`TransactionLog` (
  `TransactionLogId` INT(11) NOT NULL AUTO_INCREMENT,
  `Status` TINYINT NOT NULL,
  `MessageScheduler` INT(11) NOT NULL,
  `UserCreated` INT(11) NOT NULL,
  `DateCreated` DATETIME NOT NULL,
  `Deleted` TINYINT(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`TransactionLogId`),
  INDEX `fk_DeliveryGroups_Users1_idx` (`UserCreated` ASC),
  INDEX `fk_TransactionLog_MessageScheduler1_idx` (`MessageScheduler` ASC),
  CONSTRAINT `fk_DeliveryGroups_Users103`
    FOREIGN KEY (`UserCreated`)
    REFERENCES `DrVera`.`Users` (`UserId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_TransactionLog_MessageScheduler1`
    FOREIGN KEY (`MessageScheduler`)
    REFERENCES `DrVera`.`MessageScheduler` (`MessageSchedulerId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `DrVera`.`MessageLog`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `DrVera`.`MessageLog` (
  `MessageLogId` INT(11) NOT NULL AUTO_INCREMENT,
  `UId` VARCHAR(255) NOT NULL,
  `Status` TINYINT NOT NULL,
  `Recipient` INT(11) NULL,
  `Transaction` INT(11) NOT NULL,
  `UserCreated` INT(11) NOT NULL,
  `DateCreated` DATETIME NOT NULL,
  `Deleted` TINYINT(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`MessageLogId`),
  INDEX `fk_DeliveryGroups_Users1_idx` (`UserCreated` ASC),
  INDEX `fk_EmailLogs_TransactionLogs1_idx` (`Transaction` ASC),
  INDEX `fk_EmailLogs_Users1_idx` (`Recipient` ASC),
  CONSTRAINT `fk_DeliveryGroups_Users10300`
    FOREIGN KEY (`UserCreated`)
    REFERENCES `DrVera`.`Users` (`UserId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_EmailLogs_TransactionLogs10`
    FOREIGN KEY (`Transaction`)
    REFERENCES `DrVera`.`TransactionLog` (`TransactionLogId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_EmailLogs_Users10`
    FOREIGN KEY (`Recipient`)
    REFERENCES `DrVera`.`Users` (`UserId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `DrVera`.`DeliveryGroupHasUserGroups`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `DrVera`.`DeliveryGroupHasUserGroups` (
  `DeliveryGroupHasUserGroupsId` INT(11) NOT NULL AUTO_INCREMENT,
  `DeliveryGroup` INT(11) NOT NULL,
  `UserGroup` INT(11) NOT NULL,
  INDEX `fk_DeliveryGroup_has_UserGroups_UserGroups1_idx` (`UserGroup` ASC),
  INDEX `fk_DeliveryGroup_has_UserGroups_DeliveryGroup1_idx` (`DeliveryGroup` ASC),
  PRIMARY KEY (`DeliveryGroupHasUserGroupsId`),
  CONSTRAINT `fk_DeliveryGroup_has_UserGroups_DeliveryGroup1`
    FOREIGN KEY (`DeliveryGroup`)
    REFERENCES `DrVera`.`DeliveryGroup` (`DeliveryGroupId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_DeliveryGroup_has_UserGroups_UserGroups1`
    FOREIGN KEY (`UserGroup`)
    REFERENCES `DrVera`.`UserGroups` (`UserGroupId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `DrVera`.`DeliveryGroupHasUsers`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `DrVera`.`DeliveryGroupHasUsers` (
  `DeliveryGroupHasUsersId` INT(11) NOT NULL AUTO_INCREMENT,
  `DeliveryGroup` INT(11) NOT NULL,
  `User` INT(11) NOT NULL,
  PRIMARY KEY (`DeliveryGroupHasUsersId`),
  INDEX `fk_DeliveryGroup_has_Users_Users1_idx` (`User` ASC),
  INDEX `fk_DeliveryGroup_has_Users_DeliveryGroup1_idx` (`DeliveryGroup` ASC),
  CONSTRAINT `fk_DeliveryGroup_has_Users_DeliveryGroup1`
    FOREIGN KEY (`DeliveryGroup`)
    REFERENCES `DrVera`.`DeliveryGroup` (`DeliveryGroupId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_DeliveryGroup_has_Users_Users1`
    FOREIGN KEY (`User`)
    REFERENCES `DrVera`.`Users` (`UserId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `DrVera`.`SchedulerHasDaysOfWeek`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `DrVera`.`SchedulerHasDaysOfWeek` (
  `DayOfWeekId` INT NOT NULL,
  `SchedulerId` INT NOT NULL,
  PRIMARY KEY (`DayOfWeekId`, `SchedulerId`),
  INDEX `fk_DaysOfWeekScheduler_MessageScheduler1_idx` (`SchedulerId` ASC),
  CONSTRAINT `fk_DaysOfWeekScheduler_MessageScheduler1`
    FOREIGN KEY (`SchedulerId`)
    REFERENCES `DrVera`.`MessageScheduler` (`MessageSchedulerId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `DrVera`.`MessageSchedulerHasDeliveryGroup`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `DrVera`.`MessageSchedulerHasDeliveryGroup` (
  `MessageSchedulerId` INT(11) NOT NULL,
  `DeliveryGroupId` INT(11) NOT NULL,
  PRIMARY KEY (`MessageSchedulerId`, `DeliveryGroupId`),
  INDEX `fk_MessageScheduler_has_DeliveryGroup_DeliveryGroup1_idx` (`DeliveryGroupId` ASC),
  INDEX `fk_MessageScheduler_has_DeliveryGroup_MessageScheduler1_idx` (`MessageSchedulerId` ASC),
  CONSTRAINT `fk_MessageScheduler_has_DeliveryGroup_MessageScheduler1`
    FOREIGN KEY (`MessageSchedulerId`)
    REFERENCES `DrVera`.`MessageScheduler` (`MessageSchedulerId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_MessageScheduler_has_DeliveryGroup_DeliveryGroup1`
    FOREIGN KEY (`DeliveryGroupId`)
    REFERENCES `DrVera`.`DeliveryGroup` (`DeliveryGroupId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
