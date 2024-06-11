package com.example.Apollon.domain.playlist.service;

import com.example.Apollon.domain.comment.entity.Comment;
import com.example.Apollon.domain.comment.repository.CommentRepository;
import com.example.Apollon.domain.member.entity.Member;
import com.example.Apollon.domain.music.entity.Music;
import com.example.Apollon.domain.playlist.entity.Playlist;
import com.example.Apollon.domain.playlist.repository.PlaylistRepository;
import com.example.Apollon.domain.studio.entity.Studio;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DateTimeException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PlaylistService {
    private final PlaylistRepository playlistRepository;

    public Playlist getPlaylist(Long id) {
        Optional<Playlist> oc = this.playlistRepository.findById(id);

        if ( oc.isPresent() == false ) throw new DateTimeException("playlist not found");

        return oc.get();
    }

    public void savePlaylist(Playlist playlist) {
        playlistRepository.save(playlist);
    }
}
