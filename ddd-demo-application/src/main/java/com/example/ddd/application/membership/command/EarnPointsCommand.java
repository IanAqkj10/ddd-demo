package com.example.ddd.application.membership.command;

public class EarnPointsCommand {

    private final Long membershipId;
    private final int points;
    private final String reason;

    public EarnPointsCommand(Long membershipId, int points, String reason) {
        this.membershipId = membershipId;
        this.points = points;
        this.reason = reason;
    }

    public Long getMembershipId() {
        return membershipId;
    }

    public int getPoints() {
        return points;
    }

    public String getReason() {
        return reason;
    }
}
