package com.example.Apollon.domain.studio.repository;

import com.example.Apollon.domain.studio.entity.Studio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudioRepository extends JpaRepository<Studio, Long> {
}
