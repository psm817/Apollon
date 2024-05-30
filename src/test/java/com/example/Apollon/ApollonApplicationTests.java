package com.example.Apollon;

import com.example.Apollon.domain.post.service.PostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
class ApollonApplicationTests {
	@Autowired
	PostService postService;

	@Test
	void contextLoads() {
	}

	@Test
	void postTest() {
		for ( int i = 1; i <= 30; i++ ) {
			String title = String.format("제목 %d", i);
			String content = String.format("내용 %d", i);
			LocalDateTime createDate = LocalDateTime.now();
			this.postService.create(title, content);
		}
	}

}
