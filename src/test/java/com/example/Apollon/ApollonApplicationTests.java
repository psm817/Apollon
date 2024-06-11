package com.example.Apollon;

import com.example.Apollon.domain.member.entity.Member;
import com.example.Apollon.domain.member.service.MemberService;
import com.example.Apollon.domain.post.service.PostService;
import com.example.Apollon.domain.studio.entity.Studio;
import com.example.Apollon.domain.studio.repository.StudioRepository;
import com.example.Apollon.domain.studio.service.StudioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
class ApollonApplicationTests {
	@Autowired
	private StudioService studioService;
	@Autowired
	private MemberService memberService;

	@Autowired
	private StudioRepository studioRepository;

	@Test
	void contextLoads() {
		Member member1 = memberService.signup("user3", "user3", "user3", "user3@test.com","");
		Member member2 = memberService.signup("user4", "user4", "user4", "user4@test.com","");

		Studio studio = studioService.createOrUpdate(member1, 15, 1);

		studio.addLike(member1);
		studio.addLike(member2);
		studioRepository.save(studio);
	}

	@Autowired
	PostService postService;

//	@Test
//	void contextLoads1() {
//		for ( int i = 1; i <= 300; i++ ) {
//			String title = String.format("안녕하세요%d", i);
//			String content = String.format("내용 %d", i);
//			String author = String.format("admin %d" ,i);
//			LocalDateTime createDate = LocalDateTime.now();
//			this.postService.create(title, content, null);
//		}
//	}

}
