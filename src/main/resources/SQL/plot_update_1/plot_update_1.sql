ALTER TABLE `plotfinder`.`plot` 
ADD COLUMN `district` VARCHAR(45) NULL AFTER `address2`,
ADD COLUMN `added` DATETIME NOT NULL AFTER `size`,
ADD COLUMN `type` TINYINT(1) NOT NULL DEFAULT 1 AFTER `garage`,
ADD COLUMN `house` TINYINT(1) NOT NULL DEFAULT 0 AFTER `type`,
ADD COLUMN `farming` TINYINT(1) NOT NULL DEFAULT 0 AFTER `house`,
ADD COLUMN `grazing` TINYINT(1) NOT NULL DEFAULT 0 AFTER `farming`,
ADD COLUMN `orchard` TINYINT(1) NOT NULL DEFAULT 0 AFTER `grazing`,
CHANGE COLUMN `description` `description` TEXT NOT NULL ;

ALTER TABLE plotfinder.plot
DROP COLUMN `country`,
DROP COLUMN `power`,
DROP COLUMN `water`,
DROP COLUMN `gas`,
DROP COLUMN `sewer`,
DROP COLUMN `internet`,
DROP COLUMN `garage`,
DROP COLUMN `type`,
DROP COLUMN `house`,
DROP COLUMN `farming`,
DROP COLUMN `grazing`,
DROP COLUMN `orchard`,
DROP COLUMN `num_pic`
;