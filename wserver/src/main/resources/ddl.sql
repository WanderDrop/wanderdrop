-- Role
CREATE TABLE IF NOT EXISTS `role` (
                                      role_id CHAR(36) PRIMARY KEY,
    role_name VARCHAR(50) UNIQUE NOT NULL
    );

-- User
CREATE TABLE IF NOT EXISTS `user` (
                                      user_id CHAR(36) PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    role_id CHAR(36) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    user_status ENUM('active', 'locked') DEFAULT 'active' NOT NULL,
    FOREIGN KEY (role_id) REFERENCES role(role_id) ON DELETE RESTRICT ON UPDATE CASCADE
    );

-- DeletionReason
CREATE TABLE IF NOT EXISTS `deletionreason` (
                                                deletion_reason_id INT AUTO_INCREMENT PRIMARY KEY,
                                                reason_message VARCHAR(255) NOT NULL
    );

-- Attraction
CREATE TABLE IF NOT EXISTS `attraction` (
                                            attraction_id INT AUTO_INCREMENT PRIMARY KEY,
                                            attraction_name VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    location TEXT NOT NULL,
    city VARCHAR(255),
    country VARCHAR(255),
    created_by CHAR(36) NOT NULL,
    updated_by CHAR(36),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    status ENUM('active', 'delete') NOT NULL DEFAULT 'active',
    deletion_reason_id INT,
    FOREIGN KEY (created_by) REFERENCES user(user_id) ON DELETE NO ACTION ON UPDATE NO ACTION,
    FOREIGN KEY (updated_by) REFERENCES user(user_id) ON DELETE NO ACTION ON UPDATE NO ACTION,
    FOREIGN KEY (deletion_reason_id) REFERENCES deletionreason(deletion_reason_id) ON DELETE NO ACTION ON UPDATE NO ACTION
    );

-- Comment
CREATE TABLE IF NOT EXISTS `comment` (
                                         comment_id INT AUTO_INCREMENT PRIMARY KEY,
                                         comment_heading VARCHAR(255) NOT NULL,
    comment_text TEXT NOT NULL,
    status ENUM('active', 'deleted') NOT NULL DEFAULT 'active',
    attraction_id INT NOT NULL,
    created_by CHAR(36) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deletion_reason_id INT,
    FOREIGN KEY (attraction_id) REFERENCES attraction(attraction_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (created_by) REFERENCES user(user_id) ON DELETE NO ACTION ON UPDATE NO ACTION,
    FOREIGN KEY (deletion_reason_id) REFERENCES deletionreason(deletion_reason_id) ON DELETE NO ACTION ON UPDATE NO ACTION
    );

-- Report
CREATE TABLE IF NOT EXISTS `report` (
                                        report_id INT AUTO_INCREMENT PRIMARY KEY,
                                        report_heading VARCHAR(255) NOT NULL,
    report_message TEXT NOT NULL,
    report_status ENUM('active', 'deleted') NOT NULL DEFAULT 'active',
    attraction_id INT NOT NULL,
    created_by CHAR(36) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (attraction_id) REFERENCES attraction(attraction_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (created_by) REFERENCES user(user_id) ON DELETE NO ACTION ON UPDATE NO ACTION
    );

