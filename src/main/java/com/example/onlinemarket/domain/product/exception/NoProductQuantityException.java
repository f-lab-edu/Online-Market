package com.example.onlinemarket.domain.product.exception;

public class NoProductQuantityException extends RuntimeException {

    public NoProductQuantityException(String message) {
        super(message);
    }
}
