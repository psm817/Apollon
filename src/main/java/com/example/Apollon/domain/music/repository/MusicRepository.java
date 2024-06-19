package com.example.Apollon.domain.music.repository;

import com.example.Apollon.domain.member.entity.Member;
import com.example.Apollon.domain.music.entity.Music;
import com.example.Apollon.domain.studio.entity.Studio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MusicRepository extends JpaRepository<Music, Long> {
    List<Music> findTop100ByOrderByMusicPlayCountDesc();

    List<Music> findByStudio(Studio studio);

    List<Music> findAllByOrderByCreateDateDesc();

    @Query("SELECT m FROM Music m ORDER BY SIZE(m.musicLikers) DESC")
    List<Music> findAllByOrderByMusicLikersDesc();
}