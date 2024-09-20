package com.sparta.fmdelivery.domain.shop.controller;

import com.sparta.fmdelivery.domain.common.annotation.Auth;
import com.sparta.fmdelivery.domain.common.dto.AuthUser;
import com.sparta.fmdelivery.domain.shop.dto.request.ShopCreateRequest;
import com.sparta.fmdelivery.domain.shop.dto.request.ShopDeleteRequest;
import com.sparta.fmdelivery.domain.shop.dto.request.ShopUpdateRequest;
import com.sparta.fmdelivery.domain.shop.dto.response.ShopResponse;
import com.sparta.fmdelivery.domain.shop.service.ShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ShopController {

    private final ShopService shopService;

    @PostMapping("/shops")
    public ResponseEntity<ShopResponse> createShop(@Auth AuthUser authuser, @RequestBody ShopCreateRequest request) {

        return ResponseEntity.ok().body(shopService.createShop(authuser, request));
    }

    @GetMapping("/shops/{id}")
    public ResponseEntity<ShopResponse> getShop(@PathVariable Long id) {

        return ResponseEntity.ok().body(shopService.getShop(id));
    }

    @GetMapping("/shops")
    public ResponseEntity<List<ShopResponse>> getShopList() {

        return ResponseEntity.ok().body(shopService.getShopList());
    }

    @PutMapping("/shops")
    public ResponseEntity<ShopResponse> updateShop(@Auth AuthUser authUser, @RequestBody ShopUpdateRequest request) {

        return ResponseEntity.ok().body(shopService.updateShop(authUser, request));
    }

    @DeleteMapping("/shops")
    public ResponseEntity<String> deleteShop(@Auth AuthUser authUser, @RequestBody ShopDeleteRequest request) {

        shopService.deleteShop(authUser, request);
        return ResponseEntity.ok().body("가게가 삭제 되었습니다.");
    }
 }
