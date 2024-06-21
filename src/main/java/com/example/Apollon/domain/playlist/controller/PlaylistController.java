
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.time.LocalDateTime;

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

        return "redirect:/chart/TOP100";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/playlist/delete/{playListId}/{musicId}")
    public String deletePlayListMusic(Principal principal, @PathVariable("playListId") Long playListId, @PathVariable("musicId") Long musicId) {
        Member member = this.memberService.getMember(principal.getName());
        Playlist playlist = this.playlistService.getPlaylist(playListId);

        if (!playlist.getMember().equals(member)) {
            throw new IllegalArgumentException("회원님의 플레이리스트가 아닙니다.");
        }

        Music music = this.musicService.getMusic(musicId);

        if (!playlist.getMusicPlayList().contains(music)) {
            throw new IllegalArgumentException("플레이리스트에 존재하지 않는 음악입니다.");
        }

        playlist.getMusicPlayList().remove(music);
        this.playlistService.savePlaylist(playlist); // 플레이리스트 갱신을 저장

        return "redirect:/";
    }

    @GetMapping("/play")
    public String playPlaylist(@RequestParam Long playlistId) {
        Playlist playlist = playlistService.getPlaylist(playlistId);

        musicService.playPlaylist((Playlist) playlist);

        return "redirect:/playlist/view?playlistId=" + playlistId;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/play/{id}")
    public String likeOrUnlikeMusic(Principal principal, @PathVariable("id") Long id) {
        Music music = this.musicService.getMusic(id);
        Member member = this.memberService.getMember(principal.getName());

        this.musicService.playCountMusic(music, member);

        return "redirect:/";
    }
}
