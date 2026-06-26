package com.example.ddd.application.iam.command;

import java.util.List;

public class BatchAssignRoleCommand {

    private final Long userId;
    private final List<Long> roleIds;

    public BatchAssignRoleCommand(Long userId, List<Long> roleIds) {
        this.userId = userId;
        this.roleIds = roleIds;
    }

    public Long getUserId() {
        return userId;
    }

    public List<Long> getRoleIds() {
        return roleIds;
    }
}
