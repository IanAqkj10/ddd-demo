package com.example.ddd.domain.iam.repository;

import com.example.ddd.domain.iam.model.Role;
import com.example.ddd.domain.iam.model.RoleCode;
import com.example.ddd.domain.iam.model.RoleId;
import java.util.Collection;
import java.util.List;

public interface RoleRepository {

    void save(Role role);

    Role findById(RoleId id);

    Role findByCode(RoleCode code);

    boolean existsByCode(RoleCode code);

    List<Role> findAllByIds(Collection<RoleId> ids);
}
