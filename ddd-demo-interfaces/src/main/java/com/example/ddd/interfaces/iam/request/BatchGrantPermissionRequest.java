package com.example.ddd.interfaces.iam.request;

import java.util.List;
import javax.validation.constraints.NotEmpty;

public class BatchGrantPermissionRequest {

    @NotEmpty(message = "权限编码列表不能为空")
    private List<String> permissionCodes;

    public List<String> getPermissionCodes() {
        return permissionCodes;
    }

    public void setPermissionCodes(List<String> permissionCodes) {
        this.permissionCodes = permissionCodes;
    }
}
