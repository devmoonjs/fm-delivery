package com.sparta.fmdelivery.domain.auth.service;

import com.sparta.fmdelivery.apipayload.status.ErrorStatus;
import com.sparta.fmdelivery.config.JwtUtil;
import com.sparta.fmdelivery.config.PasswordEncoder;
import com.sparta.fmdelivery.domain.auth.dto.request.LoginRequest;
import com.sparta.fmdelivery.domain.auth.dto.request.SignoutRequest;
import com.sparta.fmdelivery.domain.auth.dto.request.SignupRequest;
import com.sparta.fmdelivery.domain.auth.dto.response.SignResponse;
import com.sparta.fmdelivery.domain.user.entity.User;
import com.sparta.fmdelivery.domain.user.enums.UserRole;
import com.sparta.fmdelivery.domain.user.repository.UserRepository;
import com.sparta.fmdelivery.exception.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
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
            throw new ApiException(ErrorStatus._BAD_REQUEST_EMAIL);
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

    /**
     * 이메일로 사용자 조회: 입력된 이메일로 데이터베이스에서 사용자를 찾고, 없으면 예외를 발생시킵니다.
     * 비밀번호 확인: 입력된 비밀번호와 데이터베이스에 저장된 비밀번호를 비교해, 맞지 않으면 예외를 발생시킵니다.
     * JWT 토큰 생성: 사용자 정보로 JWT 토큰을 생성합니다.
     * 토큰 반환: 생성된 JWT 토큰을 클라이언트에 응답으로 반환합니다.
     * 이 코드는 로그인 요청 시 이메일과 비밀번호가 정확한지 확인한 후, 로그인 성공 시 JWT 토큰을 발급하여 클라이언트가 인증된 사용자로서 서비스를 이용할 수 있도록 합니다.
     * @param loginRequest
     * @return
     */

    @Transactional
    public SignResponse login(LoginRequest loginRequest) {

        User user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(
                () -> new ApiException(ErrorStatus._NOT_FOUND_USER)
        );

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new ApiException(ErrorStatus._BAD_REQUEST_PASSWORD);
        }

        String bearerToken = jwtUtil.createToken(user.getId(), user.getEmail(), user.getUserRole());

        return new SignResponse(bearerToken);
    }

    /**
     * 회원 탈퇴 메서드:
     * - 요청된 비밀번호가 일치하는지 확인한 후, 일치하면 회원 탈퇴를 처리합니다.
     * - 비밀번호가 일치하지 않으면 예외를 발생시킵니다.
     *
     * @param signoutRequest 비밀번호를 포함한 회원 탈퇴 요청 데이터
     * @param userId 현재 로그인한 사용자의 ID
     * @throws ApiException 가입되지 않은 유저인 경우 또는 비밀번호가 일치하지 않는 경우 발생
     *  *     - ErrorStatus._NOT_FOUND_USER: 가입되지 않은 유저인 경우
     *  *     - ErrorStatus._BAD_REQUEST_PASSWORD: 비밀번호가 일치하지 않는 경우
     */
    @Transactional
    public void signout(SignoutRequest signoutRequest, Long userId) {

        User user = userRepository.findById(userId).orElseThrow(
                () -> new ApiException(ErrorStatus._NOT_FOUND_USER)
        );

        if (!passwordEncoder.matches(signoutRequest.getPassword(), user.getPassword())) {
            throw new ApiException(ErrorStatus._BAD_REQUEST_PASSWORD);
        }

        userRepository.delete(user);
    }
}
