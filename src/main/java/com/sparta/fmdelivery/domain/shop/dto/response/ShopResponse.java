package com.sparta.fmdelivery.domain.shop.dto.response;


import com.sparta.fmdelivery.domain.menu.dto.MenuResponse;
import com.sparta.fmdelivery.domain.menu.entity.Menu;
import com.sparta.fmdelivery.domain.shop.entitiy.Shop;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public class ShopResponse {

    private final Long shopId;
    private final Long userId;
    private final String shopName;
    private final List<MenuResponse> menuList;
    private final LocalTime openedAt;
    private final LocalTime closedAt;

    public static ShopResponse of(Shop shop) {
        return new ShopResponse(
                shop.getId(),
                shop.getUser().getId(),
                shop.getName(),
                shop.getMenuList().stream()
                        .map(MenuResponse::fromEntity)
                        .collect(Collectors.toList()),
                shop.getOpenedAt(),
                shop.getClosedAt()
        );
    }
}
