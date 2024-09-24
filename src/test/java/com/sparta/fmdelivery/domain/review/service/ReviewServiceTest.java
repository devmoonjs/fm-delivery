package com.sparta.fmdelivery.domain.review.service;

import com.sparta.fmdelivery.apipayload.dto.ReasonDto;
import com.sparta.fmdelivery.apipayload.status.ErrorStatus;
import com.sparta.fmdelivery.config.S3ClientUtility;
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
import com.sparta.fmdelivery.exception.ApiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

class ReviewServiceTest {

    @InjectMocks
    private ReviewService reviewService;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ShopRepository shopRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private S3ClientUtility s3ClientUtility;

    @Mock
    private MultipartFile multipartFile;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createReview_성공() {
        // given
        AuthUser authUser = new AuthUser(1L, "user@example.com", null);
        ReviewRequest reviewRequest = new ReviewRequest(1L, 1L, 5, "Great review");
        Order order = mock(Order.class);
        Shop shop = mock(Shop.class);
        User user = mock(User.class);
        Review review = new Review(reviewRequest, order, shop, user, "imageUrl");

        given(orderRepository.findById(reviewRequest.getOrderId())).willReturn(Optional.of(order));
        given(shopRepository.findById(reviewRequest.getShopId())).willReturn(Optional.of(shop));
        given(userRepository.findById(authUser.getId())).willReturn(Optional.of(user));
        given(s3ClientUtility.uploadFile(anyString(), any(MultipartFile.class))).willReturn("imageUrl");
        given(reviewRepository.save(any(Review.class))).willReturn(review);

        // when
        ReviewResponse result = reviewService.createReview(authUser, reviewRequest, multipartFile);

        // then
        assertNotNull(result);
        assertEquals("Great review", result.getContent());
        verify(reviewRepository, times(1)).save(any(Review.class));
    }

    @Test
    void createReview_주문없음_예외발생() {
        // given
        AuthUser authUser = new AuthUser(1L, "user@example.com", null);
        ReviewRequest reviewRequest = new ReviewRequest(1L, 1L, 5, "Great review");

        given(orderRepository.findById(reviewRequest.getOrderId())).willReturn(Optional.empty());

        // when & then
        ApiException exception = assertThrows(ApiException.class, () -> {
            reviewService.createReview(authUser, reviewRequest, multipartFile);
        });

        // ErrorStatus에서 getReasonHttpStatus() 호출해 ReasonDto 검증
        ReasonDto reasonDto = ErrorStatus._NOT_FOUND_ORDER.getReasonHttpStatus();

        assertEquals("404", reasonDto.getStatusCode());
        assertEquals("존재하지 않는 주문입니다.", reasonDto.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, reasonDto.getHttpStatus());
    }

    @Test
    void getReview_성공() {
        // given
        Long reviewId = 1L;
        Review review = mock(Review.class);
        Order order = mock(Order.class);
        Shop shop = mock(Shop.class);
        User user = mock(User.class);

        given(review.getOrder()).willReturn(order);
        given(order.getId()).willReturn(1L);
        given(review.getShop()).willReturn(shop);
        given(shop.getId()).willReturn(1L);
        given(review.getUser()).willReturn(user);
        given(user.getId()).willReturn(1L);

        // Mock된 Review 객체가 반환되도록 설정
        given(reviewRepository.findById(reviewId)).willReturn(Optional.of(review));

        // when
        ReviewResponse result = reviewService.getReview(reviewId);

        // then
        assertNotNull(result);
        assertEquals(1L, result.getOrderId());
        verify(reviewRepository, times(1)).findById(reviewId);
    }

    @Test
    void getReview_존재하지않음_예외발생() {
        // given
        Long reviewId = 1L;

        given(reviewRepository.findById(reviewId)).willReturn(Optional.empty());

        // when & then
        ApiException exception = assertThrows(ApiException.class, () -> {
            reviewService.getReview(reviewId);
        });

        // ErrorStatus에서 getReasonHttpStatus() 호출해 ReasonDto 검증
        ReasonDto reasonDto = ErrorStatus._NOT_FOUND_REVIEW.getReasonHttpStatus();

        assertEquals("404", reasonDto.getStatusCode());
        assertEquals("존재하지 않는 리뷰입니다.", reasonDto.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, reasonDto.getHttpStatus());
    }

