package com.sparta.fmdelivery.domain.cart.entity;

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
    private List<MenuItem> menu;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MenuItem {
        private Long menuId;
        private int count;
    }
}
