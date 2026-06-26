package com.example.ddd.domain.iam.model;

public enum UserStatus {

    ACTIVE("启用"),
    DISABLED("禁用");

    private final String description;

    UserStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
