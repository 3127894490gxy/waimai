package com.waimai.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class DishRequest {

    @NotBlank(message = "菜品名称不能为空")
    private String name;

    private String description;

    private String image;

    @NotNull(message = "价格不能为空")
    @Positive(message = "价格必须大于0")
    private BigDecimal price;

    @NotNull(message = "分类ID不能为空")
    private Long categoryId;

    @NotNull(message = "餐厅ID不能为空")
    private Long restaurantId;

    private Integer stock = 999;

    private Boolean active = true;
}
