package com.example.Apollon.global.initData;

import com.example.Apollon.domain.comment.service.CommentService;
import com.example.Apollon.domain.member.entity.Member;
import com.example.Apollon.domain.member.service.MemberService;
import com.example.Apollon.domain.music.entity.Music;
import com.example.Apollon.domain.music.repository.MusicRepository;
import com.example.Apollon.domain.music.service.MusicService;
import com.example.Apollon.domain.studio.entity.Studio;
import com.example.Apollon.domain.studio.service.StudioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

@Configuration
@Profile("dev")
public class Dev {
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    MusicService musicService;

    @Autowired
    MusicRepository musicRepository;

    @Value("${custom.fileDirPath}")
    private String fileDirPath;

    @Bean
    public ApplicationRunner init(MemberService memberService, StudioService studioService, CommentService commentService) {
        return args -> {
            Member m1 = memberService.signup("admin", "admin", "admin", "admin@test.com","");
            Member m2 = memberService.signup("user1", "user1", "user1", "user1@test.com","");
            Member m3 = memberService.signup("user2", "user2", "user2", "user2@test.com","");

            Studio s1 = studioService.createOrUpdate(m1, 55,1);
            Studio s2 = studioService.createOrUpdate(m2, 12,1);
            Studio s3 = studioService.createOrUpdate(m3, 5,1);

            commentService.create(m2, s3, "테스트입니다.1", "테스트입니다.1");
            commentService.create(m2, s3, "테스트입니다.2", "테스트입니다.2");
            commentService.create(m2, s3, "테스트입니다.3", "테스트입니다.3");

            commentService.create(m3, s2, "테스트입니다.1", "테스트입니다.1");
            commentService.create(m3, s2, "테스트입니다.2", "테스트입니다.2");
            commentService.create(m3, s2, "테스트입니다.3", "테스트입니다.3");


            // 음악 재생을 위한 테스트 데이터 1
            // 음악 파일 상대 경로 (지정된 경로 사용)
            String thumbnailDirPath = "C:\\Users\\user\\IdeaProjects\\Apollon\\src\\main\\resources\\static\\uploadFile\\uploadImgs\\옥.png";
            // 음악 파일 상대 경로 (지정된 경로 사용)
            String musicDirPath = "C:\\Users\\user\\IdeaProjects\\Apollon\\src\\main\\resources\\static\\uploadFile\\uploadMusics\\Zedd & Alessia Cara - Stay.mp3";

            String thumbnailRelPath = UUID.randomUUID() + ".jpg";
            String musicFileRelPath = UUID.randomUUID() + ".mp3";

            File thumbnailFile = new File(thumbnailDirPath + "/" + thumbnailRelPath);
            File musicFile = new File(musicDirPath + "/" + musicFileRelPath);

            String title = "Zedd & Alessia Cara - Stay"; // 음악 제목
            String content = "Zedd & Alessia Cara의 Stay입니다."; // 음악 내용
            String member = m1.getUsername(); // 스튜디오 멤버
                       String[] genres = {"Electronica", "etc"}; // 장르

            Music music = Music.builder()
                    .musicTitle(title)
                    .musicContent(content)
                    .uploadStudio(member)
                    .thumbnailImg("/uploadImgs/" + thumbnailFile)
                    .musicMp3("/uploadMusics/" + musicFile)
                    .genres(genres)
                    .musicPlayCount(Long.valueOf("324"))
                    .build();
            // 테스트 데이터 저장
            musicRepository.save(music);

            // 음악 재생을 위한 테스트 데이터 2
            thumbnailDirPath = "C:\\Users\\user\\IdeaProjects\\Apollon\\src\\main\\resources\\static\\uploadFile\\uploadImgs\\좀벗.jpg";
            musicDirPath = "C:\\Users\\user\\IdeaProjects\\Apollon\\src\\main\\resources\\static\\uploadFile\\uploadMusics\\Zedd - Clarity.mp3";

            thumbnailRelPath = UUID.randomUUID() + ".jpg";
            musicFileRelPath = UUID.randomUUID() + ".mp3";

            thumbnailFile = new File(thumbnailDirPath + "/" + thumbnailRelPath);
            musicFile = new File(musicDirPath + "/" + musicFileRelPath);

            title = "Zedd - Clarity"; // 음악 제목
            content = "Zedd의 Clarity입니다."; // 음악 내용
            member = m3.getUsername(); // 스튜디오 멤버
            genres = new String[]{"Electronica", "etc"}; // 장르

            music = Music.builder()
                    .musicTitle(title)
                    .musicContent(content)
                    .uploadStudio(member)
                    .thumbnailImg("/uploadImgs/" + thumbnailFile)
                    .musicMp3("/uploadMusics/" + musicFile)
                    .genres(genres)
                    .musicPlayCount(Long.valueOf("650"))
                    .build();
            // 테스트 데이터 저장
            musicRepository.save(music);

            // 음악 재생을 위한 테스트 데이터 3
            thumbnailDirPath = "C:\\Users\\user\\IdeaProjects\\Apollon\\src\\main\\resources\\static\\uploadFile\\uploadImgs\\와보.png";
            musicDirPath = "C:\\Users\\user\\IdeaProjects\\Apollon\\src\\main\\resources\\static\\uploadFile\\uploadMusics\\Diamond Eyes - Stay.mp3";

            thumbnailRelPath = UUID.randomUUID() + ".jpg";
            musicFileRelPath = UUID.randomUUID() + ".mp3";

            thumbnailFile = new File(thumbnailDirPath + "/" + thumbnailRelPath);
            musicFile = new File(musicDirPath + "/" + musicFileRelPath);

            title = "Diamond Eyes - Stay"; // 음악 제목
            content = "Diamond Eyes의 Stay입니다."; // 음악 내용
            member = m2.getUsername(); // 스튜디오 멤버
            genres = new String[]{"Electronica", "etc"}; // 장르

            music = Music.builder()
                    .musicTitle(title)
                    .musicContent(content)
                    .uploadStudio(member)
                    .thumbnailImg("/uploadImgs/" + thumbnailFile)
                    .musicMp3("/uploadMusics/" + musicFile)
                    .genres(genres)
                    .musicPlayCount(Long.valueOf("134"))
                    .build();
            // 테스트 데이터 저장
            musicRepository.save(music);
        };
    }
}