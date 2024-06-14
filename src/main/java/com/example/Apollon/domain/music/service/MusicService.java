package com.example.Apollon.domain.music.service;

import com.example.Apollon.domain.member.entity.Member;
import com.example.Apollon.domain.member.repository.MemberRepository;
import com.example.Apollon.domain.music.entity.Music;
import com.example.Apollon.domain.music.repository.MusicRepository;
import com.example.Apollon.domain.playlist.entity.Playlist;
import com.example.Apollon.domain.studio.entity.Studio;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MusicService {

    @Autowired
    private final MusicRepository musicRepository;
    private final MemberRepository memberRepository;

    @Value("${custom.fileDirPath}")
    private String fileDirPath;
    // 음악 업로드
    public void upload(String title, String content, Studio Studio, MultipartFile thumbnail, MultipartFile song, List<String> genres) {
        String thumbnailDirPath = fileDirPath + "/uploadFile/uploadImgs";
        String musicDirPath = fileDirPath + "/uploadFile/uploadMusics";

        String thumbnailRelPath = UUID.randomUUID() + ".png";
        String musicFileRelPath = UUID.randomUUID() + ".mp3";

        File thumbnailFile = new File(thumbnailDirPath + "/" + thumbnailRelPath);
        File musicFile = new File(musicDirPath + "/" + musicFileRelPath);

        try {
            thumbnail.transferTo(thumbnailFile);
            song.transferTo(musicFile);
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload files", e);
        }

        Music music = Music.builder()
                .musicTitle(title)
                .musicContent(content)
                .studio(Studio)
                .thumbnailImg(""+ thumbnailFile)
                .musicMp3(""+ musicFile)
                .genres(genres)
                .createDate(LocalDateTime.now())
                .build();

        musicRepository.save(music);
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
        if (!op.isPresent()) {throw new DateTimeException("음악을 찾을 수 없습니다. getMusic");}

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
        String thumbnailDirPath = fileDirPath + "/uploadFile/uploadImgs";

        String thumbnailRelPath = UUID.randomUUID() + ".png";

        File thumbnailFile = new File(thumbnailDirPath + "/" + thumbnailRelPath);

        try {
            thumbnail.transferTo(thumbnailFile);
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload files", e);
        }

        music.setMusicContent(content);
        music.setModifyDate(LocalDateTime.now());
        music.setThumbnailImg("" + thumbnailFile);
        music.setGenres(genres);

        musicRepository.save(music);
    }

    public void delete(Music music) {
        this.musicRepository.delete(music);
    }
}
