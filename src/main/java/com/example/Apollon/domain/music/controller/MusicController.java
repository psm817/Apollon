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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.Arrays;
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
        List<Music> top100MusicByLikers = this.musicService.getTop100MusicByLikers();
        model.addAttribute("top100MusicByLikers", top100MusicByLikers);

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
    public String getGenreChart(Model model, Principal principal) {
        List<Music> balladList = musicService.getMusicListByGenresContaining("Ballad");
        List<Music> danceList = musicService.getMusicListByGenresContaining("Dance");
        List<Music> rapAndHipHopList = musicService.getMusicListByGenresContaining("Rap/HipHop");
        List<Music> rAndBAndSoulList = musicService.getMusicListByGenresContaining("R&B/Soul");
        List<Music> rockAndMetalList = musicService.getMusicListByGenresContaining("Rock/Metal");
        List<Music> electronicaList = musicService.getMusicListByGenresContaining("Electronica");
        List<Music> ostList = musicService.getMusicListByGenresContaining("OST");
        List<Music> jpopList = musicService.getMusicListByGenresContaining("JPOP");
        List<Music> indieList = musicService.getMusicListByGenresContaining("indie");
        List<Music> etcList = musicService.getMusicListByGenresContaining("etc");

        model.addAttribute("balladList", balladList);
        model.addAttribute("etcList", etcList);
        model.addAttribute("danceList", danceList);
        model.addAttribute("rapAndHipHopList", rapAndHipHopList);
        model.addAttribute("rAndBAndSoulList", rAndBAndSoulList);
        model.addAttribute("rockAndMetalList", rockAndMetalList);
        model.addAttribute("electronicaList", electronicaList);
        model.addAttribute("ostList", ostList);
        model.addAttribute("jpopList", jpopList);
        model.addAttribute("indieList", indieList);

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

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/music/{id}/like_top100")
    public String likeOrUnlikeMusicOnTop100(Principal principal, @PathVariable("id") Long id) {
        if(principal != null && principal.getName() != null) {
            Music music = this.musicService.getMusic(id);
            Member member = this.memberService.getMember(principal.getName());
            this.musicService.likeMusic(music, member);

            return "redirect:/chart/TOP100";
        }
        else {
            return "redirect:/member/login";
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/music/{id}/like_genre")
    public String likeOrUnlikeMusicOnGenre(Principal principal, @PathVariable("id") Long id) {
        if(principal != null && principal.getName() != null) {
            Music music = this.musicService.getMusic(id);
            Member member = this.memberService.getMember(principal.getName());
            this.musicService.likeMusic(music, member);

            return "redirect:/chart/genreChart";
        }
        else {
            return "redirect:/member/login";
        }
    }
}