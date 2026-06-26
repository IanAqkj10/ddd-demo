package com.example.ddd.domain.iam.model;

import com.example.ddd.domain.shared.DomainException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    private User newUser() {
        return User.create(
            IamUserId.of(1L),
            Username.of("alice"),
            HashedPassword.of("hash", "salt")
        );
    }

    @Test
    public void createUserStartsAsActiveWithoutRoles() {
        User user = newUser();
        assertTrue(user.isActive());
        assertEquals(0, user.getRoleIds().size());
    }

    @Test
    public void assignRoleAddsRole() {
        User user = newUser();
        user.assignRole(RoleId.of(10L));
        assertEquals(1, user.getRoleIds().size());
    }

    @Test
    public void assignSameRoleTwiceThrows() {
        User user = newUser();
        user.assignRole(RoleId.of(10L));
        assertThrows(DomainException.class, () -> user.assignRole(RoleId.of(10L)));
    }

    @Test
    public void disableThenEnable() {
        User user = newUser();
        user.disable();
        assertFalse(user.isActive());
        user.enable();
        assertTrue(user.isActive());
    }

    @Test
    public void changePasswordToSameValueThrows() {
        User user = newUser();
        assertThrows(DomainException.class, () -> user.changePassword(HashedPassword.of("hash", "salt")));
    }

    @Test
    public void assignRolesBatchSkipsExisting() {
        User user = newUser();
        user.assignRole(RoleId.of(10L));

        int added = user.assignRoles(java.util.Arrays.asList(
            RoleId.of(10L),
            RoleId.of(20L),
            RoleId.of(30L)
        ));

        assertEquals(2, added);
        assertEquals(3, user.getRoleIds().size());
    }

    @Test
    public void revokeRolesBatchSkipsMissing() {
        User user = newUser();
        user.assignRoles(java.util.Arrays.asList(RoleId.of(10L), RoleId.of(20L)));

        int removed = user.revokeRoles(java.util.Arrays.asList(RoleId.of(10L), RoleId.of(99L)));

        assertEquals(1, removed);
        assertEquals(1, user.getRoleIds().size());
    }

    @Test
    public void assignRolesBatchEmptyThrows() {
        User user = newUser();
        assertThrows(DomainException.class, () -> user.assignRoles(java.util.Collections.emptyList()));
    }
}
