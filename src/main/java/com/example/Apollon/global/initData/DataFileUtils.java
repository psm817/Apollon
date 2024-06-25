package com.example.Apollon.global.initData;

import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Arrays;
import java.util.List;

// 폴더에 쌓이는 테스트 데이터 정리를 위한 샘플 파일(와보2.png, stay.mp3)을 제외한 데이터 삭제
@Component
public class DataFileUtils {

    // 삭제하지 않을 파일들의 이름 목록
    private static final List<String> EXCEPT_FILE_NAMES = Arrays.asList(
            "clarity.png", "rose.png", "stay.png", "closer.png",
            "Zedd - Clarity.mp3", "The Chainsmokers - Roses.mp3",
            "Zedd & Alessia Cara - Stay.mp3", "The Chainsmokers Feat Halsey - Closer (JayKode Remix).mp3"
    );

    public static void deleteFilesExcept(String directoryPath, String... exceptionFileNames) {
        File directory = new File(directoryPath);
        File[] files = directory.listFiles();

        if (files != null) {
            for (File file : files) {
                // 파일 이름이 삭제하지 않을 파일들의 목록에 포함되어 있지 않으면 삭제
                if (!EXCEPT_FILE_NAMES.contains(file.getName())) {
                    file.delete();
                }
            }
        }
    }

}