package com.example.onlinemarket.common.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = BAD_REQUEST)
public class ValidationException extends RuntimeException {

	public ValidationException() {
		super("유효하지 않은 값입니다.");
	}
}
