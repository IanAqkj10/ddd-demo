package com.example.ddd.domain.iam.service;

import com.example.ddd.domain.iam.gateway.PasswordEncoder;
import com.example.ddd.domain.iam.model.User;
import com.example.ddd.domain.shared.DomainException;

public class IamLoginService {

    public void authenticate(User user, String rawPassword, PasswordEncoder passwordEncoder) {
        if (user == null) {
            throw new DomainException("用户名或密码错误");
        }
        if (!user.isActive()) {
            throw new DomainException("用户已被禁用");
        }
        if (rawPassword == null || rawPassword.isEmpty()) {
            throw new DomainException("用户名或密码错误");
        }
        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new DomainException("用户名或密码错误");
        }
    }
}
