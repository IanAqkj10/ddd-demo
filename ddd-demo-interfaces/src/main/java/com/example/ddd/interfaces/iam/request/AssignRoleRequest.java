package com.example.ddd.interfaces.iam.request;

import javax.validation.constraints.NotNull;

public class AssignRoleRequest {

    @NotNull(message = "角色ID不能为空")
    private Long roleId;

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
}
