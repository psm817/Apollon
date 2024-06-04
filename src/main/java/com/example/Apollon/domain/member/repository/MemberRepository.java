package com.example.Apollon.domain.member.repository;

import com.example.Apollon.domain.member.entity.Member;
import com.example.Apollon.domain.studio.entity.Studio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByUsername(String username);
    Optional<Member> findByEmail(String email);
    boolean existsByUsername(String username); // 중복된 아이디 확인 메서드 추가

    Optional<Member> findById(Long memberId);
}