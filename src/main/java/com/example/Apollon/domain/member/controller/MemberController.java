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
            model.addAttribute("profilePictureError", "(프로필 사진을 첨부해주세요.)");
            return "member/signup2";
        }


        try {
            String imageFileName = storeProfilePicture(signForm.profilePicture);
            // Member 객체에 이미지 경로를 저장합니다.
            Member member = memberService.signup(signForm.getUsername(), signForm.getPassword(), signForm.getNickname(), signForm.getEmail(), imageFileName);
            String textContent = String.format(
                    "안녕하세요, <b>%s</b>님<br><br>"+
                            "Apollon 사이트에 오신 것을 진심으로 환영합니다! 저희는 음악을 사랑하는 모든 분들이 함께 모여 음악을 공유하고 즐기는 공간을 만들고자 합니다.<br><br>" +
                            "회원 가입해 주셔서 감사드립니다. Apollon에서는 여러분이 등록하신 음악이 많은 이들과 함께 소중한 경험이 될 것입니다. 우리는 음악이 인생의 일부분이며, 그 감동을 공유함으로써 새로운 모험을 시작할 수 있다고 믿습니다.<br><br>" +
                            "Apollon에 가입하신 여러분께서는 다음과 같은 특별한 혜택을 제공받으실 수 있습니다:<br><br>" +
                            "* 다양한 음악 장르와 아티스트들의 소식을 먼저 접하실 수 있는 기회<br>" +
                            "* 개인화된 추천 알림을 통해 새로운 음악을 탐색할 수 있는 기회<br>" +
                            "* Apollon 커뮤니티에서 참여하고 의견을 나눌 수 있는 기회<br><br>" +
                            "더불어, 회원님의 참여가 Apollon 커뮤니티를 더욱 풍요롭게 만들어갈 수 있습니다. 우리는 여러분이 행복하게 음악을 즐기며, Apollon이 여러분의 음악 여정을 지원하는데 도움이 될 것이라 확신합니다.<br><br>" +
                            "더 궁금하신 점이나 도움이 필요하신 경우 언제든지 저희에게 연락해 주세요. 저희의 서비스가 여러분께 많은 즐거움을 줄 수 있기를 바랍니다.<br><br>" +
                            "감사합니다,<br><br>" +
                            "Apollon 팀 드림.",
                    signForm.getUsername()
            );

            emailService.send(signForm.getEmail(), "Apollon 사이트에 오신 것을 환영합니다!", textContent);
            studioService.createOrUpdate(member, 0, 1);


        } catch (IllegalStateException e) {
            model.addAttribute("signupError", "이미 중복된 이메일 또는 아이디입니다");
            return "member/signup";
        }

        return "redirect:/member/login";
    }


    public String storeProfilePicture(MultipartFile profilePicture) {
        // 이미지 저장 디렉토리 경로


        String uploadDir = "C:\\Users\\user\\IdeaProjects\\Apollon\\src\\main\\resources\\static\\images\\uploads";

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