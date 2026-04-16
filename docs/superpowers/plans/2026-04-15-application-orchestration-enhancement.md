# Application Orchestration Enhancement Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 让 `application` 层更明显承担用例流程编排，体现“预检查 -> 领域执行 -> 持久化 -> 后处理”的 application 职责。

**Architecture:** 在 `ddd-demo-application` 中新增 `PlaceOrderPreCheckService` 与 `OrderApplicationPostProcessor`，并通过 application 端口抽象出“操作日志记录”和“应用事件发布”。基础设施层提供 logger 风格实现，AppService 负责串联这些步骤，不直接调用其他 AppService。

**Tech Stack:** Java 8, Spring Boot 2.7, JUnit 5, Maven

---

### Task 1: 为 pre-check 与 post-process 写失败测试

**Files:**
- Create: `ddd-demo-application/src/test/java/com/example/ddd/application/order/service/PlaceOrderPreCheckServiceTest.java`
- Create: `ddd-demo-application/src/test/java/com/example/ddd/application/order/service/OrderApplicationPostProcessorTest.java`
- Modify: `ddd-demo-application/src/test/java/com/example/ddd/application/order/service/PlaceOrderAppServiceTest.java`
- Modify: `ddd-demo-application/src/test/java/com/example/ddd/application/order/service/CancelOrderAppServiceTest.java`

- [ ] 覆盖“被限制用户不可下单”的 application 预检查
- [ ] 覆盖“下单后写操作日志并发布应用事件”的后处理
- [ ] 先运行 `mvn -pl ddd-demo-application -am test`，确认失败点是新类未实现或构造参数不匹配

### Task 2: 实现 application 编排组件与端口

**Files:**
- Create: `ddd-demo-application/src/main/java/com/example/ddd/application/order/service/PlaceOrderPreCheckService.java`
- Create: `ddd-demo-application/src/main/java/com/example/ddd/application/order/service/OrderApplicationPostProcessor.java`
- Create: `ddd-demo-application/src/main/java/com/example/ddd/application/order/port/OrderOperationLogRecorder.java`
- Create: `ddd-demo-application/src/main/java/com/example/ddd/application/order/port/OrderApplicationEventPublisher.java`

- [ ] 在 `PlaceOrderPreCheckService` 中表达 application 级前置检查
- [ ] 在 `OrderApplicationPostProcessor` 中串联操作日志和应用事件
- [ ] 保持这些组件不承载领域规则本身，只承载编排动作

### Task 3: 接入 AppService 与 infrastructure 实现

**Files:**
- Modify: `ddd-demo-application/src/main/java/com/example/ddd/application/order/service/PlaceOrderAppService.java`
- Modify: `ddd-demo-application/src/main/java/com/example/ddd/application/order/service/CancelOrderAppService.java`
- Create: `ddd-demo-infrastructure/src/main/java/com/example/ddd/infrastructure/order/application/LoggingOrderOperationLogRecorder.java`
- Create: `ddd-demo-infrastructure/src/main/java/com/example/ddd/infrastructure/order/application/LoggingOrderApplicationEventPublisher.java`

- [ ] 让 `PlaceOrderAppService` 执行“validator -> preCheck -> assembler -> domain -> repository -> postProcessor”
- [ ] 让 `CancelOrderAppService` 在保存后执行 postProcessor
- [ ] 由 infrastructure 提供 application 端口实现，避免 application 直接依赖技术细节

### Task 4: 更新导读文档并验证

**Files:**
- Modify: `docs/how-to-read-ddd-demo.md`

- [ ] 更新 application 层导读和链路图
- [ ] 运行 `mvn -pl ddd-demo-application -am test`
- [ ] 运行 `/opt/homebrew/Cellar/maven/3.9.14/libexec/bin/mvn test`
