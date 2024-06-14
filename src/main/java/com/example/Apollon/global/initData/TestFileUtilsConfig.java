package com.example.Apollon.global.initData;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestFileUtilsConfig {

    @Bean
    public TestFileUtils testFileUtils() {
        return new TestFileUtils();
    }
}