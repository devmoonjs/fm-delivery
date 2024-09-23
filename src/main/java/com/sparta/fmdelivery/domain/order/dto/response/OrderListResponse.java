package com.sparta.fmdelivery.domain.order.dto.response;

import com.sparta.fmdelivery.domain.order.entity.Order;
import com.sparta.fmdelivery.domain.order.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class OrderListResponse {
    private final Long id;
    private final String shopName;
    private final int totalPrice;
    private final LocalDateTime orderDate;
    private final OrderStatus status;

    public static OrderListResponse fromEntity(Order order, String shopName) {
        return new OrderListResponse(
                order.getId(),
                shopName,
                order.getTotalPrice(),
                order.getCreatedAt(),
                order.getStatus()
        );
    }
}
