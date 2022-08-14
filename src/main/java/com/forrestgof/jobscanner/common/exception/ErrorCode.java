package com.forrestgof.jobscanner.common.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

	// COMMON
	INVALID_CODE(HttpStatus.valueOf(400), "Invalid Code"),
	RESOURCE_NOT_FOUND(HttpStatus.valueOf(204), "Resource not found"),
	EXPIRED_CODE(HttpStatus.valueOf(400), "Expired Code"),

	// MEMBER
	NOT_FOUND_MEMBER(HttpStatus.UNAUTHORIZED, "Unsigned account"),
	ALREADY_EXIST_MEMBER(HttpStatus.CONFLICT, "Already joined member");

	private final HttpStatus status;
	private final String message;
}
