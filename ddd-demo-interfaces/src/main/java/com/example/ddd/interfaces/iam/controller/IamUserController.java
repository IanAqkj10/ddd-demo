package com.example.ddd.interfaces.iam.controller;

import com.example.ddd.application.iam.command.AssignRoleCommand;
import com.example.ddd.application.iam.command.BatchAssignRoleCommand;
import com.example.ddd.application.iam.command.CreateUserCommand;
import com.example.ddd.application.iam.dto.UserDetailDTO;
import com.example.ddd.application.iam.service.UserAppService;
import com.example.ddd.interfaces.iam.request.AssignRoleRequest;
import com.example.ddd.interfaces.iam.request.BatchAssignRoleRequest;
import com.example.ddd.interfaces.iam.request.CreateUserRequest;
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
@RequestMapping("/api/iam/users")
public class IamUserController {

    private final UserAppService userAppService;

    public IamUserController(UserAppService userAppService) {
        this.userAppService = userAppService;
    }

    /** 创建用户：注册一个新账号，密码会经过哈希后入库，新用户初始无任何角色。 */
    @PostMapping
    @RequirePermission("iam:user:create")
    public ApiResponse<UserDetailDTO> createUser(@Valid @RequestBody CreateUserRequest request) {
        CreateUserCommand command = new CreateUserCommand(request.getUsername(), request.getPassword());
        return ApiResponse.success(userAppService.createUser(command));
    }

    /** 给用户分配角色：把指定角色绑定到用户上，用户从此获得该角色全部权限。 */
    @PostMapping("/{userId}/roles")
    @RequirePermission("iam:user:assign-role")
    public ApiResponse<UserDetailDTO> assignRole(
        @PathVariable("userId") Long userId,
        @Valid @RequestBody AssignRoleRequest request
    ) {
        AssignRoleCommand command = new AssignRoleCommand(userId, request.getRoleId());
        return ApiResponse.success(userAppService.assignRole(command));
    }

    /** 撤销用户的角色：把指定角色从用户身上解除，该角色对应的权限随之失效。 */
    @DeleteMapping("/{userId}/roles/{roleId}")
    @RequirePermission("iam:user:assign-role")
    public ApiResponse<UserDetailDTO> revokeRole(
        @PathVariable("userId") Long userId,
        @PathVariable("roleId") Long roleId
    ) {
        AssignRoleCommand command = new AssignRoleCommand(userId, roleId);
        return ApiResponse.success(userAppService.revokeRole(command));
    }

    /** 批量分配角色：一次性给用户绑定多个角色，已绑定的自动跳过（幂等）。 */
    @PostMapping("/{userId}/roles/batch")
    @RequirePermission("iam:user:assign-role")
    public ApiResponse<UserDetailDTO> batchAssignRoles(
        @PathVariable("userId") Long userId,
        @Valid @RequestBody BatchAssignRoleRequest request
    ) {
        BatchAssignRoleCommand command = new BatchAssignRoleCommand(userId, request.getRoleIds());
        return ApiResponse.success(userAppService.batchAssignRoles(command));
    }

    /** 批量撤销角色：一次性从用户解除多个角色，未持有的自动跳过（幂等）。 */
    @PostMapping("/{userId}/roles/batch-revoke")
    @RequirePermission("iam:user:assign-role")
    public ApiResponse<UserDetailDTO> batchRevokeRoles(
        @PathVariable("userId") Long userId,
        @Valid @RequestBody BatchAssignRoleRequest request
    ) {
        BatchAssignRoleCommand command = new BatchAssignRoleCommand(userId, request.getRoleIds());
        return ApiResponse.success(userAppService.batchRevokeRoles(command));
    }

    /** 查询用户详情：返回用户基本信息及其已绑定的角色列表。 */
    @GetMapping("/{userId}")
    @RequirePermission("iam:user:read")
    public ApiResponse<UserDetailDTO> getUser(@PathVariable("userId") Long userId) {
        return ApiResponse.success(userAppService.getUserDetail(userId));
    }
}
