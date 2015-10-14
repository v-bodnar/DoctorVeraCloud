ALTER TABLE Users AUTO_INCREMENT = 1;
ALTER TABLE Address AUTO_INCREMENT = 1;
ALTER TABLE UserGroups AUTO_INCREMENT = 1;
--
-- Пользователт созданные по умолчанию
--
INSERT INTO `DrVera`.`Users`
(
`Username`,
`Password`,
`FirstName`,
`MiddleName`,
`LastName`,
`BirthDate`,
`PhoneNumberMobile`,
`UserCreated`,
`DateCreated`,
`Deleted`)
VALUES
('root','61f22ec3c233b35b784fc2719d5cb0871abeefee','Админ','Админович','Админов','1985-12-06 00:00:00','+30635744511',1,NOW(),0),
('Buterbrodnik', 'f497806b3e565dbb6f0ab7e3431e3a115f186a64', 'Перерыв', 'Кофеинов', 'Бутербродник', NOW(),'+382222222222', 1, NOW(), 0);

--
-- Адрес созданный по умолчанию
--
INSERT INTO `Address`( `Country`, `Region`, `City`, `Address`, `PostIndex`, `UserCreated`, `DateCreated`, `Deleted`) VALUES 
('Украина','Киевская область','Киев','Клименко 10/17','69096', 1, NOW(), 0);

--
-- Группы созданные по умолчанию
--

INSERT INTO `DrVera`.`usergroups` (`Name`, `Description`, `UserCreated`, `DateCreated`, `Deleted`) VALUES
('Супер Админ', 'Эта группа пользователей имеет доступ ко всем ресурсам системы', 1, NOW(), 0),
('Пациенты', 'Эта группа пользователей не должна иметь доступ к страницам этого сайта', 1, NOW(), 0),
('Персонал', 'Эта группа пользователей идентифицирует пользователя, как работника медицинского центра', 1, NOW(), 0),
('Доктор', 'Эта группа пользователей может вести прием в медицинском центре', 1, NOW(), 0),
('Ассистент', 'Эта группа пользователей может ассестировать Доктору во время приема', 1, NOW(), 0),
('Кассир', 'Эта группа пользователей может осуществлять прием и выдачу наличных средств, а также учет соответствующей документации', 1, NOW(), 0),
('Бухгалтер', 'Эта группа пользователей имеет доступ ко всей финансовой информации', 1, NOW(), 0),
('Директор', 'Эта группа пользователей имеет полный доступ ко всем функциям системы!', 1, NOW(), 0),
('Служебные', 'Группа для обеспечения работы системы', 1, NOW(), 0);

UPDATE `DrVera`.`Users` SET `Address`='1' WHERE `UserId`='1';
UPDATE `DrVera`.`Users` SET `Address`='1' WHERE `UserId`='2';

INSERT INTO `DrVera`.`methodtypes` (`ShortName`, `FullName`, `UserCreated`, `DateCreated`, `Deleted`) VALUES ('Служебный', 'Служебный', '1', Now(), '0');
INSERT INTO `DrVera`.`methods` (`MethodType`, `ShortName`, `FullName`, `ShortDescription`, `TimeInMinutes`, `UserCreated`, `DateCreated`, `Deleted`) VALUES ('1', 'Перерыв', 'Перерыв', 'Перерыв', '5', '1', Now(), '0');
INSERT INTO `DrVera`.`prices` (`Total`, `DateTime`, `Method`, `UserCreated`, `DateCreated`, `Deleted`) VALUES ('0', Now(), '1', '1', Now(), '0');
INSERT INTO `drvera`.`usershasusergroups` (`User`, `UserGroup`, `UserCreated`, `DateCreated`, `Deleted`) VALUES ('1', '1', '1', Now(), '0');