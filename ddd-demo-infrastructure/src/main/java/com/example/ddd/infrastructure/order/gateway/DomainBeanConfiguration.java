package com.example.ddd.infrastructure.order.gateway;

import com.example.ddd.domain.order.gateway.InventoryGateway;
import com.example.ddd.domain.order.service.OrderDomainService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainBeanConfiguration {

    @Bean
    public OrderDomainService orderDomainService(InventoryGateway inventoryGateway) {
        return new OrderDomainService(inventoryGateway);
    }
}
