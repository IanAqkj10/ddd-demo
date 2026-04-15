package com.example.ddd.application.order.assembler;

import com.example.ddd.application.order.dto.OrderDetailDTO;
import com.example.ddd.application.order.dto.OrderItemDTO;
import com.example.ddd.domain.order.model.Order;
import com.example.ddd.domain.order.model.OrderItem;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class OrderDtoAssembler {

    public OrderDetailDTO toDetail(Order order) {
        OrderDetailDTO dto = new OrderDetailDTO();
        dto.setOrderId(order.getId().getValue());
        dto.setUserId(order.getUserId().getValue());
        dto.setStatus(order.getStatus().name());
        dto.setTotalAmount(order.getTotalAmount().toBigDecimal());
        dto.setCancelReason(order.getCancelReason());
        dto.setCreatedAt(order.getCreatedAt());
        dto.setCanceledAt(order.getCanceledAt());
        dto.setItems(toItemDtos(order.getItems()));
        return dto;
    }

    private List<OrderItemDTO> toItemDtos(List<OrderItem> orderItems) {
        List<OrderItemDTO> dtos = new ArrayList<OrderItemDTO>();
        for (OrderItem orderItem : orderItems) {
            OrderItemDTO dto = new OrderItemDTO();
            dto.setProductId(String.valueOf(orderItem.getProductId().getValue()));
            dto.setProductName(orderItem.getProductName());
            dto.setQuantity(orderItem.getQuantity());
            dto.setSalePrice(orderItem.getSalePrice().toBigDecimal());
            dto.setSubtotalAmount(orderItem.getSubtotalAmount().toBigDecimal());
            dtos.add(dto);
        }
        return dtos;
    }
}
