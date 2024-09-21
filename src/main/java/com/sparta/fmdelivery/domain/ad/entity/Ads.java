package com.sparta.fmdelivery.domain.ad.entity;

import com.sparta.fmdelivery.domain.common.entity.Timestamped;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
public class Ads extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private Long shopId;
    @Column
    private LocalDateTime startDate;
    @Column
    private LocalDateTime endDate;
    @Column
    private boolean status;

    public Ads(Long shopId, LocalDateTime startDate, LocalDateTime endDate, boolean status) {
        this.shopId = shopId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

}
