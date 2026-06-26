package com.example.ddd.infrastructure.iam.gateway;

import com.example.ddd.application.iam.port.RolePermissionCache;
import com.example.ddd.domain.iam.model.PermissionCode;
import com.example.ddd.domain.iam.model.Role;
import com.example.ddd.domain.iam.model.RoleCode;
import com.example.ddd.domain.iam.repository.RoleRepository;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class LocalRolePermissionCache implements RolePermissionCache {

    private final RoleRepository roleRepository;
    private final Map<String, Set<String>> cache = new ConcurrentHashMap<>();

    public LocalRolePermissionCache(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Set<String> getPermissions(String roleCode) {
        return cache.computeIfAbsent(roleCode, this::loadFromDb);
    }

    @Override
    public void refresh() {
        cache.clear();
    }

    @Override
    public void evict(String roleCode) {
        cache.remove(roleCode);
    }

    private Set<String> loadFromDb(String code) {
        Role role = roleRepository.findByCode(RoleCode.of(code));
        if (role == null) {
            return new HashSet<>();
        }
        Set<String> permissions = new HashSet<>();
        for (PermissionCode pc : role.getPermissions()) {
            permissions.add(pc.getValue());
        }
        return permissions;
    }
}
