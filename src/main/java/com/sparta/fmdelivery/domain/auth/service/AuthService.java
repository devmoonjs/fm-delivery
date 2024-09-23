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
