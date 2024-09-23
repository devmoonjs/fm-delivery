package com.sparta.fmdelivery.domain.cart.service;

import com.sparta.fmdelivery.apipayload.status.ErrorStatus;
import com.sparta.fmdelivery.domain.cart.dto.CartResponse;
import com.sparta.fmdelivery.domain.cart.dto.CartRequest;
import com.sparta.fmdelivery.domain.cart.entity.Cart;
import com.sparta.fmdelivery.domain.cart.repository.CartRepository;
import com.sparta.fmdelivery.domain.common.dto.AuthUser;
import com.sparta.fmdelivery.domain.menu.repository.MenuRepository;
import com.sparta.fmdelivery.domain.menu.entity.Menu;
import com.sparta.fmdelivery.domain.order.pojo.MenuIdList;
import com.sparta.fmdelivery.domain.order.pojo.SimpleMenu;
import com.sparta.fmdelivery.domain.shop.entitiy.Shop;
import com.sparta.fmdelivery.domain.shop.repository.ShopRepository;
import com.sparta.fmdelivery.domain.user.entity.User;
import com.sparta.fmdelivery.domain.user.repository.UserRepository;
import com.sparta.fmdelivery.exception.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ShopRepository shopRepository;
    private final MenuRepository menuRepository;

    /**
     * 장바구니에 메뉴 추가 기능
     * @param authUser : 사용자 정보
     * @param request : shopId, menuId, count 정보
     * @return CartResponse : 장바구니에 담긴 식당과 메뉴 정보
     */
    public CartResponse saveCart(AuthUser authUser, CartRequest request) {

        User user = getUserById(authUser);              // 사용자 확인
        Shop shop = getShopById(request.getShopId());   // 가게 확인
        Cart cart = getCartById(user.getId());          // 해당 사용자의 장바구니가 존재하는지 확인

        if(cart == null) {
            // 기존 장바구니가 없을 경우 - cart 생성
            List<MenuIdList> menuIdList = new ArrayList<>();    // menu id 담을 list 생성
            menuIdList.add(new MenuIdList(request.getMenuId(), request.getCount()));    // menu id, count 담기
            cart = new Cart(user.getId(), shop.getId(), menuIdList);                    // 저장할 cart 생성

        } else {
            // 기존 장바구니가 있을 경우 - cart에 새로운 메뉴 추가
            List<MenuIdList> menuIdList = cart.getMenu();

            // 이미 존재하는 메뉴인지 확인
            boolean menuExists = false;
            for (MenuIdList existingMenu : menuIdList) {
                if (existingMenu.getMenuId().equals(request.getMenuId())) {
                    // 이미 존재하는 메뉴면 count만 증가
                    existingMenu.updateCount(request.getCount());
                    menuExists = true;
                    break;
                }
            }
            // 존재하지 않는 메뉴면 새롭게 추가
            if (!menuExists) {
                menuIdList.add(new MenuIdList(request.getMenuId(), request.getCount()));
            }
        }

        cartRepository.save(cart);      // 장바구니 저장
        return getCartResponse(shop, cart);     // response DTO 생성 및 반환
    }

    /**
     * 장바구니의 조회 기능
     * @param authUser : 사용자 정보
     * @return CartResponse : 장바구니에 담긴 식당과 메뉴 정보
     */
    public CartResponse getCart(AuthUser authUser) {

        User user = getUserById(authUser);  // 사용자 확인
        Cart cart = getCartById(user.getId());  // 해당 사용자의 장바구니 조회

        // response DTO 생성 및 반환
        Shop shop = getShopById(cart.getShopId());
        return getCartResponse(shop, cart);
    }

    /**
     * 장바구니 삭제 기능
     * @param authUser : 사용자 정보
     */
    public void delete(AuthUser authUser) {

        User user = getUserById(authUser);  // 사용자 확인
        Cart cart = getCartById(user.getId());  // 해당 사용자의 장바구니 조회
        cartRepository.delete(cart);    // 장바구니 삭제
    }


    private User getUserById(AuthUser authUser) {

        return userRepository.findById(authUser.getId()).orElseThrow(
                () -> new ApiException(ErrorStatus._NOT_FOUND_USER)
        );
    }

    private Shop getShopById(Long shopId) {

        return shopRepository.findById(shopId).orElseThrow(
                () -> new ApiException(ErrorStatus._NOT_FOUND_SHOP)
        );
    }

    private Menu getMenuById(Long menuId) {

        return menuRepository.findById(menuId).orElseThrow(
                () -> new ApiException(ErrorStatus._NOT_FOUND_MENU)
        );
    }

    private Cart getCartById(Long userId) {

        return cartRepository.findById(userId).orElseThrow(
                () -> new ApiException(ErrorStatus._NOT_FOUND_CART)
        );
    }

    // Shop ,Cart 객체를 받아 CartResponse 객체를 생성 및 반환하는 메서드
    private CartResponse getCartResponse(Shop shop, Cart cart) {

        List<SimpleMenu> simpleMenuList = cart.getMenu().stream()
                .map(menuIdList -> {
                    Menu savedMenu = getMenuById(menuIdList.getMenuId());
                    return new SimpleMenu(savedMenu.getId(), savedMenu.getName(), savedMenu.getPrice(), menuIdList.getCount());
                })
                .toList();

        return new CartResponse(shop.getId(), shop.getName(), simpleMenuList);
    }

}
