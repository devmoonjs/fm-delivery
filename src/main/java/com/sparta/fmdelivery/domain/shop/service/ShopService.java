package com.sparta.fmdelivery.domain.shop.service;

import com.sparta.fmdelivery.apipayload.status.ErrorStatus;
import com.sparta.fmdelivery.common.dto.AuthUser;
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

    @Transactional
    public ShopResponse createShop(AuthUser authUser, ShopCreateRequest request) {

        isValidOwner(authUser);
        User user = getUserById(authUser);
        Shop shop = new Shop(user, request);

        return ShopResponse.of(shopRepository.save(shop));
    }

    public ShopResponse getShop(Long id) {

        return ShopResponse.of(getShopById(id));
    }

    public List<ShopResponse> getShopList() {

        List<Shop> adShops = shopRepository.findAdShops();
        List<Shop> regularShops = shopRepository.findRegularShops();

        List<ShopResponse> adShopResponses = adShops.stream()
                .map(ShopResponse::of)
                .toList();

        List<ShopResponse> regularShopResponses = regularShops.stream()
                .map(ShopResponse::of)
                .toList();

        List<ShopResponse> result = new ArrayList<>();
        result.addAll(adShopResponses);
        result.addAll(regularShopResponses);

        return result;
    }

    @Transactional
    public void deleteShop(AuthUser authUser, ShopDeleteRequest request) {

        Shop shop = getShopById(request.getShopId());
        isShopOwner(authUser, shop);
        shop.shutDown();
    }

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
