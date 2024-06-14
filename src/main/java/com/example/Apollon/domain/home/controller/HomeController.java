package com.example.Apollon.domain.home.controller;

import com.example.Apollon.domain.post.entity.BoardType;
import com.example.Apollon.domain.post.entity.Post;
import com.example.Apollon.domain.post.entity.PostComment;
import com.example.Apollon.domain.post.service.PostCommentService;
import com.example.Apollon.domain.post.service.PostService;
import com.example.Apollon.domain.studio.entity.Studio;
import com.example.Apollon.domain.studio.service.StudioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final StudioService studioService;
    private final PostService postService;

    @GetMapping("/")
    public String goMain(Model model, Principal principal) {
        // 메인페이지 우측에 커뮤니티(자주묻는질문), 스튜디오(방문자TOP) 가져오기
        List<Post> top5NoticePosts = this.postService.getTop5PostsByViewAndBoardType(BoardType.공지);
        List<Studio> top5Studios = this.studioService.getTop5StudiosByVisit();

        model.addAttribute("top5NoticePosts", top5NoticePosts);
        model.addAttribute("top5Studios", top5Studios);

        // 스튜디오 진입 시 로그인된 회원 스튜디외의 차단 상태 판단을 위해 작성
        if(principal != null) {
            Studio studio = this.studioService.getStudioByMemberUsername(principal.getName());

            if(studio != null) {
                Integer studioActive = studio.getActive();

                model.addAttribute("studioActive", studioActive);
            }
        }

        return "mainPage";
    }
}
