package com.waimai.service;

import com.waimai.common.OrderStatus;
import com.waimai.entity.Order;
import com.waimai.entity.OrderItem;
import com.waimai.repository.OrderItemRepository;
import com.waimai.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    @Transactional
    public Order create(Order order, List<OrderItem> items) {
        order.setOrderNo(generateOrderNo());
        order.setStatus(OrderStatus.PENDING_PAYMENT);
        Order savedOrder = orderRepository.save(order);

        for (OrderItem item : items) {
            item.setOrderId(savedOrder.getId());
            item.setSubtotal(item.getPrice().multiply(java.math.BigDecimal.valueOf(item.getQuantity())));
            orderItemRepository.save(item);
        }

        return savedOrder;
    }

    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id);
    }

    public Optional<Order> findByOrderNo(String orderNo) {
        return orderRepository.findAll().stream()
                .filter(o -> o.getOrderNo().equals(orderNo))
                .findFirst();
    }

    public List<Order> findByUserId(Long userId) {
        return orderRepository.findByUserIdOrderByCreateTimeDesc(userId);
    }

    public List<Order> findByRestaurantId(Long restaurantId) {
        return orderRepository.findByRestaurantIdOrderByCreateTimeDesc(restaurantId);
    }

    public List<OrderItem> findItemsByOrderId(Long orderId) {
        return orderItemRepository.findByOrderId(orderId);
    }

    @Transactional
    public Order updateStatus(Long orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("订单不存在"));
        order.setStatus(status);
        return orderRepository.save(order);
    }

    /** 分配配送员 */
    @Transactional
    public Order assignDelivery(Long orderId, Long deliveryId, String deliveryName) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("订单不存在"));
        order.setDeliveryId(deliveryId);
        order.setDeliveryName(deliveryName);
        // 分配配送员时，将状态更新为"配送中"
        if (order.getStatus() == OrderStatus.PAID || order.getStatus() == OrderStatus.ACCEPTED) {
            order.setStatus(OrderStatus.DELIVERING);
        }
        return orderRepository.save(order);
    }

    /** 按配送员查询订单 */
    public List<Order> findByDeliveryId(Long deliveryId) {
        return orderRepository.findByDeliveryIdOrderByCreateTimeDesc(deliveryId);
    }

    /** 按配送员和状态查询订单 */
    public List<Order> findByDeliveryIdAndStatus(Long deliveryId, OrderStatus status) {
        return orderRepository.findByDeliveryIdAndStatusOrderByCreateTimeDesc(deliveryId, status);
    }

    /** 查询待配送的订单（已支付待接单+已接单） */
    public List<Order> findPendingDelivery() {
        return orderRepository.findByStatusOrderByCreateTimeDesc(OrderStatus.PAID);
    }

    private String generateOrderNo() {
        LocalDateTime now = LocalDateTime.now();
        String datePart = now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        int randomPart = new Random().nextInt(1000);
        return "WM" + datePart + String.format("%03d", randomPart);
    }
}
