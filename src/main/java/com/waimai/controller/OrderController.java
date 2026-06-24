package com.waimai.controller;

import com.waimai.common.OrderStatus;
import com.waimai.dto.ApiResponse;
import com.waimai.dto.OrderRequest;
import com.waimai.dto.OrderStatusRequest;
import com.waimai.dto.PaymentRequest;
import com.waimai.entity.Order;
import com.waimai.entity.OrderItem;
import com.waimai.service.OrderService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private static final Logger log = LoggerFactory.getLogger(OrderController.class);

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ApiResponse<Order> create(@Valid @RequestBody OrderRequest request) {
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

        Order saved = orderService.create(order, items);
        log.info("订单创建成功: orderNo={}, userId={}", saved.getOrderNo(), request.getUserId());
        return ApiResponse.success(saved);
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
    public ApiResponse<Order> updateStatus(@PathVariable Long id,
                                           @Valid @RequestBody OrderStatusRequest request) {
        try {
            OrderStatus status = OrderStatus.valueOf(request.getStatus().toUpperCase());
            Order updated = orderService.updateStatus(id, status);
            log.info("订单状态更新: orderId={}, status={}", id, status);
            return ApiResponse.success(updated);
        } catch (IllegalArgumentException e) {
            return ApiResponse.error(400, "无效的订单状态: " + request.getStatus());
        }
    }

    /** 模拟支付 */
    @PostMapping("/pay")
    public ApiResponse<Order> pay(@Valid @RequestBody PaymentRequest request) {
        Order order = orderService.updateStatus(request.getOrderId(), OrderStatus.PAID);
        log.info("订单支付成功: orderId={}", request.getOrderId());
        return ApiResponse.success(order);
    }

    // ===== 配送相关接口 =====

    /** 分配配送员 */
    @PutMapping("/{id}/assign-delivery")
    public ApiResponse<Order> assignDelivery(@PathVariable Long id,
                                             @Valid @RequestBody Map<String, Object> params) {
        try {
            Long deliveryId = Long.valueOf(params.get("deliveryId").toString());
            String deliveryName = (String) params.get("deliveryName");
            if (deliveryName == null || deliveryName.isBlank()) {
                return ApiResponse.error(400, "配送员姓名不能为空");
            }
            Order updated = orderService.assignDelivery(id, deliveryId, deliveryName);
            log.info("配送员分配成功: orderId={}, deliveryId={}", id, deliveryId);
            return ApiResponse.success(updated);
        } catch (NumberFormatException e) {
            return ApiResponse.error(400, "配送员ID格式错误");
        }
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

    /** 配送员统计信息 */
    @GetMapping("/delivery/{deliveryId}/stats")
    public ApiResponse<Map<String, Object>> deliveryStats(@PathVariable Long deliveryId) {
        Map<String, Object> stats = orderService.getDeliveryStats(deliveryId);
        return ApiResponse.success(stats);
    }

    /** 按状态查询订单 */
    @GetMapping("/status/{status}")
    public ApiResponse<List<Order>> listByStatus(@PathVariable String status) {
        try {
            OrderStatus orderStatus = OrderStatus.valueOf(status.toUpperCase());
            return ApiResponse.success(orderService.findByStatus(orderStatus));
        } catch (IllegalArgumentException e) {
            return ApiResponse.error(400, "无效的订单状态: " + status);
        }
    }

    /** 用户按状态查询订单 */
    @GetMapping("/user/{userId}/status/{status}")
    public ApiResponse<List<Order>> listByUserAndStatus(@PathVariable Long userId,
                                                         @PathVariable String status) {
        try {
            OrderStatus orderStatus = OrderStatus.valueOf(status.toUpperCase());
            return ApiResponse.success(orderService.findByUserIdAndStatus(userId, orderStatus));
        } catch (IllegalArgumentException e) {
            return ApiResponse.error(400, "无效的订单状态: " + status);
        }
    }

    /** 商家按状态查询订单 */
    @GetMapping("/restaurant/{restaurantId}/status/{status}")
    public ApiResponse<List<Order>> listByRestaurantAndStatus(@PathVariable Long restaurantId,
                                                               @PathVariable String status) {
        try {
            OrderStatus orderStatus = OrderStatus.valueOf(status.toUpperCase());
            return ApiResponse.success(orderService.findByRestaurantIdAndStatus(restaurantId, orderStatus));
        } catch (IllegalArgumentException e) {
            return ApiResponse.error(400, "无效的订单状态: " + status);
        }
    }
}
