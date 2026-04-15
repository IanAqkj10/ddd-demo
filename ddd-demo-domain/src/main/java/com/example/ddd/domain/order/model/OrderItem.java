package com.example.ddd.domain.order.model;

import com.example.ddd.domain.shared.DomainException;
import java.util.Objects;

public class OrderItem {

    private final ProductId productId;
    private final String productName;
    private final int quantity;
    private final Money salePrice;
    private final Money subtotalAmount;

    private OrderItem(ProductId productId, String productName, int quantity, Money salePrice) {
        if (productId == null) {
            throw new DomainException("商品ID不能为空");
        }
        if (productName == null || productName.trim().isEmpty()) {
            throw new DomainException("商品名称不能为空");
        }
        if (quantity <= 0) {
            throw new DomainException("商品数量必须大于0");
        }
        if (salePrice == null) {
            throw new DomainException("销售单价不能为空");
        }
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.salePrice = salePrice;
        this.subtotalAmount = salePrice.multiply(quantity);
    }

    public static OrderItem of(ProductId productId, String productName, int quantity, Money salePrice) {
        return new OrderItem(productId, productName, quantity, salePrice);
    }

    public ProductId getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public Money getSalePrice() {
        return salePrice;
    }

    public Money getSubtotalAmount() {
        return subtotalAmount;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof OrderItem)) {
            return false;
        }
        OrderItem orderItem = (OrderItem) other;
        return quantity == orderItem.quantity
            && Objects.equals(productId, orderItem.productId)
            && Objects.equals(productName, orderItem.productName)
            && Objects.equals(salePrice, orderItem.salePrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, productName, quantity, salePrice);
    }
}
