package com.sparta.fmdelivery.domain.cart.service;

import com.sparta.fmdelivery.domain.cart.dto.CartRequest;
import com.sparta.fmdelivery.domain.cart.dto.CartResponse;
import com.sparta.fmdelivery.domain.cart.entity.Cart;
import com.sparta.fmdelivery.domain.cart.repository.CartRepository;
import com.sparta.fmdelivery.domain.common.dto.AuthUser;
import com.sparta.fmdelivery.domain.menu.dto.MenuRequest;
import com.sparta.fmdelivery.domain.menu.entity.Menu;
import com.sparta.fmdelivery.domain.menu.repository.MenuRepository;
import com.sparta.fmdelivery.domain.order.dto.MenuIdList;
import com.sparta.fmdelivery.domain.shop.dto.request.ShopCreateRequest;
import com.sparta.fmdelivery.domain.shop.entitiy.Shop;
import com.sparta.fmdelivery.domain.shop.repository.ShopRepository;
import com.sparta.fmdelivery.domain.user.entity.User;
import com.sparta.fmdelivery.domain.user.enums.UserRole;
import com.sparta.fmdelivery.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class CartServiceTest {
    @MockBean
    private JpaMetamodelMappingContext jpaMetamodelMappingContext;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ShopRepository shopRepository;

    @Mock
    private MenuRepository menuRepository;

    @InjectMocks
    private CartService cartService;

    private AuthUser authUser;
    private ShopCreateRequest shopCreateRequest;
    private CartRequest cartRequest;
    private MenuRequest menuRequest;
    private MenuIdList menuIdList;
    private User user;
    private Shop shop;
    private Cart cart;
    private Menu menu;

    LocalTime closedAt;
    LocalTime opeedAt;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        authUser = new AuthUser(1L, "test@example.com", UserRole.OWNER);
        user = new User("test@example.com", "Password1!", UserRole.OWNER);
        ReflectionTestUtils.setField(user, "id", 1L);

        opeedAt = LocalTime.of(9,0,0);
        closedAt = LocalTime.of(18,0,0);
        shopCreateRequest = new ShopCreateRequest("가게이름", opeedAt, closedAt, 10000);
        shop = new Shop(user, shopCreateRequest);
        ReflectionTestUtils.setField(shop, "id", 1L);

        menuRequest = new MenuRequest(1L, "메뉴이름", 5000, 1);
        menu = new Menu(menuRequest, shop, "imgURL");
        ReflectionTestUtils.setField(menu, "id", 1L);

        menuIdList = new MenuIdList(1L, 2);
        cart = new Cart(authUser.getId(), shop.getId(), new ArrayList<>(Arrays.asList(menuIdList)));
        cartRequest = new CartRequest(1L, 1L, 2);
    }




}