package com.example.ddd.application.order.validator;

import com.example.ddd.application.order.command.CancelOrderCommand;
import com.example.ddd.domain.shared.DomainException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CancelOrderCommandValidatorTest {

    @Test
    void shouldRejectNullCancelCommand() {
        CancelOrderCommandValidator validator = new CancelOrderCommandValidator();

        DomainException exception = Assertions.assertThrows(DomainException.class, () -> validator.validate(null));

        Assertions.assertEquals("取消命令不能为空", exception.getMessage());
    }

    @Test
    void shouldPassValidationForLegalCancelCommand() {
        CancelOrderCommandValidator validator = new CancelOrderCommandValidator();

        Assertions.assertDoesNotThrow(() -> validator.validate(new CancelOrderCommand("order-100", "用户主动取消")));
    }
}
