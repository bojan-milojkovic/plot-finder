CREATE TABLE `user` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(45) NOT NULL,
  `password` varchar(45) NOT NULL,
  `first_name` varchar(45) NOT NULL,
  `last_name` varchar(45) NOT NULL,
  `email` varchar(100) NOT NULL,
  `phone1` varchar(15) NOT NULL,
  `phone2` varchar(15) NULL,
  `active` tinyint(1) NOT NULL,
  `not_locked` tinyint(1) NOT NULL,
  `reg_date` datetime NOT NULL,
  `last_login` datetime NOT NULL,
  `last_password_change` datetime NOT NULL,
  `last_update` datetime NOT NULL,
PRIMARY KEY (`user_id`),
UNIQUE KEY `user_id_UNIQUE` (`user_id`),
UNIQUE KEY `username_UNIQUE` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1