package com.example.Apollon.domain.studio.service;

import com.example.Apollon.domain.member.entity.Member;
import com.example.Apollon.domain.member.repository.MemberRepository;
import com.example.Apollon.domain.studio.entity.Studio;
import com.example.Apollon.domain.studio.repository.StudioRepository;
import com.example.Apollon.global.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudioService {
    private final StudioRepository studioRepository;
    private final MemberRepository memberRepository;

    public Studio getStudio(Long id) {
        Optional<Studio> studio = this.studioRepository.findById(id);

        if(studio.isEmpty()) {
            throw new DataNotFoundException("question not found");
        }

        return studio.get();
    }

    @Transactional
    public Studio createOrUpdate(Member member, Integer visit, Integer active) {
        Optional<Studio> existingStudio = studioRepository.findByMember(member);

        Studio studio;

        if(existingStudio.isPresent()) {
            studio = existingStudio.get();
            studio.setModifyDate(LocalDateTime.now());
        }
        else {
            studio = Studio.builder()
                    .member(member)
                    .visit(visit)
                    .active(active)
                    .createDate(LocalDateTime.now())
                    .build();
        }

        return this.studioRepository.save(studio);
    }

    public void like(Studio studio, Member member) {
        studio.addLike(member);

        this.studioRepository.save(studio);
    }

    public Studio getStudioByMemberUsername(String username) {
        return this.studioRepository.findByMemberUsername(username);
    }

    @Transactional
    public void incrementVisit(String username, String loginedUsername) {
        Studio studio = this.studioRepository.findByMemberUsername(username);

        if(studio != null && !studio.getMember().getUsername().equals(loginedUsername)) {
            studio.setVisit(studio.getVisit() + 1);
            this.studioRepository.save(studio);
        }
    }

    public void changeInActive(Studio studio, Integer active) {
        studio.setActive(active);

        this.studioRepository.save(studio);
    }

    public void changeActive(Studio studio, Integer active) {
        studio.setActive(active);

        this.studioRepository.save(studio);
    }

    public List<Studio> getAllStudio() {
        return this.studioRepository.findAll();
    }
}
