package com.example.ddd.application.order.validator;

import com.example.ddd.application.order.command.PlaceOrderCommand;
import com.example.ddd.application.order.command.PlaceOrderItemCommand;
import com.example.ddd.domain.shared.DomainException;
import java.math.BigDecimal;
import java.util.Collections;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PlaceOrderCommandValidatorTest {

    @Test
    void shouldRejectNullPlaceOrderCommand() {
        PlaceOrderCommandValidator validator = new PlaceOrderCommandValidator();

        DomainException exception = Assertions.assertThrows(DomainException.class, () -> validator.validate(null));

        Assertions.assertEquals("下单命令不能为空", exception.getMessage());
    }

    @Test
    void shouldRejectCommandWithoutItems() {
        PlaceOrderCommandValidator validator = new PlaceOrderCommandValidator();

        DomainException exception = Assertions.assertThrows(
            DomainException.class,
            () -> validator.validate(new PlaceOrderCommand(1001L, Collections.<PlaceOrderItemCommand>emptyList()))
        );

        Assertions.assertEquals("订单项不能为空", exception.getMessage());
    }

    @Test
    void shouldPassValidationForLegalCommand() {
        PlaceOrderCommandValidator validator = new PlaceOrderCommandValidator();

        Assertions.assertDoesNotThrow(
            () -> validator.validate(
                new PlaceOrderCommand(
                    1001L,
                    Collections.singletonList(
                        new PlaceOrderItemCommand(2001L, "DDD 实战课程", 1, new BigDecimal("199.00"))
                    )
                )
            )
        );
    }
}
