package com.example.Apollon.domain.member.entity;


import com.example.Apollon.domain.music.entity.Music;
import com.example.Apollon.domain.studio.entity.Studio;
import com.example.Apollon.global.jpa.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
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

    public void setPassword(String password) {
        this.password = password;
    }

    // 이미지 필드의 setter 메서드 추가
    public void setImage(String image) {
        this.image = image;
    }
}