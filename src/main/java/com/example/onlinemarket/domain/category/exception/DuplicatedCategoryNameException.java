package com.example.onlinemarket.domain.category.exception;

public class DuplicatedCategoryNameException extends RuntimeException {

    public DuplicatedCategoryNameException(String message) {
        super(message);
    }
}
