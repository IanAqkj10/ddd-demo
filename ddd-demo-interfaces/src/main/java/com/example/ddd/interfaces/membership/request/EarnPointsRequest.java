package com.example.ddd.interfaces.membership.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

public class EarnPointsRequest {

    @Min(value = 1, message = "积分必须大于0")
    private int points;

    @NotBlank(message = "原因不能为空")
    private String reason;

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
