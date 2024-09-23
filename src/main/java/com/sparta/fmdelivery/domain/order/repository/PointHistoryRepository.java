package com.sparta.fmdelivery.domain.order.repository;

import com.sparta.fmdelivery.domain.order.entity.PointHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PointHistoryRepository extends JpaRepository<PointHistory, Long> {
    Optional<PointHistory> findTopByUserIdOrderByCreatedAtDesc(Long userId);
}
