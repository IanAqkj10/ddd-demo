package com.example.ddd.domain.membership.repository;

import com.example.ddd.domain.membership.model.Membership;
import com.example.ddd.domain.membership.model.MembershipId;
import com.example.ddd.domain.order.model.UserId;

public interface MembershipRepository {

    void save(Membership membership);

    Membership findById(MembershipId id);

    Membership findByUserId(UserId userId);

    boolean existsByUserId(UserId userId);
}
