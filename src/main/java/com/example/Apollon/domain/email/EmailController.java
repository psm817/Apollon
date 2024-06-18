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
            String body = String.format(
                    "안녕하세요, <b>%s</b>님<br><br>" +
                            "Apollon 사이트에 오신 것을 진심으로 환영합니다! 저희는 음악을 사랑하는 모든 분들이 함께 모여 음악을 공유하고 즐기는 공간을 만들고자 합니다.<br><br>" +
                            "회원 가입해 주셔서 감사드립니다. Apollon에서는 여러분이 등록하신 음악이 많은 이들과 함께 소중한 경험이 될 것입니다. 우리는 음악이 인생의 일부분이며, 그 감동을 공유함으로써 새로운 모험을 시작할 수 있다고 믿습니다.<br><br>" +
                            "임시비밀번호는 다음과 같습니다: <b>%s</b><br><br>" +
                            "Apollon에 가입하신 여러분께서는 다음과 같은 특별한 혜택을 제공받으실 수 있습니다:<br><br>" +
                            "* 다양한 음악 장르와 아티스트들의 소식을 먼저 접하실 수 있는 기회<br>" +
                            "* 개인화된 추천 알림을 통해 새로운 음악을 탐색할 수 있는 기회<br>" +
                            "* Apollon 커뮤니티에서 참여하고 의견을 나눌 수 있는 기회<br><br>" +
                            "더불어, 회원님의 참여가 Apollon 커뮤니티를 더욱 풍요롭게 만들어갈 수 있습니다. 우리는 여러분이 행복하게 음악을 즐기며, Apollon이 여러분의 음악 여정을 지원하는데 도움이 될 것이라 확신합니다.<br><br>" +
                            "더 궁금하신 점이나 도움이 필요하신 경우 언제든지 저희에게 연락해 주세요. 저희의 서비스가 여러분께 많은 즐거움을 줄 수 있기를 바랍니다.<br><br>" +
                            "감사합니다,<br><br>" +
                            "Apollon 팀 드림.",
                    member.getUsername(), code
            );
            // 이메일 발송
            emailService.send(emailRequestDto.getEmail(), "Apollon 임시비밀번호 발급", body);

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