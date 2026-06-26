package com.example.ddd.domain.iam.model;

import com.example.ddd.domain.shared.DomainException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RoleTest {

    @Test
    public void grantPermissionAndImplies() {
        Role role = Role.create(RoleId.of(1L), RoleCode.of("ADMIN"), "管理员");
        role.grantPermission(PermissionCode.of("order:*"));

        assertTrue(role.implies(PermissionCode.of("order:create")));
        assertFalse(role.implies(PermissionCode.of("membership:create")));
    }

    @Test
    public void grantDuplicatePermissionThrows() {
        Role role = Role.create(RoleId.of(1L), RoleCode.of("ADMIN"), "管理员");
        role.grantPermission(PermissionCode.of("order:create"));
        assertThrows(DomainException.class, () -> role.grantPermission(PermissionCode.of("order:create")));
    }

    @Test
    public void revokePermissionRemovesIt() {
        Role role = Role.create(RoleId.of(1L), RoleCode.of("ADMIN"), "管理员");
        role.grantPermission(PermissionCode.of("order:create"));
        role.revokePermission(PermissionCode.of("order:create"));
        assertFalse(role.implies(PermissionCode.of("order:create")));
    }

    @Test
    public void grantPermissionsBatchSkipsExisting() {
        Role role = Role.create(RoleId.of(1L), RoleCode.of("ADMIN"), "管理员");
        role.grantPermission(PermissionCode.of("order:create"));

        int added = role.grantPermissions(java.util.Arrays.asList(
            PermissionCode.of("order:create"),
            PermissionCode.of("order:read"),
            PermissionCode.of("membership:*")
        ));

        assertEquals(2, added);
        assertEquals(3, role.getPermissions().size());
    }

    @Test
    public void revokePermissionsBatchSkipsMissing() {
        Role role = Role.create(RoleId.of(1L), RoleCode.of("ADMIN"), "管理员");
        role.grantPermissions(java.util.Arrays.asList(
            PermissionCode.of("order:create"),
            PermissionCode.of("order:read")
        ));

        int removed = role.revokePermissions(java.util.Arrays.asList(
            PermissionCode.of("order:create"),
            PermissionCode.of("not:exist")
        ));

        assertEquals(1, removed);
        assertEquals(1, role.getPermissions().size());
    }

    @Test
    public void grantPermissionsBatchEmptyThrows() {
        Role role = Role.create(RoleId.of(1L), RoleCode.of("ADMIN"), "管理员");
        assertThrows(DomainException.class, () -> role.grantPermissions(java.util.Collections.emptyList()));
    }
}
