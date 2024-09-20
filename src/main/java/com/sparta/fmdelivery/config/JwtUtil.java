package com.sparta.fmdelivery.config;

import com.sparta.fmdelivery.apipayload.status.ErrorStatus;
import com.sparta.fmdelivery.domain.user.enums.UserRole;
import com.sparta.fmdelivery.exception.ApiException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j(topic = "JwtUtil")
@Component
public class JwtUtil {

    /*
    BEARER_PREFIX: JWT 토큰 앞에 붙는 접두사(Bearer )입니다.
    클라이언트가 JWT를 보낼 때 Authorization 헤더에 "Bearer <토큰>" 형식으로 전송하기 때문에,
    이접두사를 관리하기 위해 사용
    */
    private static final String BEARER_PREFIX = "Bearer ";
    /*
    TOKEN_TIME: JWT 토큰의 유효 기간을 설정하는 상수로, 1시간(60분)을 밀리초로 표현한 값입니다.
    이 시간 동안만 토큰이 유효하며, 시간이 지나면 토큰은 만료됩니다.
     */
    private static final long TOKEN_TIME = 60 * 60 * 1000L;

    @Value("7Iqk7YyM66W07YOA7L2U65Sp7YG065+9U3ByaW5n6rCV7J2Y7Yqc7YSw7LWc7JuQ67mI7J6F64uI64ukLg==") // 설정 파일에서 jwt.secret.key라는 키에 해당하는 값을 secretKey라는 변수에 넣는 역할을 합니다.
    private String secretKey; // JWT를 서명할 때 사용하는 비밀키로, application.properties 같은 설정 파일에서 불러옵니다.
    private Key key; // secretKey 에서 생성된 서명 키. JWT 토큰을 서명하거나 검증하는 데 사용
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256; // 이는 비대칭 암호화 알고리즘으로, JWT의 무결성을 보장합니다.

    /*
    동작 원리 : secretKey는 기본적으로 Base64로 인코딩된 문자열이므로,
    이를 디코딩하여 key로 변환
    이렇게 생성된 key는 JWT를 생성하거나 검증할 때 사용
    */
    @PostConstruct // 객체가 생성되고 나서 자동으로 호출되는 메서드
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }


    public String createToken(Long userId, String email, UserRole userRole) { // 이 메서드는 JWT 토큰을 생성
        Date date = new Date(); // 위의 정보를 받아 JWT토큰을 생성, 현재 시간을 가져옴, 토큰 발행 및 만료 시간 설정에 사용

        return BEARER_PREFIX + // Bearer 접두사를 추가하여 반환, 클라이언트가 JWT를 보낼 때 사용되는 형식
                Jwts.builder() // JWT 빌더를 사용해 토큰을 구성하기 시작
                        .setSubject(String.valueOf(userId)) // 주체로 설정. 사용자의 고유 식별자 역할
                        .claim("email", email) // 이메일 정보를 토큰의 클레임에 담음
                        .claim("userRole", userRole)
                        .setExpiration(new Date(date.getTime() + TOKEN_TIME)) // 토큰의 만료 시간을 현재 시간에서 1시간 후로 설정
                        .setIssuedAt(date) // 토큰 발급 시간을 현재 시간으로 설정
                        .signWith(key, signatureAlgorithm) // 서명 키와 서명 알고리즘을 사용해 토큰에 서명 추가
                        .compact(); // JWT 토큰을 생성 후, String 형식으로 변환
    }

    public String substringToken(String tokenValue) { // 헤더로부터 받은 토큰에서 실제 JWT만 추출
        if (StringUtils.hasText(tokenValue) && tokenValue.startsWith(BEARER_PREFIX)) { // tokenValue가 비어 있지 않고 Bearer 접두사로 시작되는지 확인
            return tokenValue.substring(7); // Bearer 접두사를 제외하고 실제 JWT 토큰 부분만 반환
        }
        throw new ApiException(ErrorStatus._EXCEPTION_ERROR_TOKEN); // 만약 Bearer 접두사가 없으면 예외 발생
    }

    public Claims extractClaims(String token) { // JWT 토큰에서 클레임 정보를 추출
        return Jwts.parserBuilder() // 토큰을 파싱하기 위해 파서 생성
                .setSigningKey(key) // JWT 토큰을 검증할 때 사용하는 서명 키를 설정
                .build() // 파서 빌더 완성
                .parseClaimsJws(token) // 입력된 JWT 토큰을 파싱하여 유효성 검증
                .getBody(); // 토큰의 본문(Claims)을 반환. 여기에 userId, email, userRole 등의 정보가 들어 있다
    }
}
