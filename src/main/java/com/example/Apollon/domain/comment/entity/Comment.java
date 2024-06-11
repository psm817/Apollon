package com.example.Apollon.domain.comment.entity;

import com.example.Apollon.domain.member.entity.Member;
import com.example.Apollon.domain.studio.entity.Studio;
import com.example.Apollon.global.jpa.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Comment extends BaseEntity {
    private String title;
    private String content;

    // 방명록을 남긴 회원
    @ManyToOne
    private Member member;

    // 방명록을 남길 스튜디오
    @ManyToOne
    private Studio studio;
}
