package com.sparta.fmdelivery.domain.order.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderResponse {
    private final Long orderId;
    private final Long shopId;
}
