package com.sparta.fmdelivery.domain.menu.entity;

import com.sparta.fmdelivery.common.entity.Timestamped;
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

    @Column(name = "img_url")
    private String imageUrl;

    public Menu(MenuRequest request, Shop shop, String imageUrl) {
        this.shop = shop;  // Shop 엔티티를 할당
        this.name = request.getName();
        this.price = request.getPrice();
        this.status = request.getStatus();
        this.imageUrl = imageUrl;
    }

    public void updateMenu(String name, int price, int status, String imageUrl) {
        this.name = name;
        this.price = price;
        this.status = status;
        this.imageUrl = imageUrl;
    }
}
