package com.example.Apollon.domain.post.repository;

import com.example.Apollon.domain.member.entity.Member;
import com.example.Apollon.domain.post.entity.BoardType;
import com.example.Apollon.domain.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findByBoardType(BoardType boardType, Pageable pageable);
    Page<Post> findByBoardTypeNot(BoardType boardType, Pageable pageable);
    Page<Post> findByBoardTypeAndBoardTypeNot(BoardType boardType, BoardType excludeBoardType, Pageable pageable);
    List<Post> findTop10ByOrderByViewDesc();
    List<Post> findByAuthor(Member member);
    List<Post> findTop5ByBoardTypeOrderByViewDesc(BoardType boardType);

}