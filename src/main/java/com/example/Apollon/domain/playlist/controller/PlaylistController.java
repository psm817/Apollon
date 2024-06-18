package com.example.Apollon.domain.playlist.controller;

import com.example.Apollon.domain.member.entity.Member;
import com.example.Apollon.domain.member.service.MemberService;
import com.example.Apollon.domain.music.entity.Music;
import com.example.Apollon.domain.music.service.MusicService;
import com.example.Apollon.domain.playlist.entity.Playlist;
import com.example.Apollon.domain.playlist.service.PlaylistService;
import com.example.Apollon.domain.studio.entity.Studio;
import com.example.Apollon.domain.studio.service.StudioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

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
        Long memberId = member.getId();

        // 가져온 음악 번호의 스튜디오
        Music m = musicService.getMusic(id);
        Member memberS = this.memberService.getMember(m.getStudio().getMember().getUsername());
        Studio studio = this.studioService.getStudioByMemberUsername(memberS.getUsername());

        // 재생목록 가져오거나 생성
        Playlist playlist = playlistService.getPlaylist(memberId);

        // 음악 정보 가져오기
        Music impMusic = musicService.getMusicById(id);
        if (impMusic == null) {
            return "해당 ID의 음악을 찾을 수 없습니다.";
        }
        impMusic.getThumbnailImg();
        impMusic.getMusicTitle();
        impMusic.getStudio();

        // 재생목록에 음악 추가
        playlist.addMusic(impMusic);

        // 재생목록 저장
        playlistService.savePlaylist(playlist);

        return "redirect:/";
    }

    @GetMapping("/play")
    public String playPlaylist(@RequestParam Long playlistId) {
        Playlist playlist = playlistService.getPlaylist(playlistId);

        musicService.playPlaylist(playlist);

        return "redirect:/playlist/view?playlistId=" + playlistId;
    }
}


