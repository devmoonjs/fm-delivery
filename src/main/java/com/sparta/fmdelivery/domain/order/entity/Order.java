package com.sparta.fmdelivery.domain.order.entity;

import com.sparta.fmdelivery.common.entity.Timestamped;
import com.sparta.fmdelivery.domain.order.enums.OrderStatus;
import com.sparta.fmdelivery.domain.order.enums.PayMethod;
import com.sparta.fmdelivery.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "orders")
public class Order extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "total_price")
    private int totalPrice;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(name = "pay_method")
    @Enumerated(EnumType.STRING)
    private PayMethod payMethod;

    @Column(name = "used_point")
    private int usedPoint;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Order(int totalPrice, PayMethod payMethod, int usedPoint, User user){
        this.totalPrice = totalPrice;
        this.status = OrderStatus.ORDER_PLACED;
        this.payMethod = payMethod;
        this.usedPoint = usedPoint;
        this.user = user;
    }

    public void changeStatus(OrderStatus status) {
        this.status = status;
    }
}
