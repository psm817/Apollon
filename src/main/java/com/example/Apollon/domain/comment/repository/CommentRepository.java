package com.example.Apollon.domain.comment.repository;

import com.example.Apollon.domain.comment.entity.Comment;
import com.example.Apollon.domain.studio.entity.Studio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByStudio(Studio studio);
}
