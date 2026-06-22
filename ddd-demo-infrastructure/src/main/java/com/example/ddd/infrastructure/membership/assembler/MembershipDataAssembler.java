package com.example.ddd.infrastructure.membership.assembler;

import com.example.ddd.domain.membership.model.Membership;
import com.example.ddd.domain.membership.model.MembershipId;
import com.example.ddd.domain.membership.model.MembershipLevel;
import com.example.ddd.domain.membership.model.Points;
import com.example.ddd.domain.order.model.UserId;
import com.example.ddd.infrastructure.membership.dataobject.MembershipDO;

public class MembershipDataAssembler {

    public static MembershipDO toDataObject(Membership membership) {
        if (membership == null) {
            return null;
        }

        MembershipDO dataObject = new MembershipDO();
        dataObject.setMembershipId(membership.getId().getValue());
        dataObject.setUserId(membership.getUserId().getValue());
        dataObject.setUserName(membership.getUserName());
        dataObject.setPoints(membership.getPoints().getValue());
        dataObject.setLevel(membership.getLevel().name());
        dataObject.setJoinedAt(membership.getJoinedAt());
        dataObject.setLastUpdatedAt(membership.getLastUpdatedAt());
        return dataObject;
    }

    public static Membership toDomainModel(MembershipDO dataObject) {
        if (dataObject == null) {
            return null;
        }

        return Membership.restore(
            MembershipId.of(dataObject.getMembershipId()),
            UserId.of(dataObject.getUserId()),
            dataObject.getUserName(),
            Points.of(dataObject.getPoints()),
            MembershipLevel.valueOf(dataObject.getLevel()),
            dataObject.getJoinedAt(),
            dataObject.getLastUpdatedAt()
        );
    }
}
