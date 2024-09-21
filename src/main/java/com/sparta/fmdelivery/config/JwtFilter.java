package com.sparta.fmdelivery.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.fmdelivery.apipayload.ApiResponse;
import com.sparta.fmdelivery.apipayload.status.ErrorStatus;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter implements Filter {

    private final JwtUtil jwtUtil;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException { // 필터 초기화 메서드
        Filter.super.init(filterConfig); // 특별한 초기화 작업이 없으므로 부모 클래스의 기본 동작을 호출
    }

    // 필터가 요청을 처리할 때 실행되는 메서드. 요청과 응답을 필터 체인으로 넘기기전에 JWT 검증을 수행
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 요청과 응답 객체를 HTTP 요청과 응답으로 캐스팅하여 더 구체적으로 다룸
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String url = httpRequest.getRequestURI(); // 현재 요청된 URL 경로를 가져옴

        if (url.startsWith("/api/v1/auth")) {
            chain.doFilter(request, response);
            return;
        }
        /*
        만약 URL이 /auth로 시작하면, 인증이 필요 없는 경로로 간주하고 필터 체인을 그대로 진행
         */

        // 클라이언트가 보낸 Authorization 헤더에서 JWT 토큰을 가져옴
        String bearerJwt = httpRequest.getHeader("Authorization");
        if (bearerJwt == null) {
            jwtExceptionHandler(httpResponse, ErrorStatus._NOT_FOUND_TOKEN);
            return;
        }
        // JWT 토큰이 없으면, 400에러 반환

        String jwt = jwtUtil.substringToken(bearerJwt); // Bearer 접두사를 제거하고 실제 JWT 토큰만 추출

        // JWT 토큰을 파싱하여 클레임 정보를 추출. 클레임은 JWT에 담긴 사용자 정보
        try {
            Claims claims = jwtUtil.extractClaims(jwt);
            if (claims == null) {
                jwtExceptionHandler(httpResponse, ErrorStatus._BAD_REQUEST_TOKEN);
                return;
            }
            // 클레임 정보가 없거나 잘못된 경우, 400 에러 반환

            // 사용자 ID, 이메일, 역할 정보를 HTTP 요청 객체에 속성으로 설정하여 이후 로직에서 참조할 수 있게 만듬
            httpRequest.setAttribute("userId", Long.parseLong(claims.getSubject()));
            httpRequest.setAttribute("email", claims.get("email"));
            httpRequest.setAttribute("userRole", claims.get("userRole"));

            chain.doFilter(request, response); // 인증 성공 시 다음 필터로

        } catch (SecurityException | MalformedJwtException e) {
            log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.", e);
            jwtExceptionHandler(httpResponse, ErrorStatus._INVALID_TOKEN);
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token, 지원되지 않는 JWT 토큰 입니다.", e);
            jwtExceptionHandler(httpResponse, ErrorStatus._EXPIRED_TOKEN);
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.", e);
            jwtExceptionHandler(httpResponse, ErrorStatus._UNSUPPORTED_TOKEN);
        } catch (Exception e) {
            log.error("Invalid JWT token, 유효하지 않는 JWT 토큰 입니다.", e);
            jwtExceptionHandler(httpResponse, ErrorStatus._EXCEPTION_ERROR_TOKEN);
        }
    }

    // 필터 종료 시 호출되는 메서드, 특별한 작업 없이 기본 종료 동작을 호출
    @Override
    public void destroy() {
        Filter.super.destroy();
    }

    public void jwtExceptionHandler(HttpServletResponse response, ErrorStatus errorStatus) {
        ObjectMapper mapper = new ObjectMapper();
        response.setStatus(Integer.parseInt(errorStatus.getStatusCode()));
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            ApiResponse<String> responseMessage = ApiResponse.onFailure(errorStatus);
            response.getWriter().write(mapper.writeValueAsString(responseMessage));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
