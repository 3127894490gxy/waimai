package com.waimai.dto;

import lombok.Data;

@Data
public class PaymentRequest {
    private Long orderId;
    private String paymentMethod; // ALIPAY, WECHAT, CARD
}
