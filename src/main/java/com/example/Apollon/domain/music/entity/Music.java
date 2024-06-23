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

    @ElementCollection
    @CollectionTable(name = "music_genres", joinColumns = @JoinColumn(name = "music_id"))
    @Column(name = "genres")
    private List<String> genres;

    @OneToOne
    private Member member;

    @Setter
    @ManyToOne
    @JoinColumn(name = "playlist_id")
    private Playlist playlist;

    private LocalDateTime createDate;
    private LocalDateTime modifyDate;

    // 좋아요
    @ManyToMany
    Set<Member> musicLikers = new LinkedHashSet<>();

    @ManyToMany
    Set<Member> musicPlayCounts = new LinkedHashSet<>();

    public void addMusicLike(Member liker) {
        if (this.musicLikers == null) {
            this.musicLikers = new HashSet<>();
        }

        this.musicLikers.add(liker);
    }

    public void addMusicPlayCount(Member member) {
        if (this.musicPlayCounts == null) {
            this.musicPlayCounts = new HashSet<>();
        }

        this.musicPlayCounts.add(member);
    }
    public String getMusicMp3FullPath() {
        return "/uploadFile/uploadMusics/" + this.musicMp3;
    }
}