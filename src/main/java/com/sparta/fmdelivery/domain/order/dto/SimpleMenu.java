package com.sparta.fmdelivery.domain.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SimpleMenu {
    private Long id;
    private String name;
    private int price;
    private int count;
}
