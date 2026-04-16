package com.example.ddd.application.order.service;

import com.example.ddd.application.order.assembler.OrderDtoAssembler;
import com.example.ddd.application.order.command.CancelOrderCommand;
import com.example.ddd.application.order.dto.OrderDetailDTO;
import com.example.ddd.application.order.validator.CancelOrderCommandValidator;
import com.example.ddd.domain.order.model.Order;
import com.example.ddd.domain.order.model.OrderId;
import com.example.ddd.domain.order.repository.OrderRepository;
import com.example.ddd.domain.shared.DomainException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CancelOrderAppService {

    private final CancelOrderCommandValidator cancelOrderCommandValidator;
    private final OrderRepository orderRepository;
    private final OrderApplicationPostProcessor orderApplicationPostProcessor;
    private final OrderDtoAssembler orderDtoAssembler;

    public CancelOrderAppService(
        CancelOrderCommandValidator cancelOrderCommandValidator,
        OrderRepository orderRepository,
        OrderApplicationPostProcessor orderApplicationPostProcessor,
        OrderDtoAssembler orderDtoAssembler
    ) {
        this.cancelOrderCommandValidator = cancelOrderCommandValidator;
        this.orderRepository = orderRepository;
        this.orderApplicationPostProcessor = orderApplicationPostProcessor;
        this.orderDtoAssembler = orderDtoAssembler;
    }

    @Transactional
    public OrderDetailDTO cancelOrder(CancelOrderCommand command) {
        cancelOrderCommandValidator.validate(command);
        Order order = orderRepository.findById(OrderId.of(command.getOrderId()))
            .orElseThrow(() -> new DomainException("订单不存在"));
        order.cancel(command.getReason());
        orderRepository.save(order);
        orderApplicationPostProcessor.afterOrderCanceled(order);
        return orderDtoAssembler.toDetail(order);
    }
}
