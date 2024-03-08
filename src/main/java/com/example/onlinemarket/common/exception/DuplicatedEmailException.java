package com.example.onlinemarket.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicatedEmailException extends RuntimeException {

    public DuplicatedEmailException(String message) {
        super(message);
    }

    public DuplicatedEmailException() {
        super("이미 존재하는 이메일입니다.");
    }
}
