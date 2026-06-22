package com.example.ddd.interfaces.membership.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class RegisterMembershipRequest {

    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @NotBlank(message = "用户名不能为空")
    private String userName;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
