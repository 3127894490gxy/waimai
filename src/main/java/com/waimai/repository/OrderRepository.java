package com.waimai.repository;

import com.waimai.entity.Order;
import com.waimai.common.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserIdOrderByCreateTimeDesc(Long userId);
    List<Order> findByRestaurantIdOrderByCreateTimeDesc(Long restaurantId);
    List<Order> findByUserIdAndStatusOrderByCreateTimeDesc(Long userId, OrderStatus status);
    List<Order> findByRestaurantIdAndStatusOrderByCreateTimeDesc(Long restaurantId, OrderStatus status);
}
