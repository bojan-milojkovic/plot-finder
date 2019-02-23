ALTER TABLE `plotfinder`.`plot` 
ADD COLUMN `district` VARCHAR(45) NULL AFTER `address2`,
ADD COLUMN `added` DATETIME NOT NULL AFTER `size`,
ADD COLUMN `type` TINYINT(1) NOT NULL DEFAULT 1 AFTER `garage`,
ADD COLUMN `house` TINYINT(1) NOT NULL DEFAULT 0 AFTER `type`,
ADD COLUMN `farming` TINYINT(1) NOT NULL DEFAULT 0 AFTER `house`,
ADD COLUMN `grazing` TINYINT(1) NOT NULL DEFAULT 0 AFTER `farming`,
ADD COLUMN `orchard` TINYINT(1) NOT NULL DEFAULT 0 AFTER `grazing`,
CHANGE COLUMN `description` `description` TEXT NOT NULL ;