package com.example.ddd.application.order.service;

import com.example.ddd.application.order.assembler.OrderDtoAssembler;
import com.example.ddd.application.order.assembler.PlaceOrderCommandAssembler;
import com.example.ddd.application.order.command.PlaceOrderCommand;
import com.example.ddd.application.order.command.PlaceOrderItemCommand;
import com.example.ddd.application.order.dto.OrderDetailDTO;
import com.example.ddd.application.order.port.OrderApplicationEventPublisher;
import com.example.ddd.application.order.port.OrderOperationLogRecorder;
import com.example.ddd.application.order.validator.PlaceOrderCommandValidator;
import com.example.ddd.domain.order.gateway.InventoryGateway;
import com.example.ddd.domain.order.model.Order;
import com.example.ddd.domain.order.model.OrderId;
import com.example.ddd.domain.order.model.OrderItem;
import com.example.ddd.domain.order.repository.OrderRepository;
import com.example.ddd.domain.order.service.OrderDomainService;
import com.example.ddd.domain.shared.DomainException;
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
        CountingLogRecorder logRecorder = new CountingLogRecorder();
        CountingEventPublisher eventPublisher = new CountingEventPublisher();
        PlaceOrderAppService appService = new PlaceOrderAppService(
            new PlaceOrderCommandValidator(),
            new PlaceOrderPreCheckService(),
            new PlaceOrderCommandAssembler(),
            new OrderDomainService(inventoryGateway),
            orderRepository,
            new OrderApplicationPostProcessor(logRecorder, eventPublisher),
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
        Assertions.assertEquals(1, logRecorder.placedCount);
        Assertions.assertEquals(1, eventPublisher.createdCount);
    }

    @Test
    void shouldRejectBlockedUserBeforeCallingDomainService() {
        InMemoryOrderRepository orderRepository = new InMemoryOrderRepository();
        CountingInventoryGateway inventoryGateway = new CountingInventoryGateway();
        PlaceOrderAppService appService = new PlaceOrderAppService(
            new PlaceOrderCommandValidator(),
            new PlaceOrderPreCheckService(),
            new PlaceOrderCommandAssembler(),
            new OrderDomainService(inventoryGateway),
            orderRepository,
            new OrderApplicationPostProcessor(new CountingLogRecorder(), new CountingEventPublisher()),
            new OrderDtoAssembler()
        );

        DomainException exception = Assertions.assertThrows(
            DomainException.class,
            () -> appService.placeOrder(
                new PlaceOrderCommand(
                    999999L,
                    Collections.singletonList(
                        new PlaceOrderItemCommand(2001L, "DDD 实战课程", 1, new BigDecimal("199.00"))
                    )
                )
            )
        );

        Assertions.assertEquals("当前用户被限制下单", exception.getMessage());
        Assertions.assertEquals(0, inventoryGateway.calledCount);
        Assertions.assertEquals(0, orderRepository.store.size());
    }

    private static class PassingInventoryGateway implements InventoryGateway {

        @Override
        public void checkAvailable(List<OrderItem> orderItems) {
        }
    }

    private static class CountingInventoryGateway implements InventoryGateway {

        private int calledCount;

        @Override
        public void checkAvailable(List<OrderItem> orderItems) {
            calledCount++;
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

    private static class CountingLogRecorder implements OrderOperationLogRecorder {

        private int placedCount;

        @Override
        public void recordOrderPlaced(Order order) {
            placedCount++;
        }

        @Override
        public void recordOrderCanceled(Order order) {
        }
    }

    private static class CountingEventPublisher implements OrderApplicationEventPublisher {

        private int createdCount;

        @Override
        public void publishOrderCreated(Order order) {
            createdCount++;
        }

        @Override
        public void publishOrderCanceled(Order order) {
        }
    }
}
