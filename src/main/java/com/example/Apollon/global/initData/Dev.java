package com.example.Apollon.global.initData;

import com.example.Apollon.domain.comment.service.CommentService;
import com.example.Apollon.domain.member.entity.Member;
import com.example.Apollon.domain.member.service.MemberService;
import com.example.Apollon.domain.music.service.MusicService;
import com.example.Apollon.domain.playlist.entity.Playlist;
import com.example.Apollon.domain.playlist.service.PlaylistService;
import com.example.Apollon.domain.post.entity.BoardType;
import com.example.Apollon.domain.post.service.PostService;
import com.example.Apollon.domain.studio.entity.Studio;
import com.example.Apollon.domain.studio.service.StudioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;

@Configuration
@Profile("dev")
public class Dev {
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    MusicService musicService;

    @Autowired
    PlaylistService playlistService;

    @Autowired
    TestFileUtils testFileUtils;

    @Autowired
    PostService postService;

    @Bean
    public ApplicationRunner init(MemberService memberService, StudioService studioService, CommentService commentService) {

        // 테스트 음악 데이터 추가를 하기 전에 폴더에 쌓이는 데이터 삭제
        DataFileUtils.deleteFilesExcept("C:/Users/user/IdeaProjects/Apollon/src/main/resources/static/uploadFile/uploadImgs", "와보2.png");
        DataFileUtils.deleteFilesExcept("C:/Users/user/IdeaProjects/Apollon/src/main/resources/static/uploadFile/uploadMusics", "Zedd & Alessia Cara - Stay.mp3");
        DataFileUtils.deleteFilesExcept("C:/work/Apollon/src/main/resources/static/uploadFile/uploadImgs", "와보2.png");
        DataFileUtils.deleteFilesExcept("C:/work/Apollon/src/main/resources/static/uploadFile/uploadMusics", "Zedd & Alessia Cara - Stay.mp3");
        return args -> {
            Member m1 = memberService.signup("admin", "admin", "admin", "admin@test.com","/images/none.png");
            Member m2 = memberService.signup("5004pp", "5004pp", "5004pp", "5004pp@test.com","/images/none.png");
            Member m3 = memberService.signup("ilmong06", "ilmong06", "ilmong06", "ilmong06@test.com","/images/none.png");
            Member m4 = memberService.signup("tron0318", "tron0318", "tron0318", "tron0318@test.com","/images/none.png");
            Member m5 = memberService.signup("maegon", "maegon", "maegon", "maegon@test.com","/images/none.png");

            Studio s1 = studioService.createOrUpdate(m1, 55, 1);
            Studio s2 = studioService.createOrUpdate(m2, 94, 1);
            Studio s3 = studioService.createOrUpdate(m3, 172, 1);
            Studio s4 = studioService.createOrUpdate(m4, 58, 1);
            Studio s5 = studioService.createOrUpdate(m5, 17, 1);

            Playlist p1 = playlistService.PCreateOrUpdate(m1);
            Playlist p2 = playlistService.PCreateOrUpdate(m2);
            Playlist p3 = playlistService.PCreateOrUpdate(m3);
            Playlist p4 = playlistService.PCreateOrUpdate(m4);
            Playlist p5 = playlistService.PCreateOrUpdate(m5);

            Playlist p1 = playlistService.PCreateOrUpdate(m1);
            Playlist p2 = playlistService.PCreateOrUpdate(m2);
            Playlist p3 = playlistService.PCreateOrUpdate(m3);

            commentService.create(m2, s3, "테스트입니다.1", "테스트입니다.1");
            commentService.create(m2, s3, "테스트입니다.2", "테스트입니다.2");
            commentService.create(m2, s3, "테스트입니다.3", "테스트입니다.3");

            commentService.create(m3, s2, "테스트입니다.1", "테스트입니다.1");
            commentService.create(m3, s2, "테스트입니다.2", "테스트입니다.2");
            commentService.create(m3, s2, "테스트입니다.3", "테스트입니다.3");

            // 폴더에 쌓이는 테스트 데이터 삭제 후 신규 테스트 음악 데이터 생성
            for (int i = 1; i<=30; i++) {
                musicService.upload("admin의 테스트 음악 제목 " + i, "admin의 테스트 음악 설명 " + i, s1,
                        testFileUtils.createMockThumbnail(),  testFileUtils.createMockSong(), Arrays.asList("Electronica, etc"));
            }

            for (int i = 1; i<=30; i++) {
                musicService.upload("5004pp의 테스트 음악 제목 " + i, "5004pp의 테스트 음악 설명 " + i, s2,
                        testFileUtils.createMockThumbnail(),  testFileUtils.createMockSong(), Arrays.asList("Ballad, OST, etc"));
            }

            for (int i = 1; i<=30; i++) {
                musicService.upload("ilmong06의 테스트 음악 제목 " + i, "ilmong06의 테스트 음악 설명 " + i, s3,
                        testFileUtils.createMockThumbnail(),  testFileUtils.createMockSong(), Arrays.asList("Rap/HipHop, JPOP, indie, etc"));
            }

            for (int i = 1; i<=30; i++) {
                musicService.upload("tron0318의 테스트 음악 제목 " + i, "tron0318의 테스트 음악 설명 " + i, s4,
                        testFileUtils.createMockThumbnail(),  testFileUtils.createMockSong(), Arrays.asList("Rap/HipHop, JPOP, indie, etc"));
            }

            for (int i = 1; i<=30; i++) {
                musicService.upload("maegon의 테스트 음악 제목 " + i, "maegon의 테스트 음악 설명 " + i, s5,
                        testFileUtils.createMockThumbnail(),  testFileUtils.createMockSong(), Arrays.asList("Rap/HipHop, JPOP, indie, etc"));
            }
            // 게시글
            for (int i = 1; i <= 5; i++) {
                Member author = m1;
                postService.create("제목 " + i, "내용 " + i, author, BoardType.공지);

            }//자유게시글
            for (int i = 1; i <= 30; i++) {
                Member author = m1;
                postService.create("제목 " + i, "내용 " + i, author, BoardType.자유);

            }
        };
    }
}