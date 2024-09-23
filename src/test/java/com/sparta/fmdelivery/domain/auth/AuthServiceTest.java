package com.sparta.fmdelivery.domain.auth;

import com.sparta.fmdelivery.config.JwtUtil;
import com.sparta.fmdelivery.config.PasswordEncoder;
import com.sparta.fmdelivery.domain.auth.dto.request.SignupRequest;
import com.sparta.fmdelivery.domain.auth.dto.response.SignResponse;
import com.sparta.fmdelivery.domain.auth.service.AuthService;
import com.sparta.fmdelivery.domain.user.entity.User;
import com.sparta.fmdelivery.domain.user.enums.UserRole;
import com.sparta.fmdelivery.domain.user.repository.UserRepository;
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

}
