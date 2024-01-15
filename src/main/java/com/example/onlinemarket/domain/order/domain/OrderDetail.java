package com.example.onlinemarket.domain.order.domain;

import com.example.onlinemarket.domain.order.eunm.OrderStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderDetail {

    private Long id;
    private Long orderId;
    private Long productId;
    private String productName;
    private double productPrice;
    private long productQuantity;
    private OrderStatus status;

    @Builder
    public OrderDetail(Long orderId, Long productId, String productName, double productPrice, long productQuantity) {

        this.orderId = orderId;
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productQuantity = productQuantity;
    }
}
