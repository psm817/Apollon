package com.example.Apollon.domain.music.service;

import com.example.Apollon.domain.member.entity.Member;
import com.example.Apollon.domain.member.repository.MemberRepository;
import com.example.Apollon.domain.music.entity.Music;
import com.example.Apollon.domain.music.repository.MusicRepository;
import com.example.Apollon.domain.playlist.entity.Playlist;
import com.example.Apollon.domain.studio.entity.Studio;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class MusicService {

    @Autowired
    private final MusicRepository musicRepository;
    private final MemberRepository memberRepository;

    // 음악 업로드
    public void upload(String title, String content, Studio studio, MultipartFile thumbnail, MultipartFile song, List<String> genres) {
        String thumbnailFile = storeImg(thumbnail);
        String musicFile = storeMp3(song);

        Music music = Music.builder()
                .musicTitle(title)
                .musicContent(content)
                .studio(studio)
                .thumbnailImg(thumbnailFile)
                .musicMp3(musicFile)
                .genres(genres)
                .createDate(LocalDateTime.now())
                .build();

        musicRepository.save(music);
    }

    public String storeImg(MultipartFile profilePicture) {

        String uploadDir = "C:\\Users\\user\\IdeaProjects\\Apollon\\src\\main\\resources\\static\\uploadFile\\uploadImgs";

        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            try {
                Files.createDirectories(uploadPath);
            } catch (IOException e) {
                throw new IllegalStateException("Could not create upload directory", e);
            }
        }
        String fileName = UUID.randomUUID().toString();
        String imageFileName = fileName + ".jpg";
        try {
            Path filePath = uploadPath.resolve(imageFileName);
            Files.copy(profilePicture.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new IllegalStateException("Could not store image file", e);
        }
        return "/uploadFile/uploadImgs/" + imageFileName;
    }

    public String storeMp3(MultipartFile profilePicture) {

        String uploadDir = "C:\\Users\\user\\IdeaProjects\\Apollon\\src\\main\\resources\\static\\uploadFile\\uploadMusics";

        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            try {
                Files.createDirectories(uploadPath);
            } catch (IOException e) {
                throw new IllegalStateException("Could not create upload directory", e);
            }
        }
        String fileName = UUID.randomUUID().toString();
        String mp3FileName = fileName + ".mp3";
        try {
            Path filePath = uploadPath.resolve(mp3FileName);
            Files.copy(profilePicture.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new IllegalStateException("Could not store mp3 file", e);
        }
        return "/uploadFile/uploadMusics/" + mp3FileName;
    }

    // 좋아요 추가
    public void likeMusic(Music music, Member member) {
        music.addMusicLike(member);
        this.musicRepository.save(music);
    }

    public void unlikeMusic(Long musicId, Long memberId) {
        Music music = getMusic(musicId);
        Member member = getMemberId(memberId);
        music.getMusicLikers().remove(member);
        musicRepository.save(music);
    }

    // 원하는 음악을 가져오는데 음악 없으면 찾을 수 없다고 표시
    public Music getMusic(Long musicId) {
        Optional<Music> op = musicRepository.findById(musicId);
        if (!op.isPresent()) {
            throw new DateTimeException("음악을 찾을 수 없습니다. getMusic");
        }
        return op.get();
    }

    private Member getMemberId(Long memberId) {
        Optional<Member> memberOptional = memberRepository.findById(memberId);
        return memberOptional.orElseThrow(() -> new DateTimeException("해당 회원 번호의 정보를 찾을 수 없습니다. getMemberId : " + memberId));
    }

    public Music getMusicById(Long musicId) {
        return musicRepository.findById(musicId)
                .orElseThrow(() -> new RuntimeException("해당 번호를 가진 음악을 찾을 수 없습니다. getMusicById : " + musicId));
    }

    public void playPlaylist(Playlist playlist) {
        for (Music music : playlist.getMusicPlayList()) {
            System.out.println("재생 중인 음악 제목: " + music.getMusicTitle());
        }
    }

    public List<Music> getTop100MusicByPlayCount() {
        return musicRepository.findTop100ByOrderByMusicPlayCountDesc();
    }

    public List<Music> getListByStudio(Studio studio) {
        return musicRepository.findByStudio(studio);
    }
    public void reUpload(Music music, String content, MultipartFile thumbnail, List<String> genres) {
        if (thumbnail != null && !thumbnail.isEmpty()) {
            String thumbnailFile = storeImg(thumbnail);
            music.setThumbnailImg(thumbnailFile);
        }

        music.setMusicContent(content);
        music.setModifyDate(LocalDateTime.now());
        music.setGenres(genres);

        musicRepository.save(music);
    }

    public void delete(Music music) {
        this.musicRepository.delete(music);
    }

    public List<Music> getLatest4MusicByCreateDate() {
        List<Music> latestMusic = this.musicRepository.findAllByOrderByCreateDateDesc();

        if (latestMusic.size() <= 4) {
            return latestMusic;
        }

        // createDate가 동일한 경우 랜덤으로 4개의 음악 선택
        Collections.shuffle(latestMusic);
        return latestMusic.subList(0, 4);
    }

    public List<Music> getTop4MusicByLikers() {
        List<Music> allMusic = this.musicRepository.findAllByOrderByMusicLikersDesc();

        if (allMusic.size() <= 4) {
            return allMusic;
        }

        // 최대 4개의 음악을 선택하는 로직
        List<Music> top4Music = new ArrayList<>();
        int currentIndex = 0;

        while (top4Music.size() < 4 && currentIndex < allMusic.size()) {
            int nextIndex = currentIndex;

            // 현재 musicLikers 수와 같은 음악들을 모음
            List<Music> sameLikersMusic = new ArrayList<>();
            int currentLikersSize = allMusic.get(currentIndex).getMusicLikers().size();

            while (nextIndex < allMusic.size() && allMusic.get(nextIndex).getMusicLikers().size() == currentLikersSize) {
                sameLikersMusic.add(allMusic.get(nextIndex));
                nextIndex++;
            }

            // 같은 musicLikers 수를 가진 음악들을 섞어서 필요한 만큼 선택
            Collections.shuffle(sameLikersMusic);
            int remainingSlots = 4 - top4Music.size();
            top4Music.addAll(sameLikersMusic.subList(0, Math.min(remainingSlots, sameLikersMusic.size())));

            currentIndex = nextIndex;
        }

        return top4Music;
    }

}