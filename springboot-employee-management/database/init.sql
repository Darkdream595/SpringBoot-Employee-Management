-- 创建数据库
CREATE DATABASE IF NOT EXISTS employee_management DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE employee_management;

-- 用户表
CREATE TABLE IF NOT EXISTS `user` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `username` VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    `password` VARCHAR(100) NOT NULL COMMENT '密码',
    `salt` VARCHAR(100) COMMENT '密码盐值',
    `gmt_create` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 角色表
CREATE TABLE IF NOT EXISTS `role` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `name` VARCHAR(50) NOT NULL UNIQUE COMMENT '角色名称',
    `description` VARCHAR(100) COMMENT '角色描述',
    `role_name` VARCHAR(50) COMMENT '角色标识',
    `gmt_create` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

-- 权限表
CREATE TABLE IF NOT EXISTS `permission` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `name` VARCHAR(50) NOT NULL COMMENT '权限名称',
    `description` VARCHAR(100) COMMENT '权限描述',
    `permission_name` VARCHAR(50) COMMENT '权限标识',
    `gmt_create` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='权限表';

-- 用户角色关联表
CREATE TABLE IF NOT EXISTS `user_role` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `role_id` BIGINT NOT NULL COMMENT '角色ID',
    `gmt_create` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
    FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关联表';

-- 角色权限关联表
CREATE TABLE IF NOT EXISTS `role_permission` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `role_id` BIGINT NOT NULL COMMENT '角色ID',
    `permission_id` BIGINT NOT NULL COMMENT '权限ID',
    `gmt_create` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    FOREIGN KEY (`role_id`) REFERENCES `role` (`id`),
    FOREIGN KEY (`permission_id`) REFERENCES `permission` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色权限关联表';

-- 考勤表
CREATE TABLE IF NOT EXISTS `attendance` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `attendance_date` DATE COMMENT '考勤日期',
    `check_in` TIME COMMENT '签到时间',
    `check_out` TIME COMMENT '签退时间',
    `status` VARCHAR(20) COMMENT '考勤状态',
    `gmt_create` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='考勤表';

-- 薪资表
CREATE TABLE IF NOT EXISTS `salary` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `salary_month` VARCHAR(7) NOT NULL COMMENT '工资月份',
    `base_salary` DECIMAL(10,2) NOT NULL COMMENT '基本工资',
    `bonus` DECIMAL(10,2) DEFAULT 0 COMMENT '奖金',
    `total_salary` DECIMAL(10,2) GENERATED ALWAYS AS (base_salary + bonus) STORED COMMENT '总计',
    `gmt_create` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='薪资表';

-- 插入基础角色数据
INSERT INTO `role` (`name`, `description`, `role_name`) VALUES
('ROLE_SYSTEM_ADMIN', '系统管理员角色', 'ROLE_SYSTEM_ADMIN'),
('ROLE_HR', '人力资源管理角色', 'ROLE_HR'),
('ROLE_NORMAL_EMPLOYEE', '普通员工角色', 'ROLE_NORMAL_EMPLOYEE')
ON DUPLICATE KEY UPDATE description=VALUES(description);

-- 插入基础权限数据
INSERT INTO `permission` (`name`, `description`, `permission_name`) VALUES
('ATTENDANCE_VIEW', '查看考勤记录权限', 'attendance:view'),
('ATTENDANCE_EDIT', '编辑考勤记录权限', 'attendance:edit'),
('HR_VIEW', '查看人事信息权限', 'hr:view'),
('HR_EDIT', '编辑人事信息权限', 'hr:edit'),
('SYSTEM_VIEW', '查看系统信息权限', 'system:view'),
('SYSTEM_EDIT', '编辑系统信息权限', 'system:edit')
ON DUPLICATE KEY UPDATE description=VALUES(description);

