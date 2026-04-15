package com.example.ddd.application.order.service;

import com.example.ddd.application.order.assembler.OrderDtoAssembler;
import com.example.ddd.application.order.command.CancelOrderCommand;
import com.example.ddd.application.order.dto.OrderDetailDTO;
import com.example.ddd.domain.order.model.Order;
import com.example.ddd.domain.order.model.OrderId;
import com.example.ddd.domain.order.repository.OrderRepository;
import com.example.ddd.domain.shared.DomainException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CancelOrderAppService {

    private final OrderRepository orderRepository;
    private final OrderDtoAssembler orderDtoAssembler;

    public CancelOrderAppService(OrderRepository orderRepository, OrderDtoAssembler orderDtoAssembler) {
        this.orderRepository = orderRepository;
        this.orderDtoAssembler = orderDtoAssembler;
    }

    @Transactional
    public OrderDetailDTO cancelOrder(CancelOrderCommand command) {
        if (command == null) {
            throw new DomainException("取消命令不能为空");
        }
        Order order = orderRepository.findById(OrderId.of(command.getOrderId()))
            .orElseThrow(() -> new DomainException("订单不存在"));
        order.cancel(command.getReason());
        orderRepository.save(order);
        return orderDtoAssembler.toDetail(order);
    }
}
