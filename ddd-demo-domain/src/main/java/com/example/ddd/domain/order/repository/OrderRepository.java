package com.example.ddd.domain.order.repository;

import com.example.ddd.domain.order.model.Order;
import com.example.ddd.domain.order.model.OrderId;
import java.util.Optional;

public interface OrderRepository {

    void save(Order order);

    Optional<Order> findById(OrderId orderId);
}
