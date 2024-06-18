package com.example.Apollon.domain.playlist.service;

import com.example.Apollon.domain.member.entity.Member;
import com.example.Apollon.domain.member.repository.MemberRepository;
import com.example.Apollon.domain.music.entity.Music;
import com.example.Apollon.domain.playlist.entity.Playlist;
import com.example.Apollon.domain.playlist.repository.PlaylistRepository;
import com.example.Apollon.domain.studio.entity.Studio;
import com.example.Apollon.global.DataNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PlaylistService {
    private final PlaylistRepository playlistRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Playlist PCreateOrUpdate(Member member) {
        Optional<Playlist> existingPlaylist = playlistRepository.findByMember(member);

        Playlist playlist;

        if(existingPlaylist.isPresent()) {
            playlist = existingPlaylist.get();
            playlist.setModifyDate(LocalDateTime.now());
        }
        else {
            playlist = Playlist.builder()
                    .member(member)
                    .createDate(LocalDateTime.now())
                    .build();
        }

        return this.playlistRepository.save(playlist);
    }

    public Playlist getPlaylist(long memberId) {
        Optional<Playlist> playlist = this.playlistRepository.findById(memberId);

        if(playlist.isEmpty()) {
            throw new DataNotFoundException("플레이 리스트를 찾을 수 없음");
        }

        return playlist.get();
    }

    public void savePlaylist(Playlist playlist) {
        playlistRepository.save(playlist);
    }
}
