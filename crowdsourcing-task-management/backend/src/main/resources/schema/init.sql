CREATE TABLE IF NOT EXISTS `user_info` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '用户ID',
    `wx_open_id` VARCHAR(100) UNIQUE NOT NULL COMMENT '微信OpenID',
    `phone` VARCHAR(20) UNIQUE COMMENT '手机号',
    `nick_name` VARCHAR(100) COMMENT '昵称',
    `avatar` VARCHAR(500) COMMENT '头像',
    `identity_type` TINYINT NOT NULL DEFAULT 1 COMMENT '身份类型：1-执行者 2-雇主 3-管理员',
    `real_name` VARCHAR(50) COMMENT '真实姓名',
    `id_card` VARCHAR(20) COMMENT '身份证号（脱敏存储）',
    `enterprise_name` VARCHAR(200) COMMENT '企业名称',
    `enterprise_license` VARCHAR(500) COMMENT '营业执照路径',
    `auth_status` TINYINT NOT NULL DEFAULT 0 COMMENT '实名认证状态：0-未认证 1-审核中 2-已认证 3-认证失败',
    `credit_score` INT NOT NULL DEFAULT 100 COMMENT '信誉分',
    `bank_card` VARCHAR(50) COMMENT '银行卡号（脱敏）',
    `bank_name` VARCHAR(100) COMMENT '开户行',
    `alipay_account` VARCHAR(100) COMMENT '支付宝账号',
    `withdraw_password` VARCHAR(100) COMMENT '提现密码（加密）',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-封禁 1-正常',
    `blacklisted` TINYINT NOT NULL DEFAULT 0 COMMENT '是否拉黑：0-否 1-是',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX `idx_wx_open_id` (`wx_open_id`),
    INDEX `idx_identity_type` (`identity_type`),
    INDEX `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户信息表';

CREATE TABLE IF NOT EXISTS `task_info` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '任务ID',
    `title` VARCHAR(200) NOT NULL COMMENT '任务标题',
    `category` TINYINT NOT NULL COMMENT '任务分类：1-新媒体 2-电商运营 3-办公文职 4-创意 5-其他',
    `description` TEXT COMMENT '任务详情',
    `steps` TEXT COMMENT '操作步骤',
    `example_images` TEXT COMMENT '示例图（JSON数组）',
    `deadline` DATETIME NOT NULL COMMENT '截止时间',
    `commission` DECIMAL(10,2) NOT NULL COMMENT '单任务佣金',
    `max_workers` INT NOT NULL COMMENT '可接人数',
    `total_budget` DECIMAL(12,2) NOT NULL COMMENT '总预算',
    `acceptance_criteria` TEXT COMMENT '验收标准',
    `delivery_type` TINYINT NOT NULL DEFAULT 1 COMMENT '交付类型：1-截图 2-文档 3-链接 4-文字',
    `reject_rules` TEXT COMMENT '驳回细则',
    `permission_settings` TEXT COMMENT '权限配置（JSON）',
    `employer_id` BIGINT NOT NULL COMMENT '发布者ID',
    `status` TINYINT NOT NULL DEFAULT 0 COMMENT '状态：0-草稿 1-待审核 2-已上架 3-已下架 4-已过期',
    `audit_status` TINYINT NOT NULL DEFAULT 0 COMMENT '平台审核状态：0-待审核 1-通过 2-拒绝',
    `reject_reason` VARCHAR(500) COMMENT '平台拒绝原因',
    `auto_audit_hours` INT DEFAULT 24 COMMENT '自动审核超时时间（小时）',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX `idx_employer_id` (`employer_id`),
    INDEX `idx_status` (`status`),
    INDEX `idx_category` (`category`),
    INDEX `idx_deadline` (`deadline`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='任务信息表';

CREATE TABLE IF NOT EXISTS `order_info` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '订单ID',
    `task_id` BIGINT NOT NULL COMMENT '任务ID',
    `worker_id` BIGINT NOT NULL COMMENT '执行者ID',
    `employer_id` BIGINT NOT NULL COMMENT '雇主ID',
    `commission` DECIMAL(10,2) NOT NULL COMMENT '佣金金额',
    `delivery_content` TEXT COMMENT '交付内容（JSON）',
    `delivery_time` DATETIME COMMENT '提交时间',
    `status` TINYINT NOT NULL DEFAULT 0 COMMENT '状态：0-待接单 1-已接单 2-待提交 3-待审核 4-审核通过 5-已驳回 6-已作废 7-已完成',
    `audit_time` DATETIME COMMENT '审核时间',
    `audit_user_id` BIGINT COMMENT '审核人ID',
    `reject_reason` VARCHAR(500) COMMENT '驳回原因',
    `retry_count` INT NOT NULL DEFAULT 0 COMMENT '驳回重提次数',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX `idx_task_id` (`task_id`),
    INDEX `idx_worker_id` (`worker_id`),
    INDEX `idx_employer_id` (`employer_id`),
    INDEX `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单履约表';

