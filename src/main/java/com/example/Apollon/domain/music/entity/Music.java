package com.example.Apollon.domain.music.entity;

import com.example.Apollon.global.jpa.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
public class Music extends BaseEntity {

    private String title;
    private String user;

}
