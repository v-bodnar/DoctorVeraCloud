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
`Deleted`,
`MessagingAccepted`)
VALUES
('root','61f22ec3c233b35b784fc2719d5cb0871abeefee','Админ','Админович','Админов','1985-12-06 00:00:00','+30635744511',1,NOW(),0,1),
('Buterbrodnik', 'f497806b3e565dbb6f0ab7e3431e3a115f186a64', 'Перерыв', 'Кофеинов', 'Бутербродник', NOW(),'+382222222222', 1, NOW(), 0,0);

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
INSERT INTO `DrVera`.`usershasusergroups` (`User`, `UserGroup`) VALUES ('1', '1');
INSERT INTO `DrVera`.`messagetemplate` (`Name`, `Content`, `Description`, `Type`, `System`, `UserCreated`, `DateCreated`, `Deleted`) VALUES('Стандартный шаблон при записи пациента', '<div id=\"header\" style=\"\r\nbackground: -moz-linear-gradient(-45deg,  #006400 0%, #61c419 98%);\r\nbackground: -webkit-linear-gradient(-45deg,  #006400 0%,#61c419 98%);\r\nbackground: linear-gradient(135deg,  #006400 0%,#61c419 98%);\r\nfilter: progid:DXImageTransform.Microsoft.gradient( startColorstr=\'#006400\', endColorstr=\'#61c419\',GradientType=1 );\r\nwidth:100%; height:125px;\r\n-webkit-border-top-left-radius: 5px;\r\n-webkit-border-top-right-radius: 5px;\r\n-moz-border-radius-topleft: 5px;\r\n-moz-border-radius-topright: 5px;\r\nborder-top-left-radius: 5px;\r\nborder-top-right-radius: 5px;\r\n\"><div style=\"float:left\"><h1 style=\"color:gold; margin-left:50px;\">DoctorVera</h1></div>\r\n<div style=\"float:right;text-align:center\">\r\n	<span style=\"color:#ffcc33;\">www.doctorvera.kiev.ua<br>\r\n	info@doctorvera.kiev.ua</span><br>\r\n	<span style=\"color:darkgreen;\">Адрес:03037, Украина,<br>\r\n	&nbsp;г. Киев, ул. И. Клименка, 10/17<br>\r\n	Тел.: (044)332-86-17</span>\r\n</div>\r\n</div>\r\n<div style=\"color:#312E25;\">\r\n<p><span style=\"font-weight: bold;\">Добрый день, $usersFirstName $usersLastName!</span></p>\r\n<p>Вы были записаны на прием к доктору: $doctorsFirstName $doctorsLastName</p>\r\n<p>Прием назначен на $appointmentStartDate на $appointmentStartTime</p>\r\n<p>Исследование: $appointmentMethodName</p>\r\n<p>Стандартная стоимость: $appointmentMethodPrice грн.<br></p>\r\n</div><br>\r\n<div style=\"color:darkgreen; font-size:8pt\">\r\nДля того чтобы больше не получать эту рассылку <a style=\"color:gold;\" href=\"http://cloud.doctorvera.kiev.ua/unsubscribe.xhtml?email=$usersEmail?transactionId=$transactionId\">пройдите по ссылке\r\n</a></div>\r\n', 'При записи пациента на прием ему будет отправлено письмо с данным контентом', '1', '1', '1', Now(), '0');
INSERT INTO `DrVera`.`messagetemplate` (`Name`, `Content`, `Description`, `Type`, `System`, `UserCreated`, `DateCreated`, `Deleted`) VALUES('Стандартный шаблон СМС при записи пациента', 'Добрый день, $usersFirstName $usersLastName! Вы были записаны на прием к доктору: $doctorsFirstName $doctorsLastName Прием назначен на $appointmentStartDate на $appointmentStartTime Исследование: $appointmentMethodName Стандартная стоимость: $appointmentMethodPrice грн.', 'При записи пациента на прием ему будет отправлена смс с данным контентом', '0', '1', '1', Now(), '0');
INSERT INTO `drvera`.`locale`(`LanguageCode`,`CountryCode`,`Language`,`LanguageNative`,`UserCreated`,`DateCreated`,`Deleted`)VALUES('ru','RU','Russian','Русский',1,Now(),0);
INSERT INTO `drvera`.`locale`(`LanguageCode`,`CountryCode`,`Language`,`LanguageNative`,`UserCreated`,`DateCreated`,`Deleted`)VALUES('en','','English','English',1,Now(),0);