package com.example.Apollon.domain.home.controller;

import com.example.Apollon.domain.studio.entity.Studio;
import com.example.Apollon.domain.studio.service.StudioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final StudioService studioService;

    @GetMapping("/")
    public String goMain(Model model, Principal principal) {
        // 스튜디오 진입 시 로그인된 회원 스튜디외의 차단 상태 판단을 위해 작성
        if(principal != null) {
            Studio studio = this.studioService.getStudioByMemberUsername(principal.getName());

            if(studio != null) {
                Integer studioActive = studio.getActive();

                model.addAttribute("studioActive", studioActive);
            }
        }

        return "mainPage";
    }
}
