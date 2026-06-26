package com.example.ddd.interfaces.iam.request;

import java.util.List;
import javax.validation.constraints.NotEmpty;

public class BatchAssignRoleRequest {

    @NotEmpty(message = "角色ID列表不能为空")
    private List<Long> roleIds;

    public List<Long> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<Long> roleIds) {
        this.roleIds = roleIds;
    }
}
