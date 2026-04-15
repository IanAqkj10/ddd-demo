package com.example.ddd.application.order.service;

import com.example.ddd.application.order.assembler.OrderDtoAssembler;
import com.example.ddd.application.order.command.PlaceOrderCommand;
import com.example.ddd.application.order.command.PlaceOrderItemCommand;
import com.example.ddd.application.order.dto.OrderDetailDTO;
import com.example.ddd.domain.order.gateway.InventoryGateway;
import com.example.ddd.domain.order.model.Order;
import com.example.ddd.domain.order.model.OrderId;
import com.example.ddd.domain.order.model.OrderItem;
import com.example.ddd.domain.order.repository.OrderRepository;
import com.example.ddd.domain.order.service.OrderDomainService;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PlaceOrderAppServiceTest {

    @Test
    void shouldSaveOrderAndReturnOrderDetailWhenPlaceOrder() {
        InMemoryOrderRepository orderRepository = new InMemoryOrderRepository();
        InventoryGateway inventoryGateway = new PassingInventoryGateway();
        PlaceOrderAppService appService = new PlaceOrderAppService(
            new OrderDomainService(inventoryGateway),
            orderRepository,
            new OrderDtoAssembler()
        );

        OrderDetailDTO detail = appService.placeOrder(
            new PlaceOrderCommand(
                1001L,
                Collections.singletonList(
                    new PlaceOrderItemCommand(2001L, "DDD 实战课程", 2, new BigDecimal("199.00"))
                )
            )
        );

        Assertions.assertNotNull(detail.getOrderId());
        Assertions.assertEquals("CREATED", detail.getStatus());
        Assertions.assertEquals(new BigDecimal("398.00"), detail.getTotalAmount());
        Assertions.assertEquals(1, orderRepository.store.size());
    }

    private static class PassingInventoryGateway implements InventoryGateway {

        @Override
        public void checkAvailable(List<OrderItem> orderItems) {
        }
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
