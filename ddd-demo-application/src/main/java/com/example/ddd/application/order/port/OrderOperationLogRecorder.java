package com.example.ddd.application.order.port;

import com.example.ddd.domain.order.model.Order;

public interface OrderOperationLogRecorder {

    void recordOrderPlaced(Order order);

    void recordOrderCanceled(Order order);
}
