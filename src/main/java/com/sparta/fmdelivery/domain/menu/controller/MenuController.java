package com.sparta.fmdelivery.domain.menu.controller;

import com.sparta.fmdelivery.apipayload.ApiResponse;
import com.sparta.fmdelivery.domain.common.annotation.Auth;
import com.sparta.fmdelivery.domain.common.dto.AuthUser;
import com.sparta.fmdelivery.domain.menu.dto.MenuRequest;
import com.sparta.fmdelivery.domain.menu.dto.MenuResponse;
import com.sparta.fmdelivery.domain.menu.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class MenuController {

    private final MenuService menuService;

    @PostMapping(value = "/menus", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<MenuResponse> createMenu(
            @Auth AuthUser authUser,
            @RequestPart("request") MenuRequest request,
            @RequestParam("image") MultipartFile image) {

        return ApiResponse.onSuccess(menuService.createMenu(authUser, request, image));
    }

    @GetMapping("/menus")
    public ApiResponse<List<MenuResponse>> getMenusByShopId(@RequestParam(name = "shop_id") Long shopId) {
        return ApiResponse.onSuccess(menuService.getMenus(shopId));
    }

    @PutMapping(value = "/menus/{menuId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<MenuResponse> updateMenu(
            @Auth AuthUser authUser,
            @PathVariable Long menuId,
            @RequestPart("request") MenuRequest request,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        return ApiResponse.onSuccess(menuService.updateMenu(authUser, menuId, request, image));
    }

    @DeleteMapping("/menus")
    public ApiResponse<String> deleteMenu(
            @Auth AuthUser authUser,
            @RequestParam("menu_id") Long menuId,
            @RequestParam("shop_id") Long shopId) {
        menuService.deleteMenu(authUser, menuId, shopId);
        return ApiResponse.onSuccess("메뉴가 삭제되었습니다.");
    }

}
