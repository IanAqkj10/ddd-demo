package com.example.ddd.infrastructure.order.application;

import com.example.ddd.application.order.port.OrderApplicationEventPublisher;
import com.example.ddd.domain.order.model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LoggingOrderApplicationEventPublisher implements OrderApplicationEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(LoggingOrderApplicationEventPublisher.class);

    @Override
    public void publishOrderCreated(Order order) {
        log.info("application-event order created, orderId={}, totalAmount={}", order.getId().getValue(), order.getTotalAmount());
    }

    @Override
    public void publishOrderCanceled(Order order) {
        log.info("application-event order canceled, orderId={}, cancelReason={}", order.getId().getValue(), order.getCancelReason());
    }
}
