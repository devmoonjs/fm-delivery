package com.sparta.fmdelivery.domain.menu.service;

import com.sparta.fmdelivery.apipayload.dto.ReasonDto;
import com.sparta.fmdelivery.apipayload.status.ErrorStatus;
import com.sparta.fmdelivery.config.S3ClientUtility;
import com.sparta.fmdelivery.common.dto.AuthUser;
import com.sparta.fmdelivery.domain.menu.dto.MenuRequest;
import com.sparta.fmdelivery.domain.menu.dto.MenuResponse;
import com.sparta.fmdelivery.domain.menu.entity.Menu;
import com.sparta.fmdelivery.domain.menu.repository.MenuRepository;
import com.sparta.fmdelivery.domain.shop.entitiy.Shop;
import com.sparta.fmdelivery.domain.shop.service.ShopService;
import com.sparta.fmdelivery.domain.user.entity.User;
import com.sparta.fmdelivery.exception.ApiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

class MenuServiceTest {

    @InjectMocks
    private MenuService menuService;

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private ShopService shopService;

    @Mock
    private S3ClientUtility s3ClientUtility;

    @Mock
    private MultipartFile multipartFile;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createMenu_성공() {
        // given
        AuthUser authUser = new AuthUser(1L, "user@example.com", null);
        MenuRequest menuRequest = new MenuRequest(1L, "Burger", 10000, 1);

        Shop shop = mock(Shop.class);
        User user = mock(User.class);

        given(shop.getUser()).willReturn(user);
        given(user.getId()).willReturn(1L);

        Menu menu = new Menu(menuRequest, shop, "imageUrl");
        given(shopService.getShopById(anyLong())).willReturn(shop);
        given(s3ClientUtility.uploadFile(anyString(), any(MultipartFile.class))).willReturn("imageUrl");
        given(menuRepository.save(any(Menu.class))).willReturn(menu);

        // when
        MenuResponse result = menuService.createMenu(authUser, menuRequest, multipartFile);

        // then
        assertNotNull(result);
        assertEquals("Burger", result.getName());
        assertEquals(10000, result.getPrice());
        verify(menuRepository, times(1)).save(any(Menu.class));
    }

    @Test
    void getMenus_성공() {
        // given
        Long shopId = 1L;

        Menu menu = mock(Menu.class);
        Shop shop = mock(Shop.class);

        given(menu.getShop()).willReturn(shop);
        given(shop.getId()).willReturn(shopId);
        given(menuRepository.findAllByShopId(shopId)).willReturn(List.of(menu));

        // when
        var menuResponses = menuService.getMenus(shopId);

        // then
        assertEquals(1, menuResponses.size());
        verify(menuRepository, times(1)).findAllByShopId(shopId);
    }

    @Test
    void updateMenu_성공() {
        // given
        AuthUser authUser = new AuthUser(1L, "user@example.com", null);
        Long menuId = 1L;
        MenuRequest menuRequest = new MenuRequest(1L, "Pizza", 15000, 0);

        Shop shop = mock(Shop.class);
        User user = mock(User.class);
        Menu menu = mock(Menu.class);

        given(menu.getShop()).willReturn(shop);
        given(shop.getUser()).willReturn(user);
        given(user.getId()).willReturn(authUser.getId());
        given(shop.getId()).willReturn(menuRequest.getShopId());

        given(shopService.getShopById(anyLong())).willReturn(shop);
        given(menuRepository.findById(menuId)).willReturn(Optional.of(menu));
        given(s3ClientUtility.uploadFile(anyString(), any(MultipartFile.class))).willReturn("updatedImageUrl");

        // when
        MenuResponse result = menuService.updateMenu(authUser, menuId, menuRequest, multipartFile);

        // then
        assertNotNull(result);
        verify(menu).updateMenu("Pizza", 15000, 0, "updatedImageUrl");
        verify(menuRepository, never()).save(any()); // save 메서드가 호출되지 않았음을 검증
    }


    @Test
    void deleteMenu_성공() {
        // given
        AuthUser authUser = new AuthUser(1L, "user@example.com", null);
        Long menuId = 1L;
        Long shopId = 1L;

        Shop shop = mock(Shop.class);
        User user = mock(User.class);
        Menu menu = mock(Menu.class);

        given(shop.getUser()).willReturn(user);
        given(user.getId()).willReturn(authUser.getId());
        given(shopService.getShopById(anyLong())).willReturn(shop);
        given(menuRepository.findById(menuId)).willReturn(Optional.of(menu));

        // when
        menuService.deleteMenu(authUser, menuId, shopId);

        // then
        verify(menuRepository, times(1)).delete(menu);
    }

    @Test
    void getValidatedMenu_메뉴없음_예외발생() {
        // given
        Long menuId = 1L;

        // Menu가 존재하지 않음을 시뮬레이션
        given(menuRepository.findById(menuId)).willReturn(Optional.empty());

        // when & then
        ApiException exception = assertThrows(ApiException.class, () -> {
            menuService.getValidatedMenu(menuId);
        });

        // then: 예외 발생 후, ErrorStatus의 ReasonDto를 가져와 검증
        ReasonDto reasonDto = ErrorStatus._NOT_FOUND_MENU.getReasonHttpStatus();

        assertEquals("404", reasonDto.getStatusCode()); // 상태 코드 검증
        assertEquals("존재하지 않는 메뉴입니다.", reasonDto.getMessage()); // 메시지 검증
        assertEquals(HttpStatus.NOT_FOUND, reasonDto.getHttpStatus()); // HTTP 상태 검증
    }

}
