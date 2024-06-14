package com.example.Apollon.domain.playlist.entity;

import com.example.Apollon.domain.member.entity.Member;
import com.example.Apollon.domain.music.entity.Music;
import com.example.Apollon.global.jpa.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Playlist extends BaseEntity {
    public Long id;
    // 플레이리스트 주인(회원)
    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "playlist")
    private List<Music> musicPlayList = new ArrayList<>();

    public void addMusic(Music impMusic) {
        this.musicPlayList.add(impMusic);
        impMusic.setPlaylist(this);
    }

}
