package com.waimai.repository;

import com.waimai.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    List<Restaurant> findByActiveTrue();
    List<Restaurant> findByNameContaining(String name);
    List<Restaurant> findByMerchantId(Long merchantId);
    List<Restaurant> findByNameContainingAndActiveTrue(String name);
    List<Restaurant> findByMerchantIdAndActiveTrue(Long merchantId);
    long countByMerchantId(Long merchantId);
}
