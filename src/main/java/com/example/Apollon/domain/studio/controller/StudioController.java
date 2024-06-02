package com.example.Apollon.domain.studio.controller;

import com.example.Apollon.domain.member.entity.Member;
import com.example.Apollon.domain.member.service.MemberService;
import com.example.Apollon.domain.studio.entity.Studio;
import com.example.Apollon.domain.studio.service.StudioService;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/studio")
@RequiredArgsConstructor
public class StudioController {
    private final StudioService studioService;
    private final MemberService memberService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{username}")
    public String detail(Model model, @PathVariable("username") String username, Principal principal, @RequestParam(value= "kw", defaultValue = "") String kw ) {
        Studio studio = this.studioService.getStudioByMemberUsername(username);

        String loginedUsername = principal.getName();
        this.studioService.incrementVisit(username, loginedUsername);

        if (studio != null) {
            model.addAttribute("studio", studio);
            model.addAttribute("kw", kw);

            return "studio/studio_detail";
        }

        return "redirect:/";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/search")
    public String search(@RequestParam(value="kw", defaultValue = "") String kw) {

        return "redirect:/studio/%s".formatted(kw);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/like/{id}")
    public String studioLike(Principal principal, @PathVariable("id") Long id) {
        Studio studio = this.studioService.getStudio(id);
        Member member = this.memberService.getMember(principal.getName());

        this.studioService.like(studio, member);

        return "redirect:/studio/%s".formatted(studio.getMember().getUsername());
    }
}
