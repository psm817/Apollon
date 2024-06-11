package com.example.Apollon.domain.music.entity;

import com.example.Apollon.domain.member.entity.Member;
import com.example.Apollon.domain.playlist.entity.Playlist;
import com.example.Apollon.global.jpa.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

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
public class Music extends BaseEntity {
    @Setter
    @Getter
    private String musicTitle;
    @Getter
    @Setter
    private String musicContent;
    @Setter
    @Getter
    private String uploadStudio;
    @Getter
    @Setter
    private String thumbnailImg;
    @Getter
    @Setter
    private String musicMp3;
    @Setter
    @Getter
    private List<String> genres;

    @Setter
    @Getter
    @Transient
    private String thumbnailImgFullPath;

    @Setter
    @Getter
    @Transient
    private String musicMp3FullPath;

    @Getter
    private Long musicPlayCount = 0L;

    // 좋아요
    @ManyToMany
    Set<Member> musicLikers = new LinkedHashSet<>();

    @ManyToMany
    @JoinTable(
            name = "playlist_music",
            joinColumns = @JoinColumn(name = "music_id"),
            inverseJoinColumns = @JoinColumn(name = "playlist_id")
    )
    private Set<Playlist> playlists = new HashSet<>();

    public void setPlaylist(Playlist playlist) {
        playlists.add(playlist);
        playlist.getMusics().add(this);
    }

    public void addMusicLike(Member liker) {
        if (this.musicLikers == null) {
            this.musicLikers = new HashSet<>();
        }

        this.musicLikers.add(liker);
    }
}
