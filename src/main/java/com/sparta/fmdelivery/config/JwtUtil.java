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

    private static final String BEARER_PREFIX = "Bearer ";
    private static final long TOKEN_TIME = 180 * 60 * 1000L;

    @Value("${jwt.secret.key}")
    private String secretKey; // JWT를 서명할 때 사용하는 비밀키로, application.properties 같은 설정 파일에서 불러옵니다.
    private Key key; // secretKey 에서 생성된 서명 키. JWT 토큰을 서명하거나 검증하는 데 사용
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256; // 이는 비대칭 암호화 알고리즘으로, JWT의 무결성을 보장합니다.

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }


    public String createToken(Long userId, String email, UserRole userRole) {
        Date date = new Date();

        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(String.valueOf(userId))
                        .claim("email", email)
                        .claim("userRole", userRole)
                        .setExpiration(new Date(date.getTime() + TOKEN_TIME))
                        .setIssuedAt(date)
                        .signWith(key, signatureAlgorithm)
                        .compact();
    }

    public String substringToken(String tokenValue) {
        if (StringUtils.hasText(tokenValue) && tokenValue.startsWith(BEARER_PREFIX)) {
            return tokenValue.substring(7);
        }
        throw new ApiException(ErrorStatus._EXCEPTION_ERROR_TOKEN);
    }

    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
