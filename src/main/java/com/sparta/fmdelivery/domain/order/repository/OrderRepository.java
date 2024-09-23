package com.sparta.fmdelivery.domain.order.repository;

import com.sparta.fmdelivery.domain.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<List<Order>> findAllByUserId(Long userId);
}
