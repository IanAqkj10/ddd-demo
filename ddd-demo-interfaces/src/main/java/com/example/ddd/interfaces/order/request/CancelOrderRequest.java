package com.example.ddd.interfaces.order.request;

import javax.validation.constraints.NotBlank;

public class CancelOrderRequest {

    @NotBlank(message = "取消原因不能为空")
    private String reason;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
