package com.example.ddd.application.order.service;

import com.example.ddd.application.order.assembler.OrderDtoAssembler;
import com.example.ddd.application.order.dto.OrderDetailDTO;
import com.example.ddd.domain.order.model.OrderId;
import com.example.ddd.domain.order.repository.OrderRepository;
import com.example.ddd.domain.shared.DomainException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GetOrderDetailAppService {

    private final OrderRepository orderRepository;
    private final OrderDtoAssembler orderDtoAssembler;

    public GetOrderDetailAppService(OrderRepository orderRepository, OrderDtoAssembler orderDtoAssembler) {
        this.orderRepository = orderRepository;
        this.orderDtoAssembler = orderDtoAssembler;
    }

    @Transactional(readOnly = true)
    public OrderDetailDTO getById(String orderId) {
        return orderRepository.findById(OrderId.of(orderId))
            .map(orderDtoAssembler::toDetail)
            .orElseThrow(() -> new DomainException("订单不存在"));
    }
}
