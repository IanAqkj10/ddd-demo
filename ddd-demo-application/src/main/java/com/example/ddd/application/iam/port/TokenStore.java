package com.example.ddd.application.iam.port;

import java.util.List;

public interface TokenStore {

    String issue(Long userId, String username, List<String> roles);

    TokenPayload resolve(String token);

    void revoke(String token);
}
