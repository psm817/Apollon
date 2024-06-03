package com.example.Apollon.domain.music.repository;

import com.example.Apollon.domain.music.entity.Music;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MusicRepository extends JpaRepository<Music, Long> {
}