package com.example.Apollon.domain.member.controller;

import com.example.Apollon.domain.email.EmailService;
import com.example.Apollon.domain.member.entity.Member;
import com.example.Apollon.domain.member.repository.MemberRepository;
import com.example.Apollon.domain.member.service.MemberService;
import com.example.Apollon.domain.studio.service.StudioService;
import jakarta.persistence.Id;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;
    private final EmailService emailService;
    private final StudioService studioService;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

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
            Member member = memberService.signup(signForm.getUsername(), signForm.getPassword(), signForm.getNickname(), signForm.getEmail());
            emailService.send(signForm.getEmail(), "서비스 가입을 환영합니다!", "회원가입을 축하드립니다^^~!");
            studioService.createOrUpdate(member, 0, 1);
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

    //마이페이지-비밀번호 재설정
    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam("new-password") String newPassword,
                                @RequestParam("confirm-password") String confirmPassword,
                                Principal principal,
                                Model model) {
        // 새로운 비밀번호와 비밀번호 확인이 일치하는지 확인
        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "새로운 비밀번호와 비밀번호 확인이 일치하지 않습니다.");
            return "/";
        }

        // 현재 로그인한 사용자의 ID 가져오기
        String userId = principal.getName(); // 사용자 ID를 이용한다고 가정

        // 해당 사용자 ID를 가진 회원을 조회
        Optional<Member> optionalMember = memberRepository.findById(Long.parseLong(userId));
        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();
            // 새로운 비밀번호를 암호화하여 저장
            member.setPassword(passwordEncoder.encode(newPassword));
            memberRepository.save(member);
            return "/member/login"; // 비밀번호 재설정 후 로그인 페이지로 이동
        } else {
            throw new RuntimeException("해당 사용자를 찾을 수 없습니다.");
        }
    }
    @GetMapping("/reset-password")
    public String resetPasswordPage() {
        return "/member/reset-password";
    }
}