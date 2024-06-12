package com.example.Apollon.domain.studio.entity;

import com.example.Apollon.domain.comment.entity.Comment;
import com.example.Apollon.domain.member.entity.Member;
import com.example.Apollon.domain.music.entity.Music;
import com.example.Apollon.global.jpa.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Studio extends BaseEntity {
    public long id;

    // 스튜디오 주인(회원)
    @OneToOne
    @JoinColumn(name = "member_id", unique = true)
    private Member member;

    // 스튜디오에 등록된 노래 리스트
    @OneToMany(mappedBy = "studio", cascade = CascadeType.REMOVE)
    @LazyCollection(LazyCollectionOption.EXTRA)
    private List<Music> musicList;

    // 좋아요
    @ManyToMany
    Set<Member> likers = new LinkedHashSet<>();

    // 방문자
    @Column(columnDefinition = "Integer default 0", nullable = false)
    private Integer visit = 0;

    // 활성화 상태
    // 기본값은 1(활성화), 0은 비활성화
    private Integer active = 1;

    private LocalDateTime createDate;
    private LocalDateTime modifyDate;

    // 방명록 가져오기
    @OneToMany(mappedBy = "studio", cascade = CascadeType.REMOVE)
    @LazyCollection(LazyCollectionOption.EXTRA)
    private List<Comment> commentList;

    public void addLike(Member liker) {
        if (this.likers == null) {
            this.likers = new HashSet<>();
        }

        this.likers.add(liker);
    }
}
