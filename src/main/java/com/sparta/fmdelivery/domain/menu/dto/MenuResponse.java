package com.sparta.fmdelivery.domain.menu.dto;

import com.sparta.fmdelivery.domain.menu.entity.Menu;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MenuResponse {
    private final Long id;
    private final Long shopId;
    private final String name;
    private final int price;
    private final int status;
    private final String imageUrl;

    public static MenuResponse fromEntity(Menu menu) {
        return new MenuResponse(
                menu.getId(),
                menu.getShop().getId(),
                menu.getName(),
                menu.getPrice(),
                menu.getStatus(),
                menu.getImageUrl()
        );
    }
}
