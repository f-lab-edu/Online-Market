package com.example.onlinemarket.domain.order.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class OrderDetail {

    private Long id;
    private Long orderId;
    private Long productId;
    private String productName;
    private Long productPrice;
    private Long productQuantity;


    public void setOrderId(Long orderId) {
        this.orderId = orderId;

    }
}
