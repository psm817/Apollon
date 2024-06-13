package com.example.Apollon.domain.comment.controller;

import com.example.Apollon.domain.comment.entity.Comment;
import com.example.Apollon.domain.comment.form.CommentForm;
import com.example.Apollon.domain.comment.service.CommentService;
import com.example.Apollon.domain.member.entity.Member;
import com.example.Apollon.domain.member.service.MemberService;
import com.example.Apollon.domain.music.entity.Music;
import com.example.Apollon.domain.music.service.MusicService;
import com.example.Apollon.domain.studio.entity.Studio;
import com.example.Apollon.domain.studio.service.StudioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class CommentController {
    private final StudioService studioService;
    private final MemberService memberService;
    private final CommentService commentService;
    private final MusicService musicService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/studio/{username}/comment/create/{writer}")
    public String createComment(Model model,
                                @PathVariable("username") String username,
                                @PathVariable("writer") String writer,
                                Principal principal,
                                @Valid CommentForm commentForm)
    {
        Member member = this.memberService.getMember(writer);
        Studio studio = this.studioService.getStudioByMemberUsername(username);

        this.commentService.create(member, studio, commentForm.getTitle(), commentForm.getContent());

        return "redirect:/studio/%s".formatted(studio.getMember().getUsername());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/studio/{username}/comment/create/{writer}")
    public String createComment(CommentForm commentForm,
                                Model model,
                                @PathVariable("username") String username,
                                Principal principal,
                                @RequestParam(value= "kw", defaultValue = "") String kw,
                                @PathVariable("writer") String writer)
    {
        Studio studio = this.studioService.getStudioByMemberUsername(username);
        List<Music> musicList = this.musicService.getListByStudio(studio);

        if (studio != null) {
            model.addAttribute("studio", studio);
            model.addAttribute("kw", kw);
            model.addAttribute("musicList", musicList);
        }

        return "comment/comment_form";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/studio/{username}/comment/detail/{id}")
    public String detailComment(Model model,
                                @PathVariable("username") String username,
                                @PathVariable("id") long id,
                                Principal principal)
    {
        Studio studio = this.studioService.getStudioByMemberUsername(username);
        Comment comment = this.commentService.getComment(id);
        List<Music> musicList = this.musicService.getListByStudio(studio);

        if(studio != null) {
            model.addAttribute("studio", studio);
            model.addAttribute("comment", comment);
            model.addAttribute("musicList", musicList);
        }

        return "comment/comment_detail";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/studio/{username}/comment/delete")
    public String deleteComment(@PathVariable("username") String username,
                                @RequestParam("commentIds") List<Long> commentIds,
                                Principal principal) {
        String loginedUsername = principal.getName();

        for (Long commentId : commentIds) {
            Comment comment = this.commentService.getComment(commentId);

            if (comment != null) {
                this.commentService.delete(comment);
            }
        }

        Studio studio = this.studioService.getStudioByMemberUsername(username);

        return "redirect:/studio/%s".formatted(studio.getMember().getUsername());
    }
}
