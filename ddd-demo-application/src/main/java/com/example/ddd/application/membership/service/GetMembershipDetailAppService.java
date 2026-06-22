package com.example.ddd.application.membership.service;

import com.example.ddd.application.membership.assembler.MembershipDtoAssembler;
import com.example.ddd.application.membership.dto.MembershipDetailDTO;
import com.example.ddd.domain.membership.model.Membership;
import com.example.ddd.domain.membership.model.MembershipId;
import com.example.ddd.domain.membership.repository.MembershipRepository;
import org.springframework.stereotype.Service;

@Service
public class GetMembershipDetailAppService {

    private final MembershipRepository membershipRepository;

    public GetMembershipDetailAppService(MembershipRepository membershipRepository) {
        this.membershipRepository = membershipRepository;
    }

    public MembershipDetailDTO getMembershipDetail(Long membershipId) {
        MembershipId id = MembershipId.of(membershipId);
        Membership membership = membershipRepository.findById(id);
        return MembershipDtoAssembler.toDetailDTO(membership);
    }
}
