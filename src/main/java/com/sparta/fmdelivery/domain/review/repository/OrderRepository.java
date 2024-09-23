package com.sparta.fmdelivery.domain.review.repository;

import com.sparta.fmdelivery.domain.review.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
