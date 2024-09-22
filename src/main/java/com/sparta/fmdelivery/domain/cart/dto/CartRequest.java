package com.sparta.fmdelivery.domain.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CartRequest {
    private Long shopId;
    private Long menuId;
}
