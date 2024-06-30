package com.example.onlinemarket.domain.category.exception;

public class NotFoundCategoryException extends RuntimeException {

    public NotFoundCategoryException(String message) {
        super(message);
    }
}
