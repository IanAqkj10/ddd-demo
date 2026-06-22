package com.example.ddd.domain.membership.model;

import com.example.ddd.domain.shared.DomainException;
import java.util.Objects;

public final class Points {

    private final int value;

    private Points(int value) {
        if (value < 0) {
            throw new DomainException("积分不能为负数");
        }
        this.value = value;
    }

    public static Points of(int value) {
        return new Points(value);
    }

    public static Points zero() {
        return new Points(0);
    }

    public Points add(Points other) {
        return new Points(this.value + other.value);
    }

    public Points subtract(Points other) {
        int result = this.value - other.value;
        if (result < 0) {
            throw new DomainException("积分不足");
        }
        return new Points(result);
    }

    public boolean greaterThanOrEqual(Points other) {
        return this.value >= other.value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Points)) {
            return false;
        }
        Points points = (Points) other;
        return value == points.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
