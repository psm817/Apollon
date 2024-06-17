package com.example.Apollon.domain.post.service;

import com.example.Apollon.domain.comment.entity.Comment;
import com.example.Apollon.domain.comment.repository.CommentRepository;
import com.example.Apollon.domain.member.entity.Member;
import com.example.Apollon.domain.post.entity.Post;
import com.example.Apollon.domain.post.entity.PostComment;
import com.example.Apollon.domain.post.repository.PostCommentRepository;
import com.example.Apollon.domain.studio.entity.Studio;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostCommentService {

    private final PostCommentRepository postCommentRepository;
    private final PostService postService;

    public List<PostComment> getPostCommentsByPost(Long postId) {
        Post post = postService.getPost(postId);
        return postCommentRepository.findByPost(post);
    }

    public void createPostComment(Long postId, String content, Member member) {
        Post post = postService.getPost(postId);
        PostComment postComment = PostComment.builder()
                .post(post)
                .content(content)
                .author(member)
                .createDate(LocalDateTime.now())
                .build();
        postCommentRepository.save(postComment);
    }
    public List<PostComment> getListByPost(Post post) {
        return postCommentRepository.findByPost(post);
    }

    public List<PostComment> getCommentsByMember(Member member) {
        return postCommentRepository.findByAuthor(member);
    }


}
