package com.example.ddd.application.iam.assembler;

import com.example.ddd.application.iam.dto.RoleDetailDTO;
import com.example.ddd.application.iam.dto.UserDetailDTO;
import com.example.ddd.domain.iam.model.PermissionCode;
import com.example.ddd.domain.iam.model.Role;
import com.example.ddd.domain.iam.model.RoleId;
import com.example.ddd.domain.iam.model.User;
import java.util.ArrayList;
import java.util.List;

public class IamDtoAssembler {

    public static UserDetailDTO toUserDetail(User user, List<Role> roles) {
        if (user == null) {
            return null;
        }
        UserDetailDTO dto = new UserDetailDTO();
        dto.setId(user.getId().getValue());
        dto.setUsername(user.getUsername().getValue());
        dto.setStatus(user.getStatus().name());
        dto.setRoles(toRoleCodes(roles));
        dto.setCreatedAt(user.getCreatedAt());
        dto.setLastUpdatedAt(user.getLastUpdatedAt());
        return dto;
    }

    public static RoleDetailDTO toRoleDetail(Role role) {
        if (role == null) {
            return null;
        }
        RoleDetailDTO dto = new RoleDetailDTO();
        dto.setId(role.getId().getValue());
        dto.setCode(role.getCode().getValue());
        dto.setName(role.getName());
        dto.setPermissions(toPermissionStrings(role));
        dto.setCreatedAt(role.getCreatedAt());
        dto.setLastUpdatedAt(role.getLastUpdatedAt());
        return dto;
    }

    public static List<String> toRoleCodes(List<Role> roles) {
        List<String> codes = new ArrayList<>();
        if (roles == null) {
            return codes;
        }
        for (Role role : roles) {
            codes.add(role.getCode().getValue());
        }
        return codes;
    }

    public static List<String> toPermissionStrings(List<Role> roles) {
        List<String> permissions = new ArrayList<>();
        if (roles == null) {
            return permissions;
        }
        for (Role role : roles) {
            for (PermissionCode permission : role.getPermissions()) {
                permissions.add(permission.getValue());
            }
        }
        return permissions;
    }

    private static List<String> toPermissionStrings(Role role) {
        List<String> permissions = new ArrayList<>();
        for (PermissionCode permission : role.getPermissions()) {
            permissions.add(permission.getValue());
        }
        return permissions;
    }

    public static List<RoleId> toRoleIds(List<Long> ids) {
        List<RoleId> result = new ArrayList<>();
        if (ids == null) {
            return result;
        }
        for (Long id : ids) {
            result.add(RoleId.of(id));
        }
        return result;
    }
}
