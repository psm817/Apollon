package com.example.Apollon.domain.studio.service;

import com.example.Apollon.domain.member.entity.Member;
import com.example.Apollon.domain.member.repository.MemberRepository;
import com.example.Apollon.domain.studio.entity.Studio;
import com.example.Apollon.domain.studio.repository.StudioRepository;
import com.example.Apollon.global.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudioService {
    private final StudioRepository studioRepository;
    private final MemberRepository memberRepository;

    public Studio getStudio(Long id) {
        Optional<Studio> studio = this.studioRepository.findById(id);

        if(studio.isEmpty()) throw new DataNotFoundException("question not found");

        return studio.get();
    }

    public Studio create(Member member, Integer visit, Integer active) {
        Studio studio = Studio.builder()
                .member(member)
                .visit(visit)
                .active(active)
                .build();
        return this.studioRepository.save(studio);
    }

    public void like(Studio studio, Member member) {
        studio.addLike(member);

        this.studioRepository.save(studio);
    }

    public Studio getStudioByMemberUsername(String username) {
        return this.studioRepository.findByMemberUsername(username);
    }
}
