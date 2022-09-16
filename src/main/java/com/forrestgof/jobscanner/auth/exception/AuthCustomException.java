package com.forrestgof.jobscanner.auth.exception;

import org.springframework.http.HttpStatus;

import com.forrestgof.jobscanner.common.exception.CustomException;

public class AuthCustomException extends CustomException {

	public AuthCustomException() {
	}

	public AuthCustomException(String message) {
		super(message);
	}

	public AuthCustomException(String message, HttpStatus httpStatus) {
		super(message, httpStatus);
	}
}
