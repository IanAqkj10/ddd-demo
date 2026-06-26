package com.example.ddd.interfaces.iam.security;

import com.example.ddd.application.iam.port.TokenPayload;
import com.example.ddd.domain.iam.model.IamUserId;

public final class SecurityContextHolder {

    private static final ThreadLocal<TokenPayload> CURRENT_PAYLOAD = new ThreadLocal<>();

    private SecurityContextHolder() {
    }

    public static void setPayload(TokenPayload payload) {
        CURRENT_PAYLOAD.set(payload);
    }

    public static TokenPayload getPayload() {
        return CURRENT_PAYLOAD.get();
    }

    public static IamUserId getCurrentUserId() {
        TokenPayload payload = CURRENT_PAYLOAD.get();
        return payload == null ? null : IamUserId.of(payload.getUserId());
    }

    public static void clear() {
        CURRENT_PAYLOAD.remove();
    }
}
