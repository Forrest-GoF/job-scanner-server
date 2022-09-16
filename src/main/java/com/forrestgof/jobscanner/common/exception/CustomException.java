package com.forrestgof.jobscanner.common.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

	private final HttpStatus httpStatus;

	public CustomException() {
		super();
		this.httpStatus = HttpStatus.BAD_REQUEST;
	}

	public CustomException(String message) {
		super(message);
		this.httpStatus = HttpStatus.BAD_REQUEST;
	}

	public CustomException(String message, HttpStatus httpStatus) {
		super(message);
		this.httpStatus = httpStatus;
	}
}
