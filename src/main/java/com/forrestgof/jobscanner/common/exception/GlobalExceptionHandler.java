package com.forrestgof.jobscanner.common.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.forrestgof.jobscanner.common.util.CustomResponse;

import lombok.extern.log4j.Log4j2;

@RestControllerAdvice
@Log4j2
public class GlobalExceptionHandler {

	@ExceptionHandler
	public ResponseEntity<CustomResponse> handleUnExpectedException(Exception e) {
		log.error("UnExpected Exception", e);
		return CustomResponse.error(e);
	}

	@ExceptionHandler
	public ResponseEntity<CustomResponse> handleCustomException(CustomException e) {
		log.error("Custom Exception", e);
		return CustomResponse.customError(e);
	}
}
