package com.example.ddd.application.iam.port;

import java.util.Set;

public interface RolePermissionCache {

    Set<String> getPermissions(String roleCode);

    void refresh();

    void evict(String roleCode);
}
