package com.waimai.controller;

import com.waimai.dto.ApiResponse;
import com.waimai.dto.CartRequest;
import com.waimai.entity.ShoppingCart;
import com.waimai.service.ShoppingCartService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class ShoppingCartController {

    private static final Logger log = LoggerFactory.getLogger(ShoppingCartController.class);

    private final ShoppingCartService cartService;

    public ShoppingCartController(ShoppingCartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping
    public ApiResponse<ShoppingCart> add(@Valid @RequestBody CartRequest request) {
        ShoppingCart cart = new ShoppingCart();
        cart.setUserId(request.getUserId());
        cart.setDishId(request.getDishId());
        cart.setRestaurantId(request.getRestaurantId());
        cart.setDishName(request.getDishName());
        cart.setDishPrice(request.getDishPrice());
        cart.setQuantity(request.getQuantity());
        ShoppingCart saved = cartService.addOrUpdate(cart);
        log.info("购物车添加成功: userId={}, dishId={}", request.getUserId(), request.getDishId());
        return ApiResponse.success(saved);
    }

    @GetMapping("/user/{userId}")
    public ApiResponse<List<ShoppingCart>> list(@PathVariable Long userId) {
        return ApiResponse.success(cartService.findByUserId(userId));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> remove(@PathVariable Long id) {
        cartService.removeItem(id);
        log.info("购物车移除: cartId={}", id);
        return ApiResponse.success(null);
    }

    @DeleteMapping("/clear/{userId}")
    public ApiResponse<Void> clear(@PathVariable Long userId) {
        cartService.clearByUserId(userId);
        log.info("购物车清空: userId={}", userId);
        return ApiResponse.success(null);
    }
}
