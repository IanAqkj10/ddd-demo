package com.example.ddd.domain.order.model;

import com.example.ddd.domain.shared.DomainException;
import java.util.Objects;
import java.util.UUID;

public final class OrderId {

    private final String value;

    private OrderId(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new DomainException("订单ID不能为空");
        }
        this.value = value;
    }

    public static OrderId of(String value) {
        return new OrderId(value);
    }

    public static OrderId newId() {
        return new OrderId(UUID.randomUUID().toString().replace("-", ""));
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof OrderId)) {
            return false;
        }
        OrderId orderId = (OrderId) other;
        return Objects.equals(value, orderId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
