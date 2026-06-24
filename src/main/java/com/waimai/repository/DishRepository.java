package com.waimai.repository;

import com.waimai.entity.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DishRepository extends JpaRepository<Dish, Long> {
    List<Dish> findByRestaurantIdAndActiveTrue(Long restaurantId);
    List<Dish> findByCategoryIdAndActiveTrue(Long categoryId);
    List<Dish> findByNameContainingAndActiveTrue(String name);
    List<Dish> findByRestaurantId(Long restaurantId);
    List<Dish> findByCategoryId(Long categoryId);
    List<Dish> findByIdIn(List<Long> ids);
    long countByRestaurantIdAndActiveTrue(Long restaurantId);
    long countByCategoryIdAndActiveTrue(Long categoryId);
    List<Dish> findByRestaurantIdAndNameContaining(Long restaurantId, String name);
}
