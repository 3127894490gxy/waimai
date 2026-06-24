package com.waimai.repository;

import com.waimai.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByRestaurantIdOrderBySortOrder(Long restaurantId);
    boolean existsByNameAndRestaurantId(String name, Long restaurantId);
    List<Category> findByNameContaining(String name);
    long countByRestaurantId(Long restaurantId);
    void deleteByRestaurantId(Long restaurantId);
}
