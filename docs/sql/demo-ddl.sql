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
