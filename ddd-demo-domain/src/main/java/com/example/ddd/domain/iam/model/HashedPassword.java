package com.example.ddd.domain.iam.model;

import com.example.ddd.domain.shared.DomainException;
import java.util.Objects;

public final class HashedPassword {

    private final String hash;
    private final String salt;

    private HashedPassword(String hash, String salt) {
        if (hash == null || hash.trim().isEmpty()) {
            throw new DomainException("密码哈希不能为空");
        }
        if (salt == null || salt.trim().isEmpty()) {
            throw new DomainException("密码盐不能为空");
        }
        this.hash = hash;
        this.salt = salt;
    }

    public static HashedPassword of(String hash, String salt) {
        return new HashedPassword(hash, salt);
    }

    public String getHash() {
        return hash;
    }

    public String getSalt() {
        return salt;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof HashedPassword)) {
            return false;
        }
        HashedPassword that = (HashedPassword) other;
        return Objects.equals(hash, that.hash) && Objects.equals(salt, that.salt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hash, salt);
    }
}
