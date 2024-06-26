package com.example.Apollon.domain.playlist.repository;

import com.example.Apollon.domain.member.entity.Member;
import com.example.Apollon.domain.playlist.entity.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long> {

    Optional<Playlist> findByMember(Member member);

    Playlist findByMemberId(long memberId);

    List<Playlist> findByMember(Optional<Member> member);
}
