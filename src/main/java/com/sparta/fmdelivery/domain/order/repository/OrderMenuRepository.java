package com.sparta.fmdelivery.domain.order.repository;

import com.sparta.fmdelivery.domain.order.entity.Order;
import com.sparta.fmdelivery.domain.order.entity.OrderMenu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderMenuRepository extends JpaRepository<OrderMenu, Long> {
    Optional<OrderMenu> findAllByOrderId(Long orderId);
}
