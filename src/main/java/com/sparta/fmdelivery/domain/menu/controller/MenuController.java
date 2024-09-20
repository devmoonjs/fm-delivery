package com.sparta.fmdelivery.domain.menu.controller;

import com.sparta.fmdelivery.domain.common.annotation.Auth;
import com.sparta.fmdelivery.domain.common.dto.AuthUser;
import com.sparta.fmdelivery.domain.menu.dto.MenuRequest;
import com.sparta.fmdelivery.domain.menu.dto.MenuResponse;
import com.sparta.fmdelivery.domain.menu.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class MenuController {

    private final MenuService menuService;

    @PostMapping("/menus")
    public ResponseEntity<MenuResponse> createMenu(@Auth AuthUser authUser, @RequestBody MenuRequest request) {
        return ResponseEntity.ok(menuService.createMenu(authUser, request));
    }

    @GetMapping("/menus")
    public ResponseEntity<List<MenuResponse>> getMenusByShopId(@RequestParam(name = "shop_id") Long shopId) {
        return ResponseEntity.ok(menuService.getMenus(shopId));
    }

    @PutMapping("/menus/{menuId}")
    public ResponseEntity<MenuResponse> updateMenu(@Auth AuthUser authUser, @PathVariable Long menuId, @RequestBody MenuRequest request) {
        return ResponseEntity.ok(menuService.updateMenu(authUser, menuId, request));
    }

    @DeleteMapping("/menus/{menuId}")
    public ResponseEntity<Void> deleteMenu(@Auth AuthUser authUser, @PathVariable Long menuId) {
        menuService.deleteMenu(authUser, menuId);
        return ResponseEntity.noContent().build(); // 성공 시 204 No Content 반환
    }
}
