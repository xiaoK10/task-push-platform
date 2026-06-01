-- ============================================================
-- 地推任务平台 - 数据库初始化脚本
-- ============================================================

CREATE DATABASE IF NOT EXISTS task_push_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE task_push_db;

-- ============================================================
-- 1. 管理员表
-- ============================================================
DROP TABLE IF EXISTS `sys_admin`;
CREATE TABLE `sys_admin` (
    `id`            BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `username`      VARCHAR(50)  NOT NULL                COMMENT '用户名',
    `password`      VARCHAR(255) NOT NULL                COMMENT '密码(BCrypt加密)',
    `real_name`     VARCHAR(50)  DEFAULT NULL            COMMENT '真实姓名',
    `phone`         VARCHAR(20)  DEFAULT NULL            COMMENT '手机号',
    `role_id`       BIGINT       DEFAULT NULL            COMMENT '角色ID',
    `dept_id`       BIGINT       DEFAULT NULL            COMMENT '部门ID',
    `status`        TINYINT      DEFAULT 1               COMMENT '状态(0禁用 1正常)',
    `is_deleted`    TINYINT      DEFAULT 0               COMMENT '逻辑删除(0未删除 1已删除)',
    `create_time`   DATETIME     DEFAULT NULL            COMMENT '创建时间',
    `update_time`   DATETIME     DEFAULT NULL            COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`),
    KEY `idx_role_id` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='管理员表';

-- ============================================================
-- 2. 角色表
-- ============================================================
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
    `id`            BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `role_name`     VARCHAR(50)  NOT NULL                COMMENT '角色名称',
    `role_code`     VARCHAR(50)  NOT NULL                COMMENT '角色编码',
    `description`   VARCHAR(200) DEFAULT NULL            COMMENT '角色描述',
    `status`        TINYINT      DEFAULT 1               COMMENT '状态(0禁用 1正常)',
    `is_deleted`    TINYINT      DEFAULT 0               COMMENT '逻辑删除(0未删除 1已删除)',
    `create_time`   DATETIME     DEFAULT NULL            COMMENT '创建时间',
    `update_time`   DATETIME     DEFAULT NULL            COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_role_code` (`role_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='角色表';

-- ============================================================
-- 3. 菜单表
-- ============================================================
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
    `id`            BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `parent_id`     BIGINT       DEFAULT 0               COMMENT '父菜单ID(0为顶级)',
    `menu_name`     VARCHAR(50)  NOT NULL                COMMENT '菜单名称',
    `menu_type`     VARCHAR(20)  NOT NULL                COMMENT '菜单类型(DIRECTORY目录/MENU菜单/BUTTON按钮)',
    `path`          VARCHAR(200) DEFAULT NULL            COMMENT '路由地址',
    `component`     VARCHAR(200) DEFAULT NULL            COMMENT '组件路径',
    `perms`         VARCHAR(200) DEFAULT NULL            COMMENT '权限标识',
    `icon`          VARCHAR(100) DEFAULT NULL            COMMENT '菜单图标',
    `sort`          INT          DEFAULT 0               COMMENT '排序号',
    `status`        TINYINT      DEFAULT 1               COMMENT '状态(0隐藏 1显示)',
    `is_deleted`    TINYINT      DEFAULT 0               COMMENT '逻辑删除(0未删除 1已删除)',
    `create_time`   DATETIME     DEFAULT NULL            COMMENT '创建时间',
    `update_time`   DATETIME     DEFAULT NULL            COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='菜单表';

-- ============================================================
-- 4. 角色菜单关联表
-- ============================================================
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
    `id`        BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `role_id`   BIGINT NOT NULL                COMMENT '角色ID',
    `menu_id`   BIGINT NOT NULL                COMMENT '菜单ID',
    PRIMARY KEY (`id`),
    KEY `idx_role_id` (`role_id`),
    KEY `idx_menu_id` (`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='角色菜单关联表';

-- ============================================================
-- 5. 地推用户表
-- ============================================================
DROP TABLE IF EXISTS `user_member`;
CREATE TABLE `user_member` (
    `id`              BIGINT         NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `openid`          VARCHAR(100)   DEFAULT NULL            COMMENT '微信OpenID',
    `unionid`         VARCHAR(100)   DEFAULT NULL            COMMENT '微信UnionID',
    `phone`           VARCHAR(20)    DEFAULT NULL            COMMENT '手机号',
    `nickname`        VARCHAR(50)    DEFAULT NULL            COMMENT '昵称',
    `avatar`          VARCHAR(500)   DEFAULT NULL            COMMENT '头像地址',
    `user_level`      INT            DEFAULT 0               COMMENT '用户等级',
    `province`        VARCHAR(50)    DEFAULT NULL            COMMENT '省份',
    `city`            VARCHAR(50)    DEFAULT NULL            COMMENT '城市',
    `district`        VARCHAR(50)    DEFAULT NULL            COMMENT '区/县',
    `parent_id`       BIGINT         DEFAULT 0               COMMENT '上级代理ID',
    `balance`         DECIMAL(12,2)  DEFAULT 0.00            COMMENT '可用余额',
    `frozen_balance`  DECIMAL(12,2)  DEFAULT 0.00            COMMENT '冻结余额',
    `total_income`    DECIMAL(12,2)  DEFAULT 0.00            COMMENT '累计收入',
    `total_withdraw`  DECIMAL(12,2)  DEFAULT 0.00            COMMENT '累计提现',
    `status`          TINYINT        DEFAULT 1               COMMENT '状态(0黑名单 1正常 2冻结)',
    `is_deleted`      TINYINT        DEFAULT 0               COMMENT '逻辑删除(0未删除 1已删除)',
    `register_time`   DATETIME       DEFAULT NULL            COMMENT '注册时间',
    `create_time`     DATETIME       DEFAULT NULL            COMMENT '创建时间',
    `update_time`     DATETIME       DEFAULT NULL            COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_openid` (`openid`),
    KEY `idx_parent_id` (`parent_id`),
    KEY `idx_phone` (`phone`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='地推用户表';

-- ============================================================
-- 6. 任务分类表
-- ============================================================
DROP TABLE IF EXISTS `task_category`;
CREATE TABLE `task_category` (
    `id`             BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `category_name`  VARCHAR(100) NOT NULL                COMMENT '分类名称',
    `parent_id`      BIGINT       DEFAULT 0               COMMENT '父分类ID(0为顶级)',
    `icon`           VARCHAR(500) DEFAULT NULL            COMMENT '分类图标',
    `sort`           INT          DEFAULT 0               COMMENT '排序号',
    `status`         TINYINT      DEFAULT 1               COMMENT '状态(0隐藏 1显示)',
    `is_deleted`     TINYINT      DEFAULT 0               COMMENT '逻辑删除(0未删除 1已删除)',
    `create_time`    DATETIME     DEFAULT NULL            COMMENT '创建时间',
    `update_time`    DATETIME     DEFAULT NULL            COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='任务分类表';

-- ============================================================
-- 7. 任务主表
-- ============================================================
DROP TABLE IF EXISTS `task_info`;
CREATE TABLE `task_info` (
    `id`                  BIGINT         NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `category_id`         BIGINT         DEFAULT NULL            COMMENT '任务分类ID',
    `task_name`           VARCHAR(200)   NOT NULL                COMMENT '任务名称',
    `task_desc`           TEXT           DEFAULT NULL            COMMENT '任务详情(富文本)',
    `cover_image`         VARCHAR(500)   DEFAULT NULL            COMMENT '封面图',
    `total_stock`         INT            DEFAULT 0               COMMENT '总库存',
    `remain_stock`        INT            DEFAULT 0               COMMENT '剩余库存',
    `max_grab_per_user`   INT            DEFAULT 3               COMMENT '每人最大抢单数',
    `region_limit`        TEXT           DEFAULT NULL            COMMENT '区域限制(省市区JSON)',
    `level_requirement`   INT            DEFAULT 0               COMMENT '等级要求',
    `task_start_time`     DATETIME       DEFAULT NULL            COMMENT '任务开始时间',
    `task_end_time`       DATETIME       DEFAULT NULL            COMMENT '任务结束时间',
    `task_status`         TINYINT        DEFAULT 0               COMMENT '任务状态(0草稿 1上架 2下架 3完结)',
    `unit_price`          DECIMAL(10,2)  DEFAULT 0.00            COMMENT '单价',
    `ladder_commission`   TEXT           DEFAULT NULL            COMMENT '阶梯佣金(JSON)',
    `profit_ratio`        DECIMAL(5,2)   DEFAULT 0.00            COMMENT '下级分润比例(%)',
    `cert_config`         TEXT           DEFAULT NULL            COMMENT '上传凭证配置(JSON)',
    `grab_expire_hours`   INT            DEFAULT 24              COMMENT '抢单过期小时数',
    `is_deleted`          TINYINT        DEFAULT 0               COMMENT '逻辑删除(0未删除 1已删除)',
    `create_time`         DATETIME       DEFAULT NULL            COMMENT '创建时间',
    `update_time`         DATETIME       DEFAULT NULL            COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_category_id` (`category_id`),
    KEY `idx_task_status` (`task_status`),
    KEY `idx_task_time` (`task_start_time`, `task_end_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='任务主表';

-- ============================================================
-- 8. 用户抢单订单表
-- ============================================================
DROP TABLE IF EXISTS `task_order`;
CREATE TABLE `task_order` (
    `id`              BIGINT         NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `order_no`        VARCHAR(32)    NOT NULL                COMMENT '订单号',
    `user_id`         BIGINT         NOT NULL                COMMENT '用户ID',
    `task_id`         BIGINT         NOT NULL                COMMENT '任务ID',
    `grab_time`       DATETIME       DEFAULT NULL            COMMENT '抢单时间',
    `submit_time`     DATETIME       DEFAULT NULL            COMMENT '提交时间',
    `cert_images`     TEXT           DEFAULT NULL            COMMENT '凭证图片(JSON数组)',
    `audit_status`    TINYINT        DEFAULT 0               COMMENT '审核状态(0待审 1通过 2驳回 3补资料 4已取消)',
    `real_commission` DECIMAL(10,2)  DEFAULT 0.00            COMMENT '实际佣金',
    `reject_reason`   VARCHAR(500)   DEFAULT NULL            COMMENT '驳回原因',
    `audit_id`        BIGINT         DEFAULT NULL            COMMENT '审核人ID',
    `audit_time`      DATETIME       DEFAULT NULL            COMMENT '审核时间',
    `settle_time`     DATETIME       DEFAULT NULL            COMMENT '结算时间',
    `expire_time`     DATETIME       DEFAULT NULL            COMMENT '超时自动取消时间',
    `is_deleted`      TINYINT        DEFAULT 0               COMMENT '逻辑删除(0未删除 1已删除)',
    `create_time`     DATETIME       DEFAULT NULL            COMMENT '创建时间',
    `update_time`     DATETIME       DEFAULT NULL            COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_order_no` (`order_no`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_task_id` (`task_id`),
    KEY `idx_audit_status` (`audit_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户抢单订单表';

-- ============================================================
-- 9. 收益流水表
-- ============================================================
DROP TABLE IF EXISTS `user_income`;
CREATE TABLE `user_income` (
    `id`             BIGINT         NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id`        BIGINT         NOT NULL                COMMENT '用户ID',
    `order_id`       BIGINT         DEFAULT NULL            COMMENT '订单ID',
    `task_id`        BIGINT         DEFAULT NULL            COMMENT '任务ID',
    `amount`         DECIMAL(10,2)  NOT NULL                COMMENT '金额',
    `income_type`    TINYINT        DEFAULT NULL            COMMENT '收入类型(1任务佣金 2团队分润 3提现退回)',
    `description`    VARCHAR(500)   DEFAULT NULL            COMMENT '描述',
    `balance_after`  DECIMAL(12,2)  DEFAULT NULL            COMMENT '操作后余额',
    `is_deleted`     TINYINT        DEFAULT 0               COMMENT '逻辑删除(0未删除 1已删除)',
    `create_time`    DATETIME       DEFAULT NULL            COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_order_id` (`order_id`),
    KEY `idx_income_type` (`income_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='收益流水表';

-- ============================================================
-- 10. 提现申请表
-- ============================================================
DROP TABLE IF EXISTS `user_withdraw`;
CREATE TABLE `user_withdraw` (
    `id`               BIGINT         NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `withdraw_no`      VARCHAR(32)    NOT NULL                COMMENT '提现单号',
    `user_id`          BIGINT         NOT NULL                COMMENT '用户ID',
    `amount`           DECIMAL(10,2)  NOT NULL                COMMENT '提现金额',
    `fee`              DECIMAL(10,2)  DEFAULT 0.00            COMMENT '手续费',
    `real_amount`      DECIMAL(10,2)  NOT NULL                COMMENT '实际到账金额',
    `withdraw_type`    TINYINT        DEFAULT NULL            COMMENT '提现方式(1微信 2银行卡)',
    `receive_account`  VARCHAR(200)   DEFAULT NULL            COMMENT '收款账号',
    `receive_name`     VARCHAR(50)    DEFAULT NULL            COMMENT '收款人姓名',
    `bank_name`        VARCHAR(100)   DEFAULT NULL            COMMENT '银行名称',
    `bank_branch`      VARCHAR(200)   DEFAULT NULL            COMMENT '支行信息',
    `status`           TINYINT        DEFAULT 0               COMMENT '状态(0待审 1已打款 2驳回)',
    `audit_id`         BIGINT         DEFAULT NULL            COMMENT '审核人ID',
    `audit_time`       DATETIME       DEFAULT NULL            COMMENT '审核时间',
    `pay_time`         DATETIME       DEFAULT NULL            COMMENT '打款时间',
    `reject_reason`    VARCHAR(500)   DEFAULT NULL            COMMENT '驳回原因',
    `is_deleted`       TINYINT        DEFAULT 0               COMMENT '逻辑删除(0未删除 1已删除)',
    `create_time`      DATETIME       DEFAULT NULL            COMMENT '创建时间',
    `update_time`      DATETIME       DEFAULT NULL            COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_withdraw_no` (`withdraw_no`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='提现申请表';

-- ============================================================
-- 11. 团队分润记录表
-- ============================================================
DROP TABLE IF EXISTS `user_team_profit`;
CREATE TABLE `user_team_profit` (
    `id`             BIGINT         NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id`        BIGINT         NOT NULL                COMMENT '受益上级用户ID',
    `from_user_id`   BIGINT         NOT NULL                COMMENT '来源下级用户ID',
    `order_id`       BIGINT         DEFAULT NULL            COMMENT '订单ID',
    `task_id`        BIGINT         DEFAULT NULL            COMMENT '任务ID',
    `profit_amount`  DECIMAL(10,2)  NOT NULL                COMMENT '分润金额',
    `profit_ratio`   DECIMAL(5,2)   DEFAULT NULL            COMMENT '分润比例(%)',
    `profit_level`   INT            DEFAULT 1               COMMENT '分润层级',
    `is_deleted`     TINYINT        DEFAULT 0               COMMENT '逻辑删除(0未删除 1已删除)',
    `create_time`    DATETIME       DEFAULT NULL            COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_from_user_id` (`from_user_id`),
    KEY `idx_order_id` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='团队分润记录表';

-- ============================================================
-- 12. 风控黑名单表
-- ============================================================
DROP TABLE IF EXISTS `sys_risk_black`;
CREATE TABLE `sys_risk_black` (
    `id`           BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `black_type`   TINYINT      NOT NULL                COMMENT '黑名单类型(1手机号 2设备ID 3OpenID)',
    `black_value`  VARCHAR(200) NOT NULL                COMMENT '黑名单值',
    `reason`       VARCHAR(500) DEFAULT NULL            COMMENT '拉黑原因',
    `status`       TINYINT      DEFAULT 1               COMMENT '状态(0禁用 1启用)',
    `create_time`  DATETIME     DEFAULT NULL            COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_black_type` (`black_type`),
    KEY `idx_black_value` (`black_value`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='风控黑名单表';


-- ============================================================
-- 初始数据
-- ============================================================

-- 系统角色
INSERT INTO `sys_role` (`id`, `role_name`, `role_code`, `description`, `status`, `is_deleted`, `create_time`, `update_time`) VALUES
(1, '超级管理员', 'SUPER_ADMIN', '系统超级管理员，拥有所有权限', 1, 0, NOW(), NOW());

-- 系统管理员 (密码: admin123, BCrypt加密)
INSERT INTO `sys_admin` (`id`, `username`, `password`, `real_name`, `phone`, `role_id`, `dept_id`, `status`, `is_deleted`, `create_time`, `update_time`) VALUES
(1, 'admin', '$2a$12$buyZ2A7kiy7tA4g8V3YsQOn3OSXsXXjvCoaEfQ0ydtk78TYGmqLZy', '系统管理员', '13800000000', 1, NULL, 1, 0, NOW(), NOW());

-- 系统菜单
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort`, `status`, `is_deleted`, `create_time`, `update_time`) VALUES
-- 系统管理
(1,  0,  '系统管理', 'DIRECTORY', '/system',       NULL,              NULL,                    'system',   1, 1, 0, NOW(), NOW()),
(2,  1,  '用户管理', 'MENU',      '/system/admin',  'system/admin',    'system:admin:list',     'user',     1, 1, 0, NOW(), NOW()),
(3,  1,  '角色管理', 'MENU',      '/system/role',   'system/role',     'system:role:list',      'peoples',  2, 1, 0, NOW(), NOW()),
(4,  1,  '菜单管理', 'MENU',      '/system/menu',   'system/menu',     'system:menu:list',      'tree-table', 3, 1, 0, NOW(), NOW()),
-- 按钮权限 - 用户管理
(5,  2,  '用户新增', 'BUTTON',    NULL,             NULL,              'system:admin:add',      NULL,       1, 1, 0, NOW(), NOW()),
(6,  2,  '用户编辑', 'BUTTON',    NULL,             NULL,              'system:admin:edit',     NULL,       2, 1, 0, NOW(), NOW()),
(7,  2,  '用户删除', 'BUTTON',    NULL,             NULL,              'system:admin:delete',   NULL,       3, 1, 0, NOW(), NOW()),
-- 按钮权限 - 角色管理
(8,  3,  '角色新增', 'BUTTON',    NULL,             NULL,              'system:role:add',       NULL,       1, 1, 0, NOW(), NOW()),
(9,  3,  '角色编辑', 'BUTTON',    NULL,             NULL,              'system:role:edit',      NULL,       2, 1, 0, NOW(), NOW()),
(10, 3,  '角色删除', 'BUTTON',    NULL,             NULL,              'system:role:delete',    NULL,       3, 1, 0, NOW(), NOW()),
-- 按钮权限 - 菜单管理
(11, 4,  '菜单新增', 'BUTTON',    NULL,             NULL,              'system:menu:add',       NULL,       1, 1, 0, NOW(), NOW()),
(12, 4,  '菜单编辑', 'BUTTON',    NULL,             NULL,              'system:menu:edit',      NULL,       2, 1, 0, NOW(), NOW()),
(13, 4,  '菜单删除', 'BUTTON',    NULL,             NULL,              'system:menu:delete',    NULL,       3, 1, 0, NOW(), NOW()),
-- 任务管理
(14, 0,  '任务管理', 'DIRECTORY', '/task',          NULL,              NULL,                    'guide',    2, 1, 0, NOW(), NOW()),
(15, 14, '任务分类', 'MENU',      '/task/category', 'task/category',   'task:category:list',    'list',     1, 1, 0, NOW(), NOW()),
(16, 14, '任务列表', 'MENU',      '/task/info',     'task/info',       'task:info:list',        'documentation', 2, 1, 0, NOW(), NOW()),
-- 按钮权限 - 任务管理
(17, 15, '分类新增', 'BUTTON',    NULL,             NULL,              'task:category:add',     NULL,       1, 1, 0, NOW(), NOW()),
(18, 15, '分类编辑', 'BUTTON',    NULL,             NULL,              'task:category:edit',    NULL,       2, 1, 0, NOW(), NOW()),
(19, 15, '分类删除', 'BUTTON',    NULL,             NULL,              'task:category:delete',  NULL,       3, 1, 0, NOW(), NOW()),
(20, 16, '任务新增', 'BUTTON',    NULL,             NULL,              'task:info:add',         NULL,       1, 1, 0, NOW(), NOW()),
(21, 16, '任务编辑', 'BUTTON',    NULL,             NULL,              'task:info:edit',        NULL,       2, 1, 0, NOW(), NOW()),
(22, 16, '任务删除', 'BUTTON',    NULL,             NULL,              'task:info:delete',      NULL,       3, 1, 0, NOW(), NOW()),
-- 订单管理
(23, 0,  '订单管理', 'DIRECTORY', '/order',         NULL,              NULL,                    'order',    3, 1, 0, NOW(), NOW()),
(24, 23, '抢单记录', 'MENU',      '/order/list',    'order/list',      'order:list',            'list',     1, 1, 0, NOW(), NOW()),
-- 按钮权限 - 订单管理
(25, 24, '订单审核', 'BUTTON',    NULL,             NULL,              'order:audit',           NULL,       1, 1, 0, NOW(), NOW()),
-- 财务管理
(26, 0,  '财务管理', 'DIRECTORY', '/finance',       NULL,              NULL,                    'money',    4, 1, 0, NOW(), NOW()),
(27, 26, '收益流水', 'MENU',      '/finance/income','finance/income',  'finance:income:list',   'log',      1, 1, 0, NOW(), NOW()),
(28, 26, '提现审核', 'MENU',      '/finance/withdraw','finance/withdraw','finance:withdraw:list','validCode', 2, 1, 0, NOW(), NOW()),
-- 按钮权限 - 财务管理
(29, 28, '提现打款', 'BUTTON',    NULL,             NULL,              'finance:withdraw:pay',  NULL,       1, 1, 0, NOW(), NOW()),
(30, 28, '提现驳回', 'BUTTON',    NULL,             NULL,              'finance:withdraw:reject', NULL,    2, 1, 0, NOW(), NOW()),
-- 风控管理
(31, 0,  '风控管理', 'DIRECTORY', '/risk',          NULL,              NULL,                    'shield',   5, 1, 0, NOW(), NOW()),
(32, 31, '黑名单',   'MENU',      '/risk/black',    'risk/black',      'risk:black:list',       'bug',      1, 1, 0, NOW(), NOW()),
-- 按钮权限 - 风控管理
(33, 32, '黑名单新增', 'BUTTON',  NULL,             NULL,              'risk:black:add',        NULL,       1, 1, 0, NOW(), NOW()),
(34, 32, '黑名单删除', 'BUTTON',  NULL,             NULL,              'risk:black:delete',     NULL,       2, 1, 0, NOW(), NOW());

-- 角色菜单关联 (超级管理员拥有所有菜单权限)
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES
(1,  1, 1), (2,  1, 2), (3,  1, 3), (4,  1, 4),
(5,  1, 5), (6,  1, 6), (7,  1, 7), (8,  1, 8),
(9,  1, 9), (10, 1, 10), (11, 1, 11), (12, 1, 12),
(13, 1, 13), (14, 1, 14), (15, 1, 15), (16, 1, 16),
(17, 1, 17), (18, 1, 18), (19, 1, 19), (20, 1, 20),
(21, 1, 21), (22, 1, 22), (23, 1, 23), (24, 1, 24),
(25, 1, 25), (26, 1, 26), (27, 1, 27), (28, 1, 28),
(29, 1, 29), (30, 1, 30), (31, 1, 31), (32, 1, 32),
(33, 1, 33), (34, 1, 34);

-- 任务分类
INSERT INTO `task_category` (`id`, `category_name`, `parent_id`, `icon`, `sort`, `status`, `is_deleted`, `create_time`, `update_time`) VALUES
(1, '支付宝推广',   0, NULL, 1, 1, 0, NOW(), NOW()),
(2, '美团商户入驻', 0, NULL, 2, 1, 0, NOW(), NOW()),
(3, '信用卡开卡',   0, NULL, 3, 1, 0, NOW(), NOW()),
(4, 'APP下载推广',  0, NULL, 4, 1, 0, NOW(), NOW()),
(5, '电商平台拉新', 0, NULL, 5, 1, 0, NOW(), NOW()),
(6, '问卷调查',     0, NULL, 6, 1, 0, NOW(), NOW());
