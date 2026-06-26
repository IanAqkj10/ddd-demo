package com.example.ddd.domain.iam.model;

import com.example.ddd.domain.shared.DomainException;
import java.util.Objects;
import java.util.regex.Pattern;

public final class PermissionCode {

    private static final Pattern PATTERN = Pattern.compile("^[a-z][a-z0-9_]*(:[a-z0-9_*]+){1,3}$");

    private final String value;

    private PermissionCode(String value) {
        if (value == null || !PATTERN.matcher(value).matches()) {
            throw new DomainException("权限编码格式不合法，需为 resource:action 形式，例如 order:create");
        }
        this.value = value;
    }

    public static PermissionCode of(String value) {
        return new PermissionCode(value);
    }

    public String getValue() {
        return value;
    }

    public boolean implies(PermissionCode required) {
        if (required == null) {
            return false;
        }
        if (this.value.equals(required.value)) {
            return true;
        }
        String[] mineParts = this.value.split(":");
        String[] requiredParts = required.value.split(":");
        if (mineParts.length != requiredParts.length) {
            return false;
        }
        for (int i = 0; i < mineParts.length; i++) {
            if ("*".equals(mineParts[i])) {
                continue;
            }
            if (!mineParts[i].equals(requiredParts[i])) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof PermissionCode)) {
            return false;
        }
        PermissionCode that = (PermissionCode) other;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
