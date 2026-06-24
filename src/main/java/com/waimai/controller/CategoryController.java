package com.waimai.controller;

import com.waimai.dto.ApiResponse;
import com.waimai.dto.CategoryRequest;
import com.waimai.entity.Category;
import com.waimai.service.CategoryService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private static final Logger log = LoggerFactory.getLogger(CategoryController.class);

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ApiResponse<Category> create(@Valid @RequestBody CategoryRequest request) {
        Category category = new Category();
        category.setName(request.getName());
        category.setRestaurantId(request.getRestaurantId());
        category.setSortOrder(request.getSortOrder() != null ? request.getSortOrder() : 0);
        Category saved = categoryService.create(category);
        log.info("分类创建成功: name={}, restaurantId={}", saved.getName(), request.getRestaurantId());
        return ApiResponse.success(saved);
    }

    @GetMapping("/restaurant/{restaurantId}")
    public ApiResponse<List<Category>> listByRestaurant(@PathVariable Long restaurantId) {
        return ApiResponse.success(categoryService.findByRestaurantId(restaurantId));
    }

    @PutMapping("/{id}")
    public ApiResponse<Category> update(@PathVariable Long id, @Valid @RequestBody CategoryRequest request) {
        Category category = categoryService.findById(id).orElse(null);
        if (category == null) {
            return ApiResponse.error(404, "分类不存在");
        }
        category.setName(request.getName());
        category.setRestaurantId(request.getRestaurantId());
        category.setSortOrder(request.getSortOrder() != null ? request.getSortOrder() : 0);
        category.setId(id);
        Category updated = categoryService.update(category);
        log.info("分类更新成功: categoryId={}", id);
        return ApiResponse.success(updated);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        categoryService.deleteById(id);
        log.info("分类删除: categoryId={}", id);
        return ApiResponse.success(null);
    }
}
