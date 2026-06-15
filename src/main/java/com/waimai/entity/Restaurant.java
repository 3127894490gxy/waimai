package com.waimai.entity;

import com.waimai.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "restaurant")
public class Restaurant extends BaseEntity {

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 500)
    private String description;

    @Column(length = 500)
    private String address;

    @Column(length = 20)
    private String phone;

    @Column(length = 500)
    private String image;

    @Column(precision = 10, scale = 2)
    private BigDecimal deliveryFee = BigDecimal.ZERO;

    @Column(nullable = false, precision = 3, scale = 1)
    private BigDecimal rating = BigDecimal.valueOf(5.0);

    @Column(nullable = false)
    private Long merchantId;  // 关联商家用户 ID

    @Column(nullable = false)
    private Boolean active = true;
}
