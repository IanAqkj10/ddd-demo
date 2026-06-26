CREATE TABLE `t_order` (
  `order_id` varchar(64) NOT NULL COMMENT '订单ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `status` varchar(32) NOT NULL COMMENT '订单状态',
  `total_amount` decimal(12,2) NOT NULL COMMENT '订单总金额',
  `cancel_reason` varchar(255) DEFAULT NULL COMMENT '取消原因',
  `created_at` datetime NOT NULL COMMENT '创建时间',
  `canceled_at` datetime DEFAULT NULL COMMENT '取消时间',
  PRIMARY KEY (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

CREATE TABLE `t_order_item` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `order_id` varchar(64) NOT NULL COMMENT '订单ID',
  `product_id` bigint NOT NULL COMMENT '商品ID',
  `product_name` varchar(128) NOT NULL COMMENT '商品名称',
  `quantity` int NOT NULL COMMENT '购买数量',
  `sale_price` decimal(12,2) NOT NULL COMMENT '成交单价',
  `subtotal_amount` decimal(12,2) NOT NULL COMMENT '小计金额',
  PRIMARY KEY (`id`),
  KEY `idx_order_id` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单项表';

CREATE TABLE `t_membership` (
  `membership_id` bigint NOT NULL COMMENT '会员ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `user_name` varchar(64) NOT NULL COMMENT '用户名',
  `points` int NOT NULL DEFAULT 0 COMMENT '积分',
  `level` varchar(32) NOT NULL COMMENT '会员等级',
  `joined_at` datetime NOT NULL COMMENT '加入时间',
  `last_updated_at` datetime NOT NULL COMMENT '最后更新时间',
  PRIMARY KEY (`membership_id`),
  UNIQUE KEY `uk_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='会员表';

CREATE TABLE `t_iam_user` (
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `username` varchar(64) NOT NULL COMMENT '登录名',
  `password_hash` varchar(128) NOT NULL COMMENT '密码哈希',
  `password_salt` varchar(64) NOT NULL COMMENT '密码盐',
  `status` varchar(16) NOT NULL COMMENT '用户状态：ACTIVE/DISABLED',
  `role_ids` varchar(512) NOT NULL DEFAULT '' COMMENT '角色ID列表，逗号分隔',
  `created_at` datetime NOT NULL COMMENT '创建时间',
  `last_updated_at` datetime NOT NULL COMMENT '最后更新时间',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='IAM 用户表';

CREATE TABLE `t_iam_role` (
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `code` varchar(64) NOT NULL COMMENT '角色编码',
  `name` varchar(64) NOT NULL COMMENT '角色名称',
  `permissions` varchar(2048) NOT NULL DEFAULT '' COMMENT '权限编码列表，逗号分隔',
  `created_at` datetime NOT NULL COMMENT '创建时间',
  `last_updated_at` datetime NOT NULL COMMENT '最后更新时间',
  PRIMARY KEY (`role_id`),
  UNIQUE KEY `uk_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='IAM 角色表';
