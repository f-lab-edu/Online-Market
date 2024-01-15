package com.example.onlinemarket.domain.order.eunm;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum OrderStatus {
    
    READY(" 준비중"),

    SHIPPED("배송중"),

    DELIVERED("배송 완료");

    private final String description;
}
