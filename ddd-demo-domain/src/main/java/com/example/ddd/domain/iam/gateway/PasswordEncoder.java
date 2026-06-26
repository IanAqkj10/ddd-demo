package com.example.ddd.domain.iam.gateway;

import com.example.ddd.domain.iam.model.HashedPassword;

public interface PasswordEncoder {

    HashedPassword encode(String rawPassword);

    boolean matches(String rawPassword, HashedPassword hashedPassword);
}
