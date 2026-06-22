package com.example.ddd.domain.membership.model;

import com.example.ddd.domain.order.model.UserId;
import com.example.ddd.domain.shared.DomainException;
import java.time.LocalDateTime;

public class Membership {

    private final MembershipId id;
    private final UserId userId;
    private final String userName;
    private Points points;
    private MembershipLevel level;
    private final LocalDateTime joinedAt;
    private LocalDateTime lastUpdatedAt;

    private Membership(
        MembershipId id,
        UserId userId,
        String userName,
        Points points,
        MembershipLevel level,
        LocalDateTime joinedAt,
        LocalDateTime lastUpdatedAt
    ) {
        if (id == null) {
            throw new DomainException("会员ID不能为空");
        }
        if (userId == null) {
            throw new DomainException("用户ID不能为空");
        }
        if (userName == null || userName.trim().isEmpty()) {
            throw new DomainException("用户名不能为空");
        }
        if (points == null) {
            throw new DomainException("积分不能为空");
        }
        if (level == null) {
            throw new DomainException("会员等级不能为空");
        }
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.points = points;
        this.level = level;
        this.joinedAt = joinedAt == null ? LocalDateTime.now() : joinedAt;
        this.lastUpdatedAt = lastUpdatedAt == null ? LocalDateTime.now() : lastUpdatedAt;
    }

    public static Membership create(MembershipId id, UserId userId, String userName) {
        return new Membership(
            id,
            userId,
            userName,
            Points.zero(),
            MembershipLevel.BRONZE,
            LocalDateTime.now(),
            LocalDateTime.now()
        );
    }

    public static Membership restore(
        MembershipId id,
        UserId userId,
        String userName,
        Points points,
        MembershipLevel level,
        LocalDateTime joinedAt,
        LocalDateTime lastUpdatedAt
    ) {
        return new Membership(id, userId, userName, points, level, joinedAt, lastUpdatedAt);
    }

    public void earnPoints(Points earnedPoints, String reason) {
        if (earnedPoints == null || earnedPoints.getValue() <= 0) {
            throw new DomainException("赚取的积分必须大于0");
        }
        if (reason == null || reason.trim().isEmpty()) {
            throw new DomainException("积分变更原因不能为空");
        }
        this.points = this.points.add(earnedPoints);
        this.lastUpdatedAt = LocalDateTime.now();
        tryUpgradeLevel();
    }

    public void consumePoints(Points consumedPoints, String reason) {
        if (consumedPoints == null || consumedPoints.getValue() <= 0) {
            throw new DomainException("消费的积分必须大于0");
        }
        if (reason == null || reason.trim().isEmpty()) {
            throw new DomainException("积分变更原因不能为空");
        }
        if (!this.points.greaterThanOrEqual(consumedPoints)) {
            throw new DomainException("积分不足，当前积分：" + this.points.getValue());
        }
        this.points = this.points.subtract(consumedPoints);
        this.lastUpdatedAt = LocalDateTime.now();
    }

    private void tryUpgradeLevel() {
        MembershipLevel calculatedLevel = MembershipLevel.calculateLevel(this.points.getValue());
        if (calculatedLevel.ordinal() > this.level.ordinal()) {
            this.level = calculatedLevel;
        }
    }

    public MembershipId getId() {
        return id;
    }

    public UserId getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public Points getPoints() {
        return points;
    }

    public MembershipLevel getLevel() {
        return level;
    }

    public LocalDateTime getJoinedAt() {
        return joinedAt;
    }

    public LocalDateTime getLastUpdatedAt() {
        return lastUpdatedAt;
    }
}
