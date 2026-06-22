package com.example.ddd.application.membership.service;

import com.example.ddd.application.membership.assembler.MembershipDtoAssembler;
import com.example.ddd.application.membership.command.EarnPointsCommand;
import com.example.ddd.application.membership.dto.MembershipDetailDTO;
import com.example.ddd.domain.membership.model.Membership;
import com.example.ddd.domain.membership.model.MembershipId;
import com.example.ddd.domain.membership.model.Points;
import com.example.ddd.domain.membership.repository.MembershipRepository;
import com.example.ddd.domain.shared.DomainException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EarnPointsAppService {

    private final MembershipRepository membershipRepository;

    public EarnPointsAppService(MembershipRepository membershipRepository) {
        this.membershipRepository = membershipRepository;
    }

    @Transactional
    public MembershipDetailDTO earnPoints(EarnPointsCommand command) {
        MembershipId membershipId = MembershipId.of(command.getMembershipId());
        Membership membership = membershipRepository.findById(membershipId);

        if (membership == null) {
            throw new DomainException("会员不存在");
        }

        membership.earnPoints(Points.of(command.getPoints()), command.getReason());
        membershipRepository.save(membership);

        return MembershipDtoAssembler.toDetailDTO(membership);
    }
}
