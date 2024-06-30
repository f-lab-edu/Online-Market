package com.example.onlinemarket.domain.user.exception;

public class NotFoundUserIdException extends RuntimeException {

    public NotFoundUserIdException(String message) {
        super(message);
    }
}
