package com.sparta.fmdelivery.domain.auth.service;

import com.sparta.fmdelivery.config.JwtUtil;
import com.sparta.fmdelivery.config.PasswordEncoder;
import com.sparta.fmdelivery.domain.auth.dto.request.SignupRequest;
import com.sparta.fmdelivery.domain.auth.dto.response.SignResponse;
import com.sparta.fmdelivery.domain.common.exception.InvalidRequestException;
import com.sparta.fmdelivery.domain.user.entity.User;
import com.sparta.fmdelivery.domain.user.enums.UserRole;
import com.sparta.fmdelivery.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@RequestMapping("/api")
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    /**
     *
     * 비밀번호 암호화: 입력된 비밀번호를 안전하게 인코딩합니다.
     * 사용자 역할 설정: 요청에서 받은 사용자 역할을 설정합니다.
     * 이메일 중복 확인: 이미 존재하는 이메일인지 확인하고, 중복되면 예외를 발생시킵니다.
     * 새 사용자 생성: 사용자의 정보를 바탕으로 새 User 객체를 만듭니다.
     * 사용자 저장: 생성된 사용자를 데이터베이스에 저장합니다.
     * JWT 토큰 생성: 저장된 사용자 정보를 기반으로 JWT 토큰을 생성합니다.
     * 응답 반환: 생성된 JWT 토큰을 응답으로 반환합니다.
     *
     * @param signupRequest
     * @return
     */
    @Transactional
    public SignResponse signup(SignupRequest signupRequest) {

        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new InvalidRequestException("이미 존재하는 이메일입니다.");
        }

        String encodedPassword = passwordEncoder.encode(signupRequest.getPassword());
        UserRole userRole = UserRole.of(signupRequest.getUserRole());

        User newUser = new User(
                signupRequest.getEmail(),
                encodedPassword,
                userRole
        );

        User savedUser = userRepository.save(newUser);
        String bearerToken = jwtUtil.createToken(savedUser.getId(), savedUser.getEmail(), userRole);

        return new SignResponse(bearerToken);
    }


}
