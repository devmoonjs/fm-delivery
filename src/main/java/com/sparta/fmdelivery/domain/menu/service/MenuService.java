package com.sparta.fmdelivery.domain.menu.service;

import com.sparta.fmdelivery.domain.menu.dto.MenuRequest;
import com.sparta.fmdelivery.domain.menu.dto.MenuResponse;
import com.sparta.fmdelivery.domain.menu.entity.Menu;
import com.sparta.fmdelivery.domain.menu.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MenuService {

    private final MenuRepository menuRepository;

    /**
     * 메뉴 생성
     * @param request
     * @return MenuResponse
     */
    @Transactional
    public MenuResponse createMenu(MenuRequest request) {
        Menu menu = new Menu(request);
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
    public MenuResponse updateMenu(Long menuId, MenuRequest request) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new IllegalArgumentException("메뉴를 찾을 수 없습니다.")); //커스텀 예외 만들거임

        menu.changeName(request.getName());
        menu.changePrice(request.getPrice());
        menu.changeStatus(request.getStatus());

        return MenuResponse.fromEntity(menu);
    }

    /**
     * 메뉴 삭제
     * @param menuId
     */
    @Transactional
    public void deleteMenu(Long menuId) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new IllegalArgumentException("메뉴를 찾을 수 없습니다.")); //커스텀 예외 만들거임

        menuRepository.delete(menu);
    }
}