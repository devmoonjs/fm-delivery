package com.sparta.fmdelivery.domain.ad.service;

import com.sparta.fmdelivery.apipayload.status.ErrorStatus;
import com.sparta.fmdelivery.domain.ad.dto.request.AdChangeRequest;
import com.sparta.fmdelivery.domain.ad.dto.request.AdSaveRequest;
import com.sparta.fmdelivery.domain.ad.dto.response.AdChangeResponse;
import com.sparta.fmdelivery.domain.ad.dto.response.AdResponse;
import com.sparta.fmdelivery.domain.ad.entity.Ads;
import com.sparta.fmdelivery.domain.ad.repository.AdRepository;
import com.sparta.fmdelivery.domain.common.dto.AuthUser;
import com.sparta.fmdelivery.domain.shop.entitiy.Shop;
import com.sparta.fmdelivery.domain.shop.repository.ShopRepository;
import com.sparta.fmdelivery.domain.user.enums.UserRole;
import com.sparta.fmdelivery.exception.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdService {

    private final AdRepository adRepository;
    private final ShopRepository shopRepository;

    @Transactional
    public AdResponse createAd(AuthUser authUser, AdSaveRequest adSaveRequest) {
        validateAdminRole(authUser);

        Shop shop = shopRepository.findById(adSaveRequest.getShopId()).orElseThrow(
                () -> new ApiException(ErrorStatus._NOT_FOUND_SHOP)
        );

        Ads ads = new Ads(shop.getId(), adSaveRequest.getStartDate(), adSaveRequest.getEndDate(), adSaveRequest.isStatus());
        Ads savedAd = adRepository.save(ads);

        return new AdResponse(
                savedAd.getId(),
                shop.getId(),
                shop.getName(),
                "광고 생성",
                savedAd.isStatus() ? "활성" : "비활성",
                savedAd.getStartDate(),
                savedAd.getEndDate()
        );
    }

    @Transactional
    public AdChangeResponse updateAd(AuthUser authUser, Long adId, AdChangeRequest adChangeRequest) {
        validateAdminRole(authUser);

        Ads ads = adRepository.findById(adId).orElseThrow(
                () -> new ApiException(ErrorStatus._NOT_FOUND_AD)
        );

        ads.update(adChangeRequest.isStatus());

        return new AdChangeResponse(
                ads.getId(),
                ads.getShopId(),
                "광고 상태 변경",
                ads.isStatus() ? "활성" : "비활성",
                ads.getStartDate(),
                ads.getEndDate()
        );
    }

    private void validateAdminRole(AuthUser authUser) {
        if (!authUser.getUserRole().equals(UserRole.ADMIN)) {
            throw new ApiException(ErrorStatus._FORBIDDEN);
        }
    }
}
