package com.example.Apollon.domain.email;

import com.example.Apollon.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/sendmail")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/password")
    public ResponseEntity<Void> sendPasswordMail(@RequestBody EmailResponseDto emailResponseDto) {
        // 이메일 수신자
        String to = emailResponseDto.getEmail();
        // 이메일 제목
        String subject = "[SAVIEW] 이메일 인증을 위한 인증 코드 발송";
        // 인증번호 생성
        String code = emailService.createCode();
        // 이메일 본문 내용
        String body = "인증 코드는 다음과 같습니다: " + code;

        // 이메일 발송
        emailService.send(emailResponseDto.getEmail(), "입력하신 정보는 다음과 같습니다.", code);

        // 인증번호를 클라이언트로 반환
        return ResponseEntity.ok().build();
    }

    @GetMapping("/password")
    public ModelAndView getPasswordPage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("sendmail/password");
        return modelAndView;
    }
}
