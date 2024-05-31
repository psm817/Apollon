package com.example.Apollon.global.jpa;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass       // 이벤트 발생했을 때 특정 동작 수행
@EntityListeners(AuditingEntityListener.class)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BaseEntity {
    // 전체 DB 테이블(엔티티)에 포함된 id, createDate, modifyDate의 어노테이션을 관리하는 곳이라고 생각하면 좋음
    // 각자 엔티티 만들 때, id, createDate, modifyDate는 따로 적용할 필요가 없음
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @CreatedDate
    private LocalDateTime createDate;

    @LastModifiedDate
    private LocalDateTime modifyDate;

}
