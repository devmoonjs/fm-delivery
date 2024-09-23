package com.sparta.fmdelivery.domain.order.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    private String payMethod;
    private int totalPrice;
    private int usedPoint;
}