package com.sparta.fmdelivery.domain.review.controller;

import com.sparta.fmdelivery.apipayload.ApiResponse;
import com.sparta.fmdelivery.domain.common.annotation.Auth;
import com.sparta.fmdelivery.domain.common.dto.AuthUser;
import com.sparta.fmdelivery.domain.menu.dto.MenuRequest;
import com.sparta.fmdelivery.domain.review.dto.ReviewRequest;
import com.sparta.fmdelivery.domain.review.dto.ReviewResponse;
import com.sparta.fmdelivery.domain.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping(value = "/reviews", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<ReviewResponse> createReview(
            @Auth AuthUser authUser,
            @RequestPart("request") ReviewRequest request,
            @RequestParam("image") MultipartFile image) {
        return ApiResponse.onSuccess(reviewService.createReview(authUser, request, image));
    }

    @GetMapping("/reviews/{reviewId}")
    public ApiResponse<ReviewResponse> getReview(@PathVariable Long reviewId) {
        return ApiResponse.onSuccess(reviewService.getReview(reviewId));
    }

    @GetMapping("/reviews")
    public ApiResponse<List<ReviewResponse>> getReviews(@RequestParam("shop_id") Long shopId) {
        return ApiResponse.onSuccess(reviewService.getReviews(shopId));
    }

    @PutMapping(value = "/reviews/{reviewId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<ReviewResponse> updateReview(
            @Auth AuthUser authUser,
            @PathVariable Long reviewId,
            @RequestPart("request") ReviewRequest request,
            @RequestPart(value = "image", required = false) MultipartFile image) {

        return ApiResponse.onSuccess(reviewService.updateReview(authUser, reviewId, request, image));
    }

    @DeleteMapping("/reviews/{reviewId}")
    public ApiResponse<String> deleteReview(@Auth AuthUser authUser, @PathVariable Long reviewId) {
        reviewService.deleteReview(authUser, reviewId);
        return ApiResponse.onSuccess("리뷰가 삭제되었습니다.");
    }
}
