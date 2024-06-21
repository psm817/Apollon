    package com.example.Apollon.domain.studio.controller;

    import com.example.Apollon.domain.comment.entity.Comment;
    import com.example.Apollon.domain.comment.service.CommentService;
    import com.example.Apollon.domain.member.entity.Member;
    import com.example.Apollon.domain.member.service.MemberService;
    import com.example.Apollon.domain.music.entity.Music;
    import com.example.Apollon.domain.music.service.MusicService;
    import com.example.Apollon.domain.playlist.entity.Playlist;
    import com.example.Apollon.domain.playlist.service.PlaylistService;
    import com.example.Apollon.domain.studio.entity.Studio;
    import com.example.Apollon.domain.studio.service.StudioService;
    import lombok.RequiredArgsConstructor;
    import org.springframework.security.access.prepost.PreAuthorize;
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
        private final PlaylistService playlistService;

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
            List<Music> musicList = this.musicService.getListByStudio(studio);

            if (studio != null) {
                model.addAttribute("studio", studio);
                model.addAttribute("kw", kw);
                model.addAttribute("commentList", commentList);
                model.addAttribute("musicList", musicList);

                Member member = memberService.getMember(principal.getName());
                Playlist playList = this.playlistService.getPlaylist(member.getId());

                model.addAttribute("playList", playList);

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

        // 음악 신규 등록
        @PreAuthorize("isAuthenticated()")
        @GetMapping("/{username}/upload")
        public String upload(Model model, Principal principal, @PathVariable("username") String username) {
            Studio studio = this.studioService.getStudioByMemberUsername(username);

            model.addAttribute("studio", studio);


            if (studio != null) {
                // 회원별 플레이리스트가져오기(만들고 모델링해주고 푸터에서 넣어보자)
                Member member = memberService.getMember(principal.getName());
                Playlist playList = this.playlistService.getPlaylist(member.getId());

                model.addAttribute("playList", playList);
            }

            return "music/upload_form";
        }

        @PreAuthorize("isAuthenticated()")
        @PostMapping("/{username}/upload")
        public String upload(@RequestParam("title") String title,
                             @RequestParam("content") String content,
                             @RequestParam("thumbnail") MultipartFile thumbnail,
                             @RequestParam("musicFile") MultipartFile musicFile,
                             @RequestParam("genres") List<String> genres,
                             @PathVariable("username") String username) {

            Studio studio = this.studioService.getStudioByMemberUsername(username);
            musicService.upload(title, content, studio, thumbnail, musicFile, genres);

            return "redirect:/studio/%s".formatted(studio.getMember().getUsername());
        }

        // 음악 수정
        @PreAuthorize("isAuthenticated()")
        @GetMapping("/{username}/reUpload/{id}")
        public String modifyMusic(Model model, Principal principal, @PathVariable("username") String username, @PathVariable("id") Long id) {
            Studio studio = this.studioService.getStudioByMemberUsername(username);
            Music music = this.musicService.getMusic(id);

            model.addAttribute("studio", studio);
            model.addAttribute("music", music);

            if (studio != null) {
                // 회원별 플레이리스트가져오기(만들고 모델링해주고 푸터에서 넣어보자)
                Member member = memberService.getMember(principal.getName());
                Playlist playList = this.playlistService.getPlaylist(member.getId());

                model.addAttribute("playList", playList);
            }

            return "music/modify_form";
        }

        @PreAuthorize("isAuthenticated()")
        @PostMapping("/{username}/reUpload/{id}")
        public String modifyMusic(@RequestParam("content") String content,
                                  @RequestParam("thumbnail") MultipartFile thumbnail,
                                  @RequestParam("genres") List<String> genres,
                                  @PathVariable("username") String username,
                                  @PathVariable("id") Long id) {

            Studio studio = this.studioService.getStudioByMemberUsername(username);
            Music music = this.musicService.getMusic(id);

            musicService.reUpload(music, content, thumbnail, genres);

            return "redirect:/studio/%s".formatted(studio.getMember().getUsername());
        }

        // 음악삭제
        @PreAuthorize("isAuthenticated()")
        @PostMapping("/{username}/delete")
        public String deleteMusic(@PathVariable("username") String username,
                                    @RequestParam("musicIds") List<Long> musicIds,
                                    Principal principal) {
            String loginedUsername = principal.getName();

            for (Long musicId : musicIds) {
                Music music = this.musicService.getMusic(musicId);

                if (music != null) {
                    this.musicService.delete(music);
                }
            }

            Studio studio = this.studioService.getStudioByMemberUsername(username);

            return "redirect:/studio/%s".formatted(studio.getMember().getUsername());
        }
    }
