package com.example.ddd.application.order.command;

import java.math.BigDecimal;

public class PlaceOrderItemCommand {

    private final Long productId;
    private final String productName;
    private final Integer quantity;
    private final BigDecimal salePrice;

    public PlaceOrderItemCommand(Long productId, String productName, Integer quantity, BigDecimal salePrice) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.salePrice = salePrice;
    }

    public Long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }
}
