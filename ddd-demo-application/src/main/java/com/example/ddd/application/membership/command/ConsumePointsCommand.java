package com.example.ddd.application.membership.command;

public class ConsumePointsCommand {

    private final Long membershipId;
    private final int points;
    private final String reason;

    public ConsumePointsCommand(Long membershipId, int points, String reason) {
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
