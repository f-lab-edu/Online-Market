package com.example.onlinemarket.common.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = BAD_REQUEST)
public class InvalidPasswordException extends RuntimeException {

    public InvalidPasswordException() {
        super("유효하지 않은 비밀번호입니다.");
    }
}