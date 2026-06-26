package com.example.ddd.interfaces.iam.security;

import com.example.ddd.application.iam.port.TokenPayload;
import com.example.ddd.application.iam.port.TokenStore;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class AuthenticationInterceptor implements HandlerInterceptor {

    private static final String HEADER = "Authorization";
    private static final String PREFIX = "Bearer ";

    private final TokenStore tokenStore;

    public AuthenticationInterceptor(TokenStore tokenStore) {
        this.tokenStore = tokenStore;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String header = request.getHeader(HEADER);
        if (header == null || !header.startsWith(PREFIX)) {
            throw new UnauthorizedException("未携带有效 Token");
        }
        String token = header.substring(PREFIX.length()).trim();
        TokenPayload payload = tokenStore.resolve(token);
        if (payload == null) {
            throw new UnauthorizedException("Token 无效或已过期");
        }
        SecurityContextHolder.setPayload(payload);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        SecurityContextHolder.clear();
    }
}
