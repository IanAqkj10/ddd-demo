package com.example.ddd.infrastructure.iam;

import com.example.ddd.application.iam.port.IdGenerator;
import com.example.ddd.application.iam.port.RolePermissionCache;
import com.example.ddd.application.iam.port.TokenStore;
import com.example.ddd.domain.iam.gateway.PasswordEncoder;
import com.example.ddd.domain.iam.repository.RoleRepository;
import com.example.ddd.domain.iam.service.IamAuthorizationService;
import com.example.ddd.domain.iam.service.IamLoginService;
import com.example.ddd.infrastructure.iam.gateway.JwtTokenStore;
import com.example.ddd.infrastructure.iam.gateway.LocalRolePermissionCache;
import com.example.ddd.infrastructure.iam.gateway.Sha256PasswordEncoder;
import com.example.ddd.infrastructure.iam.gateway.SnowflakeIdGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IamBeanConfiguration {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new Sha256PasswordEncoder();
    }

    @Bean
    public TokenStore tokenStore(
            @Value("${iam.jwt.secret:ddd-demo-default-secret-key}") String secret,
            @Value("${iam.jwt.expiration:7200000}") long expirationMillis) {
        return new JwtTokenStore(secret, expirationMillis);
    }

    @Bean
    public RolePermissionCache rolePermissionCache(RoleRepository roleRepository) {
        return new LocalRolePermissionCache(roleRepository);
    }

    @Bean
    public IdGenerator idGenerator() {
        return new SnowflakeIdGenerator();
    }

    @Bean
    public IamLoginService iamLoginService() {
        return new IamLoginService();
    }

    @Bean
    public IamAuthorizationService iamAuthorizationService() {
        return new IamAuthorizationService();
    }
}
