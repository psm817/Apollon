package com.example.Apollon.domain.post.service;

import com.example.Apollon.domain.post.entity.Post;
import com.example.Apollon.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    public List<Post> getList() {
        return postRepository.findAll();
    }

    public void create( String title, String content) {
        Post post = Post.builder()
                .title(title)
                .content(content)
                .createDate(LocalDateTime.now())
                .build();
        postRepository.save(post);
    }
}
