package com.example.ddd.application.order.assembler;

import com.example.ddd.application.order.command.PlaceOrderCommand;
import com.example.ddd.application.order.command.PlaceOrderItemCommand;
import com.example.ddd.domain.order.model.OrderItem;
import com.example.ddd.domain.order.model.UserId;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PlaceOrderCommandAssemblerTest {

    @Test
    void shouldConvertCommandToUserIdAndOrderItems() {
        PlaceOrderCommandAssembler assembler = new PlaceOrderCommandAssembler();
        PlaceOrderCommand command = new PlaceOrderCommand(
            1001L,
            Collections.singletonList(
                new PlaceOrderItemCommand(2001L, "DDD 实战课程", 2, new BigDecimal("199.00"))
            )
        );

        UserId userId = assembler.toUserId(command);
        List<OrderItem> orderItems = assembler.toOrderItems(command);

        Assertions.assertEquals(Long.valueOf(1001L), userId.getValue());
        Assertions.assertEquals(1, orderItems.size());
        Assertions.assertEquals(Long.valueOf(2001L), orderItems.get(0).getProductId().getValue());
        Assertions.assertEquals("DDD 实战课程", orderItems.get(0).getProductName());
        Assertions.assertEquals(2, orderItems.get(0).getQuantity());
        Assertions.assertEquals(new BigDecimal("199.00"), orderItems.get(0).getSalePrice().toBigDecimal());
        Assertions.assertEquals(new BigDecimal("398.00"), orderItems.get(0).getSubtotalAmount().toBigDecimal());
    }
}
