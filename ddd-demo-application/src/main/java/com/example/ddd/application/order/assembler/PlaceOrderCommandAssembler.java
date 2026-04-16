package com.example.ddd.application.order.assembler;

import com.example.ddd.application.order.command.PlaceOrderCommand;
import com.example.ddd.application.order.command.PlaceOrderItemCommand;
import com.example.ddd.domain.order.model.Money;
import com.example.ddd.domain.order.model.OrderItem;
import com.example.ddd.domain.order.model.ProductId;
import com.example.ddd.domain.order.model.UserId;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class PlaceOrderCommandAssembler {

    public UserId toUserId(PlaceOrderCommand command) {
        return UserId.of(command.getUserId());
    }

    public List<OrderItem> toOrderItems(PlaceOrderCommand command) {
        List<OrderItem> items = new ArrayList<OrderItem>();
        for (PlaceOrderItemCommand item : command.getItems()) {
            items.add(
                OrderItem.of(
                    ProductId.of(item.getProductId()),
                    item.getProductName(),
                    item.getQuantity(),
                    Money.of(item.getSalePrice())
                )
            );
        }
        return items;
    }
}
