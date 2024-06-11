package com.example.Apollon.domain.music.repository;

import com.example.Apollon.domain.music.entity.Music;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MusicRepository extends JpaRepository<Music, Long> {
    List<Music> findTop100ByOrderByMusicPlayCountDesc();
}