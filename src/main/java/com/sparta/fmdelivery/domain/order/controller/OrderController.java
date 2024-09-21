package com.sparta.fmdelivery.domain.order.controller;

import com.sparta.fmdelivery.domain.common.annotation.Auth;
import com.sparta.fmdelivery.domain.common.dto.AuthUser;
import com.sparta.fmdelivery.domain.order.dto.request.OrderRequest;
import com.sparta.fmdelivery.domain.order.dto.response.OrderDetailResponse;
import com.sparta.fmdelivery.domain.order.dto.response.OrderListResponse;
import com.sparta.fmdelivery.domain.order.dto.response.OrderResponse;
import com.sparta.fmdelivery.domain.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@Auth AuthUser authUser,
                                                     @RequestBody OrderRequest orderRequest) {
        return ResponseEntity.ok(orderService.saveOrder(authUser, orderRequest));
    }

    @PatchMapping("/{orderId}")
    public ResponseEntity<OrderResponse> acceptOrder(@Auth AuthUser authUser,
                                                     @PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.updateOrder(authUser, orderId));
    }

    @GetMapping
    public ResponseEntity<List<OrderListResponse>> getAllOrders(@Auth AuthUser authUser) {
        return ResponseEntity.ok(orderService.getAllOrders(authUser));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDetailResponse> getOrder(@Auth AuthUser authUser,
                                                        @PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getOrder(authUser, orderId));
    }
}
