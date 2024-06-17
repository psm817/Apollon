package com.example.Apollon.domain.email;

import com.example.Apollon.domain.member.entity.Member;
import com.example.Apollon.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
@RequestMapping("/sendmail")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/password")
    public ResponseEntity<Void> sendPasswordMail(@RequestBody EmailRequestDto emailRequestDto) {
        // 이메일 수신자
        String email = emailRequestDto.getEmail();
        // 이메일 제목
        String subject = "[SAVIEW] 이메일 인증을 위한 인증 코드 발송";
        // 인증번호 생성
        String code = emailService.createCode();

        Optional<Member> optionalMember = memberRepository.findByUsernameAndEmail(emailRequestDto.getUsername(), emailRequestDto.getEmail());
        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();
            // 새로운 비밀번호를 암호화하여 저장
            member.setPassword(passwordEncoder.encode(code));
            memberRepository.save(member);

            // 이메일 본문 내용
            String body = "인증 코드는 다음과 같습니다: " + code;

            // 이메일 발송
            emailService.send(emailRequestDto.getEmail(), "입력하신 정보는 다음과 같습니다.", body);

            // 인증번호를 클라이언트로 반환
            return ResponseEntity.ok().build();
        } else {
            throw new RuntimeException("해당 아이디와 이메일을 가진 회원을 찾을 수 없습니다.");
        }
    }

    @GetMapping("/password")
    public ModelAndView getPasswordPage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("sendmail/password");
        return modelAndView;
    }
}