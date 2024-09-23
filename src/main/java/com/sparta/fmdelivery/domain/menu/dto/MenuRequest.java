package com.sparta.fmdelivery.domain.menu.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MenuRequest {
    private Long shopId;
    private String name;
    private int price;
    private int status;
}