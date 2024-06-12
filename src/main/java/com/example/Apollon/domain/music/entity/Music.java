package com.example.Apollon.domain.music.entity;

import com.example.Apollon.domain.member.entity.Member;
import com.example.Apollon.domain.playlist.entity.Playlist;
import com.example.Apollon.domain.studio.entity.Studio;
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
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Music extends BaseEntity {
    private String musicTitle;

    private String musicContent;

    @ManyToOne
    private Studio studio;

    private String thumbnailImg;

    private String musicMp3;

    private List<String> genres;

    @Transient
    private String thumbnailImgFullPath;

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
