package com.example.Apollon.global.initData;

import java.io.File;

// 폴더에 쌓이는 테스트 데이터 정리를 위한 샘플 파일(와보2.png, stay.mp3)을 제외한 데이터 삭제
public class DataFileUtils {

    public static void deleteFilesExcept(String directoryPath, String exceptionFileName) {
        File directory = new File(directoryPath);
        File[] files = directory.listFiles();

        if (files != null) {
            for (File file : files) {
                if (!file.getName().equals(exceptionFileName)) {
                    file.delete();
                }
            }
        }
    }

}