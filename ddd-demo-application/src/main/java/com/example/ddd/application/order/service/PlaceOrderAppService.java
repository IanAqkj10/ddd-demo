package com.example.ddd.application.order.service;

import com.example.ddd.application.order.assembler.OrderDtoAssembler;
import com.example.ddd.application.order.command.PlaceOrderCommand;
import com.example.ddd.application.order.command.PlaceOrderItemCommand;
import com.example.ddd.application.order.dto.OrderDetailDTO;
import com.example.ddd.domain.order.model.Money;
import com.example.ddd.domain.order.model.Order;
import com.example.ddd.domain.order.model.OrderItem;
import com.example.ddd.domain.order.model.ProductId;
import com.example.ddd.domain.order.model.UserId;
import com.example.ddd.domain.order.repository.OrderRepository;
import com.example.ddd.domain.order.service.OrderDomainService;
import com.example.ddd.domain.shared.DomainException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PlaceOrderAppService {

    private final OrderDomainService orderDomainService;
    private final OrderRepository orderRepository;
    private final OrderDtoAssembler orderDtoAssembler;

    public PlaceOrderAppService(
        OrderDomainService orderDomainService,
        OrderRepository orderRepository,
        OrderDtoAssembler orderDtoAssembler
    ) {
        this.orderDomainService = orderDomainService;
        this.orderRepository = orderRepository;
        this.orderDtoAssembler = orderDtoAssembler;
    }

    @Transactional
    public OrderDetailDTO placeOrder(PlaceOrderCommand command) {
        if (command == null) {
            throw new DomainException("下单命令不能为空");
        }
        Order order = orderDomainService.placeOrder(UserId.of(command.getUserId()), toOrderItems(command));
        orderRepository.save(order);
        return orderDtoAssembler.toDetail(order);
    }

    private List<OrderItem> toOrderItems(PlaceOrderCommand command) {
        if (command.getItems() == null || command.getItems().isEmpty()) {
            throw new DomainException("订单项不能为空");
        }
        List<OrderItem> items = new ArrayList<OrderItem>();
        for (PlaceOrderItemCommand item : command.getItems()) {
            items.add(
                OrderItem.of(
                    ProductId.of(item.getProductId()),
                    item.getProductName(),
                    item.getQuantity(),
                    Money.of(item.getSalePrice())
                )
            );
        }
        return items;
    }
}
