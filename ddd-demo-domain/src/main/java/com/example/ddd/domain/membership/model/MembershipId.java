package com.example.ddd.domain.membership.model;

import com.example.ddd.domain.shared.DomainException;
import java.util.Objects;

public final class MembershipId {

    private final Long value;

    private MembershipId(Long value) {
        if (value == null || value <= 0L) {
            throw new DomainException("会员ID必须大于0");
        }
        this.value = value;
    }

    public static MembershipId of(Long value) {
        return new MembershipId(value);
    }

    public Long getValue() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof MembershipId)) {
            return false;
        }
        MembershipId that = (MembershipId) other;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
