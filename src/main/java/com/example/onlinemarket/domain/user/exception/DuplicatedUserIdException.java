package com.example.onlinemarket.domain.user.exception;

public class DuplicatedUserIdException extends RuntimeException {

    public DuplicatedUserIdException(String message) {
        super(message);
    }
}
