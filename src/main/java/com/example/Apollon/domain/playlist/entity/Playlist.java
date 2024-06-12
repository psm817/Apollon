package com.example.Apollon.domain.playlist.entity;

import com.example.Apollon.domain.member.entity.Member;
import com.example.Apollon.domain.music.entity.Music;
import com.example.Apollon.global.jpa.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Playlist extends BaseEntity {
    // 플레이리스트 주인(회원)
    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "playlist")
    private List<Music> musicPlayList;

    public void addMusic(Music newMusic) {
        this.musicPlayList.add(newMusic);
        newMusic.setPlaylist(this);
    }

    public Long getId() {
        return this.id;
    }
}
