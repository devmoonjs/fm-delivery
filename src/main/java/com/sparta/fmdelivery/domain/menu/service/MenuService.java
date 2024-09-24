package com.sparta.fmdelivery.domain.menu.service;

import com.sparta.fmdelivery.apipayload.status.ErrorStatus;
import com.sparta.fmdelivery.config.S3ClientUtility;
import com.sparta.fmdelivery.domain.common.dto.AuthUser;
import com.sparta.fmdelivery.domain.menu.dto.MenuRequest;
import com.sparta.fmdelivery.domain.menu.dto.MenuResponse;
import com.sparta.fmdelivery.domain.menu.entity.Menu;
import com.sparta.fmdelivery.domain.menu.repository.MenuRepository;
import com.sparta.fmdelivery.domain.shop.entitiy.Shop;
import com.sparta.fmdelivery.domain.shop.service.ShopService;
import com.sparta.fmdelivery.exception.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final ShopService shopService;
    private final S3ClientUtility s3ClientUtility;

    private final String MENU_IMG_DIR = "menu/";

    @Transactional
    public MenuResponse createMenu(AuthUser authUser, MenuRequest request, MultipartFile multipartFile) {
        Shop shop = getValidatedShop(request.getShopId(), authUser);

        // 공통 S3 컴포넌트를 사용하여 이미지 업로드
        String imageUrl = s3ClientUtility.uploadFile(MENU_IMG_DIR, multipartFile);

        Menu menu = new Menu(request, shop, imageUrl);
        return MenuResponse.fromEntity(menuRepository.save(menu));
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> getMenus(Long shopId) {
        return menuRepository.findAllByShopId(shopId).stream()
                .map(MenuResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public MenuResponse updateMenu(AuthUser authUser, Long menuId, MenuRequest request, MultipartFile image) {
        Shop shop = getValidatedShop(request.getShopId(), authUser);
        Menu menu = getValidatedMenu(menuId);

        // 이미지가 있는 경우 새로운 이미지 업로드, 없으면 기존 이미지 유지
        String imageUrl = menu.getImageUrl(); // 기존 이미지 URL
        if (image != null && !image.isEmpty()) {
            imageUrl = s3ClientUtility.uploadFile(MENU_IMG_DIR, image); // 새 이미지 업로드
        }

        menu.updateMenu(request.getName(), request.getPrice(), request.getStatus(), imageUrl);
        return MenuResponse.fromEntity(menu);
    }

    @Transactional
    public void deleteMenu(AuthUser authUser, Long menuId, Long shopId) {
        Shop shop = getValidatedShop(shopId, authUser);
        Menu menu = getValidatedMenu(menuId);

        menuRepository.delete(menu);
    }

    // 상점 유효성 검증 로직
    private Shop getValidatedShop(Long shopId, AuthUser authUser) {
        Shop shop = shopService.getShopById(shopId);
        validateOwner(shop, authUser);
        return shop;
    }

    // 메뉴 유효성 검증 로직
    private Menu getValidatedMenu(Long menuId) {
        return menuRepository.findById(menuId)
                .orElseThrow(() -> new ApiException(ErrorStatus._NOT_FOUND_MENU));
    }

    // 상점 소유자 확인
    private void validateOwner(Shop shop, AuthUser authUser) {
        if (!shop.getUser().getId().equals(authUser.getId())) {
            throw new ApiException(ErrorStatus._BAD_REQUEST_UPDATE_SHOP);
        }
    }
}
