package com.waimai.repository;

import com.waimai.entity.Order;
import com.waimai.common.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByOrderNo(String orderNo);
    List<Order> findByUserIdOrderByCreateTimeDesc(Long userId);
    List<Order> findByRestaurantIdOrderByCreateTimeDesc(Long restaurantId);
    List<Order> findByUserIdAndStatusOrderByCreateTimeDesc(Long userId, OrderStatus status);
    List<Order> findByRestaurantIdAndStatusOrderByCreateTimeDesc(Long restaurantId, OrderStatus status);
    List<Order> findByDeliveryIdOrderByCreateTimeDesc(Long deliveryId);
    List<Order> findByStatusOrderByCreateTimeDesc(OrderStatus status);
    List<Order> findByDeliveryIdAndStatusOrderByCreateTimeDesc(Long deliveryId, OrderStatus status);
    long countByDeliveryIdAndStatus(Long deliveryId, OrderStatus status);
    long countByDeliveryId(Long deliveryId);
    long countByUserIdAndStatus(Long userId, OrderStatus status);
    long countByRestaurantIdAndStatus(Long restaurantId, OrderStatus status);
    long countByStatus(OrderStatus status);
}
