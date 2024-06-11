package com.example.Apollon.domain.post.controller;

import com.example.Apollon.domain.member.entity.Member;
import com.example.Apollon.domain.member.service.MemberService;
import com.example.Apollon.domain.post.entity.BoardType;
import com.example.Apollon.domain.post.entity.Post;
import com.example.Apollon.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final MemberService memberService;

    @GetMapping("/list")
    public String list(Model model, @RequestParam(value="page", defaultValue="0") int page,
                       @RequestParam(value="boardType", required = false) BoardType boardType) {
        Page<Post> paging;
        if (boardType != null) {
            paging = postService.getPostsByBoardType(page, boardType);
        } else {
            paging = postService.getList(page);
        }
        model.addAttribute("paging", paging);
        return "post/post_list";
    }

    @GetMapping("/detail/{id}")
    public String detail(Model model, @PathVariable("id") Long id) {
        Post p = postService.getPost(id);
        model.addAttribute("post", p);
        return "post/post_detail";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/write")
    public String create(Model model) {
        model.addAttribute("boardTypes", BoardType.values());
        return "post/post_write";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/write")
    public String create(@RequestParam(value="title") String title,
                         @RequestParam(value="content") String content,
                         @RequestParam(value="boardType") BoardType boardType,
                         Principal principal) {
        Member member = memberService.getMember(principal.getName());
        postService.create(title, content, member, boardType);
        return "redirect:/post/list";
    }
}
