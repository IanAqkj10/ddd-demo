package com.example.ddd.infrastructure.membership;

import com.example.ddd.domain.membership.service.MembershipDomainService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MembershipBeanConfiguration {

    @Bean
    public MembershipDomainService membershipDomainService() {
        return new MembershipDomainService();
    }
}
