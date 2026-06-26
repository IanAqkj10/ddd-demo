package com.example.ddd.application.iam.command;

public class GrantPermissionCommand {

    private final Long roleId;
    private final String permissionCode;

    public GrantPermissionCommand(Long roleId, String permissionCode) {
        this.roleId = roleId;
        this.permissionCode = permissionCode;
    }

    public Long getRoleId() {
        return roleId;
    }

    public String getPermissionCode() {
        return permissionCode;
    }
}
