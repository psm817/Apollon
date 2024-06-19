package com.example.Apollon.domain.playlist.controller;

import com.example.Apollon.domain.member.entity.Member;
import com.example.Apollon.domain.member.service.MemberService;
import com.example.Apollon.domain.music.entity.Music;
import com.example.Apollon.domain.music.service.MusicService;
import com.example.Apollon.domain.playlist.entity.Playlist;
import com.example.Apollon.domain.playlist.service.PlaylistService;
import com.example.Apollon.domain.studio.entity.Studio;
import com.example.Apollon.domain.studio.service.StudioService;
import com.example.Apollon.global.DataNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.security.Principal;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class PlaylistController {
    private final MusicService musicService;
    private final MemberService memberService;
    private final PlaylistService playlistService;
    private final StudioService studioService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/playlist/import/{id}")
    public String addSongToPlaylist(@PathVariable("id") Long id, Principal principal) {
        String username = principal.getName();
        // 회원 정보 가져오기
        Member member = memberService.getMember(username);
        long memberId = member.getId();

        // 재생목록 가져오거나 생성
        Playlist playlist = playlistService.getPlaylist(memberId);

        if (playlist == null) {
            // 재생목록이 없으면 생성
            playlist = Playlist.builder()
                    .member(member)
                    .createDate(LocalDateTime.now())
                    .modifyDate(LocalDateTime.now())
                    .build();
        }

        // 노래 추가 예시
        Music music = musicService.getMusicById(id); // id를 사용해 음악 객체 가져오기
        playlist.addMusic(music);

        // 재생목록 저장
        playlistService.savePlaylist(playlist);

        return "redirect:/";
    }

    @GetMapping("/play")
    public String playPlaylist(@RequestParam Long playlistId) {
        Playlist playlist = playlistService.getPlaylist(playlistId);

        musicService.playPlaylist((Playlist) playlist);

        return "redirect:/playlist/view?playlistId=" + playlistId;
    }
}


