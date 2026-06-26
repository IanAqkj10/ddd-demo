package com.example.ddd.domain.iam.model;

import com.example.ddd.domain.shared.DomainException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public class User {

    private final IamUserId id;
    private final Username username;
    private HashedPassword password;
    private UserStatus status;
    private final Set<RoleId> roleIds;
    private final LocalDateTime createdAt;
    private LocalDateTime lastUpdatedAt;

    private User(
        IamUserId id,
        Username username,
        HashedPassword password,
        UserStatus status,
        Set<RoleId> roleIds,
        LocalDateTime createdAt,
        LocalDateTime lastUpdatedAt
    ) {
        if (id == null) {
            throw new DomainException("用户ID不能为空");
        }
        if (username == null) {
            throw new DomainException("用户名不能为空");
        }
        if (password == null) {
            throw new DomainException("密码不能为空");
        }
        if (status == null) {
            throw new DomainException("用户状态不能为空");
        }
        this.id = id;
        this.username = username;
        this.password = password;
        this.status = status;
        this.roleIds = roleIds == null ? new LinkedHashSet<>() : new LinkedHashSet<>(roleIds);
        this.createdAt = createdAt == null ? LocalDateTime.now() : createdAt;
        this.lastUpdatedAt = lastUpdatedAt == null ? LocalDateTime.now() : lastUpdatedAt;
    }

    public static User create(IamUserId id, Username username, HashedPassword password) {
        return new User(
            id,
            username,
            password,
            UserStatus.ACTIVE,
            new LinkedHashSet<>(),
            LocalDateTime.now(),
            LocalDateTime.now()
        );
    }

    public static User restore(
        IamUserId id,
        Username username,
        HashedPassword password,
        UserStatus status,
        Set<RoleId> roleIds,
        LocalDateTime createdAt,
        LocalDateTime lastUpdatedAt
    ) {
        return new User(id, username, password, status, roleIds, createdAt, lastUpdatedAt);
    }

    public void changePassword(HashedPassword newPassword) {
        if (newPassword == null) {
            throw new DomainException("新密码不能为空");
        }
        if (newPassword.equals(this.password)) {
            throw new DomainException("新密码不能与旧密码相同");
        }
        this.password = newPassword;
        this.lastUpdatedAt = LocalDateTime.now();
    }

    public void assignRole(RoleId roleId) {
        if (roleId == null) {
            throw new DomainException("角色ID不能为空");
        }
        if (this.roleIds.contains(roleId)) {
            throw new DomainException("用户已拥有该角色");
        }
        this.roleIds.add(roleId);
        this.lastUpdatedAt = LocalDateTime.now();
    }

    public void revokeRole(RoleId roleId) {
        if (roleId == null) {
            throw new DomainException("角色ID不能为空");
        }
        if (!this.roleIds.contains(roleId)) {
            throw new DomainException("用户未持有该角色");
        }
        this.roleIds.remove(roleId);
        this.lastUpdatedAt = LocalDateTime.now();
    }

    public int assignRoles(Collection<RoleId> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new DomainException("角色ID集合不能为空");
        }
        int added = 0;
        for (RoleId id : ids) {
            if (id == null) {
                throw new DomainException("角色ID不能为空");
            }
            if (this.roleIds.add(id)) {
                added++;
            }
        }
        if (added > 0) {
            this.lastUpdatedAt = LocalDateTime.now();
        }
        return added;
    }

    public int revokeRoles(Collection<RoleId> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new DomainException("角色ID集合不能为空");
        }
        int removed = 0;
        for (RoleId id : ids) {
            if (id == null) {
                throw new DomainException("角色ID不能为空");
            }
            if (this.roleIds.remove(id)) {
                removed++;
            }
        }
        if (removed > 0) {
            this.lastUpdatedAt = LocalDateTime.now();
        }
        return removed;
    }

    public void disable() {
        if (this.status == UserStatus.DISABLED) {
            throw new DomainException("用户已是禁用状态");
        }
        this.status = UserStatus.DISABLED;
        this.lastUpdatedAt = LocalDateTime.now();
    }

    public void enable() {
        if (this.status == UserStatus.ACTIVE) {
            throw new DomainException("用户已是启用状态");
        }
        this.status = UserStatus.ACTIVE;
        this.lastUpdatedAt = LocalDateTime.now();
    }

    public boolean isActive() {
        return this.status == UserStatus.ACTIVE;
    }

    public IamUserId getId() {
        return id;
    }

    public Username getUsername() {
        return username;
    }

    public HashedPassword getPassword() {
        return password;
    }

    public UserStatus getStatus() {
        return status;
    }

    public Set<RoleId> getRoleIds() {
        return Collections.unmodifiableSet(roleIds);
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getLastUpdatedAt() {
        return lastUpdatedAt;
    }
}
