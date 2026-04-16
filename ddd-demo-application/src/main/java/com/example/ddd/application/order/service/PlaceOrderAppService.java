package com.example.ddd.application.order.service;

import com.example.ddd.application.order.assembler.OrderDtoAssembler;
import com.example.ddd.application.order.assembler.PlaceOrderCommandAssembler;
import com.example.ddd.application.order.command.PlaceOrderCommand;
import com.example.ddd.application.order.dto.OrderDetailDTO;
import com.example.ddd.domain.order.model.Order;
import com.example.ddd.domain.order.repository.OrderRepository;
import com.example.ddd.domain.order.service.OrderDomainService;
import com.example.ddd.application.order.validator.PlaceOrderCommandValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PlaceOrderAppService {

    private final PlaceOrderCommandValidator placeOrderCommandValidator;
    private final PlaceOrderPreCheckService placeOrderPreCheckService;
    private final PlaceOrderCommandAssembler placeOrderCommandAssembler;
    private final OrderDomainService orderDomainService;
    private final OrderRepository orderRepository;
    private final OrderApplicationPostProcessor orderApplicationPostProcessor;
    private final OrderDtoAssembler orderDtoAssembler;

    public PlaceOrderAppService(
        PlaceOrderCommandValidator placeOrderCommandValidator,
        PlaceOrderPreCheckService placeOrderPreCheckService,
        PlaceOrderCommandAssembler placeOrderCommandAssembler,
        OrderDomainService orderDomainService,
        OrderRepository orderRepository,
        OrderApplicationPostProcessor orderApplicationPostProcessor,
        OrderDtoAssembler orderDtoAssembler
    ) {
        this.placeOrderCommandValidator = placeOrderCommandValidator;
        this.placeOrderPreCheckService = placeOrderPreCheckService;
        this.placeOrderCommandAssembler = placeOrderCommandAssembler;
        this.orderDomainService = orderDomainService;
        this.orderRepository = orderRepository;
        this.orderApplicationPostProcessor = orderApplicationPostProcessor;
        this.orderDtoAssembler = orderDtoAssembler;
    }

    @Transactional
    public OrderDetailDTO placeOrder(PlaceOrderCommand command) {
        placeOrderCommandValidator.validate(command);
        placeOrderPreCheckService.check(command);
        Order order = orderDomainService.placeOrder(
            placeOrderCommandAssembler.toUserId(command),
            placeOrderCommandAssembler.toOrderItems(command)
        );
        orderRepository.save(order);
        orderApplicationPostProcessor.afterOrderPlaced(order);
        return orderDtoAssembler.toDetail(order);
    }
}
