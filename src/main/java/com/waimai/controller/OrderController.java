package com.waimai.controller;

import com.waimai.common.OrderStatus;
import com.waimai.dto.ApiResponse;
import com.waimai.dto.OrderRequest;
import com.waimai.dto.PaymentRequest;
import com.waimai.entity.Order;
import com.waimai.entity.OrderItem;
import com.waimai.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ApiResponse<Order> create(@RequestBody OrderRequest request) {
        // 构造 Order 实体
        Order order = new Order();
        order.setUserId(request.getUserId());
        order.setRestaurantId(request.getRestaurantId());
        order.setDeliveryFee(request.getDeliveryFee() != null ? request.getDeliveryFee() : BigDecimal.ZERO);
        order.setDeliveryAddress(request.getDeliveryAddress());
        order.setContactPhone(request.getContactPhone());
        order.setContactName(request.getContactName());
        order.setRemark(request.getRemark());

        // 计算总金额
        BigDecimal total = request.getItems().stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotalAmount(total.add(order.getDeliveryFee()));

        // 转换 OrderItem
        List<OrderItem> items = request.getItems().stream().map(item -> {
            OrderItem oi = new OrderItem();
            oi.setDishId(item.getDishId());
            oi.setDishName(item.getDishName());
            oi.setQuantity(item.getQuantity());
            oi.setPrice(item.getPrice());
            oi.setSubtotal(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
            return oi;
        }).collect(Collectors.toList());

        return ApiResponse.success(orderService.create(order, items));
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

    /** 模拟支付 */
    @PostMapping("/pay")
    public ApiResponse<Order> pay(@RequestBody PaymentRequest request) {
        Order order = orderService.updateStatus(request.getOrderId(), OrderStatus.PAID);
        return ApiResponse.success(order);
    }

    // ===== 配送相关接口 =====

    /** 分配配送员 */
    @PutMapping("/{id}/assign-delivery")
    public ApiResponse<Order> assignDelivery(@PathVariable Long id, @RequestBody Map<String, Object> params) {
        Long deliveryId = Long.valueOf(params.get("deliveryId").toString());
        String deliveryName = (String) params.get("deliveryName");
        return ApiResponse.success(orderService.assignDelivery(id, deliveryId, deliveryName));
    }

    /** 查询待配送订单 */
    @GetMapping("/delivery/pending")
    public ApiResponse<List<Order>> listPendingDelivery() {
        return ApiResponse.success(orderService.findPendingDelivery());
    }

    /** 按配送员查询订单 */
    @GetMapping("/delivery/{deliveryId}")
    public ApiResponse<List<Order>> listByDelivery(@PathVariable Long deliveryId) {
        return ApiResponse.success(orderService.findByDeliveryId(deliveryId));
    }
}
