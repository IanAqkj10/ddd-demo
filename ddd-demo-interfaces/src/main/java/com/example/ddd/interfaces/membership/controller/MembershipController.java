package com.example.ddd.interfaces.membership.controller;

import com.example.ddd.application.membership.command.ConsumePointsCommand;
import com.example.ddd.application.membership.command.EarnPointsCommand;
import com.example.ddd.application.membership.command.RegisterMembershipCommand;
import com.example.ddd.application.membership.dto.MembershipDetailDTO;
import com.example.ddd.application.membership.service.ConsumePointsAppService;
import com.example.ddd.application.membership.service.EarnPointsAppService;
import com.example.ddd.application.membership.service.GetMembershipDetailAppService;
import com.example.ddd.application.membership.service.RegisterMembershipAppService;
import com.example.ddd.interfaces.membership.request.ConsumePointsRequest;
import com.example.ddd.interfaces.membership.request.EarnPointsRequest;
import com.example.ddd.interfaces.membership.request.RegisterMembershipRequest;
import com.example.ddd.interfaces.shared.response.ApiResponse;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/memberships")
public class MembershipController {

    private final RegisterMembershipAppService registerMembershipAppService;
    private final EarnPointsAppService earnPointsAppService;
    private final ConsumePointsAppService consumePointsAppService;
    private final GetMembershipDetailAppService getMembershipDetailAppService;

    public MembershipController(
        RegisterMembershipAppService registerMembershipAppService,
        EarnPointsAppService earnPointsAppService,
        ConsumePointsAppService consumePointsAppService,
        GetMembershipDetailAppService getMembershipDetailAppService
    ) {
        this.registerMembershipAppService = registerMembershipAppService;
        this.earnPointsAppService = earnPointsAppService;
        this.consumePointsAppService = consumePointsAppService;
        this.getMembershipDetailAppService = getMembershipDetailAppService;
    }

    @PostMapping
    public ApiResponse<MembershipDetailDTO> registerMembership(@Valid @RequestBody RegisterMembershipRequest request) {
        RegisterMembershipCommand command = new RegisterMembershipCommand(
            request.getUserId(),
            request.getUserName()
        );
        return ApiResponse.success(registerMembershipAppService.registerMembership(command));
    }

    @PostMapping("/{membershipId}/earn-points")
    public ApiResponse<MembershipDetailDTO> earnPoints(
        @PathVariable("membershipId") Long membershipId,
        @Valid @RequestBody EarnPointsRequest request
    ) {
        EarnPointsCommand command = new EarnPointsCommand(
            membershipId,
            request.getPoints(),
            request.getReason()
        );
        return ApiResponse.success(earnPointsAppService.earnPoints(command));
    }

    @PostMapping("/{membershipId}/consume-points")
    public ApiResponse<MembershipDetailDTO> consumePoints(
        @PathVariable("membershipId") Long membershipId,
        @Valid @RequestBody ConsumePointsRequest request
    ) {
        ConsumePointsCommand command = new ConsumePointsCommand(
            membershipId,
            request.getPoints(),
            request.getReason()
        );
        return ApiResponse.success(consumePointsAppService.consumePoints(command));
    }

    @GetMapping("/{membershipId}")
    public ApiResponse<MembershipDetailDTO> getMembership(@PathVariable("membershipId") Long membershipId) {
        return ApiResponse.success(getMembershipDetailAppService.getMembershipDetail(membershipId));
    }
}
