package com.example.Apollon.domain.music.service;

import com.example.Apollon.domain.music.entity.Music;
import com.example.Apollon.domain.music.repository.MusicRepository;
import com.example.Apollon.domain.post.entity.Post;
import com.example.Apollon.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MusicService {

    private final MusicRepository musicRepository;

    // TOP100 가져옴(전체 음악중 재생한 count가 많은 순으로 정렬)
    public List<Music> getTOP100List() {
        List<Music> allMusic = musicRepository.findAll();
        allMusic.sort(Comparator.comparingLong(Music::getMusicPlayCount).reversed());
        return allMusic;
    }

    public void musicUpload(String musicTitle, String musicContent, String[] genres, String uploadStudio) {
        Music music = Music.builder()
                .musicTitle(musicTitle)
                .musicContent(musicContent)
                .genres(genres)
                .uploadStudio(uploadStudio)
                .createDate(LocalDateTime.now())
                .build();
        musicRepository.save(music);
    }


    // 원하는 음악을 가져오는데 음악 없으면 찾을 수 없다고 표시
    public Music getMusic(Long id) {
        Optional<Music> op = musicRepository.findById(id);
        if ( op.isPresent() == false ) {throw new DateTimeException("music not found");}

        return op.get();
    }


}
