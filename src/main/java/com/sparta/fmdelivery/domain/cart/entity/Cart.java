package com.sparta.fmdelivery.domain.cart.entity;

import com.sparta.fmdelivery.domain.order.dto.MenuIdList;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@RedisHash(value = "userId", timeToLive = 86400)
public class Cart implements Serializable {

    @Id
    private Long userId;
    private Long shopId;
    private List<MenuIdList> menu;
}
