package com.sparta.fmdelivery.domain.review.entity;

import com.sparta.fmdelivery.common.entity.Timestamped;
import com.sparta.fmdelivery.domain.order.entity.Order;
import com.sparta.fmdelivery.domain.review.dto.ReviewRequest;
import com.sparta.fmdelivery.domain.shop.entitiy.Shop;
import com.sparta.fmdelivery.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "reviews")
public class Review extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id", nullable = false)
    private Shop shop;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)  // 사용자와의 관계를 설정
    private User user;

    private int rating;

    @Column(nullable = false, length = 200)
    private String content;

    @Column(name = "img_url")
    private String imageUrl;

    // 생성자
    public Review(ReviewRequest request, Order order, Shop shop, User user, String imgUrl) {
        this.order = order;
        this.shop = shop;
        this.user = user;  // User 엔티티를 초기화
        this.rating = request.getRating();
        this.content = request.getContent();
        this.imageUrl = imgUrl;
    }

    // 리뷰 수정 메서드
    public void updateReview(int rating, String content, String imageUrl) {
        this.rating = rating;
        this.content = content;
        this.imageUrl = imageUrl;
    }
}
