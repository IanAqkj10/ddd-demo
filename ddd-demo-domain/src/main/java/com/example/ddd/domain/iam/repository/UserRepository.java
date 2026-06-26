package com.example.ddd.domain.iam.repository;

import com.example.ddd.domain.iam.model.IamUserId;
import com.example.ddd.domain.iam.model.User;
import com.example.ddd.domain.iam.model.Username;

public interface UserRepository {

    void save(User user);

    User findById(IamUserId id);

    User findByUsername(Username username);

    boolean existsByUsername(Username username);
}
