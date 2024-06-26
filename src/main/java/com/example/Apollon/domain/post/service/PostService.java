package com.example.Apollon.domain.post.service;

import com.example.Apollon.domain.member.entity.Member;
import com.example.Apollon.domain.post.entity.BoardType;
import com.example.Apollon.domain.post.entity.Post;
import com.example.Apollon.domain.post.repository.PostRepository;
import com.example.Apollon.global.DataNotFoundException;
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
                .view(0)
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

    public Page<Post> getListExcludeNotice(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return postRepository.findByBoardTypeNot(BoardType.공지, pageable);
    }

    public Page<Post> getPostsByBoardType(int page, BoardType boardType) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return postRepository.findByBoardType(boardType, pageable);
    }

    public Page<Post> getPostsByBoardTypeExcludeNotice(int page, BoardType boardType) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return postRepository.findByBoardTypeAndBoardTypeNot(boardType, BoardType.공지, pageable);
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

    public void delete(Post post) {
        this.postRepository.delete(post);
    }

    public Post getPostByView(Long id) {
        Optional<Post> postOptional = postRepository.findById(id);
        if (postOptional.isPresent()) {
            Post post = postOptional.get();
            post.increaseView(); // 조회수 증가
            postRepository.save(post); // 변경된 Post 객체 저장
            return post; // 조회수가 증가된 Post 객체 반환
        } else {
            throw new DataNotFoundException("Post not found with id: " + id);
        }
    }


    public List<Post> getTop10Posts() {
        return postRepository.findTop10ByOrderByViewDesc();
    }

    public List<Post> getPostsByMember(Member member) {
        return postRepository.findByAuthor(member);
    }

    public List<Post> getTop5PostsByViewAndBoardType(BoardType boardType) {
        return this.postRepository.findTop5ByBoardTypeOrderByViewDesc(boardType);

    }

    public Post getNextPostByBoardType(Long currentPostId, BoardType boardType) {
        Pageable pageable = PageRequest.of(0, 1, Sort.by(Sort.Direction.ASC, "id"));
        List<Post> nextPosts = postRepository.findByBoardTypeAndIdGreaterThanOrderById(boardType, currentPostId, pageable).getContent();
        if (nextPosts.isEmpty()) {
            throw new DataNotFoundException("No next post found");
        }
        return nextPosts.get(0);
    }


    public Post getPreviousPostByBoardType(Long currentPostId, BoardType boardType) {
        Pageable pageable = PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, "id"));
        List<Post> previousPosts = postRepository.findByBoardTypeAndIdLessThanOrderByIdDesc(boardType, currentPostId, pageable).getContent();
        if (previousPosts.isEmpty()) {
            throw new DataNotFoundException("No previous post found");
        }
        return previousPosts.get(0);
    }
}
