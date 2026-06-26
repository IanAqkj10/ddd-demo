package com.example.ddd.domain.iam.model;

import com.example.ddd.domain.shared.DomainException;
import java.util.Objects;

public final class RoleId {

    private final Long value;

    private RoleId(Long value) {
        if (value == null || value <= 0L) {
            throw new DomainException("角色ID必须大于0");
        }
        this.value = value;
    }

    public static RoleId of(Long value) {
        return new RoleId(value);
    }

    public Long getValue() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof RoleId)) {
            return false;
        }
        RoleId that = (RoleId) other;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
