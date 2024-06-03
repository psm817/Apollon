package com.example.Apollon.domain.music.service;

import com.example.Apollon.domain.member.entity.Member;
import com.example.Apollon.domain.member.repository.MemberRepository;
import com.example.Apollon.domain.music.entity.Music;
import com.example.Apollon.domain.music.repository.MusicRepository;
import com.example.Apollon.domain.post.entity.Post;
import com.example.Apollon.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MusicService {

    @Autowired
    private final MusicRepository musicRepository;
    private final MemberRepository memberRepository;

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
