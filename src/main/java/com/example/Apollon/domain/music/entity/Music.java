package com.example.Apollon.domain.music.entity;

import com.example.Apollon.domain.member.entity.Member;
import com.example.Apollon.domain.playlist.entity.Playlist;
import com.example.Apollon.domain.studio.entity.Studio;
import com.example.Apollon.global.jpa.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

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
public class Music extends BaseEntity {
    public Long id;

    private String musicTitle;

    private String musicContent;

    @ManyToOne
    private Studio studio;

    private String thumbnailImg;

    private String musicMp3;

    private List<String> genres;


    private Long musicPlayCount = 0L;

    @OneToOne
    private Member member;

    @ManyToOne
    private Playlist playlist;

    private LocalDateTime createDate;
    private LocalDateTime modifyDate;

    // 좋아요
    @ManyToMany
    Set<Member> musicLikers = new LinkedHashSet<>();


    public void addMusicLike(Member liker) {
        if (this.musicLikers == null) {
            this.musicLikers = new HashSet<>();
        }

        this.musicLikers.add(liker);
    }
    public String getMusicMp3FullPath() {
        return "/uploadFile/uploadMusics/" + this.musicMp3;
    }
}
