package com.example.ddd.interfaces.iam.security;

import com.example.ddd.application.iam.port.RolePermissionCache;
import com.example.ddd.application.iam.port.TokenPayload;
import com.example.ddd.domain.iam.model.PermissionCode;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;


public class AuthorizationInterceptor implements HandlerInterceptor {

    private final RolePermissionCache rolePermissionCache;

    public AuthorizationInterceptor(RolePermissionCache rolePermissionCache) {
        this.rolePermissionCache = rolePermissionCache;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        RequirePermission required = handlerMethod.getMethodAnnotation(RequirePermission.class);
        if (required == null) {
            required = handlerMethod.getBeanType().getAnnotation(RequirePermission.class);
        }
        if (required == null) {
            return true;
        }
        TokenPayload payload = SecurityContextHolder.getPayload();
        if (payload == null) {
            throw new UnauthorizedException("未认证");
        }
        PermissionCode requiredCode = PermissionCode.of(required.value());
        List<String> roles = payload.getRoles();
        if (roles == null || roles.isEmpty()) {
            throw new ForbiddenException("权限不足：" + required.value());
        }
        for (String roleCode : roles) {
            Set<String> permissions = rolePermissionCache.getPermissions(roleCode);
            if (permissions != null) {
                for (String perm : permissions) {
                    if (PermissionCode.of(perm).implies(requiredCode)) {
                        return true;
                    }
                }
            }
        }
        throw new ForbiddenException("权限不足：" + required.value());
    }
}
