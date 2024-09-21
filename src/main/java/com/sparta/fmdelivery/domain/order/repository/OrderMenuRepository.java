package com.sparta.fmdelivery.domain.order.repository;

import com.sparta.fmdelivery.domain.order.entity.OrderMenu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderMenuRepository extends JpaRepository<OrderMenu, Long> {
}
