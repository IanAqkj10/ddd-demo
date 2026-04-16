package com.example.ddd.application.order.port;

import com.example.ddd.domain.order.model.Order;

public interface OrderApplicationEventPublisher {

    void publishOrderCreated(Order order);

    void publishOrderCanceled(Order order);
}