    @Test
    void getReviews_성공() {
        // given
        Long shopId = 1L;
        Review review = mock(Review.class);
        Order order = mock(Order.class);
        Shop shop = mock(Shop.class);
        User user = mock(User.class);

        // Review 객체가 내부 메서드 호출 시 모킹된 객체를 반환하도록 설정
        given(review.getOrder()).willReturn(order);
        given(order.getId()).willReturn(1L);
        given(review.getShop()).willReturn(shop);
        given(shop.getId()).willReturn(shopId);
        given(review.getUser()).willReturn(user);
        given(user.getId()).willReturn(1L);

        List<Review> reviews = List.of(review);
        given(reviewRepository.findAllByShopId(shopId)).willReturn(reviews);

        // when
        List<ReviewResponse> result = reviewService.getReviews(shopId);

        // then
        assertEquals(1, result.size());
        verify(reviewRepository, times(1)).findAllByShopId(shopId); // findAllByShopId 호출 검증
    }


    @Test
    void updateReview_성공() throws Exception {
        // given
        AuthUser authUser = new AuthUser(1L, "user@example.com", null);
        Long reviewId = 1L;
        ReviewRequest reviewRequest = new ReviewRequest(1L, 1L, 4, "Updated review");
        Review review = mock(Review.class);
        User user = mock(User.class);
        Order order = mock(Order.class);
        Shop shop = mock(Shop.class);

        given(review.getUser()).willReturn(user);
        given(user.getId()).willReturn(authUser.getId());
        given(review.getOrder()).willReturn(order);
        given(review.getShop()).willReturn(shop);
        given(order.getId()).willReturn(1L);
        given(shop.getId()).willReturn(1L);

        given(reviewRepository.findById(reviewId)).willReturn(Optional.of(review));
        given(s3ClientUtility.uploadFile(anyString(), any(MultipartFile.class))).willReturn("updatedImageUrl");

        // when
        ReviewResponse result = reviewService.updateReview(authUser, reviewId, reviewRequest, multipartFile);

        // then
        assertNotNull(result); // 결과가 null이 아닌지 확인
        verify(review).updateReview(reviewRequest.getRating(), reviewRequest.getContent(), "updatedImageUrl");
    }


    @Test
    void updateReview_권한없음_예외발생() {
        // given
        AuthUser authUser = new AuthUser(2L, "user@example.com", null);
        Long reviewId = 1L;
        ReviewRequest reviewRequest = new ReviewRequest(1L, 1L, 4, "Updated review");
        Review review = mock(Review.class);
        User user = mock(User.class);

        given(review.getUser()).willReturn(user);
        given(user.getId()).willReturn(1L); // 다른 유저의 리뷰로 설정
        given(reviewRepository.findById(reviewId)).willReturn(Optional.of(review));

        // when & then
        ApiException exception = assertThrows(ApiException.class, () -> {
            reviewService.updateReview(authUser, reviewId, reviewRequest, multipartFile);
        });

        // ErrorStatus에서 getReasonHttpStatus() 호출해 ReasonDto 검증
        ReasonDto reasonDto = ErrorStatus._BAD_REQUEST_UPDATE_REVIEW.getReasonHttpStatus();

        assertEquals("400", reasonDto.getStatusCode());
        assertEquals("본인의 리뷰만 수정할 수 있습니다.", reasonDto.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, reasonDto.getHttpStatus());
    }

    @Test
    void deleteReview_성공() {
        // given
        AuthUser authUser = new AuthUser(1L, "user@example.com", null);
        Long reviewId = 1L;
        Review review = mock(Review.class);
        User user = mock(User.class);

        given(review.getUser()).willReturn(user);
        given(user.getId()).willReturn(authUser.getId());
        given(reviewRepository.findById(reviewId)).willReturn(Optional.of(review));

        // when
        reviewService.deleteReview(authUser, reviewId);

        // then
        verify(reviewRepository, times(1)).delete(review);
    }

    @Test
    void deleteReview_권한없음_예외발생() {
        // given
        AuthUser authUser = new AuthUser(2L, "user@example.com", null);
        Long reviewId = 1L;
        Review review = mock(Review.class);
        User user = mock(User.class);

        given(review.getUser()).willReturn(user);
        given(user.getId()).willReturn(1L); // 다른 유저의 리뷰로 설정
        given(reviewRepository.findById(reviewId)).willReturn(Optional.of(review));

        // when & then
        ApiException exception = assertThrows(ApiException.class, () -> {
            reviewService.deleteReview(authUser, reviewId);
        });

        // ErrorStatus에서 getReasonHttpStatus() 호출해 ReasonDto 검증
        ReasonDto reasonDto = ErrorStatus._BAD_REQUEST_UPDATE_REVIEW.getReasonHttpStatus();

        assertEquals("400", reasonDto.getStatusCode());
        assertEquals("본인의 리뷰만 수정할 수 있습니다.", reasonDto.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, reasonDto.getHttpStatus());
    }
}
