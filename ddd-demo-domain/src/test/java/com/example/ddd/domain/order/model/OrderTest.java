package com.example.ddd.domain.order.model;

import com.example.ddd.domain.shared.DomainException;
import java.math.BigDecimal;
import java.util.Arrays;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class OrderTest {

    @Test
    void shouldCalculateTotalAmountWhenCreateOrder() {
        Order order = Order.create(
            OrderId.of("order-1"),
            UserId.of(1001L),
            Arrays.asList(
                OrderItem.of(ProductId.of(2001L), "DDD 实战课程", 2, Money.of(new BigDecimal("199.00"))),
                OrderItem.of(ProductId.of(2002L), "聚合设计指南", 1, Money.of(new BigDecimal("88.00")))
            )
        );

        Assertions.assertEquals(OrderStatus.CREATED, order.getStatus());
        Assertions.assertEquals(new BigDecimal("486.00"), order.getTotalAmount().toBigDecimal());
    }

    @Test
    void shouldChangeStatusWhenCancelOrder() {
        Order order = Order.create(
            OrderId.of("order-2"),
            UserId.of(1002L),
            Arrays.asList(
                OrderItem.of(ProductId.of(2003L), "领域建模实践", 1, Money.of(new BigDecimal("99.00")))
            )
        );

        order.cancel("用户主动取消");

        Assertions.assertEquals(OrderStatus.CANCELED, order.getStatus());
        Assertions.assertEquals("用户主动取消", order.getCancelReason());
        Assertions.assertNotNull(order.getCanceledAt());
    }

    @Test
    void shouldRejectCancelCanceledOrder() {
        Order order = Order.create(
            OrderId.of("order-3"),
            UserId.of(1003L),
            Arrays.asList(
                OrderItem.of(ProductId.of(2004L), "限界上下文手册", 1, Money.of(new BigDecimal("66.00")))
            )
        );
        order.cancel("第一次取消");

        DomainException exception = Assertions.assertThrows(DomainException.class, () -> order.cancel("重复取消"));

        Assertions.assertEquals("只有已创建订单可以取消", exception.getMessage());
    }
}
