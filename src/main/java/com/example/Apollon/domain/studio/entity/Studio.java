package com.example.Apollon.domain.studio.entity;

import com.example.Apollon.domain.member.entity.Member;
import com.example.Apollon.domain.music.entity.Music;
import com.example.Apollon.global.jpa.BaseEntity;
import jakarta.persistence.*;
import com.example.Apollon.global.jpa.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Studio extends BaseEntity {
    // 스튜디오 주인(회원)
    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    // 스튜디오에 등록된 노래 리스트
//    @OneToMany(mappedBy = "studio", cascade = CascadeType.REMOVE)
//    @LazyCollection(LazyCollectionOption.EXTRA)
//    private List<Music> musicList;

    // 좋아요
    @ManyToMany
    Set<Member> likers = new LinkedHashSet<>();

    // 방문자
    @Column(columnDefinition = "Integer default 0", nullable = false)
    private Integer visit;

    // 활성화 상태
    private Integer active;

    public void addLike(Member liker) {
        if (this.likers == null) {
            this.likers = new HashSet<>();
        }

        this.likers.add(liker);
    }
}
