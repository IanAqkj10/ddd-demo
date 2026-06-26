package com.example.ddd.application.iam.service;

import com.example.ddd.application.iam.command.LoginCommand;
import com.example.ddd.application.iam.dto.LoginResultDTO;
import com.example.ddd.application.iam.port.TokenStore;
import com.example.ddd.domain.iam.gateway.PasswordEncoder;
import com.example.ddd.domain.iam.model.Role;
import com.example.ddd.domain.iam.model.RoleId;
import com.example.ddd.domain.iam.model.User;
import com.example.ddd.domain.iam.model.Username;
import com.example.ddd.domain.iam.repository.RoleRepository;
import com.example.ddd.domain.iam.repository.UserRepository;
import com.example.ddd.domain.iam.service.IamLoginService;
import com.example.ddd.domain.shared.DomainException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;

@Service
public class LoginAppService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final IamLoginService iamLoginService;
    private final TokenStore tokenStore;

    public LoginAppService(
        UserRepository userRepository,
        RoleRepository roleRepository,
        PasswordEncoder passwordEncoder,
        IamLoginService iamLoginService,
        TokenStore tokenStore
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.iamLoginService = iamLoginService;
        this.tokenStore = tokenStore;
    }

    public LoginResultDTO login(LoginCommand command) {
        if (command == null || command.getUsername() == null || command.getPassword() == null) {
            throw new DomainException("用户名和密码不能为空");
        }
        User user = userRepository.findByUsername(Username.of(command.getUsername()));
        iamLoginService.authenticate(user, command.getPassword(), passwordEncoder);

        List<String> roleCodes = resolveRoleCodes(user.getRoleIds());
        String token = tokenStore.issue(user.getId().getValue(), user.getUsername().getValue(), roleCodes);

        LoginResultDTO dto = new LoginResultDTO();
        dto.setToken(token);
        dto.setUserId(user.getId().getValue());
        dto.setUsername(user.getUsername().getValue());
        return dto;
    }

    public void logout(String token) {
        if (token == null || token.trim().isEmpty()) {
            return;
        }
        tokenStore.revoke(token);
    }

    private List<String> resolveRoleCodes(Set<RoleId> roleIds) {
        if (roleIds == null || roleIds.isEmpty()) {
            return new ArrayList<>();
        }
        List<Role> roles = roleRepository.findAllByIds(roleIds);
        List<String> codes = new ArrayList<>(roles.size());
        for (Role role : roles) {
            codes.add(role.getCode().getValue());
        }
        return codes;
    }
}
