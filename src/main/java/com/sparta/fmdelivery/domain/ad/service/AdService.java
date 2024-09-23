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

    /**
     * 광고 생성 로직:
     * 1. 관리자 권한 검증: ADMIN 권한이 없으면 예외 발생.
     * 2. 가게 조회: 가게 ID로 가게를 찾고, 없으면 예외 발생.
     * 3. 광고 객체 생성: 가게 ID, 시작일, 종료일, 상태로 광고 객체를 생성.
     * 4. 광고 저장: 생성된 광고 객체를 데이터베이스에 저장.
     * 5. 응답 반환: 저장된 광고 정보를 AdResponse로 반환.
     */
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

    /**
     * 광고 수정 로직:
     * 1. 관리자 권한 검증: ADMIN 권한이 없으면 예외 발생.
     * 2. 광고 조회: 광고 ID로 광고를 찾고, 없으면 예외 발생.
     * 3. 광고 상태 업데이트: 전달받은 상태로 광고 상태를 업데이트.
     * 4. 응답 반환: 변경된 광고 정보를 AdChangeResponse로 반환.
     */
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

    /**
     * 관리자 권한 검증 로직:
     * 1. 입력된 사용자 정보에서 권한(UserRole)을 확인.
     * 2. 사용자가 ADMIN 권한이 아닐 경우, ApiException을 발생시켜 접근 금지(FORBIDDEN) 오류를 반환.
     */
    private void validateAdminRole(AuthUser authUser) {
        if (!authUser.getUserRole().equals(UserRole.ADMIN)) {
            throw new ApiException(ErrorStatus._FORBIDDEN);
        }
    }
}
