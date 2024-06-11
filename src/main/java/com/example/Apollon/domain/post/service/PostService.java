package com.example.Apollon.domain.post.service;

import com.example.Apollon.domain.member.entity.Member;
import com.example.Apollon.domain.member.service.MemberService;
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

import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    public List<Post> getList() {
        return postRepository.findAll();
    }

    public void create(String title, String content, Member member) {
        Post post = Post.builder()
                .title(title)
                .content(content)
                .author(member)
                .createDate(LocalDateTime.now())
                .hit(0)
                .build();
        postRepository.save(post);
    }


    public Post getPost(Long id) {
        Optional<Post> op = postRepository.findById(id);
        if ( op.isPresent() == false ) {throw new DateTimeException("post not found");}

        return op.get();
    }

    public Page<Post> getList(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return this.postRepository.findAll(pageable);
    }

}