-- 插入示例用户数据（密码均为：123）
INSERT INTO `user` (`username`, `password`, `salt`, `gmt_create`, `gmt_modified`) VALUES
('admin', '$2a$10$THH81g1z6la5SZ9sxSZdauUEjvuPClkdaLwiVI97kxLL4i5oItKdW', '', NOW(), NOW()),
('user01', '$2a$10$THH81g1z6la5SZ9sxSZdauUEjvuPClkdaLwiVI97kxLL4i5oItKdW', '', NOW(), NOW()),
('user02', '$2a$10$THH81g1z6la5SZ9sxSZdauUEjvuPClkdaLwiVI97kxLL4i5oItKdW', '', NOW(), NOW()),
('user03', '$2a$10$THH81g1z6la5SZ9sxSZdauUEjvuPClkdaLwiVI97kxLL4i5oItKdW', '', NOW(), NOW())
ON DUPLICATE KEY UPDATE 
    password = VALUES(password),
    salt = VALUES(salt),
    gmt_modified = NOW();

-- 为用户分配角色
INSERT INTO `user_role` (`user_id`, `role_id`, `gmt_create`, `gmt_modified`) VALUES
(1, 1, NOW(), NOW()), -- admin -> ROLE_SYSTEM_ADMIN
(2, 2, NOW(), NOW()), -- user01 -> ROLE_HR
(3, 3, NOW(), NOW()), -- user02 -> ROLE_NORMAL_EMPLOYEE
(4, 3, NOW(), NOW())  -- user03 -> ROLE_NORMAL_EMPLOYEE
ON DUPLICATE KEY UPDATE gmt_modified = NOW();

-- 为角色分配权限
INSERT INTO `role_permission` (`role_id`, `permission_id`, `gmt_create`, `gmt_modified`) VALUES
(1, 1, NOW(), NOW()), -- SYSTEM_ADMIN -> attendance:view
(1, 2, NOW(), NOW()), -- SYSTEM_ADMIN -> attendance:edit
(1, 3, NOW(), NOW()), -- SYSTEM_ADMIN -> hr:view
(1, 4, NOW(), NOW()), -- SYSTEM_ADMIN -> hr:edit
(1, 5, NOW(), NOW()), -- SYSTEM_ADMIN -> system:view
(1, 6, NOW(), NOW()), -- SYSTEM_ADMIN -> system:edit
(2, 1, NOW(), NOW()), -- HR -> attendance:view
(2, 2, NOW(), NOW()), -- HR -> attendance:edit
(2, 3, NOW(), NOW()), -- HR -> hr:view
(2, 4, NOW(), NOW()), -- HR -> hr:edit
(3, 1, NOW(), NOW())  -- NORMAL_EMPLOYEE -> attendance:view
ON DUPLICATE KEY UPDATE gmt_modified = NOW();

-- 插入示例考勤数据
INSERT INTO `attendance` (`user_id`, `attendance_date`, `check_in`, `check_out`, `status`, `gmt_create`, `gmt_modified`) VALUES
(2, CURDATE(), '09:00:00', '18:00:00', '正常', NOW(), NOW()),
(3, CURDATE(), '09:00:00', '18:00:00', '正常', NOW(), NOW()),
(4, CURDATE(), '09:00:00', '18:00:00', '正常', NOW(), NOW())
ON DUPLICATE KEY UPDATE 
    status = VALUES(status),
    gmt_modified = NOW();

-- 插入示例薪资数据
INSERT INTO `salary` (`user_id`, `salary_month`, `base_salary`, `bonus`, `gmt_create`, `gmt_modified`) VALUES
(2, DATE_FORMAT(CURDATE(), '%Y-%m'), 8000.00, 2000.00, NOW(), NOW()),
(3, DATE_FORMAT(CURDATE(), '%Y-%m'), 8000.00, 2000.00, NOW(), NOW()),
(4, DATE_FORMAT(CURDATE(), '%Y-%m'), 8000.00, 2000.00, NOW(), NOW())
ON DUPLICATE KEY UPDATE 
    base_salary = VALUES(base_salary),
    bonus = VALUES(bonus),
    gmt_modified = NOW(); 