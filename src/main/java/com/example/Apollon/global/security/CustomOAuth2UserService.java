package com.example.Apollon.global.security;
//
//import com.example.Apollon.domain.member.entity.Member;
//import com.example.Apollon.domain.member.service.MemberService;
//import lombok.RequiredArgsConstructor;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
//import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
//import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.List;
//import java.util.Map;
//
//@Service
//@Transactional(readOnly = true)
//@RequiredArgsConstructor
//public class CustomOAuth2UserService extends DefaultOAuth2UserService {
//    private static final Logger log = LoggerFactory.getLogger(CustomOAuth2UserService.class);
//    private final MemberService memberService;
//
//    @Override
//    @Transactional
//    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
//        OAuth2User oAuth2User = super.loadUser(userRequest);
//        String providerTypeCode = userRequest.getClientRegistration().getRegistrationId().toUpperCase();
//
//        if (providerTypeCode.equals("KAKAO")) {
//            log.info("카카오 로그인");
//            return processKakaoLogin(oAuth2User);
//        } else if (providerTypeCode.equals("NAVER")) {
//            log.info("네이버 로그인");
//            return processNaverLogin(oAuth2User);
//        }
//
//
//        // 다른 소셜 미디어 타입 처리 가능
//
//        return null;
//    }
//
//    private OAuth2User processKakaoLogin(OAuth2User oAuth2User) {
//        String oauthId = oAuth2User.getName();
//        Map<String, Object> attributes = oAuth2User.getAttributes();
//        Map<String, Object> attributesProperties = (Map<String, Object>) attributes.get("properties");
//        String nickname = (String) attributesProperties.get("nickname");
//        String providerTypeCode = "KAKAO";
//        String username = providerTypeCode + "__%s".formatted(oauthId);
//        Member member = memberService.whenSocialLogin(providerTypeCode, username, nickname);
//        List<GrantedAuthority> authorityList = new ArrayList<>();
//        return new CustomOAuth2User(member.getUsername(), member.getPassword(), authorityList);
//    }
//
//    private OAuth2User processNaverLogin(OAuth2User oAuth2User) {
//        // 네이버 로그인 처리 추가
//        // 네이버 사용자 정보 가져오기
//        String nickname = oAuth2User.getAttribute("nickname");
//        String providerTypeCode = "NAVER";
//        String oauthId = oAuth2User.getAttribute("id");
//        String username = providerTypeCode + "__%s".formatted(oauthId);
//        Member member = memberService.whenSocialLogin(providerTypeCode, username, nickname);
//        List<GrantedAuthority> authorityList = new ArrayList<>();
//        return new CustomOAuth2User(member.getUsername(), member.getPassword(), authorityList);
//    }
//
//
//    class CustomOAuth2User extends User implements OAuth2User {
//
//        public CustomOAuth2User(String username, String password, Collection<? extends GrantedAuthority> authorities) {
//            super(username, password, authorities);
//        }
//
//        @Override
//        public Map<String, Object> getAttributes() {
//            return null;
//        }
//
//        @Override
//        public String getName() {
//            return getUsername();
//        }
//    }
//}





import com.example.Apollon.domain.member.entity.Member;
import com.example.Apollon.domain.member.service.MemberService;
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
        } else if (providerTypeCode.equals("GOOGLE")) { // 수정된 부분
            log.info("구글 로그인"); // 수정된 부분
            return processGoogleLogin(oAuth2User); // 수정된 부분
        }

        // 다른 소셜 미디어 타입 처리 가능

        return null;
    }

    private OAuth2User processKakaoLogin(OAuth2User oAuth2User) {
        String oauthId = oAuth2User.getName();
        Map<String, Object> attributes = oAuth2User.getAttributes();
        Map<String, Object> attributesProperties = (Map<String, Object>) attributes.get("properties");
        String nickname = (String) attributesProperties.get("nickname");
        String providerTypeCode = "KAKAO";
        String username = providerTypeCode + "__%s".formatted(oauthId);
        Member member = memberService.whenSocialLogin(providerTypeCode, username, nickname);
        List<GrantedAuthority> authorityList = new ArrayList<>();
        return new CustomOAuth2User(member.getUsername(), member.getPassword(), authorityList);
    }

    private OAuth2User processNaverLogin(OAuth2User oAuth2User) {
        // 네이버 로그인 처리 추가
        // 네이버 사용자 정보 가져오기
        String nickname = oAuth2User.getAttribute("nickname");
        String providerTypeCode = "NAVER";
        String oauthId = oAuth2User.getAttribute("id");
        String username = providerTypeCode + "__%s".formatted(oauthId);
        Member member = memberService.whenSocialLogin(providerTypeCode, username, nickname);
        List<GrantedAuthority> authorityList = new ArrayList<>();
        return new CustomOAuth2User(member.getUsername(), member.getPassword(), authorityList);
    }

    private OAuth2User processGoogleLogin(OAuth2User oAuth2User) {
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String oauthId = (String) attributes.get("sub");
        String nickname = (String) attributes.get("name");
        String email = (String) attributes.get("email");
        String providerTypeCode = "GOOGLE";
        String username = providerTypeCode + "__%s".formatted(oauthId);
        Member member = memberService.whenSocialLogin(providerTypeCode, username, nickname);
        List<GrantedAuthority> authorityList = new ArrayList<>();
        return new CustomOAuth2User(member.getUsername(), member.getPassword(), authorityList);
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