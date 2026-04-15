package com.example.ddd.interfaces.order.controller;

import com.example.ddd.application.order.command.CancelOrderCommand;
import com.example.ddd.application.order.command.PlaceOrderCommand;
import com.example.ddd.application.order.command.PlaceOrderItemCommand;
import com.example.ddd.application.order.dto.OrderDetailDTO;
import com.example.ddd.application.order.service.CancelOrderAppService;
import com.example.ddd.application.order.service.GetOrderDetailAppService;
import com.example.ddd.application.order.service.PlaceOrderAppService;
import com.example.ddd.interfaces.order.request.CancelOrderRequest;
import com.example.ddd.interfaces.order.request.OrderItemRequest;
import com.example.ddd.interfaces.order.request.PlaceOrderRequest;
import com.example.ddd.interfaces.shared.response.ApiResponse;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final PlaceOrderAppService placeOrderAppService;
    private final CancelOrderAppService cancelOrderAppService;
    private final GetOrderDetailAppService getOrderDetailAppService;

    public OrderController(
        PlaceOrderAppService placeOrderAppService,
        CancelOrderAppService cancelOrderAppService,
        GetOrderDetailAppService getOrderDetailAppService
    ) {
        this.placeOrderAppService = placeOrderAppService;
        this.cancelOrderAppService = cancelOrderAppService;
        this.getOrderDetailAppService = getOrderDetailAppService;
    }

    @PostMapping
    public ApiResponse<OrderDetailDTO> placeOrder(@Valid @RequestBody PlaceOrderRequest request) {
        return ApiResponse.success(
            placeOrderAppService.placeOrder(new PlaceOrderCommand(request.getUserId(), toItemCommands(request.getItems())))
        );
    }

    @PostMapping("/{orderId}/cancel")
    public ApiResponse<OrderDetailDTO> cancelOrder(
        @PathVariable("orderId") String orderId,
        @Valid @RequestBody CancelOrderRequest request
    ) {
        return ApiResponse.success(cancelOrderAppService.cancelOrder(new CancelOrderCommand(orderId, request.getReason())));
    }

    @GetMapping("/{orderId}")
    public ApiResponse<OrderDetailDTO> getOrder(@PathVariable("orderId") String orderId) {
        return ApiResponse.success(getOrderDetailAppService.getById(orderId));
    }

    private List<PlaceOrderItemCommand> toItemCommands(List<OrderItemRequest> items) {
        List<PlaceOrderItemCommand> commands = new ArrayList<PlaceOrderItemCommand>();
        for (OrderItemRequest item : items) {
            commands.add(
                new PlaceOrderItemCommand(
                    item.getProductId(),
                    item.getProductName(),
                    item.getQuantity(),
                    item.getSalePrice()
                )
            );
        }
        return commands;
    }
}
