package com.sparta.fmdelivery.domain.user.enums;

import com.sparta.fmdelivery.apipayload.status.ErrorStatus;
import com.sparta.fmdelivery.exception.ApiException;

import java.util.Arrays;

public enum UserRole {

    OWNER, USER;

    public static UserRole of(String role) {
        return Arrays.stream(UserRole.values())
                .filter(r -> r.name().equalsIgnoreCase(role))
                .findFirst()
                .orElseThrow(() -> new ApiException(ErrorStatus._INVALID_USER_ROLE));
    }
}
