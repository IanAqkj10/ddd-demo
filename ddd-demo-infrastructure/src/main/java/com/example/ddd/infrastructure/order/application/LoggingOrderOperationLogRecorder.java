package com.example.ddd.infrastructure.order.application;

import com.example.ddd.application.order.port.OrderOperationLogRecorder;
import com.example.ddd.domain.order.model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LoggingOrderOperationLogRecorder implements OrderOperationLogRecorder {

    private static final Logger log = LoggerFactory.getLogger(LoggingOrderOperationLogRecorder.class);

    @Override
    public void recordOrderPlaced(Order order) {
        log.info("application-log order placed, orderId={}, status={}", order.getId().getValue(), order.getStatus());
    }

    @Override
    public void recordOrderCanceled(Order order) {
        log.info("application-log order canceled, orderId={}, status={}", order.getId().getValue(), order.getStatus());
    }
}
