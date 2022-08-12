package com.forrestgof.jobscanner.auth.exception;

public class InvalidTokenException extends RuntimeException {

	public InvalidTokenException() {
		super("Failed to generate Token.");
	}

	public InvalidTokenException(String message) {
		super(message);
	}
}
