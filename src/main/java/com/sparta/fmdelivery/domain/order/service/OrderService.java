package com.sparta.fmdelivery.domain.order.service;

import com.sparta.fmdelivery.apipayload.status.ErrorStatus;
import com.sparta.fmdelivery.domain.cart.entity.Cart;
import com.sparta.fmdelivery.domain.cart.repository.CartRepository;
import com.sparta.fmdelivery.domain.common.dto.AuthUser;
import com.sparta.fmdelivery.domain.menu.entity.Menu;
import com.sparta.fmdelivery.domain.menu.repository.MenuRepository;
import com.sparta.fmdelivery.domain.order.dto.request.OrderRequest;
import com.sparta.fmdelivery.domain.order.dto.request.OrderStatusRequest;
import com.sparta.fmdelivery.domain.order.dto.response.OrderDetailResponse;
import com.sparta.fmdelivery.domain.order.dto.response.OrderListResponse;
import com.sparta.fmdelivery.domain.order.dto.response.OrderResponse;
import com.sparta.fmdelivery.domain.order.entity.Order;
import com.sparta.fmdelivery.domain.order.entity.OrderMenu;
import com.sparta.fmdelivery.domain.order.entity.PointHistory;
import com.sparta.fmdelivery.domain.order.enums.OrderStatus;
import com.sparta.fmdelivery.domain.order.enums.PayMethod;
import com.sparta.fmdelivery.domain.order.dto.SimpleMenu;
import com.sparta.fmdelivery.domain.order.repository.OrderMenuRepository;
import com.sparta.fmdelivery.domain.order.repository.OrderRepository;
import com.sparta.fmdelivery.domain.order.repository.PointHistoryRepository;

