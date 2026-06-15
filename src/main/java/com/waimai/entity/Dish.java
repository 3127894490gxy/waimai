package com.waimai.entity;

import com.waimai.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "dish")
public class Dish extends BaseEntity {

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 500)
    private String description;

    @Column(length = 500)
    private String image;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private Long categoryId;

    @Column(nullable = false)
    private Long restaurantId;

    @Column(nullable = false)
    private Integer stock = 999;

    @Column(nullable = false)
    private Boolean active = true;
}
