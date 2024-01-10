package com.example.onlinemarket.common.exception;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = NOT_FOUND)
public class NotFoundException extends RuntimeException {

	public NotFoundException() {
		super("요청한 값을 찾을 수 없습니다.");
	}
}
