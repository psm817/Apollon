package com.example.Apollon.domain.comment.service;

import com.example.Apollon.domain.comment.entity.Comment;
import com.example.Apollon.domain.comment.repository.CommentRepository;
import com.example.Apollon.domain.member.entity.Member;
import com.example.Apollon.domain.studio.entity.Studio;
import groovy.util.ConfigObject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    public void create(Member member, Studio studio, String title, String content) {
        Comment comment = Comment.builder()
                .member(member)
                .studio(studio)
                .title(title)
                .content(content)
                .build();

        this.commentRepository.save(comment);
    }

    public List<Comment> getList() {
        return commentRepository.findAll();
    }

    public List<Comment> getListByStudio(Studio studio) {
        return commentRepository.findByStudio(studio);
    }

    public Comment getComment(long id) {
        Optional<Comment> oc = this.commentRepository.findById(id);

        if ( oc.isPresent() == false ) throw new DateTimeException("post not found");

        return oc.get();
    }

    public void delete(Comment comment) {
        this.commentRepository.delete(comment);
    }
}
