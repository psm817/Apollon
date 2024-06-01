package com.example.Apollon.domain.music.entity;

import com.example.Apollon.domain.member.entity.Member;
import com.example.Apollon.global.jpa.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Music extends BaseEntity {
    private String musicTitle;
    private String musicContent;
    private String[] genres;
    private String uploadStudio;

    @Getter
    private Long musicPlayCount;

    @ManyToMany
    @JoinTable(
            name = "music_member_likes",
            joinColumns = @JoinColumn(name = "music_id"),
            inverseJoinColumns = @JoinColumn(name = "member_id")
    )
    private Set<Member> likedByMembers = new LinkedHashSet<>();
}
