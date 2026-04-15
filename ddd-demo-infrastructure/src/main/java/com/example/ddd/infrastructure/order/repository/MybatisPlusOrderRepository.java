package com.example.ddd.infrastructure.order.repository;

import com.example.ddd.domain.order.model.Order;
import com.example.ddd.domain.order.model.OrderId;
import com.example.ddd.domain.order.repository.OrderRepository;
import com.example.ddd.infrastructure.order.assembler.OrderDataAssembler;
import com.example.ddd.infrastructure.order.dataobject.OrderDO;
import com.example.ddd.infrastructure.order.dataobject.OrderItemDO;
import com.example.ddd.infrastructure.order.mapper.OrderItemMapper;
import com.example.ddd.infrastructure.order.mapper.OrderMapper;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class MybatisPlusOrderRepository implements OrderRepository {

    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final OrderDataAssembler orderDataAssembler;

    public MybatisPlusOrderRepository(
        OrderMapper orderMapper,
        OrderItemMapper orderItemMapper,
        OrderDataAssembler orderDataAssembler
    ) {
        this.orderMapper = orderMapper;
        this.orderItemMapper = orderItemMapper;
        this.orderDataAssembler = orderDataAssembler;
    }

    @Override
    public void save(Order order) {
        OrderDO orderDO = orderDataAssembler.toOrderDO(order);
        if (orderMapper.selectById(order.getId().getValue()) == null) {
            orderMapper.insert(orderDO);
        } else {
            orderMapper.updateById(orderDO);
        }
        orderItemMapper.deleteByOrderId(order.getId().getValue());
        List<OrderItemDO> itemDOs = orderDataAssembler.toOrderItemDOs(order);
        for (OrderItemDO itemDO : itemDOs) {
            orderItemMapper.insert(itemDO);
        }
    }

    @Override
    public Optional<Order> findById(OrderId orderId) {
        OrderDO orderDO = orderMapper.selectById(orderId.getValue());
        if (orderDO == null) {
            return Optional.empty();
        }
        return Optional.of(orderDataAssembler.toOrder(orderDO, orderItemMapper.selectByOrderId(orderId.getValue())));
    }
}
