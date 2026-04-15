# DDD 教学示例项目设计说明

## 目标

构建一个基于 `Spring Boot 2.7 + JDK 8 + MyBatis-Plus` 的教学型 DDD 示例项目。项目的首要目标不是覆盖复杂功能，而是让阅读者能清晰理解：

1. DDD 分层与多模块结构
2. 业务规则应落在聚合和领域服务中
3. MyBatis-Plus 在 DDD 项目中的合理位置

## 范围

本项目只实现一个限界上下文：订单上下文。

覆盖两个强相关用例：

1. 下单
2. 取消订单

库存能力不做成完整子系统，而是通过外部能力抽象接入，避免教学重点分散。

## 架构

项目采用 Maven 多模块单体结构：

- `ddd-demo-domain`：领域层
- `ddd-demo-application`：应用层
- `ddd-demo-infrastructure`：基础设施层
- `ddd-demo-interfaces`：接口层
- `ddd-demo-start`：启动层

典型调用链：

`Controller -> AppService -> DomainService / Aggregate -> Repository -> MyBatis-Plus`

## 领域模型

### 聚合根

`Order`

职责：

- 持有订单状态与订单项
- 计算订单总金额
- 约束下单基础规则
- 约束取消订单规则

### 聚合内实体

`OrderItem`

职责：

- 表示订单中的单个商品项
- 维护数量、成交价、小计金额

### 值对象

- `OrderId`
- `UserId`
- `ProductId`
- `Money`

### 领域服务

`OrderDomainService`

职责：

- 在创建订单前协调库存校验
- 组装下单所需的领域对象

### 抽象依赖

- `OrderRepository`
- `InventoryGateway`

## 用例设计

### 下单

应用层负责编排流程：

1. 接收命令对象
2. 转换为领域对象
3. 调用 `OrderDomainService`
4. 保存聚合
5. 返回 DTO

领域层负责规则：

- 订单至少有一个商品项
- 商品数量必须大于 0
- 商品价格不能小于 0
- 总金额由聚合统一计算

### 取消订单

应用层流程：

1. 根据订单 ID 读取聚合
2. 调用聚合行为 `cancel(reason)`
3. 保存聚合
4. 返回 DTO

领域规则：

- 只有 `CREATED` 状态可取消
- 已取消订单不可重复取消
- 取消原因不能为空

## 基础设施设计

MyBatis-Plus 只存在于基础设施层，用于：

- `OrderDO` / `OrderItemDO`
- `OrderMapper` / `OrderItemMapper`
- `MybatisPlusOrderRepository`

同时提供：

- `FakeInventoryGateway`
- 示例数据库配置
- 示例 DDL

## 接口设计

提供 3 个 HTTP 接口：

1. `POST /api/orders`
2. `POST /api/orders/{orderId}/cancel`
3. `GET /api/orders/{orderId}`

## 测试策略

优先覆盖教学价值最高的部分：

1. `Order` 聚合规则测试
2. `PlaceOrderAppService` 用例测试
3. `CancelOrderAppService` 用例测试

不要求真实数据库连通。

## 可读性要求

为了让项目更适合学习：

- 避免引入不必要的复杂模式
- 避免过度使用框架技巧
- 通过 README 和导读文档解释阅读顺序
- 保持每层职责清晰、文件命名直白
