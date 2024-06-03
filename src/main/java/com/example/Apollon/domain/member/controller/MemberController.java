package com.example.Apollon.domain.member.controller;

import com.example.Apollon.domain.email.EmailService;
import com.example.Apollon.domain.member.service.MemberService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;
    private final EmailService emailService;

    @PreAuthorize("isAnonymous()")
    @GetMapping("/login")
    public String loginPage() {
        return "member/login";
    }

    @GetMapping("/signup")
    public String signupPage() {
        return "member/signup";
    }

    @PostMapping("/signup2")
    public String signup(@Valid SignForm signForm, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "member/signup2";
        }

        try {
            memberService.signup(signForm.getUsername(), signForm.getPassword(), signForm.getNickname(), signForm.getEmail());
            emailService.send(signForm.getEmail(), "서비스 가입을 환영합니다!", "회원가입을 축하드립니다^^~!");
        } catch (IllegalStateException e) {
            model.addAttribute("signupError", "이미 중복된 이메일 또는 아이디입니다");
            return "member/signup";
        }

        return "redirect:/member/login";
    }

    @Getter
    @Setter
    @ToString
    public static class SignForm {
        @NotBlank
        @Length(min = 3)
        private String username;

        @NotBlank
        @Length(min = 4)
        private String password;

        @NotBlank
        @Length(min = 4)
        private String password_confirm;

        @NotBlank
        @Length(min = 3)
        private String nickname;

        @NotBlank
        @Length(min = 4)
        private String email;
    }
}