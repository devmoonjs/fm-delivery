package com.sparta.fmdelivery.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.fmdelivery.apipayload.ApiResponse;
import com.sparta.fmdelivery.apipayload.status.ErrorStatus;
import com.sparta.fmdelivery.domain.user.enums.UserRole;
import io.jsonwebtoken.*;
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
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String url = httpRequest.getRequestURI();

        if (url.startsWith("/api/v1/auth")) {
            chain.doFilter(request, response);
            return;
        }

        if (url.startsWith("/api/v1/admin")) {
            String bearerJwt = getBarerJwtFromHeader(httpRequest, httpResponse);
            if (bearerJwt == null) {
                jwtExceptionHandler(httpResponse, ErrorStatus._NOT_FOUND_TOKEN);
                return;
            }

            String jwt = jwtUtil.substringToken(bearerJwt);

            Claims claims;
            try {
                claims = jwtUtil.extractClaims(jwt);
            } catch (ExpiredJwtException e) {
                jwtExceptionHandler(httpResponse, ErrorStatus._EXPIRED_TOKEN);
                return;
            } catch (JwtException e) {
                jwtExceptionHandler(httpResponse, ErrorStatus._INVALID_TOKEN);
                return;
            }

            if (claims == null) {
                jwtExceptionHandler(httpResponse, ErrorStatus._INVALID_TOKEN);
                return;
            }

            String userRole = (String) claims.get("userRole");

            if (userRole == null || !UserRole.valueOf(userRole).equals(UserRole.ADMIN)) {
                jwtExceptionHandler(httpResponse, ErrorStatus._FORBIDDEN);
                return;
            }
        }

        String bearerJwt = getBarerJwtFromHeader(httpRequest, httpResponse);
        if (bearerJwt == null) return;

        String jwt = jwtUtil.substringToken(bearerJwt); // Bearer 접두사를 제거하고 실제 JWT 토큰만 추출

        try {
            Claims claims = jwtUtil.extractClaims(jwt);
            if (claims == null) {
                jwtExceptionHandler(httpResponse, ErrorStatus._BAD_REQUEST_TOKEN);
                return;
            }

            httpRequest.setAttribute("userId", Long.parseLong(claims.getSubject()));
            httpRequest.setAttribute("email", claims.get("email"));
            httpRequest.setAttribute("userRole", claims.get("userRole"));

            chain.doFilter(request, response);

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

    private String getBarerJwtFromHeader(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        String bearerJwt = httpRequest.getHeader("Authorization");
        if (bearerJwt == null) {
            jwtExceptionHandler(httpResponse, ErrorStatus._NOT_FOUND_TOKEN);
            return null;
        }
        return bearerJwt;
    }

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
