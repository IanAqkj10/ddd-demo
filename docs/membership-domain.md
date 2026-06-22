# Membership 领域说明

## 概述

Membership（会员）是新增的业务领域，展示了如何在 DDD 项目中添加一个完整的新限界上下文。

## 业务场景

会员领域包含以下核心功能：

1. **会员注册** - 用户首次注册成为会员
2. **积分累积** - 会员通过消费等行为获得积分
3. **积分消费** - 会员使用积分兑换商品或优惠
4. **等级自动升级** - 根据积分自动升级会员等级

## 会员等级体系

- **青铜会员 (BRONZE)** - 0 积分起
- **白银会员 (SILVER)** - 1000 积分起
- **黄金会员 (GOLD)** - 5000 积分起
- **铂金会员 (PLATINUM)** - 10000 积分起

## 模块结构

### 领域层 (`ddd-demo-domain/membership`)

- **聚合根**: `Membership` - 会员聚合根，负责会员核心业务规则
- **值对象**: 
  - `MembershipId` - 会员ID
  - `Points` - 积分值对象，封装积分运算规则
- **枚举**: `MembershipLevel` - 会员等级枚举
- **领域服务**: `MembershipDomainService` - 会员注册、积分计算等领域服务
- **仓储接口**: `MembershipRepository` - 会员仓储抽象

### 应用层 (`ddd-demo-application/membership`)

- **应用服务**:
  - `RegisterMembershipAppService` - 会员注册用例
  - `EarnPointsAppService` - 积分累积用例
  - `ConsumePointsAppService` - 积分消费用例
  - `GetMembershipDetailAppService` - 查询会员详情用例
- **命令对象**: `RegisterMembershipCommand`, `EarnPointsCommand`, `ConsumePointsCommand`
- **DTO**: `MembershipDetailDTO`

### 基础设施层 (`ddd-demo-infrastructure/membership`)

- **仓储实现**: `MybatisPlusMembershipRepository`
- **数据对象**: `MembershipDO`
- **Mapper**: `MembershipMapper`
- **数据转换器**: `MembershipDataAssembler`

### 接口层 (`ddd-demo-interfaces/membership`)

- **Controller**: `MembershipController`
- **请求对象**: `RegisterMembershipRequest`, `EarnPointsRequest`, `ConsumePointsRequest`

## API 接口示例

### 1. 注册会员

```http
POST /api/memberships
Content-Type: application/json

{
  "userId": 1001,
  "userName": "张三"
}
```

### 2. 赚取积分

```http
POST /api/memberships/{membershipId}/earn-points
Content-Type: application/json

{
  "points": 100,
  "reason": "购物消费"
}
```

### 3. 消费积分

```http
POST /api/memberships/{membershipId}/consume-points
Content-Type: application/json

{
  "points": 50,
  "reason": "积分兑换"
}
```

### 4. 查询会员详情

```http
GET /api/memberships/{membershipId}
```

## 数据库表结构

```sql
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
```

## DDD 设计亮点

### 1. 聚合根守护业务规则

`Membership` 聚合根不暴露 setter，所有状态变更通过行为方法完成：

```java
// 错误做法
membership.setPoints(membership.getPoints() + 100);

// 正确做法
membership.earnPoints(Points.of(100), "购物消费");
```

### 2. 值对象封装业务逻辑

`Points` 值对象封装积分运算规则，防止非法操作：

```java
public Points subtract(Points other) {
    int result = this.value - other.value;
    if (result < 0) {
        throw new DomainException("积分不足");
    }
    return new Points(result);
}
```

### 3. 领域服务协调外部能力

`MembershipDomainService` 负责需要外部能力参与的领域逻辑，例如根据订单金额计算赠送积分。

### 4. 自动等级升级

会员等级根据积分自动升级，业务规则封装在 `Membership` 聚合根内部：

```java
private void tryUpgradeLevel() {
    MembershipLevel calculatedLevel = MembershipLevel.calculateLevel(this.points.getValue());
    if (calculatedLevel.ordinal() > this.level.ordinal()) {
        this.level = calculatedLevel;
    }
}
```

## 与 Order 领域的集成点

虽然当前实现中 Membership 和 Order 是独立的限界上下文，但未来可以通过以下方式集成：

1. **订单完成后自动赠送积分** - 在 Order 聚合发布领域事件，Membership 领域监听事件并赠送积分
2. **积分抵扣订单金额** - Order 领域调用 Membership 领域的防腐层接口，查询可用积分
3. **会员专享优惠** - Order 领域根据会员等级提供差异化的定价策略

## 测试

运行 Membership 领域层测试：

```bash
mvn test -pl ddd-demo-domain -Dtest=MembershipTest
```

测试用例覆盖：
- 会员创建
- 积分累积
- 等级自动升级
- 积分消费
- 积分不足异常
- 参数校验
