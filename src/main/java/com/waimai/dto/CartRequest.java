package com.waimai.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartRequest {

    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @NotNull(message = "菜品ID不能为空")
    private Long dishId;

    @NotNull(message = "餐厅ID不能为空")
    private Long restaurantId;

    private String dishName;

    private BigDecimal dishPrice;

    @NotNull(message = "数量不能为空")
    @Positive(message = "数量必须大于0")
    private Integer quantity;
}
