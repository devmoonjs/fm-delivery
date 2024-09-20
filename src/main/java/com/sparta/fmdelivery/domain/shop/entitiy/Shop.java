package com.sparta.fmdelivery.domain.shop.entitiy;

import com.sparta.fmdelivery.domain.shop.dto.request.ShopCreateRequest;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Getter
@Entity
@NoArgsConstructor
public class Shop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String name;

//    @Column(nullable = false)
    private LocalTime openedAt;

//    @Column(nullable = false)
    private LocalTime closedAt;

    private int status;

    @Column(nullable = false)
    private int minAmount;

    @Column(nullable = false)
    private boolean isDeleted = false;

    public Shop(Long userId, String name, LocalTime openedAt, LocalTime closedAt, int minAmount) {
        this.userId = userId;
        this.name = name;
        this.openedAt = openedAt;
        this.closedAt = closedAt;
        status = 1;
        this.minAmount = minAmount;
    }

    public Shop(ShopCreateRequest request) {
        this.userId = request.getUserId();
        this.name = request.getName();
        this.openedAt = request.getOpenedAt();
        this.closedAt = request.getClosedAt();
        status = 1;
        this.minAmount = request.getMinAmount();
    }

    public void softDelete() {
        this.isDeleted = true;
    }

    public void changeName(String name) {
        this.name = name;
    }

    public void changeOpenedAt(LocalTime time) {
        this.openedAt = time;
    }

    public void changeClosedAt(LocalTime time) {
        this.closedAt = time;
    }
}



