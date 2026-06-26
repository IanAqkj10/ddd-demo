package com.example.ddd.infrastructure.iam.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.ddd.domain.iam.model.Role;
import com.example.ddd.domain.iam.model.RoleCode;
import com.example.ddd.domain.iam.model.RoleId;
import com.example.ddd.domain.iam.repository.RoleRepository;
import com.example.ddd.infrastructure.iam.assembler.RoleDataAssembler;
import com.example.ddd.infrastructure.iam.dataobject.RoleDO;
import com.example.ddd.infrastructure.iam.mapper.RoleMapper;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class MybatisPlusRoleRepository implements RoleRepository {

    private final RoleMapper roleMapper;

    public MybatisPlusRoleRepository(RoleMapper roleMapper) {
        this.roleMapper = roleMapper;
    }

    @Override
    public void save(Role role) {
        RoleDO dataObject = RoleDataAssembler.toDataObject(role);
        RoleDO existing = roleMapper.selectById(role.getId().getValue());
        if (existing == null) {
            roleMapper.insert(dataObject);
        } else {
            roleMapper.updateById(dataObject);
        }
    }

    @Override
    public Role findById(RoleId id) {
        RoleDO dataObject = roleMapper.selectById(id.getValue());
        return RoleDataAssembler.toDomainModel(dataObject);
    }

    @Override
    public Role findByCode(RoleCode code) {
        RoleDO dataObject = roleMapper.selectByCode(code.getValue());
        return RoleDataAssembler.toDomainModel(dataObject);
    }

    @Override
    public boolean existsByCode(RoleCode code) {
        LambdaQueryWrapper<RoleDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RoleDO::getCode, code.getValue());
        return roleMapper.selectCount(queryWrapper) > 0;
    }

    @Override
    public List<Role> findAllByIds(Collection<RoleId> ids) {
        List<Role> roles = new ArrayList<>();
        if (ids == null || ids.isEmpty()) {
            return roles;
        }
        List<Long> rawIds = new ArrayList<>();
        for (RoleId id : ids) {
            rawIds.add(id.getValue());
        }
        List<RoleDO> dataObjects = roleMapper.selectBatchIds(rawIds);
        for (RoleDO dataObject : dataObjects) {
            roles.add(RoleDataAssembler.toDomainModel(dataObject));
        }
        return roles;
    }
}
