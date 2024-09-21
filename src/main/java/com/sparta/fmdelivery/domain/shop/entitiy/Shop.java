package com.sparta.fmdelivery.domain.shop.entitiy;

import com.sparta.fmdelivery.domain.common.entity.Timestamped;
import com.sparta.fmdelivery.domain.shop.dto.request.ShopCreateRequest;
import com.sparta.fmdelivery.domain.user.entity.User;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "shops")
public class Shop extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalTime openedAt;

    @Column(nullable = false)
    private LocalTime closedAt;

    private int status;

    @Column(nullable = false)
    private int minAmount;

    @Column(nullable = false)
    private boolean isDeleted = false;

    public Shop(User user, ShopCreateRequest request) {
        this.user = user;
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



