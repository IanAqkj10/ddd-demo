package com.example.ddd.domain.order.model;

import com.example.ddd.domain.shared.DomainException;
import java.util.Objects;

public final class ProductId {

    private final Long value;

    private ProductId(Long value) {
        if (value == null || value <= 0L) {
            throw new DomainException("商品ID必须大于0");
        }
        this.value = value;
    }

    public static ProductId of(Long value) {
        return new ProductId(value);
    }

    public Long getValue() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof ProductId)) {
            return false;
        }
        ProductId productId = (ProductId) other;
        return Objects.equals(value, productId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
