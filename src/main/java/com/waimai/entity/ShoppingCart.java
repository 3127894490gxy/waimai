package com.waimai.entity;

import com.waimai.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "shopping_cart")
public class ShoppingCart extends BaseEntity {
    //用户ID
    @Column(nullable = false)
    private Long userId;
    //菜品ID
    @Column(nullable = false)
    private Long dishId;
    //餐厅ID
    @Column(nullable = false)
    private Long restaurantId;
    //菜名快照
    @Column(length = 100)
    private String dishName;
    //菜价快照
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal dishPrice;
    //数量
    @Column(nullable = false)
    private Integer quantity;
}