import com.sparta.fmdelivery.domain.shop.entitiy.Shop;
import com.sparta.fmdelivery.domain.shop.repository.ShopRepository;
import com.sparta.fmdelivery.domain.user.entity.User;
import com.sparta.fmdelivery.domain.user.enums.UserRole;
import com.sparta.fmdelivery.domain.user.repository.UserRepository;
import com.sparta.fmdelivery.exception.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ShopRepository shopRepository;
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderMenuRepository orderMenuRepository;
    private final PointHistoryRepository pointHistoryRepository;


    @Transactional
    public OrderResponse saveOrder(AuthUser authUser, OrderRequest orderRequest) {
        User user = getUserById(authUser);
        Cart cart = getCartById(authUser.getId());
        Shop shop = getShopById(cart.getShopId());

        // 가게 최소금액에 못 미칠 경우
        if (orderRequest.getTotalPrice() < shop.getMinAmount()){
            throw new ApiException(ErrorStatus._BAD_REQUEST_ORDER_AMOUNT);
        }

        // 가게 오픈/마감 시간이 아닐경우
        LocalTime orderTime = orderRequest.getOrderTime();
        if(orderTime.isBefore(shop.getOpenedAt()) || orderTime.isAfter(shop.getClosedAt())){
            throw new ApiException(ErrorStatus._BAD_REQUEST_ORDER_TIME);
        }

        Order order = new Order(
                orderRequest.getTotalPrice(),
                PayMethod.fromString(orderRequest.getPayMethod()),
                orderRequest.getUsedPoint(),
                user
        );
        orderRepository.save(order);

        OrderMenu orderMenu = new OrderMenu( cart.getMenu(), order, shop);
        orderMenuRepository.save(orderMenu);

        // 사용 포인트가 0이 아닌 경우(포인트 사용)
        if(orderRequest.getUsedPoint() != 0){
            // 사용자의 기존 포인트
            PointHistory prePoint = pointHistoryRepository
                    .findTopByUserIdOrderByCreatedAtDesc(user.getId()).orElseThrow(
                            ()-> new ApiException(ErrorStatus._NOT_FOUND_POINT_HISTORY));
            int userAmount = prePoint.getPoint();
            // 포인트 기록 생성
            PointHistory pointHistory = new PointHistory(
                    orderRequest.getUsedPoint(),
                    userAmount + orderRequest.getUsedPoint(),
                    user
            );
            pointHistoryRepository.save(pointHistory);
        }

        cartRepository.deleteById(cart.getUserId());
        return new OrderResponse(order.getId(), shop.getId());
    }



    @Transactional
    public Long updateOrder(AuthUser authUser, Long orderId) {

        // 사장이 아닌 사용자일 경우 예외발생
        isValidOwner(authUser);

        // 주문 상태를 수락으로 변경
        Order order = getOrderById(orderId);
        order.changeStatus(OrderStatus.ACCEPTED);
        orderRepository.save(order);

        return order.getId();
    }


    public Long updateOrderStatus(AuthUser authUser, Long orderId, OrderStatusRequest orderStatusRequest) {

        // 사장이 아닌 사용자일 경우 예외발생
        isValidOwner(authUser);

        // 주문 상태 변경
        Order order = getOrderById(orderId);
        order.changeStatus(OrderStatus.fromString(orderStatusRequest.getStatus()));
        orderRepository.save(order);

        // 주문 상태가 배달 완료이면 포인트 적립
        if (OrderStatus.fromString(orderStatusRequest.getStatus()).equals(OrderStatus.DELIVERED)) {
            // 사용자의 기존 포인트
            PointHistory prePoint = pointHistoryRepository
                    .findTopByUserIdOrderByCreatedAtDesc(order.getUser().getId()).orElseThrow(
                            ()-> new ApiException(ErrorStatus._NOT_FOUND_POINT_HISTORY));
            int userAmount = prePoint.getPoint();

            // 적립 포인트
            int point = (int) Math.round(order.getTotalPrice() * 0.03);
            // 포인트 기록 생성
            PointHistory pointHistory = new PointHistory(
                    point,
                    userAmount + point,
                    order.getUser()
            );
            pointHistoryRepository.save(pointHistory);
        }

        return order.getId();
    }


    public List<OrderListResponse> getAllOrders(AuthUser authUser) {
        User user = getUserById(authUser);

        List<Order> orders = orderRepository.findAllByUserId(user.getId()).orElseThrow(
                () -> new ApiException(ErrorStatus._NOT_FOUND_ORDER_LIST));

        // 목록 별 OrderListResponse 객체 생성
        List<OrderListResponse> orderListResponses = orders.stream().map(order -> {
            // 가게 id 조회
            Long shopId = orderMenuRepository.findAllByOrderId(order.getId())
                    .orElseThrow(()->new ApiException(ErrorStatus._NOT_FOUND_ORDER_MENU_LIST))
                    .getShop().getId();

            // 가게 이름 조회
            String shopName = shopRepository.findById(shopId)
                    .orElseThrow(() -> new ApiException(ErrorStatus._NOT_FOUND_SHOP))
                    .getName();

            // OrderListResponse 생성
            return OrderListResponse.fromEntity(order, shopName);
        }).toList();

        return orderListResponses;
    }

    public OrderDetailResponse getOrder(Long orderId) {
        Order order = getOrderById(orderId);
        OrderMenu orderMenu = getOrderMenuByOrderId(orderId);
        Shop shop = getShopById(orderMenu.getShop().getId());

        // shop, SimpleMenu, order -> OrderDetailResponse 생성
        List<SimpleMenu> simpleMenuList = orderMenu.getMenuIdList().stream()
                .map(menuIdList -> {
                    Menu savedMenu = getMenuById(menuIdList.getMenuId());
                    return new SimpleMenu(savedMenu.getId(), savedMenu.getName(), savedMenu.getPrice(), menuIdList.getCount());
                }).toList();
        return OrderDetailResponse.fromEntity(shop, simpleMenuList, order);
    }


    private User getUserById(AuthUser authUser) {
        return userRepository.findById(authUser.getId()).orElseThrow(
                () -> new ApiException(ErrorStatus._NOT_FOUND_USER)
        );
    }

    private Shop getShopById(Long shopId) {
        return shopRepository.findById(shopId).orElseThrow(
                () -> new ApiException(ErrorStatus._NOT_FOUND_SHOP)
        );
    }

    private Cart getCartById(Long userId) {
        return cartRepository.findById(userId).orElseThrow(
                () -> new ApiException(ErrorStatus._NOT_FOUND_CART)
        );
    }

    private Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(
                () -> new ApiException(ErrorStatus._NOT_FOUND_ORDER)
        );
    }

    private Menu getMenuById(Long menuId) {
        return menuRepository.findById(menuId).orElseThrow(
                () -> new ApiException(ErrorStatus._NOT_FOUND_MENU)
        );
    }

    private OrderMenu getOrderMenuByOrderId(Long orderId) {
        return orderMenuRepository.findById(orderId).orElseThrow(
                () -> new ApiException(ErrorStatus._NOT_FOUND_ORDER_MENU_LIST)
        );
    }

    private static void isValidOwner(AuthUser authUser) {
        if (!authUser.getUserRole().equals(UserRole.OWNER)) {
            throw new ApiException(ErrorStatus._BAD_REQUEST_UPDATE_STATUS);
        }
    }

}
