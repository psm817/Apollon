    package com.example.Apollon.domain.music.controller;

    import com.example.Apollon.domain.member.entity.Member;
    import com.example.Apollon.domain.member.service.MemberService;
    import com.example.Apollon.domain.music.entity.Music;
    import com.example.Apollon.domain.music.service.MusicService;
    import jakarta.servlet.http.HttpServletRequest;
    import lombok.RequiredArgsConstructor;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.security.access.prepost.PreAuthorize;
    import org.springframework.stereotype.Controller;
    import org.springframework.ui.Model;
    import org.springframework.web.bind.annotation.GetMapping;
    import org.springframework.web.bind.annotation.PathVariable;
    import org.springframework.web.bind.annotation.RequestMapping;

    import java.security.Principal;
    import java.util.ArrayList;
    import java.util.List;

    @Controller
    @RequestMapping("/chart")
    @RequiredArgsConstructor
    public class MusicController {
        @Autowired
        private final MusicService musicService;
        private final MemberService memberService;

        @GetMapping("/TOP100")
        public String getTop100Music(Model model) {
            List<Music> musicList = musicService.getTop100MusicByPlayCount();
            model.addAttribute("musicList", musicList);
            return "chart/TOP100";
        }


        // 곡 정보 상세보기
        @GetMapping("/musicDetail/{id}")
        public String musicDetail(Model model, @PathVariable("id") Long id) {

            Music m = musicService.getMusic(id);

            model.addAttribute("music", m);

            return "music/musicDetail";
        }

        @PreAuthorize("isAuthenticated()")
        @GetMapping("/music/{id}/like")
        public String likeOrUnlikeMusic(Principal principal, @PathVariable("id") Long id, HttpServletRequest request) {
            Music music = this.musicService.getMusic(id);
            Member member = this.memberService.getMember(principal.getName());

            if (music.getLikedByMembers().contains(member)) {
                this.musicService.unlikeMusic(music.id, member.id);
            } else {
                this.musicService.likeMusic(music.id, member.id);
            }

            String referer = request.getHeader("Referer");
            return "redirect:" + (referer != null ? referer : "/");
        }
}