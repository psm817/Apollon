package com.example.Apollon.domain.member.entity;


import com.example.Apollon.domain.music.entity.Music;
import com.example.Apollon.domain.playlist.entity.Playlist;
import com.example.Apollon.domain.studio.entity.Studio;
import com.example.Apollon.global.jpa.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import java.util.List;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Member extends BaseEntity {
    @Comment("유저 아이디")
    @Column(unique = true)
    private String username;
    @Setter
    private String password;
    @Column(unique = false)
    private String nickname;
    private String email;

    @OneToOne(mappedBy = "member", cascade = CascadeType.REMOVE)
    @LazyCollection(LazyCollectionOption.EXTRA)
    private Studio studio;

    @Getter
    @Setter
    @OneToOne(mappedBy = "member", cascade = CascadeType.REMOVE)
    private Playlist playlist;

}