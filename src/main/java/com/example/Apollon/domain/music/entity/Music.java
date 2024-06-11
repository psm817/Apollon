package com.example.Apollon.domain.music.entity;

import com.example.Apollon.domain.member.entity.Member;
import com.example.Apollon.domain.playlist.entity.Playlist;
import com.example.Apollon.global.jpa.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.LinkedHashSet;
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
    private String[] genres;

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

    @ManyToMany
    @JoinTable(
            name = "playlist_music",
            joinColumns = @JoinColumn(name = "music_id"),
            inverseJoinColumns = @JoinColumn(name = "playlist_id")
    )
    private Set<Playlist> playlists = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "music_member_likes",
            joinColumns = @JoinColumn(name = "music_id"),
            inverseJoinColumns = @JoinColumn(name = "member_id")
    )
    private Set<Member> likedByMembers = new LinkedHashSet<>();

    public void addLikedByMembers(Member liker) {
        if (this.likedByMembers == null) {
            this.likedByMembers = new HashSet<>();
        }

        this.likedByMembers.add(liker);
    }
    public void setPlaylist(Playlist playlist) {
        playlists.add(playlist);
        playlist.getMusics().add(this);
    }

}
