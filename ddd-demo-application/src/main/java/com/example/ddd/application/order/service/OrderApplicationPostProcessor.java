package com.example.ddd.application.order.service;

import com.example.ddd.application.order.port.OrderApplicationEventPublisher;
import com.example.ddd.application.order.port.OrderOperationLogRecorder;
import com.example.ddd.domain.order.model.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderApplicationPostProcessor {

    private final OrderOperationLogRecorder orderOperationLogRecorder;
    private final OrderApplicationEventPublisher orderApplicationEventPublisher;

    public OrderApplicationPostProcessor(
        OrderOperationLogRecorder orderOperationLogRecorder,
        OrderApplicationEventPublisher orderApplicationEventPublisher
    ) {
        this.orderOperationLogRecorder = orderOperationLogRecorder;
        this.orderApplicationEventPublisher = orderApplicationEventPublisher;
    }

    public void afterOrderPlaced(Order order) {
        orderOperationLogRecorder.recordOrderPlaced(order);
        orderApplicationEventPublisher.publishOrderCreated(order);
    }

    public void afterOrderCanceled(Order order) {
        orderOperationLogRecorder.recordOrderCanceled(order);
        orderApplicationEventPublisher.publishOrderCanceled(order);
    }
}
