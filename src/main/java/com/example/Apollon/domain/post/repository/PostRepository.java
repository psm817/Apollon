package com.example.Apollon.domain.post.repository;

import com.example.Apollon.domain.post.entity.BoardType;
import com.example.Apollon.domain.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findAll(Pageable pageable);
    Page<Post> findByBoardType(BoardType boardType, Pageable pageable);

    List<Post> findTop5ByBoardTypeOrderByViewDesc(BoardType boardType);
}