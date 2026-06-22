package com.example.ddd.domain.membership.service;

import com.example.ddd.domain.membership.model.Membership;
import com.example.ddd.domain.membership.model.MembershipId;
import com.example.ddd.domain.membership.model.Points;
import com.example.ddd.domain.order.model.UserId;
import com.example.ddd.domain.shared.DomainException;

public class MembershipDomainService {

    public Membership registerMembership(MembershipId id, UserId userId, String userName) {
        if (id == null) {
            throw new DomainException("会员ID不能为空");
        }
        if (userId == null) {
            throw new DomainException("用户ID不能为空");
        }
        if (userName == null || userName.trim().isEmpty()) {
            throw new DomainException("用户名不能为空");
        }
        return Membership.create(id, userId, userName);
    }

    public void earnPointsFromOrder(Membership membership, int orderAmount) {
        if (orderAmount <= 0) {
            throw new DomainException("订单金额必须大于0");
        }
        int earnedPoints = calculatePointsFromOrderAmount(orderAmount);
        membership.earnPoints(Points.of(earnedPoints), "订单消费赚取积分");
    }

    private int calculatePointsFromOrderAmount(int orderAmount) {
        return orderAmount / 10;
    }
}
