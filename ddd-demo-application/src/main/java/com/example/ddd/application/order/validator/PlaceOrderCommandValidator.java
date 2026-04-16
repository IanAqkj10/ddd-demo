package com.example.ddd.application.order.validator;

import com.example.ddd.application.order.command.PlaceOrderCommand;
import com.example.ddd.domain.shared.DomainException;
import org.springframework.stereotype.Component;

@Component
public class PlaceOrderCommandValidator {

    public void validate(PlaceOrderCommand command) {
        if (command == null) {
            throw new DomainException("下单命令不能为空");
        }
        if (command.getItems() == null || command.getItems().isEmpty()) {
            throw new DomainException("订单项不能为空");
        }
    }
}
