package com.example.Apollon.domain.post.controller;

import com.example.Apollon.domain.member.entity.Member;
import com.example.Apollon.domain.member.service.MemberService;

import com.example.Apollon.domain.post.entity.BoardType;
import com.example.Apollon.domain.post.entity.Post;
import com.example.Apollon.domain.post.entity.PostComment;
import com.example.Apollon.domain.post.entity.PostForm;
import com.example.Apollon.domain.post.service.PostCommentService;
import com.example.Apollon.domain.post.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final MemberService memberService;
    private final PostCommentService postCommentService;

    @GetMapping("/list")
    public String list(Model model, @RequestParam(value="page", defaultValue="0") int page,
                       @RequestParam(value="boardType", required = false) BoardType boardType) {
        Page<Post> paging;
        List<Post> noticePosts = postService.getNoticePosts(4); // 공지 게시글 4개까지 가져오기


        if (boardType != null && boardType != BoardType.공지) {
            paging = postService.getPostsByBoardType(page, boardType);
        } else {
            paging = postService.getList(page);
        }
        model.addAttribute("paging", paging);
        model.addAttribute("notices", noticePosts);



        return "post/post_list";
        }

    @GetMapping("/detail/{id}")
    public String detail(Model model, @PathVariable("id") Long id) {
        Post p = postService.getPost(id);
        List<PostComment> comments = postCommentService.getPostCommentsByPost(id);
        model.addAttribute("post", p);
        model.addAttribute("comments", comments);
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

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/detail/{id}/comment")
    public String createComment(@PathVariable("id") Long postId,
                                @RequestParam(value = "content") String content,
                                Principal principal) {
        Member member = memberService.getMember(principal.getName());
        postCommentService.createPostComment(postId, content, member);
        return "redirect:/post/detail/" + postId;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String postModify(PostForm postForm, @PathVariable("id") Long id, Principal principal) {
        Post post = this.postService.getPost(id);
        if(!post.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        postForm.setTitle(post.getTitle());
        postForm.setContent(post.getContent());
        return "post/post_modifyForm";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String postModify(Model model, @Valid PostForm postForm, BindingResult bindingResult,
                                 Principal principal, @PathVariable("id") Long id) {

        if (bindingResult.hasErrors()) {
            return "post/post_form";
        }
        Post post = this.postService.getPost(id);
        if (!post.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.postService.modify(post, postForm.getTitle(), postForm.getContent());
        return String.format("redirect:/post/detail/%s", id);
    }


}
