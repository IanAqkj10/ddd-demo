package com.example.ddd.domain.order.gateway;

import com.example.ddd.domain.order.model.OrderItem;
import java.util.List;

public interface InventoryGateway {

    void checkAvailable(List<OrderItem> orderItems);
}
