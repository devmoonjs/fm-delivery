package com.sparta.fmdelivery.domain.order.entity;

import com.sparta.fmdelivery.domain.common.entity.Timestamped;
import com.sparta.fmdelivery.domain.order.dto.MenuIdList;
import com.sparta.fmdelivery.domain.shop.entitiy.Shop;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "order_menu")
public class OrderMenu extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Type(JsonType.class)
    @Column(name = "menu_id_list", nullable = false, columnDefinition = "JSON")
    private List<MenuIdList> menuIdList;

    @OneToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id", nullable = false)
    private Shop shop;

    public OrderMenu(List<MenuIdList> menuIdList , Order order, Shop shop) {
        this.menuIdList = menuIdList;
        this.order = order;
        this.shop = shop;
    }
}
