package com.example.Apollon.domain.post.controller;

import com.example.Apollon.domain.post.entity.Post;
import com.example.Apollon.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    @GetMapping("/list")
    public String list(Model model) {
        List<Post> postList = this.postService.getList();

        model.addAttribute("postList", postList);

        return "post/post_list";
    }


    @GetMapping("/detail/{id}")
    public String detail(Model model, @PathVariable("id") Long id) {

        Post p = postService.getPost(id);

        model.addAttribute("post", p);

        return "post/post_detail";
    }

}