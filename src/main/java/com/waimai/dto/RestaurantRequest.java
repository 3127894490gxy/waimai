package com.waimai.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class RestaurantRequest {

    @NotBlank(message = "餐厅名称不能为空")
    private String name;

    private String description;

    private String address;

    private String phone;

    private String image;

    private BigDecimal deliveryFee = BigDecimal.ZERO;

    @NotNull(message = "商家用户ID不能为空")
    private Long merchantId;
}
