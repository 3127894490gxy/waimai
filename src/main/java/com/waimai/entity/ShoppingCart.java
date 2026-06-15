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

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long dishId;

    @Column(nullable = false)
    private Long restaurantId;

    @Column(length = 100)
    private String dishName;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal dishPrice;

    @Column(nullable = false)
    private Integer quantity;
}
