package com.sparta.fmdelivery.domain.auth;

import com.sparta.fmdelivery.apipayload.ApiResponse;
import com.sparta.fmdelivery.apipayload.status.ErrorStatus;
import com.sparta.fmdelivery.config.JwtUtil;
import com.sparta.fmdelivery.config.PasswordEncoder;
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


}
