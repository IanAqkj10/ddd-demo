package com.example.ddd.infrastructure.iam.gateway;

import com.example.ddd.application.iam.port.TokenPayload;
import com.example.ddd.application.iam.port.TokenStore;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class JwtTokenStore implements TokenStore {

    private final Key signingKey;
    private final long expirationMillis;
    private final Set<String> blacklist = ConcurrentHashMap.newKeySet();

    public JwtTokenStore(String secret, long expirationMillis) {
        this.signingKey = Keys.hmacShaKeyFor(padSecret(secret));
        this.expirationMillis = expirationMillis;
    }

    @Override
    public String issue(Long userId, String username, List<String> roles) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
            .setSubject(String.valueOf(userId))
            .claim("username", username)
            .claim("roles", roles)
            .setIssuedAt(new Date(now))
            .setExpiration(new Date(now + expirationMillis))
            .signWith(signingKey, SignatureAlgorithm.HS256)
            .compact();
    }

    @Override
    @SuppressWarnings("unchecked")
    public TokenPayload resolve(String token) {
        if (token == null || blacklist.contains(token)) {
            return null;
        }
        try {
            Claims claims = Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

            Long userId = Long.valueOf(claims.getSubject());
            String username = claims.get("username", String.class);
            List<String> roles = (List<String>) claims.get("roles");
            return new TokenPayload(userId, username, roles);
        } catch (JwtException | IllegalArgumentException e) {
            return null;
        }
    }

    @Override
    public void revoke(String token) {
        blacklist.add(token);
    }

    private byte[] padSecret(String secret) {
        byte[] raw = secret.getBytes();
        if (raw.length >= 32) {
            return raw;
        }
        byte[] padded = new byte[32];
        System.arraycopy(raw, 0, padded, 0, raw.length);
        return padded;
    }
}
