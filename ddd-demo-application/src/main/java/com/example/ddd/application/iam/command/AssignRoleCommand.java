package com.example.ddd.application.iam.command;

public class AssignRoleCommand {

    private final Long userId;
    private final Long roleId;

    public AssignRoleCommand(Long userId, Long roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getRoleId() {
        return roleId;
    }
}
