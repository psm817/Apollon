package com.example.Apollon.domain.post.entity;

import com.example.Apollon.global.jpa.BaseEntity;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor
@ToString
public class Post extends BaseEntity {
    private String title;
    private String content;
}
