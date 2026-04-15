package com.example.ddd.domain.order.model;

import com.example.ddd.domain.shared.DomainException;
import java.util.Objects;

public final class UserId {

    private final Long value;

    private UserId(Long value) {
        if (value == null || value <= 0L) {
            throw new DomainException("用户ID必须大于0");
        }
        this.value = value;
    }

    public static UserId of(Long value) {
        return new UserId(value);
    }

    public Long getValue() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof UserId)) {
            return false;
        }
        UserId userId = (UserId) other;
        return Objects.equals(value, userId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
