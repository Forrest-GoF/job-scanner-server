package com.forrestgof.jobscanner.common.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.forrestgof.jobscanner.common.util.CustomResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(CustomException.class)
	public ResponseEntity customExceptionHandle(CustomException e) {
		return CustomResponse.error(e.getErrorCode());
	}

	@ExceptionHandler
	public ResponseEntity unexpectedExceptionHandle(Exception e) {
		return CustomResponse.error(ErrorCode.UNEXPECTED_EXCEPTION);
	}
}
