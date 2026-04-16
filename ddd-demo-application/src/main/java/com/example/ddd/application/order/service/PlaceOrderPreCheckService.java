package com.example.ddd.application.order.service;

import com.example.ddd.application.order.command.PlaceOrderCommand;
import com.example.ddd.domain.shared.DomainException;
import org.springframework.stereotype.Component;

@Component
public class PlaceOrderPreCheckService {

    public void check(PlaceOrderCommand command) {
        if (Long.valueOf(999999L).equals(command.getUserId())) {
            throw new DomainException("当前用户被限制下单");
        }
    }
}
