package com.example.Apollon.domain.member.service;

import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
import com.example.Apollon.domain.member.entity.Member;
import com.example.Apollon.domain.member.repository.MemberRepository;
import com.example.Apollon.global.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

//@Service
//@RequiredArgsConstructor
//public class MemberService {
//    private final MemberRepository memberRepository;
//    private final PasswordEncoder passwordEncoder;
//
//    public Member signup(String username, String password, String nickname, String email) {
//        // Check for duplicate username
//        if (memberRepository.findByusername(username).isPresent()) {
//            throw new IllegalStateException("Username already exists");
//        }
//
//        // Check for duplicate email
//        if (memberRepository.findByEmail(email).isPresent()) {
//            throw new IllegalStateException("Email already exists");
//        }
//
//        Member member = Member
//                .builder()
//                .username(username)
//                .password(passwordEncoder.encode(password))
//                .nickname(nickname)
//                .email(email)
//                .build();
//
//        return memberRepository.save(member);
//    }
//
//    @Transactional
//    public Member whenSocialLogin(String providerTypeCode, String username, String nickname) {
//        Optional<Member> opMember = findByUsername(username);
//
//        if (opMember.isPresent()) return opMember.get();
//
//        // 소셜 로그인을 통한 가입 시 비번은 없다.
//        return signup(username, "", nickname, ""); // 최초 로그인 시 딱 한번 실행
//    }
//
//    private Optional<Member> findByUsername(String username) {
//        return memberRepository.findByusername(username);
//    }
//
//    public Member getMember(String username) {
//        Optional<Member> member = this.memberRepository.findByusername(username);
//        if (member.isPresent()) {
//            return member.get();
//        } else {
//            throw new DataNotFoundException("member not found");
//        }
//    }
//}

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Member signup(String username, String password, String nickname, String email) {
        // Check for duplicate username
        if (memberRepository.findByusername(username).isPresent()) {
            throw new IllegalStateException("Username already exists");
        }

        // Check for duplicate email
        if (memberRepository.findByEmail(email).isPresent()) {
            throw new IllegalStateException("Email already exists");
        }

        Member member = Member
                .builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .nickname(nickname)
                .email(email)
                .build();

        return memberRepository.save(member);
    }

    @Transactional
    public Member whenSocialLogin(String providerTypeCode, String username, String nickname, String email) {
        Optional<Member> opMember = findByUsername(username);

        if (opMember.isPresent()) return opMember.get();

        // 소셜 로그인을 통한 가입 시 비번은 없다.
        return signup(username, "", nickname, email); // 최초 로그인 시 딱 한번 실행
    }

    private Optional<Member> findByUsername(String username) {
        return memberRepository.findByusername(username);
    }

    public Member getMember(String username) {
        Optional<Member> member = this.memberRepository.findByusername(username);
        if (member.isPresent()) {
            return member.get();
        } else {
            throw new DataNotFoundException("Member not found");
        }
    }
}