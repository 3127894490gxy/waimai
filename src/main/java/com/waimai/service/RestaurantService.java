package com.waimai.service;

import com.waimai.entity.Restaurant;
import com.waimai.repository.RestaurantRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    public RestaurantService(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    public Restaurant create(Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }

    public Optional<Restaurant> findById(Long id) {
        return restaurantRepository.findById(id);
    }

    public List<Restaurant> findAll() {
        return restaurantRepository.findAll();
    }

    public List<Restaurant> findAllActive() {
        return restaurantRepository.findByActiveTrue();
    }

    public List<Restaurant> searchByName(String name) {
        return restaurantRepository.findByNameContaining(name);
    }

    public List<Restaurant> findByMerchantId(Long merchantId) {
        return restaurantRepository.findByMerchantId(merchantId);
    }

    public Restaurant update(Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }

    public void deleteById(Long id) {
        restaurantRepository.deleteById(id);
    }
}
