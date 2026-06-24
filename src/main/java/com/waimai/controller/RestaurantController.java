package com.waimai.controller;

import com.waimai.dto.ApiResponse;
import com.waimai.dto.RestaurantRequest;
import com.waimai.entity.Restaurant;
import com.waimai.service.RestaurantService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

    private static final Logger log = LoggerFactory.getLogger(RestaurantController.class);

    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @PostMapping
    public ApiResponse<Restaurant> create(@Valid @RequestBody RestaurantRequest request) {
        Restaurant restaurant = new Restaurant();
        restaurant.setName(request.getName());
        restaurant.setDescription(request.getDescription());
        restaurant.setAddress(request.getAddress());
        restaurant.setPhone(request.getPhone());
        restaurant.setImage(request.getImage());
        restaurant.setDeliveryFee(request.getDeliveryFee() != null ? request.getDeliveryFee() : BigDecimal.ZERO);
        restaurant.setMerchantId(request.getMerchantId());
        Restaurant saved = restaurantService.create(restaurant);
        log.info("餐厅创建成功: name={}", saved.getName());
        return ApiResponse.success(saved);
    }

    @GetMapping
    public ApiResponse<List<Restaurant>> list(@RequestParam(required = false) String mode) {
        List<Restaurant> restaurants;
        if ("all".equals(mode)) {
            restaurants = restaurantService.findAll();
        } else {
            restaurants = restaurantService.findAllActive();
        }
        return ApiResponse.success(restaurants);
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
    public ApiResponse<Restaurant> update(@PathVariable Long id, @Valid @RequestBody RestaurantRequest request) {
        Restaurant restaurant = restaurantService.findById(id)
                .orElse(null);
        if (restaurant == null) {
            return ApiResponse.error(404, "餐厅不存在");
        }
        restaurant.setName(request.getName());
        restaurant.setDescription(request.getDescription());
        restaurant.setAddress(request.getAddress());
        restaurant.setPhone(request.getPhone());
        restaurant.setImage(request.getImage());
        restaurant.setDeliveryFee(request.getDeliveryFee() != null ? request.getDeliveryFee() : BigDecimal.ZERO);
        restaurant.setMerchantId(request.getMerchantId());
        restaurant.setId(id);
        Restaurant updated = restaurantService.update(restaurant);
        log.info("餐厅更新成功: restaurantId={}", id);
        return ApiResponse.success(updated);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        restaurantService.deleteById(id);
        log.info("餐厅删除: restaurantId={}", id);
        return ApiResponse.success(null);
    }
}
