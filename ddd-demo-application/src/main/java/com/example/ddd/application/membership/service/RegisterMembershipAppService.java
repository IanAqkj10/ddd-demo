package com.example.ddd.application.membership.service;

import com.example.ddd.application.membership.assembler.MembershipDtoAssembler;
import com.example.ddd.application.membership.command.RegisterMembershipCommand;
import com.example.ddd.application.membership.dto.MembershipDetailDTO;
import com.example.ddd.domain.membership.model.Membership;
import com.example.ddd.domain.membership.model.MembershipId;
import com.example.ddd.domain.membership.repository.MembershipRepository;
import com.example.ddd.domain.membership.service.MembershipDomainService;
import com.example.ddd.domain.order.model.UserId;
import com.example.ddd.domain.shared.DomainException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegisterMembershipAppService {

    private final MembershipRepository membershipRepository;
    private final MembershipDomainService membershipDomainService;

    public RegisterMembershipAppService(
        MembershipRepository membershipRepository,
        MembershipDomainService membershipDomainService
    ) {
        this.membershipRepository = membershipRepository;
        this.membershipDomainService = membershipDomainService;
    }

    @Transactional
    public MembershipDetailDTO registerMembership(RegisterMembershipCommand command) {
        UserId userId = UserId.of(command.getUserId());

        if (membershipRepository.existsByUserId(userId)) {
            throw new DomainException("该用户已注册会员");
        }

        MembershipId membershipId = MembershipId.of(System.currentTimeMillis());
        Membership membership = membershipDomainService.registerMembership(
            membershipId,
            userId,
            command.getUserName()
        );

        membershipRepository.save(membership);

        return MembershipDtoAssembler.toDetailDTO(membership);
    }
}
