ALTER TABLE `tableA` RENAME TO `tableA1`, ENGINE=myisam, DROP cola, MODIFY colb VARCHAR(60) FIRST, ADD cold INT UNSIGNED DEFAULT 12 COMMENT 'cold remark!' AFTER colc, CONVERT TO CHARACTER SET utf8 COLLATE utf8mb4_binary