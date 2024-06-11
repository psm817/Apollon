package com.example.Apollon.domain.post.controller;

import com.example.Apollon.domain.post.entity.Post;
import com.example.Apollon.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    @GetMapping("/list")
    public String list(Model model, @RequestParam(value="page", defaultValue="0") int page) {
        Page<Post> paging = this.postService.getList(page);
        model.addAttribute("paging", paging);

        return "post/post_list";
    }


    @GetMapping("/detail/{id}")
    public String detail(Model model, @PathVariable("id") Long id) {

        Post p = postService.getPost(id);

        model.addAttribute("post", p);

        return "post/post_detail";
    }

    @GetMapping("/write")
    public String create(Model model) {
        // 필요한 모델 데이터를 추가할 수 있습니다.
        return "post/post_write"; // 글 작성 페이지 템플릿 이름
    }

    @PostMapping("/write")
    public String create(@RequestParam(value="title") String title, @RequestParam(value="content") String content) {
        this.postService.create(title, content);
        return "redirect:/post/list";
    }

}