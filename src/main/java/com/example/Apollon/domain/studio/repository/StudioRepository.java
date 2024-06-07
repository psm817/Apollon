package com.example.Apollon.domain.studio.repository;

import com.example.Apollon.domain.member.entity.Member;
import com.example.Apollon.domain.studio.entity.Studio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudioRepository extends JpaRepository<Studio, Long> {
    Studio findByMemberUsername(String username);

    boolean existsByMember(Member member);

    Optional<Studio> findByMember(Member member);
}
