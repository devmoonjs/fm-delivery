package com.sparta.fmdelivery.domain.cart.dto;

import com.sparta.fmdelivery.domain.order.dto.SimpleMenu;
import com.sparta.fmdelivery.domain.shop.entitiy.Shop;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class CartResponse {
    private final Long shopId;
    private final String shopName;
    private final List<SimpleMenu> Menu;

    public CartResponse fromEntity(Shop shop, List<SimpleMenu> menu){
        return new CartResponse(shop.getId(), shop.getName(), menu);
    }
}
