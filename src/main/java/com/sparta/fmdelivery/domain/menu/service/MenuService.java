package com.sparta.fmdelivery.domain.menu.service;

import com.sparta.fmdelivery.domain.common.dto.AuthUser;
import com.sparta.fmdelivery.domain.menu.dto.MenuRequest;
import com.sparta.fmdelivery.domain.menu.dto.MenuResponse;
import com.sparta.fmdelivery.domain.menu.entity.Menu;
import com.sparta.fmdelivery.domain.menu.repository.MenuRepository;
import com.sparta.fmdelivery.domain.shop.entitiy.Shop;
import com.sparta.fmdelivery.domain.shop.repository.ShopRepository;
import com.sparta.fmdelivery.domain.user.enums.UserRole;
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
     * @param request
     * @return MenuResponse
     */
    @Transactional
    public MenuResponse createMenu(AuthUser authUser, MenuRequest request) {
        isValidOwner(authUser);  // 사장님인지 확인하는 메서드

        Shop shop = shopRepository.findById(request.getShopId())
                .orElseThrow(() -> new IllegalArgumentException("가게가 존재하지 않습니다."));
        Menu menu = new Menu(request, shop);

        return MenuResponse.fromEntity(menuRepository.save(menu));
    }


    /**
     * 가게 메뉴 조회
     * @param shopId
     * @return List<MenuResponse>
     */
    @Transactional
    public List<MenuResponse> getMenus(Long shopId) {
        List<Menu> menus = menuRepository.findAllByShopId(shopId);

        return menus.stream()
                .map(MenuResponse::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * 메뉴 업데이트
     * @param menuId
     * @param request
     * @return MenuResponse
     */
    @Transactional
    public MenuResponse updateMenu(AuthUser authUser, Long menuId, MenuRequest request) {
        isValidOwner(authUser);

        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new IllegalArgumentException("메뉴를 찾을 수 없습니다.")); //커스텀 예외 만들거임

        menu.updateName(request.getName(), request.getPrice(), request.getStatus());
        return MenuResponse.fromEntity(menu);
    }

    /**
     * 메뉴 삭제
     * @param menuId
     */
    @Transactional
    public void deleteMenu(AuthUser authUser, Long menuId) {
        isValidOwner(authUser);

        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new IllegalArgumentException("메뉴를 찾을 수 없습니다.")); //커스텀 예외 만들거임

        menuRepository.delete(menu);
    }

    /**
     * 사장님인지 확인하는 메서드
     * @param authUser
     */
    private static void isValidOwner(AuthUser authUser) {
        if (authUser.getUserRole().equals(UserRole.USER)) {
            throw new IllegalAccessError("사장님만 접근이 가능합니다.");
        }
    }
}