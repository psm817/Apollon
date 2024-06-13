package com.example.Apollon.domain.post.service;

import com.example.Apollon.domain.member.entity.Member;
import com.example.Apollon.domain.post.entity.BoardType;
import com.example.Apollon.domain.post.entity.Post;
import com.example.Apollon.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public List<Post> getList() {
        return postRepository.findAll();
    }

    public void create(String title, String content, Member member, BoardType boardType) {
        Post post = Post.builder()
                .title(title)
                .content(content)
                .author(member)
                .createDate(LocalDateTime.now())
                .boardType(boardType)
                .hit(0)
                .build();
        postRepository.save(post);
    }

    public Post getPost(Long id) {
        Optional<Post> op = postRepository.findById(id);
        if (op.isPresent() == false) {
            throw new DateTimeException("post not found");
        }
        return op.get();
    }

    public Page<Post> getList(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return postRepository.findAll(pageable);
    }

    public Page<Post> getPostsByBoardType(int page, BoardType boardType) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return postRepository.findByBoardType(boardType, pageable);
    }
    public List<Post> getNoticePosts(int limit) {
        Pageable pageable = PageRequest.of(0, limit, Sort.by(Sort.Order.desc("createDate")));
        return postRepository.findByBoardType(BoardType.공지, pageable).getContent();
    }

    public void modify(Post post, String title, String content) {
        post.setTitle(title);
        post.setContent(content);
        post.setModifyDate(LocalDateTime.now());
        this.postRepository.save(post);
    }


}
