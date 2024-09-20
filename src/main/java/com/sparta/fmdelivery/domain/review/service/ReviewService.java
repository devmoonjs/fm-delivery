package com.sparta.fmdelivery.domain.review.service;

import com.sparta.fmdelivery.domain.common.dto.AuthUser;
import com.sparta.fmdelivery.domain.review.dto.ReviewRequest;
import com.sparta.fmdelivery.domain.review.dto.ReviewResponse;
import com.sparta.fmdelivery.domain.review.entity.Order;
import com.sparta.fmdelivery.domain.review.entity.Review;
import com.sparta.fmdelivery.domain.review.repository.OrderRepository;
import com.sparta.fmdelivery.domain.review.repository.ReviewRepository;
import com.sparta.fmdelivery.domain.shop.entitiy.Shop;
import com.sparta.fmdelivery.domain.shop.repository.ShopRepository;
import com.sparta.fmdelivery.domain.user.entity.User;
import com.sparta.fmdelivery.domain.user.repository.UserRepository;
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
        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다."));
        Shop shop = shopRepository.findById(request.getShopId())
                .orElseThrow(() -> new IllegalArgumentException("가게를 찾을 수 없습니다."));
        User user = userRepository.findById(authUser.getId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        Review review = new Review(request, order, shop, user);

        Review savedReview = reviewRepository.save(review);
        return ReviewResponse.fromEntity(savedReview);
    }

    /**
     * 리뷰 단건 조회
     * @param reviewId
     * @return
     */
    @Transactional
    public ReviewResponse getReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("리뷰를 찾을 수 없습니다."));

        return ReviewResponse.fromEntity(review);
    }


    /**
     * 해당 가게의 전체 리뷰 조회
     * @param shopId
     * @return
     */
    @Transactional
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
    public ReviewResponse updateReview(AuthUser authUser, Long reviewId, ReviewRequest request) {
        isValidUser(reviewId, authUser.getId());

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("리뷰를 찾을 수 없습니다."));

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
        isValidUser(reviewId, authUser.getId());

        Review review = reviewRepository.findById(reviewId)
                        .orElseThrow(() -> new IllegalArgumentException("리뷰를 찾을 수 없습니다."));
        reviewRepository.delete(review);
    }

    /**
     * 리뷰의 사용자 권한 확인
     * @param reviewUserId
     * @param authUserId
     */
    private void isValidUser(Long reviewUserId, Long authUserId) {
        if (!reviewUserId.equals(authUserId)) {
            throw new IllegalArgumentException("본인의 리뷰만 수정할 수 있습니다.");
        }
    }

}
