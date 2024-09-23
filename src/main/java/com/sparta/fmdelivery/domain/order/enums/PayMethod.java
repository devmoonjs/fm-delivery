package com.sparta.fmdelivery.domain.order.enums;

import com.sparta.fmdelivery.apipayload.status.ErrorStatus;
import com.sparta.fmdelivery.exception.ApiException;

public enum PayMethod {
    CARD,   // 카드
    CASH;   // 현금

    // 입력 string과 enum의 일치여부 확인
    public static PayMethod fromString(String methodString) {
        for (PayMethod method : PayMethod.values()) {
            if (method.name().equalsIgnoreCase(methodString)) {
                return method;
            }
        }
        throw new ApiException(ErrorStatus._INVALID_PAYMENT_METHOD);
    }
}
