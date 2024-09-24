package com.sparta.fmdelivery.domain.order.dto.response;

import com.sparta.fmdelivery.domain.order.dto.SimpleMenu;
import com.sparta.fmdelivery.domain.order.entity.Order;
import com.sparta.fmdelivery.domain.order.enums.OrderStatus;
import com.sparta.fmdelivery.domain.order.enums.PayMethod;
import com.sparta.fmdelivery.domain.shop.entitiy.Shop;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class OrderDetailResponse {
    private final Long id;
    private final Long shopId;
    private final String shopName;
    private final List<SimpleMenu> Menu;
    private final int totalPrice;
    private final LocalDateTime orderDate;
    private final OrderStatus status;
    private final PayMethod payMethod;
    private final int usedPoint;

    public static OrderDetailResponse fromEntity(Shop shop, List<SimpleMenu> menu, Order order ) {
        return new OrderDetailResponse(
                order.getId(),
                shop.getId(),
                shop.getName(),
                menu,
                order.getTotalPrice(),
                order.getCreatedAt(),
                order.getStatus(),
                order.getPayMethod(),
                order.getUsedPoint()
        );
    }
}
