package com.example.Apollon.global.initData;

import com.example.Apollon.domain.member.entity.Member;
import com.example.Apollon.domain.member.service.MemberService;
import com.example.Apollon.domain.studio.service.StudioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Profile("dev")
public class Dev {
    @Autowired
    PasswordEncoder passwordEncoder;

    @Bean
    public ApplicationRunner init(MemberService memberService, StudioService studioService) {
        return args -> {
            Member m1 = memberService.signup("admin", "admin", "admin", "admin@test.com");
            Member m2 = memberService.signup("user1", "user1", "user1", "user1@test.com");
            Member m3 = memberService.signup("user2", "user2", "user2", "user2@test.com");

            studioService.create(m1, 55,1);
            studioService.create(m2, 12,1);
            studioService.create(m3, 5,1);
        };
    }
}