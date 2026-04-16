package com.example.ddd.application.order.validator;

import com.example.ddd.application.order.command.CancelOrderCommand;
import com.example.ddd.domain.shared.DomainException;
import org.springframework.stereotype.Component;

@Component
public class CancelOrderCommandValidator {

    public void validate(CancelOrderCommand command) {
        if (command == null) {
            throw new DomainException("取消命令不能为空");
        }
    }
}
