package com.sparta.fmdelivery.domain.ad.controller;

import com.sparta.fmdelivery.apipayload.ApiResponse;
import com.sparta.fmdelivery.domain.ad.dto.request.AdChangeRequest;
import com.sparta.fmdelivery.domain.ad.dto.request.AdSaveRequest;
import com.sparta.fmdelivery.domain.ad.dto.response.AdChangeResponse;
import com.sparta.fmdelivery.domain.ad.dto.response.AdResponse;
import com.sparta.fmdelivery.domain.ad.service.AdService;
import com.sparta.fmdelivery.domain.common.annotation.Auth;
import com.sparta.fmdelivery.domain.common.dto.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/ads")
public class AdController {

    private final AdService adService;

    @PostMapping
    public ApiResponse<AdResponse> createAd(@Auth AuthUser authUser, @RequestBody AdSaveRequest adSaveRequest) {
        return ApiResponse.onSuccess(adService.createAd(authUser, adSaveRequest));
    }

    @PutMapping("/{adId}")
    public ApiResponse<AdChangeResponse> updateAd(@Auth AuthUser authUser, @PathVariable Long adId, @RequestBody AdChangeRequest adChangeRequest) {
        return ApiResponse.onSuccess(adService.updateAd(authUser, adId, adChangeRequest));
    }
}
