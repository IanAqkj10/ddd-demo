package com.example.ddd.infrastructure.iam.assembler;

import com.example.ddd.domain.iam.model.PermissionCode;
import com.example.ddd.domain.iam.model.Role;
import com.example.ddd.domain.iam.model.RoleCode;
import com.example.ddd.domain.iam.model.RoleId;
import com.example.ddd.infrastructure.iam.dataobject.RoleDO;
import java.util.LinkedHashSet;
import java.util.Set;

public class RoleDataAssembler {

    public static RoleDO toDataObject(Role role) {
        if (role == null) {
            return null;
        }
        RoleDO dataObject = new RoleDO();
        dataObject.setRoleId(role.getId().getValue());
        dataObject.setCode(role.getCode().getValue());
        dataObject.setName(role.getName());
        dataObject.setPermissions(joinPermissions(role.getPermissions()));
        dataObject.setCreatedAt(role.getCreatedAt());
        dataObject.setLastUpdatedAt(role.getLastUpdatedAt());
        return dataObject;
    }

    public static Role toDomainModel(RoleDO dataObject) {
        if (dataObject == null) {
            return null;
        }
        return Role.restore(
            RoleId.of(dataObject.getRoleId()),
            RoleCode.of(dataObject.getCode()),
            dataObject.getName(),
            parsePermissions(dataObject.getPermissions()),
            dataObject.getCreatedAt(),
            dataObject.getLastUpdatedAt()
        );
    }

    private static String joinPermissions(Set<PermissionCode> permissions) {
        if (permissions == null || permissions.isEmpty()) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (PermissionCode permission : permissions) {
            if (builder.length() > 0) {
                builder.append(',');
            }
            builder.append(permission.getValue());
        }
        return builder.toString();
    }

    private static Set<PermissionCode> parsePermissions(String raw) {
        Set<PermissionCode> result = new LinkedHashSet<>();
        if (raw == null || raw.trim().isEmpty()) {
            return result;
        }
        for (String part : raw.split(",")) {
            String trimmed = part.trim();
            if (trimmed.isEmpty()) {
                continue;
            }
            result.add(PermissionCode.of(trimmed));
        }
        return result;
    }
}
