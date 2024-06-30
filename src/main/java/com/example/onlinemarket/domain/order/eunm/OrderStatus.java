package com.example.onlinemarket.domain.order.eunm;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum OrderStatus {

    READY(" 준비중"),

    SHIPPED("주문 준비중"),

    DELIVERED("주문 완료");

    private final String description;
}
