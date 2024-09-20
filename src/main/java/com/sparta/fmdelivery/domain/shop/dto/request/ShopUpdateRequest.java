package com.sparta.fmdelivery.domain.shop.dto.request;

import lombok.Getter;

import java.time.LocalTime;

@Getter
public class ShopUpdateRequest {

    private Long shopId;
    private String shopName;
    private LocalTime openedAt;
    private LocalTime closedAt;
}
