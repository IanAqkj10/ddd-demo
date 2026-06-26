package com.example.ddd.interfaces.iam.config;

import com.example.ddd.application.iam.port.RolePermissionCache;
import com.example.ddd.application.iam.port.TokenStore;
import com.example.ddd.interfaces.iam.security.AuthenticationInterceptor;
import com.example.ddd.interfaces.iam.security.AuthorizationInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class IamWebMvcConfiguration implements WebMvcConfigurer {

    private final TokenStore tokenStore;
    private final RolePermissionCache rolePermissionCache;

    public IamWebMvcConfiguration(TokenStore tokenStore, RolePermissionCache rolePermissionCache) {
        this.tokenStore = tokenStore;
        this.rolePermissionCache = rolePermissionCache;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthenticationInterceptor(tokenStore))
            .addPathPatterns("/api/**")
            .excludePathPatterns("/api/auth/login");

        registry.addInterceptor(new AuthorizationInterceptor(rolePermissionCache))
            .addPathPatterns("/api/**")
            .excludePathPatterns("/api/auth/login");
    }
}
