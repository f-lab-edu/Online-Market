package com.example.onlinemarket.domain.product.exception;

public class NotFoundProductException extends RuntimeException {

    public NotFoundProductException(String message) {
        super(message);
    }
}
