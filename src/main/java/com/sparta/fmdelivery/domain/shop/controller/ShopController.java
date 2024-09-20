package com.sparta.fmdelivery.domain.shop.controller;

import com.sparta.fmdelivery.domain.shop.dto.request.ShopCreateRequest;
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
    public ResponseEntity<ShopResponse> createShop(ShopCreateRequest request) {

        return ResponseEntity.ok().body(shopService.createShop(request));
    }

    @GetMapping("/shops/{id}")
    public ResponseEntity<ShopResponse> getShop(@PathVariable Long id) {

        return ResponseEntity.ok().body(shopService.getShop(id));
    }

    @GetMapping("/shops/users/{id}")
    public ResponseEntity<List<ShopResponse>> getShopList(@PathVariable Long id) {

        return ResponseEntity.ok().body(shopService.getShopList(id));
    }

    @PostMapping("/shops/{id}")
    public ResponseEntity<ShopResponse> updateShop(@PathVariable Long id, ShopUpdateRequest request) {

        return ResponseEntity.ok().body(shopService.updateShop(id, request));
    }

    @GetMapping("/shops/{shopId}")
    public ResponseEntity<String> deleteShop(@PathVariable Long shopId) {

        shopService.deleteShop(shopId);
        return ResponseEntity.ok().body("가게가 삭제 되었습니다.");
    }
 }
