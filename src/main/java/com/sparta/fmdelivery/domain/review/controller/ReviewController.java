package com.sparta.fmdelivery.domain.review.controller;

import com.sparta.fmdelivery.apipayload.ApiResponse;
import com.sparta.fmdelivery.domain.common.annotation.Auth;
import com.sparta.fmdelivery.domain.common.dto.AuthUser;
import com.sparta.fmdelivery.domain.review.dto.ReviewRequest;
import com.sparta.fmdelivery.domain.review.dto.ReviewResponse;
import com.sparta.fmdelivery.domain.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/reviews")
    public ApiResponse<ReviewResponse> createReview(@Auth AuthUser authUser, @RequestBody ReviewRequest request) {
        return ApiResponse.onSuccess(reviewService.createReview(authUser, request));
    }

    @GetMapping("/reviews/{reviewId}")
    public ApiResponse<ReviewResponse> getReview(@PathVariable Long reviewId) {
        return ApiResponse.onSuccess(reviewService.getReview(reviewId));
    }

    @GetMapping("/reviews")
    public ApiResponse<List<ReviewResponse>> getReviews(@RequestParam("shop_id") Long shopId) {
        return ApiResponse.onSuccess(reviewService.getReviews(shopId));
    }

    @PutMapping("/reviews/{reviewId}")
    public ApiResponse<ReviewResponse> updateReview(@Auth AuthUser authUser, @PathVariable Long reviewId, @RequestBody ReviewRequest request) {
        return ApiResponse.onSuccess(reviewService.updateReview(authUser, reviewId, request));
    }

    @DeleteMapping("/reviews/{reviewId}")
    public ApiResponse<String> deleteReview(@Auth AuthUser authUser, @PathVariable Long reviewId) {
        reviewService.deleteReview(authUser, reviewId);
        return ApiResponse.onSuccess("리뷰가 삭제되었습니다.");
    }
}
