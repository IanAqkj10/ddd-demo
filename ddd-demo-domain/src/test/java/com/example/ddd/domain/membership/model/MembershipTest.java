package com.example.ddd.domain.membership.model;

import com.example.ddd.domain.order.model.UserId;
import com.example.ddd.domain.shared.DomainException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MembershipTest {

    @Test
    public void testCreateMembership() {
        MembershipId id = MembershipId.of(1L);
        UserId userId = UserId.of(1001L);
        String userName = "张三";

        Membership membership = Membership.create(id, userId, userName);

        assertNotNull(membership);
        assertEquals(id, membership.getId());
        assertEquals(userId, membership.getUserId());
        assertEquals(userName, membership.getUserName());
        assertEquals(0, membership.getPoints().getValue());
        assertEquals(MembershipLevel.BRONZE, membership.getLevel());
    }

    @Test
    public void testEarnPoints() {
        Membership membership = Membership.create(
            MembershipId.of(1L),
            UserId.of(1001L),
            "张三"
        );

        membership.earnPoints(Points.of(100), "购物赠送");

        assertEquals(100, membership.getPoints().getValue());
        assertEquals(MembershipLevel.BRONZE, membership.getLevel());
    }

    @Test
    public void testLevelUpgrade() {
        Membership membership = Membership.create(
            MembershipId.of(1L),
            UserId.of(1001L),
            "张三"
        );

        membership.earnPoints(Points.of(1000), "大额消费");

        assertEquals(1000, membership.getPoints().getValue());
        assertEquals(MembershipLevel.SILVER, membership.getLevel());

        membership.earnPoints(Points.of(4000), "继续消费");

        assertEquals(5000, membership.getPoints().getValue());
        assertEquals(MembershipLevel.GOLD, membership.getLevel());
    }

    @Test
    public void testConsumePoints() {
        Membership membership = Membership.create(
            MembershipId.of(1L),
            UserId.of(1001L),
            "张三"
        );

        membership.earnPoints(Points.of(500), "购物赠送");
        membership.consumePoints(Points.of(200), "积分兑换");

        assertEquals(300, membership.getPoints().getValue());
    }

    @Test
    public void testConsumePointsInsufficientBalance() {
        Membership membership = Membership.create(
            MembershipId.of(1L),
            UserId.of(1001L),
            "张三"
        );

        membership.earnPoints(Points.of(100), "购物赠送");

        assertThrows(DomainException.class, () -> {
            membership.consumePoints(Points.of(200), "积分兑换");
        });
    }

    @Test
    public void testEarnPointsWithInvalidReason() {
        Membership membership = Membership.create(
            MembershipId.of(1L),
            UserId.of(1001L),
            "张三"
        );

        assertThrows(DomainException.class, () -> {
            membership.earnPoints(Points.of(100), "");
        });
    }
}
