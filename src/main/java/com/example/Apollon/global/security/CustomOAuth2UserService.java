package com.example.Apollon.global.security;
import com.example.Apollon.domain.member.entity.Member;
import com.example.Apollon.domain.member.service.MemberService;
import com.example.Apollon.domain.studio.service.StudioService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private static final Logger log = LoggerFactory.getLogger(CustomOAuth2UserService.class);
    private final MemberService memberService;
    private final StudioService studioService;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String providerTypeCode = userRequest.getClientRegistration().getRegistrationId().toUpperCase();

        if (providerTypeCode.equals("KAKAO")) {
            log.info("카카오 로그인");
            return processKakaoLogin(oAuth2User);
        } else if (providerTypeCode.equals("NAVER")) {
            log.info("네이버 로그인");
            return processNaverLogin(oAuth2User);
        } else if (providerTypeCode.equals("GOOGLE")) {
            log.info("구글 로그인");
            return processGoogleLogin(oAuth2User);
        }

        return null;
    }

    private OAuth2User processKakaoLogin(OAuth2User oAuth2User) {
        String oauthId = oAuth2User.getName();
        Map<String, Object> attributes = oAuth2User.getAttributes();
        Map<String, Object> attributesProperties = (Map<String, Object>) attributes.get("properties");
        String nickname = (String) attributesProperties.get("nickname");
        String providerTypeCode = "KAKAO";
        String username = providerTypeCode +"_"+nickname+ "__%s".formatted(oauthId);
        String email = (String) ((Map<String, Object>) attributes.get("kakao_account")).get("email");

        try {
            Member member = memberService.whenSocialLogin(providerTypeCode, username, nickname, email);
            List<GrantedAuthority> authorityList = new ArrayList<>();

            // 스튜디오 자동 생성 또는 업데이트
            this.studioService.createOrUpdate(member, 0, 1);

            return new CustomOAuth2User(member.getUsername(), member.getPassword(), authorityList);
        } catch (IllegalStateException e) {
            log.error("Email already exists: " + email, e);
            throw new OAuth2AuthenticationException("Email already exists");
        }
    }

    private OAuth2User processNaverLogin(OAuth2User oAuth2User) {
        String oauthId = oAuth2User.getAttribute("id");
        Map<String, Object> attributes = oAuth2User.getAttributes();
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");
        String nickname = (String) response.get("nickname");
        String email = (String) response.get("email");
        String providerTypeCode = "NAVER";
        String username = providerTypeCode +"_"+nickname+ "__%s".formatted(oauthId);

        try {
            Member member = memberService.whenSocialLogin(providerTypeCode, username, nickname, email);
            List<GrantedAuthority> authorityList = new ArrayList<>();

            // 스튜디오 자동 생성
            this.studioService.createOrUpdate(member, 0, 1);

            return new CustomOAuth2User(member.getUsername(), member.getPassword(), authorityList);
        } catch (IllegalStateException e) {
            log.error("Email already exists: " + email, e);
            throw new OAuth2AuthenticationException("Email already exists");
        }
    }

    private OAuth2User processGoogleLogin(OAuth2User oAuth2User) {
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String oauthId = (String) attributes.get("sub");
        String nickname = (String) attributes.get("name");
        String email = (String) attributes.get("email");
        String providerTypeCode = "GOOGLE";
        String username = providerTypeCode + "_"+nickname+"__%s".formatted(oauthId);

        try {
            Member member = memberService.whenSocialLogin(providerTypeCode, username, nickname, email);
            List<GrantedAuthority> authorityList = new ArrayList<>();

            // 스튜디오 자동 생성
            this.studioService.createOrUpdate(member, 0, 1);

            return new CustomOAuth2User(member.getUsername(), member.getPassword(), authorityList);
        } catch (IllegalStateException e) {
            log.error("Email already exists: " + email, e);
            throw new OAuth2AuthenticationException("Email already exists");
        }
    }

    class CustomOAuth2User extends User implements OAuth2User {

        public CustomOAuth2User(String username, String password, Collection<? extends GrantedAuthority> authorities) {
            super(username, password, authorities);
        }

        @Override
        public Map<String, Object> getAttributes() {
            return null;
        }

        @Override
        public String getName() {
            return getUsername();
        }
    }
}

