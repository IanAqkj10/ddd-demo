package com.example.ddd.domain.order.model;

import com.example.ddd.domain.shared.DomainException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Order {

    private final OrderId id;
    private final UserId userId;
    private final List<OrderItem> items;
    private OrderStatus status;
    private Money totalAmount;
    private String cancelReason;
    private final LocalDateTime createdAt;
    private LocalDateTime canceledAt;

    private Order(
        OrderId id,
        UserId userId,
        List<OrderItem> items,
        OrderStatus status,
        String cancelReason,
        LocalDateTime createdAt,
        LocalDateTime canceledAt
    ) {
        if (id == null) {
            throw new DomainException("订单ID不能为空");
        }
        if (userId == null) {
            throw new DomainException("用户ID不能为空");
        }
        if (items == null || items.isEmpty()) {
            throw new DomainException("订单项不能为空");
        }
        this.id = id;
        this.userId = userId;
        this.items = Collections.unmodifiableList(new ArrayList<OrderItem>(items));
        this.status = status;
        this.cancelReason = cancelReason;
        this.createdAt = createdAt == null ? LocalDateTime.now() : createdAt;
        this.canceledAt = canceledAt;
        this.totalAmount = calculateTotalAmount(items);
    }

    public static Order create(OrderId id, UserId userId, List<OrderItem> items) {
        return new Order(id, userId, items, OrderStatus.CREATED, null, LocalDateTime.now(), null);
    }

    public static Order restore(
        OrderId id,
        UserId userId,
        List<OrderItem> items,
        OrderStatus status,
        String cancelReason,
        LocalDateTime createdAt,
        LocalDateTime canceledAt
    ) {
        return new Order(id, userId, items, status, cancelReason, createdAt, canceledAt);
    }

    private Money calculateTotalAmount(List<OrderItem> items) {
        Money result = Money.zero();
        for (OrderItem item : items) {
            result = result.add(item.getSubtotalAmount());
        }
        return result;
    }

    public void cancel(String reason) {
        if (reason == null || reason.trim().isEmpty()) {
            throw new DomainException("取消原因不能为空");
        }
        if (status != OrderStatus.CREATED) {
            throw new DomainException("只有已创建订单可以取消");
        }
        this.status = OrderStatus.CANCELED;
        this.cancelReason = reason;
        this.canceledAt = LocalDateTime.now();
    }

    public OrderId getId() {
        return id;
    }

    public UserId getUserId() {
        return userId;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public Money getTotalAmount() {
        return totalAmount;
    }

    public String getCancelReason() {
        return cancelReason;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getCanceledAt() {
        return canceledAt;
    }
}
