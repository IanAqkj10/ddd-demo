package com.example.ddd.infrastructure.iam.gateway;

import com.example.ddd.domain.iam.gateway.PasswordEncoder;
import com.example.ddd.domain.iam.model.HashedPassword;
import com.example.ddd.domain.shared.DomainException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class Sha256PasswordEncoder implements PasswordEncoder {

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final char[] HEX = "0123456789abcdef".toCharArray();

    @Override
    public HashedPassword encode(String rawPassword) {
        if (rawPassword == null || rawPassword.isEmpty()) {
            throw new DomainException("原始密码不能为空");
        }
        String salt = randomSalt();
        String hash = sha256(salt + rawPassword);
        return HashedPassword.of(hash, salt);
    }

    @Override
    public boolean matches(String rawPassword, HashedPassword hashedPassword) {
        if (rawPassword == null || hashedPassword == null) {
            return false;
        }
        String hash = sha256(hashedPassword.getSalt() + rawPassword);
        return constantTimeEquals(hash, hashedPassword.getHash());
    }

    private String randomSalt() {
        byte[] bytes = new byte[16];
        RANDOM.nextBytes(bytes);
        return toHex(bytes);
    }

    private String sha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return toHex(digest.digest(input.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 算法不可用", e);
        }
    }

    private String toHex(byte[] bytes) {
        char[] chars = new char[bytes.length * 2];
        for (int i = 0; i < bytes.length; i++) {
            int v = bytes[i] & 0xFF;
            chars[i * 2] = HEX[v >>> 4];
            chars[i * 2 + 1] = HEX[v & 0x0F];
        }
        return new String(chars);
    }

    private boolean constantTimeEquals(String a, String b) {
        if (a.length() != b.length()) {
            return false;
        }
        int result = 0;
        for (int i = 0; i < a.length(); i++) {
            result |= a.charAt(i) ^ b.charAt(i);
        }
        return result == 0;
    }
}
