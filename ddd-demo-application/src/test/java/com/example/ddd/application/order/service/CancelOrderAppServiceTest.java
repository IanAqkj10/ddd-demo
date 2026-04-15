package com.example.ddd.application.order.service;

import com.example.ddd.application.order.assembler.OrderDtoAssembler;
import com.example.ddd.application.order.command.CancelOrderCommand;
import com.example.ddd.application.order.dto.OrderDetailDTO;
import com.example.ddd.domain.order.model.Money;
import com.example.ddd.domain.order.model.Order;
import com.example.ddd.domain.order.model.OrderId;
import com.example.ddd.domain.order.model.OrderItem;
import com.example.ddd.domain.order.model.ProductId;
import com.example.ddd.domain.order.model.UserId;
import com.example.ddd.domain.order.repository.OrderRepository;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CancelOrderAppServiceTest {

    @Test
    void shouldCancelOrderWhenOrderExists() {
        InMemoryOrderRepository orderRepository = new InMemoryOrderRepository();
        Order order = Order.create(
            OrderId.of("order-100"),
            UserId.of(1001L),
            Collections.singletonList(
                OrderItem.of(ProductId.of(2001L), "DDD 实战课程", 1, Money.of(new BigDecimal("199.00")))
            )
        );
        orderRepository.save(order);
        CancelOrderAppService appService = new CancelOrderAppService(orderRepository, new OrderDtoAssembler());

        OrderDetailDTO detail = appService.cancelOrder(new CancelOrderCommand("order-100", "用户主动取消"));

        Assertions.assertEquals("CANCELED", detail.getStatus());
        Assertions.assertEquals("用户主动取消", detail.getCancelReason());
    }

    private static class InMemoryOrderRepository implements OrderRepository {

        private final Map<String, Order> store = new HashMap<String, Order>();

        @Override
        public void save(Order order) {
            store.put(order.getId().getValue(), order);
        }

        @Override
        public Optional<Order> findById(OrderId orderId) {
            return Optional.ofNullable(store.get(orderId.getValue()));
        }
    }
}
