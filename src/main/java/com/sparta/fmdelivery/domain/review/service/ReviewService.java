package com.sparta.fmdelivery.domain.review.service;

import com.sparta.fmdelivery.apipayload.status.ErrorStatus;
import com.sparta.fmdelivery.domain.common.dto.AuthUser;
import com.sparta.fmdelivery.domain.order.entity.Order;
import com.sparta.fmdelivery.domain.order.repository.OrderRepository;
import com.sparta.fmdelivery.domain.review.dto.ReviewRequest;
import com.sparta.fmdelivery.domain.review.dto.ReviewResponse;
import com.sparta.fmdelivery.domain.review.entity.Review;
import com.sparta.fmdelivery.domain.review.repository.ReviewRepository;
import com.sparta.fmdelivery.domain.shop.entitiy.Shop;
import com.sparta.fmdelivery.domain.shop.repository.ShopRepository;
import com.sparta.fmdelivery.domain.user.entity.User;
import com.sparta.fmdelivery.domain.user.repository.UserRepository;
import com.sparta.fmdelivery.exception.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final OrderRepository orderRepository;
    private final ShopRepository shopRepository;
    private final UserRepository userRepository;

    /**
     * 리뷰 생성
     * @param authUser
     * @param request
     * @return
     */
    @Transactional
    public ReviewResponse createReview(AuthUser authUser, ReviewRequest request) {
        Order order = getOrderById(request.getOrderId());
        Shop shop = getShopById(request.getShopId());
        User user = getUserById(authUser.getId());

        Review review = new Review(request, order, shop, user);
        return ReviewResponse.fromEntity(reviewRepository.save(review));
    }

    /**
     * 리뷰 단건 조회
     * @param reviewId
     * @return
     */
    @Transactional(readOnly = true)
    public ReviewResponse getReview(Long reviewId) {
        Review review = getReviewById(reviewId);
        return ReviewResponse.fromEntity(review);
    }

    /**
     * 해당 가게의 전체 리뷰 조회
     * @param shopId
     * @return
     */
    @Transactional(readOnly = true)
    public List<ReviewResponse> getReviews(Long shopId) {
        List<Review> reviews = reviewRepository.findAllByShopId(shopId);
        return reviews.stream()
                .map(ReviewResponse::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * 리뷰 수정
     * @param authUser
     * @param request
     * @return
     */
    @Transactional
    public ReviewResponse updateReview(AuthUser authUser, Long reviewId, ReviewRequest request) {
        Review review = getReviewById(reviewId);
        validateUserPermission(review.getUser().getId(), authUser.getId());

        review.updateReview(request.getRating(), request.getContent());
        return ReviewResponse.fromEntity(review);
    }

    /**
     * 리뷰 삭제
     * @param authUser
     * @param reviewId
     */
    @Transactional
    public void deleteReview(AuthUser authUser, Long reviewId) {
        Review review = getReviewById(reviewId);
        validateUserPermission(review.getUser().getId(), authUser.getId());

        reviewRepository.delete(review);
    }

    /**
     * 주문 조회
     * @param orderId
     * @return Order
     */
    private Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new ApiException(ErrorStatus._NOT_FOUND_ORDER));
    }

    /**
     * 가게 조회
     * @param shopId
     * @return Shop
     */
    private Shop getShopById(Long shopId) {
        return shopRepository.findById(shopId)
                .orElseThrow(() -> new ApiException(ErrorStatus._NOT_FOUND_SHOP));
    }

    /**
     * 사용자 조회
     * @param userId
     * @return User
     */
    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ErrorStatus._NOT_FOUND_USER));
    }

    /**
     * 리뷰 조회
     * @param reviewId
     * @return Review
     */
    private Review getReviewById(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ApiException(ErrorStatus._NOT_FOUND_REVIEW));
    }

    /**
     * 사용자 권한 확인
     * @param reviewUserId
     * @param authUserId
     */
    private void validateUserPermission(Long reviewUserId, Long authUserId) {
        if (!reviewUserId.equals(authUserId)) {
            throw new ApiException(ErrorStatus._BAD_REQUEST_UPDATE_REVIEW);
        }
    }
}
