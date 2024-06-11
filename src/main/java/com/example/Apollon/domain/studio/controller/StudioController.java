    package com.example.Apollon.domain.studio.controller;

    import com.example.Apollon.domain.comment.entity.Comment;
    import com.example.Apollon.domain.comment.form.CommentForm;
    import com.example.Apollon.domain.comment.service.CommentService;
    import com.example.Apollon.domain.member.entity.Member;
    import com.example.Apollon.domain.member.service.MemberService;
    import com.example.Apollon.domain.music.service.MusicService;
    import com.example.Apollon.domain.studio.entity.Studio;
    import com.example.Apollon.domain.studio.service.StudioService;
    import lombok.RequiredArgsConstructor;
    import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
    import org.springframework.security.access.prepost.PreAuthorize;
    import org.springframework.security.core.userdetails.UserDetails;
    import org.springframework.stereotype.Controller;
    import org.springframework.ui.Model;
    import org.springframework.web.bind.annotation.*;
    import org.springframework.web.multipart.MultipartFile;

    import java.security.Principal;
    import java.util.List;

    @Controller
    @RequestMapping("/studio")
    @RequiredArgsConstructor
    public class StudioController {
        private final StudioService studioService;
        private final MemberService memberService;
        private final MusicService musicService;
        private final CommentService commentService;

        @PreAuthorize("isAuthenticated()")
        @GetMapping("/{username}")
        public String detail(Model model,
                             @PathVariable("username") String username,
                             Principal principal,
                             @RequestParam(value= "kw", defaultValue = "") String kw
        ) {
            Studio studio = this.studioService.getStudioByMemberUsername(username);

            String loginedUsername = principal.getName();
            this.studioService.incrementVisit(username, loginedUsername);

            List<Comment> commentList = this.commentService.getListByStudio(studio);

            if (studio != null) {
                model.addAttribute("studio", studio);
                model.addAttribute("kw", kw);
                model.addAttribute("commentList", commentList);

                if(username.equals(loginedUsername) && studio.getActive() == 0) {
                    return "redirect:/";
                }

                return "studio/studio_detail";
            }

            return "studio/studio_detail";
        }

        @PreAuthorize("isAuthenticated()")
        @PostMapping("/search")
        public String search(Model model, Principal principal, @RequestParam(value="kw", defaultValue = "") String kw) {
            String loginedUsername = principal.getName();

            // 관리자일 때는 활성화 관계없이 모두 열람 가능
            if(loginedUsername.equals("admin")) {
                if (!kw.isEmpty()) {
                    Studio studio = this.studioService.getStudioByMemberUsername(kw);

                    if (studio == null || !kw.equals(studio.getMember().getUsername())) {
                        Studio loginedUserStudio = this.studioService.getStudioByMemberUsername(loginedUsername);

                        model.addAttribute("studio", loginedUserStudio);

                        return "redirect:/studio/%s".formatted(loginedUsername);
                    }
                    else if(studio.getActive() == 0) {
                        return "redirect:/studio/%s".formatted(kw);
                    }
                }
            }

            // 일반 사용자는 차단되어있는 스튜디오는 접근 불가
            else {
                if (!kw.isEmpty()) {
                    Studio studio = this.studioService.getStudioByMemberUsername(kw);

                    if (studio == null || !kw.equals(studio.getMember().getUsername()) || studio.getActive() == 0) {
                        Studio loginedUserStudio = this.studioService.getStudioByMemberUsername(loginedUsername);

                        model.addAttribute("studio", loginedUserStudio);

                        return "redirect:/studio/%s".formatted(loginedUsername);
                    }
                }
            }

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

        @PreAuthorize("isAuthenticated()")
        @GetMapping("/{username}/changeActive")
        public String changeActive(Model model, Principal principal, @PathVariable("username") String username) {
            Studio studio = this.studioService.getStudioByMemberUsername(username);

            model.addAttribute("studio", studio);

            if (studio != null && studio.getActive() == 1) {
                this.studioService.changeInActive(studio, 0);
            }

            else if(studio != null && studio.getActive() == 0) {
                this.studioService.changeActive(studio, 1);
            }

            return "redirect:/studio/%s".formatted(studio.getMember().getUsername());
        }

        @PreAuthorize("isAuthenticated()")
        @GetMapping("/{username}/upload")
        public String upload(Model model, Principal principal, @PathVariable("username") String username) {
            Studio studio = this.studioService.getStudioByMemberUsername(username);

            model.addAttribute("studio", studio);

            return "music/upload_form";
        }

        @PreAuthorize("isAuthenticated()")
        @PostMapping("/{username}/upload")
        public String upload(@RequestParam("title") String title,
                             @RequestParam("content") String content,
                             @PathVariable("username") String memberName,
                             @RequestParam("thumbnail") MultipartFile thumbnail,
                             @RequestParam("musicFile") MultipartFile musicFile,
                             @RequestParam("genres") List<String> genres,
                             @PathVariable("username") String username) {
            Studio studio = this.studioService.getStudioByMemberUsername(username);
            musicService.upload(title, content, memberName, thumbnail, musicFile, genres);

            return "redirect:/studio/%s".formatted(studio.getMember().getUsername());
        }
    }
