package com.waimai.controller;

import com.waimai.dto.ApiResponse;
import com.waimai.entity.ShoppingCart;
import com.waimai.service.ShoppingCartService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class ShoppingCartController {

    private final ShoppingCartService cartService;

    public ShoppingCartController(ShoppingCartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping
    public ApiResponse<ShoppingCart> add(@RequestBody ShoppingCart cart) {
        return ApiResponse.success(cartService.addOrUpdate(cart));
    }

    @GetMapping("/user/{userId}")
    public ApiResponse<List<ShoppingCart>> list(@PathVariable Long userId) {
        return ApiResponse.success(cartService.findByUserId(userId));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> remove(@PathVariable Long id) {
        cartService.removeItem(id);
        return ApiResponse.success(null);
    }

    @DeleteMapping("/clear/{userId}")
    public ApiResponse<Void> clear(@PathVariable Long userId) {
        cartService.clearByUserId(userId);
        return ApiResponse.success(null);
    }
}
