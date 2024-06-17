package com.example.Apollon.domain.member.controller;

import com.example.Apollon.domain.comment.service.CommentService;
import com.example.Apollon.domain.email.EmailService;
import com.example.Apollon.domain.member.entity.Member;
import com.example.Apollon.domain.member.repository.MemberRepository;
import com.example.Apollon.domain.member.service.MemberService;
import com.example.Apollon.domain.studio.entity.Studio;
import com.example.Apollon.domain.studio.service.StudioService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;
    private final EmailService emailService;
    private final StudioService studioService;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final CommentService commentService;

    @PreAuthorize("isAnonymous()")
    @GetMapping("/login")
    public String loginPage() {
        return "member/login";
    }

    @GetMapping("/signup")
    public String signupPage() {
        return "member/signup";
    }

    @GetMapping("/signup2")
    public String signup3Page() {
        return "member/signup2";
    }

    @PostMapping("/signup2")
    public String signup(@Valid SignForm signForm, BindingResult bindingResult, Model model,@RequestParam("profilePicture") MultipartFile profilePicture) {
        if (bindingResult.hasErrors()) {
            return "member/signup2";
        }
        if (signForm.getProfilePicture() == null || signForm.getProfilePicture().isEmpty()) {
            model.addAttribute("signupError", "프로필 사진을 첨부해주세요.");
            return "member/signup2";
        }


        try {
            String imageFileName = storeProfilePicture(signForm.profilePicture);
            // Member 객체에 이미지 경로를 저장합니다.
            Member member = memberService.signup(signForm.getUsername(), signForm.getPassword(), signForm.getNickname(), signForm.getEmail(), imageFileName);
            emailService.send(signForm.getEmail(), "서비스 가입을 환영합니다!", "회원가입을 축하드립니다^^~!");
            studioService.createOrUpdate(member, 0, 1);

        } catch (IllegalStateException e) {
            model.addAttribute("signupError", "이미 중복된 이메일 또는 아이디입니다");
            return "member/signup";
        }

        return "redirect:/member/login";
    }


    public String storeProfilePicture(MultipartFile profilePicture) {
        // 이미지 저장 디렉토리 경로


        String uploadDir = "C:\\work\\Apollon\\src\\main\\resources\\static\\images\\uploads";


        // 디렉토리가 존재하지 않으면 생성합니다.
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            try {
                Files.createDirectories(uploadPath);
            } catch (IOException e) {
                throw new IllegalStateException("Could not create upload directory", e);
            }
        }
        // 파일명 중복을 피하기 위해 임의의 파일명을 생성합니다.
        String fileName = UUID.randomUUID().toString(); // UUID로 파일명 생성
        String imageFileName = fileName + ".jpg"; // 파일 확장자 지정
        // 파일을 저장합니다.
        try {
            Path filePath = uploadPath.resolve(imageFileName);
            Files.copy(profilePicture.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new IllegalStateException("Could not store image file", e);
        }
        // 저장된 파일의 상대 경로를 반환합니다.
        return "/images/uploads/" + imageFileName;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/myPage")
    public String myPage(Principal principal, Model model) {
        Member member = this.memberService.getMember(principal.getName());

        model.addAttribute("member", member);

        return "member/myPage";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{username}")
    public String modify(SignForm signForm, @PathVariable("username") String username, Model model, Principal principal) {
        Member member = this.memberService.getMember(username);
        if (principal.getName().startsWith("KAKAO") || principal.getName().startsWith("NAVER") || principal.getName().startsWith("GOOGLE")) {
            // 소셜 로그인 사용자인 경우 modify2 페이지로 이동
            return "redirect:/member/modify2/" + username;
        } else {
            // 일반 가입 사용자인 경우 modify 페이지로 이동
            model.addAttribute("member", member);
            return "member/signup_modify";
        }
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify2/{username}")
    public String modify2(@PathVariable("username") String username, Model model, Principal principal) {
        Member member = this.memberService.getMember(username);
        if (principal.getName().startsWith("KAKAO") || principal.getName().startsWith("NAVER") || principal.getName().startsWith("GOOGLE")) {
            // 소셜 로그인 사용자인 경우 modify2 페이지로 이동
            model.addAttribute("member", member);
            return "member/signup_modify2";
        } else {
            // 일반 가입 사용자인 경우 modify 페이지로 이동
            return "redirect:/member/modify/" + username;
        }
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{username}")
    public String modify(@PathVariable("username") String username, @Valid SignForm signForm, BindingResult bindingResult, Model model, @RequestParam("profilePicture") MultipartFile profilePicture) {
        Member member = this.memberService.getMember(username);
        Studio studio = this.studioService.getStudioByMemberUsername(username);

        if (bindingResult.hasErrors()) {
            return "member/signup_modify";
        }

        String imageFileName = null;
        if (profilePicture != null && !profilePicture.isEmpty()) {
            imageFileName = storeProfilePicture(profilePicture);
        }

        this.memberService.modify(member, signForm.getUsername(), signForm.getPassword(), signForm.getNickname(), signForm.getEmail(), imageFileName);
        this.studioService.createOrUpdate(member, studio.getVisit(), studio.getActive());

        return "redirect:/member/logout";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify2/{username}")
    public String modify2(@PathVariable("username") String username, @RequestParam("nickname") String nickname, @RequestParam("profilePicture") MultipartFile profilePicture, Model model) {
        Member member = this.memberService.getMember(username);
        Studio studio = this.studioService.getStudioByMemberUsername(username);

        String imageFileName = null;
        if (profilePicture != null && !profilePicture.isEmpty()) {
            imageFileName = storeProfilePicture(profilePicture);
        }

        this.memberService.modify2(member, nickname, imageFileName);
        this.studioService.createOrUpdate(member, studio.getVisit(), studio.getActive());

        return "redirect:/member/logout";
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{username}")
    public String delete(Principal principal, @PathVariable("username") String username) {
        Member member = this.memberService.getMember(principal.getName());

        if (!member.getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }

        this.memberService.delete(member);

        return "redirect:/member/logout";
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

        // 이미지 업로드를 위한 필드 추가
        private MultipartFile profilePicture;
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