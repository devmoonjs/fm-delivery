package com.sparta.fmdelivery.domain.order.entity;

import com.sparta.fmdelivery.domain.common.entity.Timestamped;
import com.sparta.fmdelivery.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "point_history")
public class PointHistory extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int point;
    private int balance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
