package com.forrestgof.jobscanner.common.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.forrestgof.jobscanner.common.dto.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(CustomException.class)
	public ResponseEntity<ApiResponse> customExceptionHandle(CustomException e) {
		return ApiResponse.toResponseEntity(e.getErrorCode());
	}

	@ExceptionHandler
	public ResponseEntity<ApiResponse> unexpectedExceptionHandle(Exception e) {
		CustomException customException = new CustomException(e.getMessage(), ErrorCode.UNEXPECTED_EXCEPTION);
		return ApiResponse.toResponseEntity(customException.getErrorCode());
	}
}
