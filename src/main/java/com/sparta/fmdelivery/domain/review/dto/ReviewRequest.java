package com.sparta.fmdelivery.domain.review.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequest {

    private Long orderId;
    private Long shopId;
    private int rating;
    private String content;
}
