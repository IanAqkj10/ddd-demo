package com.example.ddd.interfaces.iam.request;

import javax.validation.constraints.NotBlank;

public class GrantPermissionRequest {

    @NotBlank(message = "权限编码不能为空")
    private String permissionCode;

    public String getPermissionCode() {
        return permissionCode;
    }

    public void setPermissionCode(String permissionCode) {
        this.permissionCode = permissionCode;
    }
}
