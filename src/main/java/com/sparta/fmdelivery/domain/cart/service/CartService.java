package com.sparta.fmdelivery.domain.cart.service;

import com.sparta.fmdelivery.apipayload.status.ErrorStatus;
import com.sparta.fmdelivery.domain.cart.dto.CartResponse;
import com.sparta.fmdelivery.domain.cart.dto.CartRequest;
import com.sparta.fmdelivery.domain.cart.entity.Cart;
import com.sparta.fmdelivery.domain.cart.repository.CartRepository;
import com.sparta.fmdelivery.domain.common.dto.AuthUser;
import com.sparta.fmdelivery.domain.menu.repository.MenuRepository;
import com.sparta.fmdelivery.domain.menu.entity.Menu;
import com.sparta.fmdelivery.domain.order.dto.MenuIdList;
import com.sparta.fmdelivery.domain.order.dto.SimpleMenu;
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


    public CartResponse saveCart(AuthUser authUser, CartRequest request) {
        User user = getUserById(authUser);
        Shop shop = getShopById(request.getShopId());
        Cart cart = cartRepository.findById(authUser.getId()).orElse(null);

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

        cartRepository.save(cart);
        return getCartResponse(shop, cart);
    }


    public CartResponse getCart(AuthUser authUser) {
        User user = getUserById(authUser);
        Cart cart = getCartById(user.getId());
        Shop shop = getShopById(cart.getShopId());
        return getCartResponse(shop, cart);
    }


    public void deleteCart(AuthUser authUser) {
        User user = getUserById(authUser);
        Cart cart = getCartById(user.getId());
        cartRepository.delete(cart);
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
