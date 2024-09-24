package com.sparta.fmdelivery.domain.notification.enums;

import com.sparta.fmdelivery.apipayload.status.ErrorStatus;
import com.sparta.fmdelivery.domain.user.enums.UserRole;
import com.sparta.fmdelivery.exception.ApiException;

import java.util.Arrays;

public enum TargetType {

    ALL, USER;

    public static UserRole of(String targetType) {
        return Arrays.stream(UserRole.values())
                .filter(r -> r.name().equalsIgnoreCase(targetType))
                .findFirst()

                // 반환 코드 수정해야함!
                .orElseThrow(() -> new ApiException(ErrorStatus._INVALID_USER_ROLE));
    }
}
