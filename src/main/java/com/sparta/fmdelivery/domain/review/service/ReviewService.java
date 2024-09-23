package com.sparta.fmdelivery.domain.review.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.sparta.fmdelivery.apipayload.status.ErrorStatus;
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
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final OrderRepository orderRepository;
    private final ShopRepository shopRepository;
    private final UserRepository userRepository;
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private String MENU_IMG_DIR = "review/";

    @Transactional
    public ReviewResponse createReview(AuthUser authUser, ReviewRequest request, MultipartFile image) {
        Order order = getOrderById(request.getOrderId());
        Shop shop = getShopById(request.getShopId());
        User user = getUserById(authUser.getId());

        String imageUrl = uploadImageToS3(image);

        Review review = new Review(request, order, shop, user, imageUrl);
        return ReviewResponse.fromEntity(reviewRepository.save(review));
    }

    @Transactional(readOnly = true)
    public ReviewResponse getReview(Long reviewId) {
        Review review = getReviewById(reviewId);
        return ReviewResponse.fromEntity(review);
    }

    @Transactional(readOnly = true)
    public List<ReviewResponse> getReviews(Long shopId) {
        List<Review> reviews = reviewRepository.findAllByShopId(shopId);
        return reviews.stream()
                .map(ReviewResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public ReviewResponse updateReview(AuthUser authUser, Long reviewId, ReviewRequest request, MultipartFile image) {
        Review review = getReviewById(reviewId);
        validateUserPermission(review.getUser().getId(), authUser.getId());

        // 이미지가 있는 경우에만 업로드하고, 없으면 기존 이미지 URL 유지
        String imageUrl = review.getImageUrl(); // 기존 이미지 URL
        if (image != null && !image.isEmpty()) {
            imageUrl = uploadImageToS3(image); // 새 이미지 업로드
        }

        review.updateReview(request.getRating(), request.getContent(), imageUrl);
        return ReviewResponse.fromEntity(review);
    }

    @Transactional
    public void deleteReview(AuthUser authUser, Long reviewId) {
        Review review = getReviewById(reviewId);
        validateUserPermission(review.getUser().getId(), authUser.getId());

        reviewRepository.delete(review);
    }

    private Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new ApiException(ErrorStatus._NOT_FOUND_ORDER));
    }

    private Shop getShopById(Long shopId) {
        return shopRepository.findById(shopId)
                .orElseThrow(() -> new ApiException(ErrorStatus._NOT_FOUND_SHOP));
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ErrorStatus._NOT_FOUND_USER));
    }

    private Review getReviewById(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ApiException(ErrorStatus._NOT_FOUND_REVIEW));
    }

    private void validateUserPermission(Long reviewUserId, Long authUserId) {
        if (!reviewUserId.equals(authUserId)) {
            throw new ApiException(ErrorStatus._BAD_REQUEST_UPDATE_REVIEW);
        }
    }

    private String uploadImageToS3(MultipartFile multipartFile) {
        try {
            String fileName = MENU_IMG_DIR + UUID.randomUUID() + "_" + multipartFile.getOriginalFilename();

            // 메타데이터 설정 (파일 크기와 콘텐츠 타입)
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(multipartFile.getSize());
            metadata.setContentType(multipartFile.getContentType());

            // S3에 파일 업로드
            amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, multipartFile.getInputStream(), metadata));

            // 업로드된 파일의 URL 반환
            return amazonS3Client.getUrl(bucket, fileName).toString();
        } catch (IOException e) {
            throw new ApiException(ErrorStatus._FILE_UPLOAD_ERROR);
        }
    }
}
