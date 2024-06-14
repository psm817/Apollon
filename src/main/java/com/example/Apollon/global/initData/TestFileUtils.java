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


    public MockMultipartFile createMockThumbnail() throws IOException {
        String thumbnailDirPath = fileDirPath + "/uploadFile/uploadImgs";

        String thumbnailRelPath = "와보2.png";

        // 예시로 사용할 가짜 이미지 파일 경로
        File mockThumbnailFile = ResourceUtils.getFile(thumbnailDirPath + "/" + thumbnailRelPath);
        InputStream inputMockThumbnailStream = new FileInputStream(mockThumbnailFile);

        return new MockMultipartFile(
                "test_thumbnail_와보2",    // 파일 이름
                "test_original_thumbnail_와보2",    // 원본 파일 이름
                "image/png",       // 파일 타입
                inputMockThumbnailStream       // 파일 데이터
        );
    }

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
}
