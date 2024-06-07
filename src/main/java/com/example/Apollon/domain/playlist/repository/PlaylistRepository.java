package com.example.Apollon.domain.playlist.repository;

import com.example.Apollon.domain.music.entity.Music;
import com.example.Apollon.domain.playlist.entity.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
}
