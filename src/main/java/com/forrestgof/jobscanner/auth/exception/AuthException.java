package com.forrestgof.jobscanner.auth.exception;

import lombok.Getter;

@Getter
public class AuthException extends RuntimeException {

	public AuthException() {
		super();
	}

	public AuthException(String message) {
		super(message);
	}
}
