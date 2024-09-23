package com.sparta.fmdelivery.domain.order.repository;

import com.sparta.fmdelivery.domain.order.entity.PointHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointHistoryRepository extends JpaRepository<PointHistory, Long> {
}
