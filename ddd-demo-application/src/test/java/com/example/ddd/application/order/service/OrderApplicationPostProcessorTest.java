package com.example.ddd.application.order.service;

import com.example.ddd.application.order.port.OrderApplicationEventPublisher;
import com.example.ddd.application.order.port.OrderOperationLogRecorder;
import com.example.ddd.domain.order.model.Money;
import com.example.ddd.domain.order.model.Order;
import com.example.ddd.domain.order.model.OrderId;
import com.example.ddd.domain.order.model.OrderItem;
import com.example.ddd.domain.order.model.ProductId;
import com.example.ddd.domain.order.model.UserId;
import java.math.BigDecimal;
import java.util.Collections;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class OrderApplicationPostProcessorTest {

    @Test
    void shouldRecordLogAndPublishEventAfterOrderPlaced() {
        CountingLogRecorder logRecorder = new CountingLogRecorder();
        CountingEventPublisher eventPublisher = new CountingEventPublisher();
        OrderApplicationPostProcessor postProcessor = new OrderApplicationPostProcessor(logRecorder, eventPublisher);
        Order order = Order.create(
            OrderId.of("order-100"),
            UserId.of(1001L),
            Collections.singletonList(
                OrderItem.of(ProductId.of(2001L), "DDD 实战课程", 1, Money.of(new BigDecimal("199.00")))
            )
        );

        postProcessor.afterOrderPlaced(order);

        Assertions.assertEquals(1, logRecorder.placedCount);
        Assertions.assertEquals(1, eventPublisher.createdCount);
    }

    @Test
    void shouldRecordLogAndPublishEventAfterOrderCanceled() {
        CountingLogRecorder logRecorder = new CountingLogRecorder();
        CountingEventPublisher eventPublisher = new CountingEventPublisher();
        OrderApplicationPostProcessor postProcessor = new OrderApplicationPostProcessor(logRecorder, eventPublisher);
        Order order = Order.create(
            OrderId.of("order-101"),
            UserId.of(1001L),
            Collections.singletonList(
                OrderItem.of(ProductId.of(2001L), "DDD 实战课程", 1, Money.of(new BigDecimal("199.00")))
            )
        );
        order.cancel("用户主动取消");

        postProcessor.afterOrderCanceled(order);

        Assertions.assertEquals(1, logRecorder.canceledCount);
        Assertions.assertEquals(1, eventPublisher.canceledCount);
    }

    private static class CountingLogRecorder implements OrderOperationLogRecorder {

        private int placedCount;
        private int canceledCount;

        @Override
        public void recordOrderPlaced(Order order) {
            placedCount++;
        }

        @Override
        public void recordOrderCanceled(Order order) {
            canceledCount++;
        }
    }

    private static class CountingEventPublisher implements OrderApplicationEventPublisher {

        private int createdCount;
        private int canceledCount;

        @Override
        public void publishOrderCreated(Order order) {
            createdCount++;
        }

        @Override
        public void publishOrderCanceled(Order order) {
            canceledCount++;
        }
    }
}
