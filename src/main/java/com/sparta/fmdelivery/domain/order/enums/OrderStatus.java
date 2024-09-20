package com.sparta.fmdelivery.domain.order.enums;

public enum OrderStatus {
    ORDER_PLACED,     // 주문 접수
    ACCEPTED,         // 주문 수락
    IN_PREPARATION,   // 음식 조리 중
    OUT_FOR_DELIVERY, // 배달 중
    DELIVERED         // 배달 완료
}
