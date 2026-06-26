package com.example.ddd.application.iam.service;

import com.example.ddd.application.iam.assembler.IamDtoAssembler;
import com.example.ddd.application.iam.command.BatchGrantPermissionCommand;
import com.example.ddd.application.iam.command.CreateRoleCommand;
import com.example.ddd.application.iam.command.GrantPermissionCommand;
import com.example.ddd.application.iam.dto.RoleDetailDTO;
import com.example.ddd.application.iam.port.IdGenerator;
import com.example.ddd.domain.iam.model.PermissionCode;
import com.example.ddd.domain.iam.model.Role;
import com.example.ddd.domain.iam.model.RoleCode;
import com.example.ddd.domain.iam.model.RoleId;
import com.example.ddd.domain.iam.repository.RoleRepository;
import com.example.ddd.domain.shared.DomainException;
import java.util.LinkedHashSet;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RoleAppService {

    private final RoleRepository roleRepository;
    private final IdGenerator idGenerator;

    public RoleAppService(RoleRepository roleRepository, IdGenerator idGenerator) {
        this.roleRepository = roleRepository;
        this.idGenerator = idGenerator;
    }

    @Transactional
    public RoleDetailDTO createRole(CreateRoleCommand command) {
        RoleCode code = RoleCode.of(command.getCode());
        if (roleRepository.existsByCode(code)) {
            throw new DomainException("角色编码已存在");
        }
        RoleId roleId = RoleId.of(idGenerator.nextId());
        Role role = Role.create(roleId, code, command.getName());
        roleRepository.save(role);
        return IamDtoAssembler.toRoleDetail(role);
    }

    @Transactional
    public RoleDetailDTO grantPermission(GrantPermissionCommand command) {
        Role role = roleRepository.findById(RoleId.of(command.getRoleId()));
        if (role == null) {
            throw new DomainException("角色不存在");
        }
        role.grantPermission(PermissionCode.of(command.getPermissionCode()));
        roleRepository.save(role);
        return IamDtoAssembler.toRoleDetail(role);
    }

    @Transactional
    public RoleDetailDTO revokePermission(GrantPermissionCommand command) {
        Role role = roleRepository.findById(RoleId.of(command.getRoleId()));
        if (role == null) {
            throw new DomainException("角色不存在");
        }
        role.revokePermission(PermissionCode.of(command.getPermissionCode()));
        roleRepository.save(role);
        return IamDtoAssembler.toRoleDetail(role);
    }

    @Transactional
    public RoleDetailDTO batchGrantPermissions(BatchGrantPermissionCommand command) {
        if (command.getPermissionCodes() == null || command.getPermissionCodes().isEmpty()) {
            throw new DomainException("权限编码列表不能为空");
        }
        Role role = roleRepository.findById(RoleId.of(command.getRoleId()));
        if (role == null) {
            throw new DomainException("角色不存在");
        }
        Set<PermissionCode> codes = new LinkedHashSet<>();
        for (String raw : command.getPermissionCodes()) {
            codes.add(PermissionCode.of(raw));
        }
        role.grantPermissions(codes);
        roleRepository.save(role);
        return IamDtoAssembler.toRoleDetail(role);
    }

    @Transactional
    public RoleDetailDTO batchRevokePermissions(BatchGrantPermissionCommand command) {
        if (command.getPermissionCodes() == null || command.getPermissionCodes().isEmpty()) {
            throw new DomainException("权限编码列表不能为空");
        }
        Role role = roleRepository.findById(RoleId.of(command.getRoleId()));
        if (role == null) {
            throw new DomainException("角色不存在");
        }
        Set<PermissionCode> codes = new LinkedHashSet<>();
        for (String raw : command.getPermissionCodes()) {
            codes.add(PermissionCode.of(raw));
        }
        role.revokePermissions(codes);
        roleRepository.save(role);
        return IamDtoAssembler.toRoleDetail(role);
    }

    public RoleDetailDTO getRoleDetail(Long roleId) {
        Role role = roleRepository.findById(RoleId.of(roleId));
        if (role == null) {
            throw new DomainException("角色不存在");
        }
        return IamDtoAssembler.toRoleDetail(role);
    }
}
