package com.sparta.fmdelivery.domain.menu.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.fmdelivery.config.AuthUserArgumentResolver;
import com.sparta.fmdelivery.domain.common.dto.AuthUser;
import com.sparta.fmdelivery.domain.menu.dto.MenuRequest;
import com.sparta.fmdelivery.domain.menu.dto.MenuResponse;
import com.sparta.fmdelivery.domain.menu.service.MenuService;
import com.sparta.fmdelivery.domain.user.enums.UserRole;
import com.sparta.fmdelivery.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MenuController.class)
class MenuControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MenuService menuService;

    @MockBean
    private JpaMetamodelMappingContext jpaMetamodelMappingContext;

    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private AuthUserArgumentResolver authUserArgumentResolver;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(new MenuController(menuService)).setControllerAdvice(new GlobalExceptionHandler()) // 예외 처리 핸들러 설정
                .setCustomArgumentResolvers(authUserArgumentResolver) // AuthUserArgumentResolver 설정
                .build();
    }

    @Test
    void createMenu_성공() throws Exception {
        // given
        long shopId = 1L;
        MenuRequest menuRequest = new MenuRequest(1L, "Burger", 10000, 1);
        MenuResponse menuResponse = new MenuResponse(1L, 1L, "Burger", 10000, 1, "amazon.com/burger.jpg");

        MockMultipartFile imageFile = new MockMultipartFile("image", "burger.jpg", "image/jpeg", "image data".getBytes());
        MockMultipartFile request = new MockMultipartFile("request", "", "application/json", objectMapper.writeValueAsString(menuRequest).getBytes());

        // 인증된 사용자 가짜 설정
        given(authUserArgumentResolver.supportsParameter(any())).willReturn(true);
        given(authUserArgumentResolver.resolveArgument(any(), any(), any(), any())).willReturn(new AuthUser(1L, "user@example.com", UserRole.OWNER));
        given(menuService.createMenu(any(AuthUser.class), any(MenuRequest.class), any())).willReturn(menuResponse);

        // when
        ResultActions resultActions = mockMvc.perform(multipart("/api/v1/menus").file(imageFile).file(request).contentType(MediaType.MULTIPART_FORM_DATA_VALUE));

        // then
        resultActions.andExpect(status().isOk());
    }

    @Test
    void getMenusByShopId_성공() throws Exception {
        // given
        long shopId = 1L;
        List<MenuResponse> menuResponses = List.of(new MenuResponse(1L, 1L, "pizza", 20000, 1, "amazon.com/pizza.jpeg"), new MenuResponse(2L, 1L, "burger", 10000, 1, "amazon.com/pizza.jpeg"));

        given(menuService.getMenus(anyLong())).willReturn(menuResponses);

        // when & then
        mockMvc.perform(get("/api/v1/menus").param("shopId", String.valueOf(shopId)).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    void updateMenu_성공() throws Exception {
        // given
        long menuId = 1L;
        MenuRequest menuRequest = new MenuRequest(1L, "pizza", 10000, 0);
        MenuResponse menuResponse = new MenuResponse(menuId, 1L, "burger", 15000, 1, "amazon.com/pizza.jpeg");

        MockMultipartFile imageFile = new MockMultipartFile("image", "updated_burger.jpg", "image/jpeg", "updated image data".getBytes());
        MockMultipartFile request = new MockMultipartFile("request", "", "application/json", objectMapper.writeValueAsString(menuRequest).getBytes());

        // 인증된 사용자 가짜 설정
        given(authUserArgumentResolver.supportsParameter(any())).willReturn(true);
        given(authUserArgumentResolver.resolveArgument(any(), any(), any(), any())).willReturn(new AuthUser(1L, "user@example.com", UserRole.OWNER));
        given(menuService.updateMenu(any(AuthUser.class), anyLong(), any(MenuRequest.class), any())).willReturn(menuResponse);

        // when
        MockMultipartHttpServletRequestBuilder builder = (MockMultipartHttpServletRequestBuilder) multipart("/api/v1/menus/{menuId}", menuId)
                .file(imageFile)
                .file(request)
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE);

        builder.with(request1 -> {
            request1.setMethod("PUT"); // PUT 메서드 설정
            return request1;
        });

        ResultActions resultActions = mockMvc.perform(builder);

        // then
        resultActions.andExpect(status().isOk());
    }

    @Test
    void deleteMenu_성공() throws Exception {
        // given
        long menuId = 1L;
        long shopId = 1L;

        // 인증된 사용자 가짜 설정
        given(authUserArgumentResolver.supportsParameter(any())).willReturn(true);
        given(authUserArgumentResolver.resolveArgument(any(), any(), any(), any())).willReturn(new AuthUser(1L, "user@example.com", UserRole.OWNER));

        // when & then
        mockMvc.perform(delete("/api/v1/menus").param("menuId", String.valueOf(menuId)).param("shopId", String.valueOf(shopId)).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }
}
