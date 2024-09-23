package com.sparta.fmdelivery.domain.shop.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalTime;

@Getter
@AllArgsConstructor
public class ShopCreateRequest {

    private String name;
    private LocalTime openedAt;
    private LocalTime closedAt;
    private int minAmount;
}
