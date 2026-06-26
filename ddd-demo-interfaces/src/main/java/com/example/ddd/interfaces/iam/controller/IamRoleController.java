package com.example.ddd.interfaces.iam.controller;

import com.example.ddd.application.iam.command.BatchGrantPermissionCommand;
import com.example.ddd.application.iam.command.CreateRoleCommand;
import com.example.ddd.application.iam.command.GrantPermissionCommand;
import com.example.ddd.application.iam.dto.RoleDetailDTO;
import com.example.ddd.application.iam.service.RoleAppService;
import com.example.ddd.interfaces.iam.request.BatchGrantPermissionRequest;
import com.example.ddd.interfaces.iam.request.CreateRoleRequest;
import com.example.ddd.interfaces.iam.request.GrantPermissionRequest;
import com.example.ddd.interfaces.iam.security.RequirePermission;
import com.example.ddd.interfaces.shared.response.ApiResponse;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/iam/roles")
public class IamRoleController {

    private final RoleAppService roleAppService;

    public IamRoleController(RoleAppService roleAppService) {
        this.roleAppService = roleAppService;
    }

    /** 创建角色：传入角色编码（如 ORDER_ADMIN）和名称，返回角色详情。 */
    @PostMapping
    @RequirePermission("iam:role:create")
    public ApiResponse<RoleDetailDTO> createRole(@Valid @RequestBody CreateRoleRequest request) {
        CreateRoleCommand command = new CreateRoleCommand(request.getCode(), request.getName());
        return ApiResponse.success(roleAppService.createRole(command));
    }

    /** 给角色授予权限：permissionCode 形如 order:create，支持通配 order:*。 */
    @PostMapping("/{roleId}/permissions")
    @RequirePermission("iam:role:grant")
    public ApiResponse<RoleDetailDTO> grantPermission(
        @PathVariable("roleId") Long roleId,
        @Valid @RequestBody GrantPermissionRequest request
    ) {
        GrantPermissionCommand command = new GrantPermissionCommand(roleId, request.getPermissionCode());
        return ApiResponse.success(roleAppService.grantPermission(command));
    }

    /** 撤销角色的某项权限：按精确编码撤销，不做通配匹配。 */
    @DeleteMapping("/{roleId}/permissions/{permissionCode}")
    @RequirePermission("iam:role:grant")
    public ApiResponse<RoleDetailDTO> revokePermission(
        @PathVariable("roleId") Long roleId,
        @PathVariable("permissionCode") String permissionCode
    ) {
        GrantPermissionCommand command = new GrantPermissionCommand(roleId, permissionCode);
        return ApiResponse.success(roleAppService.revokePermission(command));
    }

    /** 批量授予权限：一次性给角色加多条权限，已存在的自动跳过（幂等）。 */
    @PostMapping("/{roleId}/permissions/batch")
    @RequirePermission("iam:role:grant")
    public ApiResponse<RoleDetailDTO> batchGrantPermissions(
        @PathVariable("roleId") Long roleId,
        @Valid @RequestBody BatchGrantPermissionRequest request
    ) {
        BatchGrantPermissionCommand command = new BatchGrantPermissionCommand(roleId, request.getPermissionCodes());
        return ApiResponse.success(roleAppService.batchGrantPermissions(command));
    }

    /** 批量撤销权限：一次性从角色去除多条权限，不存在的自动跳过（幂等）。 */
    @PostMapping("/{roleId}/permissions/batch-revoke")
    @RequirePermission("iam:role:grant")
    public ApiResponse<RoleDetailDTO> batchRevokePermissions(
        @PathVariable("roleId") Long roleId,
        @Valid @RequestBody BatchGrantPermissionRequest request
    ) {
        BatchGrantPermissionCommand command = new BatchGrantPermissionCommand(roleId, request.getPermissionCodes());
        return ApiResponse.success(roleAppService.batchRevokePermissions(command));
    }

    /** 查询角色详情：返回角色基本信息及其包含的权限集合。 */
    @GetMapping("/{roleId}")
    @RequirePermission("iam:role:read")
    public ApiResponse<RoleDetailDTO> getRole(@PathVariable("roleId") Long roleId) {
        return ApiResponse.success(roleAppService.getRoleDetail(roleId));
    }
}
