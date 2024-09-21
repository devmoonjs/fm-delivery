package com.sparta.fmdelivery.domain.shop.controller;

import com.sparta.fmdelivery.apipayload.ApiResponse;
import com.sparta.fmdelivery.domain.common.annotation.Auth;
import com.sparta.fmdelivery.domain.common.dto.AuthUser;
import com.sparta.fmdelivery.domain.shop.dto.request.ShopCreateRequest;
import com.sparta.fmdelivery.domain.shop.dto.request.ShopDeleteRequest;
import com.sparta.fmdelivery.domain.shop.dto.request.ShopUpdateRequest;
import com.sparta.fmdelivery.domain.shop.dto.response.ShopResponse;
import com.sparta.fmdelivery.domain.shop.service.ShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ShopController {

    private final ShopService shopService;

    @PostMapping("/shops")
    public ApiResponse<ShopResponse> createShop(@Auth AuthUser authuser, @RequestBody ShopCreateRequest request) {

        return ApiResponse.onSuccess(shopService.createShop(authuser, request));
    }

    @GetMapping("/shops/{id}")
    public ApiResponse<ShopResponse> getShop(@PathVariable Long id) {

        return ApiResponse.onSuccess(shopService.getShop(id));
    }

    @GetMapping("/shops")
    public ApiResponse<List<ShopResponse>> getShopList() {

        return ApiResponse.onSuccess(shopService.getShopList());
    }

    @PutMapping("/shops")
    public ApiResponse<ShopResponse> updateShop(@Auth AuthUser authUser, @RequestBody ShopUpdateRequest request) {

        return ApiResponse.onSuccess(shopService.updateShop(authUser, request));
    }

    @DeleteMapping("/shops")
    public ApiResponse<String> deleteShop(@Auth AuthUser authUser, @RequestBody ShopDeleteRequest request) {

        shopService.deleteShop(authUser, request);
        return ApiResponse.onSuccess("가게가 삭제 되었습니다.");
    }
 }
