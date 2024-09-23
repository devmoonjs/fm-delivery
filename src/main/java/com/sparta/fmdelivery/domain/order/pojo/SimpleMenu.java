package com.sparta.fmdelivery.domain.order.pojo;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SimpleMenu {
    private Long id;
    private String name;
    private int price;
    private int count;
}
