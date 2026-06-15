package com.waimai.controller;

import com.waimai.dto.ApiResponse;
import com.waimai.entity.Category;
import com.waimai.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ApiResponse<Category> create(@RequestBody Category category) {
        return ApiResponse.success(categoryService.create(category));
    }

    @GetMapping("/restaurant/{restaurantId}")
    public ApiResponse<List<Category>> listByRestaurant(@PathVariable Long restaurantId) {
        return ApiResponse.success(categoryService.findByRestaurantId(restaurantId));
    }

    @PutMapping("/{id}")
    public ApiResponse<Category> update(@PathVariable Long id, @RequestBody Category category) {
        category.setId(id);
        return ApiResponse.success(categoryService.update(category));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        categoryService.deleteById(id);
        return ApiResponse.success(null);
    }
}
