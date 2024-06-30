package com.example.onlinemarket.domain.order.exception;

public class OrderQuantityExceedsStockException extends RuntimeException {

    public OrderQuantityExceedsStockException(String message) {
        super(message);
    }
}
