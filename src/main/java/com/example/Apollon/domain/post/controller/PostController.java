package com.example.Apollon.domain.post.controller;

import com.example.Apollon.domain.member.entity.Member;
import com.example.Apollon.domain.member.service.MemberService;

import com.example.Apollon.domain.playlist.entity.Playlist;
import com.example.Apollon.domain.playlist.service.PlaylistService;
import com.example.Apollon.domain.post.entity.BoardType;
import com.example.Apollon.domain.post.entity.Post;
import com.example.Apollon.domain.post.entity.PostComment;
import com.example.Apollon.domain.post.entity.PostForm;
import com.example.Apollon.domain.post.service.PostCommentService;
import com.example.Apollon.domain.post.service.PostService;
import com.example.Apollon.domain.studio.entity.Studio;
import com.example.Apollon.domain.studio.service.StudioService;
import com.example.Apollon.global.DataNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final MemberService memberService;
    private final PostCommentService postCommentService;
    private final StudioService studioService;
    private final PlaylistService playlistService;

    @GetMapping("/list")
    public String list(Model model,
                       @RequestParam(value = "page", defaultValue = "0") int page,
                       @RequestParam(value = "boardType", required = false) BoardType boardType,
                       Authentication authentication, Principal principal) {

        Page<Post> paging;
        List<Post> noticePosts = postService.getNoticePosts(4);
        Member member = null;
        List<Post> myPosts = new ArrayList<>();
        List<PostComment> myComments = new ArrayList<>();

        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            member = memberService.getMember(username);
            myPosts = postService.getPostsByMember(member);
            myComments = postCommentService.getCommentsByMember(member);
        }

        if (boardType != null) {
            paging = postService.getPostsByBoardType(page, boardType);
        } else {
            paging = postService.getListExcludeNotice(page);
        }

        model.addAttribute("paging", paging);
        model.addAttribute("notices", noticePosts);
        model.addAttribute("member", member);
        model.addAttribute("myPosts", myPosts);
        model.addAttribute("myComments", myComments);


        List<Post> topPosts = postService.getTop10Posts().stream()
                .filter(post -> post.getBoardType() != BoardType.공지)
                .collect(Collectors.toList());
        model.addAttribute("topPosts", topPosts);
        model.addAttribute("boardType", boardType);

        // 스튜디오 진입 시 로그인된 회원 스튜디외의 차단 상태 판단을 위해 작성
        if (principal != null) {
            Studio studio = this.studioService.getStudioByMemberUsername(principal.getName());

            if (studio != null) {
                // 회원별 플레이리스트가져오기(만들고 모델링해주고 푸터에서 넣어보자)
                Member member2 = memberService.getMember(principal.getName());
                Playlist playList = this.playlistService.getPlaylist(member2.getId());

                model.addAttribute("playList", playList);
            }
        }

        return "post/post_list";
    }




    // PostController.java
    @GetMapping("/detail/{id}")
    public String detail(Model model, @PathVariable("id") Long id, Principal principal) {
        List<Post> topPosts = postService.getTop10Posts();
        topPosts = topPosts.stream()
                .filter(post -> post.getBoardType() != BoardType.공지)
                .collect(Collectors.toList());
        model.addAttribute("topPosts", topPosts);

        Post post = postService.getPostByView(id);  // 조회수가 증가된 Post 객체 가져오기

        List<PostComment> comments = postCommentService.getPostCommentsByPost(id);
        Member member = null;
        List<Post> myPosts = new ArrayList<>();
        List<PostComment> myComments = new ArrayList<>();

        if (principal != null && principal.getName() != null) {
            member = memberService.getMember(principal.getName());
            myPosts = postService.getPostsByMember(member);
            myComments = postCommentService.getCommentsByMember(member);
        }

        model.addAttribute("member", member);
        model.addAttribute("myPosts", myPosts);
        model.addAttribute("myComments", myComments);

        model.addAttribute("post", post);  // 수정된 Post 객체를 모델에 추가
        model.addAttribute("comments", comments);

        // 스튜디오 진입 시 로그인된 회원 스튜디외의 차단 상태 판단을 위해 작성
        if (principal != null) {
            Studio studio = this.studioService.getStudioByMemberUsername(principal.getName());

            if (studio != null) {
                // 회원별 플레이리스트가져오기(만들고 모델링해주고 푸터에서 넣어보자)
                Member member2 = memberService.getMember(principal.getName());
                Playlist playList = this.playlistService.getPlaylist(member2.getId());

                model.addAttribute("playList", playList);
            }
        }

        try {
            Post nextPost = postService.getNextPostByBoardType(post.getId(), post.getBoardType());
            model.addAttribute("nextPostId", nextPost.getId());
        } catch (DataNotFoundException e) {
            model.addAttribute("nextPostId", null); // No next post available
        }

        try {
            Post previousPost = postService.getPreviousPostByBoardType(post.getId(), post.getBoardType());
            model.addAttribute("previousPostId", previousPost.getId());
        } catch (DataNotFoundException e) {
            model.addAttribute("previousPostId", null); // No previous post available
        }

        return "post/post_detail";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/write")
    public String create(Model model, Principal principal) {

        Member member = memberService.getMember(principal.getName());
        List<Post> myPosts = postService.getPostsByMember(member);
        List<PostComment> myComments = postCommentService.getCommentsByMember(member);
        List<Post> topPosts = postService.getTop10Posts();
        topPosts = topPosts.stream()
                .filter(post -> post.getBoardType() != BoardType.공지)
                .collect(Collectors.toList());
        model.addAttribute("topPosts", topPosts);

        model.addAttribute("member", member);
        model.addAttribute("myPosts", myPosts);
        model.addAttribute("myComments", myComments);
        model.addAttribute("boardTypes", BoardType.values());

        // 스튜디오 진입 시 로그인된 회원 스튜디외의 차단 상태 판단을 위해 작성
        Studio studio = this.studioService.getStudioByMemberUsername(principal.getName());

        if (studio != null) {
            // 회원별 플레이리스트가져오기(만들고 모델링해주고 푸터에서 넣어보자)
            Member member2 = memberService.getMember(principal.getName());
            Playlist playList = this.playlistService.getPlaylist(member2.getId());

            model.addAttribute("playList", playList);
        }

        return "post/post_write";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/write")
    public String create(@RequestParam(value="title") String title,
                         @RequestParam(value="content") String content,
                         @RequestParam(value="boardType") BoardType boardType,
                         Principal principal,Model model) {
        Member member = memberService.getMember(principal.getName());
        List<Post> myPosts = postService.getPostsByMember(member);
        List<PostComment> myComments = postCommentService.getCommentsByMember(member);


        postService.create(title, content, member, boardType);
        model.addAttribute("member", member);
        model.addAttribute("myPosts", myPosts);
        model.addAttribute("myComments", myComments);
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
    public String postModify(PostForm postForm, @PathVariable("id") Long id, Principal principal, Model model) {

        Post post = this.postService.getPost(id);
        if (!post.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }

        Member member = memberService.getMember(principal.getName());
        List<Post> myPosts = postService.getPostsByMember(member);
        List<PostComment> myComments = postCommentService.getCommentsByMember(member);

        // Fetch top 10 posts excluding notice posts
        List<Post> topPosts = postService.getTop10Posts().stream()
                .filter(p -> p.getBoardType() != BoardType.공지)
                .collect(Collectors.toList());

        model.addAttribute("member", member);
        model.addAttribute("myPosts", myPosts);
        model.addAttribute("myComments", myComments);
        model.addAttribute("topPosts", topPosts);

        postForm.setTitle(post.getTitle());
        postForm.setContent(post.getContent());
        model.addAttribute("post", post);
        model.addAttribute("postForm", postForm); // Ensure the PostForm is added to the model

        // 스튜디오 진입 시 로그인된 회원 스튜디외의 차단 상태 판단을 위해 작성
        Studio studio = this.studioService.getStudioByMemberUsername(principal.getName());

        if (studio != null) {
            // 회원별 플레이리스트가져오기(만들고 모델링해주고 푸터에서 넣어보자)
            Member member2 = memberService.getMember(principal.getName());
            Playlist playList = this.playlistService.getPlaylist(member2.getId());

            model.addAttribute("playList", playList);
        }

        return "post/post_modifyForm";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String postModify(@Valid PostForm postForm, BindingResult bindingResult,
                                 Principal principal, @PathVariable("id") Long id,Model model) {

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

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String postDelete(Principal principal, @PathVariable("id") Long id) {
        Post post = this.postService.getPost(id);

        this.postService.delete(post);
        return "redirect:/post/list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/profile")
    public String profile(Model model, Principal principal) {
        Member member = memberService.getMember(principal.getName());
        List<Post> myPosts = postService.getPostsByMember(member);
        List<PostComment> myComments = postCommentService.getCommentsByMember(member);
        Collections.reverse(myPosts);
        Collections.reverse(myComments);


        model.addAttribute("member", member);
        model.addAttribute("myPosts", myPosts);
        model.addAttribute("myComments", myComments);

        // 스튜디오 진입 시 로그인된 회원 스튜디외의 차단 상태 판단을 위해 작성
        Studio studio = this.studioService.getStudioByMemberUsername(principal.getName());


        if (studio != null) {
            // 회원별 플레이리스트가져오기(만들고 모델링해주고 푸터에서 넣어보자)
            Member member2 = memberService.getMember(principal.getName());
            Playlist playList = this.playlistService.getPlaylist(member2.getId());

            model.addAttribute("playList", playList);
        }


        return "post/post_profile";
    }
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/detail/{postId}/comment/delete/{commentId}")
    public String deleteComment(Principal principal, @PathVariable("postId") Long postId, @PathVariable("commentId") Long commentId) {
        // Implement authorization if needed, check if the user has rights to delete this comment
        postCommentService.delete(commentId);
        return "redirect:/post/detail/" + postId;
    }







}
