package com.example.Apollon;

import com.example.Apollon.domain.member.entity.Member;
import com.example.Apollon.domain.member.service.MemberService;
import com.example.Apollon.domain.studio.entity.Studio;
import com.example.Apollon.domain.studio.repository.StudioRepository;
import com.example.Apollon.domain.studio.service.StudioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
		Member member1 = memberService.signup("user3", "user3", "user3", "user3@test.com");
		Member member2 = memberService.signup("user4", "user4", "user4", "user4@test.com");

		Studio studio = studioService.create(member1, 15, 1);

		studio.addLike(member1);
		studio.addLike(member2);
		studioRepository.save(studio);
	}

}
