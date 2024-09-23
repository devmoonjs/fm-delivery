package com.sparta.fmdelivery.domain.review.repository;

import com.sparta.fmdelivery.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllByShopId(Long shopId);
}
