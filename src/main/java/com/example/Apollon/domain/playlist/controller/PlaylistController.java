package com.example.Apollon.domain.playlist.controller;

import com.example.Apollon.domain.member.entity.Member;
import com.example.Apollon.domain.member.service.MemberService;
import com.example.Apollon.domain.music.entity.Music;
import com.example.Apollon.domain.music.service.MusicService;
import com.example.Apollon.domain.playlist.entity.Playlist;
import com.example.Apollon.domain.playlist.service.PlaylistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@SuppressWarnings("ALL")
@RestController
@RequiredArgsConstructor
public class PlaylistController {
    private final MusicService musicService;
    private final MemberService memberService;
    private final PlaylistService playlistService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/playlist/import/{id}")
    public ResponseEntity<String> addSongToPlaylist(@PathVariable("id") Long id, Principal principal) {
        String username = principal.getName();
        // 회원 가져오기
        Member member = memberService.getMember(username);
        Long memberId = member.getId();
        // 재생목록 가져오거나 생성
        Playlist playlist = playlistService.getPlaylist(memberId);
        if (playlist == null) {
            playlist = new Playlist();
            playlist.setMember(member);
        }
        // 음악 가져오기
        Music impMusic = musicService.getMusicById(id);
        if (impMusic == null) {
            return ResponseEntity.badRequest().body("해당 ID의 음악을 찾을 수 없습니다.");
        }
        // 재생목록에 음악 추가
        playlist.addMusic(impMusic);
        // 재생목록 저장
        playlistService.savePlaylist(playlist);
        return ResponseEntity.ok("재생목록에 음악 추가 완료");
    }

    @GetMapping("/play")
    public String playPlaylist(@RequestParam Long playlistId) {
        Playlist playlist = playlistService.getPlaylist(playlistId);

        musicService.playPlaylist(playlist);

        return "redirect:/playlist/view?playlistId=" + playlistId;
    }
}


