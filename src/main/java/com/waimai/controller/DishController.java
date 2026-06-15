package com.waimai.controller;

import com.waimai.dto.ApiResponse;
import com.waimai.entity.Dish;
import com.waimai.service.DishService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dishes")
public class DishController {

    private final DishService dishService;

    public DishController(DishService dishService) {
        this.dishService = dishService;
    }

    @PostMapping
    public ApiResponse<Dish> create(@RequestBody Dish dish) {
        return ApiResponse.success(dishService.create(dish));
    }

    @GetMapping("/restaurant/{restaurantId}")
    public ApiResponse<List<Dish>> listByRestaurant(@PathVariable Long restaurantId,
                                                     @RequestParam(required = false) String mode) {
        if ("all".equals(mode)) {
            return ApiResponse.success(dishService.findAll().stream()
                    .filter(d -> d.getRestaurantId().equals(restaurantId))
                    .collect(java.util.stream.Collectors.toList()));
        }
        return ApiResponse.success(dishService.findByRestaurantId(restaurantId));
    }

    @GetMapping("/admin/all")
    public ApiResponse<List<Dish>> listAll() {
        return ApiResponse.success(dishService.findAll());
    }

    @GetMapping("/category/{categoryId}")
    public ApiResponse<List<Dish>> listByCategory(@PathVariable Long categoryId) {
        return ApiResponse.success(dishService.findByCategoryId(categoryId));
    }

    @GetMapping("/search")
    public ApiResponse<List<Dish>> search(@RequestParam String name) {
        return ApiResponse.success(dishService.searchByName(name));
    }

    @GetMapping("/{id}")
    public ApiResponse<Dish> getById(@PathVariable Long id) {
        return dishService.findById(id)
                .map(ApiResponse::success)
                .orElse(ApiResponse.error(404, "菜品不存在"));
    }

    @PutMapping("/{id}")
    public ApiResponse<Dish> update(@PathVariable Long id, @RequestBody Dish dish) {
        dish.setId(id);
        return ApiResponse.success(dishService.update(dish));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        dishService.deleteById(id);
        return ApiResponse.success(null);
    }
}
