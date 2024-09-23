package com.sparta.fmdelivery.domain.cart.controller;

import com.sparta.fmdelivery.apipayload.ApiResponse;
import com.sparta.fmdelivery.domain.cart.dto.CartResponse;
import com.sparta.fmdelivery.domain.cart.dto.CartRequest;
import com.sparta.fmdelivery.domain.cart.entity.Cart;
import com.sparta.fmdelivery.domain.cart.service.CartService;
import com.sparta.fmdelivery.domain.common.annotation.Auth;
import com.sparta.fmdelivery.domain.common.dto.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class CartController {
    private final CartService cartService;

    // 장바구니에 메뉴 추가
    @PostMapping("/carts")
    public ApiResponse<CartResponse> saveCart(@Auth AuthUser authUser,
                                              @RequestBody CartRequest cartRequest) {
        return ApiResponse.onSuccess(cartService.saveCart(authUser, cartRequest));
    }

    // 장바구니 조회
    @GetMapping("/carts")
    public ApiResponse<CartResponse> getCart(@Auth AuthUser authUser) {
        return ApiResponse.onSuccess(cartService.getCart(authUser));
    }

    // 장바구니 삭제(비우기)
    @PatchMapping("/carts")
    public ApiResponse<String> deleteCart(@Auth AuthUser authUser){
        cartService.delete(authUser);
        return ApiResponse.onSuccess("장바구니가 삭제되었습니다.");
    }

}
