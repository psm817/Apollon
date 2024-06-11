package com.example.Apollon.global.initData;

import com.example.Apollon.domain.comment.service.CommentService;
import com.example.Apollon.domain.member.entity.Member;
import com.example.Apollon.domain.member.service.MemberService;
import com.example.Apollon.domain.music.entity.Music;
import com.example.Apollon.domain.music.repository.MusicRepository;
import com.example.Apollon.domain.music.service.MusicService;
import com.example.Apollon.domain.studio.entity.Studio;
import com.example.Apollon.domain.studio.service.StudioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

@Configuration
@Profile("dev")
public class Dev {
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    MusicService musicService;

    @Autowired
    MusicRepository musicRepository;

    @Value("${custom.fileDirPath}")
    private String fileDirPath;

    @Bean
    public ApplicationRunner init(MemberService memberService, StudioService studioService, CommentService commentService) {

        return args -> {
            Member m1 = memberService.signup("admin", "admin", "admin", "admin@test.com");
            Member m2 = memberService.signup("user1", "user1", "user1", "user1@test.com");
            Member m3 = memberService.signup("user2", "user2", "user2", "user2@test.com");

            Studio s1 = studioService.createOrUpdate(m1, 55, 1);
            Studio s2 = studioService.createOrUpdate(m2, 12, 1);
            Studio s3 = studioService.createOrUpdate(m3, 5, 1);

            commentService.create(m2, s3, "테스트입니다.1", "테스트입니다.1");
            commentService.create(m2, s3, "테스트입니다.2", "테스트입니다.2");
            commentService.create(m2, s3, "테스트입니다.3", "테스트입니다.3");

            commentService.create(m3, s2, "테스트입니다.1", "테스트입니다.1");
            commentService.create(m3, s2, "테스트입니다.2", "테스트입니다.2");
            commentService.create(m3, s2, "테스트입니다.3", "테스트입니다.3");

        };
    }
}