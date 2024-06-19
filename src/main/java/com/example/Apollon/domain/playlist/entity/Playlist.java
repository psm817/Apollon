package com.example.Apollon.domain.playlist.entity;

import com.example.Apollon.domain.member.entity.Member;
import com.example.Apollon.domain.music.entity.Music;
import com.example.Apollon.global.jpa.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@SuperBuilder
public class Playlist extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    // 플레이리스트 주인(회원)
    @OneToOne
    @JoinColumn(name = "member_id", unique = true)
    private Member member;

    private LocalDateTime createDate;
    private LocalDateTime modifyDate;

    @ManyToMany
    private List<Music> musicPlayList = new ArrayList<>();

    public void addMusic(Music music) {
        musicPlayList.add(music);
        music.setPlaylist(this);
    }
}
