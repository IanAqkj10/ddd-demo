package com.example.ddd.application.order.command;

import java.util.List;

public class PlaceOrderCommand {

    private final Long userId;
    private final List<PlaceOrderItemCommand> items;

    public PlaceOrderCommand(Long userId, List<PlaceOrderItemCommand> items) {
        this.userId = userId;
        this.items = items;
    }

    public Long getUserId() {
        return userId;
    }

    public List<PlaceOrderItemCommand> getItems() {
        return items;
    }
}
