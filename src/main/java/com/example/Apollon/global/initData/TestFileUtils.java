package com.example.Apollon.global.initData;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

// 테스트 음악 데이터 생성을 위한 테스트 음악 썸네일과 오디오 추가
@Component
public class TestFileUtils {
    @Value("${custom.fileDirPath}")
    private String fileDirPath;

    // 음악 테스트 데이터 사진
    public MockMultipartFile createMockThumbnail() throws IOException {
        String thumbnailDirPath = fileDirPath + "/uploadFile/uploadImgs";

        String thumbnailRelPath = "stay.png";

        // 예시로 사용할 가짜 이미지 파일 경로
        File mockThumbnailFile = ResourceUtils.getFile(thumbnailDirPath + "/" + thumbnailRelPath);
        InputStream inputMockThumbnailStream = new FileInputStream(mockThumbnailFile);

        return new MockMultipartFile(
                "test_thumbnail_stay",    // 파일 이름
                "test_original_thumbnail_stay",    // 원본 파일 이름
                "image/png",       // 파일 타입
                inputMockThumbnailStream       // 파일 데이터
        );
    }

    public MockMultipartFile createMockThumbnail2() throws IOException {
        String thumbnailDirPath = fileDirPath + "/uploadFile/uploadImgs";

        String thumbnailRelPath = "rose.png";

        // 예시로 사용할 가짜 이미지 파일 경로
        File mockThumbnailFile = ResourceUtils.getFile(thumbnailDirPath + "/" + thumbnailRelPath);
        InputStream inputMockThumbnailStream = new FileInputStream(mockThumbnailFile);

        return new MockMultipartFile(
                "test_thumbnail_rose",    // 파일 이름
                "test_original_thumbnail_rose",    // 원본 파일 이름
                "image/png",       // 파일 타입
                inputMockThumbnailStream       // 파일 데이터
        );
    }

    public MockMultipartFile createMockThumbnail3() throws IOException {
        String thumbnailDirPath = fileDirPath + "/uploadFile/uploadImgs";

        String thumbnailRelPath = "closer.png";

        // 예시로 사용할 가짜 이미지 파일 경로
        File mockThumbnailFile = ResourceUtils.getFile(thumbnailDirPath + "/" + thumbnailRelPath);
        InputStream inputMockThumbnailStream = new FileInputStream(mockThumbnailFile);

        return new MockMultipartFile(
                "test_thumbnail_closer",    // 파일 이름
                "test_original_thumbnail_closer",    // 원본 파일 이름
                "image/png",       // 파일 타입
                inputMockThumbnailStream       // 파일 데이터
        );
    }

    public MockMultipartFile createMockThumbnail4() throws IOException {
        String thumbnailDirPath = fileDirPath + "/uploadFile/uploadImgs";

        String thumbnailRelPath = "clarity.png";

        // 예시로 사용할 가짜 이미지 파일 경로
        File mockThumbnailFile = ResourceUtils.getFile(thumbnailDirPath + "/" + thumbnailRelPath);
        InputStream inputMockThumbnailStream = new FileInputStream(mockThumbnailFile);

        return new MockMultipartFile(
                "test_thumbnail_clarity",    // 파일 이름
                "test_original_thumbnail_clarity",    // 원본 파일 이름
                "image/png",       // 파일 타입
                inputMockThumbnailStream       // 파일 데이터
        );
    }


    // 음악 테스트 데이터 mp3
    public MockMultipartFile createMockSong() throws IOException {
        String musicDirPath = fileDirPath + "/uploadFile/uploadMusics";

        String musicFileRelPath = "Zedd & Alessia Cara - Stay.mp3";

        // 예시로 사용할 가짜 음악 파일 경로
        File mockMusicFile = ResourceUtils.getFile(musicDirPath + "/" + musicFileRelPath);
        InputStream inputMockMusicStream = new FileInputStream(mockMusicFile);

        return new MockMultipartFile(
                "test_music",    // 파일 이름
                "test_original_music",    // 원본 파일 이름
                "audio/mp3",       // 파일 타입
                inputMockMusicStream       // 파일 데이터
        );
    }

    public MockMultipartFile createMockSong2() throws IOException {
        String musicDirPath = fileDirPath + "/uploadFile/uploadMusics";

        String musicFileRelPath = "The Chainsmokers - Roses.mp3";

        // 예시로 사용할 가짜 음악 파일 경로
        File mockMusicFile = ResourceUtils.getFile(musicDirPath + "/" + musicFileRelPath);
        InputStream inputMockMusicStream = new FileInputStream(mockMusicFile);

        return new MockMultipartFile(
                "test_music",    // 파일 이름
                "test_original_music",    // 원본 파일 이름
                "audio/mp3",       // 파일 타입
                inputMockMusicStream       // 파일 데이터
        );
    }

    public MockMultipartFile createMockSong3() throws IOException {
        String musicDirPath = fileDirPath + "/uploadFile/uploadMusics";

        String musicFileRelPath = "The Chainsmokers Feat Halsey - Closer (JayKode Remix).mp3";

        // 예시로 사용할 가짜 음악 파일 경로
        File mockMusicFile = ResourceUtils.getFile(musicDirPath + "/" + musicFileRelPath);
        InputStream inputMockMusicStream = new FileInputStream(mockMusicFile);

        return new MockMultipartFile(
                "test_music",    // 파일 이름
                "test_original_music",    // 원본 파일 이름
                "audio/mp3",       // 파일 타입
                inputMockMusicStream       // 파일 데이터
        );
    }

    public MockMultipartFile createMockSong4() throws IOException {
        String musicDirPath = fileDirPath + "/uploadFile/uploadMusics";

        String musicFileRelPath = "Zedd - Clarity.mp3";

        // 예시로 사용할 가짜 음악 파일 경로
        File mockMusicFile = ResourceUtils.getFile(musicDirPath + "/" + musicFileRelPath);
        InputStream inputMockMusicStream = new FileInputStream(mockMusicFile);

        return new MockMultipartFile(
                "test_music",    // 파일 이름
                "test_original_music",    // 원본 파일 이름
                "audio/mp3",       // 파일 타입
                inputMockMusicStream       // 파일 데이터
        );
    }
}
