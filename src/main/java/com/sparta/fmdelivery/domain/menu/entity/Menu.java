package com.sparta.fmdelivery.domain.menu.entity;

import com.sparta.fmdelivery.domain.common.entity.Timestamped;
import com.sparta.fmdelivery.domain.menu.dto.MenuRequest;
import com.sparta.fmdelivery.domain.shop.entitiy.Shop;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Menu extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // shopId를 Shop 엔티티와의 외래 키로 설정
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id", nullable = false)
    private Shop shop;

    private String name;
    private int price;
    private int status;

    public Menu(MenuRequest request, Shop shop) {
        this.shop = shop;  // Shop 엔티티를 할당
        this.name = request.getName();
        this.price = request.getPrice();
        this.status = request.getStatus();
    }

    public void updateName(String name, int price, int status) {
        this.name = name;
        this.price = price;
        this.status = status;
    }
}
