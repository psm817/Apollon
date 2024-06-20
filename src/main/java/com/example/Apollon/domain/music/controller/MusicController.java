package com.example.Apollon.domain.music.controller;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/chart")
@RequiredArgsConstructor
public class MusicController {
    @Autowired
    private final MusicService musicService;
    private final MemberService memberService;
    private final StudioService studioService;
    private final CommentService commentService;
    private final PlaylistService playlistService;

    @GetMapping("/TOP100")
    public String getTop100Music(Model model, Principal principal) {
        List<Music> musicList = musicService.getTop100MusicByPlayCount();
        model.addAttribute("musicList", musicList);

        if (principal != null) {
            Studio studio = this.studioService.getStudioByMemberUsername(principal.getName());

            if (studio != null) {
                Integer studioActive = studio.getActive();

                // 회원별 플레이리스트가져오기(만들고 모델링해주고 푸터에서 넣어보자)
                Member member = memberService.getMember(principal.getName());
                Playlist playList = this.playlistService.getPlaylist(member.getId());

                model.addAttribute("studioActive", studioActive);
                model.addAttribute("playList", playList);
            }
        }

        return "chart/TOP100";
    }

    @GetMapping("/genreChart")
    public String getGenreChart(Model model) {
        List<Music> genreMusicList = musicService.getGenreChartByGenres();
        model.addAttribute("genreMusicList", genreMusicList);
        return "chart/genreChart";
    }

    // 곡 정보 상세보기
    @GetMapping("/music/detail/{id}")
    public String musicDetail(Model model, @PathVariable("id") Long id, Principal principal) {

        Music m = musicService.getMusic(id);
        Member member = this.memberService.getMember(m.getStudio().getMember().getUsername());
        Studio studio = this.studioService.getStudioByMemberUsername(member.getUsername());
        List<Comment> commentList = this.commentService.getListByStudio(studio);
        List<Music> musicList = this.musicService.getListByStudio(studio);

        model.addAttribute("music", m);
        model.addAttribute("studio", studio);
        model.addAttribute("commentList", commentList);
        model.addAttribute("musicList", musicList);


        if (principal != null) {
            if (studio != null) {
                // 회원별 플레이리스트가져오기(만들고 모델링해주고 푸터에서 넣어보자)
                Member member2 = memberService.getMember(principal.getName());
                Playlist playList = this.playlistService.getPlaylist(member2.getId());

                model.addAttribute("playList", playList);
            }
        }

        return "music/musicDetail";
    }

    // 검색 기능 구현
    @GetMapping("/search")
    public String searchMusic(@RequestParam("keyword") String keyword, Model model) {
        List<Music> searchResults = musicService.searchMusic(keyword);

        model.addAttribute("results", searchResults);
        return "search_list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/music/{id}/like")
    public String likeOrUnlikeMusic(Principal principal, @PathVariable("id") Long id) {
        Music music = this.musicService.getMusic(id);
        Member member = this.memberService.getMember(principal.getName());
        Studio studio = this.studioService.getStudioByMemberUsername(music.getStudio().getMember().getUsername());

//            if (music.getMusicLikers().contains(member)) {
//                this.musicService.unlikeMusic(music.id, member.id);
//            } else {
//                this.musicService.likeMusic(music.id, member.id);
//            }

        this.musicService.likeMusic(music, member);

        return "redirect:/studio/%s".formatted(studio.getMember().getUsername());
    }
}