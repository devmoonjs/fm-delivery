package com.sparta.fmdelivery.domain.auth;

import com.sparta.fmdelivery.apipayload.ApiResponse;
import com.sparta.fmdelivery.apipayload.status.ErrorStatus;
import com.sparta.fmdelivery.config.JwtUtil;
import com.sparta.fmdelivery.config.PasswordEncoder;
import com.sparta.fmdelivery.domain.auth.dto.request.LoginRequest;
import com.sparta.fmdelivery.domain.auth.dto.request.SignupRequest;
import com.sparta.fmdelivery.domain.auth.dto.response.SignResponse;
import com.sparta.fmdelivery.domain.auth.service.AuthService;
import com.sparta.fmdelivery.domain.user.entity.User;
import com.sparta.fmdelivery.domain.user.enums.UserRole;
import com.sparta.fmdelivery.domain.user.repository.UserRepository;
import com.sparta.fmdelivery.exception.ApiException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtUtil jwtUtil;
    @InjectMocks
    private AuthService authService;

    @Test
    void 회원가입_성공() {
        // given
        SignupRequest signupRequest = new SignupRequest("test@example.com", "password", "USER");
        UserRole userRole = UserRole.USER;
        User realUser = new User(signupRequest.getEmail(), "encodedPassword", userRole);
        User spyUser = spy(realUser);

        doReturn(1L).when(spyUser).getId();

        given(userRepository.existsByEmail(signupRequest.getEmail())).willReturn(false);
        given(passwordEncoder.encode(signupRequest.getPassword())).willReturn("encodedPassword");
        given(userRepository.save(any(User.class))).willReturn(spyUser);
        given(jwtUtil.createToken(anyLong(), anyString(), any(UserRole.class))).willReturn("token");

        // when
        SignResponse signResponse = authService.signup(signupRequest);

        // then
        assertNotNull(signResponse);
        assertEquals("token", signResponse.getBearerToken());
        verify(userRepository).save(any(User.class));

    }

    @Test
    void 중복된_이메일로_회원가입_실패() {
        // given
        SignupRequest signupRequest = new SignupRequest("test@example.com", "password", "USER");

        given(userRepository.existsByEmail(signupRequest.getEmail())).willReturn(true);

        // when / then
        ApiException exception = assertThrows(ApiException.class, () -> authService.signup(signupRequest));

        assertEquals("이미 존재하는 이메일입니다", exception.getErrorCode().getReasonHttpStatus().getMessage());

    }

    @Test
    void 로그인_성공() {
        // given
        LoginRequest loginRequest = new LoginRequest("test@example.com", "password");
        User realUser = new User(loginRequest.getEmail(), "encodedPassword", UserRole.USER);
        User spyUser = spy(realUser);

        doReturn(1L).when(spyUser).getId();

        given(userRepository.findByEmail(loginRequest.getEmail())).willReturn(Optional.of(spyUser));
        given(passwordEncoder.matches(loginRequest.getPassword(), spyUser.getPassword())).willReturn(true);
        given(jwtUtil.createToken(anyLong(), anyString(), any(UserRole.class))).willReturn("token");

        // when
        SignResponse signResponse = authService.login(loginRequest);

        // then
        assertNotNull(signResponse);
        assertEquals("token", signResponse.getBearerToken());
    }

    @Test
    void 잘못된_비밀번호로_로그인_실패() {
        // given
        LoginRequest loginRequest = new LoginRequest("test@example.com", "wrongpassword");
        User user = new User("test@example.com", "encodedPassword", UserRole.USER);

        given(userRepository.findByEmail(loginRequest.getEmail())).willReturn(Optional.of(user));
        given(passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())).willReturn(false);

        // when / then
        ApiException exception = assertThrows(ApiException.class, () -> authService.login(loginRequest));
        assertEquals("비밀번호가 일치하지 않습니다." , exception.getErrorCode().getReasonHttpStatus().getMessage());
    }
}
