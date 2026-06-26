package com.example.ddd.domain.iam.model;

import com.example.ddd.domain.shared.DomainException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public class Role {

    private final RoleId id;
    private final RoleCode code;
    private String name;
    private final Set<PermissionCode> permissions;
    private final LocalDateTime createdAt;
    private LocalDateTime lastUpdatedAt;

    private Role(
        RoleId id,
        RoleCode code,
        String name,
        Set<PermissionCode> permissions,
        LocalDateTime createdAt,
        LocalDateTime lastUpdatedAt
    ) {
        if (id == null) {
            throw new DomainException("角色ID不能为空");
        }
        if (code == null) {
            throw new DomainException("角色编码不能为空");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new DomainException("角色名称不能为空");
        }
        this.id = id;
        this.code = code;
        this.name = name;
        this.permissions = permissions == null ? new LinkedHashSet<>() : new LinkedHashSet<>(permissions);
        this.createdAt = createdAt == null ? LocalDateTime.now() : createdAt;
        this.lastUpdatedAt = lastUpdatedAt == null ? LocalDateTime.now() : lastUpdatedAt;
    }

    public static Role create(RoleId id, RoleCode code, String name) {
        return new Role(id, code, name, new LinkedHashSet<>(), LocalDateTime.now(), LocalDateTime.now());
    }

    public static Role restore(
        RoleId id,
        RoleCode code,
        String name,
        Set<PermissionCode> permissions,
        LocalDateTime createdAt,
        LocalDateTime lastUpdatedAt
    ) {
        return new Role(id, code, name, permissions, createdAt, lastUpdatedAt);
    }

    public void rename(String newName) {
        if (newName == null || newName.trim().isEmpty()) {
            throw new DomainException("角色名称不能为空");
        }
        this.name = newName;
        this.lastUpdatedAt = LocalDateTime.now();
    }

    public void grantPermission(PermissionCode permission) {
        if (permission == null) {
            throw new DomainException("权限编码不能为空");
        }
        if (this.permissions.contains(permission)) {
            throw new DomainException("角色已拥有该权限");
        }
        this.permissions.add(permission);
        this.lastUpdatedAt = LocalDateTime.now();
    }

    public void revokePermission(PermissionCode permission) {
        if (permission == null) {
            throw new DomainException("权限编码不能为空");
        }
        if (!this.permissions.contains(permission)) {
            throw new DomainException("角色未持有该权限");
        }
        this.permissions.remove(permission);
        this.lastUpdatedAt = LocalDateTime.now();
    }

    public int grantPermissions(Collection<PermissionCode> codes) {
        if (codes == null || codes.isEmpty()) {
            throw new DomainException("权限编码集合不能为空");
        }
        int added = 0;
        for (PermissionCode code : codes) {
            if (code == null) {
                throw new DomainException("权限编码不能为空");
            }
            if (this.permissions.add(code)) {
                added++;
            }
        }
        if (added > 0) {
            this.lastUpdatedAt = LocalDateTime.now();
        }
        return added;
    }

    public int revokePermissions(Collection<PermissionCode> codes) {
        if (codes == null || codes.isEmpty()) {
            throw new DomainException("权限编码集合不能为空");
        }
        int removed = 0;
        for (PermissionCode code : codes) {
            if (code == null) {
                throw new DomainException("权限编码不能为空");
            }
            if (this.permissions.remove(code)) {
                removed++;
            }
        }
        if (removed > 0) {
            this.lastUpdatedAt = LocalDateTime.now();
        }
        return removed;
    }

    public boolean implies(PermissionCode required) {
        if (required == null) {
            return false;
        }
        for (PermissionCode owned : this.permissions) {
            if (owned.implies(required)) {
                return true;
            }
        }
        return false;
    }

    public RoleId getId() {
        return id;
    }

    public RoleCode getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public Set<PermissionCode> getPermissions() {
        return Collections.unmodifiableSet(permissions);
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getLastUpdatedAt() {
        return lastUpdatedAt;
    }
}
