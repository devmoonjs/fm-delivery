package com.sparta.fmdelivery.domain.auth.controller;

import com.sparta.fmdelivery.apipayload.ApiResponse;
import com.sparta.fmdelivery.domain.auth.dto.request.LoginRequest;
import com.sparta.fmdelivery.domain.auth.dto.request.SignoutRequest;
import com.sparta.fmdelivery.domain.auth.dto.request.SignupRequest;
import com.sparta.fmdelivery.domain.auth.dto.response.SignResponse;
import com.sparta.fmdelivery.domain.auth.service.AuthService;
import com.sparta.fmdelivery.domain.common.annotation.Auth;
import com.sparta.fmdelivery.domain.common.dto.AuthUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/auth/signup")
    public ApiResponse<SignResponse> signup(@Valid @RequestBody SignupRequest signupRequest) {
        return ApiResponse.onSuccess(authService.signup(signupRequest));
    }

    @PostMapping("/auth/login")
    public ApiResponse<SignResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        return ApiResponse.onSuccess(authService.login(loginRequest));
    }

    @DeleteMapping("/signout")
    public ApiResponse<String> signout(@Valid @RequestBody SignoutRequest signoutRequest, @Auth AuthUser authUser) {
        authService.signout(signoutRequest, authUser.getId());
        return ApiResponse.onSuccess("Success Signout");
    }
}
