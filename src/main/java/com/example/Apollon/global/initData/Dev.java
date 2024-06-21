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

        return args -> {
            Member m1 = memberService.signup("admin", "admin", "admin", "admin@test.com","/images/none.png");
            Member m2 = memberService.signup("5004pp", "5004pp", "5004pp", "5004pp@test.com","/images/none.png");
            Member m3 = memberService.signup("ilmong06", "ilmong06", "ilmong06", "ilmong06@test.com","/images/none.png");
            Member m4 = memberService.signup("tron0318", "tron0318", "tron0318", "tron0318@test.com","/images/none.png");
            Member m5 = memberService.signup("maegon", "maegon", "maegon", "maegon@test.com","/images/none.png");

            Studio s1 = studioService.createOrUpdate(m1, 55, 1);
            Studio s2 = studioService.createOrUpdate(m2, 43, 1);
            Studio s3 = studioService.createOrUpdate(m3, 172, 1);
            Studio s4 = studioService.createOrUpdate(m4, 58, 1);
            Studio s5 = studioService.createOrUpdate(m5, 71, 1);

            Playlist p1 = playlistService.PCreateOrUpdate(m1);
            Playlist p2 = playlistService.PCreateOrUpdate(m2);
            Playlist p3 = playlistService.PCreateOrUpdate(m3);
            Playlist p4 = playlistService.PCreateOrUpdate(m4);
            Playlist p5 = playlistService.PCreateOrUpdate(m5);

            // 방명록 주고받기 테스트 데이터
            commentService.create(m1, s2, "관리자입니다.", "Apollon에 오신 것을 환영합니다. 스튜디오에서 본인의 음악을 마음껏 뽐내보세요!~");
            commentService.create(m3, s2, "노래가 너무 좋아요", "매번 노래 들으러 방문하고 있어요!!");
            commentService.create(m4, s2, "자작곡 별로인듯...", "비트가 너무 단조로워서 별로예요");
            commentService.create(m5, s2, "프사가 귀엽네요", "프사가 너무 귀여운데요??");

            commentService.create(m1, s3, "관리자입니다.", "Apollon에 오신 것을 환영합니다. 스튜디오에서 본인의 음악을 마음껏 뽐내보세요!~");
            commentService.create(m2, s3, "안녕하세요!", "노래 잘 듣고 갑니다! 제 스튜디오도 한 번 방문해주세요~~");
            commentService.create(m4, s3, "자작곡 별로인듯...", "비트가 너무 단조로워서 별로예요");
            commentService.create(m5, s3, "프사가 귀엽네요", "프사가 너무 귀여운데요??");

            commentService.create(m1, s4, "관리자입니다.", "Apollon에 오신 것을 환영합니다. 스튜디오에서 본인의 음악을 마음껏 뽐내보세요!~");
            commentService.create(m2, s4, "안녕하세요!", "노래 잘 듣고 갑니다! 제 스튜디오도 한 번 방문해주세요~~");
            commentService.create(m3, s4, "노래가 너무 좋아요", "매번 노래 들으러 방문하고 있어요!!");
            commentService.create(m5, s4, "프사가 귀엽네요", "프사가 너무 귀여운데요??");

            commentService.create(m1, s5, "관리자입니다.", "Apollon에 오신 것을 환영합니다. 스튜디오에서 본인의 음악을 마음껏 뽐내보세요!~");
            commentService.create(m2, s5, "안녕하세요!", "노래 잘 듣고 갑니다! 제 스튜디오도 한 번 방문해주세요~~");
            commentService.create(m3, s5, "노래가 너무 좋아요", "매번 노래 들으러 방문하고 있어요!!");
            commentService.create(m4, s5, "자작곡 별로인듯...", "비트가 너무 단조로워서 별로예요");


            // 폴더에 쌓이는 테스트 데이터 삭제 후 신규 테스트 음악 데이터 생성
            for (int i = 1; i<=30; i++) {
                musicService.upload("Zedd & Alessia Cara - Stay " + i, "5004pp의 테스트 음악 설명 " + i, s2,
                        testFileUtils.createMockThumbnail(),  testFileUtils.createMockSong(), Arrays.asList("Ballad, Dance, etc"));
            }

            for (int i = 1; i<=30; i++) {
                musicService.upload("The Chainsmokers - Roses " + i, "ilmong06의 테스트 음악 설명 " + i, s3,
                        testFileUtils.createMockThumbnail2(),  testFileUtils.createMockSong2(), Arrays.asList("Rap/HipHop, R&B/Soul, etc"));
            }

            for (int i = 1; i<=30; i++) {
                musicService.upload("The Chainsmokers Feat Halsey - Closer (JayKode Remix) " + i, "tron0318의 테스트 음악 설명 " + i, s4,
                        testFileUtils.createMockThumbnail3(),  testFileUtils.createMockSong3(), Arrays.asList("Rock/Metal, JPOP, Electronica, etc"));
            }

            for (int i = 1; i<=30; i++) {
                musicService.upload("Zedd - Clarity " + i, "maegon의 테스트 음악 설명 " + i, s5,
                        testFileUtils.createMockThumbnail4(),  testFileUtils.createMockSong4(), Arrays.asList("OST, indie, etc"));
            }

            postService.create("비밀번호를 분실했을 경우", "비밀번호를 분실하셨다면 '로그인' 페이지로 이동하셔서 비밀번호 찾기를 통해 임시비밀번호를 발급받으세요", m1, BoardType.공지);
            postService.create("음악 업로드하는 방법", "음악 업로드 방법은 로그인 후 개인 스튜디오로 이동하셔서 등록 버튼을 클릭하시면 됩니다.", m1, BoardType.공지);
            postService.create("회원 탈퇴 시 불이익", "회원 탈퇴를 하시면 개인이 업로드했던 음악과 작성한 모든 커뮤니티 및 방명록 게시글 사라집니다.", m1, BoardType.공지);
            postService.create("스튜디오가 차단됐을 경우", "개인 스튜디오가 차단되어 진입이 불가능한 경우, 커뮤니티 자유 게시판을 통해 문의를 부탁드립니다.", m1, BoardType.공지);
            postService.create("업로드한 음악이 재생이 되지 않은 경우", "업로드한 음악의 확장자가 .mp3인지 확인 부탁드립니다.", m1, BoardType.공지);

            //자유게시글
            for (int i = 1; i <= 30; i++) {
                Member author = m3;
                postService.create("자유 게시글 " + i, "내용 " + i, author, BoardType.자유);
            }

            for (int i = 1; i <= 10; i++) {
                Member author = m4;
                postService.create("친목 게시글 " + i, "내용 " + i, author, BoardType.친목);
            }
        };
    }
}