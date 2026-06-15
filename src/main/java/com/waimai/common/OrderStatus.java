package com.waimai.common;

public enum OrderStatus {
    PENDING_PAYMENT,    // 待支付
    PAID,               // 已支付/待接单
    ACCEPTED,           // 商家已接单
    PREPARING,          // 准备中
    DELIVERING,         // 配送中
    DELIVERED,          // 已送达
    COMPLETED,          // 已完成
    CANCELLED           // 已取消
}
