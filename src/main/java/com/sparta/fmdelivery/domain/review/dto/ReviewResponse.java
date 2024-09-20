package com.sparta.fmdelivery.domain.review.dto;

import com.sparta.fmdelivery.domain.review.entity.Review;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReviewResponse {

    private Long id;
    private Long orderId;
    private Long shopId;
    private int rating;
    private String content;

    public static ReviewResponse fromEntity(Review review) {
        return new ReviewResponse(
                review.getId(),
                review.getOrder().getId(),
                review.getShop().getId(),
                review.getRating(),
                review.getContent()
        );
    }
}
