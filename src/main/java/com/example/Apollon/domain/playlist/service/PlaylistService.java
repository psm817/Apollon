package com.example.Apollon.domain.playlist.service;

import com.example.Apollon.domain.playlist.entity.Playlist;
import com.example.Apollon.domain.playlist.repository.PlaylistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DateTimeException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PlaylistService {
    private final PlaylistRepository playlistRepository;

    public Playlist getPlaylist(Long id) {
        Optional<Playlist> oc = this.playlistRepository.findById(id);

        if (oc.isEmpty()) throw new DateTimeException("플레이리스트를 찾을 수 없습니다.");

        return oc.get();
    }

    public void savePlaylist(Playlist playlist) {
        playlistRepository.save(playlist);
    }
}
