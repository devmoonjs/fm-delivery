package com.sparta.fmdelivery.domain.menu.service;

import com.sparta.fmdelivery.apipayload.status.ErrorStatus;
import com.sparta.fmdelivery.domain.common.dto.AuthUser;
import com.sparta.fmdelivery.domain.menu.dto.MenuRequest;
import com.sparta.fmdelivery.domain.menu.dto.MenuResponse;
import com.sparta.fmdelivery.domain.menu.entity.Menu;
import com.sparta.fmdelivery.domain.menu.repository.MenuRepository;
import com.sparta.fmdelivery.domain.shop.entitiy.Shop;
import com.sparta.fmdelivery.domain.shop.repository.ShopRepository;
import com.sparta.fmdelivery.exception.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final ShopRepository shopRepository;

    /**
     * 메뉴 생성
     * @param authUser
     * @param request
     * @return MenuResponse
     */
    @Transactional
    public MenuResponse createMenu(AuthUser authUser, MenuRequest request) {
        Shop shop = getValidatedShop(request.getShopId(), authUser);

        Menu menu = new Menu(request, shop);
        return MenuResponse.fromEntity(menuRepository.save(menu));
    }

    /**
     * 가게 메뉴 조회
     * @param shopId
     * @return List<MenuResponse>
     */
    @Transactional(readOnly = true)
    public List<MenuResponse> getMenus(Long shopId) {
        return menuRepository.findAllByShopId(shopId).stream()
                .map(MenuResponse::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * 메뉴 업데이트
     * @param authUser
     * @param menuId
     * @param request
     * @return MenuResponse
     */
    @Transactional
    public MenuResponse updateMenu(AuthUser authUser, Long menuId, MenuRequest request) {
        Shop shop = getValidatedShop(request.getShopId(), authUser);
        Menu menu = getValidatedMenu(menuId);

        menu.updateName(request.getName(), request.getPrice(), request.getStatus());
        return MenuResponse.fromEntity(menu);
    }

    /**
     * 메뉴 삭제
     * @param authUser
     * @param menuId
     * @param shopId
     */
    @Transactional
    public void deleteMenu(AuthUser authUser, Long menuId, Long shopId) {
        Shop shop = getValidatedShop(shopId, authUser);
        Menu menu = getValidatedMenu(menuId);

        menuRepository.delete(menu);
    }

    /**
     * 가게 유효성 검증 및 사장님 권한 확인
     * @param shopId
     * @param authUser
     * @return Shop
     */
    private Shop getValidatedShop(Long shopId, AuthUser authUser) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new ApiException(ErrorStatus._NOT_FOUND_SHOP));
        validateOwner(shop, authUser);
        return shop;
    }

    /**
     * 메뉴 유효성 검증
     * @param menuId
     * @return Menu
     */
    private Menu getValidatedMenu(Long menuId) {
        return menuRepository.findById(menuId)
                .orElseThrow(() -> new ApiException(ErrorStatus._NOT_FOUND_MENU));
    }

    /**
     * 사장님 권한 검증
     * @param shop
     * @param authUser
     */
    private void validateOwner(Shop shop, AuthUser authUser) {
        if (!shop.getUser().getId().equals(authUser.getId())) {
            throw new ApiException(ErrorStatus._BAD_REQUEST_UPDATE_SHOP);
        }
    }
}
