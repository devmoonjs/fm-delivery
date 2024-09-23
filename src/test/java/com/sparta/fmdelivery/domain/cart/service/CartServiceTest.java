package com.sparta.fmdelivery.domain.cart.service;

import com.sparta.fmdelivery.domain.cart.dto.CartRequest;
import com.sparta.fmdelivery.domain.cart.dto.CartResponse;
import com.sparta.fmdelivery.domain.cart.entity.Cart;
import com.sparta.fmdelivery.domain.cart.repository.CartRepository;
import com.sparta.fmdelivery.domain.common.dto.AuthUser;
import com.sparta.fmdelivery.domain.menu.repository.MenuRepository;
import com.sparta.fmdelivery.domain.order.pojo.MenuIdList;
import com.sparta.fmdelivery.domain.shop.dto.request.ShopCreateRequest;
import com.sparta.fmdelivery.domain.shop.entitiy.Shop;
import com.sparta.fmdelivery.domain.shop.repository.ShopRepository;
import com.sparta.fmdelivery.domain.user.entity.User;
import com.sparta.fmdelivery.domain.user.enums.UserRole;
import com.sparta.fmdelivery.domain.user.repository.UserRepository;
import com.sparta.fmdelivery.domain.menu.entity.Menu;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class CartServiceTest {

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
    private User user;
    private Shop shop;
    private Menu menu;
    private Cart cart;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Set up common objects used in tests
        authUser = new AuthUser(1L, "test@example.com", UserRole.USER);
        user = new User("test@example.com", "password", UserRole.USER);
        ReflectionTestUtils.setField(user, "id", 1L);
        shop = new Shop(user, new ShopCreateRequest("Test Shop", LocalTime.of(9, 0), LocalTime.of(21, 0), 1000));
        ReflectionTestUtils.setField(shop, "id", 1L);
        menu = new Menu(shop, "Test Menu", 1000, 1);
        ReflectionTestUtils.setField(menu, "id", 1L);
        cart = new Cart(user.getId(), shop.getId(), Collections.singletonList(new MenuIdList(1L, 1)));
    }

    @Test
    void 장바구니_없을경우_생성_및_저장_성공() {
        CartRequest request = new CartRequest(1L, 1L, 1);

        when(userRepository.findById(authUser.getId())).thenReturn(Optional.of(user));
        when(shopRepository.findById(request.getShopId())).thenReturn(Optional.of(shop));
        when(menuRepository.findById(request.getMenuId())).thenReturn(Optional.of(menu));
        when(cartRepository.findById(user.getId())).thenReturn(Optional.empty());

        CartResponse response = cartService.saveCart(authUser, request);

        verify(cartRepository, times(1)).save(any(Cart.class));
        assertEquals(shop.getId(), response.getShopId());
        assertEquals("Test Menu", response.getMenu().get(0).getName());
    }

}