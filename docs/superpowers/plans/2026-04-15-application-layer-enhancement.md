# Application Layer Enhancement Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 强化 `ddd-demo-application` 层的“用例编排”表达，让下单与取消订单的应用层职责更清晰。

**Architecture:** 在 `application` 层增加命令校验器与命令装配器，把输入校验、命令到领域对象的转换、结果 DTO 组装从 AppService 中拆出。领域规则继续保留在 `domain` 层，应用层只负责用例编排。

**Tech Stack:** Java 8, Spring Boot 2.7, JUnit 5, Maven

---

### Task 1: 为新的 application 组件写失败测试

**Files:**
- Create: `ddd-demo-application/src/test/java/com/example/ddd/application/order/validator/PlaceOrderCommandValidatorTest.java`
- Create: `ddd-demo-application/src/test/java/com/example/ddd/application/order/validator/CancelOrderCommandValidatorTest.java`
- Create: `ddd-demo-application/src/test/java/com/example/ddd/application/order/assembler/PlaceOrderCommandAssemblerTest.java`

- [ ] 覆盖“下单命令不能为空”“订单项不能为空”“取消命令不能为空”等应用层入口校验
- [ ] 覆盖 `PlaceOrderCommand` 到 `UserId` / `OrderItem` 的转换行为
- [ ] 先运行目标测试，确认失败原因是类尚未实现

### Task 2: 实现 validator 与 assembler

**Files:**
- Create: `ddd-demo-application/src/main/java/com/example/ddd/application/order/validator/PlaceOrderCommandValidator.java`
- Create: `ddd-demo-application/src/main/java/com/example/ddd/application/order/validator/CancelOrderCommandValidator.java`
- Create: `ddd-demo-application/src/main/java/com/example/ddd/application/order/assembler/PlaceOrderCommandAssembler.java`
- Modify: `ddd-demo-application/src/main/java/com/example/ddd/application/order/assembler/OrderDtoAssembler.java`

- [ ] 在 validator 中承担应用层入口校验
- [ ] 在 assembler 中承担命令到领域对象的转换
- [ ] 保持 DTO assembler 只负责 `Order -> OrderDetailDTO`

### Task 3: 重构 AppService 让编排职责更清晰

**Files:**
- Modify: `ddd-demo-application/src/main/java/com/example/ddd/application/order/service/PlaceOrderAppService.java`
- Modify: `ddd-demo-application/src/main/java/com/example/ddd/application/order/service/CancelOrderAppService.java`
- Modify: `ddd-demo-application/src/main/java/com/example/ddd/application/order/service/GetOrderDetailAppService.java`
- Modify: `ddd-demo-application/src/test/java/com/example/ddd/application/order/service/PlaceOrderAppServiceTest.java`
- Modify: `ddd-demo-application/src/test/java/com/example/ddd/application/order/service/CancelOrderAppServiceTest.java`

- [ ] 让 `PlaceOrderAppService` 明确执行“校验 -> 转换 -> 调领域服务 -> 保存 -> 返回”
- [ ] 让 `CancelOrderAppService` 明确执行“校验 -> 查聚合 -> 执行业务行为 -> 保存 -> 返回”
- [ ] 保持已有行为不变，测试继续通过

### Task 4: 更新导读文档并验证

**Files:**
- Modify: `docs/how-to-read-ddd-demo.md`

- [ ] 补充 application 层为什么要拆 validator / assembler / service
- [ ] 运行 `mvn -pl ddd-demo-application test`
- [ ] 运行 `/opt/homebrew/Cellar/maven/3.9.14/libexec/bin/mvn test`
