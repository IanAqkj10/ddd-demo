package com.example.ddd.domain.iam.service;

import com.example.ddd.domain.iam.model.PermissionCode;
import com.example.ddd.domain.iam.model.Role;
import com.example.ddd.domain.iam.model.User;
import java.util.Collection;

public class IamAuthorizationService {

    public boolean isPermitted(User user, Collection<Role> roles, PermissionCode required) {
        if (user == null || !user.isActive()) {
            return false;
        }
        if (required == null || roles == null || roles.isEmpty()) {
            return false;
        }
        for (Role role : roles) {
            if (role.implies(required)) {
                return true;
            }
        }
        return false;
    }
}
