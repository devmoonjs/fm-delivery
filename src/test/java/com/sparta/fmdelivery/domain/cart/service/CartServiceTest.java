package com.sparta.fmdelivery.domain.cart.service;

import com.sparta.fmdelivery.common.dto.AuthUser;
import com.sparta.fmdelivery.domain.cart.dto.CartRequest;
import com.sparta.fmdelivery.domain.cart.dto.CartResponse;
import com.sparta.fmdelivery.domain.cart.entity.Cart;
import com.sparta.fmdelivery.domain.cart.repository.CartRepository;
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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
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

    private static final LocalTime TEST_CLOSED_AT = LocalTime.of(18,0,0);
    private static final LocalTime TEST_OPEED_AT = LocalTime.of(9,0,0);
    private static final ShopCreateRequest TEST_SHOP_CREATE_REQUEST = new ShopCreateRequest("가게이름", TEST_OPEED_AT, TEST_CLOSED_AT, 10000);
    private static final CartRequest TEST_CART_REQUEST = new CartRequest(1L, 1L, 2);
    private static final MenuRequest TEST_MENU_REQUEST = new MenuRequest(1L, "메뉴이름", 5000, 1);
    private static final MenuIdList TEST_MENU_ID_LIST = new MenuIdList(1L, 2);
    private static final AuthUser TEST_AUTH_USER = new AuthUser(1L, "test@example.com", UserRole.OWNER);
    private static final User TEST_USER = new User("test@example.com", "Password1!", UserRole.OWNER);
    private static final Shop TEST_SHOP = new Shop(TEST_USER, TEST_SHOP_CREATE_REQUEST);
    private static final Menu TEST_MENU = new Menu(TEST_MENU_REQUEST, TEST_SHOP, "imgURL");
    private static final Cart TEST_CART =new Cart(TEST_AUTH_USER.getId(), TEST_SHOP.getId(), new ArrayList<>(Arrays.asList(TEST_MENU_ID_LIST)));

    @Test
    void 장바구니_생성_저장_성공() {

        // given
        ReflectionTestUtils.setField(TEST_USER, "id", 1L);
        ReflectionTestUtils.setField(TEST_SHOP, "id", 1L);
        ReflectionTestUtils.setField(TEST_MENU, "id", 1L);

        given(userRepository.findById(anyLong())).willReturn(Optional.of(TEST_USER));
        given(shopRepository.findById(anyLong())).willReturn(Optional.of(TEST_SHOP));
        given(menuRepository.findById(anyLong())).willReturn(Optional.of(TEST_MENU));
        given(cartRepository.findById(anyLong())).willReturn(Optional.empty());

        // when
        CartResponse response = cartService.saveCart(TEST_AUTH_USER, TEST_CART_REQUEST);

        // then
        assertNotNull(response);
        assertEquals(TEST_SHOP.getId(), response.getShopId());
        assertEquals(TEST_SHOP.getName(), response.getShopName());
        verify(cartRepository, times(1)).save(any(Cart.class));
    }

    @Test
    void 장바구니_추가_저장_성공() {
        // given
        ReflectionTestUtils.setField(TEST_USER, "id", 1L);
        ReflectionTestUtils.setField(TEST_SHOP, "id", 1L);
        ReflectionTestUtils.setField(TEST_MENU, "id", 1L);

        given(userRepository.findById(anyLong())).willReturn(Optional.of(TEST_USER));
        given(shopRepository.findById(anyLong())).willReturn(Optional.of(TEST_SHOP));
        given(menuRepository.findById(anyLong())).willReturn(Optional.of(TEST_MENU));
        given(cartRepository.findById(anyLong())).willReturn(Optional.of(TEST_CART));

        // when
        CartResponse response = cartService.saveCart(TEST_AUTH_USER, TEST_CART_REQUEST);

        assertNotNull(response);
        assertEquals(4, TEST_CART.getMenu().get(0).getCount());
        verify(cartRepository, times(1)).save(any(Cart.class));
    }


}