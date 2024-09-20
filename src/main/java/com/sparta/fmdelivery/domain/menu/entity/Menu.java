package com.sparta.fmdelivery.domain.menu.entity;

import com.sparta.fmdelivery.domain.common.entity.Timestamped;
import com.sparta.fmdelivery.domain.menu.dto.MenuRequest;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
@NoArgsConstructor
public class Menu extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "shop_id")
    private Long shopId;

    private String name;

    private int price;

    private int status;

    public Menu(MenuRequest request) {
        this.shopId = request.getShopId();
        this.name = request.getName();
        this.price = request.getPrice();
        this.status = request.getStatus();
    }

    public void changeName(String name) {
        this.name = name;
    }

    public void changePrice(int price) {
        this.price = price;
    }

    public void changeStatus(int status) {
        this.status = status;
    }
}
