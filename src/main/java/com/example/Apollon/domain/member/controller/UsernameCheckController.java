package com.example.Apollon.domain.member.controller;

import com.example.Apollon.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class UsernameCheckController {
    private final MemberService memberService;

    @GetMapping("/member/check-username")
    public ResponseEntity<?> checkUsername(@RequestParam("username") String username) {
        boolean exists = memberService.usernameExists(username);
        return ResponseEntity.ok().body("{\"exists\": " + exists + "}");
    }
}