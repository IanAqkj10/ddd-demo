package com.example.ddd.domain.iam.model;

import com.example.ddd.domain.shared.DomainException;
import java.util.Objects;
import java.util.regex.Pattern;

public final class RoleCode {

    private static final Pattern PATTERN = Pattern.compile("^[A-Z][A-Z0-9_]{1,63}$");

    private final String value;

    private RoleCode(String value) {
        if (value == null || !PATTERN.matcher(value).matches()) {
            throw new DomainException("角色编码格式不合法，需以大写字母开头，仅允许大写字母数字下划线");
        }
        this.value = value;
    }

    public static RoleCode of(String value) {
        return new RoleCode(value);
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof RoleCode)) {
            return false;
        }
        RoleCode that = (RoleCode) other;
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
