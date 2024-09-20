package com.sparta.fmdelivery.domain.shop.service;

import com.sparta.fmdelivery.apipayload.status.ErrorStatus;
import com.sparta.fmdelivery.domain.common.dto.AuthUser;
import com.sparta.fmdelivery.domain.shop.dto.request.ShopCreateRequest;
import com.sparta.fmdelivery.domain.shop.dto.request.ShopDeleteRequest;
import com.sparta.fmdelivery.domain.shop.dto.request.ShopUpdateRequest;
import com.sparta.fmdelivery.domain.shop.dto.response.ShopResponse;
import com.sparta.fmdelivery.domain.shop.entitiy.Shop;
import com.sparta.fmdelivery.domain.shop.repository.ShopRepository;
import com.sparta.fmdelivery.domain.user.entity.User;
import com.sparta.fmdelivery.domain.user.enums.UserRole;
import com.sparta.fmdelivery.domain.user.repository.UserRepository;
import com.sparta.fmdelivery.exception.ApiException;
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
    private final UserRepository userRepository;

    /*
       가게 생성
       1. 유저 계정이 '사장님(OWNER)' 계정인지 유효성 체크
       2. '사장님(OWNER)' 계정이라면 가게 생성 진행
     */
    @Transactional
    public ShopResponse createShop(AuthUser authUser, ShopCreateRequest request) {

        isValidOwner(authUser);

        User user = getUserById(authUser);

        Shop shop = new Shop(user, request);

        return ShopResponse.of(shopRepository.save(shop));
    }

    /*
       가게 단건 조회하기
     */
    public ShopResponse getShop(Long id) {

        return ShopResponse.of(getShopById(id));
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
        본인 가게인지 유효성 체크 후, soft delete 진행
     */
    @Transactional
    public void deleteShop(AuthUser authUser, ShopDeleteRequest request) {

        Shop shop = getShopById(request.getShopId());

        isShopOwner(authUser, shop);

        shop.softDelete();
    }

    /*
        가게 정보 업데이트
        본인 가게인지 유효성 체크 후, 업데이트 진행
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

        if (!shop.getUser().getId().equals(authUser.getId())) {
            throw new ApiException(ErrorStatus._BAD_REQUEST_UPDATE_SHOP);
        }
    }

    private User getUserById(AuthUser authUser) {

        return userRepository.findById(authUser.getId()).orElseThrow(
                () -> new ApiException(ErrorStatus._NOT_FOUND_USER)
        );
    }

    private Shop getShopById(Long id) {

        return shopRepository.findById(id).orElseThrow(
                () -> new ApiException(ErrorStatus._NOT_FOUND_SHOP)
        );
    }

    private static void isValidOwner(AuthUser authUser) {

        if (authUser.getUserRole().equals(UserRole.USER)) {
            throw new ApiException(ErrorStatus._BAD_REQUEST_CREATE_SHOP);
        }
    }
}
