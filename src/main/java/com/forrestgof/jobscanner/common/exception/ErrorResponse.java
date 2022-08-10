package com.forrestgof.jobscanner.common.exception;

import org.springframework.http.HttpStatus;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ErrorResponse {

	private String message;
	private HttpStatus httpStatus;

	public ErrorResponse(ErrorCode errorCode) {
		this.message = errorCode.getMessage();
		this.httpStatus = errorCode.getHttpStatus();
	}

	public static ErrorResponse of(ErrorCode code) {
		return new ErrorResponse(code);
	}
}
