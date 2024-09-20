package com.sparta.fmdelivery.domain.order.entity;

import com.sparta.fmdelivery.domain.common.entity.Timestamped;
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
    @Column(name = "menu_id_list", nullable = false)
    private List<Long> menuIdList;

    @OneToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    // 가게기능(예서님) 구현 후  추가

}
