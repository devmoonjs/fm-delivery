package com.sparta.fmdelivery.domain.menu.controller;

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
    public ResponseEntity<MenuResponse> createMenu(@RequestBody MenuRequest request) {
        return ResponseEntity.ok(menuService.createMenu(request));
    }

    @GetMapping("/menus")
    public ResponseEntity<List<MenuResponse>> getMenusByShopId(@RequestParam Long shopId) {
        return ResponseEntity.ok(menuService.getMenus(shopId));
    }

    @PostMapping("/menus/{menuId}")
    public ResponseEntity<MenuResponse> updateMenu(@PathVariable Long menuId, @RequestBody MenuRequest request) {
        return ResponseEntity.ok(menuService.updateMenu(menuId, request));
    }

    @DeleteMapping("/menus/{menuId}")
    public ResponseEntity<Void> deleteMenu(@PathVariable Long menuId) {
        menuService.deleteMenu(menuId);
        return ResponseEntity.noContent().build(); // 성공 시 204 No Content 반환
    }
}
