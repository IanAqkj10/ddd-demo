package com.example.ddd.application.iam.command;

import java.util.List;

public class BatchGrantPermissionCommand {

    private final Long roleId;
    private final List<String> permissionCodes;

    public BatchGrantPermissionCommand(Long roleId, List<String> permissionCodes) {
        this.roleId = roleId;
        this.permissionCodes = permissionCodes;
    }

    public Long getRoleId() {
        return roleId;
    }

    public List<String> getPermissionCodes() {
        return permissionCodes;
    }
}
