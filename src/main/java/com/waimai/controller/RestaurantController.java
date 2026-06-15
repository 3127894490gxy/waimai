package com.waimai.controller;

import com.waimai.dto.ApiResponse;
import com.waimai.entity.Restaurant;
import com.waimai.service.RestaurantService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @PostMapping
    public ApiResponse<Restaurant> create(@RequestBody Restaurant restaurant) {
        return ApiResponse.success(restaurantService.create(restaurant));
    }

    @GetMapping
    public ApiResponse<List<Restaurant>> list(@RequestParam(required = false) String mode) {
        if ("all".equals(mode)) {
            return ApiResponse.success(restaurantService.findAll());
        }
        return ApiResponse.success(restaurantService.findAllActive());
    }

    @GetMapping("/search")
    public ApiResponse<List<Restaurant>> search(@RequestParam String name) {
        return ApiResponse.success(restaurantService.searchByName(name));
    }

    @GetMapping("/{id}")
    public ApiResponse<Restaurant> getById(@PathVariable Long id) {
        return restaurantService.findById(id)
                .map(ApiResponse::success)
                .orElse(ApiResponse.error(404, "餐厅不存在"));
    }

    @PutMapping("/{id}")
    public ApiResponse<Restaurant> update(@PathVariable Long id, @RequestBody Restaurant restaurant) {
        restaurant.setId(id);
        return ApiResponse.success(restaurantService.update(restaurant));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        restaurantService.deleteById(id);
        return ApiResponse.success(null);
    }
}
