package com.example.Apollon.domain.music.service;

import com.example.Apollon.domain.member.entity.Member;
import com.example.Apollon.domain.member.repository.MemberRepository;
import com.example.Apollon.domain.music.entity.Music;
import com.example.Apollon.domain.music.repository.MusicRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    private final ResourceLoader resourceLoader;

    // 음악 업로드
    @SneakyThrows
    public void upload(String title, String content, String member, MultipartFile thumbnail, MultipartFile song, String[] genres) {

        // 현재 시간을 기반으로 타임스탬프 생성
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

        // 이미지와 음악을 구분할 폴더 생성
        String imgDirPath = Paths.get(fileDirPath, "uploadImgs_" + timestamp).toString();
        String audioDirPath = Paths.get(fileDirPath, "uploadAudios_" + timestamp).toString();

        // 디렉토리가 존재하지 않을 경우 생성
        createDirectoryIfNotExists(imgDirPath);
        createDirectoryIfNotExists(audioDirPath);

        // 클래스패스로부터 리소스 로드
        String uploadFilePath = resourceLoader.getResource("classpath:/resources/static/uploadFile").getFile().getAbsolutePath();

        createDirectoryIfNotExists(uploadFilePath);

        // 이미지와 음악 파일의 상대 경로 설정
        String thumbnailRelPath = "uploadImgs_" + timestamp + "/" + UUID.randomUUID().toString() + ".jpg";
        String musicFileRelPath = "uploadAudios_" + timestamp + "/" + UUID.randomUUID().toString() + ".mp3";

        // 이미지와 음악 파일 저장
        File thumbnailFile = new File(uploadFilePath + File.separator + thumbnailRelPath);
        File musicFile = new File(uploadFilePath + File.separator + musicFileRelPath);

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
                .genres(genres)
                .build();
        musicRepository.save(music);
    }

    private void createDirectoryIfNotExists(String dirPath) {
        File dir = new File(dirPath);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                throw new RuntimeException("Failed to create directory: " + dirPath);
            }
        }
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
