package com.example.ddd.application.iam.port;

import java.util.List;

public class TokenPayload {

    private final Long userId;
    private final String username;
    private final List<String> roles;

    public TokenPayload(Long userId, String username, List<String> roles) {
        this.userId = userId;
        this.username = username;
        this.roles = roles;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public List<String> getRoles() {
        return roles;
    }
}
