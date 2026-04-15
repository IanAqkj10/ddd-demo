package com.example.ddd.infrastructure.order.gateway;

import com.example.ddd.domain.order.gateway.InventoryGateway;
import com.example.ddd.domain.order.model.OrderItem;
import com.example.ddd.domain.shared.DomainException;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class FakeInventoryGateway implements InventoryGateway {

    @Override
    public void checkAvailable(List<OrderItem> orderItems) {
        for (OrderItem orderItem : orderItems) {
            if (orderItem.getQuantity() > 99 || Long.valueOf(9999L).equals(orderItem.getProductId().getValue())) {
                throw new DomainException("商品库存不足，无法下单");
            }
        }
    }
}
