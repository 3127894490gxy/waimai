package com.waimai.entity;

import com.waimai.common.BaseEntity;
import com.waimai.common.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order extends BaseEntity {

    @Column(nullable = false, unique = true, length = 32)
    private String orderNo;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long restaurantId;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(precision = 10, scale = 2)
    private BigDecimal deliveryFee = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OrderStatus status = OrderStatus.PENDING_PAYMENT;

    @Column(length = 500)
    private String deliveryAddress;

    @Column(length = 50)
    private String contactPhone;

    @Column(length = 100)
    private String contactName;

    @Column(length = 500)
    private String remark;

    /** 配送员ID */
    private Long deliveryId;

    /** 配送员姓名 */
    @Column(length = 50)
    private String deliveryName;

    /** 配送员接单时间 */
    private LocalDateTime pickupTime;

    /** 配送完成时间 */
    private LocalDateTime deliveredTime;

    @Column(nullable = false)
    private Boolean deleted = false;
}
