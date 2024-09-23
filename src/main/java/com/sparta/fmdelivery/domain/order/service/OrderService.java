package com.sparta.fmdelivery.domain.order.service;

import com.sparta.fmdelivery.domain.common.dto.AuthUser;
import com.sparta.fmdelivery.domain.order.dto.request.OrderRequest;
import com.sparta.fmdelivery.domain.order.dto.response.OrderDetailResponse;
import com.sparta.fmdelivery.domain.order.dto.response.OrderListResponse;
import com.sparta.fmdelivery.domain.order.dto.response.OrderResponse;
import com.sparta.fmdelivery.domain.order.repository.OrderMenuRepository;
import com.sparta.fmdelivery.domain.order.repository.OrderRepository;
import com.sparta.fmdelivery.domain.order.repository.PointHistoryRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMenuRepository orderMenuRepository;
    private final PointHistoryRepository pointHistoryRepository;

    @Transactional
    public OrderResponse saveOrder(AuthUser authUser, OrderRequest orderRequest) {

        /*
        orderRequest : {
            "payMethod" : "CARD",
            "totalPrice" : 10000,
            "usedPoint" : 500,
            }
        */
        /*
         1. (redis) cart 테이블에서 userId로 데이터 가져오기(key가 userid로 되어있음)
         2. (mysql) 1에서 가져온 데이터 중 shopId로 shop 테이블에서 데이터 가져오기
         2-1. 예외조건이면 예외Response 반환, 아니면 넘어가
                - 가게에서 설정한 최소 주문 금액을 만족해야 주문이 가능합니다.
                - 가게의 오픈/마감 시간이 지나면 주문할 수 없습니다.
         3. 1번, 2번에서 가져온 데이터와 orderRequest 객체의 데이터로 -> Order, OrderMenu 객체 생성
         3-2. Order 객체 생성할 때 사용 포인트가 0이 아니면 포인트 기록 객체 만들고 저장
         4. 주문 생성이 완료되면 userId로 장바구니 객체 삭제, OrderResponse 객체 생성 후 반환
        */

        return null;
    }

    public OrderResponse updateOrder(AuthUser authUser, Long orderId) {


        /*
        - 주문 상태가 배달 완료로 변경 시 주문금액의 3% 적립
        - 포인트 기록 테이블에 추가
        */

        return null;
    }

    public List<OrderListResponse> getAllOrders(AuthUser authUser) {

        return null;
    }

    public OrderDetailResponse getOrder(AuthUser authUser, Long orderId) {

        return null;
    }

}
