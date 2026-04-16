package com.example.ddd.application.order.service;

import com.example.ddd.application.order.command.PlaceOrderCommand;
import com.example.ddd.application.order.command.PlaceOrderItemCommand;
import com.example.ddd.domain.shared.DomainException;
import java.math.BigDecimal;
import java.util.Collections;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PlaceOrderPreCheckServiceTest {

    @Test
    void shouldRejectBlockedUserBeforePlaceOrder() {
        PlaceOrderPreCheckService service = new PlaceOrderPreCheckService();
        PlaceOrderCommand command = new PlaceOrderCommand(
            999999L,
            Collections.singletonList(
                new PlaceOrderItemCommand(2001L, "DDD 实战课程", 1, new BigDecimal("199.00"))
            )
        );

        DomainException exception = Assertions.assertThrows(DomainException.class, () -> service.check(command));

        Assertions.assertEquals("当前用户被限制下单", exception.getMessage());
    }

    @Test
    void shouldPassPreCheckForNormalUser() {
        PlaceOrderPreCheckService service = new PlaceOrderPreCheckService();
        PlaceOrderCommand command = new PlaceOrderCommand(
            1001L,
            Collections.singletonList(
                new PlaceOrderItemCommand(2001L, "DDD 实战课程", 1, new BigDecimal("199.00"))
            )
        );

        Assertions.assertDoesNotThrow(() -> service.check(command));
    }
}
