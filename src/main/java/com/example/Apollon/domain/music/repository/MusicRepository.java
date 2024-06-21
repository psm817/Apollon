package com.example.Apollon.domain.music.repository;

import com.example.Apollon.domain.music.entity.Music;
import com.example.Apollon.domain.studio.entity.Studio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MusicRepository extends JpaRepository<Music, Long> {

    @Query("SELECT m FROM Music m ORDER BY SIZE(m.musicPlayCounts) DESC")
    List<Music> findTop100ByOrderByMusicPlayCountDesc();

    List<Music> findByStudio(Studio studio);

    List<Music> findAllByOrderByCreateDateDesc();

    @Query("SELECT m FROM Music m ORDER BY SIZE(m.musicLikers) DESC")
    List<Music> findAllByOrderByMusicLikersDesc();

    List<Music> findByMusicTitleContainingIgnoreCase(String keyword);

    @Query("SELECT m FROM Music m JOIN m.genres g WHERE g LIKE %:genres%")
    List<Music> findByGenresContaining(@Param("genres")String genres);
}