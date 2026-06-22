package com.example.ddd.application.membership.assembler;

import com.example.ddd.application.membership.dto.MembershipDetailDTO;
import com.example.ddd.domain.membership.model.Membership;

public class MembershipDtoAssembler {

    public static MembershipDetailDTO toDetailDTO(Membership membership) {
        if (membership == null) {
            return null;
        }

        MembershipDetailDTO dto = new MembershipDetailDTO();
        dto.setId(membership.getId().getValue());
        dto.setUserId(membership.getUserId().getValue());
        dto.setUserName(membership.getUserName());
        dto.setPoints(membership.getPoints().getValue());
        dto.setLevel(membership.getLevel().name());
        dto.setLevelDescription(membership.getLevel().getDescription());
        dto.setJoinedAt(membership.getJoinedAt());
        dto.setLastUpdatedAt(membership.getLastUpdatedAt());
        return dto;
    }
}
