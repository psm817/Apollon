package com.example.Apollon.domain.member.entity;


import com.example.Apollon.domain.playlist.entity.Playlist;
import com.example.Apollon.domain.studio.entity.Studio;
import com.example.Apollon.global.jpa.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

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
    private String password;
    @Column(unique = false)
    private String nickname;
    private String email;
    @Column(unique = false)
    private String image; // 수정된 부분: 이미지 파일 이름을 저장하는 필드


    @OneToOne(mappedBy = "member", cascade = CascadeType.REMOVE)
    @LazyCollection(LazyCollectionOption.EXTRA)
    private Studio studio;

}