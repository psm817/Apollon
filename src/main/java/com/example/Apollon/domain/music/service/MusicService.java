package com.example.Apollon.domain.music.service;

import com.example.Apollon.domain.member.entity.Member;
import com.example.Apollon.domain.member.repository.MemberRepository;
import com.example.Apollon.domain.music.entity.Music;
import com.example.Apollon.domain.music.repository.MusicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.DateTimeException;
import java.util.Comparator;
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

    // 음악 업로드(나중에 studio에 등록 기능 추가되면 studio로)
    public void upload(String title, String content, String member, MultipartFile thumbnail, MultipartFile song) {
        String thumbnailRelPath = UUID.randomUUID().toString() + ".jpg";
        File thumbnailFile = new File(fileDirPath + "/" + thumbnailRelPath);

        String musicFileRelPath = UUID.randomUUID().toString() + ".mp3";
        File musicFile = new File(fileDirPath + "/" + musicFileRelPath);

        try {
            thumbnail.transferTo(thumbnailFile);
        } catch ( IOException e ) {
            throw new RuntimeException(e);
        }

        try {
            song.transferTo(musicFile);
        } catch ( IOException e ) {
            throw new RuntimeException(e);
        }

        Music music = Music.builder()
                .musicTitle(title)
                .musicContent(content)
                .uploadStudio(member)
                .thumbnailImg(thumbnailRelPath)
                .musicMp3(musicFileRelPath)
                .build();
        musicRepository.save(music);
    }

    // 좋아요 추가
    public void likeMusic(Long musicId, Long memberId) {
        Music music = getMusic(musicId);
        Member member = getMemberId(memberId);

        music.addLikedByMembers(member);
        musicRepository.save(music);
    }

    public void unlikeMusic(Long musicId, Long memberId) {
        Music music = getMusic(musicId);
        Member member = getMemberId(memberId);

        music.getLikedByMembers().remove(member);
        musicRepository.save(music);
    }


    // 해당 음악에 대한 좋아요 여부 확인
    public boolean isMusicLikedByMember(Long musicId, Long memberId) {
        Music music = getMusic(musicId);
        Member member = getMemberId(memberId);

        return music.getLikedByMembers().contains(member);
    }

    // TOP100 가져옴(전체 음악중 재생한 count가 많은 순으로 정렬)
    public List<Music> getTOP100List() {
        List<Music> allMusic = musicRepository.findAll();
        allMusic.sort(Comparator.comparingLong(Music::getMusicPlayCount).reversed());
        return allMusic;
    }

    // 원하는 음악을 가져오는데 음악 없으면 찾을 수 없다고 표시
    public Music getMusic(Long musicId) {
        Optional<Music> op = musicRepository.findById(musicId);
        if ( op.isPresent() == false ) {throw new DateTimeException("music not found");}

        return op.get();
    }

    private Member getMemberId(Long memberId) {
        Optional<Member> memberOptional = memberRepository.findById(memberId);
        return memberOptional.orElseThrow(() -> new DateTimeException("Member not found with ID: " + memberId));
    }

}
