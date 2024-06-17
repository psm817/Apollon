package com.example.Apollon.domain.post.repository;

import com.example.Apollon.domain.member.entity.Member;
import com.example.Apollon.domain.post.entity.Post;
import com.example.Apollon.domain.post.entity.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostCommentRepository extends JpaRepository<PostComment, Long> {
    List<PostComment> findByPost(Post post);
    List<PostComment> findByAuthor(Member author);
}
