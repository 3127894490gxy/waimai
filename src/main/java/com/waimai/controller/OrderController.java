package com.waimai.controller;

import com.waimai.common.OrderStatus;
import com.waimai.dto.ApiResponse;
import com.waimai.entity.Order;
import com.waimai.entity.OrderItem;
import com.waimai.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ApiResponse<Order> create(@RequestBody Map<String, Object> params) {
        // 简化版：前端传 order JSON + items JSON 数组
        // 实际项目应使用 DTO
        throw new UnsupportedOperationException("请使用完整的 DTO 传入");
    }

    @GetMapping("/{id}")
    public ApiResponse<Order> getById(@PathVariable Long id) {
        return orderService.findById(id)
                .map(ApiResponse::success)
                .orElse(ApiResponse.error(404, "订单不存在"));
    }

    @GetMapping("/user/{userId}")
    public ApiResponse<List<Order>> listByUser(@PathVariable Long userId) {
        return ApiResponse.success(orderService.findByUserId(userId));
    }

    @GetMapping("/restaurant/{restaurantId}")
    public ApiResponse<List<Order>> listByRestaurant(@PathVariable Long restaurantId) {
        return ApiResponse.success(orderService.findByRestaurantId(restaurantId));
    }

    @GetMapping("/{id}/items")
    public ApiResponse<List<OrderItem>> getItems(@PathVariable Long id) {
        return ApiResponse.success(orderService.findItemsByOrderId(id));
    }

    @PutMapping("/{id}/status")
    public ApiResponse<Order> updateStatus(@PathVariable Long id, @RequestBody Map<String, String> params) {
        OrderStatus status = OrderStatus.valueOf(params.get("status"));
        return ApiResponse.success(orderService.updateStatus(id, status));
    }
}
