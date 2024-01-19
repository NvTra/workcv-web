DROP DATABASE  IF EXISTS spring_workcv;
CREATE DATABASE  IF NOT EXISTS spring_workcv;
USE spring_workcv;

DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `role_name` VARCHAR(255) DEFAULT NULL,
    PRIMARY KEY (`id`)
)  ENGINE=INNODB DEFAULT CHARSET=UTF8MB4 COLLATE = UTF8MB4_0900_AI_CI;
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `full_name` VARCHAR(255) DEFAULT NULL,
    `email` VARCHAR(255) DEFAULT NULL,
    `address` VARCHAR(255) DEFAULT NULL,
    `phone_number` VARCHAR(255) DEFAULT NULL,
    `image` VARCHAR(255) DEFAULT NULL,
    `description` VARCHAR(255) DEFAULT NULL,
    `password` VARCHAR(128) DEFAULT NULL,
    `status` INT DEFAULT NULL,
    `isActive` TINYINT NOT NULL,
    `role_id` INT DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `email_UNIQUE` (`email`),
    KEY `role_id` (`role_id`),
    CONSTRAINT `user_ibfk_1` FOREIGN KEY (`role_id`)
        REFERENCES `role` (`id`)
)  ENGINE=INNODB DEFAULT CHARSET=UTF8MB4 COLLATE = UTF8MB4_0900_AI_CI;
DROP TABLE IF EXISTS `company`;
CREATE TABLE `company` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `name_company` VARCHAR(255) DEFAULT NULL,
    `phone_number` VARCHAR(255) DEFAULT NULL,
    `email` VARCHAR(255) DEFAULT NULL,
    `address` VARCHAR(255) DEFAULT NULL,
    `description` VARCHAR(255) DEFAULT NULL,
    `logo` VARCHAR(255) DEFAULT NULL,
    `status` INT DEFAULT NULL,
    `user_id` INT DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `user_id` (`user_id`),
    CONSTRAINT `company_ibfk_1` FOREIGN KEY (`user_id`)
        REFERENCES `user` (`id`)
)  ENGINE=INNODB DEFAULT CHARSET=UTF8MB4 COLLATE = UTF8MB4_0900_AI_CI;
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(255) DEFAULT NULL,
    `number_choose` INT DEFAULT NULL,
    PRIMARY KEY (`id`)
)  ENGINE=INNODB DEFAULT CHARSET=UTF8MB4 COLLATE = UTF8MB4_0900_AI_CI;

DROP TABLE IF EXISTS `recruitment`;
CREATE TABLE `recruitment` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `title` VARCHAR(255) DEFAULT NULL,
    `type` VARCHAR(255) DEFAULT NULL,
    `view` INT DEFAULT NULL,
    `address` VARCHAR(255) DEFAULT NULL,
    `created_at` VARCHAR(255) DEFAULT NULL,
    `description` VARCHAR(255) DEFAULT NULL,
    `experience` VARCHAR(255) DEFAULT NULL,
    `quantity` INT DEFAULT NULL,
    `ranked` VARCHAR(255) DEFAULT NULL,
    `salary` VARCHAR(255) DEFAULT NULL,
    `deadline` VARCHAR(255) DEFAULT NULL,
    `status` INT DEFAULT NULL,
    `category_id` INT DEFAULT NULL,
    `company_id` INT DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `category_id` (`category_id`),
    KEY `company_id` (`company_id`),
    CONSTRAINT `recruitment_ibfk_1` FOREIGN KEY (`category_id`)
        REFERENCES `category` (`id`),
    CONSTRAINT `recruitment_ibfk_2` FOREIGN KEY (`company_id`)
        REFERENCES `company` (`id`)
)  ENGINE=INNODB DEFAULT CHARSET=UTF8MB4 COLLATE = UTF8MB4_0900_AI_CI;
DROP TABLE IF EXISTS `cv`;
CREATE TABLE `cv` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `file_name` VARCHAR(255) DEFAULT NULL,
    `user_id` INT DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `user_id` (`user_id`),
    CONSTRAINT `cv_ibfk_1` FOREIGN KEY (`user_id`)
        REFERENCES `user` (`id`)
)  ENGINE=INNODB DEFAULT CHARSET=UTF8MB4 COLLATE = UTF8MB4_0900_AI_CI;
 
 DROP TABLE IF EXISTS `applypost`;
CREATE TABLE `applypost` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `name_cv` VARCHAR(255) DEFAULT NULL,
    `status` INT DEFAULT NULL,
    `text` VARCHAR(255) DEFAULT NULL,
    `created_at` VARCHAR(255) DEFAULT NULL,
    `recruitment_id` INT DEFAULT NULL,
    `user_id` INT DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `recruitment_id` (`recruitment_id`),
    KEY `user_id` (`user_id`),
    CONSTRAINT `applypost_ibfk_1` FOREIGN KEY (`recruitment_id`)
        REFERENCES `recruitment` (`id`),
    CONSTRAINT `applypost_ibfk_2` FOREIGN KEY (`user_id`)
        REFERENCES `user` (`id`)
)  ENGINE=INNODBDEFAULT CHARSET=UTF8MB4 COLLATE = UTF8MB4_0900_AI_CI;
DROP TABLE IF EXISTS `save_job`;
CREATE TABLE `save_job` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `recruitment_id` INT DEFAULT NULL,
    `user_id` INT DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `user_id` (`user_id`),
    KEY `recruitment_id` (`recruitment_id`),
    CONSTRAINT `save_job_ibfk_1` FOREIGN KEY (`user_id`)
        REFERENCES `user` (`id`),
    CONSTRAINT `save_job_ibfk_2` FOREIGN KEY (`recruitment_id`)
        REFERENCES `recruitment` (`id`)
)  ENGINE=INNODB DEFAULT CHARSET=UTF8MB4 COLLATE = UTF8MB4_0900_AI_CI;

DROP TABLE IF EXISTS `follow_company`;

CREATE TABLE `follow_company` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `company_id` INT DEFAULT NULL,
    `user_id` INT DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `user_id` (`user_id`),
    KEY `company_id` (`company_id`),
    CONSTRAINT `follow_company_ibfk_1` FOREIGN KEY (`user_id`)
        REFERENCES `user` (`id`),
    CONSTRAINT `follow_company_ibfk_2` FOREIGN KEY (`company_id`)
        REFERENCES `company` (`id`)
)  ENGINE=INNODB DEFAULT CHARSET=UTF8MB4 COLLATE = UTF8MB4_0900_AI_CI;

INSERT INTO spring_workcv.`role` (role_name) VALUES
	 ('ROLE_EMPLOYER'),
	 ('ROLE_CANDIDATE'),
	 ('ROLE_ADMIN');
     
INSERT INTO spring_workcv.category (name,number_choose) VALUES
	 ('Vận tải',2),
	 ('Lập trình viên',10),
	 ('Kế Toán',8),
	 ('Kinh Doanh',10),
	 ('Hành chính',5);
