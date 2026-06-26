package com.example.ddd.domain.iam.model;

import com.example.ddd.domain.shared.DomainException;
import java.util.Objects;

public final class IamUserId {

    private final Long value;

    private IamUserId(Long value) {
        if (value == null || value <= 0L) {
            throw new DomainException("用户ID必须大于0");
        }
        this.value = value;
    }

    public static IamUserId of(Long value) {
        return new IamUserId(value);
    }

    public Long getValue() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof IamUserId)) {
            return false;
        }
        IamUserId that = (IamUserId) other;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
