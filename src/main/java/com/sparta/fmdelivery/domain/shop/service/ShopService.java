package com.sparta.fmdelivery.domain.shop.service;

import com.sparta.fmdelivery.domain.common.dto.AuthUser;
import com.sparta.fmdelivery.domain.shop.dto.request.ShopCreateRequest;
import com.sparta.fmdelivery.domain.shop.dto.request.ShopDeleteRequest;
import com.sparta.fmdelivery.domain.shop.dto.request.ShopUpdateRequest;
import com.sparta.fmdelivery.domain.shop.dto.response.ShopResponse;
import com.sparta.fmdelivery.domain.shop.entitiy.Shop;
import com.sparta.fmdelivery.domain.shop.repository.ShopRepository;
import com.sparta.fmdelivery.domain.user.entity.User;
import com.sparta.fmdelivery.domain.user.enums.UserRole;
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
    public ShopResponse createShop(AuthUser authUser, ShopCreateRequest request) {

        isValidOwner(authUser);

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
    public List<ShopResponse> getShopList() {

        List<Shop> shopList = shopRepository.findAll();

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
    public ShopResponse updateShop(AuthUser authUser, ShopUpdateRequest request) {
        
        Shop shop = getShopById(request.getShopId());

        isShopOwner(authUser, shop);

        shop.changeName(request.getShopName());
        shop.changeOpenedAt(request.getOpenedAt());
        shop.changeClosedAt(request.getClosedAt());

        return ShopResponse.of(shop);
    }

    private static void isShopOwner(AuthUser authUser, Shop shop) {
        if (!shop.getUserId().equals(authUser.getId())) {
            throw new RuntimeException("본인 가게만 수정가능합니다.");
        }
    }

    private Shop getShopById(Long id) {

        return shopRepository.findById(id).orElseThrow(
                () -> new NullPointerException("없는 가게입니다.")
        );
    }

    private static void isValidOwner(AuthUser authUser) {
        if (authUser.getUserRole().equals(UserRole.USER)) {
            throw new IllegalAccessError("사장님 계정만 가게 생성이 가능합니다.");
        }
    }
}
