package com.sparta.fmdelivery.domain.order.enums;

import com.sparta.fmdelivery.apipayload.status.ErrorStatus;
import com.sparta.fmdelivery.exception.ApiException;

public enum OrderStatus {
    ORDER_PLACED,     // 주문 접수
    ACCEPTED,         // 주문 수락
    IN_PREPARATION,   // 음식 조리 중
    OUT_FOR_DELIVERY, // 배달 중
    DELIVERED;         // 배달 완료

    // 입력 string과 enum의 일치여부 확인
    public static OrderStatus fromString(String statusString) {
        for (OrderStatus status : OrderStatus.values()) {
            if (status.name().equalsIgnoreCase(statusString)) {
                return status;
            }
        }
        throw new ApiException(ErrorStatus._INVALID_ORDER_STATUS);
    }
}
