package com.example.ddd.application.membership.command;

public class RegisterMembershipCommand {

    private final Long userId;
    private final String userName;

    public RegisterMembershipCommand(Long userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }
}
