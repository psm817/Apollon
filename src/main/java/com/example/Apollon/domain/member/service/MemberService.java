package com.example.Apollon.domain.member.service;
import com.example.Apollon.domain.member.entity.Member;
import com.example.Apollon.domain.member.repository.MemberRepository;
import com.example.Apollon.global.DataNotFoundException;
import com.example.Apollon.global.security.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean usernameExists(String username) {
        return memberRepository.existsByUsername(username);
    }

    public Member signup(String username, String password, String nickname, String email, String imageFileName) {
        // Check for duplicate username
        if (memberRepository.findByUsername(username).isPresent()) {
            throw new IllegalStateException("Username already exists");
        }

        // Check for duplicate email
        if (email != null && memberRepository.findByEmail(email).isPresent()) {
            throw new IllegalStateException("Email already exists");
        }

        Member member = Member
                .builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .nickname(nickname)
                .email(email)
                .image(imageFileName) // Set the image file name
                .createDate(LocalDateTime.now())
                .build();

        return memberRepository.save(member);
    }
    @Transactional
    public Member whenSocialLogin(String providerTypeCode, String username, String nickname, String email) {
        Optional<Member> opMember = findByUsername(username);

        if (opMember.isPresent()) return opMember.get();

        // 소셜 로그인을 통한 가입 시 비번은 없다.
        return signup(username, "", nickname, email,"/images/none.png"); // 최초 로그인 시 딱 한번 실행
    }

    private Optional<Member> findByUsername(String username) {
        return memberRepository.findByUsername(username);
    }

    public Member getMember(String username) {
        Optional<Member> member = this.memberRepository.findByUsername(username);
        if (member.isPresent()) {
            return member.get();
        } else {
            throw new DataNotFoundException("Member not found");
        }
    }

    public void modify(Member member, String username, String password, String nickname, String email) {
        member.setUsername(username);
        member.setPassword(passwordEncoder.encode(password));
        member.setNickname(nickname);
        member.setEmail(email);
        member.setModifyDate(LocalDateTime.now());

        this.memberRepository.save(member);
    }

    public void delete(Member member) {
        this.memberRepository.delete(member);
    }
}