package com.example.Apollon.domain.studio.entity;

import com.example.Apollon.global.jpa.BaseEntity;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Studio extends BaseEntity {
    private long id;
    private LocalDateTime createDate;
    private LocalDateTime modifyDate;
}
