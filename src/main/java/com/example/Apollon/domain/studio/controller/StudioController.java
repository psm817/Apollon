package com.example.Apollon.domain.studio.controller;

import com.example.Apollon.domain.member.entity.Member;
import com.example.Apollon.domain.member.service.MemberService;
import com.example.Apollon.domain.studio.entity.Studio;
import com.example.Apollon.domain.studio.service.StudioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/studio")
@RequiredArgsConstructor
public class StudioController {
    private final StudioService studioService;
    private final MemberService memberService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public String detail(Model model, @PathVariable("id") Long id, Principal principal) {
        Studio studio = this.studioService.getStudio(id);

        if (studio != null) {
            model.addAttribute("studio", studio);
            return "studio_detail";
        }

        return "redirect:/";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/like/{id}")
    public String studioLike(Principal principal, @PathVariable("id") Long id) {
        Studio studio = this.studioService.getStudio(id);
        Member member = this.memberService.getMember(principal.getName());

        this.studioService.like(studio, member);

        return "redirect:/studio/%d".formatted(id);
    }
}