CREATE TABLE IF NOT EXISTS `fund_flow` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '流水ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `type` TINYINT NOT NULL COMMENT '流水类型：1-充值 2-冻结 3-放款 4-服务费 5-提现 6-退款',
    `amount` DECIMAL(12,2) NOT NULL COMMENT '金额',
    `balance_before` DECIMAL(12,2) NOT NULL COMMENT '变动前余额',
    `balance_after` DECIMAL(12,2) NOT NULL COMMENT '变动后余额',
    `related_order_id` BIGINT COMMENT '关联订单ID',
    `related_task_id` BIGINT COMMENT '关联任务ID',
    `remark` VARCHAR(500) COMMENT '备注',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-失败 1-成功',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_type` (`type`),
    INDEX `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资金流水表';

CREATE TABLE IF NOT EXISTS `dispute_info` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '纠纷ID',
    `order_id` BIGINT NOT NULL COMMENT '关联订单ID',
    `plaintiff_id` BIGINT NOT NULL COMMENT '申诉方ID',
    `defendant_id` BIGINT NOT NULL COMMENT '被申诉方ID',
    `type` TINYINT NOT NULL COMMENT '纠纷类型：1-雇主申诉 2-执行者申诉',
    `content` TEXT COMMENT '申诉内容',
    `evidence` TEXT COMMENT '举证材料（JSON）',
    `status` TINYINT NOT NULL DEFAULT 0 COMMENT '状态：0-待仲裁 1-仲裁中 2-已结案',
    `result` TINYINT COMMENT '仲裁结果：1-支持申诉方 2-支持被申诉方 3-协商解决',
    `result_content` TEXT COMMENT '仲裁结果说明',
    `arbitrator_id` BIGINT COMMENT '仲裁员ID',
    `processed_at` DATETIME COMMENT '处理时间',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX `idx_order_id` (`order_id`),
    INDEX `idx_plaintiff_id` (`plaintiff_id`),
    INDEX `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='纠纷仲裁表';

CREATE TABLE IF NOT EXISTS `user_balance` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'ID',
    `user_id` BIGINT UNIQUE NOT NULL COMMENT '用户ID',
    `available_balance` DECIMAL(12,2) NOT NULL DEFAULT 0 COMMENT '可用余额',
    `frozen_balance` DECIMAL(12,2) NOT NULL DEFAULT 0 COMMENT '冻结余额',
    `total_recharge` DECIMAL(12,2) NOT NULL DEFAULT 0 COMMENT '累计充值',
    `total_withdraw` DECIMAL(12,2) NOT NULL DEFAULT 0 COMMENT '累计提现',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户余额表';

CREATE TABLE IF NOT EXISTS `platform_config` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'ID',
    `config_key` VARCHAR(100) UNIQUE NOT NULL COMMENT '配置键',
    `config_value` TEXT COMMENT '配置值',
    `description` VARCHAR(500) COMMENT '配置说明',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='平台配置表';

CREATE TABLE IF NOT EXISTS `system_notice` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '公告ID',
    `title` VARCHAR(200) NOT NULL COMMENT '公告标题',
    `content` TEXT NOT NULL COMMENT '公告内容',
    `type` TINYINT NOT NULL DEFAULT 1 COMMENT '公告类型：1-系统公告 2-费率公告 3-风控公示 4-帮助指南',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-下架 1-上架',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX `idx_type` (`type`),
    INDEX `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统公告表';

INSERT INTO `platform_config` (`config_key`, `config_value`, `description`) VALUES
('employer_service_fee_rate', '0.05', '雇主服务费比例'),
('worker_withdraw_fee_rate', '0.01', '执行者提现费率'),
('min_withdraw_amount', '10', '最低提现金额'),
('max_withdraw_times_per_day', '3', '每日最大提现次数'),
('max_ongoing_tasks_per_worker', '5', '执行者最大同时进行任务数'),
('max_retry_count', '2', '任务最大驳回重提次数');