package com.waimai.service;

import com.waimai.entity.Dish;
import com.waimai.repository.DishRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DishService {

    private final DishRepository dishRepository;

    public DishService(DishRepository dishRepository) {
        this.dishRepository = dishRepository;
    }

    public Dish create(Dish dish) {
        return dishRepository.save(dish);
    }

    public Optional<Dish> findById(Long id) {
        return dishRepository.findById(id);
    }

    public List<Dish> findByRestaurantId(Long restaurantId) {
        return dishRepository.findByRestaurantIdAndActiveTrue(restaurantId);
    }

    public List<Dish> findByCategoryId(Long categoryId) {
        return dishRepository.findByCategoryIdAndActiveTrue(categoryId);
    }

    public List<Dish> searchByName(String name) {
        return dishRepository.findByNameContainingAndActiveTrue(name);
    }

    public Dish update(Dish dish) {
        return dishRepository.save(dish);
    }

    public void deleteById(Long id) {
        dishRepository.deleteById(id);
    }
}
