package com.example.ddd.application.iam.service;

import com.example.ddd.application.iam.assembler.IamDtoAssembler;
import com.example.ddd.application.iam.command.AssignRoleCommand;
import com.example.ddd.application.iam.command.BatchAssignRoleCommand;
import com.example.ddd.application.iam.command.CreateUserCommand;
import com.example.ddd.application.iam.dto.UserDetailDTO;
import com.example.ddd.application.iam.port.IdGenerator;
import com.example.ddd.domain.iam.gateway.PasswordEncoder;
import com.example.ddd.domain.iam.model.HashedPassword;
import com.example.ddd.domain.iam.model.IamUserId;
import com.example.ddd.domain.iam.model.Role;
import com.example.ddd.domain.iam.model.RoleId;
import com.example.ddd.domain.iam.model.User;
import com.example.ddd.domain.iam.model.Username;
import com.example.ddd.domain.iam.repository.RoleRepository;
import com.example.ddd.domain.iam.repository.UserRepository;
import com.example.ddd.domain.shared.DomainException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserAppService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final IdGenerator idGenerator;

    public UserAppService(
        UserRepository userRepository,
        RoleRepository roleRepository,
        PasswordEncoder passwordEncoder,
        IdGenerator idGenerator
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.idGenerator = idGenerator;
    }

    @Transactional
    public UserDetailDTO createUser(CreateUserCommand command) {
        Username username = Username.of(command.getUsername());
        if (userRepository.existsByUsername(username)) {
            throw new DomainException("用户名已存在");
        }
        if (command.getPassword() == null || command.getPassword().length() < 6) {
            throw new DomainException("密码长度至少 6 位");
        }
        HashedPassword hashedPassword = passwordEncoder.encode(command.getPassword());
        IamUserId userId = IamUserId.of(idGenerator.nextId());
        User user = User.create(userId, username, hashedPassword);
        userRepository.save(user);
        return IamDtoAssembler.toUserDetail(user, new ArrayList<>());
    }

    @Transactional
    public UserDetailDTO assignRole(AssignRoleCommand command) {
        User user = userRepository.findById(IamUserId.of(command.getUserId()));
        if (user == null) {
            throw new DomainException("用户不存在");
        }
        Role role = roleRepository.findById(RoleId.of(command.getRoleId()));
        if (role == null) {
            throw new DomainException("角色不存在");
        }
        user.assignRole(role.getId());
        userRepository.save(user);

        List<Role> roles = roleRepository.findAllByIds(user.getRoleIds());
        return IamDtoAssembler.toUserDetail(user, roles);
    }

    @Transactional
    public UserDetailDTO revokeRole(AssignRoleCommand command) {
        User user = userRepository.findById(IamUserId.of(command.getUserId()));
        if (user == null) {
            throw new DomainException("用户不存在");
        }
        user.revokeRole(RoleId.of(command.getRoleId()));
        userRepository.save(user);

        List<Role> roles = roleRepository.findAllByIds(user.getRoleIds());
        return IamDtoAssembler.toUserDetail(user, roles);
    }

    @Transactional
    public UserDetailDTO batchAssignRoles(BatchAssignRoleCommand command) {
        if (command.getRoleIds() == null || command.getRoleIds().isEmpty()) {
            throw new DomainException("角色ID列表不能为空");
        }
        User user = userRepository.findById(IamUserId.of(command.getUserId()));
        if (user == null) {
            throw new DomainException("用户不存在");
        }
        Set<RoleId> roleIds = new LinkedHashSet<>();
        for (Long raw : command.getRoleIds()) {
            RoleId roleId = RoleId.of(raw);
            if (roleRepository.findById(roleId) == null) {
                throw new DomainException("角色不存在：" + raw);
            }
            roleIds.add(roleId);
        }
        user.assignRoles(roleIds);
        userRepository.save(user);

        List<Role> roles = roleRepository.findAllByIds(user.getRoleIds());
        return IamDtoAssembler.toUserDetail(user, roles);
    }

    @Transactional
    public UserDetailDTO batchRevokeRoles(BatchAssignRoleCommand command) {
        if (command.getRoleIds() == null || command.getRoleIds().isEmpty()) {
            throw new DomainException("角色ID列表不能为空");
        }
        User user = userRepository.findById(IamUserId.of(command.getUserId()));
        if (user == null) {
            throw new DomainException("用户不存在");
        }
        Set<RoleId> roleIds = new LinkedHashSet<>();
        for (Long raw : command.getRoleIds()) {
            roleIds.add(RoleId.of(raw));
        }
        user.revokeRoles(roleIds);
        userRepository.save(user);

        List<Role> roles = roleRepository.findAllByIds(user.getRoleIds());
        return IamDtoAssembler.toUserDetail(user, roles);
    }

    public UserDetailDTO getUserDetail(Long userId) {
        User user = userRepository.findById(IamUserId.of(userId));
        if (user == null) {
            throw new DomainException("用户不存在");
        }
        List<Role> roles = roleRepository.findAllByIds(user.getRoleIds());
        return IamDtoAssembler.toUserDetail(user, roles);
    }
}
