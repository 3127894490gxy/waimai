package com.waimai.controller;

import com.waimai.dto.ApiResponse;
import com.waimai.dto.DishRequest;
import com.waimai.entity.Dish;
import com.waimai.service.DishService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/dishes")
public class DishController {

    private static final Logger log = LoggerFactory.getLogger(DishController.class);

    private final DishService dishService;

    public DishController(DishService dishService) {
        this.dishService = dishService;
    }

    @PostMapping
    public ApiResponse<Dish> create(@Valid @RequestBody DishRequest request) {
        Dish dish = new Dish();
        dish.setName(request.getName());
        dish.setDescription(request.getDescription());
        dish.setImage(request.getImage());
        dish.setPrice(request.getPrice());
        dish.setCategoryId(request.getCategoryId());
        dish.setRestaurantId(request.getRestaurantId());
        dish.setStock(request.getStock() != null ? request.getStock() : 999);
        dish.setActive(request.getActive() != null ? request.getActive() : true);
        Dish saved = dishService.create(dish);
        log.info("菜品创建成功: name={}, restaurantId={}", saved.getName(), request.getRestaurantId());
        return ApiResponse.success(saved);
    }

    @GetMapping("/restaurant/{restaurantId}")
    public ApiResponse<List<Dish>> listByRestaurant(@PathVariable Long restaurantId,
                                                     @RequestParam(required = false) String mode) {
        List<Dish> dishes;
        if ("all".equals(mode)) {
            dishes = dishService.findAll().stream()
                    .filter(d -> d.getRestaurantId().equals(restaurantId))
                    .collect(Collectors.toList());
        } else {
            dishes = dishService.findByRestaurantId(restaurantId);
        }
        return ApiResponse.success(dishes);
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
    public ApiResponse<Dish> update(@PathVariable Long id, @Valid @RequestBody DishRequest request) {
        Dish dish = dishService.findById(id)
                .orElse(null);
        if (dish == null) {
            return ApiResponse.error(404, "菜品不存在");
        }
        dish.setName(request.getName());
        dish.setDescription(request.getDescription());
        dish.setImage(request.getImage());
        dish.setPrice(request.getPrice());
        dish.setCategoryId(request.getCategoryId());
        dish.setRestaurantId(request.getRestaurantId());
        dish.setStock(request.getStock() != null ? request.getStock() : 999);
        dish.setActive(request.getActive() != null ? request.getActive() : true);
        dish.setId(id);
        Dish updated = dishService.update(dish);
        log.info("菜品更新成功: dishId={}", id);
        return ApiResponse.success(updated);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        dishService.deleteById(id);
        log.info("菜品删除: dishId={}", id);
        return ApiResponse.success(null);
    }
}
