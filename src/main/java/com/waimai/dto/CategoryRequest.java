package com.waimai.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CategoryRequest {

    @NotBlank(message = "分类名称不能为空")
    private String name;

    @NotNull(message = "餐厅ID不能为空")
    private Long restaurantId;

    private Integer sortOrder = 0;
}
