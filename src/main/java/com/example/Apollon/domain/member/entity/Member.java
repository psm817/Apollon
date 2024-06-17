package com.example.Apollon.domain.member.entity;


import com.example.Apollon.domain.comment.entity.Comment;
import com.example.Apollon.domain.post.entity.Post;
import com.example.Apollon.domain.post.entity.PostComment;
import com.example.Apollon.domain.studio.entity.Studio;
import com.example.Apollon.global.jpa.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Member extends BaseEntity {

    public long id;

    @Column(unique = true)
    private String username;
    @Setter
    private String password;
    @Column(unique = false)
    private String nickname;
    private String email;

    @Column(unique = false)
    private String image; // 수정된 부분: 이미지 파일 이름을 저장하는 필드

    private LocalDateTime createDate;
    private LocalDateTime modifyDate;

    @OneToOne(mappedBy = "member", cascade = CascadeType.REMOVE)
    @LazyCollection(LazyCollectionOption.EXTRA)
    private Studio studio;

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    @LazyCollection(LazyCollectionOption.EXTRA)
    private List<Comment> commentList;

    public void setPassword(String password) {
        this.password = password;
    }

    // 이미지 필드의 setter 메서드 추가
    public void setImage(String image) {
        this.image = image;
    }
    //커뮤니티 게시글이랑 댓글
    @OneToMany(mappedBy = "author", cascade = CascadeType.REMOVE)
    @LazyCollection(LazyCollectionOption.EXTRA)
    private List<Post> posts;

    @OneToMany(mappedBy = "author", cascade = CascadeType.REMOVE)
    @LazyCollection(LazyCollectionOption.EXTRA)
    private List<PostComment> postComments;
}