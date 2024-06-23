package com.example.Apollon.domain.post.entity;

import com.example.Apollon.domain.member.entity.Member;
import com.example.Apollon.global.jpa.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;


import java.time.LocalDateTime;
import java.util.List;


@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Post extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;


    @CreatedDate
    private LocalDateTime createDate;

    @LastModifiedDate
    private LocalDateTime modifyDate;

    @Column(nullable = false)
    private int view = 0; // 초기값 설정F

    @ManyToOne
    private Member author;

    @Enumerated(EnumType.STRING)
    private BoardType boardType;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<PostComment> postCommentList;

    // 조회수 증가 메서드
    public void increaseView() {
        this.view++;
    }
}