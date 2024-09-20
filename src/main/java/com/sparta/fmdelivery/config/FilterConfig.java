package com.sparta.fmdelivery.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
@RequiredArgsConstructor
public class FilterConfig { // 스프링에서 필터 설정을 담당하는 설정 클래스

    private final JwtUtil jwtUtil;

    // 필터를 등록하는 메서드. 메서드에서 반환되는 객체가 스프링에 필터로 등록
//    @Bean
    public FilterRegistrationBean<JwtFilter> jwtFilter() {

        // FilterRegistrationBean 객체를 생성하여, 필터를 설정할 준비를 함
        // 이 객체는 필터와 관련된 설정을 관리
        FilterRegistrationBean<JwtFilter> registrationBean = new FilterRegistrationBean<>();
        /*
        JwtFilter 객체를 생성하여 필터로 설정
        jwtUtil 객체를 주입받아 JWT 토큰을 검증하는 역할
         */
        registrationBean.setFilter(new JwtFilter(jwtUtil));
        /*
        필터가 적용될 URL 패턴을 설정
        "/*"는 모든 경로에 대해 이 필터가 적용된다는 의미
        즉, 모든 요청에 대해 JWT 검증을 하도록 설정합니다
         */
        registrationBean.addUrlPatterns("/*");

        return registrationBean; // 이 메서드는 설정된 필터를 반환
    }
}
