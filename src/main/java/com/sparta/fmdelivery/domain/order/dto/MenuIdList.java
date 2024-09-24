package com.sparta.fmdelivery.domain.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MenuIdList {
    private Long menuId;
    private int count;

    public void updateCount(int count) {
        this.count += count;
    }
}
