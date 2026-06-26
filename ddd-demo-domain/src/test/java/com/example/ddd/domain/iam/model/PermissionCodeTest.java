package com.example.ddd.domain.iam.model;

import com.example.ddd.domain.shared.DomainException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PermissionCodeTest {

    @Test
    public void exactMatchImplies() {
        PermissionCode owned = PermissionCode.of("order:create");
        PermissionCode required = PermissionCode.of("order:create");
        assertTrue(owned.implies(required));
    }

    @Test
    public void wildcardActionImpliesConcreteAction() {
        PermissionCode owned = PermissionCode.of("order:*");
        PermissionCode required = PermissionCode.of("order:create");
        assertTrue(owned.implies(required));
    }

    @Test
    public void differentResourceDoesNotImply() {
        PermissionCode owned = PermissionCode.of("order:*");
        PermissionCode required = PermissionCode.of("membership:create");
        assertFalse(owned.implies(required));
    }

    @Test
    public void invalidFormatThrows() {
        assertThrows(DomainException.class, () -> PermissionCode.of("Order:Create"));
        assertThrows(DomainException.class, () -> PermissionCode.of("nocolon"));
    }
}
