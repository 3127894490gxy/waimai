package com.waimai.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderRequest {
    private Long userId;
    private Long restaurantId;
    private BigDecimal deliveryFee;
    private String deliveryAddress;
    private String contactPhone;
    private String contactName;
    private String remark;
    private List<OrderItemRequest> items;

    @Data
    public static class OrderItemRequest {
        private Long dishId;
        private String dishName;
        private Integer quantity;
        private BigDecimal price;
    }
}
