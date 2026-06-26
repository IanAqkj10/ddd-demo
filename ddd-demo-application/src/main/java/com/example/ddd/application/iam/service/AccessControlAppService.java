package com.example.ddd.application.iam.service;

import com.example.ddd.application.iam.assembler.IamDtoAssembler;
import com.example.ddd.application.iam.dto.CurrentUserDTO;
import com.example.ddd.domain.iam.model.IamUserId;
import com.example.ddd.domain.iam.model.PermissionCode;
import com.example.ddd.domain.iam.model.Role;
import com.example.ddd.domain.iam.model.User;
import com.example.ddd.domain.iam.repository.RoleRepository;
import com.example.ddd.domain.iam.repository.UserRepository;
import com.example.ddd.domain.iam.service.IamAuthorizationService;
import com.example.ddd.domain.shared.DomainException;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class AccessControlAppService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final IamAuthorizationService authorizationService;

    public AccessControlAppService(
        UserRepository userRepository,
        RoleRepository roleRepository,
        IamAuthorizationService authorizationService
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.authorizationService = authorizationService;
    }

    public boolean isPermitted(IamUserId userId, String permissionCode) {
        if (userId == null || permissionCode == null) {
            return false;
        }
        User user = userRepository.findById(userId);
        if (user == null || !user.isActive()) {
            return false;
        }
        List<Role> roles = roleRepository.findAllByIds(user.getRoleIds());
        return authorizationService.isPermitted(user, roles, PermissionCode.of(permissionCode));
    }

    public CurrentUserDTO loadCurrentUser(IamUserId userId) {
        User user = userRepository.findById(userId);
        if (user == null) {
            throw new DomainException("用户不存在");
        }
        List<Role> roles = roleRepository.findAllByIds(user.getRoleIds());

        CurrentUserDTO dto = new CurrentUserDTO();
        dto.setUserId(user.getId().getValue());
        dto.setUsername(user.getUsername().getValue());
        dto.setStatus(user.getStatus().name());
        dto.setRoles(IamDtoAssembler.toRoleCodes(roles));
        dto.setPermissions(IamDtoAssembler.toPermissionStrings(roles));
        return dto;
    }
}
