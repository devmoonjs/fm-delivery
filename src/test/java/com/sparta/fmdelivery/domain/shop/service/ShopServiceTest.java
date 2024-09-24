package com.sparta.fmdelivery.domain.shop.service;

import com.sparta.fmdelivery.common.dto.AuthUser;
import com.sparta.fmdelivery.domain.shop.dto.request.ShopCreateRequest;
import com.sparta.fmdelivery.domain.shop.dto.request.ShopDeleteRequest;
import com.sparta.fmdelivery.domain.shop.dto.response.ShopResponse;
import com.sparta.fmdelivery.domain.shop.entitiy.Shop;
import com.sparta.fmdelivery.domain.shop.repository.ShopRepository;
import com.sparta.fmdelivery.domain.user.entity.User;
import com.sparta.fmdelivery.domain.user.enums.UserRole;
import com.sparta.fmdelivery.domain.user.repository.UserRepository;
import com.sparta.fmdelivery.exception.ApiException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ShopServiceTest {

    @Mock
    private ShopRepository shopRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ShopService shopService;

    @Test
    void 가게_정상적으로_생성() {

        // given
        User user = new User("email", "qwer", UserRole.OWNER);
        ReflectionTestUtils.setField(user, "id", 1L);
        AuthUser authUser = new AuthUser(1L, "email", UserRole.OWNER);
        ShopCreateRequest request = new ShopCreateRequest("test", LocalTime.of(8,0), LocalTime.of(22,0), 10000);

        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));

        Shop shop = new Shop(user, request);
        ShopResponse shopResponse = ShopResponse.of(shop);
        given(shopRepository.save(any())).willReturn(shop);

        // when
        ShopResponse response = shopService.createShop(authUser, request);

        // then
        assertNotNull(response);
    }

    @Test
    void USER권한으로_가게_생성시_오류() {

        // given

        AuthUser authUser = new AuthUser(1L, "email", UserRole.USER);
        ShopCreateRequest request = new ShopCreateRequest("test", LocalTime.of(8, 0), LocalTime.of(22, 0), 10000);

        // when
        ApiException exception = assertThrows(ApiException.class, () -> shopService.createShop(authUser, request));

        // then
        assertEquals("사장님 계정만 가게 생성이 가능합니다.", exception.getErrorCode().getReasonHttpStatus().getMessage());
    }

    @Test
    void 가게id로_가게_조회_성공() {

        // given
        User user = new User("email", "qwer", UserRole.OWNER);
        ReflectionTestUtils.setField(user, "id", 1L);

        ShopCreateRequest request = new ShopCreateRequest("test", LocalTime.of(8, 0), LocalTime.of(22, 0), 10000);

        Shop shop = new Shop(user, request);
        given(shopRepository.findById(anyLong())).willReturn(Optional.of(shop));

        // when
        ShopResponse response = shopService.getShop(anyLong());

        // then
        assertNotNull(response);
    }

    @Test
    void 가게_삭제() {

        // given
        User user = new User("email", "qwer", UserRole.OWNER);
        ReflectionTestUtils.setField(user, "id", 1L);

        AuthUser authUser = new AuthUser(1L, "qwer", UserRole.OWNER);

        ShopCreateRequest request = new ShopCreateRequest("test", LocalTime.of(8, 0), LocalTime.of(22, 0), 10000);

        Shop shop = new Shop(user, request);
        ShopDeleteRequest deleteRequest = new ShopDeleteRequest();
        ReflectionTestUtils.setField(deleteRequest, "shopId", 1L);
        given(shopRepository.findById(anyLong())).willReturn(Optional.of(shop));

        // when
        shopService.deleteShop(authUser, deleteRequest);

        // then
        assertTrue(shop.isDeleted());
    }
}