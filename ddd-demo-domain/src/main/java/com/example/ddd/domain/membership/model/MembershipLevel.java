package com.example.ddd.domain.membership.model;

public enum MembershipLevel {

    BRONZE("青铜会员", 0),
    SILVER("白银会员", 1000),
    GOLD("黄金会员", 5000),
    PLATINUM("铂金会员", 10000);

    private final String description;
    private final int requiredPoints;

    MembershipLevel(String description, int requiredPoints) {
        this.description = description;
        this.requiredPoints = requiredPoints;
    }

    public String getDescription() {
        return description;
    }

    public int getRequiredPoints() {
        return requiredPoints;
    }

    public static MembershipLevel calculateLevel(int totalPoints) {
        if (totalPoints >= PLATINUM.requiredPoints) {
            return PLATINUM;
        }
        if (totalPoints >= GOLD.requiredPoints) {
            return GOLD;
        }
        if (totalPoints >= SILVER.requiredPoints) {
            return SILVER;
        }
        return BRONZE;
    }

    public MembershipLevel nextLevel() {
        switch (this) {
            case BRONZE:
                return SILVER;
            case SILVER:
                return GOLD;
            case GOLD:
                return PLATINUM;
            case PLATINUM:
            default:
                return PLATINUM;
        }
    }

    public boolean canUpgrade(int currentPoints) {
        if (this == PLATINUM) {
            return false;
        }
        return currentPoints >= nextLevel().requiredPoints;
    }
}
