package com.sparta.fmdelivery.domain.review.controller;

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
    public ResponseEntity<ReviewResponse> createReview(@Auth AuthUser authUser, @RequestBody ReviewRequest request) {
        return ResponseEntity.ok(reviewService.createReview(authUser, request));
    }

    @GetMapping("/reviews/{reviewId}")
    public ResponseEntity<ReviewResponse> getReview(@PathVariable Long reviewId) {
        return ResponseEntity.ok(reviewService.getReview(reviewId));
    }

    @GetMapping("/reviews")
    public ResponseEntity<List<ReviewResponse>> getReviews(@RequestParam("shop_id") Long shopId) {
        return ResponseEntity.ok(reviewService.getReviews(shopId));
    }

    @PutMapping("/reviews/{reviewId}")
    public ResponseEntity<ReviewResponse> updateReview(@Auth AuthUser authUser, @PathVariable Long reviewId, @RequestBody ReviewRequest request) {
        return ResponseEntity.ok(reviewService.updateReview(authUser, reviewId, request));
    }

    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<Void> deleteReview(@Auth AuthUser authUser, @PathVariable Long reviewId) {
        reviewService.deleteReview(authUser, reviewId);
        return ResponseEntity.noContent().build(); // 성공 시 204 No Content 반환
    }
}
