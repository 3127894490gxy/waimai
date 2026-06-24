package com.waimai.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderRequest {

    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @NotNull(message = "餐厅ID不能为空")
    private Long restaurantId;

    private BigDecimal deliveryFee;

    @NotBlank(message = "配送地址不能为空")
    private String deliveryAddress;

    @NotBlank(message = "联系电话不能为空")
    private String contactPhone;

    @NotBlank(message = "联系人不能为空")
    private String contactName;

    private String remark;

    @Valid
    @NotEmpty(message = "订单项不能为空")
    private List<OrderItemRequest> items;

    @Data
    public static class OrderItemRequest {

        @NotNull(message = "菜品ID不能为空")
        private Long dishId;

        @NotBlank(message = "菜品名称不能为空")
        private String dishName;

        @NotNull(message = "数量不能为空")
        @Positive(message = "数量必须大于0")
        private Integer quantity;

        @NotNull(message = "价格不能为空")
        private BigDecimal price;
    }
}
