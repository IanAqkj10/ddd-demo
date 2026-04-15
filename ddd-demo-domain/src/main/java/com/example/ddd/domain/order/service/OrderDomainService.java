package com.example.ddd.domain.order.service;

import com.example.ddd.domain.order.gateway.InventoryGateway;
import com.example.ddd.domain.order.model.Order;
import com.example.ddd.domain.order.model.OrderId;
import com.example.ddd.domain.order.model.OrderItem;
import com.example.ddd.domain.order.model.UserId;
import java.util.List;

public class OrderDomainService {

    private final InventoryGateway inventoryGateway;

    public OrderDomainService(InventoryGateway inventoryGateway) {
        this.inventoryGateway = inventoryGateway;
    }

    public Order placeOrder(UserId userId, List<OrderItem> orderItems) {
        inventoryGateway.checkAvailable(orderItems);
        return Order.create(OrderId.newId(), userId, orderItems);
    }
}
