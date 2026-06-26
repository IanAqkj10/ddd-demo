package com.example.ddd.domain.iam.model;

import com.example.ddd.domain.shared.DomainException;
import java.util.Objects;
import java.util.regex.Pattern;

public final class Username {

    private static final Pattern PATTERN = Pattern.compile("^[A-Za-z0-9_]{3,32}$");

    private final String value;

    private Username(String value) {
        if (value == null || !PATTERN.matcher(value).matches()) {
            throw new DomainException("用户名格式不合法，仅允许字母数字下划线，长度3-32");
        }
        this.value = value;
    }

    public static Username of(String value) {
        return new Username(value);
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Username)) {
            return false;
        }
        Username that = (Username) other;
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
