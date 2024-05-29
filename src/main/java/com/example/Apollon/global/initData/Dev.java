package com.example.Apollon.global.initData;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("dev")
// 여기는 테스트 데이터 만드는 곳
// 시작되자마자 데이터 생성이 되고, 모두가 같은 조건의 데이터를 사용하기 위해서 TestApplication 대신 이곳에 테스트 데이터 생성하기
public class Dev {

}
