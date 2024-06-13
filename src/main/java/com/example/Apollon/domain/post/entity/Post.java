package com.example.Apollon.domain.post.entity;

import com.example.Apollon.domain.member.entity.Member;
import com.example.Apollon.global.jpa.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;


import java.time.LocalDateTime;
import java.util.List;


@Entity
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Post extends BaseEntity {
    private String title;
    private String content;
    private String writer;

    @CreatedDate
    private LocalDateTime createDate;

    private long hit;

    @ManyToOne
    private Member author;

    @Enumerated(EnumType.STRING)
    private BoardType boardType;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<PostComment> postCommentList;



}