package com.sparta.fmdelivery.domain.shop.service;

import com.sparta.fmdelivery.domain.shop.dto.request.ShopCreateRequest;
import com.sparta.fmdelivery.domain.shop.dto.request.ShopDeleteRequest;
import com.sparta.fmdelivery.domain.shop.dto.request.ShopUpdateRequest;
import com.sparta.fmdelivery.domain.shop.dto.response.ShopResponse;
import com.sparta.fmdelivery.domain.shop.entitiy.Shop;
import com.sparta.fmdelivery.domain.shop.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ShopService {

    private final ShopRepository shopRepository;

    /*
       가게 생성
     */
    @Transactional
    public ShopResponse createShop(ShopCreateRequest request) {

        Shop shop = new Shop(request);

        return ShopResponse.of(shopRepository.save(shop));
    }

    /*
       가게 단건 조회하기
     */
    public ShopResponse getShop(Long id) {

        return ShopResponse.of(shopRepository.findById(id).orElseThrow(
                () -> new NullPointerException("없는 가게입니다.")
        ));
    }

    /*
       가게 리스트 조회하기
     */
    public List<ShopResponse> getShopList(Long id) {

        List<Shop> shopList = shopRepository.findAllByUserId(id);

        return new ArrayList<>(shopList.stream()
                .map(ShopResponse::of).toList());
    }

    /*
        가게 삭제
        soft delete 로 boolean 값만 false 로 변경.
     */
    public void deleteShop(ShopDeleteRequest request) {

        Shop shop = getShopById(request.getShopId());

        shop.softDelete();
    }

    /*
        가게 정보 업데이트
     */
    @Transactional
    public ShopResponse updateShop(Long id, ShopUpdateRequest request) {

        Shop shop = getShopById(id);

        shop.changeName(request.getShopName());
        shop.changeOpenedAt(request.getOpenedAt());
        shop.changeClosedAt(request.getClosedAt());

        return ShopResponse.of(shop);
    }

    private Shop getShopById(Long id) {

        return shopRepository.findById(id).orElseThrow(
                () -> new NullPointerException("없는 가게입니다.")
        );
    }
}
