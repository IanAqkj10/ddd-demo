package com.example.ddd.infrastructure.order.assembler;

import com.example.ddd.domain.order.model.Money;
import com.example.ddd.domain.order.model.Order;
import com.example.ddd.domain.order.model.OrderId;
import com.example.ddd.domain.order.model.OrderItem;
import com.example.ddd.domain.order.model.OrderStatus;
import com.example.ddd.domain.order.model.ProductId;
import com.example.ddd.domain.order.model.UserId;
import com.example.ddd.infrastructure.order.dataobject.OrderDO;
import com.example.ddd.infrastructure.order.dataobject.OrderItemDO;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class OrderDataAssembler {

    public OrderDO toOrderDO(Order order) {
        OrderDO orderDO = new OrderDO();
        orderDO.setOrderId(order.getId().getValue());
        orderDO.setUserId(order.getUserId().getValue());
        orderDO.setStatus(order.getStatus().name());
        orderDO.setTotalAmount(order.getTotalAmount().toBigDecimal());
        orderDO.setCancelReason(order.getCancelReason());
        orderDO.setCreatedAt(order.getCreatedAt());
        orderDO.setCanceledAt(order.getCanceledAt());
        return orderDO;
    }

    public List<OrderItemDO> toOrderItemDOs(Order order) {
        List<OrderItemDO> itemDOs = new ArrayList<OrderItemDO>();
        for (OrderItem item : order.getItems()) {
            OrderItemDO itemDO = new OrderItemDO();
            itemDO.setOrderId(order.getId().getValue());
            itemDO.setProductId(item.getProductId().getValue());
            itemDO.setProductName(item.getProductName());
            itemDO.setQuantity(item.getQuantity());
            itemDO.setSalePrice(item.getSalePrice().toBigDecimal());
            itemDO.setSubtotalAmount(item.getSubtotalAmount().toBigDecimal());
            itemDOs.add(itemDO);
        }
        return itemDOs;
    }

    public Order toOrder(OrderDO orderDO, List<OrderItemDO> itemDOs) {
        List<OrderItem> items = new ArrayList<OrderItem>();
        for (OrderItemDO itemDO : itemDOs) {
            items.add(
                OrderItem.of(
                    ProductId.of(itemDO.getProductId()),
                    itemDO.getProductName(),
                    itemDO.getQuantity(),
                    Money.of(itemDO.getSalePrice())
                )
            );
        }
        return Order.restore(
            OrderId.of(orderDO.getOrderId()),
            UserId.of(orderDO.getUserId()),
            items,
            OrderStatus.valueOf(orderDO.getStatus()),
            orderDO.getCancelReason(),
            orderDO.getCreatedAt(),
            orderDO.getCanceledAt()
        );
    }
}
